package com.smk.bt.presenter;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.res.NfDef;
import com.smk.bt.service.manager.BTServiceProxyManager;
import com.smk.bt.utils.Logger;
import com.smk.bt.views.activity.IBTMainView;

public class BTMainPresenter<V extends IBTMainView> extends BasePresenter<V> {
    private static final String TAG = Logger.makeLogTag(BTMainPresenter.class);
    private Handler _handler;

    public BTMainPresenter() {
        _handler = new Handler(Looper.getMainLooper());
        BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
        BTServiceProxyManager.getInstance().registerOnServiceConnectListener(mOnServiceConnectListener);
    }

    BTServiceProxyManager.OnServiceConnectListener mOnServiceConnectListener = new BTServiceProxyManager.OnServiceConnectListener() {
        @Override
        public void onServiceConnected() {
            if(!isBindView()){
                return;
            }
            BTServiceProxyManager.getInstance().registerBtCallback(mCallbackBluetooth);
            boolean isBtEnable = BTServiceProxyManager.getInstance().isBtEnabled();
            boolean isHfpConnected = BTServiceProxyManager.getInstance().isHfpConnected();

            Logger.v(TAG, "onServiceConnected() isBtEnable = " + isBtEnable);
            Logger.v(TAG, "onServiceConnected() isHfpConnected = " + isHfpConnected);

            if (isHfpConnected) {
                // TO DO 显示蓝牙已连接界面
                mViewRfr.get().showBTConnectedBottombar();
                mViewRfr.get().switchFragment(IBTMainView.FRAGMENT_DIALPAD_FLAG);
            } else {
                // TO DO 显示蓝牙未连接界面
                mViewRfr.get().showBTDisconnectedBottombar();
                mViewRfr.get().switchFragment(IBTMainView.FRAGMENT_DEVICE_LIST_FLAG);
            }

        }
    };

    public void handlerIntent(Intent intent) {
        if (null == intent) {
            return;
        }
        if (!isBindView()) {
            return;
        }
        boolean isBtEnable = BTServiceProxyManager.getInstance().isBtEnabled();
        boolean isHfpConnected = BTServiceProxyManager.getInstance().isHfpConnected();
        Logger.v(TAG, "handlerIntent() isBtEnable = " + isBtEnable);
        Logger.v(TAG, "handlerIntent() isHfpConnected = " + isHfpConnected);

        if (isHfpConnected) {
            // TO DO 显示蓝牙已连接界面
            mViewRfr.get().showBTConnectedBottombar();
            mViewRfr.get().initShowFragment(IBTMainView.FRAGMENT_DIALPAD_FLAG);
        } else {
            // TO DO 显示蓝牙未连接界面
            mViewRfr.get().showBTDisconnectedBottombar();
            mViewRfr.get().initShowFragment(IBTMainView.FRAGMENT_DEVICE_LIST_FLAG);
        }
    }

    public void switchFragment(int fragmentFlag) {
        if (isBindView()) {
            mViewRfr.get().switchFragment(fragmentFlag);
        }
    }

    @Override
    public void onDetachView() {
        BTServiceProxyManager.getInstance().unregisterBtCallback(mCallbackBluetooth);
        BTServiceProxyManager.getInstance().unregisterOnServiceConnectListener(mOnServiceConnectListener);
        super.onDetachView();
    }

    private UiCallbackBluetooth.Stub mCallbackBluetooth = new UiCallbackBluetooth.Stub() {
        @Override
        public void onBluetoothServiceReady() throws RemoteException {

        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {

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
        public void onHfpStateChanged(String address, final int prevState,final int newState) throws RemoteException {
            Logger.v(TAG, "onHfpStateChanged() prevState = " + prevState + " newState = " + newState);
            // HFP正在连接
            if (prevState < NfDef.STATE_CONNECTING && newState == NfDef.STATE_CONNECTING) {
                Logger.v(TAG, "onHfpStateChanged() STATE_CONNECTING");
            }
            // HFP已连接
            else if (prevState < NfDef.STATE_CONNECTED && newState == NfDef.STATE_CONNECTED) {
                Logger.v(TAG, "onHfpStateChanged() STATE_CONNECTED");
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isBindView()){
                            mViewRfr.get().showBTConnectedBottombar();
                            mViewRfr.get().switchFragment(IBTMainView.FRAGMENT_DIALPAD_FLAG);
                        }
                    }
                });
            }
            // HFP正在断开
            else if (prevState > NfDef.STATE_DISCONNECTING && newState == NfDef.STATE_DISCONNECTING) {
                Logger.v(TAG, "onHfpStateChanged() STATE_DISCONNECTING");
            }
            // HFP已断开
            else if (prevState >= NfDef.STATE_READY && newState <= NfDef.STATE_READY) {
                Logger.v(TAG, "onHfpStateChanged() STATE_DISCONNECTED");
                _handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(isBindView()){
                            mViewRfr.get().showBTDisconnectedBottombar();
                            mViewRfr.get().switchFragment(IBTMainView.FRAGMENT_DEVICE_LIST_FLAG);
                        }
                    }
                });
            }
        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onA2dpStateChanged() prevState = " + prevState + " newState = " + newState);
            // A2DP正在连接
            if (prevState < NfDef.STATE_CONNECTING && newState == NfDef.STATE_CONNECTING) {
                Logger.v(TAG, "onA2dpStateChanged() STATE_CONNECTING");
            }
            // A2DP已连接
            else if (prevState < NfDef.STATE_CONNECTED && newState == NfDef.STATE_CONNECTED) {
                Logger.v(TAG, "onA2dpStateChanged() STATE_CONNECTED");
            }
            // A2DP正在断开
            else if (prevState > NfDef.STATE_DISCONNECTING && newState == NfDef.STATE_DISCONNECTING) {
                Logger.v(TAG, "onA2dpStateChanged() STATE_DISCONNECTING");
            }
            // A2DP已断开
            else if (prevState >= NfDef.STATE_READY && newState <= NfDef.STATE_READY) {
                Logger.v(TAG, "onA2dpStateChanged() STATE_DISCONNECTED");
            }
        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAvrcpStateChanged() prevState = " + prevState + " newState = " + newState);
            // AVRCP正在连接
            if (prevState < NfDef.STATE_CONNECTING && newState == NfDef.STATE_CONNECTING) {
                Logger.v(TAG, "onAvrcpStateChanged() STATE_CONNECTING");
            }
            // AVRCP已连接
            else if (prevState < NfDef.STATE_CONNECTED && newState == NfDef.STATE_CONNECTED) {
                Logger.v(TAG, "onAvrcpStateChanged() STATE_CONNECTED");
            }
            // AVRCP正在断开
            else if (prevState > NfDef.STATE_DISCONNECTING && newState == NfDef.STATE_DISCONNECTING) {
                Logger.v(TAG, "onAvrcpStateChanged() STATE_DISCONNECTING");
            }
            // AVRCP已断开
            else if (prevState >= NfDef.STATE_READY && newState <= NfDef.STATE_READY) {
                Logger.v(TAG, "onAvrcpStateChanged() STATE_DISCONNECTED");
            }
        }
    };

}
