package com.smk.bt.presenter;

import java.lang.ref.WeakReference;

public class BasePresenter<V> {
    protected WeakReference<V> mViewRfr;

    public void onAttatchView(V view) {
        this.mViewRfr = new WeakReference<V>(view);
    }

    public void onDetachView() {
        if (null != mViewRfr) {
            this.mViewRfr.clear();
            this.mViewRfr = null;
        }
    }

    protected boolean isBindView() {
        return (null != mViewRfr && null != mViewRfr.get());
    }
}
