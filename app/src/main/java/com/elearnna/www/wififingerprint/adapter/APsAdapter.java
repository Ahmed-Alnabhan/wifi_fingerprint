package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.FrequenceyToChannel;
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
        int channel = FrequenceyToChannel.convertFrequencyToChannel(freq);
        int rssi = ap.getRssi();
        holder.getTxtSSID().setText(ap.getSsid());
        holder.getTxtIPAddress().setText(ap.getIpAddress());
        holder.getTxtConnectionStatus().setText(String.valueOf(ap.isConnected()));
        holder.getTxtChennel().setText(String.valueOf("Channel: " + channel));
        holder.getTxtMAC().setText("MAC: " + ap.getMacAddress());
        holder.getTxtSignalStrength().setText(String.valueOf(rssi));
        int rssiSignalColor = setWifiImage(holder, rssi);
        holder.getRoundCornerProgressBar().setProgress((120 + (rssi)));
        holder.getRoundCornerProgressBar().setProgressColor(rssiSignalColor);
    }

    private int setWifiImage(APsListViewHolder holder, int rssi) {
        int rssiSiganlColor = 0;
        int signalRange;
        signalRange = isBetween(rssi);
        switch (signalRange) {
            case Constants.EXCELLENT:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_full));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.green);
                break;
            case Constants.GOOD:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_good));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.light_green);
                break;
            case Constants.MEDIUM:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_medium));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.orange);
                break;
            case Constants.FAIR:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_average));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.dark_orange);
                break;
            case Constants.WEAK:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_weak));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.red);
                break;
            case Constants.VERY_WEAK:
                holder.getWifiImage().setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_wifi_signal_very_weak));
                rssiSiganlColor = ContextCompat.getColor(context, R.color.dark_red);
                break;
            default:
                Log.e("APsAdapter", "Error in wifi signals reading");
                break;
        }
        return rssiSiganlColor;
    }

    private int isBetween(int rssi) {
        if(rssi <= Constants.MAX_EXCELLENT && rssi >= Constants.MIN_EXCELLENT){
            return Constants.EXCELLENT;
        } else if(rssi <= Constants.MAX_GOOD && rssi >= Constants.MIN_GOOD){
            return Constants.GOOD;
        } else if(rssi <= Constants.MAX_MEDIUM && rssi >= Constants.MIN_MEDIUM){
            return Constants.MEDIUM;
        } else if(rssi <= Constants.MAX_FAIR && rssi >= Constants.MIN_FAIR){
            return Constants.FAIR;
        }else if(rssi <= Constants.MAX_WEAK && rssi >= Constants.MIN_WEAK){
            return Constants.WEAK;
        }else if(rssi <= Constants.MAX_VERY_WEAK && rssi >= Constants.MIN_VERY_WEAK){
            return Constants.VERY_WEAK;
        }
        return Constants.ERROR;
    }

    @Override
    public int getItemCount() {
        return listOfAPs.size();
    }
}