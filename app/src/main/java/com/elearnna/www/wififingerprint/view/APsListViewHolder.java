package com.elearnna.www.wififingerprint.view;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.elearnna.www.wififingerprint.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/6/2017.
 */

public class APsListViewHolder extends RecyclerView.ViewHolder{
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

    Activity activity;
    public APsListViewHolder(View itemView) {
        super(itemView);
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
}
