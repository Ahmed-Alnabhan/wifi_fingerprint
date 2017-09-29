package com.elearnna.www.wififingerprint.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.elearnna.www.wififingerprint.loader.DeviceLoader;
import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.view.DeviceInfoView;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class DeviceInfoPresenterImplementer implements DeviceInfoPresenter, LoaderManager.LoaderCallbacks<Device> {

    private DeviceInfoView deviceInfoView;
    private Device device;
    private Context context;
    private LoaderManager loaderManager;

    public DeviceInfoPresenterImplementer(Context context, LoaderManager loaderManager){
        this.context = context;
        this.loaderManager = loaderManager;
    }

    @Override
    public void setDeviceInfoView(DeviceInfoView view) {
        deviceInfoView = view;
    }

    @Override
    public void getDeviceInfo() {
        loaderManager.initLoader(1, null, this).forceLoad();
    }

    @Override
    public Loader<Device> onCreateLoader(int id, Bundle args) {
        device = new Device();
        return new DeviceLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<Device> loader, Device data) {
        device = data;
        deviceInfoView.dispalyDeviceInfo(device);
    }

    @Override
    public void onLoaderReset(Loader<Device> loader) {

    }
}
