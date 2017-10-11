package com.elearnna.www.wififingerprint.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Utils;
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
    @BindView(R.id.device_info_manufacturer_label)
    TextView txtDIManufacturerLabel;

    @BindView(R.id.device_info_manufacturer_value)
    TextView txtDIManufacturer;

    @BindView(R.id.device_info_model_label)
    TextView txtDIModelLabel;

    @BindView(R.id.device_info_model_value)
    TextView txtDIModel;

    @BindView(R.id.device_info_brand_label)
    TextView txtDIBrandLabel;

    @BindView(R.id.device_info_brand_value)
    TextView txtDIBrand;

    @BindView(R.id.device_info_device_label)
    TextView txtDIDeviceLabel;

    @BindView(R.id.device_info_device_value)
    TextView txtDIDevice;

    @BindView(R.id.device_info_product_label)
    TextView txtDIProductLabel;

    @BindView(R.id.device_info_product_value)
    TextView txtDIProduct;

    @BindView(R.id.device_info_os_label)
    TextView txtDIOSLabel;

    @BindView(R.id.device_info_os_value)
    TextView txtDIOS;

    @BindView(R.id.device_info_os_version_label)
    TextView txtDIOSVersionLabel;

    @BindView(R.id.device_info_os_version_value)
    TextView txtDIOSVersion;

    @BindView(R.id.device_info_api_level_label)
    TextView txtDIAPILevelLabel;

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

        // Set views style
        setViewsStyle();

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

    /**
     * This method sets style to the views
     */

    private void setViewsStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getContext());
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(getContext());

        // Set the style of the manufacturer value TextView
        Utils.setTextViewStyle(getContext(), txtDIManufacturer, bold_font, "Regular");

        // Set the style of the manufacturer label TextView
        Utils.setTextViewStyle(getContext(), txtDIManufacturerLabel, regular_font, "Regular");

        // Set the style of the model value TextView
        Utils.setTextViewStyle(getContext(), txtDIModel, bold_font, "Regular");

        // Set the style of the model label TextView
        Utils.setTextViewStyle(getContext(), txtDIModelLabel, regular_font, "Regular");

        // Set the style of the brand value TextView
        Utils.setTextViewStyle(getContext(), txtDIBrand, bold_font, "Regular");

        // Set the style of the brand label TextView
        Utils.setTextViewStyle(getContext(), txtDIBrandLabel, regular_font, "Regular");

        // Set the style of the device value TextView
        Utils.setTextViewStyle(getContext(), txtDIDevice, bold_font, "Regular");

        // Set the style of the device label TextView
        Utils.setTextViewStyle(getContext(), txtDIDeviceLabel, regular_font, "Regular");

        // Set the style of the product value TextView
        Utils.setTextViewStyle(getContext(), txtDIProduct, bold_font, "Regular");

        // Set the style of the product label TextView
        Utils.setTextViewStyle(getContext(), txtDIProductLabel, regular_font, "Regular");

        // Set the style of the os value TextView
        Utils.setTextViewStyle(getContext(), txtDIOS, bold_font, "Regular");

        // Set the style of the os label TextView
        Utils.setTextViewStyle(getContext(), txtDIOSLabel, regular_font, "Regular");

        // Set the style of the os version value TextView
        Utils.setTextViewStyle(getContext(), txtDIOSVersion, bold_font, "Regular");

        // Set the style of the os version label TextView
        Utils.setTextViewStyle(getContext(), txtDIOSVersionLabel, regular_font, "Regular");

        // Set the style of the api level value TextView
        Utils.setTextViewStyle(getContext(), txtDIAPILevel, bold_font, "Regular");

        // Set the style of the api level label TextView
        Utils.setTextViewStyle(getContext(), txtDIAPILevelLabel, regular_font, "Regular");
    }
}
