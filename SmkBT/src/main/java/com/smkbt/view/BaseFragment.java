package com.smkbt.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smkbt.presenter.BasePresenter;

public abstract class BaseFragment<V ,P extends BasePresenter<V>> extends Fragment {
    protected View mContentView;
    protected P mPresenter;
    protected abstract P createPresenter();
    protected abstract int getLayoutResId();
    protected abstract void initViews();
    protected abstract void initState();

    
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        return mContentView = inflater.inflate(getLayoutResId(),container);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = createPresenter();
        mPresenter.attachView((V)this);
        initViews();
        initState();
    }

    public boolean isBindPresenter() {
        return (null != mPresenter);
    }
}
