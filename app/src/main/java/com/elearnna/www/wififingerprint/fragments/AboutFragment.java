package com.elearnna.www.wififingerprint.fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.elearnna.www.wififingerprint.BuildConfig;
import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends Fragment {

    @BindView(R.id.scroll_about_app_activity)
    ScrollView aboutScrollView;

    @BindView(R.id.description_title)
    TextView txtDescriptionTitle;

    @BindView(R.id.description_body)
    TextView txtDescriptionBody;

    @BindView(R.id.icons_title)
    TextView txtIconsTitle;

    @BindView(R.id.icons_body)
    TextView txtIconsBody;

    @BindView(R.id.libraries_title)
    TextView txtLibrariesTitle;

    @BindView(R.id.libraries_body)
    TextView txtLibrariesBody;
    
    @BindView(R.id.txt_app_version)
    TextView  txtAppVersion;

    private int scrollPositionX;
    private int scrollPositionY;

    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        ButterKnife.bind(this, view);

        // Set the style of TextViews
        setTextViewStyle();
        
        // Get app version
        String appName = BuildConfig.VERSION_NAME;

        txtAppVersion.setText(getContext().getResources().getString(R.string.app_version_label) + appName);

        txtLibrariesBody.setText(Html.fromHtml(getString(R.string.libraries_body)));
        txtIconsBody.setText(Html.fromHtml(getString(R.string.icons_body)));

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("scrollPositionX",aboutScrollView.getScrollX());
        outState.putInt("scrollPositionY",aboutScrollView.getScrollY());
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            scrollPositionX = savedInstanceState.getInt("scrollPositionX");
            scrollPositionY = savedInstanceState.getInt("scrollPositionY");
            aboutScrollView.post(new Runnable() {
                @Override
                public void run() {
                    aboutScrollView.scrollTo(scrollPositionX, scrollPositionY);
                }
            });
        }

    }

    /**
     * This method sets style to the views
     */

    private void setTextViewStyle() {
        // set quicksand bold font
        Typeface bold_font = Utils.setQuicksandBoldFont(getContext());
        // set quicksand regular font
        Typeface regular_font = Utils.setQuicksandRegularFont(getContext());

        // Set the style of the description title TextView
        Utils.setTextViewStyle(getContext(), txtDescriptionTitle, regular_font, "Large");

        // Set the style of the description body TextView
        Utils.setTextViewStyle(getContext(), txtDescriptionBody, regular_font, "Regular");

        // Set the style of the libraries title TextView
        Utils.setTextViewStyle(getContext(), txtLibrariesTitle, regular_font, "Large");

        // Set the style of the libraries body TextView
        Utils.setTextViewStyle(getContext(), txtLibrariesBody, regular_font, "Regular");

        // Set the style of the icons title TextView
        Utils.setTextViewStyle(getContext(), txtIconsTitle, regular_font, "Large");

        // Set the style of the icons body TextView
        Utils.setTextViewStyle(getContext(), txtIconsBody, regular_font, "Regular");

        // Set the style of the app version TextView
        Utils.setTextViewStyle(getContext(), txtAppVersion, bold_font, "Large");
    }
}
