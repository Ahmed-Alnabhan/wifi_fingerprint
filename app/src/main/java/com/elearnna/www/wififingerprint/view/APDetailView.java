package com.elearnna.www.wififingerprint.view;

import com.elearnna.www.wififingerprint.model.AP;

/**
 * Created by Ahmed on 9/14/2017.
 */

public interface APDetailView {
    void dispalyAP(AP ap);
    void showErrorMessage();
    void showAPLoading();
    void hideAPLoading();
}
