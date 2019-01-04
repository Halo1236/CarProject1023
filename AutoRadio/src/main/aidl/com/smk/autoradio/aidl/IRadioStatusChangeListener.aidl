package com.smk.autoradio.aidl;

import java.util.List;
import java.lang.String;
import com.smk.autoradio.aidl.ChannelInfo;

interface IRadioStatusChangeListener {
	/**
	 * 正在扫台
	 * 
	 */
	void onChannelSearching();
	/**
	 * 扫台时频段改变
	 * 
	 * @param channel
	 */
	void onSearchChannelChanged(int channel);
	/**
	 * 扫台完成
	 * 
	 */
	void onChannelSearchFinished();
	
	/**
	 * 频段类型改变
	 * 
	 * @param channelType
	 */
	void onChannelTypeChanged(int channelType);

	/**
	 * 强频段改变
	 * 
	 * @param channel
	 */
	void onStrongChannelChanged(int channel);

	/**
	 * 收藏频段清单列表数据变化
	 * 
	 * @param channelList
	 */
	void onReqGetFavoriteChannelList(in List<ChannelInfo> channelList);

	/**
	 * 扫描频段清单列表数据变化
	 * 
	 * @param channelList
	 */
	void onRequestGetFullSearchChannelList(in List<ChannelInfo> channelList);

	/**
	 * 声道类型改变
	 * 
	 * @param soundtrackType
	 */
	void onSoundtrackTypeChanged(int soundtrackType);

	/**
	 * 远近程变化
	 * 
	 * @param farOrShortRange
	 */
	void onDxLocChanged(int farOrShortRange);

}
