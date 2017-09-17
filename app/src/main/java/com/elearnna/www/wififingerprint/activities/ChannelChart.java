package com.elearnna.www.wififingerprint.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.fragments.ChannelChartFragment;

public class ChannelChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_chart);

        // Add channel chart fragment to the activity
        ChannelChartFragment fragment = new ChannelChartFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.fragment_cahnnel_chart, fragment).commit();
    }
}
