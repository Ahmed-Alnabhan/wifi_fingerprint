package com.elearnna.www.wififingerprint.presenter;

import android.os.Build;

import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.view.DeviceInfoView;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class DeviceInfoPresenterImplementer implements DeviceInfoPresenter {

    private DeviceInfoView deviceInfoView;
    private Device device;

    @Override
    public void setDeviceInfoView(DeviceInfoView view) {
        deviceInfoView = view;
    }

    @Override
    public void getDeviceInfo() {
        device = new Device();
        device.setManufacturer(Build.MANUFACTURER);
        device.setBrand(Build.BRAND);
        device.setDevice(Build.DEVICE);
        device.setModel(Build.MODEL);
        device.setProduct(Build.PRODUCT);
        device.setOs("Android");
        device.setApiLevel(Build.VERSION.SDK_INT);
        device.setOsVersion(Build.VERSION.RELEASE);
        deviceInfoView.dispalyDeviceInfo(device);
    }
}
