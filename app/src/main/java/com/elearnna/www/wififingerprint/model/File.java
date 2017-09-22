package com.elearnna.www.wififingerprint.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Ahmed on 9/21/2017.
 */

public class File implements Parcelable {
    private int id;
    private String name;
    private String location;
    private String type;

    public File() {
    }

    public File(int id, String name, String location, String type) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.location);
        dest.writeString(this.type);
    }

    protected File(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.location = in.readString();
        this.type = in.readString();
    }

    public static final Parcelable.Creator<File> CREATOR = new Parcelable.Creator<File>() {
        @Override
        public File createFromParcel(Parcel source) {
            return new File(source);
        }

        @Override
        public File[] newArray(int size) {
            return new File[size];
        }
    };
}
