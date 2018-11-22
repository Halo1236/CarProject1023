package com.smk.bt.views.fragment;

import com.smk.bt.R;
import com.smk.bt.presenter.BTContractsListPresenter;

public class BTContractsListFragment extends BaseFragment<IBTContractsListView,BTContractsListPresenter<IBTContractsListView>> implements IBTContractsListView {
    @Override
    protected BTContractsListPresenter<IBTContractsListView> createPresenter() {
        return new BTContractsListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contracs_list;
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
