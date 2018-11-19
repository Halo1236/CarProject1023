package com.smk.bt.views;

import com.smk.bt.presenter.BTMusicPlayerPresenter;

public class BTMusicPlayerFragment extends BaseFragment<IBTMusicPlayerView, BTMusicPlayerPresenter<IBTMusicPlayerView>> implements IBTMusicPlayerView {
    @Override
    protected BTMusicPlayerPresenter<IBTMusicPlayerView> createPresenter() {
        return new BTMusicPlayerPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return 0;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListener() {

    }
}
