package com.elearnna.www.wififingerprint.presenter;

import com.elearnna.www.wififingerprint.view.StoredFilesView;

/**
 * Created by Ahmed on 10/4/2017.
 */

public interface StoredFilesPresenter {
    void getStoredFilesList();
    void setStoredFilesView(StoredFilesView view);
}
