package com.smk.bt.views.fragment;

import com.smk.bt.bean.BTDeviceInfo;

import java.util.List;

public interface IBTDeviceListView {
    // BT SWITCH STATE
    int BT_STATE_OFF = 1;
    int BT_STATE_TURNING_ON = 2;
    int BT_STATE_ON = 3;
    int BT_STATE_TURNING_OFF = 4;
    // BT PAIR STATE
    int BT_STATE_BOND_NONE = 20;
    int BT_STATE_BONDING = 21;
    int BT_STATE_BONDED = 22;
    // BT DEVICE DISCOVERY
    int BT_STATE_DISCOVERY_START = 30;
    int BT_STATE_DISCOVERY_FINISHED = 31;

    void onBTSwitchStateChange(boolean enable);

    void onChangeDeviceList(List<BTDeviceInfo> btDeviceInfoList);

    void onChangeStateOfBTSwitchDialog(int state);

    void onChangeStateOfBTPair(int state);

    void onChangeStateOfBTDiscovery(int state);
}
