package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ahmed on 9/6/2017.
 */

public class AP implements Parcelable {
    private String id;
    private String location;
    private String ssid;
    private int rssi;
    private List<Integer> rssiList;
    private int frequency;
    private String macAddress;
    private int channel;
    private int isLocked;
    private String manufacturer;
    private String securityProtocol;
    private String ipAddress;
    private int isConnected;
    private String time;

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public int isLocked() {
        return isLocked;
    }

    public void setLocked(int locked) {
        isLocked = locked;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getSecurityProtocol() {
        return securityProtocol;
    }

    public void setSecurityProtocol(String securityProtocol) {
        this.securityProtocol = securityProtocol;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public int isConnected() {
        return isConnected;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Integer> getRssiList() {
        return rssiList;
    }

    public void setRssiList(List<Integer> rssiList) {
        this.rssiList = rssiList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.location);
        dest.writeString(this.ssid);
        dest.writeInt(this.rssi);
        dest.writeList(this.rssiList);
        dest.writeInt(this.frequency);
        dest.writeString(this.macAddress);
        dest.writeInt(this.channel);
        dest.writeInt(this.isLocked);
        dest.writeString(this.manufacturer);
        dest.writeString(this.securityProtocol);
        dest.writeString(this.ipAddress);
        dest.writeInt(this.isConnected);
        dest.writeString(this.time);
    }

    public AP() {
    }

    protected AP(Parcel in) {
        this.id = in.readString();
        this.location = in.readString();
        this.ssid = in.readString();
        this.rssi = in.readInt();
        this.rssiList = new ArrayList<>();
        in.readList(this.rssiList, Integer.class.getClassLoader());
        this.frequency = in.readInt();
        this.macAddress = in.readString();
        this.channel = in.readInt();
        this.isLocked = in.readInt();
        this.manufacturer = in.readString();
        this.securityProtocol = in.readString();
        this.ipAddress = in.readString();
        this.isConnected = in.readInt();
        this.time = in.readString();
    }

    public static final Parcelable.Creator<AP> CREATOR = new Parcelable.Creator<AP>() {
        @Override
        public AP createFromParcel(Parcel source) {
            return new AP(source);
        }

        @Override
        public AP[] newArray(int size) {
            return new AP[size];
        }
    };
}
