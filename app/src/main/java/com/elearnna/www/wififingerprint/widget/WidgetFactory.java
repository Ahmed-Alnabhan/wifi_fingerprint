package com.elearnna.www.wififingerprint.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.model.AP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 10/16/2017.
 */

public class WidgetFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private Intent intent;
    private AP ap;
    private List<AP> aps;
    private IntentFilter intentFilter;
    private WifiManager wm;
    private WidgetAPsBR br;
    private List<ScanResult> apsList;

    public WidgetFactory(Context applicationContext, Intent intent) {
        this.context = applicationContext;
        this.intent = intent;
    }

    @Override
    public void onCreate() {
        int x = 0;
    }

    @Override
    public void onDataSetChanged() {
        loadAPsInfo();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (aps != null) {
            return aps.size();
        }else {
            return 0;
        }
    }

    @Override
    public RemoteViews getViewAt(int i) {
        String ssid = null;
        String mac = null;
        int rssi = 0;

        RemoteViews remoteViews = null;
        //RemoteViews widgetRemoteViews = new RemoteViews(context.getPackageName(), R.layout.wifi_fingerprint_widget);
        if (aps != null) {
            AP ap = aps.get(i);
            remoteViews = new RemoteViews(context.getPackageName(), R.layout.ap_item_widget);
            ssid = ap.getSsid();
            mac = context.getResources().getString(R.string.mac_label) + ap.getMacAddress();
            rssi = ap.getRssi();
        } 
        remoteViews.setTextViewText(R.id.widget_ssid, ssid);
        remoteViews.setTextViewText(R.id.widget_mac, mac);
        remoteViews.setTextViewText(R.id.widget_rssi, String.valueOf(rssi));
        //widgetRemoteViews.addView(R.id.widget_aps_list_view, remoteViews);

        Intent intent = new Intent();
        intent.putExtra("mac", mac);
        remoteViews.setOnClickFillInIntent(R.id.widget_mac, intent);

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return (1);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void loadAPsInfo() {
        intentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        br = new WidgetAPsBR();
        context.registerReceiver(br, intentFilter);
    }

    private class WidgetAPsBR extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("RECEIVED:", "FROM WIDGET");
            apsList = new ArrayList<>();
            aps = new ArrayList<>();
            apsList = wm.getScanResults();
            for (ScanResult sr : apsList) {
                ap = new AP();
                ap.setSsid(sr.SSID);
                ap.setMacAddress(sr.BSSID);
                ap.setRssi(sr.level);
                aps.add(ap);
            }
        }
    }
}
