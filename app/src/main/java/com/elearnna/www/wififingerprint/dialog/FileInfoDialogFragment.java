package com.elearnna.www.wififingerprint.dialog;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileInfoDialogFragment extends DialogFragment {private int duration;

    long currentTick;
    @BindView(R.id.spinner_file_type_value)
    Spinner spinnerSaveAs;

    @BindView(R.id.et_file_name_value)
    EditText etFileName;

    @BindView(R.id.txt_default_file_location_value)
    TextView txtDefaultFileLocation;

    @BindView(R.id.btn_browse)
    Button btnBrowse;


    @BindView(R.id.txt_countdown_timer)
    TextView txtCountDownTimer;

    @BindView(R.id.btn_file_cancel)
    Button btnCancel;

    @BindView(R.id.btn_save_file)
    Button btnSave;

    public FileInfoDialogFragment() {
    }

    public static FileInfoDialogFragment newInstance(String title, int timer) {
        FileInfoDialogFragment frag = new FileInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("timer", timer);
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
        View view = inflater.inflate(R.layout.file_properties_layout, container);
        ButterKnife.bind(this, view);
        disableControls();
        if (savedInstanceState == null) {
            duration = getArguments().getInt("timer");
        } else {
            duration = (int)savedInstanceState.getLong("currentTick");
        }
        startTimer();
        return view;
    }

    private void startTimer() {
        txtCountDownTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(duration * 1000, 1000) {
            @Override
            public void onTick(long l) {
                currentTick = l / 1000;
                txtCountDownTimer.setText(String.valueOf(currentTick));
            }

            @Override
            public void onFinish() {
                txtCountDownTimer.setVisibility(View.GONE);
                enableControls();
            }
        }.start();

    }

    private void disableControls() {
        spinnerSaveAs.setEnabled(false);
        etFileName.setEnabled(false);
        txtDefaultFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnBrowse.setClickable(false);
        btnSave.setEnabled(false);
        btnSave.setClickable(false);
    }

    private void enableControls() {
        spinnerSaveAs.setEnabled(true);
        etFileName.setEnabled(true);
        txtDefaultFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnBrowse.setClickable(true);
        btnSave.setEnabled(true);
        btnSave.setClickable(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentTick",currentTick);
    }
}
