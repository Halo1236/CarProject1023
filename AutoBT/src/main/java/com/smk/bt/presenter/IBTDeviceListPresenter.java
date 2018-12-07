package com.smk.bt.presenter;

public interface IBTDeviceListPresenter {

    void initBTSwitchState();

    void setBtEnable();

    void startBtDiscovery();

    void reqBtPair(String address);


}
