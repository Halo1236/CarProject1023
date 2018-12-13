package com.smk.bt.views.fragment;

import android.content.DialogInterface;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.smk.bt.R;
import com.smk.bt.bean.BTDeviceInfo;
import com.smk.bt.broadcast.BTReceiver;
import com.smk.bt.presenter.BTDeviceListPresenter;
import com.smk.bt.utils.Logger;
import com.smk.bt.views.adapter.BTDeviceListAdapter;
import com.smk.bt.views.custom.DialogFactory;
import com.smk.bt.views.custom.SmartDialog;

import java.util.List;

public class BTDeviceListFragment extends BaseFragment<IBTDeviceListView, BTDeviceListPresenter<IBTDeviceListView>> implements IBTDeviceListView, View.OnClickListener, BTDeviceListAdapter.OnDeviceListItemListener {
    private static final String TAG = Logger.makeLogTag(BTDeviceListFragment.class);
    private BTDeviceListAdapter mBTDeviceListAdapter;
    private Button btn_bt_switch, btn_bt_rediscovery;
    private ListView lv_devicelist;
    private SmartDialog mBTSwitchStateDialog;
    private SmartDialog mBTPairDialog;
    private SmartDialog mBTDiscoveryDialog;


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
        mBTDeviceListAdapter = new BTDeviceListAdapter(getActivity());
        mBTDeviceListAdapter.registerCallback(this);
        btn_bt_switch = (Button) mContentView.findViewById(R.id.btn_bt_switch);
        btn_bt_rediscovery = (Button) mContentView.findViewById(R.id.btn_bt_rediscovery);
        lv_devicelist = (ListView) mContentView.findViewById(R.id.lv_devicelist);
    }

    @Override
    protected void initListener() {
        btn_bt_switch.setOnClickListener(this);
        btn_bt_rediscovery.setOnClickListener(this);
        lv_devicelist.setAdapter(mBTDeviceListAdapter);
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
                Logger.v(TAG, "CLICK BT SWITCH ...");
                mPresenter.setBtEnable();
                break;
            case R.id.btn_bt_rediscovery:
                Logger.v(TAG, "CLICK startBtDiscovery ...");
                mPresenter.startBtDiscovery();
                break;
        }
    }

    @Override
    public void onBTSwitchStateChange(boolean enable) {
        btn_bt_switch.setText(enable ? R.string.bt_tab_device_list_swtich_on_text : R.string.bt_tab_device_list_swtich_off_text);
        btn_bt_rediscovery.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onChangeDeviceList(List<BTDeviceInfo> btDeviceInfoList) {
        if (isBindPresenter()) {
            mBTDeviceListAdapter.updateData(btDeviceInfoList);
        }
    }

    @Override
    public void onChangeStateOfBTSwitchDialog(int state) {
        if (!isBindPresenter()) {
            return;
        }
        String titleText = "";
        if (null == mBTSwitchStateDialog) {
            mBTSwitchStateDialog = DialogFactory.createOneTitleThemeDialog(getActivity(), titleText, R.drawable.dialog_global_bg);

        }
        switch (state) {
            case BT_STATE_OFF:
                mBTSwitchStateDialog.setTimeout(2000);
                mBTSwitchStateDialog.enableTimeoutDismiss(true);
                mBTSwitchStateDialog.setCanceledOnTouchOutside(true);
                titleText = getString(R.string.bt_switch_state_off_text);
                break;
            case BT_STATE_TURNING_ON:
                mBTSwitchStateDialog.setTimeout(20000);
                mBTSwitchStateDialog.enableTimeoutDismiss(true);
                mBTSwitchStateDialog.setCanceledOnTouchOutside(false);
                titleText = getString(R.string.bt_switch_state_turning_on_text);
                break;
            case BT_STATE_ON:
                mBTSwitchStateDialog.setTimeout(2000);
                mBTSwitchStateDialog.enableTimeoutDismiss(true);
                mBTSwitchStateDialog.setCanceledOnTouchOutside(true);
                titleText = getString(R.string.bt_switch_state_on_text);
                break;
            case BT_STATE_TURNING_OFF:
                mBTSwitchStateDialog.setTimeout(20000);
                mBTSwitchStateDialog.enableTimeoutDismiss(true);
                mBTSwitchStateDialog.setCanceledOnTouchOutside(false);
                titleText = getString(R.string.bt_switch_state_off_text);
                break;

        }
        if (state >= BT_STATE_OFF && state <= BT_STATE_TURNING_OFF) {
            mBTSwitchStateDialog.setTitleText(SmartDialog.TITLE_FIRST, titleText);
            mBTSwitchStateDialog.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != mBTSwitchStateDialog && mBTSwitchStateDialog.isShowing()) {
            mBTSwitchStateDialog.dismiss();
            mBTSwitchStateDialog = null;
        }
    }

    // BTDeviceListAdapter.OnDeviceListItemListener.onBtDevicePair()
    @Override
    public void onBtDevicePair(String address) {
        if (isBindPresenter()) {
            mPresenter.reqBtConnectHfpA2dp(address);
        }
    }

    // BTDeviceListAdapter.OnDeviceListItemListener.onBtDeviceDelete()
    @Override
    public void onBtDeviceDelete(String address) {
        if (isBindPresenter()) {

        }
    }

    @Override
    public void onChangeStateOfBTPair(int state) {
        String titleText = "";
        if (!isBindPresenter()) {
            return;
        }
        if (null == mBTPairDialog) {
            this.mBTPairDialog = DialogFactory.createOneTitleThemeDialog(getActivity(), titleText, R.drawable.dialog_global_bg);
        }
        switch (state) {
            case BT_STATE_BOND_NONE:// pair fail
                mBTPairDialog.setTimeout(2000);
                mBTPairDialog.enableTimeoutDismiss(true);
                mBTPairDialog.setCanceledOnTouchOutside(true);
                titleText = getString(R.string.bt_pair_state_pair_fail);
                break;
            case BT_STATE_BONDING:// pairing
                mBTPairDialog.setTimeout(20000);
                mBTPairDialog.enableTimeoutDismiss(true);
                mBTPairDialog.setCanceledOnTouchOutside(false);
                titleText = getString(R.string.bt_pair_state_pairing);
                break;
            case BT_STATE_BONDED:// paired
                mBTPairDialog.setTimeout(500);
                mBTPairDialog.enableTimeoutDismiss(true);
                mBTPairDialog.setCanceledOnTouchOutside(true);
                titleText = getString(R.string.bt_pair_state_pair_success);
                break;
        }

        if (state >= BT_STATE_BOND_NONE && state <= BT_STATE_BONDED) {
            mBTPairDialog.setTitleText(SmartDialog.TITLE_FIRST, titleText);
            mBTPairDialog.show();
        }

    }

    @Override
    public void onChangeStateOfBTDiscovery(int state) {
        if (!isBindPresenter()) {
            return;
        }
        if (null == mBTDiscoveryDialog) {
            this.mBTDiscoveryDialog = DialogFactory.createOneTitleOneButtonThemeDialog(getActivity(),
                    "",
                    getString(R.string.bt_discovery_dialog_btn_cancel),
                    R.drawable.dialog_global_bg,
                    R.drawable.dialog_btn_negative_normal,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (isBindPresenter()) {
                                mPresenter.cancelBtDiscovery();
                            }
                        }
                    });

            this.mBTDiscoveryDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    if (isBindPresenter()) {
                        mPresenter.cancelBtDiscovery();
                    }
                }
            });
        }
        switch (state) {
            case BT_STATE_DISCOVERY_START:
                mBTDiscoveryDialog.setTitleText(SmartDialog.TITLE_FIRST, getString(R.string.bt_discovery_start));
                mBTDiscoveryDialog.show();
                break;
            case BT_STATE_DISCOVERY_FINISHED:
                mBTDiscoveryDialog.dismiss();
                break;
        }
    }
}
