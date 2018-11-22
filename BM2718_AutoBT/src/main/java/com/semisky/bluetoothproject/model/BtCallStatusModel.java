package com.semisky.bluetoothproject.model;

import android.content.Context;
import android.util.Log;

import com.nforetek.bt.aidl.NfHfpClientCall;
import com.semisky.autoservice.manager.AutoManager;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.application.BtApplication;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.CallNameActive;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.modelInterface.OnCallStatusListener;
import com.semisky.bluetoothproject.presenter.BtBaseUiCommandMethod;
import com.semisky.bluetoothproject.responseinterface.BtCallStatusResponse;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;

import org.litepal.LitePal;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtCallStatusModel implements BtCallStatusResponse {

    private static final String TAG = "BtCallStatusModel";

    private BtBaseUiCommandMethod btBaseUiCommandMethod;
    private Context context;
    private AutoManager autoManager;
    private BtStatusModel btStatusModel;

    private BtCallStatusModel() {
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
        context = BtApplication.getContext();
        autoManager = AutoManager.getInstance();
        btStatusModel = BtStatusModel.getInstance();
    }

    public static BtCallStatusModel getInstance() {
        return BtCallStatusModelHolder.INSTANCE;
    }

    private static class BtCallStatusModelHolder {
        private static final BtCallStatusModel INSTANCE = new BtCallStatusModel();
    }

    private OnCallStatusListener statusListener;

    public void setOnCallStatusListener(OnCallStatusListener statusListener) {
        this.statusListener = statusListener;
    }

    @Override
    public void onHfpCallChanged(String address, NfHfpClientCall call) {
        Log.d(TAG, "onHfpCallChanged: " + address + " NfHfpClientCall " + call);
        int state = call.getState();
        int id = call.getId();
        String number = call.getNumber();

        switch (state) {
            case NfHfpClientCall.CALL_STATE_HELD://保留状态
                Log.d(TAG, "onHfpCallChanged: 保留状态 " + number);
                break;
            case NfHfpClientCall.CALL_STATE_INCOMING://来电
                Log.d(TAG, "onHfpCallChanged: 来电 " + number);
                setBTStatus(BtConstant.CallStatus.INCOMING, number);
                getCallInformation(call);
                // SHOW DIALOG
                btStatusModel.setCallPhone(true);
                if (statusListener != null) {
                    applyAudioFocus();
                    statusListener.callStatusIncomming(id);
                }
                break;
            case NfHfpClientCall.CALL_STATE_WAITING://第三方来电
                Log.d(TAG, "onHfpCallChanged:第三方来电 " + number);
                break;
            case NfHfpClientCall.CALL_STATE_DIALING://去电
                Log.d(TAG, "onHfpCallChanged:去电 " + number);
                setBTStatus(BtConstant.CallStatus.DIALING, number);
                getCallInformation(call);
                callDialing(id);
                break;
            case NfHfpClientCall.CALL_STATE_ALERTING://拨号
                Log.d(TAG, "onHfpCallChanged:拨号 " + number);
                setBTStatus(BtConstant.CallStatus.DIALING, number);
                callDialing(id);
                break;

            case NfHfpClientCall.CALL_STATE_ACTIVE://接通
                Log.d(TAG, "onHfpCallChanged:接通 " + number + " id " + id);
                btStatusModel.setCallPhone(true);
                showActive(call, id, number);
                break;

            case NfHfpClientCall.CALL_STATE_TERMINATED://挂断
                Log.d(TAG, "onHfpCallChanged:挂断 " + number);
                Log.d(TAG, "onHfpCallChanged:挂断前 " + callNameMap.size());

                if (callNameMap.containsKey(id)) {
                    callNameMap.remove(id);
                }

                Log.d(TAG, "onHfpCallChanged:挂断后 " + callNameMap.size());
                if (callNameMap.size() == 0) {
                    btStatusModel.setCallPhone(false);
                    BtMiddleSettingManager.getInstance().setCallTerminated();
                    setBTStatus(BtConstant.CallStatus.TERMINATED, number);
                    BtPhoneAudioFocusModel.getINSTANCE().abandonAudioFocus();
                    // SHOW DIALOG
                    if (statusListener != null) {
                        statusListener.callStatusTerminated();
                    }
                } else {
                    if (callNameMap.size() == 1) {
                        for (Integer integer : callNameMap.keySet()) {
                            statusListener.callStatusActive(integer);
                        }
                    }
                }
                break;
        }
    }

    /**
     * 1.显示通话中页面
     * 2.连接上蓝牙恢复通话页面
     */
    private void showActive(NfHfpClientCall call, int id, String number) {
        Logger.d(TAG, "showActive: ");
        setBTStatus(BtConstant.CallStatus.ACTIVE, number);
        getCallInformation(call);
        // SHOW DIALOG
        if (statusListener != null) {
            applyAudioFocus();
            statusListener.callStatusActive(id);
            if (id == secondActiveCall) {
                btStatusModel.setThirdParty(true);
            }
        }
    }

    private int secondActiveCall = 2;

    private Map<Integer, String> callNameMap = new TreeMap<>();

    private void getCallInformation(NfHfpClientCall call) {
        int id = call.getId();
        String number = call.getNumber();
        String fullName = queryNameForDatabase(number);

        int firstActiveCall = 1;
        secondActiveCall = 2;

        if (id == firstActiveCall) {
            CallNameActive callNameActive = new CallNameActive(id, fullName, number);
            btStatusModel.setFirstCallInformation(callNameActive);

            callNameMap.put(id, number);

            CallNameActive firstCallInformation = btStatusModel.getFirstCallInformation();
            Logger.d(TAG, "getCallInformation:获取第一个电话信息 id " + firstCallInformation.getId()
                    + " name: " + firstCallInformation.getName() + " number: " + firstCallInformation.getNumber());
        }

        if (id == secondActiveCall) {
            btStatusModel.setSecondCallInformation(new CallNameActive(id, fullName, number));

            callNameMap.put(id, number);

            CallNameActive secondCallInformation = btStatusModel.getSecondCallInformation();
            Logger.d(TAG, "getCallInformation:获取第二个电话信息 id " + secondCallInformation.getId()
                    + " name: " + secondCallInformation.getName() + " number: " + secondCallInformation.getNumber());
        }
    }

    /**
     * 来电去数据库查询联系人名字
     */
    private String queryNameForDatabase(String number) {
        boolean isSaveContact = BtSPUtil.getInstance().getSyncContactsStateSP(context);
        Logger.d(TAG, "queryNameForDatabase: number " + number);
        if (isSaveContact && number != null) {
            List<ContactsEntity> contactsEntities = LitePal.select("fullName", "number")
                    .where("number = ?", number).find(ContactsEntity.class);
            if (contactsEntities.size() > 0) {
                String fullName = contactsEntities.get(0).getFullName();
                Logger.d(TAG, "queryNameForDatabase:FullName " + fullName);
                return fullName;
            } else {
                Log.d(TAG, "queryNameForDatabase: query null");
                return context.getString(R.string.cx62_bt_unknown);
            }
        } else {
            Log.d(TAG, "queryNameForDatabase: 未同步联系人");
            return context.getString(R.string.cx62_bt_unknown);
        }
    }

    /**
     * 当前是否正在打电话
     */
    public void recoverCallView() {
        List<NfHfpClientCall> hfpCallList = btBaseUiCommandMethod.getHfpCallList();
        Log.d(TAG, "recoverCallView: " + hfpCallList.size());
        if (hfpCallList.size() > 0) {
            NfHfpClientCall nfHfpClientCall = hfpCallList.get(0);
            if (nfHfpClientCall.getState() == NfHfpClientCall.CALL_STATE_ACTIVE) {
                int id = nfHfpClientCall.getId();
                String number = nfHfpClientCall.getNumber();
                showActive(nfHfpClientCall, id, number);
                BtMiddleSettingManager.getInstance().setBtIncallState(NfHfpClientCall.CALL_STATE_ACTIVE);
            }
        }
    }

    private void setBTStatus(BtConstant.CallStatus callStatus, String number) {
        btStatusModel.setCallStatus(callStatus, number);
    }

    private void callDialing(int id) {
        // SHOW DIALOG
        btStatusModel.setCallPhone(true);
        if (statusListener != null) {
            applyAudioFocus();
            statusListener.callStatusDialing(id);
        }
    }

    private void applyAudioFocus() {
        BtPhoneAudioFocusModel.getINSTANCE().applyAudioFocus();
    }
}
