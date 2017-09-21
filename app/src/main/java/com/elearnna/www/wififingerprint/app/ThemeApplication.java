package com.elearnna.www.wififingerprint.app;

import android.app.Application;
import android.content.Context;
import android.support.v7.preference.PreferenceManager;

import com.elearnna.www.wififingerprint.R;

/**
 * Created by Ahmed on 9/20/2017.
 */

public class ThemeApplication  extends Application {
    private static int themeId;
    private static String themeSetting;

    public static int getThemeId(){
        return themeId;
    }

    public static void changeTheme(Context context) {
        themeSetting = PreferenceManager.getDefaultSharedPreferences(context).getString("theme", "0");
        if(themeSetting.equals("0"))
            themeId = R.style.Theme_Material_Light;
        else
            themeId = R.style.Theme_Material_Dark;
    }
}
