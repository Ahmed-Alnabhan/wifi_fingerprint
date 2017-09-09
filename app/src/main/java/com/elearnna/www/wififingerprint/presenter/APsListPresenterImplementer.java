package com.elearnna.www.wififingerprint.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.util.Log;

import com.elearnna.www.wififingerprint.view.APsListView;

import java.util.List;

/**
 * Created by Ahmed on 9/6/2017.
 */

public class APsListPresenterImplementer implements APsListPresenter{

    private APsListView aPsListView;
    private WifiManager wifiManager;
    private Handler handler;
    private Runnable runnable;
    private List<ScanResult> wifiAPsList;
    private WiFiBroadcastReceiver wiFiBroadcastReceiver;
    private Context mContext;
    private IntentFilter intentFilter;

    public APsListPresenterImplementer(Context context) {
        this.mContext = context;
    }

    private void readWifiNetworks() {
        wifiManager.setWifiEnabled(true);
        wifiManager.startScan();
        handler.postDelayed(runnable, 1000);
    }

    @Override
    public void setAPsListView(APsListView view) {
        aPsListView = view;
    }

    @Override
    public void getAPsList() {
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wiFiBroadcastReceiver = new WiFiBroadcastReceiver();
        mContext.getApplicationContext().registerReceiver(wiFiBroadcastReceiver, intentFilter);
        aPsListView.showAPsLoading();
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                readWifiNetworks();
            }
        };
        runnable.run();
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }


    }

    private class WiFiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            wifiAPsList = wifiManager.getScanResults();
            Log.d("AAA", String.valueOf(wifiAPsList.size()));
        }
    }
}
