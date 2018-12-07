package com.smk.bt.views.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smk.bt.presenter.BasePresenter;

public abstract class BaseFragment<V, P extends BasePresenter<V>> extends Fragment {
    protected View mContentView;
    protected P mPresenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initViews();

    protected abstract void initListener();

    protected abstract void initUiState();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContentView = inflater.inflate(getLayoutResId(), container, false);
        this.mPresenter = createPresenter();
        if (null != mPresenter) {
            this.mPresenter.onAttatchView((V) this);
        }
        return this.mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListener();
        initUiState();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter) {
            this.mPresenter.onDetachView();
        }
    }

    protected boolean isBindPresenter(){
        return (null != mPresenter);
    }
}
