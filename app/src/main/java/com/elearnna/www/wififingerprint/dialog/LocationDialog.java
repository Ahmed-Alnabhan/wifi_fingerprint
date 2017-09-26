package com.elearnna.www.wififingerprint.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.fragments.APsListFragment;
import com.elearnna.www.wififingerprint.model.Locator;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Ahmed on 9/23/2017.
 */

public class LocationDialog extends DialogFragment implements View.OnClickListener{
    
    private LocationDuration locDuration, cowntdownTimer;
    private Locator locator;
    private Integer[] spinnerItems;

    @BindView(R.id.scanning_duration_spinner)
    Spinner spinScanningDuration;

    @BindView(R.id.location_name_value)
    EditText etLocationName;

    @BindView(R.id.cancel_location_dialog)
    Button btnCancelLocationDialog;

    @BindView(R.id.save_location_dialog)
    Button btnSaveLocationDialog;
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
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        locDuration = new APsListFragment();
        locator = new Locator();
        spinnerItems = new Integer[]{5,10,15,20,25};
        Integer defaultSpinnerValue = spinnerItems[0];

        ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
        int spinnerPosition = adapter.getPosition(defaultSpinnerValue);
        spinScanningDuration.setSelection(spinnerPosition);
        spinScanningDuration.setAdapter(adapter);

        btnCancelLocationDialog.setOnClickListener(this);
        btnSaveLocationDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_location_dialog:
                locator.setDuration(Integer.parseInt(spinScanningDuration.getSelectedItem().toString()));
                if (!(etLocationName.getText().toString().isEmpty()) && etLocationName.getText() != null){
                    locator.setLocation(etLocationName.getText().toString());
                }
                locDuration.getLocationandDuration(locator);
                dismiss();
                showFileInfoDialog();
                break;
            case R.id.cancel_location_dialog:
                dismiss();
                break;
        }
    }

    private void showFileInfoDialog() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FileInfoDialogFragment fileInfoDialogFragment = FileInfoDialogFragment.newInstance("Enter File Info", locator.getDuration());
        fileInfoDialogFragment.show(fm, "timer");
    }
}
