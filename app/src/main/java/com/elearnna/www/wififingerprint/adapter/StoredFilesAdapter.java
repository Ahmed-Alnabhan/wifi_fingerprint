package com.elearnna.www.wififingerprint.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
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
        final String fileName = file.getName();
        final java.io.File filePath = new java.io.File(file.getLocation() + "/" + fileName + ".json");
        holder.getTxtFileName().setText(fileName);

        // Set file name style
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(context);
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(context);

        // Set the style of the file name title TextView
        Utils.setTextViewStyle(context, holder.getTxtFileName(), regular_font, Constants.LARGE_FONT);

        // Set OnClickListener of the browse button
        holder.getImgFolderLocation().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileContent(filePath);
            }
        });

        // Set OnClickListener of the Share button
        holder.getImgShareStoredFile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.shareFile(filePath, context);
            }
        });

        // Set OnClickListener of the Delete button
        holder.getImgDeleteStoredFile().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteDialog = Utils.showDeleteDialog(filePath, fileName, context);
                deleteDialog.show();
            }
        });
    }

    private void showFileContent(java.io.File filePath) {
        String pm = context.getApplicationContext().getPackageName();
        Uri selectedUri = FileProvider.getUriForFile(context, pm + ".provider.GenericFileProvider", filePath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(selectedUri, "text/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null){
        context.startActivity(intent);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.no_text_reader_installed_message), Toast.LENGTH_LONG).show();
            // if you reach this place, it means there is no any file
            // explorer app installed on your device
        }
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }
}
