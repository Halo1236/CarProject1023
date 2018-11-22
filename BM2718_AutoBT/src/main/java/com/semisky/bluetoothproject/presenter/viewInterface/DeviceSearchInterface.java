package com.semisky.bluetoothproject.presenter.viewInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface DeviceSearchInterface {

    /**
     * 开始扫描设备
     */
    void onAdapterDiscoveryStarted();

    /**
     * 扫描设备结束
     */
    void onAdapterDiscoveryFinished();

    /**
     * 扫描设备中
     */
    void onDeviceFound(String address, final String name, byte category);

    /**
     * 连接过的设备
     */
    void onPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category);

    /**
     * 断开中
     */
    void stateDisconnecting(String address);

    /**
     * 已连接
     */
    void stateConnected(String address);

    /**
     * 连接中
     */
    void stateConnecting(String address);

    /**
     * 已断开
     */
    void stateDisconnect(String address);

    /**
     * 蓝牙是否连接
     */
    void isBtConnect(boolean status);

    /**
     * 蓝牙已开启
     */
    void btStatusOpen();

    /**
     * 蓝牙开启中
     */
    void btStatusOpening();

    /**
     * 蓝牙已关闭
     */
    void btStatusClose();

    /**
     * 蓝牙关闭中
     */
    void btStatusClosing();

}
