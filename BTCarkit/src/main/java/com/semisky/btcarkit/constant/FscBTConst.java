package com.semisky.btcarkit.constant;

public class FscBTConst {

    // BLUETOOTH
    // SCAN BT DEVICE STATE
    public static final int SCANN_DEVICE_STATE_UNSUPPORTED = 0;
    public static final int SCANN_DEVICE_STATE_START = 1;
    public static final int SCANN_DEVICE_STATE_SCANNING = 2;
    public static final int SCANN_DEVICE_STATE_FINISHED = 3;

    // A2DP STATUS
    public static final int A2DP_UNSUPPORTED = 0;
    public static final int A2DP_STANDBY = 1;
    public static final int A2DP_CONNECTING = 2;
    public static final int A2DP_CONNECTED = 3;
    public static final int A2DP_PAUSED = 4;
    public static final int A2DP_STREAMING = 5;

    // HFP constants
    public static final int HFP_STATE_UNSUPPORTED = 0;
    public static final int HFP_STATE_STANDBY = 1;
    public static final int HFP_STATE_CONNECTING = 2;
    public static final int HFP_STATE_CONNECTED = 3;
    // HFP CALL STATE
    public static final int HFP_CALL_STATE_OUTGOING_CALL = 4;
    public static final int HFP_CALL_STATE_INCOMING_CALL = 5;
    public static final int HFP_CALL_STATE_ACTIVE_CALL = 6;

    // AVRCP STATUS
    public static final int AVRCP_UNSUPPORTED = 0;
    public static final int AVRCP_STATE_STANDBY = 1;
    public static final int AVRCP_CONNECTION = 2;
    public static final int AVRCP_CONNECTED = 3;

    public static final int AVRCP_MEDIA_PLAYER_STATE_STOPPED = 0;// 停止
    public static final int AVRCP_MEDIA_PLAYER_STATE_PLAYING = 1;// 播放
    public static final int AVRCP_MEDIA_PLAYER_STATE_PAUSED = 2;// 暂停
    public static final int AVRCP_MEDIA_PLAYER_STATE_FAST_FORWARDING = 3;// 快进
    public static final int AVRCP_MEDIA_PLAYER_STATE_FAST_REWINDING = 4;// 快退

    public static final int AVRCP_METADATA_ID_TITLE = 0;
    public static final int AVRCP_METADATA_ID_ARTIST = 1;
    public static final int AVRCP_METADATA_ID_ALBUM = 2;
}
