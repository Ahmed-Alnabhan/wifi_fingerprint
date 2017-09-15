package com.elearnna.www.wififingerprint.presenter;

import com.elearnna.www.wififingerprint.view.APDetailView;

/**
 * Created by Ahmed on 9/14/2017.
 */

public class APDetailPresenterImplemeter implements APDetailPresenter {

    APDetailView apDetailView;
    @Override
    public void setAPDetailView(APDetailView apDetailView) {
        this.apDetailView = apDetailView;
    }

    @Override
    public void getAPDetail() {

    }
}
