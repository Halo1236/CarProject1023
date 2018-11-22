package com.semisky.bluetoothproject.presenter;

import android.util.Log;

import com.semisky.bluetoothproject.model.BtDeviceSearchModel;
import com.semisky.bluetoothproject.model.BtHfpModel;
import com.semisky.bluetoothproject.presenter.viewInterface.DeviceSearchInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要: 蓝牙搜索设备Presenter
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtDeviceSearchPresenter extends BasePresenter<DeviceSearchInterface> {

    private static final String TAG = "BtDeviceSearchPresenter";

    private BtDeviceSearchModel btDeviceSearchModel;
    private BtHfpModel btHfpModel;
    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    public BtDeviceSearchPresenter() {
        btDeviceSearchModel = BtDeviceSearchModel.getInstance();
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
        btHfpModel = BtHfpModel.getInstance();
    }

    public void initListener() {
        btDeviceSearchModel.setListener(getViewRfr());
        btHfpModel.setListener(getViewRfr());
    }

    public void unregisterListener() {
        btDeviceSearchModel.unSetListener();
        btHfpModel.unSetListener();
    }

    public void startBtDiscovery() {
        btBaseUiCommandMethod.startBtDiscovery();
    }

    public void cancelBtDiscovery() {
        btBaseUiCommandMethod.cancelBtDiscovery();
    }

    public void reqBtConnectHfpA2dp(String address) {
        btBaseUiCommandMethod.reqBtConnectHfpA2dp(address);
    }

    public void reqBtUnpair(String address) {
        btBaseUiCommandMethod.reqBtUnpair(address);
    }

    public void setBtSwitch() {
        boolean btEnabled = btBaseUiCommandMethod.isBtEnabled();
        Log.d(TAG, "setBtSwitch: " + btEnabled);
        getViewRfr().isBtConnect(btEnabled);
    }

    public void setBtEnable(boolean setBtEnable) {
        btBaseUiCommandMethod.setBtEnable(setBtEnable);
    }

    public void reqBtPairedDevices() {
        btBaseUiCommandMethod.reqBtPairedDevices();
    }

    public void reqBtDisconnectAll() {
        btBaseUiCommandMethod.reqBtDisconnectAll();
    }
}
