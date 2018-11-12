package com.semisky.btcarkit.view.fragment;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.prenster.BTContactsPresenter;
import com.semisky.btcarkit.utils.Logutil;

public class BTContactsFragment extends BaseFragment<IBTContactsView, BTContactsPresenter<IBTContactsView>> implements IBTContactsView{

    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTContactsFragment.class);
    }

    @Override
    protected BTContactsPresenter<IBTContactsView> createPresenter() {
        return new BTContactsPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void setListener() {

    }
}
