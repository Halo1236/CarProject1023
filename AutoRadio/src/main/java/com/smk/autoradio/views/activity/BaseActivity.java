package com.smk.autoradio.views.activity;

import android.app.Activity;
import android.os.Bundle;

import com.smk.autoradio.presenter.BasePresenter;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends Activity {

    protected P mPrestener;

    protected abstract int getLayoutResID();

    protected abstract void initViews();

    protected abstract void initData();

    protected abstract P createPresenter();

    protected boolean isBindPresenter() {
        return (null != mPrestener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResID());
        this.initViews();// 初始化控件
        this.mPrestener = createPresenter();// 初始化表示层
        mPrestener.attatchView((V) this);// 视图层与表示层绑定
        initData();// 初始化数据
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBindPresenter()) {
            mPrestener.detachView();
        }
    }
}
