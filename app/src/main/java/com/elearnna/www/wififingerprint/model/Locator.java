package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 9/23/2017.
 */

public class Locator implements Parcelable {

    private String location;
    private int duration;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.location);
        dest.writeInt(this.duration);
    }

    public Locator() {
    }

    protected Locator(Parcel in) {
        this.location = in.readString();
        this.duration = in.readInt();
    }

    public static final Parcelable.Creator<Locator> CREATOR = new Parcelable.Creator<Locator>() {
        @Override
        public Locator createFromParcel(Parcel source) {
            return new Locator(source);
        }

        @Override
        public Locator[] newArray(int size) {
            return new Locator[size];
        }
    };
}
