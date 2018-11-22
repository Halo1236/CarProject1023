package com.semisky.bluetoothproject.entity;

/**
 * Created by chenhongrui on 2018/8/6
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class DevicesListEntity {

    private String deviceName;
    private String address;

    private DeviceConnectStatus status;
    private long timestamp;

    public DevicesListEntity(String address, String deviceName, DeviceConnectStatus status, long timestamp) {
        this.address = address;
        this.deviceName = deviceName;
        this.status = status;
        this.timestamp = timestamp;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public enum DeviceConnectStatus {
        NOT_CONNECT, CONNECTING, CONNECTED
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public DeviceConnectStatus getStatus() {
        return status;
    }

    public void setStatus(DeviceConnectStatus status) {
        this.status = status;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
