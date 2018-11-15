package com.smk.bt.service.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.UiCallbackA2dp;
import com.nforetek.bt.aidl.UiCallbackAvrcp;
import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.aidl.UiCallbackGattServer;
import com.nforetek.bt.aidl.UiCallbackHfp;
import com.nforetek.bt.aidl.UiCallbackHid;
import com.nforetek.bt.aidl.UiCallbackMap;
import com.nforetek.bt.aidl.UiCallbackOpp;
import com.nforetek.bt.aidl.UiCallbackPbap;
import com.nforetek.bt.aidl.UiCallbackSpp;
import com.nforetek.bt.aidl.UiCommand;
import com.nforetek.bt.res.NfDef;
import com.smk.bt.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class BTServiceProxyManager extends UiCommand.Stub {
    private static final String TAG = Logger.makeLogTag(BTServiceProxyManager.class);
    private Context mContext;
    private UiCommand mProxyService;

    private static BTServiceProxyManager _INSTANCE;
    private List<OnServiceConnectListener> mCallbacks;

    private BTServiceProxyManager() {
        mCallbacks = new ArrayList<OnServiceConnectListener>();
    }

    public static BTServiceProxyManager getInstance() {
        if (null == _INSTANCE) {
            synchronized (BTServiceProxyManager.class) {
                if (null == _INSTANCE) {
                    _INSTANCE = new BTServiceProxyManager();
                }
            }
        }
        return _INSTANCE;
    }

    // utils

    /**
     * 是否绑定服务
     *
     * @return
     */
    public boolean isBindService() {
        return (null != mProxyService);
    }

    /**
     * 解绑蓝牙服务
     */
    public void unbindBTService() {
        if (!isBindService()) {
            Logger.e(TAG, "Service is unbind !!!");
            return;
        }
        if (null == mContext) {
            Logger.e(TAG, "Context is null !!!");
            return;
        }
        mContext.unbindService(mConnection);
    }

    /**
     * 绑定蓝牙服务
     */
    public void bindBTService() {
        if (isBindService()) {
            Logger.e(TAG, "Service is bind !!!");
            return;
        }
        if (null == mContext) {
            Logger.e(TAG, "Context is null !!!");
            return;
        }
        mContext.bindService(new Intent("com.smk.service.ACTION_BT_START"), mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 初始化上下文
     *
     * @param ctx
     * @return
     */
    public BTServiceProxyManager init(Context ctx) {
        this.mContext = ctx;
        return this;
    }

    /**
     * 服务连接状态监听
     */
    public interface OnServiceConnectListener {
        void onServiceConnected();
    }

    /**
     * 注册服务连接状态监听
     *
     * @param cb
     */
    public void registerOnServiceConnectListener(OnServiceConnectListener cb) {
        if (null != cb && !mCallbacks.contains(cb)) {
            mCallbacks.add(cb);
        }
    }

    /**
     * 反注册服务连接状态监听
     *
     * @param cb
     */
    public void unregisterOnServiceConnectListener(OnServiceConnectListener cb) {
        if (null != cb && mCallbacks.contains(cb)) {
            mCallbacks.remove(cb);
        }
    }

    // 服务连接通知
    void notifyServiceConnectChange() {
        if (null != mCallbacks && mCallbacks.size() > 0) {
            for (OnServiceConnectListener cb : mCallbacks) {
                cb.onServiceConnected();
            }
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Logger.v(TAG, "onServiceConnected()");
            mProxyService = UiCommand.Stub.asInterface(service);
            notifyServiceConnectChange();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Logger.v(TAG, "onServiceDisconnected()");
            mProxyService = null;
        }
    };

    @Override
    public String getUiServiceVersionName() {
        if (isBindService()) {
            try {
                return mProxyService.getUiServiceVersionName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean isAvrcpServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isAvrcpServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isA2dpServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isA2dpServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isSppServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isSppServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isBluetoothServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isBluetoothServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isHfpServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isHidServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isHidServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isPbapServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isPbapServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isOppServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isOppServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isMapServiceReady() {
        if (isBindService()) {
            try {
                return mProxyService.isMapServiceReady();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean registerA2dpCallback(UiCallbackA2dp cb) {
        if (isBindService()) {
            try {
                return mProxyService.registerA2dpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean unregisterA2dpCallback(UiCallbackA2dp cb) {
        if (isBindService()) {
            try {
                return mProxyService.unregisterA2dpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getA2dpConnectionState() {
        if (isBindService()) {
            try {
                return mProxyService.getA2dpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isA2dpConnected() {
        if (isBindService()) {
            try {
                return mProxyService.isA2dpConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getA2dpConnectedAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getA2dpConnectedAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqA2dpConnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqA2dpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqA2dpDisconnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqA2dpDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void pauseA2dpRender() {
        if (isBindService()) {
            try {
                mProxyService.pauseA2dpRender();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startA2dpRender() {
        if (isBindService()) {
            try {
                mProxyService.startA2dpRender();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean setA2dpLocalVolume(float vol) {
        if (isBindService()) {
            try {
                return mProxyService.setA2dpLocalVolume(vol);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean setA2dpStreamType(int type) {
        if (isBindService()) {
            try {
                return mProxyService.setA2dpStreamType(type);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getA2dpStreamType() {
        if (isBindService()) {
            try {
                return mProxyService.getA2dpStreamType();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean registerAvrcpCallback(UiCallbackAvrcp cb) {
        if (isBindService()) {
            try {
                return mProxyService.registerAvrcpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean unregisterAvrcpCallback(UiCallbackAvrcp cb) {
        if (isBindService()) {
            try {
                return mProxyService.unregisterAvrcpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getAvrcpConnectionState() {
        if (isBindService()) {
            try {
                return mProxyService.getAvrcpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isAvrcpConnected() {
        if (isBindService()) {
            try {
                return mProxyService.isAvrcpConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getAvrcpConnectedAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getAvrcpConnectedAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqAvrcpConnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpDisconnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpDisconnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isAvrcp13Supported(String address) {
        if (isBindService()) {
            try {
                return mProxyService.isAvrcp13Supported(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isAvrcp14Supported(String address) {
        if (isBindService()) {
            try {
                return mProxyService.isAvrcp14Supported(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPlay() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpPlay();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStop() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpStop();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPause() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpPause();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpForward() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpForward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpBackward() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpBackward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeUp() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpVolumeUp();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeDown() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpVolumeDown();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartFastForward() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpStartFastForward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopFastForward() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpStopFastForward();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartRewind() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpStartRewind();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopRewind() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpStopRewind();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetCapabilitiesSupportEvent() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetCapabilitiesSupportEvent();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingAttributesList() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetPlayerSettingAttributesList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetPlayerSettingValuesList(attributeId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingCurrentValues() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetPlayerSettingCurrentValues();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13SetPlayerSettingValue(byte attributeId, byte valueId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13SetPlayerSettingValue(attributeId, valueId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetElementAttributesPlaying() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetElementAttributesPlaying();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayStatus() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13GetPlayStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpRegisterEventWatcher(eventId, interval);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcpUnregisterEventWatcher(byte eventId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcpUnregisterEventWatcher(eventId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13NextGroup() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13NextGroup();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13PreviousGroup() {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp13PreviousGroup();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isAvrcp14BrowsingChannelEstablished() {
        if (isBindService()) {
            try {
                return mProxyService.isAvrcp14BrowsingChannelEstablished();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAddressedPlayer(int playerId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14SetAddressedPlayer(playerId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetBrowsedPlayer(int playerId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14SetBrowsedPlayer(playerId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14GetFolderItems(byte scopeId) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14GetFolderItems(scopeId);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14ChangePath(uidCounter, uid, direction);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter, long uid) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14GetItemAttributes(scopeId, uidCounter, uid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter, long uid) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14PlaySelectedItem(scopeId, uidCounter, uid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14Search(String text) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14Search(text);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter, long uid) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14AddToNowPlaying(scopeId, uidCounter, uid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAbsoluteVolume(byte volume) {
        if (isBindService()) {
            try {
                return mProxyService.reqAvrcp14SetAbsoluteVolume(volume);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean registerBtCallback(UiCallbackBluetooth cb) {
        if (isBindService()) {
            try {
                return mProxyService.registerBtCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean unregisterBtCallback(UiCallbackBluetooth cb) {
        if (isBindService()) {
            try {
                return mProxyService.unregisterBtCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getNfServiceVersionName() {
        if (isBindService()) {
            try {
                return mProxyService.getNfServiceVersionName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean setBtEnable(boolean enable) {
        if (isBindService()) {
            try {
                return mProxyService.setBtEnable(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean setBtDiscoverableTimeout(int timeout) {
        if (isBindService()) {
            try {
                return mProxyService.setBtDiscoverableTimeout(timeout);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean startBtDiscovery() {
        if (isBindService()) {
            try {
                return mProxyService.startBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean cancelBtDiscovery() {
        if (isBindService()) {
            try {
                return mProxyService.cancelBtDiscovery();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqBtPair(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqBtPair(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqBtUnpair(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqBtUnpair(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqBtPairedDevices() {
        if (isBindService()) {
            try {
                return mProxyService.reqBtPairedDevices();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getBtLocalName() {
        if (isBindService()) {
            try {
                return mProxyService.getBtLocalName();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBtRemoteDeviceName(String address) {
        if (isBindService()) {
            try {
                return mProxyService.getBtRemoteDeviceName(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getBtLocalAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getBtLocalAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean setBtLocalName(String name) {
        if (isBindService()) {
            try {
                return mProxyService.setBtLocalName(name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isBtEnabled() {
        if (isBindService()) {
            try {
                return mProxyService.isBtEnabled();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getBtState() {
        if (isBindService()) {
            try {
                return mProxyService.getBtState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isBtDiscovering() {
        if (isBindService()) {
            try {
                return mProxyService.isBtDiscovering();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isBtDiscoverable() {
        if (isBindService()) {
            try {
                return mProxyService.isBtDiscoverable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isBtAutoConnectEnable() {
        if (isBindService()) {
            try {
                return mProxyService.isBtAutoConnectEnable();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int reqBtConnectHfpA2dp(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqBtConnectHfpA2dp(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public int reqBtDisconnectAll() {
        if (isBindService()) {
            try {
                return mProxyService.reqBtDisconnectAll();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public int getBtRemoteUuids(String address) {
        if (isBindService()) {
            try {
                return mProxyService.getBtRemoteUuids(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean switchBtRoleMode(int mode) {
        if (isBindService()) {
            try {
                return mProxyService.switchBtRoleMode(mode);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getBtRoleMode() {
        if (isBindService()) {
            try {
                return mProxyService.getBtRoleMode();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public void setBtAutoConnect(int condition, int period) {
        if (isBindService()) {
            try {
                mProxyService.setBtAutoConnect(condition, period);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getBtAutoConnectCondition() {
        if (isBindService()) {
            try {
                return mProxyService.getBtAutoConnectCondition();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public int getBtAutoConnectPeriod() {
        if (isBindService()) {
            try {
                return mProxyService.getBtAutoConnectPeriod();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public int getBtAutoConnectState() {
        if (isBindService()) {
            try {
                return mProxyService.getBtAutoConnectState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public String getBtAutoConnectingAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getBtAutoConnectingAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean registerHfpCallback(UiCallbackHfp cb) {
        if (isBindService()) {
            try {
                return mProxyService.registerHfpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean unregisterHfpCallback(UiCallbackHfp cb) {
        if (isBindService()) {
            try {
                return mProxyService.unregisterHfpCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getHfpConnectionState() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isHfpConnected() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpConnected();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getHfpConnectedAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpConnectedAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public int getHfpAudioConnectionState() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpAudioConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean reqHfpConnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpDisconnect(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpConnect(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getHfpRemoteSignalStrength() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpRemoteSignalStrength();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    @Override
    public List<NfHfpClientCall> getHfpCallList() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpCallList();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean isHfpRemoteOnRoaming() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpRemoteOnRoaming();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getHfpRemoteBatteryIndicator() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpRemoteBatteryIndicator();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isHfpRemoteTelecomServiceOn() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpRemoteTelecomServiceOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean isHfpRemoteVoiceDialOn() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpRemoteVoiceDialOn();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpDialCall(String number) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpDialCall(number);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpReDial() {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpReDial();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpMemoryDial(String index) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpMemoryDial(index);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpAnswerCall(int flag) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpAnswerCall(flag);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpRejectIncomingCall() {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpRejectIncomingCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpTerminateCurrentCall() {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpTerminateCurrentCall();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpSendDtmf(String number) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpSendDtmf(number);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToCarkit() {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpAudioTransferToCarkit();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToPhone() {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpAudioTransferToPhone();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getHfpRemoteNetworkOperator() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpRemoteNetworkOperator();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public String getHfpRemoteSubscriberNumber() {
        if (isBindService()) {
            try {
                return mProxyService.getHfpRemoteSubscriberNumber();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean reqHfpVoiceDial(boolean enable) {
        if (isBindService()) {
            try {
                return mProxyService.reqHfpVoiceDial(enable);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void pauseHfpRender() {
        if (isBindService()) {
            try {
                mProxyService.pauseHfpRender();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void startHfpRender() {
        if (isBindService()) {
            try {
                mProxyService.pauseHfpRender();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isHfpMicMute() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpMicMute();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void muteHfpMic(boolean mute) {
        if (isBindService()) {
            try {
                mProxyService.muteHfpMic(mute);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isHfpInBandRingtoneSupport() {
        if (isBindService()) {
            try {
                return mProxyService.isHfpInBandRingtoneSupport();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean registerPbapCallback(UiCallbackPbap cb) {
        if (isBindService()) {
            try {
                return mProxyService.registerPbapCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean unregisterPbapCallback(UiCallbackPbap cb) {
        if (isBindService()) {
            try {
                return mProxyService.unregisterPbapCallback(cb);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public int getPbapConnectionState() {
        if (isBindService()) {
            try {
                return mProxyService.getPbapConnectionState();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public boolean isPbapDownloading() {
        if (isBindService()) {
            try {
                return mProxyService.isPbapDownloading();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public String getPbapDownloadingAddress() {
        if (isBindService()) {
            try {
                return mProxyService.getPbapDownloadingAddress();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean reqPbapDownload(String address, int storage, int property) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownload(address, storage, property);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadRange(address, storage, property, startPos, offset);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadToDatabase(String address, int storage, int property) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadToDatabase(address, storage, property);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToDatabase(String address, int storage, int property, int startPos, int offset) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadRangeToDatabase(address, storage, property, startPos, offset);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadToContactsProvider(String address, int storage, int property) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadToContactsProvider(address, storage, property);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadRangeToContactsProvider(address, storage, property, startPos, offset);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public void reqPbapDatabaseQueryNameByNumber(String address, String target) {
        if (isBindService()) {
            try {
                mProxyService.reqPbapDatabaseQueryNameByNumber(address, target);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPbapDatabaseQueryNameByPartialNumber(String address, String target, int findPosition) {
        if (isBindService()) {
            try {
                mProxyService.reqPbapDatabaseQueryNameByPartialNumber(address, target, findPosition);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPbapDatabaseAvailable(String address) {
        if (isBindService()) {
            try {
                mProxyService.reqPbapDatabaseAvailable(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPbapDeleteDatabaseByAddress(String address) {
        if (isBindService()) {
            try {
                mProxyService.reqPbapDeleteDatabaseByAddress(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void reqPbapCleanDatabase() {
        if (isBindService()) {
            try {
                mProxyService.reqPbapCleanDatabase();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean reqPbapDownloadInterrupt(String address) {
        if (isBindService()) {
            try {
                return mProxyService.reqPbapDownloadInterrupt(address);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean setPbapDownloadNotify(int frequency) {
        if (isBindService()) {
            try {
                return mProxyService.setPbapDownloadNotify(frequency);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public boolean registerSppCallback(UiCallbackSpp cb) {
        return false;
    }

    @Override
    public boolean unregisterSppCallback(UiCallbackSpp cb) {
        return false;
    }

    @Override
    public boolean reqSppConnect(String address) {
        return false;
    }

    @Override
    public boolean reqSppDisconnect(String address) {
        return false;
    }

    @Override
    public void reqSppConnectedDeviceAddressList() {
    }

    @Override
    public boolean isSppConnected(String address) {
        return false;
    }

    @Override
    public void reqSppSendData(String address, byte[] sppData) {
    }

    @Override
    public boolean registerHidCallback(UiCallbackHid cb) {
        return false;
    }

    @Override
    public boolean unregisterHidCallback(UiCallbackHid cb) {
        return false;
    }

    @Override
    public int getHidConnectionState() {
        return 0;
    }

    @Override
    public boolean isHidConnected() {
        return false;
    }

    @Override
    public String getHidConnectedAddress() {
        return null;
    }

    @Override
    public boolean reqHidConnect(String address) {
        return false;
    }

    @Override
    public boolean reqHidDisconnect(String address) {
        return false;
    }

    @Override
    public boolean reqSendHidMouseCommand(int button, int offset_x, int offset_y, int wheel) {
        return false;
    }

    @Override
    public boolean reqSendHidVirtualKeyCommand(int key_1, int key_2) {
        return false;
    }

    @Override
    public boolean registerMapCallback(UiCallbackMap cb) {
        return false;
    }

    @Override
    public boolean unregisterMapCallback(UiCallbackMap cb) {
        return false;
    }

    @Override
    public boolean reqMapDownloadSingleMessage(String address, int folder, String handle, int storage) {
        return false;
    }

    @Override
    public boolean reqMapDownloadMessage(String address, int folder, boolean isContentDownload, int count, int startPos, int storage, String periodBegin, String periodEnd, String sender, String recipient, int readStatus, int typeFilter) {
        return false;
    }

    @Override
    public boolean reqMapRegisterNotification(String address, boolean downloadNewMessage) {
        return false;
    }

    @Override
    public void reqMapUnregisterNotification(String address) {
    }

    @Override
    public boolean isMapNotificationRegistered(String address) {
        return false;
    }

    @Override
    public boolean reqMapDownloadInterrupt(String address) {
        return false;
    }

    @Override
    public void reqMapDatabaseAvailable() {
    }

    @Override
    public void reqMapDeleteDatabaseByAddress(String address) {
    }

    @Override
    public void reqMapCleanDatabase() {
    }

    @Override
    public int getMapCurrentState(String address) {
        return 0;
    }

    @Override
    public int getMapRegisterState(String address) {
        return 0;
    }

    @Override
    public boolean reqMapSendMessage(String address, String message, String target) {
        return false;
    }

    @Override
    public boolean reqMapDeleteMessage(String address, int folder, String handle) {
        return false;
    }

    @Override
    public boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus) {
        return false;
    }

    @Override
    public boolean setMapDownloadNotify(int frequency) {
        return false;
    }

    @Override
    public boolean registerOppCallback(UiCallbackOpp cb) {
        return false;
    }

    @Override
    public boolean unregisterOppCallback(UiCallbackOpp cb) {
        return false;
    }

    @Override
    public boolean setOppFilePath(String path) {
        return false;
    }

    @Override
    public String getOppFilePath() {
        return null;
    }

    @Override
    public boolean reqOppAcceptReceiveFile(boolean accept) {
        return false;
    }

    @Override
    public boolean reqOppInterruptReceiveFile() {
        return false;
    }

    @Override
    public void setTargetAddress(String address) {

    }

    @Override
    public String getTargetAddress() {
        return null;
    }

    @Override
    public void reqAvrcpUpdateSongStatus() {
        if (isBindService()) {
            try {
                mProxyService.reqAvrcpUpdateSongStatus();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isGattServiceReady() {
        return false;
    }

    @Override
    public boolean registerGattServerCallback(UiCallbackGattServer cb) {
        return false;
    }

    @Override
    public boolean unregisterGattServerCallback(UiCallbackGattServer cb) {
        return false;
    }

    @Override
    public int getGattServerConnectionState() {
        return 0;
    }

    @Override
    public boolean reqGattServerDisconnect(String address) {
        return false;
    }

    @Override
    public boolean reqGattServerBeginServiceDeclaration(int srvcType, ParcelUuid srvcUuid) {
        return false;
    }

    @Override
    public boolean reqGattServerAddCharacteristic(ParcelUuid charUuid, int properties, int permissions) {
        return false;
    }

    @Override
    public boolean reqGattServerAddDescriptor(ParcelUuid descUuid, int permissions) {
        return false;
    }

    @Override
    public boolean reqGattServerEndServiceDeclaration() {
        return false;
    }

    @Override
    public boolean reqGattServerRemoveService(int srvcType, ParcelUuid srvcUuid) {
        return false;
    }

    @Override
    public boolean reqGattServerClearServices() {
        return false;
    }

    @Override
    public boolean reqGattServerListen(boolean listen) {
        return false;
    }

    @Override
    public boolean reqGattServerSendResponse(String address, int requestId, int status, int offset, byte[] value) {
        return false;
    }

    @Override
    public boolean reqGattServerSendNotification(String address, int srvcType, ParcelUuid srvcUuid, ParcelUuid charUuid, boolean confirm, byte[] value) {
        return false;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattServiceUuidList() {
        return null;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattCharacteristicUuidList(ParcelUuid srvcUuid) {
        return null;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattDescriptorUuidList(ParcelUuid srvcUuid, ParcelUuid charUuid) {
        return null;
    }
}
