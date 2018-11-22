package com.smk.bt.presenter;

import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.res.NfDef;
import com.smk.bt.service.manager.BTServiceProxyManager;
import com.smk.bt.utils.Logger;
import com.smk.bt.views.fragment.IBTDeviceListView;

public class BTDeviceListPresenter<V extends IBTDeviceListView> extends BasePresenter<V> {
    private static final String TAG = Logger.makeLogTag(BTDeviceListPresenter.class);
    private Handler _handler;

    public BTDeviceListPresenter() {
        this._handler = new Handler(Looper.getMainLooper());
        BTServiceProxyManager.getInstance().registerOnServiceConnectListener(mOnServiceConnectListener);
        BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
    }

    private BTServiceProxyManager.OnServiceConnectListener mOnServiceConnectListener = new BTServiceProxyManager.OnServiceConnectListener() {
        @Override
        public void onServiceConnected() {
            BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
            boolean isBtEnabled = BTServiceProxyManager.getInstance().isBtEnabled();
            if (isBindView()) {
                mViewRfr.get().onBTSwitchStateChange(isBtEnabled);
            }
        }
    };

    private UiCallbackBluetooth.Stub mCallbackBluetooth = new UiCallbackBluetooth.Stub() {
        @Override
        public void onBluetoothServiceReady() throws RemoteException {

        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAdapterStateChanged() prevState = " + prevState + " newState = " + newState);
            if (newState == NfDef.BT_STATE_ON) {
                Logger.v(TAG, "onAdapterStateChanged() BT_STATE_ON");
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isBindView()) {
                            mViewRfr.get().onBTSwitchStateChange(true);
                        }
                    }
                });
            } else if (newState == NfDef.BT_STATE_OFF) {
                Logger.v(TAG, "onAdapterStateChanged() BT_STATE_OFF");
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isBindView()) {
                            mViewRfr.get().onBTSwitchStateChange(false);
                        }
                    }
                });
            }
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
        super.onDetachView();
    }

    public void setBTSwitchState() {
        if (isBindView()) {
            boolean isBtEnable = BTServiceProxyManager.getInstance().isBtEnabled();
            Logger.v(TAG, "setBTSwitchState() isBtEnable = " + isBtEnable);
            BTServiceProxyManager.getInstance().setBtEnable(isBtEnable ? false : true);
        }
    }

    public void initBTSwitchState() {
        if (isBindView()) {
            boolean isBtEnabled = BTServiceProxyManager.getInstance().isBtEnabled();
            mViewRfr.get().onBTSwitchStateChange(isBtEnabled);
        }
    }

    public void startBtDiscovery(){
        if(isBindView()){
            Logger.v(TAG,"startBtDiscovery()");
            BTServiceProxyManager.getInstance().startBtDiscovery();
        }
    }
}
