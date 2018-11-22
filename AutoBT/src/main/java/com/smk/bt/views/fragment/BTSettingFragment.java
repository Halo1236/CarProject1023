package com.smk.bt.views.fragment;

import com.smk.bt.R;
import com.smk.bt.presenter.BTSettingPresenter;

public class BTSettingFragment extends BaseFragment<IBTSettingView,BTSettingPresenter<IBTSettingView>> implements IBTSettingView {
    @Override
    protected BTSettingPresenter<IBTSettingView> createPresenter() {
        return new BTSettingPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting;
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
