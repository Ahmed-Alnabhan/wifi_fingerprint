package com.elearnna.www.wififingerprint.database;

import android.provider.BaseColumns;

/**
 * Created by Ahmed on 9/21/2017.
 */

public class DatabaseContract {
    public static final class APEntry implements BaseColumns {
        public static final String TABLE_AP = "ap";
        public static final String COLUMN_AP_ID = "id";
        public static final String COLUMN_LOCATION = "location";
        public static final String COLUMN_SSID = "ssid";
        public static final String COLUMN_RSSI = "rssi";
        public static final String COLUMN_FREQUENCY = "frequency";
        public static final String COLUMN_MAC_ADDRESS = "mac_address";
        public static final String COLUMN_CHANNEL = "channel";
        public static final String COLUMN_IS_LOCKED = "is_locked";
        public static final String COLUMN_AP_MANUFACTURER = "manufacturer";
        public static final String COLUMN_SECURITY_PROTOCOL = "security_protocol";
        public static final String COLUMN_IP_ADDRESS = "ip_address";
        public static final String COLUMN_TIME = "time";
    }

    public static final class DeviceEntry implements BaseColumns {
        public static final String TABLE_DEVICE = "device";
        public static final String COLUMN_DEVICE_ID = "id";
        public static final String COLUMN_DEVICE_MANUFACTURER = "manufacturer";
        public static final String COLUMN_MODEL = "model";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_DEVICE = "device";
        public static final String COLUMN_PRODUCT = "product";
        public static final String COLUMN_OS = "os";
        public static final String COLUMN_OS_VERSION = "os_verion";
        public static final String COLUMN_API_LEVEL = "api_level";
    }

    public static final class FileEntry implements BaseColumns {
        public static final String TABLE_FILE = "file";
        public static final String COLUMN_FILE_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_FILE_LOCATION = "location";
        public static final String COLUMN_TYPE = "type";
    }

}
