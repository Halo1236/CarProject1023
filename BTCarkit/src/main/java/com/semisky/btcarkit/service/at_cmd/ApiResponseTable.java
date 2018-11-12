package com.semisky.btcarkit.service.at_cmd;

import android.util.Log;

import com.semisky.btcarkit.aidl.BTDeviceInfo;
import com.semisky.btcarkit.aidl.BTHfpClientCall;
import com.semisky.btcarkit.constant.FscBTConst;
import com.semisky.btcarkit.service.callbacks.IFscCallbackA2dp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackAvrcp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackBluetooth;
import com.semisky.btcarkit.service.callbacks.IFscCallbackHfp;
import com.semisky.btcarkit.service.natives.FscBwNative;
import com.semisky.btcarkit.utils.Logutil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 18108 on 2018/10/14.
 */

public class ApiResponseTable implements IApiResponseTable {
    private final String TAG = Logutil.makeTagLog(ApiResponseTable.class);
    //telephone
    private BTHfpClientCall mIncomingCall;
    private BTHfpClientCall mOutgoingCall;

    //status
    public static int mHfpState;
    public static int hand_free_audio_status;
    private int mAvrcpState = FscBTConst.AVRCP_UNSUPPORTED;
    private int mA2dpState = FscBTConst.A2DP_UNSUPPORTED;
    private int mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_STOPPED;
    private int mDeviceState = 0;// 记录蓝牙开关状态 0：OFF,1：ON
    // scan bt device
    private int mScanBtDeviceState = FscBTConst.SCANN_DEVICE_STATE_UNSUPPORTED;
    private List<BTDeviceInfo> mBTDeviceInfoList = new ArrayList<BTDeviceInfo>();
    //flag
    public static boolean isDownloadComplete = false;
    public static boolean isScanComplete = false;

    //track information
    private String mBtMusicTitle;
    private String mBtMusicArtist;
    private String mBtMusicAlbum;
    private int[] mMetadataIds = new int[3];
    private String[] mMetadataValues = new String[3];

    //phone book
    public static String phoneBook_type;
    public static String phoneBook_name;
    public static String phoneBook_number;
    public static StringBuffer phoneBook = new StringBuffer();

    public String[] responseSet = new String[]{};

    private FscBwNative mFscBwNative = new FscBwNative();
    public static ApiCommandTable apiCommandTable = new ApiCommandTable();

    private IFscCallbackBluetooth mIFscCallbackBluetooth;
    private IFscCallbackA2dp mIFscCallbackA2dp;
    private IFscCallbackAvrcp mIFscCallbackAvrcp;
    private IFscCallbackHfp mIFscCallbackHfp;

    @Override
    public void registerBtStateCallback(IFscCallbackBluetooth callback) {
        this.mIFscCallbackBluetooth = callback;
    }

    @Override
    public void registerHfpStateCallback(IFscCallbackHfp callback) {
        this.mIFscCallbackHfp = callback;
    }

    @Override
    public void registerA2dpCallback(IFscCallbackA2dp callback) {
        this.mIFscCallbackA2dp = callback;
    }

    public void registerHfpCallback(IFscCallbackHfp callback) {
        this.mIFscCallbackHfp = callback;
    }

    @Override
    public void registerAvrcpCallback(IFscCallbackAvrcp callback) {
        this.mIFscCallbackAvrcp = callback;
    }


    // 蓝牙开关状态变化
    private void notifyBtStateChanged(int oldState, int newState) {
        if (null != mIFscCallbackBluetooth) {
            mIFscCallbackBluetooth.onAdapterStateChanged(oldState, newState);
        }
    }

    // 开始扫描到蓝牙设备通知
     void notifyDeviceDiscoveryStarted(){
         if (null != mIFscCallbackBluetooth) {
             synchronized (mBTDeviceInfoList) {
                 if(null != mBTDeviceInfoList){
                     mBTDeviceInfoList.clear();
                 }
                 this.mScanBtDeviceState = FscBTConst.SCANN_DEVICE_STATE_START;
                 mIFscCallbackBluetooth.onDeviceDiscoveryStarted();
             }
         }
     }

    // 扫描到蓝牙设备通知
    void notifyDeviceFound(BTDeviceInfo info) {
        if (null != mIFscCallbackBluetooth) {
            synchronized (mBTDeviceInfoList){
                this.mScanBtDeviceState = FscBTConst.SCANN_DEVICE_STATE_SCANNING;
                mBTDeviceInfoList.add(info);
                mIFscCallbackBluetooth.onDeviceFound(mBTDeviceInfoList);
            }
        }
    }

    // 蓝牙设备扫描完成通知
    void notifyDeviceDiscoveryFinished() {
        if (null != mIFscCallbackBluetooth) {
            this.mScanBtDeviceState = FscBTConst.SCANN_DEVICE_STATE_FINISHED;
            mIFscCallbackBluetooth.onDeviceDiscoveryFinished();
        }
    }

    // HFP状态变化通知
    void notifyHfpStateChange(String address, int oldState, int newState) {
        if (null != mIFscCallbackHfp) {
            mIFscCallbackHfp.onHfpStateChanged(address, oldState, newState);
        }
    }

    // HFP 电话状态变化通知
    void notifyHfpCallStateChanged(String address, BTHfpClientCall call) {
        if (null != mIFscCallbackHfp) {
            mIFscCallbackHfp.onHfpCallStateChanged(address, call);
        }
    }

    // A2DP状态变化通知
    void notifyA2dpStateChanged(int oldState, int newState) {
        if (null != mIFscCallbackA2dp) {
            mIFscCallbackA2dp.onA2dpStateChanged(oldState, newState);
        }
    }

    // AVRCP状态变化通知
    void notifyAvrcpStateChanged(String address, int oldState, int newState) {
        if (null != mIFscCallbackAvrcp) {
            mIFscCallbackAvrcp.onAvrcpStateChanged(address, oldState, newState);
        }
    }

    // AVRCP媒体播放状态变化通知
    void notifyAvrcpPlayingStateChanged(int oldState, int newState) {
        if (null != mIFscCallbackAvrcp) {
            mIFscCallbackAvrcp.onAvrcpPlayStateChanged(oldState, newState);
        }
    }

    // AVRCP媒体播放状态变化通知
    void notifyAvrcpMediaMetadataChanged(int[] mediaMetadataIds, String[] mediaMetadataValues) {
        if (null != mIFscCallbackAvrcp) {
            mIFscCallbackAvrcp.onAvrcpMediaMetadataChanged(mediaMetadataIds, mediaMetadataValues);
        }
    }

    // Getter

    @Override
    public int getDeviceState() {
        return this.mDeviceState;
    }

    @Override
    public int getHfpState() {
        return this.mHfpState;
    }

    @Override
    public int getA2dpState() {
        return this.mA2dpState;
    }

    @Override
    public String getBtMusicTitle() {
        return this.mBtMusicTitle;
    }

    @Override
    public String getBtMusicArtist() {
        return this.mBtMusicArtist;
    }

    @Override
    public String getBtMusicAlbum() {
        return this.mBtMusicAlbum;
    }

    @Override
    public int getAvrcpMediaPlayStatus() {
        return this.mAvrcpMediaPlayStatus;
    }

    @Override
    public void removeIncomingCall() {
        this.mIncomingCall = null;
    }

    @Override
    public void removeOutgoingCall() {
        this.mOutgoingCall = null;
    }

    @Override
    public void notifyStartBtDeviceDiscovery() {
        notifyDeviceDiscoveryStarted();
    }

    @Override
    public int getScanBtDeviceState() {
        return this.mScanBtDeviceState;
    }

    public void handleResponseTable(String response) {

        if (response.contains(MEDIA_PLAY_STATE)) {
            handlePlayState(response);
        } else if (response.contains(HANDFREE_CALL_ID)) {
            handleHfpCallId(response);
        } else if (response.contains(HANDFREE_STATE)) {
            handleHfpState(response);
        } else if (response.contains(HANDFREE_AUDIO_STATE)) {
            handleHfpAudioState(response);
        } else if (response.contains(A2DP_STATE)) {
            handleA2dpState(response);
        } else if (response.contains(AVRCP_STATE)) {
            handleAvrcpState(response);
        } else if (response.contains(PHONEBOOK_DATA)) {
            handlePhonebookData(response);
        } else if (response.contains(DEVICE_STATE)) {
            handleDeviceState(response);
        } else if (response.contains(MEDIA_TRACK_INFORMATION)) {
            handleTrackInformation(response);
        } else if (response.contains(SCAN)) {
            handleScanInformation(response);
        } else {
            //Log.i(TAG, "parseApiResponseTable: "+response);
        }
    }

    @Override
    public void onDataParse(int[] data) {
        try {
            parse(data);
        } catch (Exception e) {
            Logutil.e(TAG, "onDataParse() error !!!!================================");
            e.printStackTrace();
        }
    }

    private void parse(int[] data) {

        for (int i = 0; i < data.length; i++) {
            if (data[i] == 255) {
                data[i] = '|';
            }
        }

        byte[] byteTemp = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            byteTemp[i] = (byte) data[i];
        }

        String s = new String(byteTemp);

        Logutil.i(TAG, ">>>>>>>> PARSE DATA START >>>>>>>>>");
        Logutil.i(TAG, "data contents : " + (null != s ? s : "null"));
        Logutil.i(TAG, ">>>>>>>>> PARSE DATA END >>>>>>>>>");

        String strTemp;
        strTemp = s.substring(2, s.length() - 2);
        if (s.indexOf("\r\n\r\n") == -1) {
            Log.i(TAG, "single parse: " + strTemp);
            handleResponseTable(strTemp);
        } else {
            responseSet = strTemp.split("\r\n\r\n");
            for (int i = 0, len = responseSet.length; i < len; i++) {
                Log.i(TAG, "multi parse: " + responseSet[i].toString());
                handleResponseTable(responseSet[i].toString());
            }
        }


    }

    // AVRCP 媒体播放状态指令解析并分发消息
    private void handlePlayState(String s) {

        if (s.substring(s.indexOf("=") + 1).equals("0")) {
            Log.i(TAG, "handlePlayState: STOPPED");
            notifyAvrcpPlayingStateChanged(mAvrcpMediaPlayStatus, FscBTConst.AVRCP_MEDIA_PLAYER_STATE_STOPPED);
            mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_STOPPED;
        } else if (s.substring(s.indexOf("=") + 1).equals("1")) {
            Log.i(TAG, "handlePlayState: PLAYING");
            notifyAvrcpPlayingStateChanged(mAvrcpMediaPlayStatus, FscBTConst.AVRCP_MEDIA_PLAYER_STATE_PLAYING);
            mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_PLAYING;
        } else if (s.substring(s.indexOf("=") + 1).equals("2")) {
            Log.i(TAG, "handlePlayState: PAUSED");
            notifyAvrcpPlayingStateChanged(mAvrcpMediaPlayStatus, FscBTConst.AVRCP_MEDIA_PLAYER_STATE_PAUSED);
            mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_PAUSED;
        } else if (s.substring(s.indexOf("=") + 1).equals("3")) {
            Log.i(TAG, "handlePlayState: FAST Forwarding");
            notifyAvrcpPlayingStateChanged(mAvrcpMediaPlayStatus, FscBTConst.AVRCP_MEDIA_PLAYER_STATE_FAST_FORWARDING);
            mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_FAST_FORWARDING;
        } else if (s.substring(s.indexOf("=") + 1).equals("4")) {
            Log.i(TAG, "handlePlayState: FAST Rewinding");
            notifyAvrcpPlayingStateChanged(mAvrcpMediaPlayStatus, FscBTConst.AVRCP_MEDIA_PLAYER_STATE_FAST_REWINDING);
            mAvrcpMediaPlayStatus = FscBTConst.AVRCP_MEDIA_PLAYER_STATE_FAST_REWINDING;
        } else {
            return;
        }
    }

    public void handleHfpState(String s) {
        try {
            if (null == s) {
                Logutil.e(TAG, "handleHfpState() s==null !!!");
                return;
            } else if (s.indexOf("=") < 0) {
                Logutil.e(TAG, "handleHfpState() s.indexOf() < 0 !!! ");
                return;
            }

            // 解析HFP状态AT指令
            if (s.substring(s.indexOf("=") + 1).equals("0")) {
                Log.i(TAG, "handleHfpState: 0");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_STATE_UNSUPPORTED);
                mHfpState = FscBTConst.HFP_STATE_UNSUPPORTED;
            } else if (s.substring(s.indexOf("=") + 1).equals("1")) {
                Log.i(TAG, "handleHfpState: 1");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_STATE_STANDBY);
                mHfpState = FscBTConst.HFP_STATE_STANDBY;
            } else if (s.substring(s.indexOf("=") + 1).equals("2")) {
                Log.i(TAG, "handleHfpState: 2");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_STATE_CONNECTING);
                mHfpState = FscBTConst.HFP_STATE_CONNECTING;
            } else if (s.substring(s.indexOf("=") + 1).equals("3")) {
                Log.i(TAG, "handleHfpState: 3");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_STATE_CONNECTED);
                mHfpState = FscBTConst.HFP_STATE_CONNECTED;
            } else if (s.substring(s.indexOf("=") + 1).equals("4")) {
                Log.i(TAG, "handleHfpState: 4");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_CALL_STATE_OUTGOING_CALL);
                mHfpState = FscBTConst.HFP_CALL_STATE_OUTGOING_CALL;
            } else if (s.substring(s.indexOf("=") + 1).equals("5")) {
                Log.i(TAG, "handleHfpState: 5");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_CALL_STATE_INCOMING_CALL);
                mHfpState = FscBTConst.HFP_CALL_STATE_INCOMING_CALL;
            } else if (s.substring(s.indexOf("=") + 1).equals("6")) {
                Log.i(TAG, "handleHfpState: 6");
                notifyHfpStateChange(null, mHfpState, FscBTConst.HFP_CALL_STATE_ACTIVE_CALL);
                mHfpState = FscBTConst.HFP_CALL_STATE_ACTIVE_CALL;
            } else {
                return;
            }
        } catch (Exception e) {
            Logutil.e(TAG, "handleHfpState ERROR !!!!");
            e.printStackTrace();
        }
    }

    public void handleHfpCallId(String s) {
        int phoneNumberStartIndex = -1;
        int phoneNumberEndIndex = -1;
        int phoneNameStartIndex = -1;
        String phoneName = "";
        String tempPhoneNumber = "";

        Logutil.i(TAG, "handleHfpCallId() cmd[ " + (null != s ? s : "null") + " ]");
        if (null == s) {
            Logutil.e(TAG, "handleHfpCallId() AT cmd == null");
        } else {
            phoneNumberStartIndex = s.indexOf(HANDFREE_CALL_ID);
            phoneNumberStartIndex = phoneNumberStartIndex > -1 ? phoneNumberStartIndex + HANDFREE_CALL_ID.length() + SYMBOL_EQUAL.length() : -1;

            phoneNumberEndIndex = s.indexOf(SYMBOL_SEPARATOR);
            phoneNumberEndIndex = phoneNumberEndIndex > 0 ? phoneNumberEndIndex : s.length();

            phoneNameStartIndex = s.indexOf(SYMBOL_SEPARATOR);
            phoneNameStartIndex = phoneNameStartIndex > -1 ? phoneNameStartIndex + SYMBOL_SEPARATOR.length() : -1;
            Logutil.w(TAG, "handleHfpCallId() phoneNumberStartIndex = " + phoneNumberStartIndex + ",phoneNumberEndIndex=" + phoneNumberEndIndex);

        }

        if (phoneNumberStartIndex > -1) {
            tempPhoneNumber = s.substring(phoneNumberStartIndex, phoneNumberEndIndex);
        }

        if (phoneNameStartIndex > 0) {
            phoneName = s.substring(phoneNameStartIndex);
        } else {
            phoneName = null;
        }

        if (mHfpState == FscBTConst.HFP_CALL_STATE_INCOMING_CALL) {// 来电
            Logutil.i(TAG, "HFP_CALL_STATE_INCOMING_CALL ...");
            this.mIncomingCall = new BTHfpClientCall(0, BTHfpClientCall.CALL_STATE_INCOMING, tempPhoneNumber, phoneName, false, false);
            notifyHfpCallStateChanged(null, mIncomingCall);
        } else if (mHfpState == FscBTConst.HFP_CALL_STATE_OUTGOING_CALL) {// 去电
            Logutil.i(TAG, "HFP_CALL_STATE_OUTGOING_CALL ...");
            this.mOutgoingCall = new BTHfpClientCall(0, BTHfpClientCall.CALL_STATE_DIALING, tempPhoneNumber, phoneName, false, true);
            notifyHfpCallStateChanged(null, mOutgoingCall);
        }
        Log.i(TAG, "------------------------号码 :  " + tempPhoneNumber);
        Log.i(TAG, "------------------------名字 :  " + (null != phoneName ? phoneName : "null"));

    }

    public void handleHfpAudioState(String s) {
        if (s.substring(s.indexOf("=") + 1).equals("0")) {
            Log.i(TAG, "音频断开: 0");
            hand_free_audio_status = 0;

        } else if (s.substring(s.indexOf("=") + 1).equals("1")) {
            Log.i(TAG, "音频连接: 1");
            hand_free_audio_status = 1;
        }
    }

    public void handleA2dpState(String s) {
        if (s.substring(s.indexOf("=") + 1).equals("0")) {
            Log.i(TAG, "unsupported: 0");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_UNSUPPORTED);
            this.mA2dpState = FscBTConst.A2DP_UNSUPPORTED;
        } else if (s.substring(s.indexOf("=") + 1).equals("1")) {
            Log.i(TAG, "standby: 1");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_STANDBY);
            this.mA2dpState = FscBTConst.A2DP_STANDBY;
        } else if (s.substring(s.indexOf("=") + 1).equals("2")) {
            Log.i(TAG, "connecting: 2");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_CONNECTING);
            this.mA2dpState = FscBTConst.A2DP_CONNECTING;
        } else if (s.substring(s.indexOf("=") + 1).equals("3")) {
            Log.i(TAG, "connected: 3");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_CONNECTED);
            this.mA2dpState = FscBTConst.A2DP_CONNECTED;
        } else if (s.substring(s.indexOf("=") + 1).equals("4")) {
            Log.i(TAG, "paused: 4");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_PAUSED);
            this.mA2dpState = FscBTConst.A2DP_CONNECTED;
        } else if (s.substring(s.indexOf("=") + 1).equals("5")) {
            Log.i(TAG, "streaming: 5");
            this.notifyA2dpStateChanged(mA2dpState, FscBTConst.A2DP_STREAMING);
            this.mA2dpState = FscBTConst.A2DP_STREAMING;
        }
    }

    public void handleAvrcpState(String s) {
        if (s.substring(s.indexOf("=") + 1).equals("0")) {
            Log.i(TAG, "unsupported: 0");
            notifyAvrcpStateChanged(null, mAvrcpState, FscBTConst.AVRCP_UNSUPPORTED);
            mAvrcpState = FscBTConst.AVRCP_UNSUPPORTED;
        } else if (s.substring(s.indexOf("=") + 1).equals("1")) {
            Log.i(TAG, "standby: 1");
            notifyAvrcpStateChanged(null, mAvrcpState, FscBTConst.AVRCP_STATE_STANDBY);
            mAvrcpState = FscBTConst.AVRCP_STATE_STANDBY;
        } else if (s.substring(s.indexOf("=") + 1).equals("2")) {
            Log.i(TAG, "connecting: 2");
            notifyAvrcpStateChanged(null, mAvrcpState, FscBTConst.AVRCP_CONNECTION);
            mAvrcpState = FscBTConst.AVRCP_CONNECTION;
        } else if (s.substring(s.indexOf("=") + 1).equals("3")) {
            Log.i(TAG, "connected: 3");
            notifyAvrcpStateChanged(null, mAvrcpState, FscBTConst.AVRCP_CONNECTED);
            mAvrcpState = FscBTConst.AVRCP_CONNECTED;
        }
    }


    public void handleDeviceState(String s) {
//        queryDeviceStateTimer.cancel();
        int deviceState = Integer.parseInt(s.substring(s.indexOf("=") + 1));
        Log.i(TAG, "handleDeviceState: " + s.substring(s.indexOf("=") + 1));
        notifyBtStateChanged(mDeviceState, deviceState);
        this.mDeviceState = deviceState;
    }

    public void handleTrackInformation(String s) {
        synchronized (mMetadataValues) {
            Log.i(TAG, "title: " + s.substring(s.indexOf("=") + 1, s.indexOf(SYMBOL_SEPARATOR)));
            Log.i(TAG, "artist: " + s.substring(s.indexOf(SYMBOL_SEPARATOR) + 1, s.lastIndexOf(SYMBOL_SEPARATOR)));
            Log.i(TAG, "album: " + s.substring(s.lastIndexOf(SYMBOL_SEPARATOR) + 1, s.length()));
            mBtMusicTitle = s.substring(s.indexOf("=") + 1, s.indexOf(SYMBOL_SEPARATOR));
            mBtMusicArtist = s.substring(s.indexOf(SYMBOL_SEPARATOR) + 1, s.lastIndexOf(SYMBOL_SEPARATOR));
            mBtMusicAlbum = s.substring(s.lastIndexOf(SYMBOL_SEPARATOR) + 1, s.length());

            mMetadataIds[0] = FscBTConst.AVRCP_METADATA_ID_TITLE;
            mMetadataIds[1] = FscBTConst.AVRCP_METADATA_ID_ARTIST;
            mMetadataIds[2] = FscBTConst.AVRCP_METADATA_ID_ALBUM;
            this.mMetadataValues[FscBTConst.AVRCP_METADATA_ID_TITLE] = mBtMusicTitle;
            this.mMetadataValues[FscBTConst.AVRCP_METADATA_ID_ARTIST] = mBtMusicArtist;
            this.mMetadataValues[FscBTConst.AVRCP_METADATA_ID_ALBUM] = mBtMusicAlbum;
            notifyAvrcpMediaMetadataChanged(mMetadataIds, mMetadataValues);
        }
    }

    public void handlePhonebookData(String s) {

        if (s.substring(s.indexOf("=") + 1).equals("E")) {
            //download complete
            isDownloadComplete = true;
            return;
        }

        Log.i(TAG, "type: " + s.substring(s.indexOf("=") + 1, s.indexOf("=") + 2));
        Log.i(TAG, "name: " + s.substring(s.indexOf("=") + 3, s.lastIndexOf(SYMBOL_SEPARATOR)));
        Log.i(TAG, "number: " + s.substring(s.lastIndexOf(SYMBOL_SEPARATOR) + 1, s.length()));
        phoneBook_type = s.substring(s.indexOf("=") + 1, s.indexOf("=") + 2);
        phoneBook_name = s.substring(s.indexOf("=") + 3, s.lastIndexOf(SYMBOL_SEPARATOR));
        phoneBook_number = s.substring(s.lastIndexOf(SYMBOL_SEPARATOR) + 1, s.length());

        phoneBook.append(phoneBook_name + ":   " + phoneBook_number + "\r\n");
    }

    // 处理扫描到蓝牙设备AT指令信息解析
    private void handleScanInformation(String s) {
        if (s.substring(s.indexOf("=") + 1).equals("E")) {
            //scan complete
            notifyDeviceDiscoveryFinished();// 扫描完成通知
            isScanComplete = true;
            return;
        }
        String deviceName = getScanDeivceName(s);
        String deviceRssi = getScanDeviceRssi(s);
        String deviceAddress = getScanDeviceAddress(s);

        notifyDeviceFound(new BTDeviceInfo(deviceAddress, deviceName, deviceRssi));// 扫描到蓝牙设备通知
    }

    public String getScanDeviceRssi(String s) {
        String s1 = s.substring(s.indexOf(SYMBOL_SEPARATOR) + 1, s.length());
        String s2 = s1.substring(0, s1.indexOf(SYMBOL_SEPARATOR));
        return s2;
    }

    public String getScanDeviceAddress(String s) {
        String s1 = s.substring(s.indexOf(SYMBOL_SEPARATOR) + 1, s.length());
        String s2 = s1.substring(s1.indexOf(SYMBOL_SEPARATOR) + 1, s1.length());
        String s3 = s2.substring(s2.indexOf(SYMBOL_SEPARATOR) + 1, s2.length());
        String s4 = s3.substring(0, s3.indexOf(SYMBOL_SEPARATOR));

        return s4;
    }

    public String getScanDeivceName(String s) {
        String s1 = s.substring(s.indexOf(SYMBOL_SEPARATOR) + 1, s.length());
        String s2 = s1.substring(s1.indexOf(SYMBOL_SEPARATOR) + 1, s1.length());
        String s3 = s2.substring(s2.indexOf(SYMBOL_SEPARATOR) + 1, s2.length());
        String s4 = s3.substring(s3.indexOf(SYMBOL_SEPARATOR) + 1, s3.length());
        String s5 = s4.substring(0, s4.length());
        return s5;
    }

    public int getCount(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

}
