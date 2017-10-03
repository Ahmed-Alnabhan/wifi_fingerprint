package com.elearnna.www.wififingerprint.dialog;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.elearnna.www.wififingerprint.loader.FingerprintLoader;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.model.Fingerprint;
import com.elearnna.www.wififingerprint.provider.APContentProvider;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileInfoDialogFragment extends DialogFragment{

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


    private int duration;
    private long currentTick;
    private String[] spinnerFileTypeItems;
    private Fingerprint fingerprint;
    private LoaderManager.LoaderCallbacks<Fingerprint> mLoader;

    private IntentFilter intentFilter;
    private WifiManager wifiManager;
    private AP ap;
    private WiFiBroadcastReceiver wiFiBroadcastReceiver;
    private List<ScanResult> wifiAPsList;
    private String location;
    private Context mContext;
    private String selectedFileType;
    private String fileName;
    private String fullFileName;
    private DateFormat dateFormat;

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

        // Set Date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        // disable the save button
        disableSaveButton();

        // disable the save button unless the file name edit text is not empty
        etFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    enableSaveButton();
                } else {
                    disableSaveButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Get the current location
        location = getArguments().getString("location");

        // Define the loader
        mLoader = new LoaderManager.LoaderCallbacks<Fingerprint>() {
            @Override
            public Loader<Fingerprint> onCreateLoader(int id, Bundle args) {
                fingerprint = new Fingerprint();
                return new FingerprintLoader(getActivity(), location);
            }

            @Override
            public void onLoadFinished(Loader<Fingerprint> loader, Fingerprint data) {
                fingerprint = data;
            }

            @Override
            public void onLoaderReset(Loader<Fingerprint> loader) {

            }
        };

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
                fileName = etFileName.getText().toString();
                dismiss();
                boolean fileCreatedSuccessfully = createFile(fingerprint);
                showFileSavingResultDialog(fileCreatedSuccessfully);

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
                getLoaderManager().initLoader(1, null, mLoader).forceLoad();
            }
        }.start();

    }

    private void disableControls() {
        spinnerFileTypes.setEnabled(false);
        etFileName.setEnabled(false);
        txtDefaultFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnBrowse.setClickable(false);
        disableSaveButton();
    }

    private void enableControls() {
        spinnerFileTypes.setEnabled(true);
        etFileName.setEnabled(true);
        txtDefaultFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnBrowse.setClickable(true);
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
        spinnerFileTypeItems = new String[]{Constants.JSON_TYPE, Constants.XML_TYPE, Constants.CSV_TYPE};
        String defaultSpinnerValue = spinnerFileTypeItems[0];

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerFileTypeItems);
        int spinnerPosition = adapter.getPosition(defaultSpinnerValue);
        spinnerFileTypes.setSelection(spinnerPosition);
        spinnerFileTypes.setAdapter(adapter);
    }
    private void showFileSavingResultDialog(boolean isFileCreatedSuccessfully) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FileStoringResultDF fileInfoDialogFragment = FileStoringResultDF.newInstance("File Saving Result", isFileCreatedSuccessfully, fullFileName);
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
                Date currentDate = Calendar.getInstance().getTime();
                String formattedDate = dateFormat.format(currentDate);
                ap.setTime(formattedDate);
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
        Cursor cursor = getActivity().getContentResolver().query(Constants.APS_CONTENT_URL, null, selection, selectionArgs, null);
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

    private boolean createFile(Fingerprint fp){
        selectedFileType = spinnerFileTypes.getSelectedItem().toString();
        boolean fileCreated = false;
        if (selectedFileType.equals(Constants.JSON_TYPE)){
            fileCreated = createJSONFile(fp);
        } else if (selectedFileType.equals(Constants.XML_TYPE)){
            fileCreated = createXMLFile(fp);
        } else if (selectedFileType.equals(Constants.CSV_TYPE)){
            fileCreated = createCSVFile(fp);
        }
        return fileCreated;
    }
    
    private boolean createJSONFile(Fingerprint fp){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filePath = sharedPreferences.getString(Constants.DEFAULT_DIRECTORY_PATH, Constants.DEFAULT_DIRECTORY_PATH);
        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .serializeNulls()
                .create();
        if (fp != null) {
            String jsonString = gson.toJson(fp);
            File fullFileName = new File(filePath + "/" + fileName + ".json");
            Writer fileWriter = null;
            try {
                fileWriter = new FileWriter(fullFileName, true);
                fileWriter.write(jsonString);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.i("FingerPrint JSON: ", jsonString);
            return true;
        } else {
            return false;
        }
    }

    private boolean createXMLFile(Fingerprint fp){
        return true;
    }

    private boolean createCSVFile(Fingerprint fp){
        return true;
    }

    private void disableSaveButton() {
        btnSave.setEnabled(false);
        btnSave.setClickable(false);
    }

    private void enableSaveButton() {
        btnSave.setEnabled(true);
        btnSave.setClickable(true);
    }
}
