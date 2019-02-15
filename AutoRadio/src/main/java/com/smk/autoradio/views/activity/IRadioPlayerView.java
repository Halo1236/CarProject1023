package com.smk.autoradio.views.activity;

import com.smk.autoradio.aidl.ChannelInfo;

import java.util.List;

public interface IRadioPlayerView {

    /**
     * 频道类型改变<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_FM}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_AM}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_TYPE_INVALID}<br>
     *
     * @param channelType
     */
    void onChangeChannelType(int channelType);

    /**
     * 收藏状态改变
     *
     * @param isFavorite
     */
    void onChangeFavorite(boolean isFavorite);

    /**
     * 声道类型改变<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_MONO}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
     *
     * @param soundtrackType
     */
    void onChangeSoundtrack(int soundtrackType);

    /**
     * 远近程类型<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_INVALID}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_DX}<br>
     * {@link com.smk.autoradio.constants.RadioConst#CHANNEL_DX_LOC_TYPE_LOC}<br>
     *
     * @param channelDxLocType
     */
    void onChangeDxLocType(int channelDxLocType);

    /**
     * 刷新频道清单列表
     *
     * @param channelList
     */
    void onChangeChannelList(List<ChannelInfo> channelList);
}
