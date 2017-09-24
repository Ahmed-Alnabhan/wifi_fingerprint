package com.elearnna.www.wififingerprint.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
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

public class LocationDialog extends Dialog implements View.OnClickListener{
    private Activity activity;
    private Dialog dialog;
    private LocationDuration locDuration;
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

    public LocationDialog(@NonNull Activity activity) {
        super(activity);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.location_dialog);
        ButterKnife.bind(this);
        spinnerItems = new Integer[]{5,10,15,20,25};
        if(spinScanningDuration != null && spinScanningDuration.getSelectedItem() !=null ) {
            ArrayAdapter<Integer> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, spinnerItems);
            spinScanningDuration.setAdapter(adapter);
            locator.setDuration(Integer.parseInt(spinScanningDuration.getSelectedItem().toString()));
        }
        locDuration = new APsListFragment();
        locator = new Locator();

        if (!(etLocationName.getText().toString().isEmpty()) && etLocationName.getText() != null){
            locator.setLocation(etLocationName.getText().toString());
        }

        btnCancelLocationDialog.setOnClickListener(this);
        btnSaveLocationDialog.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_location_dialog:
                locDuration.getLocationandDuration(locator);
                break;
            case R.id.cancel_location_dialog:
                dismiss();
                break;
        }
    }
}
