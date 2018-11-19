package com.smk.bt.views;

import com.smk.bt.presenter.BTDialpadPresenter;

public class BTDialpadFragment extends BaseFragment<IBTDialpadView,BTDialpadPresenter<IBTDialpadView>> implements IBTDialpadView {
    @Override
    protected BTDialpadPresenter<IBTDialpadView> createPresenter() {
        return new BTDialpadPresenter();
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
