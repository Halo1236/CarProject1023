package com.smk.autoradio.presenter;

public interface IRadioListPresenter {

    /**
     * 获取全搜频道清单列表
     */
    void reqGetFullSearchList();

    /**
     * 获取收藏清单列表
     */
    void reqGetFavoriteList();
}
