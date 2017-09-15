package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 9/6/2017.
 */

public class AP implements Parcelable {
    private String id;
    private String locationName;
    private String ssid;
    private int rssi;
    private int frequency;
    private String macAddress;
    private int chennel;
    private int isLocked;
    private String manufacturer;
    private String securityProtocol;
    private String ipAddress;
    private int isConnected;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

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

    public int getChennel() {
        return chennel;
    }

    public void setChennel(int chennel) {
        this.chennel = chennel;
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

    public void setConnected(int connected) {
        isConnected = connected;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.locationName);
        dest.writeString(this.ssid);
        dest.writeInt(this.rssi);
        dest.writeInt(this.frequency);
        dest.writeString(this.macAddress);
        dest.writeInt(this.chennel);
        dest.writeInt(this.isLocked);
        dest.writeString(this.manufacturer);
        dest.writeString(this.securityProtocol);
        dest.writeString(this.ipAddress);
        dest.writeInt(this.isConnected);
    }

    public AP() {
    }

    protected AP(Parcel in) {
        this.id = in.readString();
        this.locationName = in.readString();
        this.ssid = in.readString();
        this.rssi = in.readInt();
        this.frequency = in.readInt();
        this.macAddress = in.readString();
        this.chennel = in.readInt();
        this.isLocked = in.readInt();
        this.manufacturer = in.readString();
        this.securityProtocol = in.readString();
        this.ipAddress = in.readString();
        this.isConnected = in.readInt();
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
