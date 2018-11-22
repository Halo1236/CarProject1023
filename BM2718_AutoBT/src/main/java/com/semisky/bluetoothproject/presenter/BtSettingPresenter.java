package com.semisky.bluetoothproject.presenter;

import com.semisky.bluetoothproject.manager.BtServiceManager;
import com.semisky.bluetoothproject.presenter.viewInterface.BtSettingInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtSettingPresenter extends BasePresenter<BtSettingInterface> {

    private BtServiceManager btServiceManager;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    public BtSettingPresenter() {
        btServiceManager = BtServiceManager.getInstance();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
    }

    public void initListener() {

    }

    public void unregisterListener() {

    }

    public void getDevicesInformation() {
        btServiceManager.initBTStatus();
    }

    public void breakConnect() {
        btServiceManager.breakConnect();
    }

    public void syncContacts() {
        btServiceManager.reqPbapDownloadConnect();
    }

    public void syncRecord() {
        btServiceManager.reqPbapDownloadedCallLog();
    }

    public void initBTStatus() {
        btServiceManager.initBTStatus();
    }

    public void setAutoConnect(boolean autoConnect) {
        btServiceManager.setAutoConnect(autoConnect);
    }

    public void reqPbapDownloadAll() {
        btServiceManager.reqPbapDownloadAll();
    }

    public boolean isConnected() {
        return btBaseUiCommandMethod.isConnected();
    }
}
