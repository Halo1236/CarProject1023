package com.semisky.bluetoothproject.responseinterface;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtDeviceSearchResponse {

    /**
     * 蓝牙开启连接回调
     *
     * @param prevState
     * @param newState
     */
    void onAdapterStateChanged(int prevState, int newState);

    /**
     * 开始扫描设备
     */
    void onAdapterDiscoveryStarted();

    /**
     * 扫描设备结束
     */
    void onAdapterDiscoveryFinished();

    /**
     * 扫描到的蓝牙设备
     *
     * @param address
     * @param name
     * @param category
     */
    void onDeviceFound(String address, String name, byte category);

    void onDeviceBondStateChanged(String address, String name, int prevState, int newState);

    /**
     * 获取匹配历史列表
     */
    void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category);
}
