package com.smk.bt.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.res.NfDef;
import com.smk.bt.bean.BTDeviceInfo;
import com.smk.bt.service.manager.BTServiceProxyManager;
import com.smk.bt.utils.Logger;
import com.smk.bt.views.fragment.IBTDeviceListView;

import java.util.ArrayList;
import java.util.List;

public class BTDeviceListPresenter<V extends IBTDeviceListView> extends BasePresenter<V> implements IBTDeviceListPresenter {
    private static final String TAG = Logger.makeLogTag(BTDeviceListPresenter.class);
    private Handler _handler;
    private List<BTDeviceInfo> mBTDeviceInfoList = new ArrayList<BTDeviceInfo>();

    public BTDeviceListPresenter() {
        this._handler = new Handler(Looper.getMainLooper());
        BTServiceProxyManager.getInstance().registerOnServiceConnectListener(mOnServiceConnectListener);
        BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
    }

    private BTServiceProxyManager.OnServiceConnectListener mOnServiceConnectListener = new BTServiceProxyManager.OnServiceConnectListener() {
        @Override
        public void onServiceConnected() {
            BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
            initBTSwitchState();
        }
    };

    private UiCallbackBluetooth.Stub mCallbackBluetooth = new UiCallbackBluetooth.Stub() {
        @Override
        public void onBluetoothServiceReady() throws RemoteException {

        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAdapterStateChanged() prevState = " + prevState + " newState = " + newState);
            changeStateOfBTSwitch(newState, false);
        }

        @Override
        public void onAdapterDiscoverableModeChanged(int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onAdapterDiscoveryStarted() throws RemoteException {

        }

        @Override
        public void onAdapterDiscoveryFinished() throws RemoteException {

        }

        @Override
        public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) throws RemoteException {

        }

        @Override
        public void onDeviceFound(String address, String name, byte category) throws RemoteException {
            Logger.i(TAG, "onDeviceFound() address : " + address + " , name : " + name);
            addDeviceInfoToList(address, name);

        }

        @Override
        public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onDeviceUuidsUpdated(String address, String name, int supportProfile) throws RemoteException {

        }

        @Override
        public void onLocalAdapterNameChanged(String name) throws RemoteException {

        }

        @Override
        public void onDeviceOutOfRange(String address) throws RemoteException {

        }

        @Override
        public void onDeviceAclDisconnected(String address) throws RemoteException {

        }

        @Override
        public void onBtRoleModeChanged(int mode) throws RemoteException {

        }

        @Override
        public void onBtAutoConnectStateChanged(String address, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {

        }
    };

    @Override
    public void onDetachView() {
        BTServiceProxyManager.getInstance().unregisterBtCallback(mCallbackBluetooth);
        BTServiceProxyManager.getInstance().unregisterOnServiceConnectListener(mOnServiceConnectListener);
        mBTDeviceInfoList.clear();
        mBTDeviceInfoList = null;
        _handler.removeCallbacksAndMessages(null);
        _handler = null;
        super.onDetachView();
    }

    // IBTDeviceListPresenter Function

    @Override
    public void initBTSwitchState() {
        if (isBindView()) {
            int btState = BTServiceProxyManager.getInstance().getBtState();
            Logger.i(TAG, "initBTSwitchState() btState : " + btState);
            changeStateOfBTSwitch(btState, true);
        }
    }

    @Override
    public void setBtEnable() {
        if (isBindView()) {
            boolean isBtEnable = BTServiceProxyManager.getInstance().isBtEnabled();
            Logger.v(TAG, "setBTSwitchState() isBtEnable = " + isBtEnable);
            if (isBtEnable) {
                changeStateOfBTSwitch(NfDef.BT_STATE_TURNING_OFF, false);
                BTServiceProxyManager.getInstance().setBtEnable(false);
                removeDeviceList();
            } else {
                BTServiceProxyManager.getInstance().setBtEnable(true);
                changeStateOfBTSwitch(NfDef.BT_STATE_TURNING_ON, false);
            }
        }
    }

    @Override
    public void startBtDiscovery() {
        if (isBindView()) {
            Logger.v(TAG, "startBtDiscovery()");
            removeDeviceList();
            BTServiceProxyManager.getInstance().startBtDiscovery();
        }
    }

    private void removeDeviceList() {
        synchronized (mBTDeviceInfoList) {
            if(null != mBTDeviceInfoList && mBTDeviceInfoList.size() > 0){
                mBTDeviceInfoList.clear();
                notifyChangeDeviceInfoList();
            }
        }
    }

    private void addDeviceInfoToList(String address, String name) {
        synchronized (mBTDeviceInfoList) {
            if (null != mBTDeviceInfoList && !mBTDeviceInfoList.contains(address)) {
                mBTDeviceInfoList.add(new BTDeviceInfo(name, address, false));
                notifyChangeDeviceInfoList();
            }
        }
    }

    // bt device info list change
    private void notifyChangeDeviceInfoList() {
        synchronized (mBTDeviceInfoList) {
            _handler.post(new Runnable() {
                @Override
                public void run() {
                    if (isBindView()) {
                        mViewRfr.get().onChangeDeviceList(mBTDeviceInfoList);
                    }
                }
            });
        }
    }

    // 蓝牙开关状态改变
    private void changeStateOfBTSwitch(final int state, final boolean fromInitCall) {

        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (isBindView()) {
                    mViewRfr.get().onBTSwitchStateChange(state == NfDef.BT_STATE_ON);
                    switch (state) {
                        case NfDef.BT_STATE_OFF:
                            if (!fromInitCall) {
                                mViewRfr.get().onChangeStateOfBTSwitchDialog(IBTDeviceListView.BT_STATE_OFF);
                            }
                            break;
                        case NfDef.BT_STATE_TURNING_ON:
                            mViewRfr.get().onChangeStateOfBTSwitchDialog(IBTDeviceListView.BT_STATE_TURNING_ON);
                            break;
                        case NfDef.BT_STATE_ON:
                            if (!fromInitCall) {
                                mViewRfr.get().onChangeStateOfBTSwitchDialog(IBTDeviceListView.BT_STATE_ON);
                            }
                            break;
                        case NfDef.BT_STATE_TURNING_OFF:
                            mViewRfr.get().onChangeStateOfBTSwitchDialog(IBTDeviceListView.BT_STATE_TURNING_OFF);
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void reqBtPair(String address){
        if(isBindView()){
            BTServiceProxyManager.getInstance().reqBtPair(address);
        }
    }


}
