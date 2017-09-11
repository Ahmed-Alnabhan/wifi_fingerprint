package com.elearnna.www.wififingerprint.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.adapter.APsAdapter;
import com.elearnna.www.wififingerprint.app.FrequenceyToChannel;
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
        state = new Bundle();
        // request the user permission for location access
        requestUserPermission();
        // Retrieve the info of the currently connected APs
        getConnectedAPInfo();
        rvAPsList.setLayoutManager(new LinearLayoutManager(getContext()));
        aPsListPresenter = new APsListPresenterImplementer(getContext());
        aPsListPresenter.setAPsListView(this);
        aPsListPresenter.getAPsList();
        // Keep the screen on
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getConnectedAPInfo() {
        wifiManager = (WifiManager) getActivity().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        txtSSID.setText(wifiInfo.getSSID());
        txtIPAddress.setText("IP: " + wifiInfo.getIpAddress());
        txtChennel.setText("Channel: " + FrequenceyToChannel.convertFrequencyToChannel(wifiInfo.getFrequency()));
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
        // Restore the RecyclerView scroll every time a new reading is displayed
        onSaveInstanceState(state);
        rvAPsList.setAdapter(new APsAdapter(APsList, getContext()));
        rvAPsList.getAdapter().notifyDataSetChanged();
        onViewStateRestored(state);
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
