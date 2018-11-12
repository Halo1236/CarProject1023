package com.semisky.btcarkit.service.at_cmd;

/**
 * Created by 18108 on 2018/10/14.
 */

public class ApiCommandTable {
    public static final String HEAD = "AT+";
    public static final String TAIL = "\r\n";
    public static final String EQUAL_SIGN = "=";
    public static final String VERSION = "VER";
    public static final String NAME = "NAME";
    public static final String DEVICE_STATE = "DEVSTAT";
    public static final String ADDRESS = "ADDR";
    public static final String LIST = "LIST";
    public static final String SCAN = "SCAN";
    public static final String PARAM_SCAN_STOP = "0";
    public static final String PARAM_SCAN_BR_EDR = "1";
    public static final String PARAM_SCAN_ELE = "2";
    public static final String PARAM_SCAN_BR_EDR_BLE = "3";

    public static final String DISCONNECT_ALL = "DSCA";
    public static final String BT_ENABLE = "BTEN=1";
    public static final String BT_DISABLE = "BTEN=0";
    public static final String HANDFREE_STATE = "HFPSTAT";
    public static final String HANDFREE_CONNCECT = "HFPCONN";
    public static final String HANDFREE_DIAL = "HFPDIAL";
    public static final String HANDFREE_ANSWER = "HFPANSW";
    public static final String HANDFREE_HANGUP = "HFPCHUP";
    public static final String AVRCP_STATE = "AVRCPSTAT";

    public static final String A2DP_CONNECT = "A2DPCONN";
    public static final String A2DP_STATE = "A2DPSTAT";

    public static final String MEDIA_PLAY = "PLAY";
    public static final String MEDIA_PAUSE = "PAUSE";
    public static final String MEDIA_PLAY_OR_PAUSE = "PLAYPAUSE";
    public static final String MEDIA_NEXT_TRACK = "FORWARD";
    public static final String MEDIA_PREVIOUS_TRACK = "BACKWARD";
    public static final String PHONEBOOK_DOWNLOAD = "PBDOWN=1";

    public String makeCmd(String command, String parameter){
        String ret;
        if(parameter == null){
            ret = HEAD + command + TAIL;
        }
        else{
            ret = HEAD + command + EQUAL_SIGN + parameter + TAIL;
        }
        return ret;
    }

}