package com.elearnna.www.wififingerprint.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.elearnna.www.wififingerprint.R;
import com.elearnna.www.wififingerprint.fragments.StoredFilesFragment;

public class StoredFiles extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stored_files);

        // Add storedFiles fragment to the activity
        StoredFilesFragment fragment = new StoredFilesFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().add(R.id.stored_files_fragment_container, fragment).commit();


    }
}
