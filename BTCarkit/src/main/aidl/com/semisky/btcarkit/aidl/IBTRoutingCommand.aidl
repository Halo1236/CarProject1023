// IBTRoutingCommand.aidl
package com.semisky.btcarkit.aidl;
import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.aidl.ISmkCallbackAvrcp;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;

// Declare any non-default types here with import statements

interface IBTRoutingCommand {
    // Bluetooth general function
    void registerBluetoothCallback(ISmkCallbackBluetooth callback);
    void unregisterBluetoothCallback(ISmkCallbackBluetooth callback);
    boolean isBtEnable();
    void setBtEnable(boolean enable);
    void startBtDeviceDiscovery();
    void reqBtConnectHfpA2dp(String address);
    void reqBtDisconnectAll();

    // HFP general function
    void registerHfpCallback(ISmkCallbackHfp callback);
    void unregisterHfpCallback(ISmkCallbackHfp callback);
    /**
    * HFP当前状态
    * */
    int getHfpState();
    /**
    * HFP是否连接
    * */
    boolean isHfpConnected();
    /**
    * 拨打指定号码电话
    * */
    void reqHfpDialCall(String phoneNumber);
    /**
    * 挂断当前拨打电话
    * */
    void reqHfpTerminateCurrentCall();

    // A2DP general function
    void registerA2dpCallback(ISmkCallbackA2dp callback);
    void unregisterA2dpCallback(ISmkCallbackA2dp callback);
    int getA2dpConnectionState();
    boolean isA2dpConnected();
    // AVRCP general function
    void registerAvrcpCallback(ISmkCallbackAvrcp callback);
    void unregisterAvrcpCallback(ISmkCallbackAvrcp callback);
    int getAvrcpMediaPlayState();
    void reqAvrcpBackward();
    void reqAvrcpForward();
    void reqAvrcpPause();
    void reqAvrcpPlay();
    void reqAvrcpPlayOrPause();
    String getBtMusicTitle();
    String getBtMusicArtist();
    String getBtMusicAlbum();
    // PBAP general funtion
}
