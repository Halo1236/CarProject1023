package com.smk.autoradio.aidl;

import com.smk.autoradio.aidl.IRadioStatusChangeListener;

interface IRadioPlayer {

	/**
	 * 注册监听收音机状态变化
	 * 
	 * @param l
	 */
	void registerOnRadioStatusChangeListener(IRadioStatusChangeListener l);

	/**
	 * 注册监听收音机状态变化
	 * 
	 * @param l
	 */
	void unregisterOnRadioStatusChangeListener(IRadioStatusChangeListener l);

	/**
	 * 恢复断点记忆播放
	 */
	void reqRestoreChannelPlay();

	/**
	 * 获取频段类型
	 * 
	 * @return <p>
	 *         {@link RadioConst#CHANNEL_TYPE_FM}<br>
	 *         {@link RadioConst#CHANNEL_TYPE_AM}<br>
	 */
	int getChannelType();

	/**
	 * 根据频段类型获取频点
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
	 * @param channel
	 *            频点
	 */
	void reqPlayChannel(int channel);

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
	 * 
	 * @return
	 */
	void reqGetFavoriteList();

	/**
	 * 获取全搜频道清单列表
	 */
	void reqGetSearchList();

	/**
	 * 切换远近程
	 */
	void reqSwitchDxOrLoc();

	/**
	 * 获取声道类型
	 * 
	 * @return
	 */
	int getSoundtrackType();

}
