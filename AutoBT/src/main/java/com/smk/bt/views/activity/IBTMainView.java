package com.smk.bt.views.activity;

import android.content.Intent;

public interface IBTMainView {
    // Fragments flag
    int FRAGMENT_DEVICE_LIST_FLAG = 0;// 设备列表标识
    int FRAGMENT_DIALPAD_FLAG = 1;// 拨号界面标识
    int FRAGMENT_CONTACTS_FLAG = 2;// 联系人界面标识
    int FRAGMENT_CALLLOG_FLAG = 3;// 通话记录界面标识
    int FRAGMENT_MUSIC_FLAG = 4;// 蓝牙音乐界面标识
    int FRAGMENT_SETTING_FLAG = 5;// 设置界面标识
    int FRAGMENT_MIN_FLAG = FRAGMENT_DEVICE_LIST_FLAG;
    int FRAGMENT_MAX_FLAG = FRAGMENT_SETTING_FLAG;
    int FRAGMENT_DEF_FLAG = FRAGMENT_DEVICE_LIST_FLAG;
    int FRAGMENT_TATAL = 6;// Fragment 页面总数

    void initShowFragment(int fragmentFlag);
    void switchFragment(int fragmentFlag);
    void showBTConnectedBottombar();
    void showBTDisconnectedBottombar();

}
