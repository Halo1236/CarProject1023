package com.semisky.btcarkit.service.callbacks;

public interface IFscCallbackAvrcp {
    /**
     * @param address  MAC addraess
     * @param oldState
     * @param newState <br> Possible values are:
     *                 <br>{@link com.semisky.btcarkit.constant.FscBTConst#A2DP_UNSUPPORTED}
     *                 <br>{@link com.semisky.btcarkit.constant.FscBTConst#A2DP_STANDBY}
     *                 <br>{@link com.semisky.btcarkit.constant.FscBTConst#A2DP_CONNECTING}
     *                 <br>{@link com.semisky.btcarkit.constant.FscBTConst#A2DP_CONNECTED}
     *                 <br>{@link com.semisky.btcarkit.constant.FscBTConst#A2DP_STREAMING}
     */
    void onAvrcpStateChanged(String address, int oldState, int newState);

    /**
     * @param oldState playing status
     * @param newState playing status
     *                <br> Possible values are:
     *                <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_MEDIA_PLAYER_STATE_STOPPED}
     *                <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_MEDIA_PLAYER_STATE_PLAYING}
     *                <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_MEDIA_PLAYER_STATE_PAUSED}
     *                <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_MEDIA_PLAYER_STATE_FAST_FORWARDING}
     *                <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_MEDIA_PLAYER_STATE_FAST_REWINDING}
     **/
    void onAvrcpPlayStateChanged(int oldState, int newState);

    /**
     * @param metadataIds    list of media attributes IDs
     *                       <br>Possible values are:
     *                       <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_METADATA_ID_TITLE}
     *                       <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_METADATA_ID_ARTIST}
     *                       <br>{@link com.semisky.btcarkit.constant.FscBTConst#AVRCP_METADATA_ID_ALBUM}
     * @param metadataValues the result of String type to corresponding metadata attribute ID
     */
    void onAvrcpMediaMetadataChanged(int[] metadataIds, String[] metadataValues);
}
