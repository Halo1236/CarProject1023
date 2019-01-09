package com.smk.autoradio.aidl;

import java.util.List;
import java.lang.String;
import com.smk.autoradio.aidl.ChannelInfo;

interface IRadioStatusListener {

    /**
    * 全搜开始
    * */
    void onFullSearchChannelStart();
    /**
    * 全搜时上报频道
    * @param channel 频道
    * */
    void onFullSearchChannelReport(int channel);
    /**
    * 全搜结束
    * */
    void onFullSearchChanelEnd();



    /**
    * 强频道搜索开始
    * */
    void onStrongChannelSearchStart();
    /**
    * 强频道搜索上报频道
    * @param channel 频道
    * */
    void onStrongChannelSearchReport(int channel);
    /**
    * 强频道搜索结束
    * */
    void onStrongChannelSearchEnd();



	/**
	 * 加载全搜存台清单列表
	 * @param channelType 频段类型<br>
	 *     {@link RadioConst#CHANNEL_TYPE_FM1}<br>
     *     {@link RadioConst#CHANNEL_TYPE_FM2}<br>
     *     {@link RadioConst#CHANNEL_TYPE_AM}<br>
	 * @param channelList 频道集合<br>
	 */
	void onLoadFullSearchChannelList(int channelType,in List<ChannelInfo> channelList);
	/**
	* 加载收藏清单列表
	* @param channelType 频段类型<br>
	 *     {@link RadioConst#CHANNEL_TYPE_FM1}<br>
     *     {@link RadioConst#CHANNEL_TYPE_FM2}<br>
     *     {@link RadioConst#CHANNEL_TYPE_AM}<br>
	* @param channelList 频道集合<br>
	* */
	void onLoadFavoriteChannelList(int channelType,in List<ChannelInfo> channelList);


	/**
     * 频段类型改变
     *
     * @param channelType 频段类型<br>
	 *     {@link RadioConst#CHANNEL_TYPE_FM1}<br>
     *     {@link RadioConst#CHANNEL_TYPE_FM2}<br>
     *     {@link RadioConst#CHANNEL_TYPE_AM}<br>
     */
    void onChannelTypeChanged(int channelType);
	/**
	 * 声道类型改变
	 * 
	 * @param soundtrackType 频段声道类型<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_MONO}<br>
	 *     {@link RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
	 */
	void onChannelSoundtrackTypeChanged(int soundtrackType);

	/**
	 * 远近程变化
	 * 
	 * @param channelDxLocType 频段远近程类型<br>
	 *     {@link RadioConst#CHANNEL_DX_LOC_TYPE_INVALID}<br>
     *     {@link RadioConst#CHANNEL_DX_LOC_TYPE_DX}<br>
     *     {@link RadioConst#CHANNEL_DX_LOC_TYPE_LOC}<br>
	 */
	void onChannelDxLocTypeChanged(int channelDxLocType);



}
