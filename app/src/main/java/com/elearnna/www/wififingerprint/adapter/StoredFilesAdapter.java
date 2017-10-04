package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.model.File;
import com.elearnna.www.wififingerprint.view.StoredFilesViewHolder;

import java.util.List;

/**
 * Created by Ahmed on 10/4/2017.
 */

public class StoredFilesAdapter extends RecyclerView.Adapter<StoredFilesViewHolder> {

    private List<File> filesList;
    private Context context;
    private File file;

    public StoredFilesAdapter(List<File> filesList, Context context) {
        this.context = context;
        this.filesList = filesList;
    }

    @Override
    public StoredFilesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new StoredFilesViewHolder(inflater.inflate(R.layout.file_info_layout, parent, false), filesList);
    }

    @Override
    public void onBindViewHolder(StoredFilesViewHolder holder, int position) {
        file = filesList.get(position);
        String fileName = file.getName();
        holder.getTxtFileName().setText(fileName);
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }
}
