package com.smk.autoradio.model;

import com.smk.autoradio.aidl.ChannelInfo;

import java.util.List;

public interface IRadioDataModel {

    /**
     * 批量添加频段信息到数据库存
     *
     * @param channelInfos 频道实体
     */
    long addBatchSearchChannelToDB(List<ChannelInfo> channelInfos);

    /**
     * 添加单条频段信息到收藏数据库
     *
     * @param channelInfo 频道实体
     * @return
     */
    long addSingleChannelInfoToFavorite(ChannelInfo channelInfo);
}
