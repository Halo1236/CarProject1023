package com.smk.bt.service;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.INfCallbackA2dp;
import com.nforetek.bt.aidl.INfCallbackAvrcp;
import com.nforetek.bt.aidl.INfCallbackBluetooth;
import com.nforetek.bt.aidl.INfCallbackHfp;
import com.nforetek.bt.aidl.INfCallbackPbap;
import com.nforetek.bt.aidl.INfCommandA2dp;
import com.nforetek.bt.aidl.INfCommandAvrcp;
import com.nforetek.bt.aidl.INfCommandBluetooth;
import com.nforetek.bt.aidl.INfCommandHfp;
import com.nforetek.bt.aidl.INfCommandPbap;
import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.NfPbapContact;
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
import com.smk.bt.service.callbacks.DoCallbackA2dp;
import com.smk.bt.service.callbacks.DoCallbackAvrcp;
import com.smk.bt.service.callbacks.DoCallbackBluetooth;
import com.smk.bt.service.callbacks.DoCallbackHfp;
import com.smk.bt.service.callbacks.DoCallbackPbap;
import com.smk.bt.utils.Logger;

import java.util.Arrays;
import java.util.List;

/**
 * Author:LiuYong
 * Time:2018-11-12 16:12
 * Description:This is BTServiceImpl
 */
public class BTServiceImpl extends UiCommand.Stub {
    private static final String TAG = Logger.makeLogTag(BTServiceImpl.class);

    private Context mContext;
    private String mVersionName = "";

    private INfCommandHfp mCommandHfp;
    private INfCommandA2dp mCommandA2dp;
    private INfCommandAvrcp mCommandAvrcp;
    private INfCommandPbap mCommandPbap;
    private INfCommandBluetooth mCommandBluetooth;

    private DoCallbackA2dp mDoCallbackA2dp;
    private DoCallbackAvrcp mDoCallbackAvrcp;
    private DoCallbackHfp mDoCallbackHfp;
    private DoCallbackPbap mDoCallbackPbap;
    private DoCallbackBluetooth mDoCallbackBluetooth;

    // avrcp media info
    private String
            mTitle,
            mArtist,
            mAlbum;

    public BTServiceImpl(Context ctx) {
        this.mContext = ctx;
        mDoCallbackA2dp = new DoCallbackA2dp();
        mDoCallbackAvrcp = new DoCallbackAvrcp();
        mDoCallbackHfp = new DoCallbackHfp();
        mDoCallbackPbap = new DoCallbackPbap();
        mDoCallbackBluetooth = new DoCallbackBluetooth();
        Log.v(TAG, "bindA2dpService");
        ctx.bindService(new Intent(NfDef.CLASS_SERVICE_A2DP), this.mConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "bindAvrcpService");
        ctx.bindService(new Intent(NfDef.CLASS_SERVICE_AVRCP), this.mConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "bindHfpService");
        ctx.bindService(new Intent(NfDef.CLASS_SERVICE_HFP), this.mConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "bindPbapService");
        ctx.bindService(new Intent(NfDef.CLASS_SERVICE_PBAP), this.mConnection, Context.BIND_AUTO_CREATE);
        Log.v(TAG, "bindBluetoothService");
        ctx.bindService(new Intent(NfDef.CLASS_SERVICE_BLUETOOTH), this.mConnection, Context.BIND_AUTO_CREATE);
    }

    // utils
    public void onDestroy() {
        try {
            if (mCommandHfp != null) {
                mCommandHfp.unregisterHfpCallback(mCallbackHfp);
            }
            if (mCommandA2dp != null) {
                mCommandA2dp.unregisterA2dpCallback(mCallbackA2dp);
            }
            if (mCommandAvrcp != null) {
                mCommandAvrcp.unregisterAvrcpCallback(mCallbackAvrcp);
            }
            if (mCommandPbap != null) {
                mCommandPbap.unregisterPbapCallback(mCallbackPbap);
            }
            if (mCommandBluetooth != null) {
                mCommandBluetooth.unregisterBtCallback(mCallbackBluetooth);
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        Log.e(TAG, "start unbind service");
        mContext.unbindService(mConnection);
        Log.e(TAG, "end unbind service");
        /** Unregister all callbacks */
        if (mDoCallbackHfp != null)
            mDoCallbackHfp.kill();
        if (mDoCallbackA2dp != null)
            mDoCallbackA2dp.kill();
        if (mDoCallbackAvrcp != null)
            mDoCallbackAvrcp.kill();
        if (mDoCallbackPbap != null)
            mDoCallbackPbap.kill();
        if (mDoCallbackBluetooth != null)
            mDoCallbackBluetooth.kill();
        Log.e(TAG, " --- onDestroy --- ");
    }

    // ServiceConnection
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.e(TAG, "ready onServiceConnected");
            Logger.v(TAG, "IBinder className : " + className);

            if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_HFP + ")");
                mCommandHfp = INfCommandHfp.Stub.asInterface(service);
                if (mCommandHfp == null) {
                    Logger.e(TAG, "mCommandHfp is null!!");
                    return;
                }
                try {
                    mCommandHfp.registerHfpCallback(mCallbackHfp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_A2DP))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_A2DP + ")");
                mCommandA2dp = INfCommandA2dp.Stub.asInterface(service);
                if (mCommandA2dp == null) {
                    Logger.e(TAG, "mCommandA2dp is null !!");
                    return;
                }
                try {
                    mCommandA2dp.registerA2dpCallback(mCallbackA2dp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_AVRCP))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_AVRCP + ")");
                mCommandAvrcp = INfCommandAvrcp.Stub.asInterface(service);
                if (mCommandAvrcp == null) {
                    Logger.e(TAG, "mCommandAvrcp is null !!");
                    return;
                }
                try {
                    mCommandAvrcp.registerAvrcpCallback(mCallbackAvrcp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_PBAP + ")");
                mCommandPbap = INfCommandPbap.Stub.asInterface(service);
                if (mCommandPbap == null) {
                    Logger.e(TAG, "mCommandPbap is null !!");
                    return;
                }
                try {
                    mCommandPbap.registerPbapCallback(mCallbackPbap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_BLUETOOTH + ")");
                mCommandBluetooth = INfCommandBluetooth.Stub.asInterface(service);
                if (mCommandBluetooth == null) {
                    Logger.e(TAG, "mCommandBluetooth is null !!");
                    return;
                }
                try {
                    mCommandBluetooth.registerBtCallback(mCallbackBluetooth);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            Logger.e(TAG, "end onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Log.e(TAG, "ready onServiceDisconnected: " + className);
            if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
                mCommandHfp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_A2DP))) {
                mCommandA2dp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_AVRCP))) {
                mCommandAvrcp = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
                mCommandPbap = null;
            } else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
                mCommandBluetooth = null;
            }
            Log.e(TAG, "end onServiceDisconnected");

        }
    };

    // UiCallbackHfp.aidl---------------------------------------------------------------------------
    private INfCallbackHfp.Stub mCallbackHfp = new INfCallbackHfp.Stub() {
        @Override
        public void onHfpServiceReady() throws RemoteException {
            Logger.v(TAG, "onHfpServiceReady()");
            mDoCallbackHfp.onHfpServiceReady();
        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onHfpStateChanged() address : " + address + " , prevState : " + prevState + " , newState : " + newState);
            mDoCallbackHfp.onHfpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onHfpStateChanged(address, prevState, newState);
        }

        @Override
        public void onHfpAudioStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onHfpAudioStateChanged() address : " + address + " , prevState : " + prevState + " , newState : " + newState);
            mDoCallbackHfp.onHfpAudioStateChanged(address, prevState, newState);
        }

        @Override
        public void onHfpVoiceDial(String address, boolean isVoiceDialOn) throws RemoteException {
            Logger.v(TAG, "onHfpErrorResponse() address : " + address + " , isVoiceDialOn : " + isVoiceDialOn);
            mDoCallbackHfp.onHfpVoiceDial(address, isVoiceDialOn);
        }

        @Override
        public void onHfpErrorResponse(String address, int code) throws RemoteException {
            Logger.v(TAG, "onHfpErrorResponse() address : " + address + " , code : " + code);
            mDoCallbackHfp.onHfpErrorResponse(address, code);
        }

        @Override
        public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) throws RemoteException {
            Logger.v(TAG, "onHfpRemoteTelecomService() address : " + address + " , isTelecomServiceOn : " + isTelecomServiceOn);
            mDoCallbackHfp.onHfpRemoteTelecomService(address, isTelecomServiceOn);
        }

        @Override
        public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) throws RemoteException {
            Logger.v(TAG, "onHfpRemoteRoamingStatus() address : " + address + " , isRoamingOn : " + isRoamingOn);
            mDoCallbackHfp.onHfpRemoteRoamingStatus(address, isRoamingOn);
        }

        @Override
        public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) throws RemoteException {
            Logger.v(TAG, "onHfpRemoteBatteryIndicator() address : " + address
                    + " , currentValue : " + currentValue
                    + " , maxValue : " + maxValue
                    + " , minValue : " + minValue);
            mDoCallbackHfp.onHfpRemoteBatteryIndicator(address, currentValue, maxValue, minValue);
        }

        @Override
        public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) throws RemoteException {
            Logger.v(TAG, "onHfpRemoteSignalStrength() address : " + address
                    + " , currentStrength : " + currentStrength
                    + " , maxStrength : " + maxStrength
                    + " , minStrength : " + minStrength);
            mDoCallbackHfp.onHfpRemoteSignalStrength(address, currentStrength, maxStrength, minStrength);
        }

        @Override
        public void onHfpCallChanged(String address, NfHfpClientCall call) throws RemoteException {
            Logger.v(TAG, "onHfpCallChanged() address : " + address);
            Logger.v(TAG, "onHfpCallChanged() NfHfpClientCall : " + (null != call ? call.toString() : "NULL"));
            mDoCallbackHfp.onHfpCallChanged(address, call);
        }
    };

    // INfCallbackA2dp.aidl-------------------------------------------------------------------------
    private INfCallbackA2dp.Stub mCallbackA2dp = new INfCallbackA2dp.Stub() {
        @Override
        public void onA2dpServiceReady() throws RemoteException {
            Logger.v(TAG, "onA2dpServiceReady()");
            mDoCallbackA2dp.onA2dpServiceReady();
        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onA2dpStateChanged() address : " + address + " , prevState : " + prevState + " , newState : " + newState);
            mDoCallbackA2dp.onA2dpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onA2dpStateChanged(address, prevState, newState);
        }
    };

    private INfCallbackAvrcp.Stub mCallbackAvrcp = new INfCallbackAvrcp.Stub() {
        @Override
        public void onAvrcpServiceReady() throws RemoteException {
            Logger.v(TAG, "onAvrcpServiceReady()");
            mDoCallbackAvrcp.onAvrcpServiceReady();
        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAvrcpStateChanged() address : " + address + " , prevState : " + prevState + " , newState : " + newState);
            if (newState >= NfDef.STATE_CONNECTED && prevState < NfDef.STATE_CONNECTED) {
                Log.e(TAG, "reqAvrcpCtRegisterEventWatcher");
                mCommandAvrcp.reqAvrcpRegisterEventWatcher(NfDef.AVRCP_EVENT_ID_TRACK_CHANGED, 0);
                mCommandAvrcp.reqAvrcpRegisterEventWatcher(NfDef.AVRCP_EVENT_ID_PLAYBACK_STATUS_CHANGED, 0);
            } else if (newState == NfDef.STATE_READY) {
                mTitle = "";
                mArtist = "";
                mAlbum = "";
            }
            mDoCallbackAvrcp.onAvrcpStateChanged(address, prevState, newState);
            mDoCallbackBluetooth.onAvrcpStateChanged(address, prevState, newState);
        }

        @Override
        public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) throws RemoteException {
            Logger.v(TAG, "retAvrcp13CapabilitiesSupportEvent()");
            mDoCallbackAvrcp.retAvrcp13CapabilitiesSupportEvent(eventIds);
        }

        @Override
        public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) throws RemoteException {
            Logger.v(TAG, "retAvrcp13PlayerSettingAttributesList()");
            mDoCallbackAvrcp.retAvrcp13PlayerSettingAttributesList(attributeIds);
        }

        @Override
        public void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) throws RemoteException {
            Logger.v(TAG, "retAvrcp13PlayerSettingValuesList() attributeId : " + attributeId);
            mDoCallbackAvrcp.retAvrcp13PlayerSettingValuesList(attributeId, valueIds);
        }

        @Override
        public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) throws RemoteException {
            Logger.v(TAG, "retAvrcp13PlayerSettingCurrentValues()");
            mDoCallbackAvrcp.retAvrcp13PlayerSettingCurrentValues(attributeIds, valueIds);
        }

        @Override
        public void retAvrcp13SetPlayerSettingValueSuccess() throws RemoteException {
            Logger.v(TAG, "retAvrcp13SetPlayerSettingValueSuccess()");
            mDoCallbackAvrcp.retAvrcp13SetPlayerSettingValueSuccess();
        }

        @Override
        public void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
            mTitle = "";
            mArtist = "";
            mAlbum = "";
            for (int i = 0; i < metadataAtrributeIds.length; i++) {
                if (NfDef.AVRCP_META_ATTRIBUTE_ID_TITLE == metadataAtrributeIds[i]) {
                    mTitle = texts[i];
                } else if (NfDef.AVRCP_META_ATTRIBUTE_ID_ARTIST == metadataAtrributeIds[i]) {
                    mArtist = texts[i];
                } else if (NfDef.AVRCP_META_ATTRIBUTE_ID_ALBUM == metadataAtrributeIds[i]) {
                    mAlbum = texts[i];
                }
            }
            Logger.v(TAG, "retAvrcp13ElementAttributesPlaying() mTitle : " + mTitle);
            Logger.v(TAG, "retAvrcp13ElementAttributesPlaying() mArtist : " + mArtist);
            Logger.v(TAG, "retAvrcp13ElementAttributesPlaying() mAlbum : " + mAlbum);
            mDoCallbackAvrcp.retAvrcp13ElementAttributesPlaying(metadataAtrributeIds, texts);
        }

        @Override
        public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) throws RemoteException {
            Logger.v(TAG, "retAvrcp13PlayStatus() songLen : " + songLen + ",songPos : " + songPos + ",statusId : " + statusId);
            StringBuilder builder = new StringBuilder();
            switch (statusId) {
                case NfDef.AVRCP_PLAYING_STATUS_ID_STOPPED:
                    builder.append("AVRCP_PLAYING_STATUS_ID_STOPPED");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PLAYING:
                    builder.append("AVRCP_PLAYING_STATUS_ID_PLAYING");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PAUSED:
                    builder.append("AVRCP_PLAYING_STATUS_ID_PAUSED");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_FWD_SEEK:
                    builder.append("AVRCP_PLAYING_STATUS_ID_FWD_SEEK");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_REW_SEEK:
                    builder.append("AVRCP_PLAYING_STATUS_ID_REW_SEEK");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_ERROR:
                    builder.append("AVRCP_PLAYING_STATUS_ID_ERROR");
                    break;
            }
            Logger.v(TAG, builder.toString());
            mDoCallbackAvrcp.retAvrcp13PlayStatus(songLen, songPos, statusId);
        }

        @Override
        public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13RegisterEventWatcherSuccess() eventId : " + eventId);
            mDoCallbackAvrcp.onAvrcp13RegisterEventWatcherSuccess(eventId);
        }

        @Override
        public void onAvrcp13RegisterEventWatcherFail(byte eventId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13RegisterEventWatcherFail()");
            mDoCallbackAvrcp.onAvrcp13RegisterEventWatcherFail(eventId);
        }

        @Override
        public void onAvrcp13EventPlaybackStatusChanged(byte statusId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventPlaybackStatusChanged() statusId : " + statusId);
            StringBuilder builder = new StringBuilder();
            switch (statusId) {
                case NfDef.AVRCP_PLAYING_STATUS_ID_STOPPED:
                    builder.append("AVRCP_PLAYING_STATUS_ID_STOPPED");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PLAYING:
                    builder.append("AVRCP_PLAYING_STATUS_ID_PLAYING");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_PAUSED:
                    builder.append("AVRCP_PLAYING_STATUS_ID_PAUSED");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_FWD_SEEK:
                    builder.append("AVRCP_PLAYING_STATUS_ID_FWD_SEEK");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_REW_SEEK:
                    builder.append("AVRCP_PLAYING_STATUS_ID_REW_SEEK");
                    break;
                case NfDef.AVRCP_PLAYING_STATUS_ID_ERROR:
                    builder.append("AVRCP_PLAYING_STATUS_ID_ERROR");
                    break;
            }
            Logger.v(TAG, builder.toString());
            mDoCallbackAvrcp.onAvrcp13EventPlaybackStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventTrackChanged(long elementId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventTrackChanged() elementId : " + elementId);
            mCommandAvrcp.reqAvrcp13GetElementAttributesPlaying();
            mDoCallbackAvrcp.onAvrcp13EventTrackChanged(elementId);
        }

        @Override
        public void onAvrcp13EventTrackReachedEnd() throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventTrackReachedEnd()");
            mDoCallbackAvrcp.onAvrcp13EventTrackReachedEnd();
        }

        @Override
        public void onAvrcp13EventTrackReachedStart() throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventTrackReachedStart()");
            mDoCallbackAvrcp.onAvrcp13EventTrackReachedStart();
        }

        @Override
        public void onAvrcp13EventPlaybackPosChanged(long songPos) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventPlaybackPosChanged() songPos : " + songPos);
            mDoCallbackAvrcp.onAvrcp13EventPlaybackPosChanged(songPos);
        }

        @Override
        public void onAvrcp13EventBatteryStatusChanged(byte statusId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventBatteryStatusChanged() statusId : " + statusId);
            mDoCallbackAvrcp.onAvrcp13EventBatteryStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventSystemStatusChanged(byte statusId) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventSystemStatusChanged() statusId : " + statusId);
            mDoCallbackAvrcp.onAvrcp13EventSystemStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) throws RemoteException {
            Logger.v(TAG, "onAvrcp13EventPlayerSettingChanged()"
                    + "\nattributeIds : " + (null != attributeIds ? Arrays.toString(attributeIds) : "null")
                    + "\nvalueIds : " + (null != valueIds ? Arrays.toString(valueIds) : "null"));
            mDoCallbackAvrcp.onAvrcp13EventPlayerSettingChanged(attributeIds, valueIds);
        }

        @Override
        public void onAvrcp14EventNowPlayingContentChanged() throws RemoteException {
            Logger.v(TAG, "onAvrcp14EventNowPlayingContentChanged()");
            mDoCallbackAvrcp.onAvrcp14EventNowPlayingContentChanged();
        }

        @Override
        public void onAvrcp14EventAvailablePlayerChanged() throws RemoteException {
            Logger.v(TAG, "onAvrcp14EventAvailablePlayerChanged()");
            mDoCallbackAvrcp.onAvrcp14EventAvailablePlayerChanged();
        }

        @Override
        public void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) throws RemoteException {
            Logger.v(TAG, "onAvrcp14EventAddressedPlayerChanged() playerId : " + playerId + " , uidCounter : " + uidCounter);
            mDoCallbackAvrcp.onAvrcp14EventAddressedPlayerChanged(playerId, uidCounter);
        }

        @Override
        public void onAvrcp14EventUidsChanged(int uidCounter) throws RemoteException {
            Logger.v(TAG, "onAvrcp14EventUidsChanged()");
            mDoCallbackAvrcp.onAvrcp14EventUidsChanged(uidCounter);
        }

        @Override
        public void onAvrcp14EventVolumeChanged(byte volume) throws RemoteException {
            Logger.v(TAG, "onAvrcp14EventVolumeChanged()");
            mDoCallbackAvrcp.onAvrcp14EventVolumeChanged(volume);
        }

        @Override
        public void retAvrcp14SetAddressedPlayerSuccess() throws RemoteException {
            Logger.v(TAG, "retAvrcp14SetAddressedPlayerSuccess()");
            mDoCallbackAvrcp.retAvrcp14SetAddressedPlayerSuccess();
        }

        @Override
        public void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) throws RemoteException {

            Logger.v(TAG, "retAvrcp14SetBrowsedPlayerSuccess()"
                    + "\npath : " + (null != path ? Arrays.toString(path) : "null")
                    + "\nuidCounter : " + uidCounter
                    + "\nitemCount : " + itemCount);
            mDoCallbackAvrcp.retAvrcp14SetBrowsedPlayerSuccess(path, uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14FolderItems(int uidCounter, long itemCount) throws RemoteException {
            Logger.v(TAG, "retAvrcp14FolderItems() uidCounter ; " + uidCounter + " , itemCount : " + itemCount);
            mDoCallbackAvrcp.retAvrcp14FolderItems(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14MediaItems(int uidCounter, long itemCount) throws RemoteException {
            Logger.v(TAG, "retAvrcp14MediaItems() uidCounter ; " + uidCounter + " , itemCount : " + itemCount);
            mDoCallbackAvrcp.retAvrcp14MediaItems(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14ChangePathSuccess(long itemCount) throws RemoteException {
            Logger.v(TAG, "retAvrcp14ChangePathSuccess() itemCount ; " + itemCount);
            mDoCallbackAvrcp.retAvrcp14ChangePathSuccess(itemCount);
        }

        @Override
        public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
            Logger.v(TAG, "retAvrcp14ItemAttributes() "
                    + "\nmetadataAtrributeIds : " + (null != metadataAtrributeIds ? Arrays.toString(metadataAtrributeIds) : "null")
                    + "\ntexts : " + (null != texts ? Arrays.toString(texts) : "null"));
            mDoCallbackAvrcp.retAvrcp14ItemAttributes(metadataAtrributeIds, texts);
        }

        @Override
        public void retAvrcp14PlaySelectedItemSuccess() throws RemoteException {
            Logger.v(TAG, "retAvrcp14PlaySelectedItemSuccess()");
            mDoCallbackAvrcp.retAvrcp14PlaySelectedItemSuccess();
        }

        @Override
        public void retAvrcp14SearchResult(int uidCounter, long itemCount) throws RemoteException {
            Logger.v(TAG, "retAvrcp14SearchResult() uidCounter ; " + uidCounter + " , itemCount : " + itemCount);
            mDoCallbackAvrcp.retAvrcp14SearchResult(uidCounter, itemCount);
        }

        @Override
        public void retAvrcp14AddToNowPlayingSuccess() throws RemoteException {
            Logger.v(TAG, "retAvrcp14AddToNowPlayingSuccess()");
            mDoCallbackAvrcp.retAvrcp14AddToNowPlayingSuccess();
        }

        @Override
        public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) throws RemoteException {
            Logger.v(TAG, "retAvrcp14SetAbsoluteVolumeSuccess() volume : " + volume);
            mDoCallbackAvrcp.retAvrcp14SetAbsoluteVolumeSuccess(volume);
        }

        @Override
        public void onAvrcpErrorResponse(int opId, int reason, byte eventId) throws RemoteException {
            Logger.v(TAG, "onAvrcpErrorResponse() opId : " + opId + " , reason : " + reason + " , eventId : " + eventId);
            mDoCallbackAvrcp.onAvrcpErrorResponse(opId, reason, eventId);
        }
    };

    // INfCallbackPbap.aidl-------------------------------------------------------------------------
    private INfCallbackPbap.Stub mCallbackPbap = new INfCallbackPbap.Stub() {
        @Override
        public void onPbapServiceReady() throws RemoteException {
            Logger.i(TAG, "onPbapServiceReady()");
            mDoCallbackPbap.onPbapServiceReady();
        }

        @Override
        public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) throws RemoteException {
            Logger.i(TAG, "onPbapStateChanged() address : " + address + " , prevState : " + prevState + " , reason : " + reason + " , counts : " + counts);
            mDoCallbackPbap.onPbapStateChanged(address, prevState, newState, reason, counts);
        }

        @Override
        public void retPbapDownloadedContact(NfPbapContact contact) throws RemoteException {
            Logger.v(TAG, "retPbapDownloadedContact()");
            mDoCallbackPbap.retPbapDownloadedContact(contact);
        }

        @Override
        public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) throws RemoteException {
            Logger.v(TAG, "retPbapDownloadedCallLog() address : " + address
                    + " , firstName : " + firstName
                    + " , middleName : " + middleName
                    + " , number : " + number
                    + " , type : " + type
                    + " , timestamp : " + timestamp);
            mDoCallbackPbap.retPbapDownloadedCallLog(address, firstName, middleName, lastName, number, type, timestamp);
        }

        @Override
        public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) throws RemoteException {
            Logger.v(TAG, "onPbapDownloadNotify() address : " + address
                    + " , storage : " + storage
                    + " , totalContacts : " + totalContacts
                    + " , downloadedContacts : " + downloadedContacts);
            mDoCallbackPbap.onPbapDownloadNotify(address, storage, totalContacts, downloadedContacts);
        }

        @Override
        public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) throws RemoteException {
            Logger.v(TAG, "retPbapDatabaseQueryNameByNumber() address : " + address
                    + " , target : " + target
                    + " , name : " + name
                    + " , isSuccess : " + isSuccess);
            mDoCallbackPbap.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
            mDoCallbackHfp.retPbapDatabaseQueryNameByNumber(address, target, name, isSuccess);
        }

        @Override
        public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) throws RemoteException {
            Logger.v(TAG, "retPbapDatabaseQueryNameByPartialNumber()"
                    + "\naddress : " + address
                    + "\ntarget : " + target
                    + "\nnames : " + ((null != names) ? Arrays.toString(names) : "null")
                    + "\nnumbers : " + (null != numbers ? Arrays.toString(numbers) : "null")
                    + "\nisSuccess : " + isSuccess);
            mDoCallbackPbap.retPbapDatabaseQueryNameByPartialNumber(address, target, names, numbers, isSuccess);
        }

        @Override
        public void retPbapDatabaseAvailable(String address) throws RemoteException {
            Logger.v(TAG, "retPbapDatabaseAvailable() address : " + address);
            mDoCallbackPbap.retPbapDatabaseAvailable(address);
        }

        @Override
        public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
            Logger.v(TAG, "retPbapDeleteDatabaseByAddressCompleted() address : " + address + " , isSuccess : " + isSuccess);
            mDoCallbackPbap.retPbapDeleteDatabaseByAddressCompleted(address, isSuccess);
        }

        @Override
        public void retPbapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
            Logger.v(TAG, "retPbapCleanDatabaseCompleted() isSuccess : " + isSuccess);
            mDoCallbackPbap.retPbapCleanDatabaseCompleted(isSuccess);
        }
    };

    // INfCallbackBluetooth.aidl--------------------------------------------------------------------
    private INfCallbackBluetooth.Stub mCallbackBluetooth = new INfCallbackBluetooth.Stub() {
        @Override
        public void onBluetoothServiceReady() throws RemoteException {
            Logger.v(TAG, "onBluetoothServiceReady()");
            mDoCallbackBluetooth.onBluetoothServiceReady();
        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAdapterStateChanged() prevState : " + prevState + " , newState : " + newState);
            mDoCallbackBluetooth.onAdapterStateChanged(prevState, newState);
        }

        @Override
        public void onAdapterDiscoverableModeChanged(int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onAdapterDiscoverableModeChanged() prevState : " + prevState + " , newState : " + newState);
            mDoCallbackBluetooth.onAdapterDiscoverableModeChanged(prevState, newState);
        }

        @Override
        public void onAdapterDiscoveryStarted() throws RemoteException {
            Logger.v(TAG, "onAdapterDiscoveryStarted()");
            mDoCallbackBluetooth.onAdapterDiscoveryStarted();
        }

        @Override
        public void onAdapterDiscoveryFinished() throws RemoteException {
            Logger.v(TAG, "onAdapterDiscoveryFinished()");
            mDoCallbackBluetooth.onAdapterDiscoveryFinished();
        }

        @Override
        public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) throws RemoteException {
            Logger.v(TAG, "retPairedDevices()"
                    + "\naddress : " + (null != address ? Arrays.toString(address) : "null")
                    + "\nname : " + (null != name ? Arrays.toString(name) : "null")
                    + "\nsupportProfile : " + (null != supportProfile ? Arrays.toString(supportProfile) : "null")
                    + "\ncategory : " + (null != category ? Arrays.toString(category) : "null"));
            mDoCallbackBluetooth.retPairedDevices(elements, address, name, supportProfile, category);
        }

        @Override
        public void onDeviceFound(String address, String name, byte category) throws RemoteException {
            Logger.v(TAG, "onDeviceFound() address : " + address + " , name : " + name + " , category : " + category);
            mDoCallbackBluetooth.onDeviceFound(address, name, category);
        }

        @Override
        public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onDeviceBondStateChanged() address : " + address + " , name : " + name + " , prevState : " + prevState + " , newState : " + newState);
            mDoCallbackBluetooth.onDeviceBondStateChanged(address, name, prevState, newState);
        }

        @Override
        public void onDeviceUuidsUpdated(String address, String name, int supportProfile) throws RemoteException {
            Logger.v(TAG, "onDeviceUuidsUpdated() address : " + address + " , name : " + name + " , supportProfile : " + supportProfile);
            mDoCallbackBluetooth.onDeviceUuidsUpdated(address, name, supportProfile);
        }

        @Override
        public void onLocalAdapterNameChanged(String name) throws RemoteException {
            Logger.v(TAG, "onLocalAdapterNameChanged() name : " + name);
            mDoCallbackBluetooth.onLocalAdapterNameChanged(name);
        }

        @Override
        public void onDeviceOutOfRange(String address) throws RemoteException {
            Logger.v(TAG, "onDeviceOutOfRange() address : " + address);
            mDoCallbackBluetooth.onDeviceOutOfRange(address);
        }

        @Override
        public void onDeviceAclDisconnected(String address) throws RemoteException {
            Logger.v(TAG, "onDeviceAclDisconnected() address : " + address);
        }

        @Override
        public void onBtRoleModeChanged(int mode) throws RemoteException {
            Logger.v(TAG, "onBtRoleModeChanged() mode : " + mode);
            mDoCallbackBluetooth.onBtRoleModeChanged(mode);
        }

        @Override
        public void onBtAutoConnectStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.v(TAG, "onBtAutoConnectStateChanged() address : " + address + " , prevState : " + prevState + " , newState : " + newState);
            mDoCallbackBluetooth.onBtAutoConnectStateChanged(address, prevState, newState);
        }
    };

    // UiCommand.aidl-------------------------------------------------------------------------------

    @Override
    public String getUiServiceVersionName() throws RemoteException {
        return this.mVersionName;
    }

    @Override
    public boolean isAvrcpServiceReady() throws RemoteException {
        if (null != mCommandAvrcp) {
            return this.mCommandAvrcp.isAvrcpServiceReady();
        }
        return false;
    }

    @Override
    public boolean isA2dpServiceReady() throws RemoteException {
        if (null != mCommandA2dp) {
            return this.mCommandA2dp.isA2dpServiceReady();
        }
        return false;
    }

    @Override
    public boolean isSppServiceReady() throws RemoteException {
        return false;
    }

    @Override
    public boolean isBluetoothServiceReady() throws RemoteException {
        if (null != mCommandBluetooth) {
            return this.mCommandBluetooth.isBluetoothServiceReady();
        }
        return false;
    }

    @Override
    public boolean isHfpServiceReady() throws RemoteException {
        if (null != mCommandHfp) {
            return this.mCommandHfp.isHfpServiceReady();
        }
        return false;
    }

    @Override
    public boolean isHidServiceReady() throws RemoteException {
        return false;
    }

    @Override
    public boolean isPbapServiceReady() throws RemoteException {
        if (null != mCommandPbap) {
            return this.mCommandPbap.isPbapServiceReady();
        }
        return false;
    }

    @Override
    public boolean isOppServiceReady() throws RemoteException {
        return false;
    }

    @Override
    public boolean isMapServiceReady() throws RemoteException {
        return false;
    }

    @Override
    public boolean registerA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
        this.mDoCallbackA2dp.register(cb);
        return false;
    }

    @Override
    public boolean unregisterA2dpCallback(UiCallbackA2dp cb) throws RemoteException {
        this.mDoCallbackA2dp.unregister(cb);
        return false;
    }

    @Override
    public int getA2dpConnectionState() throws RemoteException {
        if (null != mCommandA2dp) {
            return this.mCommandA2dp.getA2dpConnectionState();
        }
        return -1;
    }

    @Override
    public boolean isA2dpConnected() throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.isA2dpConnected();
        }
        return false;
    }

    @Override
    public String getA2dpConnectedAddress() throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.getA2dpConnectedAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqA2dpConnect(String address) throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.reqA2dpConnect(address);
        }
        return false;
    }

    @Override
    public boolean reqA2dpDisconnect(String address) throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.reqA2dpDisconnect(address);
        }
        return false;
    }

    @Override
    public void pauseA2dpRender() throws RemoteException {
        if (null != mCommandA2dp) {
            mCommandA2dp.pauseA2dpRender();
        }
    }

    @Override
    public void startA2dpRender() throws RemoteException {
        if (null != mCommandA2dp) {
            mCommandA2dp.startA2dpRender();
        }
    }

    @Override
    public boolean setA2dpLocalVolume(float vol) throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.setA2dpLocalVolume(vol);
        }
        return false;
    }

    @Override
    public boolean setA2dpStreamType(int type) throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.setA2dpStreamType(type);
        }
        return false;
    }

    @Override
    public int getA2dpStreamType() throws RemoteException {
        if (null != mCommandA2dp) {
            return mCommandA2dp.getA2dpStreamType();
        }
        return -1;
    }

    @Override
    public boolean registerAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
        mDoCallbackAvrcp.register(cb);
        return false;
    }

    @Override
    public boolean unregisterAvrcpCallback(UiCallbackAvrcp cb) throws RemoteException {
        mDoCallbackAvrcp.unregister(cb);
        return false;
    }

    @Override
    public int getAvrcpConnectionState() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.getAvrcpConnectionState();
        }
        return -1;
    }

    @Override
    public boolean isAvrcpConnected() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.isAvrcpConnected();
        }
        return false;
    }

    @Override
    public String getAvrcpConnectedAddress() throws RemoteException {
        if (null != mCommandAvrcp) {
            mCommandAvrcp.getAvrcpConnectedAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqAvrcpConnect(String address) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpConnect(address);
        }
        return false;
    }

    @Override
    public boolean reqAvrcpDisconnect(String address) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpDisconnect(address);
        }
        return false;
    }

    @Override
    public boolean isAvrcp13Supported(String address) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.isAvrcp13Supported(address);
        }
        return false;
    }

    @Override
    public boolean isAvrcp14Supported(String address) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.isAvrcp14Supported(address);
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPlay() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpPlay();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStop() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpStop();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPause() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpPause();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpForward() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpBackward() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpBackward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeUp() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpVolumeUp();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeDown() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpVolumeDown();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartFastForward() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpStartFastForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopFastForward() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpStopFastForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartRewind() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpStartRewind();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopRewind() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpStopRewind();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetCapabilitiesSupportEvent() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetCapabilitiesSupportEvent();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingAttributesList() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingAttributesList();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingValuesList(attributeId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingCurrentValues() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetPlayerSettingCurrentValues();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13SetPlayerSettingValue(byte attributeId, byte valueId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13SetPlayerSettingValue(attributeId, valueId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetElementAttributesPlaying() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetElementAttributesPlaying();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayStatus() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13GetPlayStatus();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpRegisterEventWatcher(eventId, interval);
        }
        return false;
    }

    @Override
    public boolean reqAvrcpUnregisterEventWatcher(byte eventId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcpUnregisterEventWatcher(eventId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13NextGroup() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13NextGroup();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp13PreviousGroup() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp13PreviousGroup();
        }
        return false;
    }

    @Override
    public boolean isAvrcp14BrowsingChannelEstablished() throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.isAvrcp14BrowsingChannelEstablished();
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAddressedPlayer(int playerId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14SetAddressedPlayer(playerId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetBrowsedPlayer(int playerId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14SetBrowsedPlayer(playerId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14GetFolderItems(byte scopeId) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14GetFolderItems(scopeId);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14ChangePath(uidCounter, uid, direction);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter, long uid) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14GetItemAttributes(scopeId, uidCounter, uid);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter, long uid) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14PlaySelectedItem(scopeId, uidCounter, uid);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14Search(String text) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14Search(text);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter, long uid) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14AddToNowPlaying(scopeId, uidCounter, uid);
        }
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAbsoluteVolume(byte volume) throws RemoteException {
        if (null != mCommandAvrcp) {
            return mCommandAvrcp.reqAvrcp14SetAbsoluteVolume(volume);
        }
        return false;
    }

    @Override
    public boolean registerBtCallback(UiCallbackBluetooth cb) throws RemoteException {
        mDoCallbackBluetooth.register(cb);
        return false;
    }

    @Override
    public boolean unregisterBtCallback(UiCallbackBluetooth cb) throws RemoteException {
        mDoCallbackBluetooth.unregister(cb);
        return false;
    }

    @Override
    public String getNfServiceVersionName() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getNfServiceVersionName();
        }
        return "";
    }

    @Override
    public boolean setBtEnable(boolean enable) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.setBtEnable(enable);
        }
        return false;
    }

    @Override
    public boolean setBtDiscoverableTimeout(int timeout) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.setBtDiscoverableTimeout(timeout);
        }
        return false;
    }

    @Override
    public boolean startBtDiscovery() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.startBtDiscovery();
        }
        return false;
    }

    @Override
    public boolean cancelBtDiscovery() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.cancelBtDiscovery();
        }
        return false;
    }

    @Override
    public boolean reqBtPair(String address) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.reqBtPair(address);
        }
        return false;
    }

    @Override
    public boolean reqBtUnpair(String address) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.reqBtUnpair(address);
        }
        return false;
    }

    @Override
    public boolean reqBtPairedDevices() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.reqBtPairedDevices();
        }
        return false;
    }

    @Override
    public String getBtLocalName() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtLocalName();
        }
        return null;
    }

    @Override
    public String getBtRemoteDeviceName(String address) throws RemoteException {
        Logger.i(TAG, "getBtRemoteDeviceName() " + address);
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtRemoteDeviceName(address);
        }
        return null;
    }

    @Override
    public String getBtLocalAddress() throws RemoteException {
        Logger.i(TAG, "getBtLocalAddress()");
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtLocalAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean setBtLocalName(String name) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.setBtLocalName(name);
        }
        return false;
    }

    @Override
    public boolean isBtEnabled() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.isBtEnabled();
        }
        return false;
    }

    @Override
    public int getBtState() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtState();
        }
        return NfDef.STATE_NOT_INITIALIZED;
    }

    @Override
    public boolean isBtDiscovering() throws RemoteException {
        if (null != mCommandBluetooth) {
            mCommandBluetooth.isBtDiscovering();
        }
        return false;
    }

    @Override
    public boolean isBtDiscoverable() throws RemoteException {
        if (null != mCommandBluetooth) {
            mCommandBluetooth.isBtDiscoverable();
        }
        return false;
    }

    @Override
    public boolean isBtAutoConnectEnable() throws RemoteException {
        if (null != mCommandBluetooth) {
            mCommandBluetooth.isBtAutoConnectEnable();
        }
        return false;
    }

    @Override
    public int reqBtConnectHfpA2dp(String address) throws RemoteException {
        if (null != mCommandBluetooth) {
            mCommandBluetooth.reqBtConnectHfpA2dp(address);
        }
        return -1;
    }

    @Override
    public int reqBtDisconnectAll() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.reqBtDisconnectAll();
        }
        return -1;
    }

    @Override
    public int getBtRemoteUuids(String address) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtRemoteUuids(address);
        }
        return -1;
    }

    @Override
    public boolean switchBtRoleMode(int mode) throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.switchBtRoleMode(mode);
        }
        return false;
    }

    @Override
    public int getBtRoleMode() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtRoleMode();
        }
        return -1;
    }

    @Override
    public void setBtAutoConnect(int condition, int period) throws RemoteException {
        if (null != mCommandBluetooth) {
            mCommandBluetooth.setBtAutoConnect(condition, period);
        }
    }

    @Override
    public int getBtAutoConnectCondition() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtAutoConnectCondition();
        }
        return -1;
    }

    @Override
    public int getBtAutoConnectPeriod() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtAutoConnectPeriod();
        }
        return -1;
    }

    @Override
    public int getBtAutoConnectState() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtAutoConnectState();
        }
        return -1;
    }

    @Override
    public String getBtAutoConnectingAddress() throws RemoteException {
        if (null != mCommandBluetooth) {
            return mCommandBluetooth.getBtAutoConnectingAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean registerHfpCallback(UiCallbackHfp cb) throws RemoteException {
        return mDoCallbackHfp.register(cb);
    }

    @Override
    public boolean unregisterHfpCallback(UiCallbackHfp cb) throws RemoteException {
        return mDoCallbackHfp.unregister(cb);
    }

    @Override
    public int getHfpConnectionState() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpConnectionState();
        }
        return NfDef.STATE_NOT_INITIALIZED;
    }

    @Override
    public boolean isHfpConnected() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpConnected();
        }
        return false;
    }

    @Override
    public String getHfpConnectedAddress() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpConnectedAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public int getHfpAudioConnectionState() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpAudioConnectionState();
        }
        return NfDef.STATE_NOT_INITIALIZED;
    }

    @Override
    public boolean reqHfpConnect(String address) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpConnect(address);
        }
        return false;
    }

    @Override
    public boolean reqHfpDisconnect(String address) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpDisconnect(address);
        }
        return false;
    }

    @Override
    public int getHfpRemoteSignalStrength() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpRemoteSignalStrength();
        }
        return 0;
    }

    @Override
    public List<NfHfpClientCall> getHfpCallList() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpCallList();
        }
        return null;
    }

    @Override
    public boolean isHfpRemoteOnRoaming() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpRemoteOnRoaming();
        }
        return false;
    }

    @Override
    public int getHfpRemoteBatteryIndicator() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpRemoteBatteryIndicator();
        }
        return -1;
    }

    @Override
    public boolean isHfpRemoteTelecomServiceOn() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpRemoteTelecomServiceOn();
        }
        return false;
    }

    @Override
    public boolean isHfpRemoteVoiceDialOn() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpRemoteVoiceDialOn();
        }
        return false;
    }

    @Override
    public boolean reqHfpDialCall(String number) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpDialCall(number);
        }
        return false;
    }

    @Override
    public boolean reqHfpReDial() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpReDial();
        }
        return false;
    }

    @Override
    public boolean reqHfpMemoryDial(String index) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpMemoryDial(index);
        }
        return false;
    }

    @Override
    public boolean reqHfpAnswerCall(int flag) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpAnswerCall(flag);
        }
        return false;
    }

    @Override
    public boolean reqHfpRejectIncomingCall() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpRejectIncomingCall();
        }
        return false;
    }

    @Override
    public boolean reqHfpTerminateCurrentCall() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpTerminateCurrentCall();
        }
        return false;
    }

    @Override
    public boolean reqHfpSendDtmf(String number) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpSendDtmf(number);
        }
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToCarkit() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpAudioTransferToCarkit();
        }
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToPhone() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpAudioTransferToPhone();
        }
        return false;
    }

    @Override
    public String getHfpRemoteNetworkOperator() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpRemoteNetworkOperator();
        }
        return null;
    }

    @Override
    public String getHfpRemoteSubscriberNumber() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.getHfpRemoteSubscriberNumber();
        }
        return null;
    }

    @Override
    public boolean reqHfpVoiceDial(boolean enable) throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.reqHfpVoiceDial(enable);
        }
        return false;
    }

    @Override
    public void pauseHfpRender() throws RemoteException {
        if (null != mCommandHfp) {
            mCommandHfp.pauseHfpRender();
        }
    }

    @Override
    public void startHfpRender() throws RemoteException {
        if (null != mCommandHfp) {
            mCommandHfp.startHfpRender();
        }
    }

    @Override
    public boolean isHfpMicMute() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpMicMute();
        }
        return false;
    }

    @Override
    public void muteHfpMic(boolean mute) throws RemoteException {
        if (null != mCommandHfp) {
            mCommandHfp.muteHfpMic(mute);
        }
    }

    @Override
    public boolean isHfpInBandRingtoneSupport() throws RemoteException {
        if (null != mCommandHfp) {
            return mCommandHfp.isHfpInBandRingtoneSupport();
        }
        return false;
    }

    @Override
    public boolean registerPbapCallback(UiCallbackPbap cb) throws RemoteException {
        return mDoCallbackPbap.register(cb);
    }

    @Override
    public boolean unregisterPbapCallback(UiCallbackPbap cb) throws RemoteException {
        return mDoCallbackPbap.unregister(cb);
    }

    @Override
    public int getPbapConnectionState() throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.getPbapConnectionState();
        }
        return NfDef.STATE_NOT_INITIALIZED;
    }

    @Override
    public boolean isPbapDownloading() throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.isPbapDownloading();
        }
        return false;
    }

    @Override
    public String getPbapDownloadingAddress() throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.getPbapDownloadingAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqPbapDownload(String address, int storage, int property) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownload(address, storage, property);
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadRange(address, storage, property, startPos, offset);
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadToDatabase(String address, int storage, int property) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadToDatabase(address, storage, property);
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToDatabase(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadRangeToDatabase(address, storage, property, startPos, offset);
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadToContactsProvider(String address, int storage, int property) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadToContactsProvider(address, storage, property);
        }
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadRangeToContactsProvider(address, storage, property, startPos, offset);
        }
        return false;
    }

    @Override
    public void reqPbapDatabaseQueryNameByNumber(String address, String target) throws RemoteException {
        if (null != mCommandPbap) {
            mCommandPbap.reqPbapDatabaseQueryNameByNumber(address, target);
        }
    }

    @Override
    public void reqPbapDatabaseQueryNameByPartialNumber(String address, String target, int findPosition) throws RemoteException {
        if (null != mCommandPbap) {
            mCommandPbap.reqPbapDatabaseQueryNameByPartialNumber(address, target, findPosition);
        }
    }

    @Override
    public void reqPbapDatabaseAvailable(String address) throws RemoteException {
        if (null != mCommandPbap) {
            mCommandPbap.reqPbapDatabaseAvailable(address);
        }
    }

    @Override
    public void reqPbapDeleteDatabaseByAddress(String address) throws RemoteException {
        if (null != mCommandPbap) {
            mCommandPbap.reqPbapDeleteDatabaseByAddress(address);
        }
    }

    @Override
    public void reqPbapCleanDatabase() throws RemoteException {
        if (null != mCommandPbap) {
            mCommandPbap.reqPbapCleanDatabase();
        }
    }

    @Override
    public boolean reqPbapDownloadInterrupt(String address) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.reqPbapDownloadInterrupt(address);
        }
        return false;
    }

    @Override
    public boolean setPbapDownloadNotify(int frequency) throws RemoteException {
        if (null != mCommandPbap) {
            return mCommandPbap.setPbapDownloadNotify(frequency);
        }
        return false;
    }

    // SPP------------------------------------------------------------------------------------------
    @Override
    public boolean registerSppCallback(UiCallbackSpp cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterSppCallback(UiCallbackSpp cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqSppConnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqSppDisconnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public void reqSppConnectedDeviceAddressList() throws RemoteException {

    }

    @Override
    public boolean isSppConnected(String address) throws RemoteException {
        return false;
    }

    @Override
    public void reqSppSendData(String address, byte[] sppData) throws RemoteException {

    }

    // HID------------------------------------------------------------------------------------------
    @Override
    public boolean registerHidCallback(UiCallbackHid cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterHidCallback(UiCallbackHid cb) throws RemoteException {
        return false;
    }

    @Override
    public int getHidConnectionState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isHidConnected() throws RemoteException {
        return false;
    }

    @Override
    public String getHidConnectedAddress() throws RemoteException {
        return null;
    }

    @Override
    public boolean reqHidConnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHidDisconnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqSendHidMouseCommand(int button, int offset_x, int offset_y, int wheel) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqSendHidVirtualKeyCommand(int key_1, int key_2) throws RemoteException {
        return false;
    }

    // MAP------------------------------------------------------------------------------------------
    @Override
    public boolean registerMapCallback(UiCallbackMap cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterMapCallback(UiCallbackMap cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapDownloadSingleMessage(String address, int folder, String handle, int storage) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapDownloadMessage(String address, int folder, boolean isContentDownload, int count, int startPos, int storage, String periodBegin, String periodEnd, String sender, String recipient, int readStatus, int typeFilter) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapRegisterNotification(String address, boolean downloadNewMessage) throws RemoteException {
        return false;
    }

    @Override
    public void reqMapUnregisterNotification(String address) throws RemoteException {

    }

    @Override
    public boolean isMapNotificationRegistered(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapDownloadInterrupt(String address) throws RemoteException {
        return false;
    }

    @Override
    public void reqMapDatabaseAvailable() throws RemoteException {

    }

    @Override
    public void reqMapDeleteDatabaseByAddress(String address) throws RemoteException {

    }

    @Override
    public void reqMapCleanDatabase() throws RemoteException {

    }

    @Override
    public int getMapCurrentState(String address) throws RemoteException {
        return 0;
    }

    @Override
    public int getMapRegisterState(String address) throws RemoteException {
        return 0;
    }

    @Override
    public boolean reqMapSendMessage(String address, String message, String target) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapDeleteMessage(String address, int folder, String handle) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqMapChangeReadStatus(String address, int folder, String handle, boolean isReadStatus) throws RemoteException {
        return false;
    }

    @Override
    public boolean setMapDownloadNotify(int frequency) throws RemoteException {
        return false;
    }

    @Override
    public boolean registerOppCallback(UiCallbackOpp cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterOppCallback(UiCallbackOpp cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean setOppFilePath(String path) throws RemoteException {
        return false;
    }

    @Override
    public String getOppFilePath() throws RemoteException {
        return null;
    }

    @Override
    public boolean reqOppAcceptReceiveFile(boolean accept) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqOppInterruptReceiveFile() throws RemoteException {
        return false;
    }

    @Override
    public void setTargetAddress(String address) throws RemoteException {

    }

    @Override
    public String getTargetAddress() throws RemoteException {
        return null;
    }

    @Override
    public void reqAvrcpUpdateSongStatus() throws RemoteException {
        mDoCallbackAvrcp.retAvrcpUpdateSongStatus(mArtist, mAlbum, mTitle);
    }

    @Override
    public boolean isGattServiceReady() throws RemoteException {
        return false;
    }

    @Override
    public boolean registerGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterGattServerCallback(UiCallbackGattServer cb) throws RemoteException {
        return false;
    }

    @Override
    public int getGattServerConnectionState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean reqGattServerDisconnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerBeginServiceDeclaration(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerAddCharacteristic(ParcelUuid charUuid, int properties, int permissions) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerAddDescriptor(ParcelUuid descUuid, int permissions) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerEndServiceDeclaration() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerRemoveService(int srvcType, ParcelUuid srvcUuid) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerClearServices() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerListen(boolean listen) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerSendResponse(String address, int requestId, int status, int offset, byte[] value) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqGattServerSendNotification(String address, int srvcType, ParcelUuid srvcUuid, ParcelUuid charUuid, boolean confirm, byte[] value) throws RemoteException {
        return false;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattServiceUuidList() throws RemoteException {
        return null;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattCharacteristicUuidList(ParcelUuid srvcUuid) throws RemoteException {
        return null;
    }

    @Override
    public List<ParcelUuid> getGattAddedGattDescriptorUuidList(ParcelUuid srvcUuid, ParcelUuid charUuid) throws RemoteException {
        return null;
    }
}