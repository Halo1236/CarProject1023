package com.semisky.bluetoothproject.responseinterface;

/**
 * Created by chenhongrui on 2018/8/15
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtMusicStatusResponse {

    void onAvrcpStateChanged(String address, int prevState, int newState);

    void retAvrcp13PlayStatus(long songLen, long songPos, byte statusId);

    void retAvrcp13ElementAttributesPlaying(int[] metadataAtrributeIds, String[] texts);

    /**
     * <blockquote><b>AVRCP_PLAYING_STATUS_ID_STOPPED</b> 		(byte) 0x00 : stopped
     * <br><b>AVRCP_PLAYING_STATUS_ID_PLAYING</b> 				(byte) 0x01 : playing
     * <br><b>AVRCP_PLAYING_STATUS_ID_PAUSED</b> 				(byte) 0x02 : paused
     * <br><b>AVRCP_PLAYING_STATUS_ID_FWD_SEEK</b> 			(byte) 0x03 : fwd seek
     * <br><b>AVRCP_PLAYING_STATUS_ID_REW_SEEK</b> 			(byte) 0x04 : rev seek
     * <br><b>AVRCP_PLAYING_STATUS_ID_ERROR</b> 				(byte) 0xFF : error</blockquote>
     *
     * @param statusId
     */
    void onAvrcp13EventPlaybackStatusChanged(byte statusId);
}
