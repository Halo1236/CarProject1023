package com.smk.bt.views.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.smk.bt.presenter.BasePresenter;
import com.smk.bt.utils.Logger;

public abstract class BaseActivity<V, P extends BasePresenter<V>> extends FragmentActivity {
    private BroadcastReceiver mHomeReceiver;
    protected String TAG;

    protected P mPresenter;

    protected abstract String getTagLog();

    protected abstract int getLayoutResId();

    protected abstract void handlerIntent(Intent intent);

    protected abstract P createPresenter();

    protected abstract void initViews();

    protected abstract void initListeners();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        TAG = getTagLog();
        Logger.i(TAG, "onCreate() ...");
        this.mPresenter = createPresenter();
        if (null != mPresenter) {
            this.mPresenter.onAttatchView((V) this);
        }
        initViews();
        initListeners();
        handlerIntent(super.getIntent());
        registerHomeReceiver();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.i(TAG, "onDestroy() ...");
        if (null != mPresenter) {
            mPresenter.onDetachView();
            unregisterHomeReceiver();
        }

    }

    protected boolean isBindPresenter() {
        return (null != mPresenter);
    }

    private void registerHomeReceiver() {
        if (null == mHomeReceiver) {
            mHomeReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    finish();
                }
            };
            registerReceiver(mHomeReceiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
            Logger.i(TAG, "registerHomeReceiver() ...");
        }
    }

    private void unregisterHomeReceiver() {
        if (null != mHomeReceiver) {
            unregisterReceiver(mHomeReceiver);
            this.mHomeReceiver = null;
            Logger.i(TAG, "unregisterHomeReceiver() ...");
        }
    }

}
