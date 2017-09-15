package com.elearnna.www.wififingerprint.presenter;

import android.widget.TextView;

import com.elearnna.www.wififingerprint.view.APDetailView;

/**
 * Created by Ahmed on 9/14/2017.
 */

public interface APDetailPresenter {
    void setAPDetailView(APDetailView apDetailView);
    void getAPDetail();
    void getVendorFromMac(String mac, TextView tv);
}
