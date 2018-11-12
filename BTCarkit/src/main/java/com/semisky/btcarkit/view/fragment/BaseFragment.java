package com.semisky.btcarkit.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semisky.btcarkit.prenster.BasePresenter;

public abstract class BaseFragment<V, P extends BasePresenter<V>> extends Fragment {
    protected String TAG;

    protected abstract String getTagLog();

    protected abstract P createPresenter();

    protected abstract int getLayoutResId();

    protected abstract void initView();

    protected abstract void setListener();

    public P mPresenter;
    protected View mContentView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContentView = inflater.inflate(getLayoutResId(), container, false);
        TAG = getTagLog();
        initView();
        return mContentView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.mPresenter = createPresenter();
        this.mPresenter.onAttatchView((V) this);
        setListener();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetachView();
        super.onDestroyView();
    }

    protected boolean isBindPresenter() {
        return (null != this.mPresenter);
    }
}
