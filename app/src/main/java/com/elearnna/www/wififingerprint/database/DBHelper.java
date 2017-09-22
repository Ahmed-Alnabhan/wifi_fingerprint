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

    // Create string query that creates the position table
    private final String SQL_CREATE_POSITION_TABLE = "CREATE TABLE " +
            DatabaseContract.PositionEntry.TABLE_POSITION + " (" +
            DatabaseContract.PositionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.PositionEntry.COLUMN_POSITION_NAME + " TEXT " +
            ");";

    // Create string query that creates the ap_position table
    private final String SQL_CREATE_AP_POSITION_TABLE = "CREATE TABLE " +
            DatabaseContract.APPositionEntry.TABLE_AP_POSITION + " (" +
            DatabaseContract.APPositionEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DatabaseContract.APPositionEntry.COLUMN_AP_ID + " INTEGER, " +
            DatabaseContract.APPositionEntry.COLUMN_POSITION_ID + " INTEGER " +
            ");";

    private static final String DATABASE_ALTER_AP_1 = "ALTER TABLE "
            + DatabaseContract.APEntry.TABLE_AP + " ADD COLUMN " + DatabaseContract.APEntry.COLUMN_MAC_ADDRESS + " TEXT;";

    private static final String DATABASE_ALTER_AP_2 = "ALTER TABLE "
            + DatabaseContract.APEntry.TABLE_AP + " ADD COLUMN " + DatabaseContract.APEntry.COLUMN_IP_ADDRESS + " TEXT;";

    private static final String DATABASE_ALTER_DEVICE_1 = "ALTER TABLE "
            + DatabaseContract.DeviceEntry.TABLE_DEVICE + " ADD COLUMN " + DatabaseContract.DeviceEntry.COLUMN_MODEL + " TEXT;";

    private static final String DATABASE_ALTER_DEVICE_2 = "ALTER TABLE "
            + DatabaseContract.DeviceEntry.TABLE_DEVICE + " ADD COLUMN " + DatabaseContract.DeviceEntry.COLUMN_BRAND + " TEXT;";

    private static final String DATABASE_ALTER_FILE_1 = "ALTER TABLE "
            + DatabaseContract.FileEntry.TABLE_FILE + " ADD COLUMN " + DatabaseContract.FileEntry.COLUMN_NAME + " TEXT;";

    private static final String DATABASE_ALTER_FILE_2 = "ALTER TABLE "
            + DatabaseContract.FileEntry.TABLE_FILE + " ADD COLUMN " + DatabaseContract.FileEntry.COLUMN_TYPE + " TEXT;";

    private static final String DATABASE_ALTER_POSITION_1 = "ALTER TABLE "
            + DatabaseContract.PositionEntry.TABLE_POSITION + " ADD COLUMN " + DatabaseContract.PositionEntry.COLUMN_POSITION_NAME + " TEXT;";

    private static final String DATABASE_ALTER_POSITION_2 = "ALTER TABLE "
            + DatabaseContract.PositionEntry.TABLE_POSITION + " ADD COLUMN " + DatabaseContract.PositionEntry.COLUMN_POSITION_NAME + " TEXT;";

    private static final String DATABASE_ALTER_AP_POSITION_1 = "ALTER TABLE "
            + DatabaseContract.APPositionEntry.TABLE_AP_POSITION + " ADD COLUMN " + DatabaseContract.APPositionEntry.COLUMN_POSITION_ID + " INTEGER;";

    private static final String DATABASE_ALTER_AP_POSITION_2 = "ALTER TABLE "
            + DatabaseContract.APPositionEntry.TABLE_AP_POSITION + " ADD COLUMN " + DatabaseContract.APPositionEntry.COLUMN_AP_ID + " INTEGER;";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Execute the queries to create tables
        sqLiteDatabase.execSQL(SQL_CREATE_AP_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEVICE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FILE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_POSITION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_AP_POSITION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        if (i < 2) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_AP_1);
            sqLiteDatabase.execSQL(DATABASE_ALTER_DEVICE_1);
            sqLiteDatabase.execSQL(DATABASE_ALTER_FILE_1);
            sqLiteDatabase.execSQL(DATABASE_ALTER_POSITION_1);
            sqLiteDatabase.execSQL(DATABASE_ALTER_AP_POSITION_1);
        }
        if (i < 3) {
            sqLiteDatabase.execSQL(DATABASE_ALTER_AP_2);
            sqLiteDatabase.execSQL(DATABASE_ALTER_DEVICE_2);
            sqLiteDatabase.execSQL(DATABASE_ALTER_FILE_2);
            sqLiteDatabase.execSQL(DATABASE_ALTER_POSITION_2);
            sqLiteDatabase.execSQL(DATABASE_ALTER_AP_POSITION_2);
        }
    }
}
