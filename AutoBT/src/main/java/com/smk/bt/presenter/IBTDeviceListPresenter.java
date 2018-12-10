package com.smk.bt.presenter;

public interface IBTDeviceListPresenter {

    void initBTSwitchState();

    void setBtEnable();

    void startBtDiscovery();

    void reqBtConnectHfpA2dp(String address);

    void cancelBtDiscovery();


}
