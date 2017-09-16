package com.elearnna.www.wififingerprint.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.fragments.DeviceInfoFragment;

public class DeviceInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_info);

        // Add the device info fragment to the activity
        DeviceInfoFragment fragment = new DeviceInfoFragment();
        FragmentManager fragmentMananger = getSupportFragmentManager();
        fragmentMananger.beginTransaction().add(R.id.fragment_device_info, fragment).commit();
    }
}
