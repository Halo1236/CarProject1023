package com.smk.bt.views;

import com.smk.bt.presenter.BTCallLogListPresenter;

public class BTCallLogListFragment extends BaseFragment<IBTCallLogListView,BTCallLogListPresenter<IBTCallLogListView>> implements IBTCallLogListView {
    @Override
    protected BTCallLogListPresenter<IBTCallLogListView> createPresenter() {
        return new BTCallLogListPresenter();
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
