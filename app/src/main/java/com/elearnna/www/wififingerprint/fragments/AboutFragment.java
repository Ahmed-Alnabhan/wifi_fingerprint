package com.elearnna.www.wififingerprint.fragments;


import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    private String aboutApp;
    private String appVersion;
    private Bundle savedInstance;
    private int scrollState;

    @BindView(R.id.txt_about_app)
    TextView txtAboutApp;

    @BindView(R.id.txt_app_version)
    TextView txtAppVersion;

    @BindView(R.id.scroll_about_app_activity)
    ScrollView scrollView;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);
        savedInstance = savedInstanceState;
        // About app
        aboutApp = Constants.ABOUT_APP;
        if (Build.VERSION.SDK_INT >= 24){
            txtAboutApp.setText(Html.fromHtml(aboutApp,Html.FROM_HTML_MODE_LEGACY));
        } else {
            txtAboutApp.setText(Html.fromHtml(Constants.ABOUT_APP));
        }

        // App's version
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
            appVersion = pInfo.versionName;
            txtAppVersion.setText("Version: " + appVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(savedInstance != null){
            txtAppVersion.setText("");
            txtAboutApp.setText("");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("aboutAPP", String.valueOf(txtAboutApp.getText()));
        outState.putString("appVersion", String.valueOf(txtAppVersion.getText()));
        outState.putInt("scrollState", scrollView.getScrollY());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState != null) {
            aboutApp = savedInstanceState.getString("aboutAPP");
            appVersion = savedInstanceState.getString("appVersion");

            scrollView.setScrollY(savedInstanceState.getInt("scrollState"));
        }
    }
}
