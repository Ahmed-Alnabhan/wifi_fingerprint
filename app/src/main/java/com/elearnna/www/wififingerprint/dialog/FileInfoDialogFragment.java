package com.elearnna.www.wififingerprint.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.loader.FingerprintLoader;
import com.elearnna.www.wififingerprint.model.Fingerprint;
import com.elearnna.www.wififingerprint.provider.APContentProvider;
import com.elearnna.www.wififingerprint.service.APsFingerprintService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/25/2017.
 */

public class FileInfoDialogFragment extends DialogFragment{

    @BindView(R.id.txt_file_type_label)
    TextView txtFileTypeLabel;

    @BindView(R.id.txt_file_type_value)
    TextView txtFileType;

    @BindView(R.id.txt_file_name_label)
    TextView txtFileNameLabel;

    @BindView(R.id.et_file_name_value)
    MaterialEditText etFileName;

    @BindView(R.id.txt_file_location_label)
    TextView txtDefaultFileLocationLabel;

    @BindView(R.id.txt_default_file_location_value)
    TextView txtDefaultFileLocation;

    @BindView(R.id.txt_countdown_timer)
    TextView txtCountDownTimer;

    @BindView(R.id.btn_file_cancel)
    Button btnCancel;

    @BindView(R.id.btn_save_file)
    Button btnSave;


    private int duration;
    private long currentTick;
    private Fingerprint fingerprint;
    private LoaderManager.LoaderCallbacks<Fingerprint> mLoader;
    private String location;
    private Context mContext;
    private String selectedFileType;
    private String fileName;
    private com.elearnna.www.wififingerprint.model.File myFile;
    private Intent intent;

    public FileInfoDialogFragment() {
    }

    public static FileInfoDialogFragment newInstance(String title, int timer, String location) {
        FileInfoDialogFragment frag = new FileInfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("timer", timer);
        args.putString("location", location);
        frag.setArguments(args);
        return frag;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
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

        // Set TextViews style
        setTextViewsStyle();

        // Get the default file location from the sharedpreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String defaultFileLocation = sharedPreferences.getString(Constants.DEFAULT_DIRECTORY_PATH, "");

        // Set the default file location
        txtDefaultFileLocation.setText(defaultFileLocation.toString());

        // Create the intent od the service
        intent = new Intent(getActivity(), APsFingerprintService.class);

        // Create new instance of file
        myFile = new com.elearnna.www.wififingerprint.model.File();

        // disable the save button
        disableSaveButton();

        // Get app context
        mContext = getActivity().getApplicationContext();

        // disable the save button unless the file name edit text is not empty
        etFileName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() != 0){
                    enableSaveButton();
                } else {
                    disableSaveButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Get the current location
        location = getArguments().getString("location");

        // Define the loader
        mLoader = new LoaderManager.LoaderCallbacks<Fingerprint>() {
            @Override
            public Loader<Fingerprint> onCreateLoader(int id, Bundle args) {
                fingerprint = new Fingerprint();
                return new FingerprintLoader(getActivity(), location);
            }

            @Override
            public void onLoadFinished(Loader<Fingerprint> loader, Fingerprint data) {
                fingerprint = data;
            }

            @Override
            public void onLoaderReset(Loader<Fingerprint> loader) {

            }
        };

        hideControls();
        if (savedInstanceState == null) {
            duration = getArguments().getInt("timer");
        } else {
            duration = (int)savedInstanceState.getLong("currentTick");
        }
        intent.putExtra("duration", getArguments().getInt("timer"));
        intent.putExtra("location", location);
        mContext.startService(intent);
        //startTimer();
        CounterAsyncTask at = new CounterAsyncTask();
        at.execute(duration);

        // Set OnClickListener for the cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        // Set OnClickListener for the Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileName = etFileName.getText().toString();
                dismiss();
                boolean fileCreatedSuccessfully = createFile(fingerprint);
                showFileSavingResultDialog(fileCreatedSuccessfully);

                // If the file is created successfully, save info in the database
                if (fileCreatedSuccessfully) {
                    saveFileInfoInDB();
                }

            }
        });
        return view;
    }


//    private void startTimer() {
//        txtCountDownTimer.setVisibility(View.VISIBLE);
//        //readAPInfo();
//        new CountDownTimer(duration * Constants.ONE_SECOND, Constants.ONE_SECOND) {
//            @Override
//            public void onTick(long l) {
//                currentTick = l / Constants.ONE_SECOND;
//                txtCountDownTimer.setText(String.valueOf(currentTick));
//            }
//
//            @Override
//            public void onFinish() {
//                txtCountDownTimer.setVisibility(View.GONE);
//                showControls();
//                readAPInfoFromDB(location);
//                if (isAdded()) {
//                    getLoaderManager().initLoader(1, null, mLoader).forceLoad();
//                }
//            }
//        }.start();
//
//    }

    private void hideControls() {
        txtFileTypeLabel.setVisibility(View.GONE);
        txtFileType.setVisibility(View.GONE);
        txtFileNameLabel.setVisibility(View.GONE);
        etFileName.setVisibility(View.GONE);
        txtDefaultFileLocationLabel.setVisibility(View.GONE);
        txtDefaultFileLocation.setVisibility(View.GONE);
        disableSaveButton();
    }

    private void showControls() {
        txtFileTypeLabel.setVisibility(View.VISIBLE);
        txtFileType.setVisibility(View.VISIBLE);
        txtFileNameLabel.setVisibility(View.VISIBLE);
        etFileName.setVisibility(View.VISIBLE);
        txtDefaultFileLocationLabel.setVisibility(View.VISIBLE);
        txtDefaultFileLocation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("currentTick",currentTick);
    }

    private void showFileSavingResultDialog(boolean isFileCreatedSuccessfully) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        String filePath = sp.getString(Constants.DEFAULT_DIRECTORY_PATH, Constants.DEFAULT_DIRECTORY_PATH);
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FileStoringResultDF fileInfoDialogFragment = FileStoringResultDF.newInstance("File Saving Result", isFileCreatedSuccessfully, fileName, filePath);
        fileInfoDialogFragment.show(fm, "saving result");
    }

    private void readAPInfoFromDB(String location){
        String selection = APContentProvider.location + " LIKE ?";
        String[] selectionArgs = {location};
        Cursor cursor = mContext.getContentResolver().query(Constants.APS_CONTENT_URL, null, selection, selectionArgs, null);
        if (cursor.moveToFirst()) {
            try {

            } finally {
                cursor.close();
            }
        }
    }

    private boolean createFile(Fingerprint fp){
        selectedFileType = txtFileType.getText().toString();
        boolean fileCreated = false;
        if (selectedFileType.equals(Constants.JSON_TYPE)){
            fileCreated = createJSONFile(fp);
        } else if (selectedFileType.equals(Constants.XML_TYPE)){
            fileCreated = createXMLFile(fp);
        } else if (selectedFileType.equals(Constants.CSV_TYPE)){
            fileCreated = createCSVFile(fp);
        }
        return fileCreated;
    }
    
    private boolean createJSONFile(Fingerprint fp){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filePath = sharedPreferences.getString(Constants.DEFAULT_DIRECTORY_PATH, Constants.DEFAULT_DIRECTORY_PATH);

        // Set the file location filed in the File object
        if (!filePath.isEmpty() && filePath != null) {
            myFile.setLocation(filePath);
        }
        // Set the file name field in the File object
        if (!fileName.isEmpty() && fileName != null) {
            myFile.setName(fileName);
        }
        // Set the file type field in the file
        myFile.setType(Constants.JSON_TYPE);

        Gson gson = new GsonBuilder()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.IDENTITY)
                .serializeNulls()
                .create();
        if (fp != null) {
            String jsonString = gson.toJson(fp);
            File fullFileName = new File(filePath + "/" + fileName + ".json");
            Writer fileWriter = null;
            try {
                fileWriter = new FileWriter(fullFileName, true);
                fileWriter.write(jsonString);
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return true;
        } else {
            return false;
        }
    }

    private boolean createXMLFile(Fingerprint fp){
        return true;
    }

    private boolean createCSVFile(Fingerprint fp){
        return true;
    }

    private void disableSaveButton() {
        btnSave.setEnabled(false);
        btnSave.setClickable(false);
    }

    private void enableSaveButton() {
        btnSave.setEnabled(true);
        btnSave.setClickable(true);
    }

    private void saveFileInfoInDB() {
        // Create a new map of file values
        ContentValues fileValues = new ContentValues();
        fileValues.put(APContentProvider.file_location, myFile.getLocation());
        fileValues.put(APContentProvider.type, myFile.getType());
        fileValues.put(APContentProvider.name, myFile.getName());

        Uri uri = getActivity().getContentResolver().insert(Constants.FILES_CONTENT_URL, fileValues);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setTextViewsStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getActivity());

        // Set the style of the SSID based on the app theme
        Utils.setTextViewStyle(getContext(), txtFileTypeLabel, bold_font, "Regular");

        // Set the style of the IP address based on the app theme
        Utils.setTextViewStyle(getContext(), txtFileType, bold_font, "Regular");

        // Set the style of the Connection status based on the app theme
        Utils.setTextViewStyle(getContext(), txtFileNameLabel, bold_font, "Regular");

        // Set the style of the Channel based on the app theme
        Utils.setTextViewStyle(getContext(), txtDefaultFileLocationLabel, bold_font, "Regular");

        // Set the style of the MAC based on the app theme
        Utils.setTextViewStyle(getContext(), txtDefaultFileLocation, bold_font, "Regular");

        // Set the font type of the file name edit text
        etFileName.setTypeface(bold_font);
        etFileName.setTextColor(ContextCompat.getColor(getContext(), R.color.wifi_light_gray));
    }

    private class CounterAsyncTask extends AsyncTask<Integer, Long, Void> {


        @Override
        protected Void doInBackground(Integer... params) {
            startTimer(params[0]);
            return null;
        }

        @Override
        protected void onProgressUpdate(Long... values) {
            txtCountDownTimer.setText(String.valueOf(values[0]));
        }

        private void startTimer(final Integer duration) {
            txtCountDownTimer.setVisibility(View.VISIBLE);
            //readAPInfo();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
            new CountDownTimer(duration * Constants.ONE_SECOND, Constants.ONE_SECOND) {
                @Override
                public void onTick(long l) {
                    currentTick = l / Constants.ONE_SECOND;
                    //txtCountDownTimer.setText(String.valueOf(currentTick));
                    publishProgress(currentTick);
                }

                @Override
                public void onFinish() {
                    txtCountDownTimer.setVisibility(View.GONE);
                    showControls();
                    readAPInfoFromDB(location);
                    if (isAdded()) {
                        getLoaderManager().initLoader(1, null, mLoader).forceLoad();
                    }
                }
            }.start();
                }
            });
        }
    }

}
