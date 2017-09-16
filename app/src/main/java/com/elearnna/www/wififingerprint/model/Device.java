package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 9/16/2017.
 */

public class Device implements Parcelable {

    private String manufacturer;
    private String model;
    private String brand;
    private String device;
    private String product;
    private String os;
    private String osVersion;
    private int apiLevel;

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public int getApiLevel() {
        return apiLevel;
    }

    public void setApiLevel(int apiLevel) {
        this.apiLevel = apiLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.model);
        dest.writeString(this.manufacturer);
        dest.writeString(this.brand);
        dest.writeString(this.device);
        dest.writeString(this.product);
        dest.writeString(this.os);
        dest.writeString(this.osVersion);
        dest.writeInt(this.apiLevel);
    }

    public Device() {
    }

    protected Device(Parcel in) {
        this.model = in.readString();
        this.manufacturer = in.readString();
        this.brand = in.readString();
        this.device = in.readString();
        this.product = in.readString();
        this.os = in.readString();
        this.osVersion = in.readString();
        this.apiLevel = in.readInt();
    }

    public static final Parcelable.Creator<Device> CREATOR = new Parcelable.Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel source) {
            return new Device(source);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };
}
