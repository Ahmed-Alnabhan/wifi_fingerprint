package com.elearnna.www.wififingerprint.view;

import com.elearnna.www.wififingerprint.model.Device;

/**
 * Created by Ahmed on 9/16/2017.
 */

public interface DeviceInfoView {
    void dispalyDeviceInfo(Device device);
    void showErrorMessage();
    void showAPLoading();
    void hideAPLoading();
}
