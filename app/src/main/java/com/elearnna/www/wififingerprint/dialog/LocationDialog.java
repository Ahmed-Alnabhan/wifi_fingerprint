package com.elearnna.www.wififingerprint.dialog;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.adapter.SpinnerAdapter;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.fragments.APsListFragment;
import com.elearnna.www.wififingerprint.model.Locator;
import com.rengwuxian.materialedittext.MaterialEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by Ahmed on 9/23/2017.
 */

public class LocationDialog extends DialogFragment implements View.OnClickListener{

    @BindView(R.id.scanning_duration_label)
    TextView scanningDurationLabel;

    @BindView(R.id.location_name_label)
    TextView locationNameLabel;

    @BindView(R.id.scanning_duration_spinner)
    MaterialSpinner spinScanningDuration;

    @BindView(R.id.location_name_value)
    MaterialEditText etLocationName;

    @BindView(R.id.cancel_location_dialog)
    Button btnCancelLocationDialog;

    @BindView(R.id.save_location_dialog)
    Button btnSaveLocationDialog;

    private LocationDuration locDuration;
    private Locator locator;
    private String[] spinnerItems;

    public LocationDialog() {

    }

    public static LocationDialog newInstance(String title) {
        LocationDialog frag = new LocationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        getDialog().setTitle(title);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
        View view = inflater.inflate(R.layout.location_dialog, container);
        ButterKnife.bind(this, view);
        locDuration = new APsListFragment();
        locator = new Locator();
        spinnerItems = new String[]{"5","10","15","20","25"};
        String defaultSpinnerValue = spinnerItems[0];

        // Set TextViews style
        setViewsStyle();

        // Set EditText style
        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(),"fonts/Quicksand-Regular.otf");
        etLocationName.setTypeface(tf,Typeface.BOLD);

        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        int spinnerPosition = adapter.getPosition(defaultSpinnerValue);
        spinScanningDuration.setSelection(spinnerPosition, true);
        spinScanningDuration.setAdapter(adapter);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Disable the save button when the view is created
        disableSaveButton();

        // disable the save button unless the file name edit text is not empty
        etLocationName.addTextChangedListener(new TextWatcher() {
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


        btnCancelLocationDialog.setOnClickListener(this);
        btnSaveLocationDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_location_dialog:
                if (spinScanningDuration.getSelectedItem() != null) {
                    String selectedItem = spinScanningDuration.getSelectedItem().toString();
                    locator.setDuration(Integer.parseInt(selectedItem));
                    if (!(etLocationName.getText().toString().isEmpty()) && etLocationName.getText() != null) {
                        locator.setLocation(etLocationName.getText().toString());
                    }
                    locDuration.getLocationandDuration(locator);
                    dismiss();
                    showFileInfoDialog();
                } else {
                    Toast.makeText(getContext(), getResources().getString(R.string.duration_message), Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.cancel_location_dialog:
                dismiss();
                break;
        }
    }

    private void showFileInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FileInfoDialogFragment fileInfoDialogFragment = FileInfoDialogFragment.newInstance("Enter File Info", locator.getDuration(), locator.getLocation());
        fileInfoDialogFragment.show(fm, "timer");
    }

    private void enableSaveButton() {
        btnSaveLocationDialog.setEnabled(true);
        btnSaveLocationDialog.setClickable(true);
    }

    private void disableSaveButton() {
        btnSaveLocationDialog.setEnabled(false);
        btnSaveLocationDialog.setEnabled(false);
    }

    /**
     * This method sets style to the views
     */

    private void setViewsStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getContext());
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(getContext());

        // Set the style of the scanning duration label TextView
        Utils.setTextViewStyle(getContext(), scanningDurationLabel, bold_font, "Regular");

        // Set the style of the location name label TextView
        Utils.setTextViewStyle(getContext(), locationNameLabel, bold_font, "Regular");
    }
}
