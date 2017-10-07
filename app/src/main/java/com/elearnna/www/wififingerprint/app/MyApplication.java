package com.elearnna.www.wififingerprint.app;

import android.app.Application;
import android.os.StrictMode;

/**
 * Created by Ahmed on 10/7/2017.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
