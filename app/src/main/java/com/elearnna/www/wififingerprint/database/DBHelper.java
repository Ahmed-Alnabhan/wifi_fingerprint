package com.elearnna.www.wififingerprint.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.elearnna.www.wififingerprint.app.Constants;

/**
 * Created by Ahmed on 9/21/2017.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    // Create string query that creates the ap table
    private final String SQL_CREATE_AP_TABLE = "CREATE TABLE " +
            DatabaseContract.APEntry.TABLE_AP + " (" +
            DatabaseContract.APEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.APEntry.COLUMN_LOCATION + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_SSID + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_RSSI + " INTEGER, " +
            DatabaseContract.APEntry.COLUMN_FREQUENCY + " INTEGER, " +
            DatabaseContract.APEntry.COLUMN_MAC_ADDRESS + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_CHANNEL + " INTEGER, " +
            DatabaseContract.APEntry.COLUMN_IS_LOCKED + " INTEGER, " +
            DatabaseContract.APEntry.COLUMN_AP_MANUFACTURER + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_SECURITY_PROTOCOL + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_IP_ADDRESS + " TEXT, " +
            DatabaseContract.APEntry.COLUMN_TIME + " TEXT " +
            ");";

    // Create string query that creates the device table
    private final String SQL_CREATE_DEVICE_TABLE = "CREATE TABLE " +
            DatabaseContract.DeviceEntry.TABLE_DEVICE + " (" +
            DatabaseContract.DeviceEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.DeviceEntry.COLUMN_DEVICE_MANUFACTURER + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_MODEL + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_BRAND + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_DEVICE + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_PRODUCT + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_OS + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_OS_VERSION + " TEXT, " +
            DatabaseContract.DeviceEntry.COLUMN_API_LEVEL + " INTEGER " +
            ");";

    // Create string query that creates the file table
    private final String SQL_CREATE_FILE_TABLE = "CREATE TABLE " +
            DatabaseContract.FileEntry.TABLE_FILE + " (" +
            DatabaseContract.FileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.FileEntry.COLUMN_NAME + " TEXT, " +
            DatabaseContract.FileEntry.COLUMN_FILE_LOCATION + " TEXT, " +
            DatabaseContract.FileEntry.COLUMN_TYPE + " TEXT " +
            ");";


    private static final String DATABASE_ALTER_AP_1 = "ALTER TABLE "
            + DatabaseContract.APEntry.TABLE_AP + " ADD COLUMN " + DatabaseContract.APEntry.COLUMN_LOCATION + " TEXT;";

    private static final String DATABASE_DROP_AP_POSITION = "DROP TABLE IF EXISTS ap_position;";

    private static final String DATABASE_DROP_POSITION = "DROP TABLE IF EXISTS position;";
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute the queries to create tables
        sqLiteDatabase.execSQL(SQL_CREATE_AP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEVICE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i1 > i) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_AP_1);
            sqLiteDatabase.execSQL(DATABASE_DROP_AP_POSITION);
            sqLiteDatabase.execSQL(DATABASE_DROP_POSITION);
        }
    }
}
