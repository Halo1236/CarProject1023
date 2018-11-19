package com.smk.bt.views;

import com.smk.bt.presenter.BTMainPresenter;

public class BTMainActivity extends BaseActivity<IBTMainView,BTMainPresenter<IBTMainView>> implements IBTMainView{
    @Override
    protected BTMainPresenter<IBTMainView> createPresenter() {
        return new BTMainPresenter();
    }

    @Override
    protected void initViews() {

    }

    @Override
    protected void initListeners() {

    }
}
