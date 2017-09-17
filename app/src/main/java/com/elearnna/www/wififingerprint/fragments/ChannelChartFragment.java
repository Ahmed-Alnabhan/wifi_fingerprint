package com.elearnna.www.wififingerprint.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.elearnna.www.wififingerprint.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChannelChartFragment extends Fragment {


    public ChannelChartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel_chart, container, false);
        return view;
    }

}
