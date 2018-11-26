package com.semisky.bluetoothproject.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.presenter.BtSettingPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.BtSettingInterface;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.view.BtMessageDialog;
import com.semisky.bluetoothproject.view.DialogTips;

import java.util.Locale;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要: 设置页面
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtSettingFragment extends BaseFragment<BtSettingInterface, BtSettingPresenter>
        implements BtSettingInterface, View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = Logger.makeTagLog(BtSettingFragment.class);

    private TextView tvDeviceName;
    private Switch tvSetAnswerSwitch, tvSetContactSwitch, tvSetConnectSwitch;
    private RelativeLayout rlSyncContentItem, rlBreakConnectItem;

    private BtMessageDialog btMessageDialog;
    private BtStatusModel btStatusModel;

    @Override
    protected BtSettingPresenter createPresenter() {
        btStatusModel = BtStatusModel.getInstance();
        return new BtSettingPresenter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d(TAG, "onHiddenChanged: hidden " + hidden);
        if (hidden) {
            fragmentHide();
        } else {
            fragmentShow();
        }
    }

    protected void fragmentHide() {
        mPresenter.unregisterListener();
    }

    protected void fragmentShow() {
        mPresenter.initListener();
        initSettingData();
        checkBTConnect();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_setting_fragment));
    }

    @Override
    protected void loadData() {
        mPresenter.initListener();
        initSettingData();
    }

    @Override
    protected void initView(View view) {
        TextView tvSetBtBreakConnect = view.findViewById(R.id.tvSetBtBreakConnect);
        TextView tvSetBtSync = view.findViewById(R.id.tvSetBtSync);
        tvDeviceName = view.findViewById(R.id.tvSetDeviceName);
        tvSetContactSwitch = view.findViewById(R.id.tvSetContactSwitch);
        tvSetConnectSwitch = view.findViewById(R.id.tvSetConnectSwitch);
        tvSetAnswerSwitch = view.findViewById(R.id.tvSetAnswerSwitch);
        rlBreakConnectItem = view.findViewById(R.id.rlBreakConnectItem);
        rlSyncContentItem = view.findViewById(R.id.rlSyncContentItem);

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.contains("zh")) {
            tvSetContactSwitch.setTrackResource(R.drawable.btn_set_switch_selector);
            tvSetConnectSwitch.setTrackResource(R.drawable.btn_set_switch_selector);
            tvSetAnswerSwitch.setTrackResource(R.drawable.btn_set_switch_selector);
        } else {
            tvSetContactSwitch.setTrackResource(R.drawable.btn_eng_set_switch_selector);
            tvSetConnectSwitch.setTrackResource(R.drawable.btn_eng_set_switch_selector);
            tvSetAnswerSwitch.setTrackResource(R.drawable.btn_eng_set_switch_selector);
        }

        tvSetAnswerSwitch.setOnCheckedChangeListener(this);
        tvSetContactSwitch.setOnCheckedChangeListener(this);
        tvSetConnectSwitch.setOnCheckedChangeListener(this);

        tvSetBtSync.setOnClickListener(this);
        tvSetBtBreakConnect.setOnClickListener(this);

        checkBTConnect();
    }

    /**
     * 根据蓝牙连接状态显示View
     */
    private void checkBTConnect() {
        boolean btConnect = mPresenter.isConnected();
        Log.d(TAG, "checkBTConnect: " + btConnect);
        if (btConnect) {
            rlBreakConnectItem.setVisibility(View.VISIBLE);
            rlSyncContentItem.setVisibility(View.VISIBLE);
        } else {
            rlBreakConnectItem.setVisibility(View.GONE);
            rlSyncContentItem.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_setting_fragment));
    }

    @Override
    protected int getResourceId() {
        return R.layout.bt_set_fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvSetBtBreakConnect:
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                mPresenter.breakConnect();
                BtSPUtil.getInstance().clean(getContext());
//                BtSPUtil.getInstance().cleanSetting(getContext());
                break;
            case R.id.tvSetBtSync:
                startDownload();
                break;
        }
    }

    private int checkDownLoadStatus() {
        Logger.d(TAG, "------------getSyncForStatus:" + btStatusModel.getSyncForStatus());
        switch (btStatusModel.getSyncForStatus()) {
            case RECORD:
            case ALL_IN_RECORD:
            case RECORD_LOADING_WAIT_CONTACT:
                return R.string.cx62_bt_syncing_call_logs;

            case CONTACT_LOADING_WAIT_RECORD:
            case ALL_IN_CONTACT:
            case CONTACT:
                return R.string.cx62_bt_syncing_contacts;

            case NULL:
            case PERMISSION:
            case ONCE_RECORD:
            default:
                return -1;
        }
    }

    private DialogTips mDialogTips;

    private void startDownload() {
        int status = checkDownLoadStatus();
        Logger.d(TAG, "startDownload------------status:" + status);
        if (status != -1) {
            if (mDialogTips == null) {
                mDialogTips = new DialogTips(getContext());
            }
            if (!mDialogTips.isShowing()) {
                mDialogTips.setTextId(status);
                mDialogTips.show();
            }
            return;
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                btMessageDialog = new BtMessageDialog(getContext());
                btMessageDialog.initCancelConfirmButtonView(getString(R.string.cx62_bt_dialog_if_sync_contacts),
                        new BtMessageDialog.ClickConfirmListener() {
                            @Override
                            public void clickConfirm() {
                                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.ALL_IN_CONTACT);
                                mPresenter.reqPbapDownloadAll();
                                btMessageDialog.dismiss();
                            }
                        });
                Logger.d(TAG, "startDownload------------show:");
                btMessageDialog.show();
            }
        });
    }

    public void initSettingData() {
        mPresenter.initBTStatus();
//        tvDeviceName.setText(btStatusModel.getPhoneName());
        tvDeviceName.setText("BFDA");
        tvSetConnectSwitch.setChecked(BtSPUtil.getInstance().getAutoConnStateSP(getContext()));
        tvSetAnswerSwitch.setChecked(BtSPUtil.getInstance().getAutoAnswerStateSP(getContext()));
        tvSetContactSwitch.setChecked(BtSPUtil.getInstance().getAutoSyncBookStateSP(getContext()));
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.tvSetAnswerSwitch:
                if (isChecked) {
                    BtSPUtil.getInstance().setAutoAnswerStateSP(getContext(), true);
                } else {
                    BtSPUtil.getInstance().setAutoAnswerStateSP(getContext(), false);
                }
                break;
            case R.id.tvSetContactSwitch:
                if (isChecked) {
                    BtSPUtil.getInstance().setAutoSyncBookStateSP(getContext(), true);
                } else {
                    BtSPUtil.getInstance().setAutoSyncBookStateSP(getContext(), false);
                }
                break;
            case R.id.tvSetConnectSwitch:
                if (isChecked) {
                    mPresenter.setAutoConnect(true);
                    BtSPUtil.getInstance().setAutoConnStateSP(getContext(), true);
                } else {
                    mPresenter.setAutoConnect(false);
                    BtSPUtil.getInstance().setAutoConnStateSP(getContext(), false);
                }
                break;
        }
    }

}
