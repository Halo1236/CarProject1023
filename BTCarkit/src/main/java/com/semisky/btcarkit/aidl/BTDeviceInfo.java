package com.semisky.btcarkit.aidl;

import android.os.Parcel;
import android.os.Parcelable;

public class BTDeviceInfo implements Parcelable {
    private String address;
    private String name;
    private String rssi;

    public BTDeviceInfo(String address, String name, String rssi) {
        this.address = address;
        this.name = name;
        this.rssi = rssi;
    }

    public static final Creator<BTDeviceInfo> CREATOR = new Creator<BTDeviceInfo>() {
        @Override
        public BTDeviceInfo createFromParcel(Parcel in) {
            return new BTDeviceInfo(in.readString(), in.readString(), in.readString());
        }

        @Override
        public BTDeviceInfo[] newArray(int size) {
            return new BTDeviceInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(name);
        dest.writeString(rssi);
    }

    // Getter/Setter
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRssi() {
        return rssi;
    }

    public void setRssi(String rssi) {
        this.rssi = rssi;
    }

    @Override
    public String toString() {
        return "BTDeviceInfo{" +
                "address='" + address + '\'' +
                ", name='" + name + '\'' +
                ", rssi='" + rssi + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return ((BTDeviceInfo)obj).address.equals(this.address);
    }
}
