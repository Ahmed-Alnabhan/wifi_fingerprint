package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.RSSIRepresenter;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.view.APsListViewHolder;

import java.util.List;

/**
 * Created by Ahmed on 9/8/2017.
 */

public class APsAdapter extends RecyclerView.Adapter<APsListViewHolder>{

    private List<AP> listOfAPs;
    private Context context;
    private APsAdapterOnClickHandler onClickHandler;
    private AP ap;

    public APsAdapter(List<AP> APsList, Context context, APsAdapterOnClickHandler onClickHandler){
        this.listOfAPs = APsList;
        this.context = context;
        this.onClickHandler = onClickHandler;
    }
    @Override
    public APsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new APsListViewHolder(inflater.inflate(R.layout.ap_item, parent, false), listOfAPs, onClickHandler);
    }

    @Override
    public void onBindViewHolder(APsListViewHolder holder, int position) {
        ap = listOfAPs.get(position);
        int freq = ap.getChennel();
        int channel = Utils.convertFrequencyToChannel(freq);
        int rssi = ap.getRssi();

        // Set the name of the network SSID
        String ssid = ap.getSsid();
        if (!ssid.isEmpty() && ssid != null) {
            holder.getTxtSSID().setText(ssid);
        } else {
            holder.getTxtSSID().setText(Constants.UNKNOWN);
        }

        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(context);
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(context);

        // Set the style of the SSID based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtSSID(), bold_font, "Large");

        // Set the style of the IP address based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtIPAddress(), regular_font, "Regular");

        // Set the style of the Connection status based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtConnectionStatus(), regular_font, "Small");

        // Set the style of the Channel based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtChennel(), regular_font, "Large");

        // Set the style of the MAC based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtMAC(), regular_font, "Large");

        // Set the style of the RSSI based on the app theme
        Utils.setTextViewStyle(context, holder.getTxtSignalStrength(), bold_font, "Large");

        holder.getTxtIPAddress().setText(ap.getIpAddress());
        holder.getTxtConnectionStatus().setText(String.valueOf(ap.isConnected()));
        holder.getTxtChennel().setText(String.valueOf("Channel: " + channel));
        holder.getTxtMAC().setText("MAC: " + ap.getMacAddress());
        holder.getTxtSignalStrength().setText(String.valueOf(rssi));
        RSSIRepresenter rssiRepresenter = Utils.setWifiImage(rssi, context);
        holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context, rssiRepresenter.getRSSIImage()));
        holder.getRoundCornerProgressBar().setProgress((120 + (rssi)));
        holder.getRoundCornerProgressBar().setProgressColor(rssiRepresenter.getRSSIStrength());
        // Check if the AP is connected to the Internet then show a connected textview
        if (Utils.isConnected(context, ap.getMacAddress())){
            holder.getTxtConnectionStatus().setText("Connected");
        } else {
            holder.getTxtConnectionStatus().setText("");
        }

        // Set the background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if (context.getApplicationInfo().theme == R.style.Theme_Material_Dark) {
                holder.getApItemContainer().setBackground(ContextCompat.getDrawable(context,R.drawable.dark_background_gradient));
            } else if (context.getApplicationInfo().theme == R.style.Theme_Material_Light) {
                holder.getApItemContainer().setBackground(ContextCompat.getDrawable(context,R.drawable.light_background_gradient));
            }
        }

    }

    @Override
    public int getItemCount() {
        return listOfAPs.size();
    }
}
