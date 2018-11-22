package com.smk.bt.views.fragment;

import com.smk.bt.R;
import com.smk.bt.presenter.BTMusicPlayerPresenter;

public class BTMusicPlayerFragment extends BaseFragment<IBTMusicPlayerView, BTMusicPlayerPresenter<IBTMusicPlayerView>> implements IBTMusicPlayerView {
    @Override
    protected BTMusicPlayerPresenter<IBTMusicPlayerView> createPresenter() {
        return new BTMusicPlayerPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_musicplayer;
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void initUiState() {

    }
}
