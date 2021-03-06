package com.elearnna.www.wififingerprint.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.activities.APDetail;
import com.elearnna.www.wififingerprint.adapter.APsAdapter;
import com.elearnna.www.wififingerprint.adapter.APsAdapterOnClickHandler;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.RSSIRepresenter;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.dialog.LocationDialog;
import com.elearnna.www.wififingerprint.dialog.LocationDuration;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.model.Locator;
import com.elearnna.www.wififingerprint.presenter.APsListPresenter;
import com.elearnna.www.wififingerprint.presenter.APsListPresenterImplementer;
import com.elearnna.www.wififingerprint.provider.APContentProvider;
import com.elearnna.www.wififingerprint.view.APsListView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class APsListFragment extends Fragment implements APsListView, APsAdapterOnClickHandler, LocationDuration, SharedPreferences.OnSharedPreferenceChangeListener{

    @BindView(R.id.wifi_image)
    ImageView wifiImage;

    @BindView(R.id.tv_ssid)
    TextView txtSSID;

    @BindView(R.id.tv_ip_address)
    TextView txtIPAddress;

    @BindView(R.id.tv_connection_status)
    TextView txtConnectionStatus;

    @BindView(R.id.tv_channel)
    TextView txtChannel;

    @BindView(R.id.tv_mac_address)
    TextView txtMAC;

    @BindView(R.id.roundCornerProgressBar)
    RoundCornerProgressBar roundCornerProgressBar;

    @BindView(R.id.tv_signal_strength)
    TextView txtSignalStrength;

    @BindView(R.id.aps_loading_progress_bar)
    ProgressBar apsLoadingProgressBar;

    @BindView(R.id.rv_aps_list)
    RecyclerView rvAPsList;

    @BindView(R.id.txt_wifi_not_connected)
    TextView txtWifiNotConnected;

    @BindView(R.id.ap_item_layout)
    FrameLayout apItemLayout;

    @BindView(R.id.adView)
    AdView adView;

    private APsListPresenter aPsListPresenter;
    private WifiManager wifiManager;
    private WifiInfo wifiInfo;
    private Bundle state, bundle;
    private Handler handler;
    private Runnable runnable;
    private boolean mTwoPane;
    private FragmentManager fragmentManager;
    private Context context;
    private Intent intent;
    private String band;
    private String sort;


    public APsListFragment() {
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aps_list, container, false);
        ButterKnife.bind(this, view);

        // Write device info to the database once
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        band = sharedPreferences.getString(getActivity().getResources().getString(R.string.pref_band_key), getActivity().getResources().getString(R.string.band_default));
        sort = sharedPreferences.getString(getActivity().getResources().getString(R.string.pref_sort_key), getActivity().getResources().getString(R.string.aps_sort_default));
        if(!sharedPreferences.getBoolean(Constants.EXECUTED_ONCE, false)){
            readDeviceInfoOnce();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(Constants.EXECUTED_ONCE, true);
            editor.apply();
        }

        // Register onSharedPreferenceChangeListener
        PreferenceManager.getDefaultSharedPreferences(getContext()).registerOnSharedPreferenceChangeListener(this);

        // Create default wifi fingerprint directory path
        createDefaultDirectoryPath();

        state = new Bundle();
        bundle = new Bundle();
        mTwoPane = false;
        setTextViewsStyle();


        // request the user permission for location access
        requestUserPermission();
        // Retrieve the info of the currently connected APs
        rvAPsList.setLayoutManager(new LinearLayoutManager(getContext()));
        aPsListPresenter = new APsListPresenterImplementer(getContext());
        aPsListPresenter.setAPsListView(this);
        aPsListPresenter.getAPsList();
        // Keep the screen on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        readConnectedAPPeriodically();

        // FAB

        FloatingActionButton fab = view.findViewById(R.id.fab);

        // Set fab elevation
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLocationDialog();
            }
        });

        // Set content description of used drawables
        fab.setContentDescription(getResources().getString(R.string.fab_description));

        // AdView stuff// Initialize the admob
        MobileAds.initialize(getActivity(), Constants.AP_ID_MOBILE_AD);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        adView.loadAd(adRequest);
        return view;
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getConnectedAPInfo(int timer) {
        if (getActivity() != null) {
            wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getNetworkId() != -1) {
                int rssi = wifiInfo.getRssi();

                // Set the name of the network SSID
                String ssid = wifiInfo.getSSID();
                if (!ssid.isEmpty() && ssid != null) {
                    txtSSID.setText(ssid);
                } else {
                    txtSSID.setText(Constants.UNKNOWN);
                }

                int ip = wifiInfo.getIpAddress();
                String ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                txtIPAddress.setText(getContext().getResources().getString(R.string.ip_label) + ipAddress);
                if (Build.VERSION.SDK_INT >= 21) {
                    txtChannel.setText(getContext().getResources().getString(R.string.channel_label) + Utils.convertFrequencyToChannel(wifiInfo.getFrequency()));
                } else {
                    txtChannel.setText(getContext().getResources().getString(R.string.channel_label) + Constants.NOT_APPLICABLE);
                }
                txtMAC.setText(getContext().getResources().getString(R.string.mac_label) + Utils.getMacAddr());

                RSSIRepresenter rssiRepresenter = Utils.setWifiImage(rssi, getContext());
                wifiImage.setImageResource(rssiRepresenter.getRSSIImage());
                roundCornerProgressBar.setProgress((120 + (rssi)));
                roundCornerProgressBar.setProgressColor(rssiRepresenter.getRSSIStrength());
                txtSignalStrength.setText(String.valueOf(rssi));
                handler.postDelayed(runnable, timer);
                txtWifiNotConnected.setVisibility(View.GONE);
                apItemLayout.setVisibility(View.VISIBLE);
            } else if (wifiInfo.getNetworkId() == -1){
                apItemLayout.setVisibility(View.GONE);
                txtWifiNotConnected.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestUserPermission() {
        int locationPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(!(locationPermission == PackageManager.PERMISSION_GRANTED)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                // do request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        10);
            }
        }

        int storagePermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(!(storagePermission == PackageManager.PERMISSION_GRANTED)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            } else {
                // do request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        11);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
            case 11: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }

        }
    }

    @Override
    public void displayAPsList(List<AP> APsList) {
        // Restore the RecyclerView scroll position every time a new reading is displayed
        List<AP> sortedAPsListByBand;
        List<AP> orderedAPsList;
        if (APsList != null) {
            sortedAPsListByBand = applyBandSort(APsList);
            orderedAPsList = applyAPsSort(sortedAPsListByBand);
            hideAPsLoading();
            onSaveInstanceState(state);
            rvAPsList.setAdapter(new APsAdapter(orderedAPsList, getContext(), this));
            rvAPsList.getAdapter().notifyDataSetChanged();
            onViewStateRestored(state);
        }
    }

    private List<AP> applyBandSort(List<AP> aps) {
        List<AP> sortedAPs = new ArrayList<>();
        for(AP ap : aps) {
            if (band.equals(Constants.ALL_BANDS)) {
                sortedAPs.add(ap);
            } else if (band.equals(Constants.BAND_5_GHZ) && (ap.getFrequency() > 5000)) {
                sortedAPs.add(ap);
            } else if (band.equals(Constants.BAND_2_4_GHZ) && (ap.getFrequency()) < 5000) {
                sortedAPs.add(ap);
            }
        }
        return sortedAPs;
    }

    private List<AP> applyAPsSort(List<AP> aps) {
        
        List<AP> sortedAPS = new ArrayList<>();
        // Create Temporary HashMap
        HashMap<String, AP> map =  new HashMap<String, AP>();

        // Add APs to Map using MAC to avoid duplicates
        for (AP ap : aps) {
            if (ap.getMacAddress() != null && !ap.getMacAddress().isEmpty()) {
                map.put(ap.getMacAddress(), ap);
            }
        }

        // Add to new List
        List<AP> sortedWifiList =  new ArrayList<>(map.values());

        Comparator<AP> comparator = new Comparator<AP>() {
            @Override
            public int compare(AP leftSSID, AP rightSSID) {
                return (rightSSID.getSsid().compareTo(leftSSID.getSsid()));
            }
        };
        // Apply Comparator and sort
        Collections.sort(sortedWifiList, comparator);

        if (sort.equals(Constants.RSSI_ASCENDING)) {
            sortedAPS = aps;
        } else if (sort.equals(Constants.ALPHABETICALLY)) {
            sortedAPS =  sortedWifiList;
        }
        return sortedAPS;
    }

    private void readConnectedAPPeriodically() {
        // Create handler that gets connected AP info every 5 seconds
        handler = new Handler();
        runnable = new Runnable(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                getConnectedAPInfo(1000);
            }
        };
        runnable.run();
    }


    @Override
    public void showAPDetail(AP ap) {

    }

    @Override
    public void displayErrorMessage() {

    }

    @Override
    public void showAPsLoading() {

    }

    @Override
    public void hideAPsLoading() {
        apsLoadingProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECYCLER_STATE, rvAPsList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE);
            rvAPsList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onClick(AP ap) {
        bundle.putParcelable(Constants.ACCESS_POINT, ap);
        if (mTwoPane) {
            fragmentManager = getFragmentManager();
            APDetailFragment detailFragment = new APDetailFragment();
            detailFragment.setArguments(bundle);
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_ap_detail_view, detailFragment).commit();
        } else {
            context = getActivity().getApplicationContext();
            intent = new Intent(context, APDetail.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    public void getLocationandDuration(Locator locator) {
    }

    /**
     * Write device info to the database using the content provider
     */
    private void writeDeviceInfoToDB(Device device){
        // Create a new map of values
        ContentValues deviceValues = new ContentValues();
        deviceValues.put(APContentProvider.deviceManufacturer, device.getManufacturer());
        deviceValues.put(APContentProvider.brand, device.getBrand());
        deviceValues.put(APContentProvider.device, device.getDevice());
        deviceValues.put(APContentProvider.model, device.getModel());
        deviceValues.put(APContentProvider.product, device.getProduct());
        deviceValues.put(APContentProvider.os, device.getOs());
        deviceValues.put(APContentProvider.osVersion, device.getOsVersion());
        deviceValues.put(APContentProvider.apiLevel, device.getApiLevel());
        Uri uri = getActivity().getContentResolver().insert(Constants.DEVICE_CONTENT_URL, deviceValues);
    }

    // Read the device info and write them to the database only once
    private synchronized void readDeviceInfoOnce(){
        Device device = Utils.readDeviceInfo(getContext());
        if (device != null) {
            writeDeviceInfoToDB(device);
        }
    }

    // Create Default directory path to save files
    private void createDefaultDirectoryPath() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        File fingerprintDirectory = new File(Environment.getExternalStorageDirectory() + "/Wifi_Fingerprint");

        if (!fingerprintDirectory.exists()){
            fingerprintDirectory.mkdir();
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.DEFAULT_DIRECTORY_PATH, String.valueOf(fingerprintDirectory));
        editor.apply();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(getActivity().getResources().getString(R.string.pref_band_key))) {
            band = sharedPreferences.getString(s, getActivity().getResources().getString(R.string.band_default));
        } else if (s.equals(getActivity().getResources().getString(R.string.pref_sort_key))) {
            sort = sharedPreferences.getString(s, getActivity().getResources().getString(R.string.aps_sort_default));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        PreferenceManager.getDefaultSharedPreferences(getContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setTextViewsStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getActivity());
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(getActivity());

        // Set the style of the SSID based on the app theme
        Utils.setTextViewStyle(getActivity(), txtSSID, bold_font, Constants.LARGE_FONT);

        // Set the style of the IP address based on the app theme
        Utils.setTextViewStyle(getActivity(), txtIPAddress, regular_font, Constants.REGULAR_FONT);

        // Set the style of the Connection status based on the app theme
        Utils.setTextViewStyle(getActivity(), txtConnectionStatus, regular_font, Constants.SMALL_FONT);

        // Set the style of the Channel based on the app theme
        Utils.setTextViewStyle(getActivity(), txtChannel, regular_font, Constants.LARGE_FONT);

        // Set the style of the MAC based on the app theme
        Utils.setTextViewStyle(getActivity(), txtMAC, regular_font, Constants.LARGE_FONT);

        // Set the style of the RSSI based on the app theme
        Utils.setTextViewStyle(getActivity(), txtSignalStrength, bold_font, Constants.LARGE_FONT);

        // Set the style of the wifiNotConnected text view
        Utils.setTextViewStyle(getActivity(), txtWifiNotConnected, bold_font, Constants.LARGE_FONT);
    }

    private void showLocationDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        LocationDialog editLocationDialogFragment = LocationDialog.newInstance("Location-duration Info");
        editLocationDialogFragment.show(fm, "fragment_edit_name");
    }

}
