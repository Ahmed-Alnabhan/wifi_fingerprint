package com.elearnna.www.wififingerprint.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.adapter.APsAdapterOnClickHandler;
import com.elearnna.www.wififingerprint.model.AP;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/6/2017.
 */

public class APsListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
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

    @BindView(R.id.ap_item_view)
    ViewGroup apItemContainer;

    Activity activity;
    private List<AP> listOfAps;
    private APsAdapterOnClickHandler listener;


    public APsListViewHolder(View itemView, List<AP> aps, APsAdapterOnClickHandler handler) {
        super(itemView);
        itemView.setOnClickListener(this);
        listOfAps = aps;
        listener = handler;
        ButterKnife.bind(this, itemView);
    }

    public ImageView getWifiImage() {
        return wifiImage;
    }

    public TextView getTxtSSID() {
        return txtSSID;
    }

    public TextView getTxtIPAddress() {
        return txtIPAddress;
    }

    public TextView getTxtConnectionStatus() {
        return txtConnectionStatus;
    }

    public TextView getTxtChennel() {
        return txtChennel;
    }

    public TextView getTxtMAC() {
        return txtMAC;
    }

    public RoundCornerProgressBar getRoundCornerProgressBar() {
        return roundCornerProgressBar;
    }

    public TextView getTxtSignalStrength() {
        return txtSignalStrength;
    }

    public Activity getActivity() {
        return activity;
    }

    public ViewGroup getApItemContainer() {return apItemContainer;}

    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        AP apData = listOfAps.get(adapterPosition);
        listener.onClick(apData);
    }
}
