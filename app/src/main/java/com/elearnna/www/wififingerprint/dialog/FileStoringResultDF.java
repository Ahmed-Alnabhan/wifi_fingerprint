package com.elearnna.www.wififingerprint.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileStoringResultDF extends DialogFragment {

    @BindView(R.id.txt_file_saved_successfully)
    TextView txtFileSavedSuccessfully;

    @BindView(R.id.txt_file_saved_failed)
    TextView getTxtFileSavedFailed;

    @BindView(R.id.btn_delete)
    ImageView btnDelete;

    @BindView(R.id.btn_share)
    ImageView btnShare;

    @BindView(R.id.btn_ok)
    Button btnOK;

    public FileStoringResultDF() {
    }

    public static FileStoringResultDF newInstance(String title, boolean isFileCreated, String fullFileName, String fileLocation) {
        FileStoringResultDF frag = new FileStoringResultDF();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("isCreated", isFileCreated);
        args.putString("fullFileName", fullFileName);
        args.putString("fileLocation", fileLocation);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        boolean isCreated = getArguments().getBoolean("isCreated");
        final String fileName = getArguments().getString("fullFileName");
        final String filePath = getArguments().getString("fileLocation");

        // Set a title to the dialog
        getDialog().setTitle(title);

        // Prevent closing the dialog when a user touches outside the dialog
        getDialog().setCanceledOnTouchOutside(false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        View view = inflater.inflate(R.layout.saved_file_layout, container);
        ButterKnife.bind(this, view);

        // Create file path

        final java.io.File fileFullPath = new java.io.File(filePath + "/" + fileName + ".json");

        if (isCreated) {
            showFileActions();
        } else {
            hideFileActions();
        }

        // Implement the share button click listener
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.shareFile(fileFullPath, getContext());
            }
        });

        // Implement the delete button click listener
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog deleteDialog = Utils.showDeleteDialog(fileFullPath, fileName, getContext());
                deleteDialog.show();
            }
        });

        // Implement the OK button click listener
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;
    }

    /**
     * This method shows the actions (share and delete) and a message
     * telling the user that the file has been created successfully
     */
    private void showFileActions() {
        txtFileSavedSuccessfully.setVisibility(View.VISIBLE);
        getTxtFileSavedFailed.setVisibility(View.GONE);
        btnDelete.setVisibility(View.VISIBLE);
        btnShare.setVisibility(View.VISIBLE);
    }

    /**
     * This method hides the actions buttons and tells the user that the
     * process of creating the file iss failed
     */
    private void hideFileActions() {
        txtFileSavedSuccessfully.setVisibility(View.GONE);
        getTxtFileSavedFailed.setVisibility(View.VISIBLE);
        btnDelete.setVisibility(View.GONE);
        btnShare.setVisibility(View.GONE);
    }
}
