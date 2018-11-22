package com.smk.bt.views.fragment;

import com.smk.bt.R;
import com.smk.bt.presenter.BTCallLogListPresenter;

public class BTCallLogListFragment extends BaseFragment<IBTCallLogListView,BTCallLogListPresenter<IBTCallLogListView>> implements IBTCallLogListView {
    @Override
    protected BTCallLogListPresenter<IBTCallLogListView> createPresenter() {
        return new BTCallLogListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_calllog_list;
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
