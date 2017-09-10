package com.elearnna.www.wififingerprint.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.view.APsListViewHolder;

import java.util.List;

/**
 * Created by Ahmed on 9/8/2017.
 */

public class APsAdapter extends RecyclerView.Adapter<APsListViewHolder>{

    private List<AP> listOfAPs;
    public APsAdapter(List<AP> APsList){
        this.listOfAPs = APsList;
    }
    @Override
    public APsListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new APsListViewHolder(inflater.inflate(R.layout.ap_item, parent, false));
    }

    @Override
    public void onBindViewHolder(APsListViewHolder holder, int position) {
        AP ap = listOfAPs.get(position);
        holder.getTxtSSID().setText(ap.getSsid());
        holder.getTxtIPAddress().setText(ap.getIpAddress());
        holder.getTxtConnectionStatus().setText(String.valueOf(ap.isConnected()));
        holder.getTxtChennel().setText(String.valueOf("Channel: " + ap.getChennel()));
        holder.getTxtMAC().setText("MAC: " + ap.getMacAddress());
        holder.getTxtSignalStrength().setText(String.valueOf(ap.getRssi()));
    }

    @Override
    public int getItemCount() {
        return listOfAPs.size();
    }
}
