package com.elearnna.www.wififingerprint.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.app.Utils;

public class APsList extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {


    private SharedPreferences sharedPreferences;
    private String theme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ////setupPreferences();
        //Utils.onActivityCreateSetTheme(APsList.this);

        super.onCreate(savedInstanceState);
        //setTheme(R.style.Theme_Material_Dark);
        setContentView(R.layout.activity_aps_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_chart) {
            Intent channelChartIntent = new Intent(this, ChannelChart.class);
            startActivity(channelChartIntent);
        } else if (id == R.id.nav_aps) {

        } else if (id == R.id.nav_files) {
            Intent storedFilesIntent = new Intent(this, StoredFiles.class);
            startActivity(storedFilesIntent);
        } else if (id == R.id.nav_device_info) {
            Intent deviceInfoIntent = new Intent(this, DeviceInfo.class);
            startActivity(deviceInfoIntent);
        } else if (id == R.id.nav_settings) {
            Intent settingsIntent = new Intent(this, Settings.class);
            startActivity(settingsIntent);
        } else if (id == R.id.nav_about) {
            Intent aboutIntent = new Intent(this, About.class);
            startActivity(aboutIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

}
