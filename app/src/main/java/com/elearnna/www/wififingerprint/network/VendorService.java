package com.elearnna.www.wififingerprint.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by Ahmed on 9/15/2017.
 */

public interface VendorService {
    @GET("{mac}")
    Call<String> getVendor(@Path("mac") String mac);
}
