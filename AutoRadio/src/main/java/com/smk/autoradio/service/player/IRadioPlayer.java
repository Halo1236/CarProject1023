package com.smk.autoradio.service.player;

import com.smk.autoradio.aidl.ChannelInfo;

import java.util.List;

public interface IRadioPlayer {

    interface OnRadioPlayerStatusListener {
        /**
         * 全搜开始
         */
        void onFullSearchChannelStart();

        /**
         * 全搜时上报频道
         *
         * @param channel 频道
         */
        void onFullSearchChannelReport(int channel);

        /**
         * 全搜结束
         */
        void onFullSearchChannelEnd();


        /**
         * 强频道搜索开始
         */
        void onStrongChannelSearchStart();

        /**
         * 强频道搜索上报频道
         *
         * @param channel 频道
         */
        void onStrongChannelSearchReport(int channel);

        /**
         * 强频道搜索结束
         */
        void onStrongChannelSearchEnd();


        /**
         * 加载全搜存台清单列表
         * @param channelList 频道集合<br>
         */
        void onLoadFullSearchChannelList(List<ChannelInfo> channelList);

        /**
         * 加载收藏清单列表
         * @param channelList 频道集合<br>
         */
        void onLoadFavoriteChannelList(List<ChannelInfo> channelList);

        /**
         * 加载全搜与收藏频道合集到一块集合清单
         * @param channelList 频道集合<br>
         */
        void onLoadFullSearchAndFavoriteChannelList(List<ChannelInfo> channelList);

        /**
         * 频段类型改变
         *
         * @param channelType 频段类型<br>
         *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM1}<br>
         *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM2}<br>
         *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_AM}<br>
         */
        void onChannelTypeChanged(int channelType);

        /**
         * 频率范围改变
         *
         * @param channelValueMin 频道最小值
         * @param channelValueMax 频道最大值
         */
        void onChannelRangeChanged(int channelValueMin, int channelValueMax);

        /**
         * 声道类型改变
         *
         * @param soundtrackType 频段声道类型<br>
         *                       {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
         *                       {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_MONO}<br>
         *                       {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
         */
        void onChannelSoundtrackTypeChanged(int soundtrackType);

        /**
         * 远近程变化
         *
         * @param channelDxLocType 频段远近程类型<br>
         *                         {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_INVALID}<br>
         *                         {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_DX}<br>
         *                         {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_LOC}<br>
         */
        void onChannelDxLocTypeChanged(int channelDxLocType);
    }

    void registerOnRadioPlayerStatusListener(OnRadioPlayerStatusListener l);

    void unregisterOnRadioPlayerStatusListener();

    /**
     * 恢复断点记忆播放
     */
    void reqRestoreChannelPlay();

    /**
     * 获取频段类型
     *
     * @return <p>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM1}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM2}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_AM}<br>
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
     * @param channelType 频道类型<br>
     *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM1}<br>
     *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM2}<br>
     *                    {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_AM}<br>
     * @param channel     频道<br>
     */
    void reqPlayChannel(int channelType, int channel);

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
    void reqGetSearchList();

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
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_MONO}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
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
