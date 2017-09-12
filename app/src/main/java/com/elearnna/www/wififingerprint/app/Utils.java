package com.elearnna.www.wififingerprint.app;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.elearnna.www.wififingerprint.R;

/**
 * Created by Ahmed on 9/10/2017.
 */

public class Utils {
    public static int convertFrequencyToChannel(int frequency) {
        int channel = 0;
        switch (frequency) {
            case Constants.FREQ_2412:
                channel = Constants.CHANNEL_1;
                break;
            case Constants.FREQ_2417:
                channel = Constants.CHANNEL_2;
                break;
            case Constants.FREQ_2422:
                channel = Constants.CHANNEL_3;
                break;
            case Constants.FREQ_2427:
                channel = Constants.CHANNEL_4;
                break;
            case Constants.FREQ_2432:
                channel = Constants.CHANNEL_5;
                break;
            case Constants.FREQ_2437:
                channel = Constants.CHANNEL_6;
                break;
            case Constants.FREQ_2442:
                channel = Constants.CHANNEL_7;
                break;
            case Constants.FREQ_2447:
                channel = Constants.CHANNEL_8;
                break;
            case Constants.FREQ_2452:
                channel = Constants.CHANNEL_9;
                break;
            case Constants.FREQ_2457:
                channel = Constants.CHANNEL_10;
                break;
            case Constants.FREQ_2462:
                channel = Constants.CHANNEL_11;
                break;
            case Constants.FREQ_2467:
                channel = Constants.CHANNEL_12;
                break;
            case Constants.FREQ_2472:
                channel = Constants.CHANNEL_13;
                break;
            case Constants.FREQ_2484:
                channel = Constants.CHANNEL_14;
                break;
            case Constants.FREQ_5180:
                channel = Constants.CHANNEL_36;
                break;
            case Constants.FREQ_5200:
                channel = Constants.CHANNEL_40;
                break;
            case Constants.FREQ_5220:
                channel = Constants.CHANNEL_44;
                break;
            case Constants.FREQ_5240:
                channel = Constants.CHANNEL_48;
                break;
            case Constants.FREQ_5260:
                channel = Constants.CHANNEL_52;
                break;
            case Constants.FREQ_5280:
                channel = Constants.CHANNEL_56;
                break;
            case Constants.FREQ_5300:
                channel = Constants.CHANNEL_60;
                break;
            case Constants.FREQ_5320:
                channel = Constants.CHANNEL_64;
                break;
            case Constants.FREQ_5500:
                channel = Constants.CHANNEL_100;
                break;
            case Constants.FREQ_5520:
                channel = Constants.CHANNEL_104;
                break;
            case Constants.FREQ_5540:
                channel = Constants.CHANNEL_108;
                break;
            case Constants.FREQ_5560:
                channel = Constants.CHANNEL_112;
                break;
            case Constants.FREQ_5580:
                channel = Constants.CHANNEL_116;
                break;
            case Constants.FREQ_5600:
                channel = Constants.CHANNEL_120;
                break;
            case Constants.FREQ_5620:
                channel = Constants.CHANNEL_124;
                break;
            case Constants.FREQ_5640:
                channel = Constants.CHANNEL_128;
                break;
            case Constants.FREQ_5660:
                channel = Constants.CHANNEL_132;
                break;
            case Constants.FREQ_5680:
                channel = Constants.CHANNEL_136;
                break;
            case Constants.FREQ_5700:
                channel = Constants.CHANNEL_140;
                break;
            case Constants.FREQ_5745:
                channel = Constants.CHANNEL_149;
                break;
            case Constants.FREQ_5765:
                channel = Constants.CHANNEL_153;
                break;
            case Constants.FREQ_5785:
                channel = Constants.CHANNEL_157;
                break;
            case Constants.FREQ_5805:
                channel = Constants.CHANNEL_161;
                break;
            case Constants.FREQ_5825:
                channel = Constants.CHANNEL_165;
                break;
            default:
                Log.e("FrequencyToChannel", "Error in wifi frequency reading");
                break;
        }

        return channel;
    }

    public static RSSIRepresenter setWifiImage(int rssi, Context context) {
        int rssiSiganlColor = 0;
        int rssiSignalImage = 0;
        int signalRange;
        signalRange = isBetween(rssi);
        switch (signalRange) {
            case Constants.EXCELLENT:
                rssiSignalImage = R.drawable.ic_wifi_signal_full;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.green);
                break;
            case Constants.GOOD:
                rssiSignalImage = R.drawable.ic_wifi_signal_good;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.light_green);
                break;
            case Constants.MEDIUM:
                rssiSignalImage = R.drawable.ic_wifi_signal_medium;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.orange);
                break;
            case Constants.FAIR:
                rssiSignalImage = R.drawable.ic_wifi_signal_average;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.dark_orange);
                break;
            case Constants.WEAK:
                rssiSignalImage = R.drawable.ic_wifi_signal_weak;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.red);
                break;
            case Constants.VERY_WEAK:
                rssiSignalImage = R.drawable.ic_wifi_signal_very_weak;
                rssiSiganlColor = ContextCompat.getColor(context, R.color.dark_red);
                break;
            default:
                Log.e("APsAdapter", "Error in wifi signals reading");
                break;
        }
        return new RSSIRepresenter(rssiSignalImage, rssiSiganlColor);
    }

    public static int isBetween(int rssi) {
        if(rssi <= Constants.MAX_EXCELLENT && rssi >= Constants.MIN_EXCELLENT){
            return Constants.EXCELLENT;
        } else if(rssi <= Constants.MAX_GOOD && rssi >= Constants.MIN_GOOD){
            return Constants.GOOD;
        } else if(rssi <= Constants.MAX_MEDIUM && rssi >= Constants.MIN_MEDIUM){
            return Constants.MEDIUM;
        } else if(rssi <= Constants.MAX_FAIR && rssi >= Constants.MIN_FAIR){
            return Constants.FAIR;
        }else if(rssi <= Constants.MAX_WEAK && rssi >= Constants.MIN_WEAK){
            return Constants.WEAK;
        }else if(rssi <= Constants.MAX_VERY_WEAK && rssi >= Constants.MIN_VERY_WEAK){
            return Constants.VERY_WEAK;
        }
        return Constants.ERROR;
    }

}
