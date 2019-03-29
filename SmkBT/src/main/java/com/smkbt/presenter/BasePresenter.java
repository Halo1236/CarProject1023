package com.smkbt.presenter;

import java.lang.ref.WeakReference;

public abstract class BasePresenter<V> {
    private WeakReference<V> mViewRfr;

    public void attachView(V view){
        if(null == mViewRfr){
            mViewRfr = new WeakReference<>(view);
        }
    }

    public void detachView(){
        if(isBindView()){
            mViewRfr.clear();
            mViewRfr = null;
        }
    }

    public boolean isBindView(){
        return (null != mViewRfr && null != mViewRfr.get());
    }

}
