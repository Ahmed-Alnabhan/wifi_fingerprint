package com.elearnna.www.wififingerprint.dialog;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileInfoDialogFragment extends DialogFragment {private int duration;

    private long currentTick;
    private String[] spinnerFileTypeItems;

    @BindView(R.id.spinner_file_type_value)
    Spinner spinnerFileTypes;

    @BindView(R.id.et_file_name_value)
    EditText etFileName;

    @BindView(R.id.txt_default_file_location_value)
    TextView txtDefaultFileLocation;

    @BindView(R.id.btn_browse)
    Button btnBrowse;


    @BindView(R.id.txt_countdown_timer)
    TextView txtCountDownTimer;

    @BindView(R.id.btn_file_cancel)
    Button btnCancel;

    @BindView(R.id.btn_save_file)
    Button btnSave;

    private IntentFilter intentFilter;
    private WifiManager wifiManager;
    private AP ap;
    private WiFiBroadcastReceiver wiFiBroadcastReceiver;
    private List<ScanResult> wifiAPsList;
    private String location;
    private Context mContext;

    public FileInfoDialogFragment() {
    }

    public static FileInfoDialogFragment newInstance(String title, int timer, String location) {
        FileInfoDialogFragment frag = new FileInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("timer", timer);
        args.putString("location", location);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        // Set a title to the dialog
        getDialog().setTitle(title);

        // Prevent closing the dialog when a user touches outside the dialog
        getDialog().setCanceledOnTouchOutside(false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        View view = inflater.inflate(R.layout.file_properties_layout, container);
        ButterKnife.bind(this, view);

        // Get the current location
        location = getArguments().getString("location");

        // Set Spinner items
        setSpinnerItems();

        disableControls();
        if (savedInstanceState == null) {
            duration = getArguments().getInt("timer");
        } else {
            duration = (int)savedInstanceState.getLong("currentTick");
        }
        startTimer();

        // Set OnClickListener for the cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Set OnClickListener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                showFileSavingResultDialog();
            }
        });
        return view;
    }

    private void startTimer() {
        txtCountDownTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long l) {
                currentTick = l / 1000;
                txtCountDownTimer.setText(String.valueOf(currentTick));
                readAPInfo();
            }

            @Override
            public void onFinish() {
                txtCountDownTimer.setVisibility(View.GONE);
                enableControls();
                readAPInfoFromDB(location);
            }
        }.start();

    }

    private void disableControls() {
        spinnerFileTypes.setEnabled(false);
        etFileName.setEnabled(false);
        txtDefaultFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnBrowse.setClickable(false);
        btnSave.setEnabled(false);
        btnSave.setClickable(false);
    }

    private void enableControls() {
        spinnerFileTypes.setEnabled(true);
        etFileName.setEnabled(true);
        txtDefaultFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnBrowse.setClickable(true);
        btnSave.setEnabled(true);
        btnSave.setClickable(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentTick",currentTick);
    }

    /**
     * Fill out the spinner with the file types
     */
    private void setSpinnerItems(){
        spinnerFileTypeItems = new String[]{"JSON", "XML", "CSV"};
        String defaultSpinnerValue = spinnerFileTypeItems[0];

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerFileTypeItems);
        int spinnerPosition = adapter.getPosition(defaultSpinnerValue);
        spinnerFileTypes.setSelection(spinnerPosition);
        spinnerFileTypes.setAdapter(adapter);
    }
    private void showFileSavingResultDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FileStoringResultDF fileInfoDialogFragment = FileStoringResultDF.newInstance("File Saving Result");
        fileInfoDialogFragment.show(fm, "saving result");
    }

    private void readAPInfo(){
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiManager = (WifiManager) getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wiFiBroadcastReceiver = new WiFiBroadcastReceiver();
        getContext().getApplicationContext().registerReceiver(wiFiBroadcastReceiver, intentFilter);
        wifiManager.startScan();
    }

    private class WiFiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            wifiAPsList = wifiManager.getScanResults();
            for(ScanResult sr : wifiAPsList){
                ap = new AP();
                ap.setSsid(sr.SSID);
                ap.setLocation(location);
                ap.setChannel(Utils.convertFrequencyToChannel(sr.frequency));
                ap.setFrequency(sr.frequency);
                ap.setMacAddress(sr.BSSID);
                ap.setRssi(sr.level);
                ap.setSecurityProtocol(sr.capabilities);
                if(sr.capabilities != null){
                    ap.setLocked(1);
                } else {
                    ap.setLocked(0);
                }
                ap.setTime(sr.timestamp);
                writeAPInfoToDB(ap);
            }
        }
    }

    private void writeAPInfoToDB(AP ap) {
        // Create a new map of values
        ContentValues apValues = new ContentValues();
        apValues.put(APContentProvider.location, ap.getLocation());
        apValues.put(APContentProvider.rssi, ap.getRssi());
        apValues.put(APContentProvider.ssid, ap.getSsid());
        apValues.put(APContentProvider.channel, ap.getChannel());
        apValues.put(APContentProvider.frequency, ap.getFrequency());
        apValues.put(APContentProvider.ipAddress, "");
        apValues.put(APContentProvider.isLocked, ap.isLocked());
        apValues.put(APContentProvider.securityProtocol, ap.getSecurityProtocol());
        apValues.put(APContentProvider.apManufacturer, ap.getManufacturer());
        apValues.put(APContentProvider.macAddress, ap.getMacAddress());
        apValues.put(APContentProvider.time, ap.getTime());
        Uri uri = mContext.getContentResolver().insert(Constants.APS_CONTENT_URL, apValues);
    }

    private void readAPInfoFromDB(String location){
        String selection = APContentProvider.location + " LIKE ?";
        String[] selectionArgs = {location};
        Cursor cursor = mContext.getContentResolver().query(Constants.APS_CONTENT_URL, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            try {
                while (cursor.moveToNext()) {
                    Log.i("APS_RECORDS", cursor.getString(cursor.getColumnIndex("mac_address")));
                }
            } finally {
                cursor.close();
            }
        }
    }
}
