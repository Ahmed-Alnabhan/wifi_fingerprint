package com.elearnna.www.wififingerprint.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.view.APsListView;

import java.util.ArrayList;
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
    private List<AP> APsList;
    private AP ap;

    public APsListPresenterImplementer(Context context) {
        this.mContext = context;
    }

    @Override
    public void setAPsListView(APsListView view) {
        aPsListView = view;
    }

    @Override
    public void getAPsList() {
        APsList = new ArrayList<>();
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wiFiBroadcastReceiver = new WiFiBroadcastReceiver();
        mContext.getApplicationContext().registerReceiver(wiFiBroadcastReceiver, intentFilter);
        aPsListView.showAPsLoading();
        handler = new Handler();
        runnable = new Runnable(){
            @Override
            public void run() {
                Utils.readWifiNetworks(1000, wifiManager, handler, runnable);
            }
        };
        handler.postDelayed(runnable, 1000);
        if(!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }


    }

    public class WiFiBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            APsList.clear();
            wifiAPsList = wifiManager.getScanResults();
            for(ScanResult sr : wifiAPsList){
                ap = new AP();
                ap.setSsid(sr.SSID);
                ap.setChannel(Utils.convertFrequencyToChannel(sr.frequency));
                ap.setFrequency(sr.frequency);
                ap.setMacAddress(sr.BSSID);
                ap.setRssi(sr.level);
                if (Build.VERSION.SDK_INT >= 23) {
                    ap.setManufacturer(String.valueOf(sr.channelWidth));
                } else {
                    ap.setManufacturer(Constants.NOT_APPLICABLE);
                }
                ap.setSecurityProtocol(sr.capabilities);
                APsList.add(ap);
            }
            aPsListView.displayAPsList(APsList);
            Log.i("MAIN", "EVERY ... SECONDS");
        }
    }
}
