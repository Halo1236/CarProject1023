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
import com.semisky.bluetoothproject.adapter.ContactsListItemAdapter;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtStatusModel;
import com.semisky.bluetoothproject.presenter.BtTelephoneContactPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.ContactsInterface;
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
 * 内容摘要: 联系人fragment
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtTelephoneContactFragment extends BaseFragment<ContactsInterface, BtTelephoneContactPresenter>
        implements ContactsInterface {

    private static final String TAG = Logger.makeTagLog(BtTelephoneContactFragment.class);
    private static final int MSG_CONTACT_DOWNLOADING_REFRESH_CONTACT_UI = 0x000001;
    private static final int MSG_CONTACT_DOWNLOAD_CONPLETED_REFRESH_CONTACT_UI = 0x000002;
    private static final int MSG_CONTACT_START_DOWNLOAD_BACKGROUND = 0x000003;
    private static final int MSG_CONTACT_DATA_CHANGE = 0x000004;
    private final static int SHOW_DEVICE_FOUND = 0x000005;
    private final static int NONE_DEVICE_FOUND = 0x000006;
    private final static int MSG_CONTACT_TIPS = 0x000007;
    private ArrayList<ContactsEntity> contactsData = new ArrayList<>();
    private ContactsListItemAdapter contactsListItemAdapter;

    private BtMessageDialog btMessageDialog;
    private BtStatusModel btStatusModel;

    private RelativeLayout rlNoContacts;
    private TextView tvNoCallRecords,tvSyncFail,tvSyncManual;
    private ListView lvContactList;
    private RefreshViewHandler refreshViewHandler;
    private int total = 0;

    @Override
    protected BtTelephoneContactPresenter createPresenter() {
        return new BtTelephoneContactPresenter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Logger.d(TAG, "onHiddenChanged: ");
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
        Logger.d(TAG, "fragmentShow   -----contactsData: " + contactsData.size());
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_contact_fragment));
//        mPresenter.initListener();
        download();
    }

    @Override
    public void onResume() {
        super.onResume();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_contact_fragment));
    }

    @Override
    protected void loadData() {
        Logger.d(TAG, "loadData: ");
        download();
    }

    private void download() {
        Logger.d(TAG, "download: " + btStatusModel.getSyncForStatus());
        if(contactsData != null && !contactsData.isEmpty()){
            return;
        }
        Logger.d(TAG, "download----contactsData: " + contactsData.size());
        switch (btStatusModel.getSyncForStatus()){
            case ONCE_RECORD_CONTACT:
            case ONCE_RECORD:
                return;
            case RECORD_LOADING_WAIT_CONTACT:
                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
                        R.string.cx62_bt_syncing_contacts,R.string.cx62_bt_syncing_contacts));
                return;
            case ALL_IN_CONTACT:
//                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
//                        R.string.cx62_bt_syncing_contacts,R.string.cx62_bt_syncing_contacts));
            case CONTACT:
            case CONTACT_LOADING_WAIT_RECORD:
                if(contactsData.size() == 0){
                    refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
                            R.string.cx62_bt_syncing_contacts,R.string.cx62_bt_syncing_contacts));
                }
                return;
//            case RECORD:
//                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
//                        R.string.cx62_bt_syncing_contacts,R.string.cx62_bt_syncing_contacts));
//                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.RECORD_LOADING_WAIT_CONTACT);
//                return;
        }
        if (BtSPUtil.getInstance().getSyncContactsStateSP(getContext())) {
            if (null == contactsData || contactsData.isEmpty()) {
                total = LitePal.count(ContactsEntity.class);
                Logger.d(TAG, "----total:" + total);
                contactsData.clear();
                syncQuery(0);
            }


        } else {
            if(!BtSPUtil.getInstance().getAutoSyncBookStateSP(getContext())){
                Logger.d(TAG, "download: 下载联系人");
                startDownload();
            }

        }
    }

    private void syncQuery(int offset) {

        LitePal.limit(BtConstant.PBAP.PBAP_QUERY_COUNT).offset(offset).order("order asc").findAsync(ContactsEntity.class).listen(new FindMultiCallback() {
            @Override
            public <T> void onFinish(List<T> t) {
                refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
                refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_DATA_CHANGE, (List<ContactsEntity>) t));
            }
        });
    }

    @Override
    protected void initView(View view) {
        lvContactList = view.findViewById(R.id.lvContactList);
        rlNoContacts = view.findViewById(R.id.rlNoCallRecords);
        tvNoCallRecords = view.findViewById(R.id.tvNoCallRecords);
        tvSyncFail = view.findViewById(R.id.tvSyncFail);
        tvSyncManual = view.findViewById(R.id.tvSyncManual);
        contactsListItemAdapter = new ContactsListItemAdapter(getContext(), R.layout.item_call_contacts_view, contactsData);
        lvContactList.setAdapter(contactsListItemAdapter);
        btMessageDialog = new BtMessageDialog(getContext());
        btStatusModel = BtStatusModel.getInstance();
        lvContactList.setOnItemClickListener(new MyListViewItem());

        refreshViewHandler = new RefreshViewHandler(this);
        mPresenter.initListener();
    }

    @Override
    protected int getResourceId() {
        return R.layout.telephone_contact_fragment;
    }
    private boolean isConfirm = false;
    private void startDownload() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (btMessageDialog != null) {
                    btMessageDialog.initCancelConfirmButtonView(getString(R.string.cx62_bt_dialog_if_sync_contacts),
                            new BtMessageDialog.ClickConfirmListener() {
                                @Override
                                public void clickConfirm() {
                                    isConfirm = true;
                                    if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT){
                                        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
                                                R.string.cx62_bt_syncing_contacts,R.string.cx62_bt_syncing_contacts));
                                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.RECORD_LOADING_WAIT_CONTACT);
                                    }else{
                                        refreshViewHandler.sendEmptyMessage(MSG_CONTACT_START_DOWNLOAD_BACKGROUND);
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
                                    refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
                                            R.string.cx62_bt_no_contacts,R.string.cx62_bt_please_manual_sync));
                                }
                            });

                    btMessageDialog.show();
                }
            }
        });
    }

    private void downloadBackground() {
        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.CONTACT);
        mPresenter.downloadedConnect();
    }

    @Override
    public void onDownloadStart() {
        Logger.d(TAG, "onDownloadStart: " );
        contactsData.clear();
    }

    @Override
    public void onDownloadFailed() {
        Logger.d(TAG, "onDownloadFailed: " );
        contactsData.clear();
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_TIPS,
                R.string.cx62_bt_dialog_sync_no_count,R.string.cx62_bt_dialog_sync_no_count));
    }

    @Override
    public void onDownloadCompleted(List<ContactsEntity> contactsData) {
        Logger.d(TAG, "downloadCompleted: " + contactsData.size());
        refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
//        contactsData.removeAll(this.contactsData);
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_DOWNLOAD_CONPLETED_REFRESH_CONTACT_UI,contactsData ));
    }

    @Override
    public void onDownloading(List<ContactsEntity> contactsData) {
        Logger.d(TAG, "onDownloading: " + contactsData.size());
        refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
//        contactsData.removeAll(this.contactsData);
        refreshViewHandler.sendMessage(refreshViewHandler.obtainMessage(MSG_CONTACT_DOWNLOADING_REFRESH_CONTACT_UI, contactsData));
    }

    @Override
    public void onRecordDownloadCompleted() {
        Logger.d(TAG, "onRecordDownloadCompleted: " );
        refreshViewHandler.sendEmptyMessage(MSG_CONTACT_START_DOWNLOAD_BACKGROUND);
    }

    @Override
    public void showNoneData() {
        refreshViewHandler.sendEmptyMessage(NONE_DEVICE_FOUND);
    }

    private void showListData() {
        if(lvContactList.getVisibility() != View.VISIBLE){
            rlNoContacts.setVisibility(View.GONE);
            lvContactList.setVisibility(View.VISIBLE);
        }
    }

    private void showTips(int stringID){
        rlNoContacts.setVisibility(View.VISIBLE);
        lvContactList.setVisibility(View.GONE);
        tvNoCallRecords.setText(stringID);
        if(stringID == R.string.cx62_bt_syncing_call_logs
                || stringID == R.string.cx62_bt_syncing_contacts
                || stringID == R.string.cx62_bt_dialog_sync_no_count){
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
            TextView tvNumber = view.findViewById(R.id.tvCallContactNumber);
            TextView tvName = view.findViewById(R.id.tvCallContactName);
            String number = (String) tvNumber.getText();
            String name = (String) tvName.getText();
            mPresenter.reqHfpDialCall(number);
            btStatusModel.setCallName(name);
            btStatusModel.setCallNumber(number);
        }
    }


    private static class RefreshViewHandler extends Handler {

        WeakReference<BtTelephoneContactFragment> reference;

        RefreshViewHandler(BtTelephoneContactFragment reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            BtTelephoneContactFragment presenter = reference.get();
            switch (msg.what) {
                case MSG_CONTACT_DOWNLOADING_REFRESH_CONTACT_UI:
                    Logger.i(TAG,"-----------------MSG_CONTACT_DOWNLOADING_REFRESH_CONTACT_UI");
                    presenter.contactsData.clear();
                    presenter.contactsData.addAll((List<ContactsEntity>) msg.obj);
                    presenter.contactsListItemAdapter.setList(presenter.contactsData);
                    break;
                case MSG_CONTACT_DOWNLOAD_CONPLETED_REFRESH_CONTACT_UI:
                    Logger.i(TAG,"-----------------MSG_CONTACT_DOWNLOAD_CONPLETED_REFRESH_CONTACT_UI");
                    presenter.contactsData.clear();
                    presenter.contactsData.addAll((List<ContactsEntity>) msg.obj);
                    presenter.contactsListItemAdapter.setList(presenter.contactsData);
                    ((List<ContactsEntity>) msg.obj).clear();
                    break;
                case MSG_CONTACT_START_DOWNLOAD_BACKGROUND:
                    presenter.showTips(R.string.cx62_bt_syncing_contacts);
                    presenter.downloadBackground();
                    break;
                case MSG_CONTACT_DATA_CHANGE:
                    Logger.i(TAG,"-----------------MSG_CONTACT_DATA_CHANGE");
                    presenter.contactsData.addAll((List<ContactsEntity>) msg.obj);
                    presenter.contactsListItemAdapter.setList(presenter.contactsData);
                    if (presenter.contactsData.size() < presenter.total) {
                        presenter.syncQuery(presenter.contactsData.size());
                    }
                    break;
                case SHOW_DEVICE_FOUND:
                    presenter.showListData();
                    break;
                case NONE_DEVICE_FOUND:
                    presenter.showTips(R.string.cx62_bt_no_contacts);
                    break;
                case MSG_CONTACT_TIPS:
                    presenter.showTips(msg.arg1);
                    break;
            }
        }
    }

}
