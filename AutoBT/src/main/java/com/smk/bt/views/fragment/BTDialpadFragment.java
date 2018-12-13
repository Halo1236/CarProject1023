package com.smk.bt.views.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.smk.bt.R;
import com.smk.bt.presenter.BTDialpadPresenter;
import com.smk.bt.views.adapter.BTDialpadAdapter;

import java.util.List;

public class BTDialpadFragment extends BaseFragment<IBTDialpadView, BTDialpadPresenter<IBTDialpadView>> implements IBTDialpadView,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,
        View.OnClickListener,
        View.OnLongClickListener {
    private BTDialpadAdapter mBTDialpadAdapter;
    private GridView keyContainer;
    private TextView tvInputboxText;
    private ImageButton btnDelete, btnDial;

    @Override
    protected BTDialpadPresenter<IBTDialpadView> createPresenter() {
        return new BTDialpadPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialpad;
    }

    @Override
    protected void initViews() {
        mBTDialpadAdapter = new BTDialpadAdapter(getContext());
        keyContainer = mContentView.findViewById(R.id.keyContainer);
        tvInputboxText = mContentView.findViewById(R.id.tvInputboxText);
        btnDelete = mContentView.findViewById(R.id.btnDelete);
        btnDial = mContentView.findViewById(R.id.btnDial);
    }

    @Override
    protected void initListener() {
        keyContainer.setAdapter(mBTDialpadAdapter);
        keyContainer.setOnItemClickListener(this);
        keyContainer.setOnItemLongClickListener(this);
        btnDelete.setOnClickListener(this);
        btnDelete.setOnLongClickListener(this);
        btnDial.setOnClickListener(this);
    }

    @Override
    protected void initUiState() {
        if (!isBindPresenter()) {
            return;
        }
        mPresenter.loadDiapadContant();
    }

    @Override
    public void onUpdateDialpadContent(List<String> data) {
        if (!isBindPresenter()) {
            return;
        }
        mBTDialpadAdapter.updateData(data);
    }

    @Override
    public void onChangeInputboxTextContent(String str) {
        if (!isBindPresenter()) {
            return;
        }
        tvInputboxText.setText(str);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isBindPresenter()) {
            return;
        }
        String dialpadText = (String) mBTDialpadAdapter.getItem(position);
        mPresenter.handlerDialpadTextContent(dialpadText);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        if (!isBindPresenter()) {
            return false;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(!isBindPresenter()){
            return;
        }
        switch (v.getId()) {
            case R.id.btnDelete:
                mPresenter.handlerOneByOneDeleteInputboxContent();
                break;
            case R.id.btnDial:
                mPresenter.handlerDialing();
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(!isBindPresenter()){
            return false;
        }
        switch (v.getId()) {
            case R.id.btnDelete:
                mPresenter.deleteAllInputboxContent();
                break;
        }
        return false;
    }
}
