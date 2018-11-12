package com.semisky.btcarkit.service;

import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTConst;
import com.semisky.btcarkit.aidl.BTDeviceInfo;
import com.semisky.btcarkit.aidl.BTHfpClientCall;
import com.semisky.btcarkit.aidl.IBTRoutingCommand;
import com.semisky.btcarkit.aidl.ISmkCallbackA2dp;
import com.semisky.btcarkit.aidl.ISmkCallbackAvrcp;
import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;
import com.semisky.btcarkit.constant.FscBTConst;
import com.semisky.btcarkit.service.at_cmd.ApiCommandTable;
import com.semisky.btcarkit.service.at_cmd.ApiResponseTable;
import com.semisky.btcarkit.service.at_cmd.IApiResponseTable;
import com.semisky.btcarkit.service.callbacks.IFscCallbackA2dp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackAvrcp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackBluetooth;
import com.semisky.btcarkit.service.callbacks.IFscCallbackHfp;
import com.semisky.btcarkit.service.natives.FscBwNative;
import com.semisky.btcarkit.service.natives.IFscBwNative;
import com.semisky.btcarkit.service.notifications.DoCallbackA2dp;
import com.semisky.btcarkit.service.notifications.DoCallbackAvrcp;
import com.semisky.btcarkit.service.notifications.DoCallbackBluetooth;
import com.semisky.btcarkit.service.notifications.DoCallbackHfp;
import com.semisky.btcarkit.service.thread.CmdHandlerRunnable;
import com.semisky.btcarkit.utils.Logutil;

import java.util.List;

public class BTRoutingCommandImpl extends IBTRoutingCommand.Stub {
    private static final String TAG = Logutil.makeTagLog(BTRoutingCommandImpl.class);

    private ApiCommandTable mApiCommandTable;// 发送AT指令帮助类
    private IApiResponseTable mApiResponseTable;// 接收并处理AT指令帮助类
    private IFscBwNative mFscBwNative;// 本地接口类

    private DoCallbackBluetooth mDoCallbackBluetooth;// 回调通知类
    private DoCallbackHfp mDoCallbackHfp;
    private DoCallbackA2dp mDoCallbackA2dp;
    private DoCallbackAvrcp mDoCallbackAvrcp;

    private CmdHandlerRunnable mCmdHandlerRunnable;

    public BTRoutingCommandImpl() {
        this.mFscBwNative = new FscBwNative();
        this.mApiCommandTable = new ApiCommandTable();

        this.mApiResponseTable = new ApiResponseTable();
        this.mApiResponseTable.registerBtStateCallback(this.mIFscCallbackBluetooth);
        this.mApiResponseTable.registerHfpStateCallback(this.mIFscCallbackHfp);
        this.mApiResponseTable.registerA2dpCallback(this.mIFscCallbackA2dp);
        this.mApiResponseTable.registerAvrcpCallback(this.mIFscCallbackAvrcp);
        // callback
        this.mDoCallbackBluetooth = new DoCallbackBluetooth();
        this.mDoCallbackHfp = new DoCallbackHfp();
        this.mDoCallbackA2dp = new DoCallbackA2dp();
        this.mDoCallbackAvrcp = new DoCallbackAvrcp();

        this.mCmdHandlerRunnable = new CmdHandlerRunnable();
        this.mCmdHandlerRunnable.onAttach(mFscBwNative);
        this.mCmdHandlerRunnable.onAttach(mApiResponseTable);
        this.mCmdHandlerRunnable.registerListener(mOnSerialStateListener);

        startCmdHandlerThread();
        //queryDeviceState();
    }

    void queryDeviceState() {
        Logutil.d(TAG, "queryDeviceState() ...");

    }

    // utils

    void startCmdHandlerThread() {
        if (null != mCmdHandlerRunnable && !mCmdHandlerRunnable.isRunning()) {
            Logutil.d(TAG, "startDataParseThread() ...");
            new Thread(mCmdHandlerRunnable).start();
        }
    }

    public void onRelease() {
        Logutil.d(TAG, "============onRelease()===========");
        if (null != mCmdHandlerRunnable) {
            mCmdHandlerRunnable.stop();
            mCmdHandlerRunnable = null;
        }
    }

    private CmdHandlerRunnable.OnSerialStateListener mOnSerialStateListener = new CmdHandlerRunnable.OnSerialStateListener() {
        @Override
        public void onStateChanged(int state) {
            Logutil.d(TAG, "OnSerialStateListener.onStateChanged() state : " + state);
            if (state == BTConst.BT_SERIAL_OPENED) {
                mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.DEVICE_STATE, null));
                mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.HANDFREE_STATE, null));
                mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.A2DP_STATE, null));
            }
        }
    };

    /*Bluetooth*--------------------------------------------------------------------------------------------------------------*/

    private IFscCallbackBluetooth mIFscCallbackBluetooth = new IFscCallbackBluetooth() {
        @Override
        public void onAdapterStateChanged(int oldState, int newState) {
            Logutil.i(TAG, "onAdapterStateChanged() oldState=" + oldState + ",newState=" + newState);
            mDoCallbackBluetooth.onAdapterStateChanged(oldState, newState);
        }

        @Override
        public void onDeviceDiscoveryStarted() {
            mDoCallbackBluetooth.onDeviceDiscoveryStarted();
        }

        @Override
        public void onDeviceFound(List<BTDeviceInfo> btDeviceInfo) {
            mDoCallbackBluetooth.onDeviceFound(btDeviceInfo);
        }

        @Override
        public void onDeviceDiscoveryFinished() {
            mDoCallbackBluetooth.onDeviceDiscoveryFinished();
        }
    };

    @Override
    public void registerBluetoothCallback(ISmkCallbackBluetooth callback) throws RemoteException {
        Logutil.i(TAG, "registerBtCallback() ...callback=" + callback);
        mDoCallbackBluetooth.register(callback);
    }

    @Override
    public void unregisterBluetoothCallback(ISmkCallbackBluetooth callback) throws RemoteException {
        Logutil.i(TAG, "unregisterBtCallback() ...callback=" + callback);
        mDoCallbackBluetooth.unregister(callback);
    }

    @Override
    public boolean isBtEnable() throws RemoteException {
        Logutil.i(TAG,"isBtEnable() ..." +mApiResponseTable.getDeviceState());
        return mApiResponseTable.getDeviceState() >= BTConst.BT_ON;
    }

    @Override
    public void setBtEnable(boolean enable) throws RemoteException {
        String btState = enable ? ApiCommandTable.BT_ENABLE : ApiCommandTable.BT_DISABLE;
        this.mFscBwNative.sendCMD(mApiCommandTable.makeCmd(btState, null));
    }

    @Override
    public void startBtDeviceDiscovery() throws RemoteException {
        int scanBtState = mApiResponseTable.getScanBtDeviceState();
        boolean isBtEnable = isBtEnable();

        Logutil.i(TAG, "startBtDeviceDiscovery() scanBtState = " + scanBtState + " , isBtEnable = " + isBtEnable);
        if (isBtEnable) {
            if (scanBtState == FscBTConst.SCANN_DEVICE_STATE_UNSUPPORTED ||
                    scanBtState == FscBTConst.SCANN_DEVICE_STATE_FINISHED) {
                this.mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.SCAN, ApiCommandTable.PARAM_SCAN_BR_EDR));
                mApiResponseTable.notifyStartBtDeviceDiscovery();
            }
        }
    }

    @Override
    public void reqBtConnectHfpA2dp(String address) throws RemoteException {
        if(isBtEnable()){
            mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.HANDFREE_CONNCECT,address));
        }
    }

    @Override
    public void reqBtDisconnectAll() throws RemoteException {
        if(isBtEnable()){
            Logutil.i(TAG,"reqBtDisconnectAll() ...");
            mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.DISCONNECT_ALL,null));
        }

    }

    /*HFP----------------------------------------------------------------------------------*/

    private IFscCallbackHfp mIFscCallbackHfp = new IFscCallbackHfp() {
        @Override
        public void onHfpStateChanged(String address, int oldState, int newState) {
            if (FscBTConst.HFP_CALL_STATE_INCOMING_CALL == oldState && newState < FscBTConst.HFP_CALL_STATE_INCOMING_CALL) { // 来电，挂断状态 5 -> 3
                Logutil.i(TAG, "HFP_CALL_STATE_INCOMING_CALL Hung up !!!");
                mApiResponseTable.removeIncomingCall();

            } else if (FscBTConst.HFP_CALL_STATE_OUTGOING_CALL == oldState && newState < FscBTConst.HFP_CALL_STATE_OUTGOING_CALL) {// 去电，挂断状态 4->3
                Logutil.i(TAG, "HFP_CALL_STATE_OUTGOING_CALL Hung up !!!");
                mApiResponseTable.removeOutgoingCall();
            }
            mDoCallbackHfp.onHfpStateChanged(address, oldState, newState);
        }

        @Override
        public void onHfpCallStateChanged(String address, BTHfpClientCall call) {
            mDoCallbackHfp.onHfpCallStateChanged(address, call);
        }
    };

    @Override
    public void registerHfpCallback(ISmkCallbackHfp callback) throws RemoteException {
        mDoCallbackHfp.register(callback);
    }

    @Override
    public void unregisterHfpCallback(ISmkCallbackHfp callback) throws RemoteException {
        mDoCallbackHfp.unregister(callback);
    }

    @Override
    public int getHfpState() throws RemoteException {
        return this.mApiResponseTable.getHfpState();
    }

    @Override
    public boolean isHfpConnected() throws RemoteException {
        if (FscBTConst.HFP_STATE_CONNECTED == mApiResponseTable.getHfpState()) {
            return true;
        }
        return false;
    }

    @Override
    public void reqHfpDialCall(String phoneNumber) throws RemoteException {
        Logutil.i(TAG, "reqHfpDialCall() hfpState=" + mApiResponseTable.getHfpState());
        if (mApiResponseTable.getHfpState() < FscBTConst.HFP_CALL_STATE_OUTGOING_CALL) {
            mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.HANDFREE_DIAL, phoneNumber));
        }
    }

    @Override
    public void reqHfpTerminateCurrentCall() throws RemoteException {
        Logutil.i(TAG, "reqHfpTerminateCurrentCall() hfpState=" + mApiResponseTable.getHfpState());
        if (mApiResponseTable.getHfpState() >= BTConst.HFP_CALL_STATE_OUTGOING_CALL) {
            mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.HANDFREE_HANGUP, null));
        }
    }
    /*A2DP----------------------------------------------------------------------------------*/

    private IFscCallbackA2dp mIFscCallbackA2dp = new IFscCallbackA2dp() {
        @Override
        public void onA2dpStateChanged(int oldState, int newState) {
            mDoCallbackA2dp.onA2dpStateChanged(oldState, newState);
        }
    };

    @Override
    public void registerA2dpCallback(ISmkCallbackA2dp callback) throws RemoteException {
        mDoCallbackA2dp.register(callback);
    }

    @Override
    public void unregisterA2dpCallback(ISmkCallbackA2dp callback) throws RemoteException {
        mDoCallbackA2dp.unregister(callback);
    }

    @Override
    public int getA2dpConnectionState() throws RemoteException {
        if (null != mApiResponseTable) {
            return mApiResponseTable.getA2dpState();
        }
        return 0;
    }

    @Override
    public boolean isA2dpConnected() throws RemoteException {
        if (null != mApiResponseTable) {
            if (FscBTConst.A2DP_CONNECTED == mApiResponseTable.getA2dpState()) {
                return true;
            }
        }
        return false;
    }

    /*AVRCP----------------------------------------------------------------------------------*/

    private IFscCallbackAvrcp mIFscCallbackAvrcp = new IFscCallbackAvrcp() {
        @Override
        public void onAvrcpStateChanged(String address, int oldState, int newState) {
            mDoCallbackAvrcp.onAvrcpStateChanged(address, oldState, newState);
        }

        @Override
        public void onAvrcpPlayStateChanged(int oldState, int newState) {
            mDoCallbackAvrcp.onAvrcpPlayStateChanged(oldState, newState);
        }

        @Override
        public void onAvrcpMediaMetadataChanged(int[] metadataIds, String[] metadataValues) {
            mDoCallbackAvrcp.onAvrcpMediaMetadataChanged(metadataIds, metadataValues);
        }
    };

    @Override
    public void registerAvrcpCallback(ISmkCallbackAvrcp callback) throws RemoteException {
        mDoCallbackAvrcp.register(callback);
    }

    @Override
    public void unregisterAvrcpCallback(ISmkCallbackAvrcp callback) throws RemoteException {
        mDoCallbackAvrcp.unregister(callback);
    }

    @Override
    public int getAvrcpMediaPlayState() throws RemoteException {

        return mApiResponseTable.getAvrcpMediaPlayStatus();
    }

    @Override
    public void reqAvrcpBackward() throws RemoteException {
        mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.MEDIA_PREVIOUS_TRACK, null));
    }

    @Override
    public void reqAvrcpForward() throws RemoteException {
        mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.MEDIA_NEXT_TRACK, null));
    }

    @Override
    public void reqAvrcpPause() throws RemoteException {
        mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.MEDIA_PAUSE, null));
    }

    @Override
    public void reqAvrcpPlay() throws RemoteException {
        mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.MEDIA_PLAY, null));
    }

    @Override
    public void reqAvrcpPlayOrPause() throws RemoteException {
        mFscBwNative.sendCMD(mApiCommandTable.makeCmd(ApiCommandTable.MEDIA_PLAY_OR_PAUSE, null));
    }

    @Override
    public String getBtMusicTitle() throws RemoteException {
        return mApiResponseTable.getBtMusicTitle();
    }

    @Override
    public String getBtMusicArtist() throws RemoteException {
        return mApiResponseTable.getBtMusicArtist();
    }

    @Override
    public String getBtMusicAlbum() throws RemoteException {
        return mApiResponseTable.getBtMusicAlbum();
    }
}
