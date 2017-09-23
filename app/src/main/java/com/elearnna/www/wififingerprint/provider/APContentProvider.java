package com.elearnna.www.wififingerprint.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
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
    static final String ssid = "ssid";
    static final String rssi = "rssi";
    static final String frequency = "frequency";
    static final String macAddress = "mac_address";
    static final String channel = "channel";
    static final String isLocked = "is_locked";
    static final String apManufacturer = "manufacturer";
    static final String securtiyProtocol = "security_protocol";
    static final String ipAddress = "ip_address";
    static final String time = "time";

    // Define fields of Device table
    static final String deviceManufacturer = "manufacturer";
    static final String model = "model";
    static final String brand = "brand";
    static final String device = "device";
    static final String product = "product";
    static final String os = "os";
    static final String osVesrion = "os_version";
    static final String apiLevel = "api_level";

    // Define fields of File table
    static final String name = "name";
    static final String location = "location";
    static final String type = "type";

    // Define fields of Position table
    static final String positionName = "name";

    // Define fields of ap_position table
    static final String apID = "ap_id";
    static final String positionId = "position_id";

    // Define the uriCode
    static final int uriCode1 = 1;

    private static HashMap<String, String> values;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(Constants.AP_PROVIDER_NAME, "apscp", uriCode1);
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
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DatabaseContract.APEntry.TABLE_AP);
        queryBuilder.setTables(DatabaseContract.DeviceEntry.TABLE_DEVICE);
        queryBuilder.setTables(DatabaseContract.FileEntry.TABLE_FILE);
        queryBuilder.setTables(DatabaseContract.PositionEntry.TABLE_POSITION);
        queryBuilder.setTables(DatabaseContract.APPositionEntry.TABLE_AP_POSITION);

        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
