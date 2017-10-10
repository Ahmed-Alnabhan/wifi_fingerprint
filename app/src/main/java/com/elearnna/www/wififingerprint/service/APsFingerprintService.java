package com.elearnna.www.wififingerprint.service;

import android.app.IntentService;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed on 10/5/2017.
 */

public class APsFingerprintService extends IntentService {

    private IntentFilter intentFilter;
    private WifiManager wifiManager;
    private Context mContext;
    private WiFiBroadcastReceiver wiFiBroadcastReceiver;
    private List<ScanResult> wifiAPsList;
    private AP ap;
    private DateFormat dateFormat;
    private Handler handler;
    private Runnable runnable;
    private int numberOfReadings;
    private String location;


    public APsFingerprintService() {
        super(APsFingerprintService.class.getSimpleName());
    }
        int counter1 = 1;
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // Set Date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        // Get app context
        mContext = getApplication().getApplicationContext();

        // Get the number of wifi scanning
        numberOfReadings = intent.getIntExtra("duration", Constants.DEFAULT_DURATION);

        // Get the location
        location = intent.getStringExtra("location");
        readAPInfo();
    }

    private void readAPInfo(){
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        wiFiBroadcastReceiver = new WiFiBroadcastReceiver();
        mContext.registerReceiver(wiFiBroadcastReceiver, intentFilter);
        //wifiManager.startScan();
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (numberOfReadings <= 0) {
                    handler.removeCallbacks(runnable);
                } else {
                    numberOfReadings--;
                    Utils.readWifiNetworks(1000, wifiManager, handler, runnable);
                    Log.i("SNANNIG:::: REAAAD", "READ" + counter1++);
                }
            }
        };
        runnable.run();
    }


    private class WiFiBroadcastReceiver extends BroadcastReceiver {
        int counter = 1;
        int writen = 1;
        @Override
        public void onReceive(Context context, Intent intent) {
            mContext = context;
            wifiAPsList = wifiManager.getScanResults();
            Log.i("SNANNIG:::: RECIEVED", "READ" + counter++);
            for(ScanResult sr : wifiAPsList){
                ap = new AP();
                ap.setSsid(sr.SSID);
                ap.setLocation(location);
                ap.setChannel(Utils.convertFrequencyToChannel(sr.frequency));
                ap.setFrequency(sr.frequency);
                ap.setMacAddress(sr.BSSID);
                ap.setRssi(sr.level);
                ap.setSecurityProtocol(sr.capabilities);
                if(sr.capabilities != null){
                    ap.setLocked(1);
                } else {
                    ap.setLocked(0);
                }
                Date currentDate = Calendar.getInstance().getTime();
                String formattedDate = dateFormat.format(currentDate);
                ap.setTime(formattedDate);
                writeAPInfoToDB(ap, writen++);
            }
        }
    }

    private void writeAPInfoToDB(AP ap, int writen) {
        // Create a new map of values
        ContentValues apValues = new ContentValues();
        apValues.put(APContentProvider.location, ap.getLocation());
        apValues.put(APContentProvider.rssi, ap.getRssi());
        apValues.put(APContentProvider.ssid, ap.getSsid());
        apValues.put(APContentProvider.channel, ap.getChannel());
        apValues.put(APContentProvider.frequency, ap.getFrequency());
        apValues.put(APContentProvider.ipAddress, "");
        apValues.put(APContentProvider.isLocked, ap.isLocked());
        apValues.put(APContentProvider.securityProtocol, ap.getSecurityProtocol());
        apValues.put(APContentProvider.apManufacturer, ap.getManufacturer());
        apValues.put(APContentProvider.macAddress, ap.getMacAddress());
        apValues.put(APContentProvider.time, ap.getTime());
        Uri uri = mContext.getContentResolver().insert(Constants.APS_CONTENT_URL, apValues);

        Log.i("SNANNIG:::: WRITTENNN", "READ" + writen);
    }
}
