package com.semisky.btcarkit.view.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.aidl.BTDeviceInfo;
import com.semisky.btcarkit.prenster.BTDeviceListPresenter;
import com.semisky.btcarkit.utils.Logutil;
import com.semisky.btcarkit.view.adpter.BTDeviceListAdapter;

import java.util.List;

public class BTDeviceListFragment extends BaseFragment<IBTDeviceListView, BTDeviceListPresenter<IBTDeviceListView>> implements IBTDeviceListView, View.OnClickListener, AdapterView.OnItemClickListener {
    private ListView listview;
    private BTDeviceListAdapter mBTDeviceListAdapter;

    private Button
            btn_start_scann_new_device,
            btn_disconnect_current_device;
    private TextView tv_device_scan_state, tv_device_hfp_state,tv_device_a2dp_state;

    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTDeviceListFragment.class);
    }

    @Override
    protected BTDeviceListPresenter<IBTDeviceListView> createPresenter() {
        return new BTDeviceListPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_device_list;
    }

    @Override
    protected void initView() {
        listview = (ListView) mContentView.findViewById(R.id.listview);
        btn_start_scann_new_device = (Button) mContentView.findViewById(R.id.btn_start_scann_new_device);
        btn_disconnect_current_device = (Button) mContentView.findViewById(R.id.btn_disconnect_current_device);
        tv_device_scan_state = (TextView) mContentView.findViewById(R.id.tv_device_scan_state);
        tv_device_hfp_state = (TextView) mContentView.findViewById(R.id.tv_device_hfp_state);
        tv_device_a2dp_state = (TextView) mContentView.findViewById(R.id.tv_device_a2dp_state);

    }

    @Override
    protected void setListener() {
        mBTDeviceListAdapter = new BTDeviceListAdapter(getActivity());
        listview.setAdapter(mBTDeviceListAdapter);
        listview.setOnItemClickListener(this);
        btn_start_scann_new_device.setOnClickListener(this);
        btn_disconnect_current_device.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start_scann_new_device:
                if (isBindPresenter()) {
                    mPresenter.reqStartBtDeviceDiscovery();
                }
                break;
            case R.id.btn_disconnect_current_device:
                if(isBindPresenter()){
                    mPresenter.reqBtDisconnectAll();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        BTDeviceInfo mBTDeviceInfo = (BTDeviceInfo) mBTDeviceListAdapter.getItem(position);
        String address = mBTDeviceInfo.getAddress();
        mPresenter.reqBtConnectHfpA2dp(address);
        Logutil.i(TAG, "onItemClick() address = " + address);
    }

    @Override
    public void onRefreshDeviceList(List<BTDeviceInfo> infos) {
        mBTDeviceListAdapter.refreshDataToAdapter(infos);
    }

    @Override
    public void onScanStateChanged(String state) {
        String text = getString(R.string.bt_device_scan_state_text);
        state = null != state ? state : "null";
        tv_device_scan_state.setText(text + state);
    }

    @Override
    public void onNotifyHfpStateChanged(String state) {
        String text = getString(R.string.bt_device_hfp_state_text);
        tv_device_hfp_state.setText(text + state);
    }

    @Override
    public void onNotifyA2dpStateChanged(String state) {
        String text = getString(R.string.bt_device_a2dp_state_text);
        tv_device_a2dp_state.setText(text + state);
    }
}
