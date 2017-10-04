package com.elearnna.www.wififingerprint.loader;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.model.File;
import com.elearnna.www.wififingerprint.provider.APContentProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 10/4/2017.
 */

public class StoredFilesLoader extends AsyncTaskLoader<List<File>> {

    private List<File> filesList;
    private File file;

    public StoredFilesLoader(Context context) {
        super(context);
    }

    @Override
    public List<File> loadInBackground() {
        filesList = new ArrayList<>();
        Cursor cursor = getContext().getContentResolver().query(Constants.FILES_CONTENT_URL, null, null, null, null);
        if (cursor.moveToFirst()) {
            try {
                do {
                    file = new File();
                    file.setName(cursor.getString(cursor.getColumnIndex(APContentProvider.name)));
                    file.setType(cursor.getString(cursor.getColumnIndex(APContentProvider.type)));
                    file.setLocation(cursor.getString(cursor.getColumnIndex(APContentProvider.file_location)));
                    filesList.add(file);
                } while (cursor.moveToNext());
            } finally {
                cursor.close();
            }
        }
        return filesList;
    }
}
