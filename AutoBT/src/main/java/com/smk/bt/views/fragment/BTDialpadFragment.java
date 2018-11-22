package com.smk.bt.views.fragment;

import com.smk.bt.R;
import com.smk.bt.presenter.BTDialpadPresenter;

public class BTDialpadFragment extends BaseFragment<IBTDialpadView,BTDialpadPresenter<IBTDialpadView>> implements IBTDialpadView {
    @Override
    protected BTDialpadPresenter<IBTDialpadView> createPresenter() {
        return new BTDialpadPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialpad;
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
