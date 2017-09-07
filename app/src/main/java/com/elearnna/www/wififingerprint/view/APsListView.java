package com.elearnna.www.wififingerprint.view;

import com.elearnna.www.wififingerprint.model.AP;

import java.util.List;

/**
 * Created by Ahmed on 9/6/2017.
 */

public interface APsListView {
    void displayAPsList(List<AP> APsList);
    void showAPDetail(AP ap);
    void displayErrorMessage();
    void showAPsLoading();
    void hideAPsLoading();
}
