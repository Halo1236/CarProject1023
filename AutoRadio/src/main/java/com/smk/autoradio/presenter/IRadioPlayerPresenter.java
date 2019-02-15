package com.smk.autoradio.presenter;

public interface IRadioPlayerPresenter {
    /**
     * 请求恢复断点记忆播放
     */
    void reqRestoreChannelPlay();

    /**
     * 请求上一个强台
     */
    void reqPrevStrongChannel();

    /**
     * 请下一个强台
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
     * 请求切换频道类型
     */
    void reqSwitchBand();

    /**
     * 请求设置EQ
     */
    void reqSettingEQ();

    /**
     * 请求切换远近程
     */
    void reqSwitchDxOrLoc();

    /**
     * 请求全搜
     */
    void reqFullSearch();

    /**
     * 获取全搜频道清单列表
     */
    void reqGetFullSearchList();

    /**
     * 请求获取全搜与收藏频道合集到一块集合清单
     */
    void reqGetFullSearchAndFavoriteChannelList();
}
