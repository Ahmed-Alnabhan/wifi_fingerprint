package com.elearnna.www.wififingerprint.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

/**
 * Created by Ahmed on 9/28/2017.
 */

public class DeviceLoader extends AsyncTaskLoader<Device> {

        public DeviceLoader(Context context) {
            super(context);
        }

        @Override
        public Device loadInBackground() {
            Device device = new Device();
            Cursor cursor = getContext().getContentResolver().query(Constants.DEVICE_CONTENT_URL, null, null, null, null);
            if (cursor.moveToFirst()) {
                try {
                    do {
                        device.setManufacturer(cursor.getString(cursor.getColumnIndex(APContentProvider.deviceManufacturer)));
                        device.setModel(cursor.getString(cursor.getColumnIndex(APContentProvider.model)));
                        device.setBrand(cursor.getString(cursor.getColumnIndex(APContentProvider.brand)));
                        device.setDevice(cursor.getString(cursor.getColumnIndex(APContentProvider.device)));
                        device.setProduct(cursor.getString(cursor.getColumnIndex(APContentProvider.product)));
                        device.setOs(cursor.getString(cursor.getColumnIndex(APContentProvider.os)));
                        device.setOsVersion(cursor.getString(cursor.getColumnIndex(APContentProvider.osVersion)));
                        device.setApiLevel(cursor.getInt(cursor.getColumnIndex(APContentProvider.apiLevel)));
                    } while (cursor.moveToNext());
                } finally {
                    cursor.close();
                }
            }
            return device;
        }

}
