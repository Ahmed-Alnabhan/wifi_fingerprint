package com.elearnna.www.wififingerprint.fragments;


import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.adapter.StoredFilesAdapter;
import com.elearnna.www.wififingerprint.app.Constants;
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

    @BindView(R.id.txt_no_stored_files)
    TextView txtNoStoredFilesMessage;

    @BindView(R.id.stored_files_header)
    ViewGroup vgFilesHesder;

    private StoredFilesPresenter storedFilesPresenter;
    private LoaderManager loaderManager;
    private Bundle savedInstance, state;
    private StoredFilesAdapter storedFilesAdapter;

    public StoredFilesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stored_files, container, false);
        ButterKnife.bind(this, view);

        // Show APs loading progress bar
        showAPsLoading();

        savedInstance = savedInstanceState;
        state = new Bundle();

        loaderManager = getLoaderManager();
        rvStoredFilesList.setLayoutManager(new LinearLayoutManager(getContext()));
        storedFilesPresenter = new StoredFilesPresenterImplementer(loaderManager, getContext());
        storedFilesPresenter.setStoredFilesView(this);
        storedFilesPresenter.getStoredFilesList();
        return view;
    }

    @Override
    public void displayFilesList(List<File> filesList) {
        if (filesList.size() > 0) {
            vgFilesHesder.setVisibility(View.VISIBLE);
            rvStoredFilesList.setVisibility(View.VISIBLE);
            hideNoFilesMessage();
            onSaveInstanceState(state);
            storedFilesAdapter= new StoredFilesAdapter(filesList, getContext());
            rvStoredFilesList.setAdapter(storedFilesAdapter);
            rvStoredFilesList.getAdapter().notifyDataSetChanged();
            onViewStateRestored(state);

        } else {
            displayNoFilesMessage();
            vgFilesHesder.setVisibility(View.GONE);
            rvStoredFilesList.setVisibility(View.GONE);
            //onSaveInstanceState(state);
            //storedFilesAdapter= new StoredFilesAdapter(filesList, getContext());
            //rvStoredFilesList.setAdapter(storedFilesAdapter);
            //rvStoredFilesList.getAdapter().notifyDataSetChanged();
            //onViewStateRestored(state);
        }

        // Hide APs loading progress bar
        hideAPsLoading();
    }

    @Override
    public void displayNoFilesMessage() {
        txtNoStoredFilesMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoFilesMessage() {
        txtNoStoredFilesMessage.setVisibility(View.GONE);
    }

    @Override
    public void showAPsLoading() {
        pbStoredFilesList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideAPsLoading() {
        pbStoredFilesList.setVisibility(View.GONE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECYCLER_STATE, rvStoredFilesList.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null){
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(Constants.RECYCLER_STATE);
            rvStoredFilesList.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.i("DESTOYED::::" , "DESTROYED");
    }
}
