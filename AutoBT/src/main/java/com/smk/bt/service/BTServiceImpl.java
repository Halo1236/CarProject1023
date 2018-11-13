package com.smk.bt.service;


import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.os.RemoteException;
import android.util.Log;

import com.nforetek.bt.aidl.INfCallbackHfp;
import com.nforetek.bt.aidl.INfCommandA2dp;
import com.nforetek.bt.aidl.INfCommandAvrcp;
import com.nforetek.bt.aidl.INfCommandBluetooth;
import com.nforetek.bt.aidl.INfCommandHfp;
import com.nforetek.bt.aidl.INfCommandPbap;
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
import com.smk.bt.service.callbacks.DoCallbackA2dp;
import com.smk.bt.service.callbacks.DoCallbackAvrcp;
import com.smk.bt.service.callbacks.DoCallbackBluetooth;
import com.smk.bt.service.callbacks.DoCallbackHfp;
import com.smk.bt.service.callbacks.DoCallbackPbap;
import com.smk.bt.utils.Logger;

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

    public BTServiceImpl(Context ctx){
        this.mContext = ctx;
        mDoCallbackA2dp = new DoCallbackA2dp();
        mDoCallbackAvrcp = new DoCallbackAvrcp();
        mDoCallbackHfp = new DoCallbackHfp();
        mDoCallbackPbap = new DoCallbackPbap();
        mDoCallbackBluetooth = new DoCallbackBluetooth();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.e(TAG, "ready onServiceConnected");
            Logger.v(TAG,"IBinder className : " + className);

            if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_HFP))) {
                Logger.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_HFP + ")");
                mCommandHfp = INfCommandHfp.Stub.asInterface(service);
                if (mCommandHfp == null) {
                    Logger.e(TAG,"mCommandHfp is null!!");
                    return;
                }
                try {
                    mCommandHfp.registerHfpCallback(mCallbackHfp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_A2DP))) {
                Logger.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_A2DP +")");
                mCommandA2dp = INfCommandA2dp.Stub.asInterface(service);
                if (mCommandA2dp == null) {
                    Logger.e(TAG,"mCommandA2dp is null !!");
                    return;
                }
              /*  try {
                    mCommandA2dp.registerA2dpCallback(mCallbackA2dp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_AVRCP))) {
                Logger.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_AVRCP + ")");
                mCommandAvrcp = INfCommandAvrcp.Stub.asInterface(service);
                if (mCommandAvrcp == null) {
                    Logger.e(TAG,"mCommandAvrcp is null !!");
                    return;
                }
              /*  try {
                    mCommandAvrcp.registerAvrcpCallback(mCallbackAvrcp);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
            else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_PBAP))) {
                Logger.e(TAG,"ComponentName(" + NfDef.CLASS_SERVICE_PBAP + ")");
                mCommandPbap = INfCommandPbap.Stub.asInterface(service);
                if (mCommandPbap == null) {
                    Logger.e(TAG,"mCommandPbap is null !!");
                    return;
                }
              /*  try {
                    mCommandPbap.registerPbapCallback(mCallbackPbap);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
                      else if (className.equals(new ComponentName(NfDef.PACKAGE_NAME, NfDef.CLASS_SERVICE_BLUETOOTH))) {
                Logger.e(TAG, "ComponentName(" + NfDef.CLASS_SERVICE_BLUETOOTH + ")");
                mCommandBluetooth = INfCommandBluetooth.Stub.asInterface(service);
                if (mCommandBluetooth == null) {
                    Logger.e(TAG, "mCommandBluetooth is null !!");
                    return;
                }
              /*  try {
                    mCommandBluetooth.registerBtCallback(mCallbackBluetooth);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }*/
            }
            Logger.e(TAG, "end onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };


    // UiCallbackHfp.aidl
    private INfCallbackHfp.Stub mCallbackHfp = new INfCallbackHfp.Stub(){
        @Override
        public void onHfpServiceReady() throws RemoteException {

        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onHfpAudioStateChanged(String address, int prevState, int newState) throws RemoteException {

        }

        @Override
        public void onHfpVoiceDial(String address, boolean isVoiceDialOn) throws RemoteException {

        }

        @Override
        public void onHfpErrorResponse(String address, int code) throws RemoteException {

        }

        @Override
        public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) throws RemoteException {

        }

        @Override
        public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) throws RemoteException {

        }

        @Override
        public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) throws RemoteException {

        }

        @Override
        public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) throws RemoteException {

        }

        @Override
        public void onHfpCallChanged(String address, NfHfpClientCall call) throws RemoteException {

        }
    };

    // UiCommand.aidl

    @Override
    public String getUiServiceVersionName() throws RemoteException {
        return this.mVersionName;
    }

    @Override
    public boolean isAvrcpServiceReady() throws RemoteException {
        if(null != mCommandAvrcp){
            return this.mCommandAvrcp.isAvrcpServiceReady();
        }
        return false;
    }

    @Override
    public boolean isA2dpServiceReady() throws RemoteException {
        if(null != mCommandA2dp){
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
        if(null != mCommandBluetooth){
            return this.mCommandBluetooth.isBluetoothServiceReady();
        }
        return false;
    }

    @Override
    public boolean isHfpServiceReady() throws RemoteException {
        if(null != mCommandHfp){
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
        if(null != mCommandPbap){
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
        if(null != mCommandA2dp){
            return this.mCommandA2dp.getA2dpConnectionState();
        }
        return -1;
    }

    @Override
    public boolean isA2dpConnected() throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.isA2dpConnected();
        }
        return false;
    }

    @Override
    public String getA2dpConnectedAddress() throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.getA2dpConnectedAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqA2dpConnect(String address) throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.reqA2dpConnect(address);
        }
        return false;
    }

    @Override
    public boolean reqA2dpDisconnect(String address) throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.reqA2dpDisconnect(address);
        }
        return false;
    }

    @Override
    public void pauseA2dpRender() throws RemoteException {
        if(null != mCommandA2dp){
            mCommandA2dp.pauseA2dpRender();
        }
    }

    @Override
    public void startA2dpRender() throws RemoteException {
        if(null != mCommandA2dp){
            mCommandA2dp.startA2dpRender();
        }
    }

    @Override
    public boolean setA2dpLocalVolume(float vol) throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.setA2dpLocalVolume(vol);
        }
        return false;
    }

    @Override
    public boolean setA2dpStreamType(int type) throws RemoteException {
        if(null != mCommandA2dp){
            return mCommandA2dp.setA2dpStreamType(type);
        }
        return false;
    }

    @Override
    public int getA2dpStreamType() throws RemoteException {
        if(null != mCommandA2dp){
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
        if(null != mCommandAvrcp){
            return mCommandAvrcp.getAvrcpConnectionState();
        }
        return -1;
    }

    @Override
    public boolean isAvrcpConnected() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.isAvrcpConnected();
        }
        return false;
    }

    @Override
    public String getAvrcpConnectedAddress() throws RemoteException {
        if(null != mCommandAvrcp){
            mCommandAvrcp.getAvrcpConnectedAddress();
        }
        return NfDef.DEFAULT_ADDRESS;
    }

    @Override
    public boolean reqAvrcpConnect(String address) throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpConnect(address);
        }
        return false;
    }

    @Override
    public boolean reqAvrcpDisconnect(String address) throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpDisconnect(address);
        }
        return false;
    }

    @Override
    public boolean isAvrcp13Supported(String address) throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.isAvrcp13Supported(address);
        }
        return false;
    }

    @Override
    public boolean isAvrcp14Supported(String address) throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.isAvrcp14Supported(address);
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPlay() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpPlay();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStop() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpStop();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpPause() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpPause();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpForward() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpBackward() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpBackward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeUp() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpVolumeUp();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpVolumeDown() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpVolumeDown();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartFastForward() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpStartFastForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopFastForward() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpStopFastForward();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStartRewind() throws RemoteException {
        if(null != mCommandAvrcp){
            return mCommandAvrcp.reqAvrcpStartRewind();
        }
        return false;
    }

    @Override
    public boolean reqAvrcpStopRewind() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetCapabilitiesSupportEvent() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingAttributesList() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingValuesList(byte attributeId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayerSettingCurrentValues() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13SetPlayerSettingValue(byte attributeId, byte valueId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetElementAttributesPlaying() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13GetPlayStatus() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcpRegisterEventWatcher(byte eventId, long interval) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcpUnregisterEventWatcher(byte eventId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13NextGroup() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp13PreviousGroup() throws RemoteException {
        return false;
    }

    @Override
    public boolean isAvrcp14BrowsingChannelEstablished() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAddressedPlayer(int playerId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14SetBrowsedPlayer(int playerId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14GetFolderItems(byte scopeId) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14ChangePath(int uidCounter, long uid, byte direction) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14GetItemAttributes(byte scopeId, int uidCounter, long uid) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14PlaySelectedItem(byte scopeId, int uidCounter, long uid) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14Search(String text) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14AddToNowPlaying(byte scopeId, int uidCounter, long uid) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqAvrcp14SetAbsoluteVolume(byte volume) throws RemoteException {
        return false;
    }

    @Override
    public boolean registerBtCallback(UiCallbackBluetooth cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterBtCallback(UiCallbackBluetooth cb) throws RemoteException {
        return false;
    }

    @Override
    public String getNfServiceVersionName() throws RemoteException {
        return null;
    }

    @Override
    public boolean setBtEnable(boolean enable) throws RemoteException {
        return false;
    }

    @Override
    public boolean setBtDiscoverableTimeout(int timeout) throws RemoteException {
        return false;
    }

    @Override
    public boolean startBtDiscovery() throws RemoteException {
        return false;
    }

    @Override
    public boolean cancelBtDiscovery() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqBtPair(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqBtUnpair(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqBtPairedDevices() throws RemoteException {
        return false;
    }

    @Override
    public String getBtLocalName() throws RemoteException {
        return null;
    }

    @Override
    public String getBtRemoteDeviceName(String address) throws RemoteException {
        return null;
    }

    @Override
    public String getBtLocalAddress() throws RemoteException {
        return null;
    }

    @Override
    public boolean setBtLocalName(String name) throws RemoteException {
        return false;
    }

    @Override
    public boolean isBtEnabled() throws RemoteException {
        return false;
    }

    @Override
    public int getBtState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isBtDiscovering() throws RemoteException {
        return false;
    }

    @Override
    public boolean isBtDiscoverable() throws RemoteException {
        return false;
    }

    @Override
    public boolean isBtAutoConnectEnable() throws RemoteException {
        return false;
    }

    @Override
    public int reqBtConnectHfpA2dp(String address) throws RemoteException {
        return 0;
    }

    @Override
    public int reqBtDisconnectAll() throws RemoteException {
        return 0;
    }

    @Override
    public int getBtRemoteUuids(String address) throws RemoteException {
        return 0;
    }

    @Override
    public boolean switchBtRoleMode(int mode) throws RemoteException {
        return false;
    }

    @Override
    public int getBtRoleMode() throws RemoteException {
        return 0;
    }

    @Override
    public void setBtAutoConnect(int condition, int period) throws RemoteException {

    }

    @Override
    public int getBtAutoConnectCondition() throws RemoteException {
        return 0;
    }

    @Override
    public int getBtAutoConnectPeriod() throws RemoteException {
        return 0;
    }

    @Override
    public int getBtAutoConnectState() throws RemoteException {
        return 0;
    }

    @Override
    public String getBtAutoConnectingAddress() throws RemoteException {
        return null;
    }

    @Override
    public boolean registerHfpCallback(UiCallbackHfp cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterHfpCallback(UiCallbackHfp cb) throws RemoteException {
        return false;
    }

    @Override
    public int getHfpConnectionState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isHfpConnected() throws RemoteException {
        return false;
    }

    @Override
    public String getHfpConnectedAddress() throws RemoteException {
        return null;
    }

    @Override
    public int getHfpAudioConnectionState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean reqHfpConnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpDisconnect(String address) throws RemoteException {
        return false;
    }

    @Override
    public int getHfpRemoteSignalStrength() throws RemoteException {
        return 0;
    }

    @Override
    public List<NfHfpClientCall> getHfpCallList() throws RemoteException {
        return null;
    }

    @Override
    public boolean isHfpRemoteOnRoaming() throws RemoteException {
        return false;
    }

    @Override
    public int getHfpRemoteBatteryIndicator() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isHfpRemoteTelecomServiceOn() throws RemoteException {
        return false;
    }

    @Override
    public boolean isHfpRemoteVoiceDialOn() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpDialCall(String number) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpReDial() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpMemoryDial(String index) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpAnswerCall(int flag) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpRejectIncomingCall() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpTerminateCurrentCall() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpSendDtmf(String number) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToCarkit() throws RemoteException {
        return false;
    }

    @Override
    public boolean reqHfpAudioTransferToPhone() throws RemoteException {
        return false;
    }

    @Override
    public String getHfpRemoteNetworkOperator() throws RemoteException {
        return null;
    }

    @Override
    public String getHfpRemoteSubscriberNumber() throws RemoteException {
        return null;
    }

    @Override
    public boolean reqHfpVoiceDial(boolean enable) throws RemoteException {
        return false;
    }

    @Override
    public void pauseHfpRender() throws RemoteException {

    }

    @Override
    public void startHfpRender() throws RemoteException {

    }

    @Override
    public boolean isHfpMicMute() throws RemoteException {
        return false;
    }

    @Override
    public void muteHfpMic(boolean mute) throws RemoteException {

    }

    @Override
    public boolean isHfpInBandRingtoneSupport() throws RemoteException {
        return false;
    }

    @Override
    public boolean registerPbapCallback(UiCallbackPbap cb) throws RemoteException {
        return false;
    }

    @Override
    public boolean unregisterPbapCallback(UiCallbackPbap cb) throws RemoteException {
        return false;
    }

    @Override
    public int getPbapConnectionState() throws RemoteException {
        return 0;
    }

    @Override
    public boolean isPbapDownloading() throws RemoteException {
        return false;
    }

    @Override
    public String getPbapDownloadingAddress() throws RemoteException {
        return null;
    }

    @Override
    public boolean reqPbapDownload(String address, int storage, int property) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqPbapDownloadRange(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqPbapDownloadToDatabase(String address, int storage, int property) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToDatabase(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqPbapDownloadToContactsProvider(String address, int storage, int property) throws RemoteException {
        return false;
    }

    @Override
    public boolean reqPbapDownloadRangeToContactsProvider(String address, int storage, int property, int startPos, int offset) throws RemoteException {
        return false;
    }

    @Override
    public void reqPbapDatabaseQueryNameByNumber(String address, String target) throws RemoteException {

    }

    @Override
    public void reqPbapDatabaseQueryNameByPartialNumber(String address, String target, int findPosition) throws RemoteException {

    }

    @Override
    public void reqPbapDatabaseAvailable(String address) throws RemoteException {

    }

    @Override
    public void reqPbapDeleteDatabaseByAddress(String address) throws RemoteException {

    }

    @Override
    public void reqPbapCleanDatabase() throws RemoteException {

    }

    @Override
    public boolean reqPbapDownloadInterrupt(String address) throws RemoteException {
        return false;
    }

    @Override
    public boolean setPbapDownloadNotify(int frequency) throws RemoteException {
        return false;
    }

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