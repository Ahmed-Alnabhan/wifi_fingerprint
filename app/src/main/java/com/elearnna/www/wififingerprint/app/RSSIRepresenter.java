package com.elearnna.www.wififingerprint.app;

/**
 * Created by Ahmed on 9/11/2017.
 */

public class RSSIRepresenter {
    private int RSSIImage;
    private int RSSIStrength;
    public RSSIRepresenter(int imageValue, int strengthValue){
        RSSIImage = imageValue;
        RSSIStrength = strengthValue;
    }

    public int getRSSIImage() {
        return RSSIImage;
    }

    public int getRSSIStrength() {
        return RSSIStrength;
    }
}
