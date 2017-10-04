package com.elearnna.www.wififingerprint.presenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.elearnna.www.wififingerprint.loader.StoredFilesLoader;
import com.elearnna.www.wififingerprint.model.File;
import com.elearnna.www.wififingerprint.view.StoredFilesView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 10/4/2017.
 */

public class StoredFilesPresenterImplementer implements StoredFilesPresenter, LoaderManager.LoaderCallbacks<List<File>> {

    private StoredFilesView storedFilesView;
    private Context context;
    private LoaderManager loaderManager;
    private List<File> myFiles;

    public StoredFilesPresenterImplementer(LoaderManager loaderManager, Context context) {
        this.loaderManager = loaderManager;
        this.context = context;
    }

    @Override
    public void getStoredFilesList() {
        loaderManager.initLoader(1, null, this).forceLoad();
    }

    @Override
    public void setStoredFilesView(StoredFilesView view) {
        this.storedFilesView = view;
    }

    @Override
    public Loader<List<File>> onCreateLoader(int id, Bundle args) {
        myFiles = new ArrayList<>();
        return new StoredFilesLoader(context);
    }

    @Override
    public void onLoadFinished(Loader<List<File>> loader, List<File> data) {
        myFiles = data;
        storedFilesView.displayFilesList(myFiles);
    }

    @Override
    public void onLoaderReset(Loader<List<File>> loader) {

    }
}
