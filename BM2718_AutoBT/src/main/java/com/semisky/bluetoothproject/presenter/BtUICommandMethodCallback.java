package com.semisky.bluetoothproject.presenter;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.widget.Toast;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.aidl.UiCallbackA2dp;
import com.nforetek.bt.aidl.UiCallbackAvrcp;
import com.nforetek.bt.aidl.UiCallbackBluetooth;
import com.nforetek.bt.aidl.UiCallbackHfp;
import com.nforetek.bt.aidl.UiCallbackHid;
import com.nforetek.bt.aidl.UiCallbackPbap;
import com.nforetek.bt.aidl.UiCommand;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtCallStatusModel;
import com.semisky.bluetoothproject.model.BtContactsDownloadModel;
import com.semisky.bluetoothproject.model.BtDeviceSearchModel;
import com.semisky.bluetoothproject.model.BtHfpModel;
import com.semisky.bluetoothproject.model.BtMusicModel;
import com.semisky.bluetoothproject.model.BtTtsClientModel;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.nforeservice.service.BtBaseService;

import static android.content.Context.BIND_AUTO_CREATE;

/**
 * Created by luoyin on 17-10-17.
 */

public class BtUICommandMethodCallback {

    private final String TAG = Logger.makeTagLog(BtUICommandMethodCallback.class);
    private UiCommand mCommand;
    private Context mContext;
    private BtHfpModel btHfpModel;
    private BtTtsClientModel btTtsClientModel;
    private BtMiddleSettingManager btMiddleSettingManager;

    private BtContactsDownloadModel btContactsDownloadModel;
    private BtCallStatusModel btCallStatusModel;
    private BtDeviceSearchModel btDeviceSearchModel;
    private BtMusicModel btMusicModel;

    private static class SingletonHolder {
        private static final BtUICommandMethodCallback INSTANCE = new BtUICommandMethodCallback();
    }

    private BtUICommandMethodCallback() {
        btHfpModel = BtHfpModel.getInstance();
        btTtsClientModel = BtTtsClientModel.getInstance();
        btMiddleSettingManager = BtMiddleSettingManager.getInstance();

        btDeviceSearchModel = BtDeviceSearchModel.getInstance();
        btCallStatusModel = BtCallStatusModel.getInstance();
        btMusicModel = BtMusicModel.getInstance();
        btContactsDownloadModel = BtContactsDownloadModel.getInstance();
    }

    public static BtUICommandMethodCallback getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void startBTMusicService(Context context) {
        mContext = context.getApplicationContext();
        Logger.d(TAG, "start bt music service");
        //启动蓝牙服务
        Intent intent = new Intent(context, BtBaseService.class);
        mContext.startService(intent);
        mContext.bindService(intent, this.mConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            Logger.d(TAG, "ready  onServiceConnected");
            mCommand = UiCommand.Stub.asInterface(service);
            if (mCommand == null) {
                Logger.d(TAG, "mCommand is null!!");
                Toast.makeText(mContext.getApplicationContext(), "UiService is null!", Toast.LENGTH_SHORT).show();
                return;
            }

            BtBaseUiCommandMethod.getInstance().setmCommand(mCommand);

            try {
                mCommand.registerBtCallback(mCallbackBluetooth);
                mCommand.registerHfpCallback(mCallbackHfp);
                mCommand.registerA2dpCallback(mCallbackA2dp);
                mCommand.registerAvrcpCallback(mCallbackAvrcp);
                mCommand.registerHidCallback(mCallbackHid);
                mCommand.registerPbapCallback(mCallbackPbap);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            Logger.d(TAG, "end  onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            Logger.d(TAG, "onServiceDisconnected");
            mCommand = null;
        }
    };

    private UiCallbackHid mCallbackHid = new UiCallbackHid.Stub() {

        @Override
        public void onHidServiceReady() throws RemoteException {
            Logger.d(TAG, "UiCallbackHid-onHidServiceReady");
        }

        @Override
        public void onHidStateChanged(String address, int prevState, int newState, int reason) throws RemoteException {
            Logger.d(TAG, "UiCallbackHid-onHidStateChanged");
        }
    };
    private UiCallbackHfp mCallbackHfp = new UiCallbackHfp.Stub() {

        @Override
        public void onHfpServiceReady() throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpServiceReady");
        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpStateChanged");
            btMiddleSettingManager.onHfpStateChanged(address, prevState, newState);
            btHfpModel.onHfpStateChanged(address, prevState, newState);
        }

        @Override
        public void onHfpAudioStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpAudioStateChanged");
            btHfpModel.onHfpAudioStateChanged(address, prevState, newState);
        }

        @Override
        public void onHfpVoiceDial(String address, boolean isVoiceDialOn) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpVoiceDial-address=" + address);
            Logger.d(TAG, "mCallbackHfp-onHfpVoiceDial-isVoiceDialOn=" + isVoiceDialOn);
        }

        @Override
        public void onHfpErrorResponse(String address, int code) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpErrorResponse");
        }

        @Override
        public void onHfpRemoteTelecomService(String address, boolean isTelecomServiceOn) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpRemoteTelecomService");
        }

        @Override
        public void onHfpRemoteRoamingStatus(String address, boolean isRoamingOn) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpRemoteRoamingStatus");
        }

        @Override
        public void onHfpRemoteBatteryIndicator(String address, int currentValue, int maxValue, int minValue) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpRemoteBatteryIndicator");
            btHfpModel.onHfpRemoteBatteryIndicator(address, currentValue, maxValue, minValue);
        }

        @Override
        public void onHfpRemoteSignalStrength(String address, int currentStrength, int maxStrength, int minStrength) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpRemoteSignalStrength");
            btHfpModel.onHfpRemoteSignalStrength(address, currentStrength, maxStrength, minStrength);
        }

        @Override
        public void onHfpCallChanged(String address, NfHfpClientCall call) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-onHfpCallChanged");
            btMiddleSettingManager.onHfpCallChanged(address, call);
            btCallStatusModel.onHfpCallChanged(address, call);
        }

        @Override
        public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) throws RemoteException {
            Logger.d(TAG, "mCallbackHfp-retPbapDatabaseQueryNameByNumber address " + address);
            Logger.d(TAG, "mCallbackHfp-retPbapDatabaseQueryNameByNumber target " + target);
            Logger.d(TAG, "mCallbackHfp-retPbapDatabaseQueryNameByNumber name " + name);
            Logger.d(TAG, "mCallbackHfp-retPbapDatabaseQueryNameByNumber isSuccess " + isSuccess);
        }
    };

    private UiCallbackA2dp mCallbackA2dp = new UiCallbackA2dp.Stub() {

        @Override
        public void onA2dpServiceReady() throws RemoteException {
            Logger.d(TAG, "mCallbackA2dp-onA2dpServiceReady");
        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackA2dp-onA2dpStateChanged");
        }
    };

    private UiCallbackAvrcp mCallbackAvrcp = new UiCallbackAvrcp.Stub() {

        @Override
        public void onAvrcpServiceReady() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcpServiceReady");
        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcpStateChanged");
            btMusicModel.onAvrcpStateChanged(address, prevState, newState);
        }

        @Override
        public void retAvrcp13CapabilitiesSupportEvent(byte[] eventIds) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13CapabilitiesSupportEvent");
        }

        @Override
        public void retAvrcp13PlayerSettingAttributesList(byte[] attributeIds) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13PlayerSettingAttributesList");
        }

        @Override
        public void retAvrcp13PlayerSettingValuesList(byte attributeId, byte[] valueIds) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13PlayerSettingValuesList");
        }

        @Override
        public void retAvrcp13PlayerSettingCurrentValues(byte[] attributeIds, byte[] valueIds) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13PlayerSettingCurrentValues");
        }

        @Override
        public void retAvrcp13SetPlayerSettingValueSuccess() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13SetPlayerSettingValueSuccess");
        }

        @Override
        public void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13ElementAttributesPlaying");
            btMusicModel.retAvrcp13ElementAttributesPlaying(metadataAtrributeIds, texts);
        }

        @Override
        public void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp13PlayStatus");
            btMusicModel.retAvrcp13PlayStatus(songLen, songPos, statusId);
        }

        @Override
        public void onAvrcp13RegisterEventWatcherSuccess(byte eventId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13RegisterEventWatcherSuccess");
        }

        @Override
        public void onAvrcp13RegisterEventWatcherFail(byte eventId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13RegisterEventWatcherFail");
        }

        @Override
        public void onAvrcp13EventPlaybackStatusChanged(byte statusId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventPlaybackStatusChanged");
            btMusicModel.onAvrcp13EventPlaybackStatusChanged(statusId);
        }

        @Override
        public void onAvrcp13EventTrackChanged(long elementId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventTrackChanged");

        }

        @Override
        public void onAvrcp13EventTrackReachedEnd() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventTrackReachedEnd");
        }

        @Override
        public void onAvrcp13EventTrackReachedStart() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventTrackReachedStart");
        }

        @Override
        public void onAvrcp13EventPlaybackPosChanged(long songPos) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventPlaybackPosChanged");
        }

        @Override
        public void onAvrcp13EventBatteryStatusChanged(byte statusId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventBatteryStatusChanged");
        }

        @Override
        public void onAvrcp13EventSystemStatusChanged(byte statusId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventSystemStatusChanged");
        }

        @Override
        public void onAvrcp13EventPlayerSettingChanged(byte[] attributeIds, byte[] valueIds) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp13EventPlayerSettingChanged");
        }

        @Override
        public void onAvrcp14EventNowPlayingContentChanged() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp14EventNowPlayingContentChanged");
        }

        @Override
        public void onAvrcp14EventAvailablePlayerChanged() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp14EventAvailablePlayerChanged");
        }

        @Override
        public void onAvrcp14EventAddressedPlayerChanged(int playerId, int uidCounter) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp14EventAddressedPlayerChanged");
        }

        @Override
        public void onAvrcp14EventUidsChanged(int uidCounter) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp14EventUidsChanged");
        }

        @Override
        public void onAvrcp14EventVolumeChanged(byte volume) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcp14EventVolumeChanged");
        }

        @Override
        public void retAvrcp14SetAddressedPlayerSuccess() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14SetAddressedPlayerSuccess");
        }

        @Override
        public void retAvrcp14SetBrowsedPlayerSuccess(String[] path, int uidCounter, long itemCount) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14SetBrowsedPlayerSuccess");
        }

        @Override
        public void retAvrcp14FolderItems(int uidCounter, long itemCount) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14FolderItems");
        }

        @Override
        public void retAvrcp14MediaItems(int uidCounter, long itemCount) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14MediaItems");
        }

        @Override
        public void retAvrcp14ChangePathSuccess(long itemCount) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14ChangePathSuccess");
        }

        @Override
        public void retAvrcp14ItemAttributes(int[] metadataAtrributeIds, String[] texts) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14ItemAttributes");
        }

        @Override
        public void retAvrcp14PlaySelectedItemSuccess() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14PlaySelectedItemSuccess");
        }

        @Override
        public void retAvrcp14SearchResult(int uidCounter, long itemCount) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14SearchResult");
        }

        @Override
        public void retAvrcp14AddToNowPlayingSuccess() throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14AddToNowPlayingSuccess");
        }

        @Override
        public void retAvrcp14SetAbsoluteVolumeSuccess(byte volume) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcp14SetAbsoluteVolumeSuccess");
        }

        @Override
        public void onAvrcpErrorResponse(int opId, int reason, byte eventId) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-onAvrcpErrorResponse");
        }

        @Override
        public void retAvrcpUpdateSongStatus(String artist, String album, String title) throws RemoteException {
            Logger.d(TAG, "mCallbackAvrcp-retAvrcpUpdateSongStatus");
        }
    };

    private UiCallbackPbap mCallbackPbap = new UiCallbackPbap.Stub() {
        @Override
        public void onPbapServiceReady() throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-onPbapServiceReady");
        }

        @Override
        public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-onPbapStateChangedcounts" + counts);
            btContactsDownloadModel.onPbapStateChanged(address, prevState, newState, reason, counts);
        }

        @Override
        public void retPbapDownloadedContact(NfPbapContact contact) throws RemoteException {
//            Logger.d(TAG, "mCallbackPbap-onPbapStateChanged");
            btContactsDownloadModel.retPbapDownloadedContact(contact);
        }

        @Override
        public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) throws RemoteException {
//            Logger.d(TAG, "mCallbackPbap-retPbapDownloadedCallLog");
            btContactsDownloadModel.retPbapDownloadedCallLog(address, firstName, middleName, lastName, number, type, timestamp);
        }

        @Override
        public void onPbapDownloadNotify(String address, int storage, int totalContacts, int downloadedContacts) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-onPbapDownloadNotify-address=" + address);
            Logger.d(TAG, "mCallbackPbap-onPbapDownloadNotify-storage=" + storage);
            Logger.d(TAG, "mCallbackPbap-onPbapDownloadNotify-totalContacts=" + totalContacts);
            Logger.d(TAG, "mCallbackPbap-onPbapDownloadNotify-downloadedContacts=" + downloadedContacts);
        }

        @Override
        public void retPbapDatabaseQueryNameByNumber(String address, String target, String name, boolean isSuccess) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-retPbapDatabaseQueryNameByNumber");
        }

        @Override
        public void retPbapDatabaseQueryNameByPartialNumber(String address, String target, String[] names, String[] numbers, boolean isSuccess) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-retPbapDatabaseQueryNameByPartialNumber");
        }

        @Override
        public void retPbapDatabaseAvailable(String address) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-retPbapDatabaseAvailable");
        }

        @Override
        public void retPbapDeleteDatabaseByAddressCompleted(String address, boolean isSuccess) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-retPbapDeleteDatabaseByAddressCompleted");
        }

        @Override
        public void retPbapCleanDatabaseCompleted(boolean isSuccess) throws RemoteException {
            Logger.d(TAG, "mCallbackPbap-retPbapCleanDatabaseCompleted");
        }
    };

    private UiCallbackBluetooth mCallbackBluetooth = new UiCallbackBluetooth.Stub() {

        @Override
        public void onBluetoothServiceReady() throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onBluetoothServiceReady");
        }

        @Override
        public void onAdapterStateChanged(int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onAdapterStateChanged");
            btMiddleSettingManager.onAdapterStateChanged(prevState, newState);
            btDeviceSearchModel.onAdapterStateChanged(prevState, newState);
        }

        @Override
        public void onAdapterDiscoverableModeChanged(int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onAdapterDiscoverableModeChanged");
        }

        @Override
        public void onAdapterDiscoveryStarted() throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onAdapterDiscoveryStarted");
            btDeviceSearchModel.onAdapterDiscoveryStarted();
        }

        @Override
        public void onAdapterDiscoveryFinished() throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onAdapterDiscoveryFinished");
            btDeviceSearchModel.onAdapterDiscoveryFinished();
        }

        @Override
        public void retPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-retPairedDevices");
            btDeviceSearchModel.retPairedDevices(elements, address, name, supportProfile, category);
        }

        @Override
        public void onDeviceFound(String address, String name, byte category) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onDeviceFound");
            btDeviceSearchModel.onDeviceFound(address, name, category);
        }

        @Override
        public void onDeviceBondStateChanged(String address, String name, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onDeviceBondStateChanged");
            btDeviceSearchModel.onDeviceBondStateChanged(address, name, prevState, newState);
        }

        @Override
        public void onDeviceUuidsUpdated(String address, String name, int supportProfile) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onDeviceUuidsUpdated");
        }

        @Override
        public void onLocalAdapterNameChanged(String name) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onLocalAdapterNameChanged");
        }

        @Override
        public void onDeviceOutOfRange(String address) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onDeviceOutOfRange--address = " + address);

        }

        @Override
        public void onDeviceAclDisconnected(String address) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onDeviceAclDisconnected");
        }

        @Override
        public void onBtRoleModeChanged(int mode) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onBtRoleModeChanged");
        }

        @Override
        public void onBtAutoConnectStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onBtAutoConnectStateChanged");
        }

        @Override
        public void onHfpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onHfpStateChanged");
        }

        @Override
        public void onA2dpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onA2dpStateChanged");
        }

        @Override
        public void onAvrcpStateChanged(String address, int prevState, int newState) throws RemoteException {
            Logger.d(TAG, "mCallbackBluetooth-onAvrcpStateChanged");
        }
    };

}
