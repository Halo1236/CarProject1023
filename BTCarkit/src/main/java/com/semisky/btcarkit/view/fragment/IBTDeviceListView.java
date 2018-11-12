package com.semisky.btcarkit.view.fragment;

import com.semisky.btcarkit.aidl.BTDeviceInfo;

import java.util.List;

public interface IBTDeviceListView {
    void onRefreshDeviceList(List<BTDeviceInfo> infos);
    void onScanStateChanged(String state);
    void onNotifyHfpStateChanged(String state);
    void onNotifyA2dpStateChanged(String state);
}
