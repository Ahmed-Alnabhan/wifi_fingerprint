package com.elearnna.www.wififingerprint.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.app.MyApplication;
import com.elearnna.www.wififingerprint.app.Utils;
import com.elearnna.www.wififingerprint.fragments.APsListFragment;
import com.elearnna.www.wififingerprint.fragments.AboutFragment;
import com.elearnna.www.wififingerprint.fragments.DeviceInfoFragment;
import com.elearnna.www.wififingerprint.fragments.SettingsFragment;
import com.elearnna.www.wififingerprint.fragments.StoredFilesFragment;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class APsList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private SharedPreferences sharedPreferences;
    private String theme;
    private boolean mTwoPane = false;
    private int selectedItem;
    private Tracker mTracker;
    private GoogleAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////setupPreferences();
       Utils.onActivityCreateSetTheme(APsList.this);

        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_Material_Dark);
        setContentView(R.layout.activity_aps_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        if (findViewById(R.id.drawer_layout) != null) {
            mTwoPane = false;
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();
        } else {
            mTwoPane = true;
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // insert detail fragment into detail container
        if (savedInstanceState == null) {
            APsListFragment aPsListFragment = new APsListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.aps_fragment_container, aPsListFragment)
                    .commit();
        } else {
            selectedItem = savedInstanceState.getInt(Constants.SELECTED_ITEM);
        }
        // Obtain the shared Tracker instance.
        MyApplication application = (MyApplication) getApplication();
        mTracker = application.getDefaultTracker();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(!mTwoPane) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_aps) {
            if (!mTwoPane) {
                Intent intent = new Intent(this, APsList.class);
                startActivity(intent);
            } else {
                APsListFragment aPsListFragment = new APsListFragment();
                transactFragment(aPsListFragment);
            }
        } else if (id == R.id.nav_files) {
            if (!mTwoPane) {
                Intent intent = new Intent(this, StoredFiles.class);
                startActivity(intent);
            } else {
                selectedItem = id;
                StoredFilesFragment fragment = new StoredFilesFragment();
                transactFragment(fragment);
            }
        } else if (id == R.id.nav_device_info) {
            if (!mTwoPane) {
                Intent intent = new Intent(this, DeviceInfo.class);
                startActivity(intent);
            } else {
                selectedItem = id;
                DeviceInfoFragment fragment = new DeviceInfoFragment();
                transactFragment(fragment);
            }
        } else if (id == R.id.nav_settings) {
            if (!mTwoPane) {
                Intent intent = new Intent(this, Settings.class);
                startActivity(intent);
            } else {
                selectedItem = id;
                SettingsFragment fragment = new SettingsFragment();
                transactFragment(fragment);
            }
        } else if (id == R.id.nav_about) {
            if (!mTwoPane) {
                Intent intent = new Intent(this, About.class);
                startActivity(intent);
            } else {
                selectedItem = id;
                AboutFragment fragment = new AboutFragment();
                transactFragment(fragment);
            }
        }
        if (!mTwoPane) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void transactFragment(Fragment fragment) {
        // insert detail fragment into detail container
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.aps_fragment_container, fragment)
                .commit();
    }

    private void setupPreferences(){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        theme = sharedPreferences.getString(getString(R.string.pref_theme_key), getResources().getString(R.string.light_value));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (theme.equals("dark")) {
            Utils.changeToTheme(APsList.this, 0);
        } else if (theme.equals("light")){
            Utils.changeToTheme(APsList.this, 1);
        }
        if(s.equals(getString(R.string.pref_theme_key))){
            Utils.onActivityCreateSetTheme(this);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(Constants.SELECTED_ITEM, selectedItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        selectedItem = savedInstanceState.getInt(Constants.SELECTED_ITEM);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("Image~" + Constants.AP_LIST_SCREEN_NAME);
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
