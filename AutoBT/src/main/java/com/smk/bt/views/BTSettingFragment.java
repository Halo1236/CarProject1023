package com.smk.bt.views;

import com.smk.bt.presenter.BTSettingPresenter;

public class BTSettingFragment extends BaseFragment<IBTSettingView,BTSettingPresenter<IBTSettingView>> implements IBTSettingView {
    @Override
    protected BTSettingPresenter<IBTSettingView> createPresenter() {
        return new BTSettingPresenter();
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
