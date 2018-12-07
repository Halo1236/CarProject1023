package com.smk.bt.views.fragment;

import com.smk.bt.bean.BTDeviceInfo;

import java.util.List;

public interface IBTDeviceListView {
    int BT_STATE_OFF = 1;
    int BT_STATE_TURNING_ON = 2;
    int BT_STATE_ON = 3;
    int BT_STATE_TURNING_OFF = 4;

    void onBTSwitchStateChange(boolean enable);

    void onChangeDeviceList(List<BTDeviceInfo> btDeviceInfoList);

    void onChangeStateOfBTSwitchDialog(int state);

    void onChangeStateOfBTPair(int state);
}
