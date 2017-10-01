package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Ahmed on 9/29/2017.
 */

public class Fingerprint implements Parcelable {

    private String location;
    private Device device;
    private List<AP> aps;

    public Fingerprint() {
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public List<AP> getAps() {
        return aps;
    }

    public void setAps(List<AP> aps) {
        this.aps = aps;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.location);
        dest.writeParcelable(this.device, flags);
        dest.writeTypedList(this.aps);
    }

    protected Fingerprint(Parcel in) {
        this.location = in.readString();
        this.device = in.readParcelable(Device.class.getClassLoader());
        this.aps = in.createTypedArrayList(AP.CREATOR);
    }

    public static final Parcelable.Creator<Fingerprint> CREATOR = new Parcelable.Creator<Fingerprint>() {
        @Override
        public Fingerprint createFromParcel(Parcel source) {
            return new Fingerprint(source);
        }

        @Override
        public Fingerprint[] newArray(int size) {
            return new Fingerprint[size];
        }
    };
}
