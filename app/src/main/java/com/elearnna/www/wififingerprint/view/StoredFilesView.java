package com.elearnna.www.wififingerprint.view;

import com.elearnna.www.wififingerprint.model.File;

import java.util.List;

/**
 * Created by Ahmed on 10/4/2017.
 */

public interface StoredFilesView {
    void displayFilesList(List<File> DevicesList);
    void displayErrorMessage();
    void showAPsLoading();
    void hideAPsLoading();
}
