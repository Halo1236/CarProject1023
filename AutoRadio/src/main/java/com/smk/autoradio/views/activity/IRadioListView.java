package com.smk.autoradio.views.activity;

import com.smk.autoradio.aidl.ChannelInfo;

import java.util.List;

public interface IRadioListView {

    /**
     * 刷新频道清单列表
     *
     * @param channelList
     */
    void onChangeChannelList(List<ChannelInfo> channelList);
}
