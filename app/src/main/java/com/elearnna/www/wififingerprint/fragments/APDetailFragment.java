package com.elearnna.www.wififingerprint.fragments;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.ApiUtils;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.RSSIRepresenter;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.network.VendorService;
import com.elearnna.www.wififingerprint.presenter.APDetailPresenter;
import com.elearnna.www.wififingerprint.view.APDetailView;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class APDetailFragment extends Fragment implements APDetailView, APDetailPresenter{
    private Intent intent;
    private AP ap;
    private APDetailView apDetailView;
    private VendorService vendorService;
    private String vendor;

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

    @BindView(R.id.roundCornerProgressBarDetail)
    RoundCornerProgressBar roundCornerProgressBarDetail;

    @BindView(R.id.txt_manufacturer_label)
    TextView txtManufacturerLabel;

    @BindView(R.id.txt_frequency_label)
    TextView txtFrequencyLabel;

    @BindView(R.id.txt_band_label)
    TextView txtBandLabel;

    @BindView(R.id.txt_mac_label)
    TextView txtMACLabel;

    @BindView(R.id.txt_channel_label)
    TextView txtChannelLabel;

    @BindView(R.id.txt_security_protocol_label)
    TextView txtSecurityProtocolLabel;

    @BindView(R.id.rssi_signal_strength)
    TextView rssiSignalStrength;

    public APDetailFragment() {
        // Required empty public constructor
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apdetail, container, false);
        ButterKnife.bind(this, view);
        setViewsStyle();
        setAPDetailView(this);
        getAPDetail();
        dispalyAP(ap);

        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void dispalyAP(AP ap) {
        if(ap != null) {
            String ssid = ap.getSsid();
            int rssi = ap.getRssi();
            String mac = ap.getMacAddress();
            int frequency = ap.getFrequency();
            int channel = Utils.convertFrequencyToChannel(frequency);
            String band = Utils.getBandFromFrequency(frequency);
            String capabilities = ap.getSecurityProtocol();
            int rssiColor = Utils.getRSSIColor(rssi);
            rssiSignalStrength.setText(String.valueOf(rssi));

            securityProtocol.setText(capabilities);
            // set the AP name (SSID)
            if (!ssid.isEmpty() && ssid != null) {
                apNameTitle.setText(ssid);
            } else {
                apNameTitle.setText(Constants.UNKNOWN);
            }

            // set the band
            if (!band.isEmpty() && band != null) {
                bandValue.setText(band);
            } else {
                bandValue.setText(Constants.UNKNOWN);
            }
            getVendorFromMac(mac, manufacturerValue);
            frequencyValue.setText(String.valueOf(frequency));
            macValue.setText(mac);
            channelValue.setText(String.valueOf(channel));
            RSSIRepresenter rssiRepresenter = Utils.setWifiImage(rssi, getContext());
            roundCornerProgressBarDetail.setProgress((120 + (rssi)));
            roundCornerProgressBarDetail.setProgressColor(rssiRepresenter.getRSSIStrength());
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

    @Override
    public void getVendorFromMac(String mac, final TextView tv) {
        vendorService = ApiUtils.getVendorService();
        Call<String> call = vendorService.getVendor(mac);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    vendor = response.body();
                    if (!vendor.isEmpty() && vendor != null) {
                        tv.setText(vendor);
                    } else {
                        tv.setText(Constants.UNKNOWN);
                    }
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                showErrorMessage();
                Log.e("failure", String.valueOf(t.getCause()));
            }
        });
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

    /**
     * This method sets style to the views
     */

    private void setViewsStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getContext());
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(getContext());

        // Set the style of the SSID value TextView
        Utils.setTextViewStyle(getContext(), apNameTitle, bold_font, "Large");

        // Set the style of the manufacturer label TextView
        Utils.setTextViewStyle(getContext(), txtManufacturerLabel, regular_font, "Regular");

        // Set the style of the manufacturer value TextView
        Utils.setTextViewStyle(getContext(), manufacturerValue, bold_font, "Regular");

        // Set the style of the Frequency label TextView
        Utils.setTextViewStyle(getContext(), txtFrequencyLabel, regular_font, "Regular");

        // Set the style of the Frequency value TextView
        Utils.setTextViewStyle(getContext(), frequencyValue, bold_font, "Regular");

        // Set the style of the band label TextView
        Utils.setTextViewStyle(getContext(), txtBandLabel, regular_font, "Regular");

        // Set the style of the band value TextView
        Utils.setTextViewStyle(getContext(), bandValue, bold_font, "Regular");

        // Set the style of the Mac Address label TextView
        Utils.setTextViewStyle(getContext(), txtMACLabel, regular_font, "Regular");

        // Set the style of the Mac Address value TextView
        Utils.setTextViewStyle(getContext(), macValue, bold_font, "Regular");

        // Set the style of the channel label TextView
        Utils.setTextViewStyle(getContext(), txtChannelLabel, regular_font, "Regular");

        // Set the style of the channel value TextView
        Utils.setTextViewStyle(getContext(), channelValue, bold_font, "Regular");

        // Set the style of the security protocols label TextView
        Utils.setTextViewStyle(getContext(), txtSecurityProtocolLabel, regular_font, "Regular");

        // Set the style of the security protocols value TextView
        Utils.setTextViewStyle(getContext(), securityProtocol, bold_font, "Regular");

        // Set the style of the security protocols value TextView
        Utils.setTextViewStyle(getContext(), rssiSignalStrength, bold_font, "Large");
    }
}
