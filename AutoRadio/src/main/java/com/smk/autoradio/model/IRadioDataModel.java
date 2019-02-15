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

    /**
     * 获取指定频道类型全搜频道集合
     *
     * @param channelType 频道类型
     * @return
     */
    List<ChannelInfo> getAllFullSearchChannelList(int channelType);

    /**
     * 获取指定频道类型收藏频道集合
     *
     * @param channelType 频道类型
     * @return
     */
    List<ChannelInfo> getAllFavoriteChannelList(int channelType);

    /**
     * 查询指定类型与频道是否收藏
     *
     * @param channelType 频道类型
     * @param channel     频道
     * @return
     */
    boolean isFavorite(int channelType, int channel);

    /**
     * 删除指定频道类型，所有全搜频道信息
     *
     * @param channelType 频道类型
     * @return
     */
    int deleteFullSeachChannel(int channelType);

    /**
     * 删除所有全搜频道信息
     *
     * @return
     */
    int deleteAllFullSeachChannel();

    /**
     * 删除指定频道类型，所有收藏频道信息
     *
     * @param channelType 频道类型
     * @return
     */
    int deleteFavoriteChannel(int channelType);

    /**
     * 删除所有收藏频道信息
     *
     * @return
     */
    int deleteAllFavoriteChannel();
}
