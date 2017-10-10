package com.elearnna.www.wififingerprint.dialog;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.loader.FingerprintLoader;
import com.elearnna.www.wififingerprint.model.Fingerprint;
import com.elearnna.www.wififingerprint.provider.APContentProvider;
import com.elearnna.www.wififingerprint.service.APsFingerprintService;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

    @BindView(R.id.txt_file_type_value)
    TextView txtFileType;

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


    private int duration;
    private long currentTick;
    private Fingerprint fingerprint;
    private LoaderManager.LoaderCallbacks<Fingerprint> mLoader;
    private String location;
    private Context mContext;
    private String selectedFileType;
    private String fileName;
    private String fullFileName;
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

        disableControls();
        if (savedInstanceState == null) {
            duration = getArguments().getInt("timer");
        } else {
            duration = (int)savedInstanceState.getLong("currentTick");
        }
        intent.putExtra("duration", getArguments().getInt("timer"));
        intent.putExtra("location", location);
        mContext.startService(intent);
        startTimer();

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


    private void startTimer() {
        txtCountDownTimer.setVisibility(View.VISIBLE);
        //readAPInfo();
        new CountDownTimer(duration * Constants.ONE_SECOND, Constants.ONE_SECOND) {
            @Override
            public void onTick(long l) {
                currentTick = l / Constants.ONE_SECOND;
                txtCountDownTimer.setText(String.valueOf(currentTick));
            }

            @Override
            public void onFinish() {
                txtCountDownTimer.setVisibility(View.GONE);
                enableControls();
                readAPInfoFromDB(location);
                if (isAdded()) {
                    getLoaderManager().initLoader(1, null, mLoader).forceLoad();
                }
            }
        }.start();

    }

    private void disableControls() {
        txtFileType.setEnabled(false);
        etFileName.setEnabled(false);
        txtDefaultFileLocation.setEnabled(false);
        btnBrowse.setEnabled(false);
        btnBrowse.setClickable(false);
        disableSaveButton();
    }

    private void enableControls() {
        txtFileType.setEnabled(true);
        etFileName.setEnabled(true);
        txtDefaultFileLocation.setEnabled(true);
        btnBrowse.setEnabled(true);
        btnBrowse.setClickable(true);
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
                while (cursor.moveToNext()) {
                    Log.i("APS_RECORDS", cursor.getString(cursor.getColumnIndex("mac_address")));
                }
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
            Log.i("FingerPrint JSON: ", jsonString);
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
}
