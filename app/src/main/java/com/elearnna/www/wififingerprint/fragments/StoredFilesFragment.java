package com.elearnna.www.wififingerprint.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.adapter.StoredFilesAdapter;
import com.elearnna.www.wififingerprint.model.File;
import com.elearnna.www.wififingerprint.presenter.StoredFilesPresenter;
import com.elearnna.www.wififingerprint.presenter.StoredFilesPresenterImplementer;
import com.elearnna.www.wififingerprint.view.StoredFilesView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class StoredFilesFragment extends Fragment implements StoredFilesView{

    @BindView(R.id.rv_stored_files_list)
    RecyclerView rvStoredFilesList;

    @BindView(R.id.stored_files_loading_pb)
    ProgressBar pbStoredFilesList;

    private StoredFilesPresenter storedFilesPresenter;
    private LoaderManager loaderManager;

    public StoredFilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stored_files, container, false);
        ButterKnife.bind(this, view);
        loaderManager = getLoaderManager();
        rvStoredFilesList.setLayoutManager(new LinearLayoutManager(getContext()));
        storedFilesPresenter = new StoredFilesPresenterImplementer(loaderManager, getContext());
        storedFilesPresenter.setStoredFilesView(this);
        storedFilesPresenter.getStoredFilesList();
        return view;
    }

    @Override
    public void displayFilesList(List<File> filesList) {
        if (filesList != null) {
            rvStoredFilesList.setAdapter(new StoredFilesAdapter(filesList, getContext()));
            rvStoredFilesList.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void displayErrorMessage() {

    }

    @Override
    public void showAPsLoading() {

    }

    @Override
    public void hideAPsLoading() {

    }
}
