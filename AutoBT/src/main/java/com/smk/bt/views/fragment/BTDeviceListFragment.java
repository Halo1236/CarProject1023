package com.smk.bt.views.fragment;

import android.view.View;
import android.widget.Button;

import com.smk.bt.R;
import com.smk.bt.presenter.BTDeviceListPresenter;
import com.smk.bt.utils.Logger;

public class BTDeviceListFragment extends BaseFragment<IBTDeviceListView, BTDeviceListPresenter<IBTDeviceListView>> implements IBTDeviceListView, View.OnClickListener {
    private static final String TAG = Logger.makeLogTag(BTDeviceListFragment.class);
    private Button btn_bt_switch,btn_bt_rediscovery;

    @Override
    protected BTDeviceListPresenter<IBTDeviceListView> createPresenter() {
        return new BTDeviceListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_device_list;
    }

    @Override
    protected void initViews() {
        btn_bt_switch = (Button) mContentView.findViewById(R.id.btn_bt_switch);
        btn_bt_rediscovery = (Button) mContentView.findViewById(R.id.btn_bt_rediscovery);
    }

    @Override
    protected void initListener() {
        btn_bt_switch.setOnClickListener(this);
        btn_bt_rediscovery.setOnClickListener(this);
    }


    @Override
    protected void initUiState() {
        if (isBindPresenter()) {
            mPresenter.initBTSwitchState();
        }
    }

    @Override
    public void onClick(View v) {
        if (!isBindPresenter()) {
            return;
        }
        switch (v.getId()) {
            case R.id.btn_bt_switch:
                Logger.v(TAG,"CLICK BT SWITCH ...");
                mPresenter.setBTSwitchState();
                break;
            case R.id.btn_bt_rediscovery:
                break;
        }
    }

    @Override
    public void onBTSwitchStateChange(boolean enable) {
        int textResId = enable ? R.string.bt_tab_device_list_swtich_on_text : R.string.bt_tab_device_list_swtich_off_text;
        btn_bt_switch.setText(textResId);
    }
}
