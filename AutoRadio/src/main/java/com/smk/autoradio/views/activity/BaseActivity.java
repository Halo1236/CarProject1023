package com.smk.autoradio.views.activity;

import android.app.Activity;
import android.os.Bundle;

import com.smk.autoradio.presenter.BasePresenter;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends Activity {

    protected P mPrestener;

    protected abstract int getLayoutResID();

    protected abstract void initViews();

    protected abstract P createPresenter();

    protected boolean isBindPresenter() {
        return (null != mPrestener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        this.initViews();
        this.mPrestener = createPresenter();
        mPrestener.attatchView((V) this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBindPresenter()) {
            mPrestener.detachView();
        }
    }
}
