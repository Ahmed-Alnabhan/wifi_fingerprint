package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;
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
    public APsAdapter(List<AP> APsList, Context context){
        this.listOfAPs = APsList;
        this.context = context;
    }
    @Override
    public APsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new APsListViewHolder(inflater.inflate(R.layout.ap_item, parent, false));
    }

    @Override
    public void onBindViewHolder(APsListViewHolder holder, int position) {
        AP ap = listOfAPs.get(position);
        int freq = ap.getChennel();
        int channel = Utils.convertFrequencyToChannel(freq);
        int rssi = ap.getRssi();
        holder.getTxtSSID().setText(ap.getSsid());
        holder.getTxtIPAddress().setText(ap.getIpAddress());
        holder.getTxtConnectionStatus().setText(String.valueOf(ap.isConnected()));
        holder.getTxtChennel().setText(String.valueOf("Channel: " + channel));
        holder.getTxtMAC().setText("MAC: " + ap.getMacAddress());
        holder.getTxtSignalStrength().setText(String.valueOf(rssi));
        RSSIRepresenter rssiRepresenter = Utils.setWifiImage(rssi, context);
        holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context, rssiRepresenter.getRSSIImage()));
        holder.getRoundCornerProgressBar().setProgress((120 + (rssi)));
        holder.getRoundCornerProgressBar().setProgressColor(rssiRepresenter.getRSSIStrength());
    }

    @Override
    public int getItemCount() {
        return listOfAPs.size();
    }
}
