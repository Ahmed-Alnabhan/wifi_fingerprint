package com.elearnna.www.wififingerprint.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.elearnna.www.wififingerprint.app.Constants;
import com.elearnna.www.wififingerprint.database.DBHelper;
import com.elearnna.www.wififingerprint.database.DatabaseContract;

import java.util.HashMap;

/**
 * Created by Ahmed on 9/21/2017.
 */

public class APContentProvider extends ContentProvider {

    // Define fields of AP table
    public static final String location = "location";
    public static final String ssid = "ssid";
    public static final String rssi = "rssi";
    public static final String frequency = "frequency";
    public static final String macAddress = "mac_address";
    public static final String channel = "channel";
    public static final String isLocked = "is_locked";
    public static final String apManufacturer = "manufacturer";
    public static final String securityProtocol = "security_protocol";
    public static final String ipAddress = "ip_address";
    public static final String time = "time";

    // Define fields of Device table
    public static final String deviceManufacturer = "manufacturer";
    public static final String model = "model";
    public static final String brand = "brand";
    public static final String device = "device";
    public static final String product = "product";
    public static final String os = "os";
    public static final String osVersion = "os_verion";
    public static final String apiLevel = "api_level";

    // Define fields of File table
    public static final String name = "name";
    public static final String file_location = "location";
    public static final String type = "type";

    // Define the uriCode
    public static final int uriAP = 1;
    public static final int uriFile = 2;
    public static final int uriDevice = 3;

    private static HashMap<String, String> values;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "apscp", uriAP);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "filescp", uriFile);
        uriMatcher.addURI(Constants.PROVIDER_NAME, "devicecp", uriDevice);
    }

    private SQLiteDatabase mDb;
    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        mDb = dbHelper.getWritableDatabase();
        return mDb != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        int myUri = uriMatcher.match(uri);
        switch (myUri){
            case uriAP:
                queryBuilder.setTables(DatabaseContract.APEntry.TABLE_AP);
                queryBuilder.setProjectionMap(values);
                break;
            case uriFile:
                queryBuilder.setTables(DatabaseContract.FileEntry.TABLE_FILE);
                queryBuilder.setProjectionMap(values);
                break;
            case uriDevice:
                queryBuilder.setTables(DatabaseContract.DeviceEntry.TABLE_DEVICE);
                queryBuilder.setProjectionMap(values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        Cursor cursor = queryBuilder.query(mDb, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getApplicationContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        switch (uriMatcher.match(uri)) {
            case uriAP:
                return "vnd.android.cursor.dir/apscp";
            case uriFile:
                return "vnd.android.cursor.dir/filescp";
            case uriDevice:
                return "vnd.android.cursor.dir/devicecp";
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        Uri myUri = null;
        switch (uriMatcher.match(uri)) {
            case uriAP:
                long recordID1 = mDb.insert(DatabaseContract.APEntry.TABLE_AP, null, contentValues);
                if (recordID1 > 0) {
                    myUri = ContentUris.withAppendedId(Constants.APS_CONTENT_URL, recordID1);
                    getContext().getContentResolver().notifyChange(myUri, null);
                }
                break;
            case uriFile:
                long recordID2 = mDb.insert(DatabaseContract.FileEntry.TABLE_FILE, null, contentValues);
                if (recordID2 > 0) {
                    myUri = ContentUris.withAppendedId(Constants.FILES_CONTENT_URL, recordID2);
                    getContext().getContentResolver().notifyChange(myUri, null);
                }
                break;
            case uriDevice:
                long recordID3 = mDb.insert(DatabaseContract.DeviceEntry.TABLE_DEVICE, null, contentValues);
                if (recordID3 > 0) {
                    myUri = ContentUris.withAppendedId(Constants.DEVICE_CONTENT_URL, recordID3);
                    getContext().getContentResolver().notifyChange(myUri, null);
                }
                break;
            default:throw new SQLException("Failed to insert row into " + uri);
        }
        return myUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsDeleted;
        switch(uriMatcher.match(uri)){
            case uriAP:
                rowsDeleted = mDb.delete(DatabaseContract.APEntry.TABLE_AP, selection, selectionArgs);
                break;
            case uriDevice:
                rowsDeleted = mDb.delete(DatabaseContract.DeviceEntry.TABLE_DEVICE, selection, selectionArgs);
                break;
            case uriFile:
                rowsDeleted = mDb.delete(DatabaseContract.FileEntry.TABLE_FILE, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int rowsUpdated;
        switch(uriMatcher.match(uri)){
            case uriAP:
                rowsUpdated = mDb.update(DatabaseContract.APEntry.TABLE_AP, contentValues, selection, selectionArgs);
                break;
            case uriDevice:
                rowsUpdated = mDb.update(DatabaseContract.DeviceEntry.TABLE_DEVICE, contentValues, selection, selectionArgs);
                break;
            case uriFile:
                rowsUpdated = mDb.update(DatabaseContract.FileEntry.TABLE_FILE, contentValues, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
