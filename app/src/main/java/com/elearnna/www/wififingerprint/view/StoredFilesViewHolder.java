package com.elearnna.www.wififingerprint.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.model.File;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 10/4/2017.
 */

public class StoredFilesViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.txt_stored_file_name)
    TextView txtFileName;

    @BindView(R.id.img_folder_location)
    ImageView imgFolderLocation;

    @BindView(R.id.img_share_file)
    ImageView imgShareStoredFile;

    @BindView(R.id.img_delete_file)
    ImageView imgDeleteStoredFile;

    private List<File> filesList;

    public StoredFilesViewHolder(View itemView, List<File> myFiles) {
        super(itemView);
        this.filesList = myFiles;
        ButterKnife.bind(this, itemView);
    }

    public TextView getTxtFileName() {
        return txtFileName;
    }

    public ImageView getImgFolderLocation() {
        return imgFolderLocation;
    }

    public ImageView getImgShareStoredFile() {
        return imgShareStoredFile;
    }

    public ImageView getImgDeleteStoredFile() {
        return imgDeleteStoredFile;
    }
}
