// IMyAidlInterface.aidl
package com.semisky.bluetoothproject;

import com.semisky.bluetoothproject.IBtStatusListener;

interface IBtMethodInterface {

    void startBtDiscovery();

    void cancelBtDiscovery();

    void reqHfpDialCall(String number);

    void reqBtConnectHfpA2dp(String address);

    void reqBtUnpair(String address);

    void reqPbapDownloadConnect();

    void reqPbapDownloadedCallLog();

    void playSong();

    void pauseSong();

    void playNext();

    void playLast();

    void breakConnect();

    void isBtConnect();

    void setBtEnable(boolean status);

    void reqBtPairedDevices();

    void initBTStatus();

    void reqAvrcp13GetElementAttributesPlaying();

    void setAutoConnect(boolean autoConnect);

    void setOnBTStatusListener(IBtStatusListener listener);

    boolean isConnected();

    void reqPbapDownloadInterrupt();

    void reqAvrcp13GetPlayStatus();
}