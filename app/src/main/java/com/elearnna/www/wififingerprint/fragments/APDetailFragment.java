package com.elearnna.www.wififingerprint.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.presenter.APDetailPresenter;
import com.elearnna.www.wififingerprint.view.APDetailView;
import com.shinelw.library.ColorArcProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class APDetailFragment extends Fragment implements APDetailView, APDetailPresenter{
    private Intent intent;
    private AP ap;
    private APDetailView apDetailView;

    // Fields
    @BindView(R.id.txt_ap_name_title)
    TextView apNameTitle;

    @BindView(R.id.txt_mac_value)
    TextView macValue;

    @BindView(R.id.txt_manufacturer_value)
    TextView manufacturerValue;

    @BindView(R.id.txt_frequency_value)
    TextView frequencyValue;

    @BindView(R.id.txt_band_value)
    TextView bandValue;

    @BindView(R.id.txt_security_protocol_value)
    TextView securityProtocol;

    @BindView(R.id.txt_channel_value)
    TextView channelValue;

    @BindView(R.id.colorArcProgressBar)
    ColorArcProgressBar colorArcProgressBar;

    public APDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apdetail, container, false);
        ButterKnife.bind(this, view);
        setAPDetailView(this);
        getAPDetail();
        dispalyAP(ap);
        return view;
    }

    @Override
    public void dispalyAP(AP ap) {
        if(ap != null) {
            apNameTitle.setText(ap.getSsid());
            manufacturerValue.setText(ap.getManufacturer());
            //frequencyValue.setText(ap.getFrequency());
            macValue.setText(ap.getMacAddress());
            int rssi = ap.getRssi();
            colorArcProgressBar.setIsShowCurrentSpeed(true);
            colorArcProgressBar.setCurrentValues((float)Math.round(rssi * (-0.6)));
            colorArcProgressBar.setMaxValues(-100);
        }
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void showAPLoading() {

    }

    @Override
    public void hideAPLoading() {

    }

    @Override
    public void setAPDetailView(APDetailView apDetailView) {
        this.apDetailView = apDetailView;
    }

    @Override
    public void getAPDetail() {
        intent = getActivity().getIntent();
        if(intent.hasExtra(Constants.ACCESS_POINT)){
            ap = intent.getParcelableExtra(Constants.ACCESS_POINT);
        } else {
            ap = getArguments().getParcelable(Constants.ACCESS_POINT);
        }
    }

    /**
     * Save the instance
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.ACCESS_POINT, ap);
    }

    /**
     * Restore the instance
     * @param savedInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            ap = savedInstanceState.getParcelable(Constants.ACCESS_POINT);
        }
    }
}
