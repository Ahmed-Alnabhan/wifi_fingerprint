package com.elearnna.www.wififingerprint.fragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceCategory;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{


    public SettingsFragment() {}

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
        // Get the number of preferences and iterate through them
        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        //PreferenceScreen preferenceScreen = getPreferenceScreen();
        //int count = preferenceScreen.getPreferenceCount();
        PreferenceCategory pc = (PreferenceCategory) findPreference("configuration");
        int counter =pc.getPreferenceCount();

        for (int i = 0; i < counter; i++) {
            Preference preference = pc.getPreference(i);
            if (preference instanceof EditTextPreference) {
                String value = sharedPreferences.getString(preference.getKey(), sharedPreferences.getString(Constants.DEFAULT_DIRECTORY_PATH, Constants.DEFAULT_DIRECTORY_PATH));
                setPreferenceSummary(preference, value);
            }
        }
    }

    private void setPreferenceSummary (Preference p, String value) {
        if (p instanceof EditTextPreference) {
            EditTextPreference etPreference = (EditTextPreference) p;
                etPreference.setSummary(value);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        Preference preference = findPreference(s);
        if (preference != null ) {
            if (preference instanceof ListPreference) {
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
