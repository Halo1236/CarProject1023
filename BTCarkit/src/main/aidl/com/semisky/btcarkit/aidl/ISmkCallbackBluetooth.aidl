// ISmkCallbackBluetooth.aidl
package com.semisky.btcarkit.aidl;
import com.semisky.btcarkit.aidl.BTDeviceInfo;

// Declare any non-default types here with import statements

interface ISmkCallbackBluetooth {

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
         * @param btDeviceInfo
         */
        void onDeviceFound(in List<BTDeviceInfo> btDeviceInfos);

        /**
         * 蓝牙设备扫描完成
         */
        void onDeviceDiscoveryFinished();
}
