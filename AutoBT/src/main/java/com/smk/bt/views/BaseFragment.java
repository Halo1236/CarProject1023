package com.smk.bt.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smk.bt.presenter.BasePresenter;

public abstract class BaseFragment<V, P extends BasePresenter<V>> extends Fragment {
    private View mContentView;
    private P mPresenter;

    protected abstract P createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initViews();

    protected abstract void initListener();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContentView = inflater.inflate(getLayoutResId(), container, false);
        this.mPresenter = createPresenter();
        if (null != mPresenter) {
            this.mPresenter.onAttatch((V) this);
        }
        return this.mContentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
        initListener();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mPresenter) {
            this.mPresenter.onDetach();
        }
    }
}
