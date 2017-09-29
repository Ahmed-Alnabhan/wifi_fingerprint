package com.elearnna.www.wififingerprint.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.model.Device;
import com.elearnna.www.wififingerprint.presenter.DeviceInfoPresenter;
import com.elearnna.www.wififingerprint.presenter.DeviceInfoPresenterImplementer;
import com.elearnna.www.wififingerprint.view.DeviceInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeviceInfoFragment extends Fragment implements DeviceInfoView{
    private DeviceInfoPresenter deviceInfoPresenter;
    private LoaderManager loaderManager;

    // Butterknife definitions
    @BindView(R.id.device_info_manufacturer_value)
    TextView txtDIManufacturer;

    @BindView(R.id.device_info_model_value)
    TextView txtDIModel;

    @BindView(R.id.device_info_brand_value)
    TextView txtDIBrand;

    @BindView(R.id.device_info_device_value)
    TextView txtDIDevice;

    @BindView(R.id.device_info_product_value)
    TextView txtDIProduct;

    @BindView(R.id.device_info_os_value)
    TextView txtDIOS;

    @BindView(R.id.device_info_os_version_value)
    TextView txtDIOSVersion;

    @BindView(R.id.device_info_api_level_value)
    TextView txtDIAPILevel;

    public DeviceInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_device_info, container, false);
        ButterKnife.bind(this, view);
        loaderManager = getLoaderManager();
        deviceInfoPresenter = new DeviceInfoPresenterImplementer(getContext(), loaderManager);
        deviceInfoPresenter.setDeviceInfoView(this);
        deviceInfoPresenter.getDeviceInfo();
        return view;
    }

    @Override
    public void dispalyDeviceInfo(Device device) {
        txtDIManufacturer.setText(device.getManufacturer());
        txtDIModel.setText(device.getModel());
        txtDIBrand.setText(device.getBrand());
        txtDIDevice.setText(device.getDevice());
        txtDIProduct.setText(device.getProduct());
        txtDIOS.setText(device.getOs());
        txtDIOSVersion.setText(device.getOsVersion());
        txtDIAPILevel.setText(String.valueOf(device.getApiLevel()));
    }

    @Override
    public void showErrorMessage() {

    }

    @Override
    public void showAPLoading() {

    }

    @Override
    public void hideAPLoading() {

    }
}
