package com.elearnna.www.wififingerprint.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;

import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileStoringResultDF extends DialogFragment {
    public FileStoringResultDF() {
    }

    public static FileStoringResultDF newInstance(String title, boolean isFileCreated, String fullFileName) {
        FileStoringResultDF frag = new FileStoringResultDF();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putBoolean("isCreated", isFileCreated);
        args.putString("fullFileName", fullFileName);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");

        // Set a title to the dialog
        getDialog().setTitle(title);

        // Prevent closing the dialog when a user touches outside the dialog
        getDialog().setCanceledOnTouchOutside(false);

        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        View view = inflater.inflate(R.layout.saved_file_layout, container);
        ButterKnife.bind(this, view);
        return view;
    }
}
