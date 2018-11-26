package com.semisky.bluetoothproject.model;

import android.content.Context;
import android.provider.CallLog;
import android.util.Log;

import com.nforetek.bt.aidl.NfPbapContact;
import com.nforetek.bt.res.NfDef;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.entity.CallLogEntity;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.presenter.viewInterface.BTContactAndRecordInterface;
import com.semisky.bluetoothproject.presenter.viewInterface.CallRecordsInterface;
import com.semisky.bluetoothproject.presenter.viewInterface.ContactsInterface;
import com.semisky.bluetoothproject.responseinterface.BtDownloadResponse;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.DateUtils;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.utils.PinYinUtil;

import org.litepal.LitePal;
import org.litepal.crud.callback.UpdateOrDeleteCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要: 处理联系人以及通话记录Model
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtContactsDownloadModel implements BtDownloadResponse {

    private static final String TAG = Logger.makeTagLog(BtContactsDownloadModel.class);
    private Context context;

    private List<ContactsEntity> contactsData = new ArrayList<ContactsEntity>();
    private List<CallLogEntity> callLogData = new ArrayList<>();

    private BtStatusModel btStatusModel;
    private BTContactAndRecordInterface btContactAndRecordInterface;

    private BtContactsDownloadModel() {
        btStatusModel = BtStatusModel.getInstance();
        this.context = BtApplication.getContext();
    }

    public void setbtContactAndRecord(BTContactAndRecordInterface btContactAndRecord) {
        this.btContactAndRecordInterface = btContactAndRecord;
    }

    private static class BtContactsModelHolder {
        private static final BtContactsDownloadModel instance = new BtContactsDownloadModel();
    }

    public static BtContactsDownloadModel getInstance() {
        return BtContactsModelHolder.instance;
    }

    private ContactsInterface contactsInterface;

    public void setContactsInterface(ContactsInterface contactsInterface) {
        Logger.d(TAG, "setContactsInterface: ");
        this.contactsInterface = contactsInterface;
        if(null != contactsInterface && !contactsData.isEmpty()){
            Logger.d(TAG, "contactsData: "+contactsData.size());
            if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.ALL_IN_CONTACT
                    || btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT
                    || btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT_LOADING_WAIT_RECORD
                    ){
                contactsInterface.onDownloading(contactsData);
            }
        }

    }

    public void unSetContactsInterface() {
        this.contactsInterface = null;
    }

    private void showContactNoneData() {
        if (contactsInterface != null) {
            contactsInterface.showNoneData();
        }
    }

    private CallRecordsInterface callRecordsInterface;

    public void setCallRecordsInterface(CallRecordsInterface callRecordsInterface) {
        Logger.d(TAG, "setCallRecordsInterface: ");
        this.callRecordsInterface = callRecordsInterface;
        if(null != callRecordsInterface && !callLogData.isEmpty()){
            Logger.d(TAG, "callLogData: "+callLogData.size());
            if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.ALL_IN_RECORD
                    || btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.RECORD
                    || btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.RECORD_LOADING_WAIT_CONTACT
                    ){
                callRecordsInterface.downloading(callLogData);
            }

        }

    }

    public void unSetCallRecordsInterface() {
        this.callRecordsInterface = null;
    }

    private void showCallRecordsNoneData() {
        if (callRecordsInterface != null) {
            callRecordsInterface.showNoneData();
        }
    }

    @Override
    public void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts) {
        Logger.d(TAG, "onPbapStateChanged:prevState " + prevState + " newState " + newState
                + " reason " + reason+ " counts " + counts);
        if((reason == NfDef.REASON_DOWNLOAD_FULL_CONTENT_COMPLETED || reason == NfDef.REASON_DOWNLOAD_USER_REJECT)
                && counts == 0){
            Logger.d(TAG, "onPbapStateChanged:getSyncForStatus "+btStatusModel.getSyncForStatus());
            switch(btStatusModel.getSyncForStatus()){
                case ALL_IN_CONTACT:
                    if (contactsInterface != null) {
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        contactsInterface.onDownloadFailed();

                    }
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged: 联系人同步失败 ");
                        btContactAndRecordInterface.callAllForContactCompleted();
                    }
                    break;
                case CONTACT:
                case CONTACT_LOADING_WAIT_RECORD:
                    if (contactsInterface != null) {
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        contactsInterface.onDownloadFailed();

                    }
                    break;

                case RECORD:
                case ALL_IN_RECORD:
                case RECORD_LOADING_WAIT_CONTACT:
                    if (callRecordsInterface != null) {
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        callRecordsInterface.downloadFailed();
                    }
                    break;

                case PERMISSION:
                    if (callRecordsInterface != null) {
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        callRecordsInterface.downloadFailed();
                    }
                    if (contactsInterface != null) {
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        contactsInterface.onDownloadFailed();

                    }
                    break;
            }
            return;

        }
        if (newState == NfDef.STATE_DOWNLOAD_COMPLETED && reason == NfDef.REASON_DOWNLOAD_FULL_CONTENT_COMPLETED) {
            //同步完成
            switch (btStatusModel.getSyncForStatus()) {
                case ONCE_RECORD_CONTACT:
                    saveOnceRecord();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:RECORD更新最新通话记录同步完成 ");
                        btContactAndRecordInterface.callRecordDownloadCompleted();
                    }
                    break;
                case ALL_IN_CONTACT:
                    saveContactData();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:ALL_IN_CONTACT联系人同步完成 ");
                        btContactAndRecordInterface.callAllForContactCompleted();
                        BtSPUtil.getInstance().setSyncContactsStateSP(context, true);
                    }
                    break;
                case ALL_IN_RECORD:
                    saveRecordData();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:ALL_IN_RECORD通话记录同步完成 ");
                        btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                        btContactAndRecordInterface.callAllForRecordCompleted();
                        BtSPUtil.getInstance().setSyncCallLogStateSP(context, true);
                    }
                    break;
                case RECORD:
                case RECORD_LOADING_WAIT_CONTACT:
                    saveRecordData();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:RECORD通话记录同步完成 ");
                        btContactAndRecordInterface.callRecordDownloadCompleted();
                        BtSPUtil.getInstance().setSyncCallLogStateSP(context, true);
                    }
                    break;
                case CONTACT:
                case CONTACT_LOADING_WAIT_RECORD:
                    saveContactData();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:CONTACT联系人同步完成 ");
                        btContactAndRecordInterface.contactDownloadCompleted();
                        BtSPUtil.getInstance().setSyncContactsStateSP(context, true);
                    }
                    break;
                case ONCE_RECORD:
                    saveOnceRecord();
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged:RECORD更新最新通话记录同步完成 ");
                        btContactAndRecordInterface.callRecordDownloadCompleted();
                    }
                    break;
                case PERMISSION:
                    //如果手机端同意授权
                    if (btContactAndRecordInterface != null) {
                        btContactAndRecordInterface.callAccessPermissions();
                    }
                    break;
                default:

                    break;
            }

        } else if (prevState == NfDef.STATE_DOWNLOAD_COMPLETED && newState == NfDef.STATE_DOWNLOADING) {
            //开始同步
            switch (btStatusModel.getSyncForStatus()) {
                case ALL_IN_CONTACT:
                    startDownload();
                    if (btContactAndRecordInterface != null) {
                        btContactAndRecordInterface.callAllForContactStart();
                    }
                    break;
                case ALL_IN_RECORD:
                    startDownload();
                    if (btContactAndRecordInterface != null) {
                        btContactAndRecordInterface.callAllForRecordStart();
                    }
                    break;
                case RECORD:
                    startDownload();
                    if (btContactAndRecordInterface != null) {
                        btContactAndRecordInterface.callRecordDownloadStart();
                    }
                    break;
                case CONTACT:
                    startDownload();
                    if (btContactAndRecordInterface != null) {
                        btContactAndRecordInterface.contactDownloadStart();
                    }
                    break;
                case PERMISSION:

                    break;

                default:

                    break;
            }

            Logger.d(TAG, "onPbapStateChanged:同步开始---- "+btStatusModel.getSyncForStatus());
        } else if ((newState == NfDef.STATE_READY && reason > NfDef.REASON_DOWNLOAD_FULL_CONTENT_COMPLETED)
                || (newState == NfDef.STATE_READY && reason == NfDef.ERROR)) {
            Logger.d(TAG, "onPbapStateChanged 同步失败:"+btStatusModel.getSyncForStatus());
            switch (btStatusModel.getSyncForStatus()) {
                case CONTACT:
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged: contact同步失败");
                        btContactAndRecordInterface.contactDownloadFail();
                        showContactNoneData();
                        BtSPUtil.getInstance().setSyncContactsStateSP(context, false);
                    }
                    break;
                case RECORD:
                    if (btContactAndRecordInterface != null) {
                        Logger.d(TAG, "onPbapStateChanged: callRecord同步失败");
                        btContactAndRecordInterface.callRecordDownloadFail();
                        showCallRecordsNoneData();
                        BtSPUtil.getInstance().setSyncCallLogStateSP(context, false);
                    }
                    break;
            }
        }
    }
    /***
     * 开始下载清空上次数据
     */
    private void startDownload(){
        Logger.d(TAG, "startDownload:callLogData.size() " + callLogData.size());
        if(contactsInterface != null ){
            switch (btStatusModel.getSyncForStatus()) {
                case ALL_IN_CONTACT:
                case CONTACT:
                    contactsData.clear();
                    contactsInterface.onDownloadStart();
                    break;
            }

        }
        if(callRecordsInterface != null){
            switch (btStatusModel.getSyncForStatus()) {
                case ALL_IN_RECORD:
                case RECORD:
                    callLogData.clear();
                    callRecordsInterface.onDownloadStart();
                    break;
            }
            callRecordsInterface.onDownloadStart();
        }
    }

    /**
     * 保存最新的通话记录
     */
    private void saveOnceRecord() {
        if (callLogData != null && callLogData.size() > 0) {
            int count = LitePal.count(CallLogEntity.class);
            if (count == 0) {
                //存储数据库
                LitePal.saveAll(callLogData);
            } else {
                CallLogEntity callLogEntity = callLogData.get(0);
                callLogEntity.save();
            }

//            if (callRecordsInterface != null) {
//                callRecordsInterface.downloadCompleted(callLogData);
//            }
        }
    }

    /**
     * 保存通话记录
     * 先下载联系人，再下载通话记录
     */
    private void saveRecordData() {
        if (callLogData != null && callLogData.size() > 0) {
            Logger.d(TAG, "onPbapStateChanged:callLogData.size() " + callLogData.size());
            int count = LitePal.count(CallLogEntity.class);
            Logger.d(TAG, "onPbapStateChanged: count " + count);
            if (count == 0) {
                //异步存储数据库
                LitePal.saveAll(callLogData);
                count = LitePal.count(CallLogEntity.class);
                Logger.d(TAG, "callLogData: count1 " + count);
                recordDownloadCompleted();
            } else {
                //异步删除数据库表数据再存储
                LitePal.deleteAllAsync(CallLogEntity.class).listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        Logger.d(TAG, "onPbapStateChanged:callLogData2.size() " + callLogData.size());
                        LitePal.saveAll(callLogData);
                        int count = LitePal.count(CallLogEntity.class);
                        Logger.d(TAG, "callLogData: count2 " + count);
                        recordDownloadCompleted();
                    }
                });

            }

        }

        BtSPUtil.getInstance().setSyncCallLogStateSP(context, true);
    }
    private void recordDownloadCompleted(){
        Logger.d(TAG, "saveRecordData: callRecordsInterface " + callRecordsInterface);
        Logger.d(TAG, "saveRecordData: contactsInterface " + contactsInterface);
        Logger.d(TAG, "saveRecordData: getSyncForStatus " + btStatusModel.getSyncForStatus());
        if(contactsInterface != null && btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.RECORD_LOADING_WAIT_CONTACT){
            contactsInterface.onRecordDownloadCompleted();
        }
        if (callRecordsInterface != null) {
            Logger.d(TAG, "saveRecordData: downloadCompleted ");
            if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.RECORD
                    || btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.ALL_IN_RECORD){
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
            }
            callRecordsInterface.downloadCompleted(callLogData);
        }
    }

    /**
     * 保存联系人下载数据
     */
    private void saveContactData() {
        Logger.d(TAG, "saveContactData: contactsData " + contactsData);
        if (contactsData != null && contactsData.size() > 0) {
            PinYinUtil.listSort(contactsData);
            Logger.d(TAG, "saveContactData:contactsData.size() " + contactsData.size());
            int count = LitePal.count(ContactsEntity.class);
            Logger.d(TAG, "saveContactData: count " + count);
            if (count == 0) {
                //存储数据库
//                LitePal.saveAllAsync(contactsData);
                LitePal.saveAll(contactsData);
                count = LitePal.count(ContactsEntity.class);
                Logger.d(TAG, "saveContactData: count " + count);
                contactsDownloadCompleted();
            } else {
                //删除数据库表数据
//                LitePal.deleteAll();
                LitePal.deleteAllAsync(ContactsEntity.class).listen(new UpdateOrDeleteCallback() {
                    @Override
                    public void onFinish(int rowsAffected) {
                        LitePal.saveAll(contactsData);
                        contactsDownloadCompleted();
                    }
                });

            }

        }

        BtSPUtil.getInstance().setSyncContactsStateSP(context, true);
    }
    private void contactsDownloadCompleted(){
        Logger.d(TAG, "saveContactData: contactsInterface " + contactsInterface);
        Logger.d(TAG, "saveContactData: callRecordsInterface " + callRecordsInterface);
        Logger.d(TAG, "saveContactData: getSyncForStatus " + btStatusModel.getSyncForStatus());
        if (callRecordsInterface != null && btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT_LOADING_WAIT_RECORD) {
            callRecordsInterface.contactsDownloadCompleted();
        }
        if (contactsInterface != null) {
            if(btStatusModel.getSyncForStatus() == BtStatusModel.syncStatus.CONTACT){
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
            }
            Logger.d(TAG, "saveContactData: onDownloadCompleted ");
            contactsInterface.onDownloadCompleted(contactsData);
        }
    }

    @Override
    public void retPbapDownloadedCallLog(String address, String firstName, String middleName, String lastName, String number, int type, String timestamp) {
        Logger.d(TAG, "下载到通话记录" + firstName + middleName + lastName + ", number = " +
                number + ",  timeStamp" + timestamp + ", type =" + type);
        CallLogEntity callLogEntity = new CallLogEntity(address, firstName, middleName, lastName, number, timestamp);

        switch (type) {
            case NfDef.PBAP_STORAGE_MISSED_CALLS:
                callLogEntity.setType(CallLog.Calls.MISSED_TYPE);
                break;
            case NfDef.PBAP_STORAGE_RECEIVED_CALLS:
                callLogEntity.setType(CallLog.Calls.INCOMING_TYPE);
                break;
            case NfDef.PBAP_STORAGE_DIALED_CALLS:
                callLogEntity.setType(CallLog.Calls.OUTGOING_TYPE);
                break;
            default:
                callLogEntity.setType(CallLog.Calls.MISSED_TYPE);
                break;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd", BtStatusModel.getInstance().getLocalStats());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", BtStatusModel.getInstance().getLocalStats());
        String timeStamp = callLogEntity.getTimestamp();
        String[] dt = timeStamp.split("T");
        if (dt.length > 1) {
            Date callDate = DateUtils.stringToDate(dt[0], "yyyyMMdd");
            if (callDate != null) {
                //是否是当天日期
                if (dateFormat.format(callDate).equals(dateFormat.format(new Date()))) {
                    callLogEntity.setTimeIsToday(true);
                } else {
                    callLogEntity.setTimeIsToday(false);
                }
                callLogEntity.setCallData(dateFormat.format(callDate));
            }
        }

        if (dt.length > 1) {
            Date timeDate = DateUtils.stringToDate(dt[1], "HHmmss");
            if (timeDate != null && !"".equals(timeDate)) {
                callLogEntity.setCallTime(timeFormat.format(timeDate));
            }
        }

//        Log.d(TAG, "retPbapDownloadedCallLog: " + Long.parseLong(dt[0]) + Long.parseLong(dt[1]));
//        callLogEntity.setMyTimestamp(Long.parseLong(dt[0] + dt[1]));

        callLogData.add(callLogEntity);
        Log.d(TAG, "retPbapDownloadedCallLog---------getSyncForStatus： " + btStatusModel.getSyncForStatus());
        switch (btStatusModel.getSyncForStatus()) {
            case RECORD:
            case ALL_IN_RECORD:
            case RECORD_LOADING_WAIT_CONTACT:
                if (callRecordsInterface != null) {
                    callRecordsInterface.downloading(callLogData);
                }
                break;
            case ONCE_RECORD:
                btStatusModel.setSyncForStatus(BtStatusModel.syncStatus.NULL);
                if (callRecordsInterface != null) {
                    callRecordsInterface.downloadingAfterDial(callLogEntity);
                }
                break;
        }
    }

    @Override
    public void retPbapDownloadedContact(NfPbapContact nfPbapContact) {
        String fullName = nfPbapContact.getLastName() + nfPbapContact.getMiddleName() + nfPbapContact.getFirstName();
//        Logger.d(TAG, "retPbapDownloadedContact:下载到联系人 fullName " + fullName + " number " +
//                Arrays.toString(nfPbapContact.getNumberArray())+"----"+btStatusModel.getSyncForStatus());

        String[] numberArray = nfPbapContact.getNumberArray();

        if (numberArray != null && numberArray.length > 0) {
            for (String aNumberArray : numberArray) {
                ContactsEntity contactsEntity = new ContactsEntity();
                contactsEntity.setFirstName(nfPbapContact.getFirstName());
                contactsEntity.setMiddleName(nfPbapContact.getMiddleName());
                contactsEntity.setLastName(nfPbapContact.getLastName());
                contactsEntity.setFullName(fullName);
                contactsEntity.setNumber(aNumberArray);
                contactsEntity.setOrderASCII(PinYinUtil.pintinToASCII(fullName,aNumberArray));

                switch (btStatusModel.getSyncForStatus()) {
                    case CONTACT:
                    case ALL_IN_CONTACT:
                    case CONTACT_LOADING_WAIT_RECORD:
                        contactsData.add(contactsEntity);
                        if (contactsInterface != null) {
                            if(contactsData.size()%50 == 0){
                                PinYinUtil.listSort(contactsData);
                                contactsInterface.onDownloading(contactsData);
                            }

                        }
                    case PERMISSION:

                        break;
                }
            }

        }
    }

}
