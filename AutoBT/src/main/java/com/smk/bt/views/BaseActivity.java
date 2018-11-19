package com.smk.bt.views;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.smk.bt.presenter.BasePresenter;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends FragmentActivity {

    protected P mPresenter;

    protected abstract P createPresenter();

    protected abstract void initViews();

    protected abstract void initListeners();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = createPresenter();
        if(null != mPresenter){
            this.mPresenter.onAttatch((V)this);
        }
        initViews();
        initListeners();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPresenter){
            mPresenter.onDetach();
        }
    }
}
