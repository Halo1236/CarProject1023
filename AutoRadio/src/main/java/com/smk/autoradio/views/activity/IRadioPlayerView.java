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
     * @param type
     */
    void changeChannelType(int type);

    /**
     * 收藏状态改变
     *
     * @param isFavorite
     */
    void changeFavorite(boolean isFavorite);

    /**
     * 声道类型改变<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_INVALID}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_MONO}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SOUNDTRACK_TYPE_STEREO}<br>
     *
     * @param soundtrackType
     */
    void changeSoundtrack(int soundtrackType);

    /**
     * 远近程类型<br>
     * {@link com.smk.autoradio.constants.RadioConst#SIGNAL_TYPE_INVALID}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SIGNAL_TYPE_DX}<br>
     * {@link com.smk.autoradio.constants.RadioConst#SIGNAL_YTPE_LOC}<br>
     *
     * @param type
     */
    void changeDxLocType(int type);

    /**
     * 刷新频道清单列表
     *
     * @param channelList
     */
    void changeChannelList(List<ChannelInfo> channelList);
}
