package com.semisky.btcarkit.prenster;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;


public class BasePresenter<V> {
    protected WeakReference<V> mViewRef;
    protected Handler _handler = new Handler(Looper.getMainLooper());

    public void onAttatchView(V view) {
        this.mViewRef = new WeakReference<V>(view);
    }

    public void onDetachView() {
        _handler.removeCallbacksAndMessages(null);
        this.mViewRef.clear();
        this.mViewRef = null;
        this._handler = null;
    }

    public boolean isBindView() {
        return (null != this.mViewRef && null != this.mViewRef.get());
    }
}
