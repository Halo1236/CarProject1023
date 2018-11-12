package com.semisky.btcarkit.aidl;

public class BTConst {
    // Bluetooth constants
    public static final int BT_OFF = 0;
    public static final int BT_ON = 1;
    public static final int BT_SERIAL_OPENED = 1;
    public static final int BT_SERIAL_CLOSED = 0;
    // SCAN BT DEVICE STATE
    public static final int SCANN_DEVICE_STATE_UNSUPPORTED = 0;
    public static final int SCANN_DEVICE_STATE_START = 1;
    public static final int SCANN_DEVICE_STATE_SCANNING = 2;
    public static final int SCANN_DEVICE_STATE_FINISHED = 3;
    // HFP constants
    public static final int HFP_STATE_NOT_INITIALIZED = 0;
    public static final int HFP_STATE_READY = 1;
    public static final int HFP_STATE_CONNECTING = 2;
    public static final int HFP_STATE_CONNECTED = 3;
    // HFP CALL STATE
    public static final int HFP_CALL_STATE_OUTGOING_CALL = 4;
    public static final int HFP_CALL_STATE_INCOMING_CALL = 5;
    public static final int HFP_CALL_STATE_ACTIVE_CALL = 6;

    // A2DP constants
    public static final int A2DP_STATE_NOT_INITIALIZED = 0;
    public static final int A2DP_STATE_READY = 1;
    public static final int A2DP_STATE_CONNECTING = 2;
    public static final int A2DP_STATE_CONNECTED = 3;
    public static final int A2DP_STATE_PAUSED = 4;
    public static final int A2DP_STATE_STREAMING =5;
    // AVRCP constants
    public static final int AVRCP_STATE_NOT_INITIALIZED = 0;
    public static final int AVRCP_STATE_READY = 1;
    public static final int AVRCP_CONNECTION = 2;
    public static final int AVRCP_CONNECTED = 3;

    public static final int AVRCP_PLAYING_STATE_ID_STOPPED = 0;// 停止
    public static final int AVRCP_PLAYING_STATE_ID_PLAYING = 1;// 播放
    public static final int AVRCP_PLAYING_STATE_ID_PAUSED = 2;// 暂停
    public static final int AVRCP_PLAYING_STATE_ID_FAST_FWD = 3;// 快进
    public static final int AVRCP_PLAYING_STATE_ID_FAST_REW = 4;// 快退

    public static final int AVRCP_METADATA_ID_TITLE = 0;
    public static final int AVRCP_METADATA_ID_ARTIST = 1;
    public static final int AVRCP_METADATA_ID_ALBUM = 2;


    // PBAP constants

}
