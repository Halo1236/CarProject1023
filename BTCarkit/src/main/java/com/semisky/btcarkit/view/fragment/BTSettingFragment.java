package com.semisky.btcarkit.view.fragment;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.prenster.BTSettingPresenter;
import com.semisky.btcarkit.utils.Logutil;

public class BTSettingFragment extends BaseFragment<IBTSettingView, BTSettingPresenter<IBTSettingView>> implements IBTSettingView {

    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTSettingFragment.class);
    }

    @Override
    protected BTSettingPresenter<IBTSettingView> createPresenter() {
        return new BTSettingPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }
}
