package com.semisky.bluetoothproject.presenter;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * 基础表示层
 * 为防止Presenter一直拥有Activity的对象，加入弱引用
 *
 * @param <V> 视图接口
 * @author chenhongrui
 */

public class BasePresenter<V> {

    private Reference<V> mViewRfr;

    public void onAttachView(V view) {
        this.mViewRfr = new WeakReference<>(view);
    }

    protected V getViewRfr() {
        return mViewRfr.get();
    }

    public void onDetachView() {
        if (mViewRfr != null) {
            mViewRfr.clear();
            mViewRfr = null;
        }
    }

    public boolean isViewAttached() {
        return mViewRfr != null && mViewRfr.get() != null;
    }


}
