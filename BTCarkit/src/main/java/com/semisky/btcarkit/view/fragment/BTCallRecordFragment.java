package com.semisky.btcarkit.view.fragment;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.prenster.BTCallRecordPresenter;
import com.semisky.btcarkit.utils.Logutil;

public class BTCallRecordFragment extends BaseFragment<IBTCallRecordView, BTCallRecordPresenter<IBTCallRecordView>> {

    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTCallRecordFragment.class);
    }

    @Override
    protected BTCallRecordPresenter<IBTCallRecordView> createPresenter() {
        return new BTCallRecordPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_callrecord;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Logutil.i(TAG,"onHiddenChanged() hidden : "+ hidden);
    }
}
