package com.smk.bt.bean;

public class BTDeviceInfo {
    private String name;
    private String address;
    private boolean isFavorite;

    public BTDeviceInfo(String name, String address, boolean isFavorite) {
        this.name = name;
        this.address = address;
        this.isFavorite = isFavorite;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public String toString() {
        return "BTDeviceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", isFavorite=" + isFavorite +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && ((BTDeviceInfo) obj).address.equals(address);
    }
}
