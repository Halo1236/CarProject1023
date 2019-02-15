package com.smk.autoradio.aidl;

import com.smk.autoradio.aidl.IRadioStatusListener;

interface IRadioProxyService {

	/**
	 * 注册监听收音机状态变化
	 * 
	 * @param l
	 */
	void registerOnRadioStatusListener(IRadioStatusListener l);
	/**
	 * 反注册监听收音机状态变化
	 * 
	 * @param l
	 */
	void unregisterOnRadioStatusListener(IRadioStatusListener l);
	/**
	 * 恢复断点记忆播放
	 */
	void reqRestoreChannelPlay();

	/**
	 * 获取频段类型
	 * 
	 * @return <p>
	 *         {@link RadioConst#CHANNEL_TYPE_FM1}<br>
	 *         {@link RadioConst#CHANNEL_TYPE_FM2}<br>
	 *         {@link RadioConst#CHANNEL_TYPE_AM}<br>
	 */
	int getChannelType();

	/**
	 * 获取频点
	 * 
	 * @return
	 */
	int getChannel();

	/**
	 * 切换频段类型
	 */
	void reqSwitchBand();

	/**
	 * 上一个强频段
	 */
	void reqPrevStrongChannel();

	/**
	 * 下一个强频段
	 */
	void reqNextStroneChannel();

	/**
	 * 频点步进递增
	 */
	void reqStepIncreaseChannel();

	/**
	 * 频点步进递减
	 */
	void reqStepDecreaseChannel();

	/**
	 * 播放指定频点
	 *
	 * @param channelType 频道类型<br>
	 *     {@link RadioConst#CHANNEL_TYPE_FM1}<br>
	 *     {@link RadioConst#CHANNEL_TYPE_FM2}<br>
	 *     {@link RadioConst#CHANNEL_TYPE_AM}<br>
	 * 
	 * @param channel 频道<br>
	 */
	void reqPlayChannel(int channelType,int channel);

	/**
	 * 请求全搜
	 */
	void reqFullSearch();

	/**
	 * 请求设置EQ
	 */
	void reqSettingEQ();

	/**
	 * 添加当前频段到收藏
	 */
	void reqAddCurrentChannelToFavorite();

	/**
	 * 获取收藏清单列表
	 */
	void reqGetFavoriteList();

	/**
	 * 获取全搜频道清单列表
	 */
	void reqGetFullSearchList();

    /**
     * 请求获取全搜与收藏频道合集到一块集合清单
     */
	void reqGetFullSearchAndFavoriteChannelList();

	/**
	 * 切换远近程
	 */
	void reqSwitchDxOrLoc();

	/**
	 * 获取声道类型
	 * 
	 * @return 频段声道类型<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_MONO}<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
	 */
	int getSoundtrackType();
	 /**
     * 当前最小频道值
     *
     * @return 最小频道值
     */
    int getMinChannelValue();

    /**
     * 当前最大频道值
     *
     * @return 最大频道值
     */
    int getMaxChannelValue();

}
