package com.semisky.bluetoothproject.constant;

public class BtConstant {

    public static final String SERVICE_CLZ = "com.semisky.bluetoothproject.service.BtLocalService";
    public static final String SERVICE_PKG = "com.semisky.bluetoothproject";

    public static final String ACTION_BT_START = "com.semisky.service.BTSERVICE_START";
    public static final String ACTION_MODE_START = "com.semisky.service.ACTION_MODE_START_ACTIVITY";
    public static final String MODE_START_KEY = "MODE_START_KEY";
    public static final String ACTION_START_ACTIVITY = "com.semisky.action_START_ACTIVITY";
    public static final int START_ACTIVITY_VALUE = 1;

    public static final String SERIALIZABLE_DATA = "Serializable_data";

    public class CommandMethod {
        public static final int START_BT_DISCOVERY = 0x01;
        public static final int REQ_HFP_DIAL_CALL = 0x02;
        public static final int REQ_BT_CONNECT_HFP_A2DP = 0x03;
        public static final int REQ_BT_UN_PAIR = 0x04;
        public static final int REQ_BT_DOWNLOAD_CONNECT = 0x05;
        public static final int REQ_BT_DOWNLOAD_CALLLOG = 0x06;
        public static final int REQ_BT_MUSIC_PLAY = 0x07;
        public static final int REQ_BT_MUSIC_NEXT = 0x08;
        public static final int REQ_BT_MUSIC_LAST = 0x09;
        public static final int REQ_BT_SET_BREAK_CONNECT = 0x0A;
        public static final int SET_BT_ENABLE = 0x0B;
        public static final int CANCEL_BT_DISCOVERY = 0x0C;
        public static final int REQ_BT_PAIRE_DDEVICES = 0x0D;
        public static final int REQ_BT_MUSIC_PAUSE = 0x0E;
        public static final int REQ_AVRCP13_GET_PLAYING = 0x0F;
        public static final int SET_AUTO_CONNECT = 0x10;
        public static final int CHECK_PERMISSION = 0x11;
        public static final int REQ_BT_ONCE_CALLLOG = 0x12;
        public static final int REQ_HFP_TERMINATE_CURRENT_CALL = 0x13;
        public static final int REQ_HFP_ANSWER_CALL = 0x14;
        public static final int REQ_PBAP_DOWNLOAD_INTERRUPT = 0x15;
        public static final int REQ_AVRCP_13_GET_PLAY_STATUS = 0x16;
    }

    public class CountryLocal {
        public static final int COUNTRY_CN = 1;
        public static final int COUNTRY_EN = 2;
        public static final int COUNTRY_US = 3;
        public static final int COUNTRY_SA = 4;
    }

    public static class ArrayList {
        public static final String[] keyboardData = {
                "1", "4", "7", "*",
                "2", "5", "8", "0+",
                "3", "6", "9", "#"};
    }

    public enum CallStatus {
        NULL,//默认
        INCOMING,//来电
        DIALING,//去电
        ACTIVE,//通话中
        TERMINATED,//挂断
        HELD,//一开始的电话
        WAITING//第三方来电
    }

    /**
     * 记录当前是什么fragment
     */
    public enum FragmentFlag {
        KEYBOARD,
        CONTACT,
        CALL,
        MUSIC,
        SET
    }

    public static class PBAP {
        public static final int PBAP_QUERY_COUNT = 50;
    }

    /**
     * 设置-状态相关(保存sharedpreferences)
     */
    public class SettingState {
        public static final String PAIRED_DEVICES_LIST = "paired_devices_list";//已配对蓝牙设备列表
        public static final String SYNC_CALLLOG_STATE = "sync_calllog_state";//当前设备是否已同步通话记录
        public static final String CONTACT_NUM = "sync_contact_number";//当前设备是否已同步通话记录
        public static final String SYNC_CONTACTS_STATE = "sync_contacts_state";//当前设备是否已同步通讯录
        public static final String SYNC_CALLLOGPAGE_STATE = "sync_calllogpage_state";//是否来自通话记录页面
        public static final String LAST_CONNECT_ADDRESS = "last_connect_address";//上次连接蓝牙地址
        public static final String BT_SWITCH_STATE = "bt_switch_state";//蓝牙开关状态
        public static final String BT_CONN_STATE = "bt_conn_state";//蓝牙连接状态
        public static final String AUTO_CONN_STATE = "auto_conn_state";//自动连接状态
        public static final String AUTO_READ_STATE = "auto_read_state";//来电播报状态
        public static final String AUTO_ANSWER_STATE = "auto_answer_state";//自动接听状态
        public static final String AUTO_SYNC_BOOK_STATE = "auto_sync_book_state";//自动同步通讯录状态
        public static final String BT_CONN_CODE = "bt_conn_code";//配对码
    }
}
