// ISmkCallbackAvrcp.aidl
package com.semisky.btcarkit.aidl;

// Declare any non-default types here with import statements

interface ISmkCallbackAvrcp {
    /**
    *  <br>BTConst#AVRCP_STATE_NOT_INITIALIZED
    *  <br>BTConst#AVRCP_STATE_STANDBY
    *  <br>BTConst#AVRCP_CONNECTION
    *  <br>BTConst#AVRCP_CONNECTED
    * */
    void onAvrcpStateChanged(String address,int oldState,int newState );
    /**
    * @param stateId playing status
    * <br> Possible values are:
    * <br>BTConst#AVRCP_PLAYING_STATE_ID_STOPPED
    * <br>BTConst#AVRCP_PLAYING_STATE_ID_PLAYING
    * <br>BTConst#AVRCP_PLAYING_STATE_ID_PAUSED
    * <br>BTConst#AVRCP_PLAYING_STATE_ID_FAST_FWD
    * <br>BTConst#AVRCP_PLAYING_STATE_ID_FAST_REW
    *
    **/
    void onAvrcpPlayStateChanged(int oldState,int newState);

    /**
    *  @param metadataIds list of media attributes IDs
    *  <br>Possible values are:
    *  <br>BTConst#AVRCP_METADATA_ID_TITLE
    *  <br>BTConst#AVRCP_METADATA_ID_ARTIST
    *  <br>BTConst#AVRCP_METADATA_ID_ALBUM
    *  @param metadataValues the result of String type to corresponding metadata attribute ID
    * */
    void onAvrcpMediaMetadataChanged(in int[] metadataIds,in String[] metadataValues);
}
