package com.smk.bt.views;

import com.smk.bt.presenter.BTContractsListPresenter;

public class BTContractsListFragment extends BaseFragment<IBTContractsListView,BTContractsListPresenter<IBTContractsListView>> implements IBTContractsListView {
    @Override
    protected BTContractsListPresenter<IBTContractsListView> createPresenter() {
        return new BTContractsListPresenter();
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
