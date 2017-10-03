package com.elearnna.www.wififingerprint.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.model.AP;
import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.model.Fingerprint;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/29/2017.
 */

public class FingerprintLoader extends AsyncTaskLoader<Fingerprint> {

    String location;
    String ssid;
    int frequency;
    int channel;
    int isLocked;
    String apManufacturer;
    String securityProtocol;
    String ipAddress;
    String time;
    int rssi;
    String mac;
    boolean apExist;

    public FingerprintLoader(Context context, String location) {
        super(context);
        this.location = location;
    }

    @Override
    public Fingerprint loadInBackground() {
        Fingerprint fingerprint = new Fingerprint();
        Device device = new Device();
        List<AP> aps = new ArrayList<>();
        String selection = APContentProvider.location + " LIKE ? ";
        String[] selectionArgs = {location};

        Cursor apsCursor = getContext().getContentResolver().query(Constants.APS_CONTENT_URL, null, selection, selectionArgs, null);
        Cursor deviceCursor = getContext().getContentResolver().query(Constants.DEVICE_CONTENT_URL, null, null, null, null);
        if (deviceCursor.moveToFirst()){
            try {
                while (deviceCursor.moveToNext()) {
                    device.setManufacturer(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.deviceManufacturer)));
                    device.setModel(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.model)));
                    device.setBrand(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.brand)));
                    device.setDevice(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.device)));
                    device.setProduct(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.product)));
                    device.setOs(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.os)));
                    device.setOsVersion(deviceCursor.getString(deviceCursor.getColumnIndex(APContentProvider.osVersion)));
                    device.setApiLevel(deviceCursor.getInt(deviceCursor.getColumnIndex(APContentProvider.apiLevel)));
                }
            } finally {
                deviceCursor.close();
            }
        }

        if (apsCursor.moveToFirst()){
            AP ap;
            try{
                while (apsCursor.moveToNext()){
                    ssid = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.ssid));
                    frequency = apsCursor.getInt(apsCursor.getColumnIndex(APContentProvider.frequency));
                    channel = apsCursor.getInt(apsCursor.getColumnIndex(APContentProvider.channel));
                    isLocked = apsCursor.getInt(apsCursor.getColumnIndex(APContentProvider.isLocked));
                    apManufacturer = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.apManufacturer));
                    securityProtocol = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.securityProtocol));
                    ipAddress = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.ipAddress));
                    time = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.ipAddress));
                    rssi = apsCursor.getInt(apsCursor.getColumnIndex(APContentProvider.rssi));
                    mac = apsCursor.getString(apsCursor.getColumnIndex(APContentProvider.macAddress));

                    apExist = false;
                    ap = createAPobject();
                    if (aps.size() == 0) {
                        aps.add(ap);
                    } else if (aps.size() > 0) {
                        for (AP accessPoint : aps) {
                            if (ap.getMacAddress().equals(accessPoint.getMacAddress())) {
                                List<Integer> arr = accessPoint.getRssiList();

                                arr.add(ap.getRssi());
                                ap.setRssiList(arr);
                                apExist = true;
                                break;
                            }
                        }
                        if (!apExist) {
                            aps.add(ap);
                        }
                    }
                }
            } finally {
                apsCursor.close();
            }
        }
        // Add device object to the Fingerprint object
        fingerprint.setDevice(device);

        // Add location to the Fingerprint object
        fingerprint.setLocation(location);
        fingerprint.setAps(aps);
        return fingerprint;
    }

    private AP createAPobject() {
        AP ap = new AP();
        ap.setSsid(ssid);
        ap.setManufacturer(apManufacturer);
        ap.setChannel(channel);
        ap.setFrequency(frequency);
        ap.setLocked(isLocked);
        ap.setIpAddress(ipAddress);
        ap.setMacAddress(mac);
        ap.setSecurityProtocol(securityProtocol);
        ap.setRssi(rssi);
        List<Integer> array = new ArrayList<>();
        array.add(ap.getRssi());
        ap.setRssiList(array);
        return ap;
    }
}
