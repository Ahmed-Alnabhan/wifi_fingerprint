package com.elearnna.www.wififingerprint.app;

import android.net.Uri;

/**
 * Created by Ahmed on 9/10/2017.
 */

public class Constants {
    /**
     * Wifi signal strength descriptions
     */
    public static final int EXCELLENT = 1;
    public static final int GOOD = 2;
    public static final int MEDIUM = 3;
    public static final int FAIR = 4;
    public static final int WEAK = 5;
    public static final int VERY_WEAK = 6;
    public static final int ERROR = 7;

    /**
     * Wifi signal ranges
     */
    public static final int MIN_EXCELLENT = -50;
    public static final int MAX_EXCELLENT = -30;
    public static final int MIN_GOOD = -60;
    public static final int MAX_GOOD = -51;
    public static final int MIN_MEDIUM = -67;
    public static final int MAX_MEDIUM = -61;
    public static final int MIN_FAIR = -70;
    public static final int MAX_FAIR = -68;
    public static final int MIN_WEAK = -80;
    public static final int MAX_WEAK = -71;
    public static final int MIN_VERY_WEAK = -90;
    public static final int MAX_VERY_WEAK = -81;

    /**
     * Frequencies
     */
    // 2.4 GHz
    public static final int FREQ_2412 = 2412;
    public static final int FREQ_2417 = 2417;
    public static final int FREQ_2422 = 2422;
    public static final int FREQ_2427 = 2427;
    public static final int FREQ_2432 = 2432;
    public static final int FREQ_2437 = 2437;
    public static final int FREQ_2442 = 2442;
    public static final int FREQ_2447 = 2447;
    public static final int FREQ_2452 = 2452;
    public static final int FREQ_2457 = 2457;
    public static final int FREQ_2462 = 2462;
    public static final int FREQ_2467 = 2467;
    public static final int FREQ_2472 = 2472;
    public static final int FREQ_2484 = 2484;
    // 5GHz
    public static final double FREQ_3657_5 = 3657.5;
    public static final double FREQ_3662_5 = 3662.5;
    public static final double FREQ_3660_0 = 3660.0;
    public static final double FREQ_3667_5 = 3667.5;
    public static final double FREQ_3665_0 = 3665.0;
    public static final double FREQ_3672_5 = 3672.5;
    public static final double FREQ_3670_0 = 3670.0;
    public static final double FREQ_3677_5 = 3677.5;
    public static final double FREQ_3682_5 = 3682.5;
    public static final double FREQ_3680_0 = 3680.0;
    public static final double FREQ_3687_5 = 3687.5;
    public static final double FREQ_3685_0 = 3685.0;
    public static final double FREQ_3689_5 = 3689.5;
    public static final double FREQ_3690_0 = 3690.0;

    public static final int FREQ_5180 = 5180;
    public static final int FREQ_5200 = 5200;
    public static final int FREQ_5220 = 5220;
    public static final int FREQ_5240 = 5240;
    public static final int FREQ_5260 = 5260;
    public static final int FREQ_5280 = 5280;
    public static final int FREQ_5300 = 5300;
    public static final int FREQ_5320 = 5320;
    public static final int FREQ_5500 = 5500;
    public static final int FREQ_5520 = 5520;
    public static final int FREQ_5540 = 5540;
    public static final int FREQ_5560 = 5560;
    public static final int FREQ_5580 = 5580;
    public static final int FREQ_5600 = 5600;
    public static final int FREQ_5620 = 5620;
    public static final int FREQ_5640 = 5640;
    public static final int FREQ_5660 = 5660;
    public static final int FREQ_5680 = 5680;
    public static final int FREQ_5700 = 5700;
    public static final int FREQ_5745 = 5745;
    public static final int FREQ_5765 = 5765;
    public static final int FREQ_5785 = 5785;
    public static final int FREQ_5805 = 5805;
    public static final int FREQ_5825 = 5825;

    /**
     * Channel numbers
     */
    // For 2.4 GHz
    public static final int CHANNEL_1 = 1;
    public static final int CHANNEL_2 = 2;
    public static final int CHANNEL_3 = 3;
    public static final int CHANNEL_4 = 4;
    public static final int CHANNEL_5 = 5;
    public static final int CHANNEL_6 = 6;
    public static final int CHANNEL_7 = 7;
    public static final int CHANNEL_8 = 8;
    public static final int CHANNEL_9 = 9;
    public static final int CHANNEL_10 = 10;
    public static final int CHANNEL_11 = 11;
    public static final int CHANNEL_12 = 12;
    public static final int CHANNEL_13 = 13;
    public static final int CHANNEL_14 = 14;
    // For 5 GHz
    public static final int CHANNEL_36 = 36;
    public static final int CHANNEL_40 = 40;
    public static final int CHANNEL_44 = 44;
    public static final int CHANNEL_48 = 48;
    public static final int CHANNEL_52 = 52;
    public static final int CHANNEL_56 = 56;
    public static final int CHANNEL_60 = 60;
    public static final int CHANNEL_64 = 64;
    public static final int CHANNEL_100 = 100;
    public static final int CHANNEL_104 = 104;
    public static final int CHANNEL_108 = 108;
    public static final int CHANNEL_112 = 112;
    public static final int CHANNEL_116 = 116;
    public static final int CHANNEL_120 = 120;
    public static final int CHANNEL_124 = 124;
    public static final int CHANNEL_128 = 128;
    public static final int CHANNEL_132 = 132;
    public static final int CHANNEL_136 = 136;
    public static final int CHANNEL_140 = 140;
    public static final int CHANNEL_149 = 149;
    public static final int CHANNEL_153 = 153;
    public static final int CHANNEL_157 = 157;
    public static final int CHANNEL_161 = 161;
    public static final int CHANNEL_165 = 165;
    public static final int CHANNEL_131 = 131;
    public static final int CHANNEL_133 = 133;
    public static final int CHANNEL_134 = 134;
    public static final int CHANNEL_135 = 135;
    public static final int CHANNEL_137 = 137;
    public static final int CHANNEL_138 = 138;
    /**
     * Access Point
     */
    public static final String ACCESS_POINT = "ap";

    public static final String UNKNOWN = "Unknown";
    public static final String NOT_APPLICABLE = "n/a";


    public static final String EXECUTED_ONCE = "executedOnce";
    public static final String JSON_TYPE = "JSON";
    public static final String XML_TYPE = "XML";
    public static final String CSV_TYPE = "CSV";
    public static final String DEFAULT_DIRECTORY_PATH = "default_dir_path";
    public static final int ONE_SECOND = 1000;
    public static final int DEFAULT_DURATION = 15;
    public static final String DEFAULT_APS_SORT = "sort";
    public static final String ALL_BANDS = "all";
    public static final String RSSI_ASCENDING = "RSSI (DESC)";
    public static final String ALPHABETICALLY = "alphabetically";

    public static int MIN_5_GHZ_FREQUENCY = 5180;
    public static int MAX_5_GHZ_FREQUENCY =  5809;
    public static int MIN_2_4_GHZ_FREQUENCY = 2412;
    public static int MAX_2_4_GHZ_FREQUENCY = 2484;
    public static String BAND_5_GHZ = "5GHz";
    public static String BAND_2_4_GHZ = "2.4GHz";

    public static final String ABOUT_APP = "<b>Description:</b><br /> " +
            "This app enables users to scan WiFi networks and" +
            " collect data about the access points. The app " +
            "displays access points and network data in" +
            " a customizable way and provides charts to " +
            "support the visualization of data. Furthermore," +
            " the app enables users to store WiFi information" +
            " in a SQLite database and retrieve the chosen data" +
            " in a form of JSON, XML or CSV file and share the retrieved" +
            " file.<br /><br /><b>Used Libraries:</b><br />ButterKnife<br/>" +
            "<a href=\"http://jakewharton.github.io/butterknife/\">" +
            "http://jakewharton.github.io/butterknife/</a><br/><br/>" +
            "Retrofit<br/><a href=\"http://square.github.io/retrofit/\">" +
            "http://square.github.io/retrofit/</a><br/><br/>";

    /**
     * APContentProvider constants
     */
    public static final String PROVIDER_NAME = "com.elearnna.www.wififingerprint.provider.APContentProvider";
    public static final String APS_URL = "content://" + PROVIDER_NAME + "/apscp";
    public static final String FILES_URL = "content://" + PROVIDER_NAME + "/filescp";
    public static final String DEVICE_URL = "content://" + PROVIDER_NAME + "/devicecp";


    public static final Uri APS_CONTENT_URL = Uri.parse(APS_URL);
    public static final Uri FILES_CONTENT_URL = Uri.parse(FILES_URL);
    public static final Uri DEVICE_CONTENT_URL = Uri.parse(DEVICE_URL);



    /**
     * Database constants
     */
    public static final String DATABASE_NAME = "wifi_fingerprint.db";
    public static final int DATABASE_VERSION = 2;

    /**
     * Stored state
     */
    public static final String RECYCLER_STATE = "recycler.state";

}
