package com.elearnna.www.wififingerprint.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import com.elearnna.www.wififingerprint.adapter.APsAdapter;
import com.elearnna.www.wififingerprint.app.RSSIRepresenter;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.presenter.APsListPresenter;
import com.elearnna.www.wififingerprint.presenter.APsListPresenterImplementer;
import com.elearnna.www.wififingerprint.view.APsListView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class APsListFragment extends Fragment implements APsListView{

    private static final String RECYCLER_STATE = "recycler.state";
    APsListPresenter aPsListPresenter;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    Bundle state;
    Handler handler;
    Runnable runnable;

    public APsListFragment() {
    }

    @BindView(R.id.wifi_image)
    ImageView wifiImage;

    @BindView(R.id.tv_ssid)
    TextView txtSSID;

    @BindView(R.id.tv_ip_address)
    TextView txtIPAddress;

    @BindView(R.id.tv_connection_status)
    TextView txtConnectionStatus;

    @BindView(R.id.tv_channel)
    TextView txtChennel;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void readConnectedAPPeriodically() {
        // Create handler that gets connected AP info every 5 seconds
        handler = new Handler();
        runnable = new Runnable(){
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void run() {
                getConnectedAPInfo(5000);
            }
        };
        runnable.run();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_aps_list, container, false);
        ButterKnife.bind(this, view);
        state = new Bundle();
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
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getConnectedAPInfo(int timer) {
        if (getActivity() != null) {
            wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            wifiInfo = wifiManager.getConnectionInfo();
            if (wifiInfo.getNetworkId() != -1) {
                txtWifiNotConnected.setVisibility(View.GONE);
                int rssi = wifiInfo.getRssi();
                txtSSID.setText(wifiInfo.getSSID());
                int ip = wifiInfo.getIpAddress();
                String ipAddress = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
                txtIPAddress.setText("IP: " + ipAddress);
                txtChennel.setText("Channel: " + Utils.convertFrequencyToChannel(wifiInfo.getFrequency()));
                txtMAC.setText("MAC: " + Utils.getMacAddr());

                RSSIRepresenter rssiRepresenter = Utils.setWifiImage(rssi, getContext());
                wifiImage.setImageDrawable(ContextCompat.getDrawable(getContext(), rssiRepresenter.getRSSIImage()));
                roundCornerProgressBar.setProgress((120 + (rssi)));
                roundCornerProgressBar.setProgressColor(rssiRepresenter.getRSSIStrength());
                txtSignalStrength.setText(String.valueOf(rssi));
                handler.postDelayed(runnable, timer);
                apItemLayout.setVisibility(View.VISIBLE);
            } else {
                apItemLayout.setVisibility(View.GONE);
                txtWifiNotConnected.setVisibility(View.VISIBLE);
            }
        }
    }

    private void requestUserPermission() {
        int myPermission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION);
        if(!(myPermission == PackageManager.PERMISSION_GRANTED)){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {

            } else {
                // do request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        10);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                }
                return;
            }
        }
    }

    @Override
    public void displayAPsList(List<AP> APsList) {
        // Restore the RecyclerView scroll position every time a new reading is displayed
        if (APsList != null) {
            apsLoadingProgressBar.setVisibility(View.GONE);
            onSaveInstanceState(state);
            rvAPsList.setAdapter(new APsAdapter(APsList, getContext()));
            rvAPsList.getAdapter().notifyDataSetChanged();
            onViewStateRestored(state);
        }
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

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_STATE, rvAPsList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null)
        {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(RECYCLER_STATE);
            rvAPsList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }
}
