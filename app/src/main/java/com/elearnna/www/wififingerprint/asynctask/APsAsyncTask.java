package com.elearnna.www.wififingerprint.asynctask;

import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.model.Locator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Ahmed on 10/13/2017.
 */

public class APsAsyncTask extends AsyncTask<Locator, Void, Void> {

    private int duration;
    private String location;
    private IntentFilter intentFilter;
    private WifiManager wm;
    private Context mContext;
    private List<ScanResult> wifiAPsList;
    private AP ap;
    private DateFormat dateFormat;
    int counter1 = 1;

    public APsAsyncTask(Context context) {
        mContext = context;
    }

    @Override
    protected Void doInBackground(Locator... locators) {
        Locator locator = locators[0];
        duration = locator.getDuration();
        location = locator.getLocation();
        // Set Date format
        dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        readAPInfo();

        return null;
    }

    private void readAPInfo() {

        Log.i("START READING", "START READING" + counter1);

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
        new CountDownTimer(duration * Constants.ONE_SECOND, Constants.ONE_SECOND) {
            @Override
            public void onTick(long l) {
                wm.startScan();
                duration--;
                Log.i("NEW READING", "CONTIN READING" + counter1++);
            }

            @Override
            public void onFinish() {
                try {
                    //mContext.unregisterReceiver(apBroadcastReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
            }
        });

//        handler = new Handler();
//        runnable = new Runnable() {
//            @Override
//            public void run() {
//                if (duration > 0) {
//                    duration -= 1;
//                    wm.setWifiEnabled(true);
//                    wm.startScan();
//                    Log.i("START READING", "START READING" + counter1++);
//                    handler.postDelayed(this, 1000);
//                } else {
//                    Log.i("STOPED:", "FINISH TIMER" );
//                    handler.removeCallbacks(runnable);
//                    mContext.unregisterReceiver(apBroadcastReceiver);
//                }
//            }
//        };
//        handler.postDelayed(runnable, 1000);
//        runnable.run();
    }



//        private class APBroadcastReceiver extends BroadcastReceiver {
//        int counter = 1;
//        int writen = 1;
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            if (duration > 0) {
//                wifiAPsList = wm.getScanResults();
//                Log.i("RECEIVED:", "RECEIVED" + counter++);
//                for (ScanResult sr : wifiAPsList) {
//                    ap = new AP();
//                    ap.setSsid(sr.SSID);
//                    ap.setLocation(location);
//                    ap.setChannel(Utils.convertFrequencyToChannel(sr.frequency));
//                    ap.setFrequency(sr.frequency);
//                    ap.setMacAddress(sr.BSSID);
//                    ap.setRssi(sr.level);
//                    ap.setSecurityProtocol(sr.capabilities);
//                    if (sr.capabilities != null) {
//                        ap.setLocked(1);
//                    } else {
//                        ap.setLocked(0);
//                    }
//                    Date currentDate = Calendar.getInstance().getTime();
//                    String formattedDate = dateFormat.format(currentDate);
//                    ap.setTime(formattedDate);
//                    writeAPInfoToDB(ap, writen++);
//                    duration--;
//                }
//            }else {
//                Log.i("STOPED:", "FINISH TIMER" );
//                //handler.removeCallbacks(runnable);
//                mContext.unregisterReceiver(apBroadcastReceiver);
//            }
//        }
//    }

//    private void writeAPInfoToDB(AP ap, int writen) {
//        // Create a new map of values
//        ContentValues apValues = new ContentValues();
//        apValues.put(APContentProvider.location, ap.getLocation());
//        apValues.put(APContentProvider.rssi, ap.getRssi());
//        apValues.put(APContentProvider.ssid, ap.getSsid());
//        apValues.put(APContentProvider.channel, ap.getChannel());
//        apValues.put(APContentProvider.frequency, ap.getFrequency());
//        apValues.put(APContentProvider.ipAddress, "");
//        apValues.put(APContentProvider.isLocked, ap.isLocked());
//        apValues.put(APContentProvider.securityProtocol, ap.getSecurityProtocol());
//        apValues.put(APContentProvider.apManufacturer, ap.getManufacturer());
//        apValues.put(APContentProvider.macAddress, ap.getMacAddress());
//        apValues.put(APContentProvider.time, ap.getTime());
//        Uri uri = mContext.getContentResolver().insert(Constants.APS_CONTENT_URL, apValues);
//
//        Log.i("WRITTEN TO:", "DB" + writen);
//    }
}
