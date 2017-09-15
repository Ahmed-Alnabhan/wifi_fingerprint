package com.elearnna.www.wififingerprint.app;

import com.elearnna.www.wififingerprint.network.RetrofitClient;
import com.elearnna.www.wififingerprint.network.VendorService;

/**
 * Created by Ahmed on 9/15/2017.
 */

public final class ApiUtils {
    public static final String BASE_URL = "https://api.macvendors.com/";

    //private constructor
    private ApiUtils() {
        throw new AssertionError();
    }

    public static VendorService getVendorService() {
        VendorService vs = RetrofitClient.getClient(BASE_URL).create(VendorService.class);
        return vs;
    }
}
