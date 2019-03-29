package com.smkbt.view;

import android.app.Activity;
import android.os.Bundle;

import com.smkbt.presenter.BasePresenter;

public abstract class BaseActivity<V , P extends BasePresenter<V>> extends Activity {

    private P mPresenter;

    protected abstract P createPresenter();

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mPresenter = createPresenter();
        mPresenter.attachView((V)this);
    }
}
