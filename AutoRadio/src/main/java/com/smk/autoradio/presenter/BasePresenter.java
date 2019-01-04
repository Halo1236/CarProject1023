package com.smk.autoradio.presenter;

import java.lang.ref.WeakReference;

public class BasePresenter<V> {
    protected WeakReference<V> mRfrView;

    public void attatchView(V view) {
        this.mRfrView = new WeakReference<V>(view);
    }

    public void detachView() {
        if (isBindView()) {
            this.mRfrView.clear();
            this.mRfrView = null;
        }
    }

    public boolean isBindView() {
        return (null != mRfrView && null != mRfrView.get());
    }

}
