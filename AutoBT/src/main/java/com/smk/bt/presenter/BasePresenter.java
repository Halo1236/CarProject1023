package com.smk.bt.presenter;

import java.lang.ref.WeakReference;

public class BasePresenter<V> {
    protected WeakReference<V> mViewRfr;

    public void onAttatch(V view) {
        this.mViewRfr = new WeakReference<V>(view);
    }

    public void onDetach() {
        if (null != mViewRfr) {
            this.mViewRfr.clear();
            this.mViewRfr = null;
        }
    }

    public boolean isBindView() {
        return (null != mViewRfr && null != mViewRfr.get());
    }
}
