package com.smk.bt.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.smk.bt.presenter.BasePresenter;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends FragmentActivity {

    protected P mPresenter;

    protected abstract int getLayoutResId();

    protected abstract void handlerIntent(Intent intent);

    protected abstract P createPresenter();

    protected abstract void initViews();

    protected abstract void initListeners();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        this.mPresenter = createPresenter();
        if(null != mPresenter){
            this.mPresenter.onAttatchView((V)this);
        }
        initViews();
        initListeners();
        handlerIntent(super.getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null != mPresenter){
            mPresenter.onDetachView();
        }
    }

    protected boolean isBindPresenter(){
        return (null != mPresenter);
    }
}
