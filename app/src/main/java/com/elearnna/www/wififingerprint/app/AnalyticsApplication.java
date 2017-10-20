package com.elearnna.www.wififingerprint.app;

import android.app.Application;

import com.elearnna.www.wififingerprint.R;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

/**
 * Created by Ahmed on 10/19/2017.
 */

public class AnalyticsApplication extends Application {
    private static GoogleAnalytics sAnalytics;
    private static Tracker sTracker;

    @Override
    public void onCreate() {
        super.onCreate();

        sAnalytics = GoogleAnalytics.getInstance(this);
    }


}
