package com.semisky.btcarkit.service.at_cmd;

import com.semisky.btcarkit.service.callbacks.IFscCallbackA2dp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackAvrcp;
import com.semisky.btcarkit.service.callbacks.IFscCallbackBluetooth;
import com.semisky.btcarkit.service.callbacks.IFscCallbackHfp;

public interface IApiResponseTable {
    String HEAD = "AT+";
    String TAIL = "\r\n";
    String EQUAL_SIGN = "=";
    String ACK_OK = "OK";
    //command response
    String VERSION = "+VER";
    String NAME = "+NAME";
    String ADDRESS = "+ADDR";
    String LIST = "+LIST";
    String SCAN = "+SCAN";
    String PAIRED = "+PAIRED";
    String DEVICE_STATE = "+DEVSTAT";

    //hfp response
    String HANDFREE_STATE = "+HFPSTAT";
    String HANDFREE_CALL_ID = "+HFPCID";
    String HANDFREE_AUDIO_STATE = "+HFPAUDIO";

    //a2dp & avrcp response
    String A2DP_STATE = "+A2DPSTAT";
    String AVRCP_STATE = "+AVRCPSTAT";
    String MEDIA_PLAY_STATE = "+PLAYSTAT";
    String MEDIA_TRACK_STATE = "+TRACKSTAT";
    String MEDIA_TRACK_INFORMATION = "+TRACKINFO";

    //phonebook response
    String PHONEBOOK_DATA = "+PBDATA";

    String SYMBOL_SEPARATOR = "|";
    String SYMBOL_EQUAL = "=";

    /*Bluetooth-----------------------------------------------------------------------------------*/

    /**
     * 注册监听蓝牙蓝牙模块状态监听
     *
     * @param callback
     */
    void registerBtStateCallback(IFscCallbackBluetooth callback);

    /**
     * 解析接受AT指定数据
     *
     * @param data
     */
    void onDataParse(int[] data);

    /**
     * 获取蓝牙设备开关状态
     *
     * @return
     */
    int getDeviceState();

    /**
     * 扫描蓝牙设备开始
     *
     * @return
     */
    void notifyStartBtDeviceDiscovery();

    /**
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#SCANN_DEVICE_STATE_UNSUPPORTED}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#SCANN_DEVICE_STATE_START}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#SCANN_DEVICE_STATE_SCANNING}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#SCANN_DEVICE_STATE_FINISHED}
     * 扫描蓝牙设备状态
     *
     * @return
     */
    int getScanBtDeviceState();
    /*HFP----------------------------------------------------------------------------------------*/

    void registerHfpStateCallback(IFscCallbackHfp callback);

    /**
     * HFP状态
     *
     * @return
     */
    int getHfpState();

    void removeIncomingCall();

    void removeOutgoingCall();
    /*A2DP----------------------------------------------------------------------------------------*/

    /**
     * 注册A2DP状态变化监听
     *
     * @param callback
     */
    void registerA2dpCallback(IFscCallbackA2dp callback);

    /**
     * 获取A2DP当前状态
     *
     * @return
     */
    int getA2dpState();

    /*AVRCP---------------------------------------------------------------------------------------*/

    /**
     * 注册AVRCP状态变化监听
     *
     * @param callback
     */
    void registerAvrcpCallback(IFscCallbackAvrcp callback);

    /**
     * 获取AVRCP媒体状态
     */
    int getAvrcpMediaPlayStatus();

    /**
     * 歌名
     *
     * @return
     */
    String getBtMusicTitle();

    /**
     * 歌手
     *
     * @return
     */
    String getBtMusicArtist();

    /**
     * 专辑
     *
     * @return
     */
    String getBtMusicAlbum();

}
