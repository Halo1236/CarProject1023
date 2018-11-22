package com.semisky.bluetoothproject.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.semisky.bluetoothproject.presenter.BasePresenter;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要:fragment公共类
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public abstract class BaseFragment<V, P extends BasePresenter<V>> extends Fragment {

    private static final String TAG = Logger.makeTagLog(BaseFragment.class);

    protected Context mContent;

    protected P mPresenter;

    private View rootView;
    protected LayoutInflater inflater;

    protected abstract P createPresenter();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContent = getActivity();
        this.mPresenter = createPresenter();
        this.mPresenter.onAttachView((V) this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            this.inflater = inflater;
            rootView = inflater.inflate(getResourceId(), null);
        }
        //缓存的rootView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        initView(rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
    }

    protected abstract void loadData();

    protected abstract void initView(View view);

    protected abstract int getResourceId();

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mPresenter.onDetachView();
    }
}
