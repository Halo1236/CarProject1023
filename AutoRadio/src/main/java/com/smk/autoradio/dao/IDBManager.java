package com.smk.autoradio.dao;

import android.database.Cursor;

import com.smk.autoradio.aidl.ChannelInfo;

import java.util.List;

public interface IDBManager {

    /**
     * 批量添加频段信息到数据库存
     *
     * @param channelInfos 频道实体
     */
    long addBatchSearchChannelToDB(List<ChannelInfo> channelInfos);

    /**
     * 添加单条频段信息到数据库存
     *
     * @param channelInfo 频道实体
     * @return
     */
    long addSingleChannelInfoToFavorite(ChannelInfo channelInfo);

    /**
     * 查询指定频道类型，所有全搜频道信息
     *
     * @param channelType
     * @return
     */
    Cursor queryAllFullSearchChannel(int channelType);

    /**
     * 查询指定频道类型，所有收藏频道
     *
     * @param channelType 频道类型
     * @return
     */
    Cursor queryAllFavoriteChannel(int channelType);

    /**
     * 查询指定收藏频段
     *
     * @param channelType
     * @param channel
     * @return
     */
    Cursor querySpecifyFavoriteChannel(int channelType, int channel);

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
