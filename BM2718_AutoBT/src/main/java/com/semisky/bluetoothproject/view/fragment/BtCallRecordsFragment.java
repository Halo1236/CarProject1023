package com.semisky.bluetoothproject.view.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.adapter.CallLogListItemAdapter;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.CallLogEntity;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.presenter.BtCallRecordsPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.CallRecordsInterface;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.view.BtMessageDialog;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要: 通话记录fragment
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtCallRecordsFragment extends BaseFragment<CallRecordsInterface, BtCallRecordsPresenter>
        implements CallRecordsInterface {

    private static final String TAG = Logger.makeTagLog(BtCallRecordsFragment.class);
    private final static int MSG_CALLRECORDS_DOWNLOADING_REFRESH_CONTACT_UI = 0x000001;
    private final static int MSG_CALLRECORDS_DOWNLOAD_COMPLETED_REFRESH_CONTACT_UI = 0x000002;
    private final static int MSG_CALLRECORDS_DOWNLOADING_DIAL_REFRESH_CONTACT_UI = 0x000003;
    private final static int MSG_CALLRECORDS_START_DOWNLOAD_BACKGROUND = 0x000004;
    private final static int MSG_CALLRECORDS_DATA_CHANGE = 0x000005;
    private final static int SHOW_DEVICE_FOUND = 0x000006;
    private final static int NONE_DEVICE_FOUND = 0x000007;
    private final static int MSG_CALLRECORDS_TIPS = 0x000008;

    private ArrayList<CallLogEntity> callLogData = new ArrayList<>();
    private CallLogListItemAdapter callLogListItemAdapter;

    private BtMessageDialog btMessageDialog;

    private BtStatusModel btStatusModel;
    private RefreshViewHandler refreshViewHandler;
    private RelativeLayout rlNoneCallLog;
    private TextView tvNoCallRecords,tvSyncFail,tvSyncManual;
    private ListView callLogList;
    private int total = 0;

    @Override
    protected BtCallRecordsPresenter createPresenter() {
        return new BtCallRecordsPresenter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            fragmentHide();
        } else {
            fragmentShow();
        }
    }

    protected void fragmentHide() {
        Logger.d(TAG, "fragmentHide: ");
        if(btMessageDialog != null && btMessageDialog.isShowing()){
            btMessageDialog.dismiss();
            Logger.d(TAG, "dismiss: " );
        }
//        mPresenter.unregisterListener();
    }

    protected void fragmentShow() {
        Logger.d(TAG, "fragmentShow---callLogData: " + callLogData.size());
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_calllog_fragment));
//        mPresenter.initListener();
        download();
    }

    @Override
    public void onResume() {
        super.onResume();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_calllog_fragment));
    }

    @Override
    protected void loadData() {
        Logger.d(TAG, "loadData: ");
        download();
    }

    private void download() {
        Logger.d(TAG, "download----getSyncForStatus: " + btStatusModel.getSyncForStatus());
        if(callLogData != null && !callLogData.isEmpty()){
            return;
        }
        Logger.d(TAG, "download----callLogData: " + callLogData.size());
        switch (btStatusModel.getSyncForStatus()){
            case ONCE_RECORD:
                return;
            case ONCE_RECORD_CONTACT:
            case CONTACT_LOADING_WAIT_RECORD:
            case ALL_IN_CONTACT:
            case RECORD:
            case ALL_IN_RECORD:
            case RECORD_LOADING_WAIT_CONTACT:
                if(callLogData.size() == 0){
                    refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_TIPS,
                            R.string.cx62_bt_syncing_call_logs,R.string.cx62_bt_syncing_call_logs));
                }
                return;
//            case CONTACT:
//                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_TIPS,
//                        R.string.cx62_bt_syncing_call_logs,R.string.cx62_bt_syncing_contacts));
//                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.CONTACT_LOADING_WAIT_RECORD);
//                return;
        }
        if (BtSPUtil.getInstance().getSyncCallLogStateSP(getContext())) {
            if (null == callLogData || callLogData.isEmpty()) {
                total = LitePal.count(CallLogEntity.class);
                Logger.d(TAG, "----total:" + total);
                callLogData.clear();
                syncQuery(0);
            }

        } else {
            if(!BtSPUtil.getInstance().getAutoSyncBookStateSP(getContext())){
                Logger.d(TAG, "download: 下载通话记录");
                startDownload();
            }
        }
    }

    private void syncQuery(int offset) {

        LitePal.limit(BtConstant.PBAP.PBAP_QUERY_COUNT).offset(offset).findAsync(CallLogEntity.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_DATA_CHANGE, (List<CallLogEntity>) t));
            }
        });
    }

    @Override
    protected void initView(View view) {
        callLogList = view.findViewById(R.id.lvCallLogList);
        rlNoneCallLog = view.findViewById(R.id.rlNoneCallLog);
        tvNoCallRecords = view.findViewById(R.id.tvNoCallRecords);
        tvSyncFail = view.findViewById(R.id.tvSyncFail);
        tvSyncManual = view.findViewById(R.id.tvSyncManual);
        callLogListItemAdapter = new CallLogListItemAdapter(getContext(), R.layout.item_call_log_view, callLogData);
        callLogList.setAdapter(callLogListItemAdapter);
        btStatusModel = BtStatusModel.getInstance();
        btMessageDialog = new BtMessageDialog(getContext());
        callLogList.setOnItemClickListener(new MyListViewItem());

        refreshViewHandler = new RefreshViewHandler(this);
        mPresenter.initListener();
    }

    @Override
    protected int getResourceId() {
        return R.layout.call_records_fragment;
    }
    private boolean isConfirm = false;
    private void startDownload() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btMessageDialog != null) {
                    btMessageDialog.initCancelConfirmButtonView(getString(R.string.cx62_bt_dialog_if_sync_calllogs),
                            new BtMessageDialog.ClickConfirmListener() {
                                @Override
                                public void clickConfirm() {
                                    isConfirm = true;
                                    if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT){
                                        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_TIPS,
                                                R.string.cx62_bt_syncing_call_logs,R.string.cx62_bt_syncing_contacts));
                                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.CONTACT_LOADING_WAIT_RECORD);
                                    }else{
                                        refreshViewHandler.sendEmptyMessage(MSG_CALLRECORDS_START_DOWNLOAD_BACKGROUND);
                                    }
                                    btMessageDialog.dismiss();
                                }
                            }, new BtMessageDialog.ClickCancelListener() {
                                @Override
                                public void clickCancel() {
                                    refreshViewHandler.sendEmptyMessage(NONE_DEVICE_FOUND);
                                    btMessageDialog.dismiss();
                                }
                            },new BtMessageDialog.DialogDisMissListener(){
                                @Override
                                public void dialogDisMissCallback(DialogInterface dialog) {
                                    if(isConfirm){
                                        isConfirm = false;
                                        return;
                                    }
                                    refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_TIPS,
                                            R.string.cx62_bt_no_call_log,R.string.cx62_bt_please_manual_sync));
                                }
                            });

                    btMessageDialog.show();
                }
            }
        });

    }

    private void downloadBackground() {
        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.RECORD);
        mPresenter.downloadedCallLog();
    }

    @Override
    public void onDownloadStart() {
        callLogData.clear();
    }

    @Override
    public void downloadFailed() {
        Logger.d(TAG, "downloadFailed: 下载通话记录失败");
        callLogData.clear();
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_TIPS,
                R.string.cx62_bt_dialog_sync_record_no_count,R.string.cx62_bt_dialog_sync_record_no_count));
    }

    @Override
    public void downloadCompleted(List<CallLogEntity> callLogData) {
        Logger.d(TAG, "downloadCompleted: 下载通话记录");
        refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_DOWNLOAD_COMPLETED_REFRESH_CONTACT_UI, callLogData));
    }

    @Override
    public void downloading(List<CallLogEntity> callLogData) {
//        Logger.d(TAG, "downloading: 下载通话记录");
        refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_DOWNLOADING_REFRESH_CONTACT_UI, callLogData));

    }

    @Override
    public void downloadingAfterDial(CallLogEntity data) {
//        refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CALLRECORDS_DOWNLOADING_DIAL_REFRESH_CONTACT_UI, data));
    }

    @Override
    public void contactsDownloadCompleted() {
        Logger.d(TAG, "contactsDownloadCompleted: 下载通话记录");
        refreshViewHandler.sendEmptyMessage(MSG_CALLRECORDS_START_DOWNLOAD_BACKGROUND);
    }

    @Override
    public void showNoneData() {
        refreshViewHandler.sendEmptyMessage(NONE_DEVICE_FOUND);
    }

    public void showListData() {
        if(callLogList.getVisibility() != View.VISIBLE){
            rlNoneCallLog.setVisibility(View.GONE);
            callLogList.setVisibility(View.VISIBLE);
        }
    }
    private void showTips(int stringID){
        rlNoneCallLog.setVisibility(View.VISIBLE);
        callLogList.setVisibility(View.GONE);
        tvNoCallRecords.setText(stringID);
        if(stringID == R.string.cx62_bt_syncing_call_logs
                || stringID == R.string.cx62_bt_syncing_contacts
                || stringID == R.string.cx62_bt_dialog_sync_record_no_count){
            tvSyncFail.setVisibility(View.GONE);
            tvSyncManual.setVisibility(View.GONE);
        }else{
            tvSyncFail.setVisibility(View.GONE);
            tvSyncManual.setVisibility(View.VISIBLE);
        }
    }


    private class MyListViewItem implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView tvNumber = view.findViewById(R.id.tvCallLogNumber);
            TextView tvName = view.findViewById(R.id.tvCallLogName);
            String number = (String) tvNumber.getText();
            String name = (String) tvName.getText();
            mPresenter.reqHfpDialCall(number);
            btStatusModel.setCallName(name);
            btStatusModel.setCallNumber(number);
        }
    }


    private static class RefreshViewHandler extends Handler {

        WeakReference<BtCallRecordsFragment> reference;

        RefreshViewHandler(BtCallRecordsFragment reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            BtCallRecordsFragment presenter = reference.get();
            switch (msg.what) {
                case SHOW_DEVICE_FOUND:
                    presenter.showListData();
                    break;
                case NONE_DEVICE_FOUND:
                    presenter.showTips(R.string.cx62_bt_no_call_log);
                    break;
                case MSG_CALLRECORDS_DOWNLOADING_REFRESH_CONTACT_UI:
//                    Logger.d(TAG, "----MSG_CALLRECORDS_DOWNLOADING_REFRESH_CONTACT_UI: ");
                    presenter.callLogData.clear();
                    presenter.callLogData.addAll((List<CallLogEntity>) msg.obj);
                    presenter.callLogListItemAdapter.setList(presenter.callLogData);
                    break;
                case MSG_CALLRECORDS_DOWNLOAD_COMPLETED_REFRESH_CONTACT_UI:
                    Logger.d(TAG, "----MSG_CALLRECORDS_DOWNLOAD_COMPLETED_REFRESH_CONTACT_UI: ");
                    presenter.callLogData.clear();
                    presenter.callLogData.addAll((List<CallLogEntity>) msg.obj);
                    presenter.callLogListItemAdapter.setList(presenter.callLogData);
                    ((List<CallLogEntity>) msg.obj).clear();
                    break;
                case MSG_CALLRECORDS_DOWNLOADING_DIAL_REFRESH_CONTACT_UI:
                    Logger.d(TAG, "----MSG_CALLRECORDS_DOWNLOADING_DIAL_REFRESH_CONTACT_UI: ");
                    if(presenter.callLogData == null || presenter.callLogData.isEmpty()){
                        return;
                    }
                    Logger.d(TAG, "----callLogData: "+presenter.callLogData.size());
                    presenter.callLogData.add(0,(CallLogEntity) msg.obj);
                    presenter.callLogListItemAdapter.setList(presenter.callLogData);
                    break;
                case MSG_CALLRECORDS_START_DOWNLOAD_BACKGROUND:
                    presenter.showTips(R.string.cx62_bt_syncing_call_logs);
                    presenter.downloadBackground();
                    break;
                case MSG_CALLRECORDS_DATA_CHANGE:
                    Logger.d(TAG, "----MSG_CALLRECORDS_DATA_CHANGE: ");
                    presenter.callLogData.addAll((List<CallLogEntity>) msg.obj);
                    presenter.callLogListItemAdapter.setList(presenter.callLogData);
                    if (presenter.callLogData.size() < presenter.total) {
                        presenter.syncQuery(presenter.callLogData.size());
                    }
                    break;
                case MSG_CALLRECORDS_TIPS:
                    presenter.showTips(msg.arg1);
                    break;
            }
        }
    }


}
