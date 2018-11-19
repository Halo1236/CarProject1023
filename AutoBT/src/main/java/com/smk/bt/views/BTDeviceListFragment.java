package com.smk.bt.views;

import com.smk.bt.presenter.BTDeviceListPresenter;

public class BTDeviceListFragment extends BaseFragment<IBTDeviceListView, BTDeviceListPresenter<IBTDeviceListView>> implements IBTDeviceListView {

    @Override
    protected BTDeviceListPresenter<IBTDeviceListView> createPresenter() {
        return new BTDeviceListPresenter();
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
