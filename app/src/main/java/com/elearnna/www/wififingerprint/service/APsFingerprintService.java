package com.elearnna.www.wififingerprint.service;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Ahmed on 10/5/2017.
 */

public class APsFingerprintService extends Service {

    private IntentFilter intentFilter;
    private WifiManager wm;
    private Context mContext;
//    private APBroadcastReceiver apBroadcastReceiver;
    private List<ScanResult> wifiAPsList;
    private AP ap;
    private DateFormat dateFormat;
    private Handler handler;
    private Runnable runnable;
    private int numberOfReadings;
    private String location;
    int counter1 = 1;


    public APsFingerprintService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Set Date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        // Get app context
        mContext = getApplication().getApplicationContext();
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wm = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);

        // Get the number of wifi scanning
        if (intent.hasExtra("duration")) {
            numberOfReadings = intent.getIntExtra("duration", Constants.DEFAULT_DURATION);
        }

        // Get the location
        if (intent.hasExtra("location")) {
            location = intent.getStringExtra("location");
        }
        readAPInfo();
        return super.onStartCommand(intent, flags, startId);
    }

    private void readAPInfo(){
        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                if (numberOfReadings > 0) {
                    numberOfReadings -= 1;
                    performWifiScan();
                    handler.postDelayed(this, 1000);
                } else {
                    handler.removeCallbacks(runnable);
                    stopSelf();
                }
            }
        };
        handler.postDelayed(runnable, 1000);

    }

    private void writeAPInfoToDB(AP ap) {
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

    }

    private void performWifiScan() {
        List<ScanResult> scanResults = new ArrayList<>();
        try {
            if (!wm.isWifiEnabled()) {
                wm.setWifiEnabled(true);
            }
            if (wm.startScan()) {
                scanResults = wm.getScanResults();
            }
            for (ScanResult sr : scanResults) {
                ap = new AP();
                ap.setSsid(sr.SSID);
                ap.setLocation(location);
                ap.setChannel(Utils.convertFrequencyToChannel(sr.frequency));
                ap.setFrequency(sr.frequency);
                ap.setMacAddress(sr.BSSID);
                ap.setRssi(sr.level);
                ap.setSecurityProtocol(sr.capabilities);
                if (sr.capabilities != null) {
                    ap.setLocked(1);
                } else {
                    ap.setLocked(0);
                }
                Date currentDate = Calendar.getInstance().getTime();
                String formattedDate = dateFormat.format(currentDate);
                ap.setTime(formattedDate);
                writeAPInfoToDB(ap);
            }
        } catch (Exception e) {
            // critical error: set to no results and do not die
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
