package com.semisky.btcarkit.service.callbacks;

import com.semisky.btcarkit.aidl.BTDeviceInfo;

import java.util.List;

public interface IFscCallbackBluetooth {

    /**
     * 蓝牙开关状态变化
     * */
    void onAdapterStateChanged(int oldState, int newState);

    /**
     * 开始扫描蓝牙设备
     */
    void onDeviceDiscoveryStarted();

    /**
     * 发现蓝牙设备
     *
     * @param btDeviceInfos
     */
    void onDeviceFound(List<BTDeviceInfo> btDeviceInfos);

    /**
     * 蓝牙设备扫描完成
     */
    void onDeviceDiscoveryFinished();

}
