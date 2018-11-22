package com.smk.bt.bean;

public class BTDeviceInfo {
    private String name;
    private String address;

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

    @Override
    public String toString() {
        return "BTDeviceInfo{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
