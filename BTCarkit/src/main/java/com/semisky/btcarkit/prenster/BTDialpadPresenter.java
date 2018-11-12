package com.semisky.btcarkit.prenster;

import android.os.RemoteException;

import com.semisky.btcarkit.aidl.BTConst;
import com.semisky.btcarkit.aidl.BTHfpClientCall;
import com.semisky.btcarkit.aidl.ISmkCallbackHfp;
import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.utils.Logutil;
import com.semisky.btcarkit.view.fragment.IBTDialpadView;

public class BTDialpadPresenter<V extends IBTDialpadView> extends BasePresenter<V> {
    private static final String TAG = Logutil.makeTagLog(BTDialpadPresenter.class);

    private String mInputPhoneNumber = "";

    // Constractor
    public BTDialpadPresenter() {
        BTRoutingCommandManager.getInstance().registerCallback(mOnServiceStateListener);
        BTRoutingCommandManager.getInstance().registerHfpCallback(mISmkCallbackHfp);
    }


    private BTRoutingCommandManager.OnServiceStateListener mOnServiceStateListener = new BTRoutingCommandManager.OnServiceStateListener() {
        @Override
        public void onServiceConnected() {
            BTRoutingCommandManager.getInstance().registerHfpCallback(mISmkCallbackHfp);
        }
    };

    private ISmkCallbackHfp.Stub mISmkCallbackHfp = new ISmkCallbackHfp.Stub() {
        @Override
        public void onHfpStateChanged(String address, int oldState, int newState) throws RemoteException {

            Logutil.i(TAG, "onHfpStateChanged() oldState = " + oldState + " , newState = " + newState);
            if (BTConst.HFP_CALL_STATE_INCOMING_CALL == newState) {// 来电
                refreshHfpState("来电");
            } else if (BTConst.HFP_CALL_STATE_OUTGOING_CALL == newState) {// 去电
                refreshHfpState("去电");
            } else if (BTConst.HFP_CALL_STATE_ACTIVE_CALL == newState) {// 接通电话
                refreshHfpState("接通电话");
            } else if (oldState > BTConst.HFP_CALL_STATE_INCOMING_CALL && newState < BTConst.HFP_CALL_STATE_INCOMING_CALL) {// 挂断 来电
                refreshHfpState("挂断电话");
            } else if (oldState > BTConst.HFP_CALL_STATE_OUTGOING_CALL && newState < BTConst.HFP_CALL_STATE_OUTGOING_CALL) {// 挂断 去电
                refreshHfpState("挂断电话");
            } else if (oldState > BTConst.HFP_STATE_READY && newState <= BTConst.HFP_STATE_READY) { // HFP 连接断开
                refreshHfpState("HFP 连接断开");
            } else if (oldState < BTConst.HFP_STATE_CONNECTED && newState == BTConst.HFP_STATE_CONNECTED) {// HFP 已连接
                refreshHfpState("HFP 已连接");
            } else if (oldState < BTConst.HFP_STATE_CONNECTING && newState == BTConst.HFP_STATE_CONNECTING) {// HFP 正在连接
                refreshHfpState("HFP 正在连接");
            }
        }

        @Override
        public void onHfpCallStateChanged(String address, BTHfpClientCall call) throws RemoteException {
            Logutil.i(TAG, "onHfpCallStateChanged()..");
            refreshPhoneCall(call.getmName(), call.getNumber());
        }
    };

    void refreshHfpState(final String state) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (isBindView()) {
                    mViewRef.get().onRefreshHfpState(state);
                }
            }
        });
    }

    void refreshPhoneCall(final String phoneName, final String phoneNumber) {
        _handler.post(new Runnable() {
            @Override
            public void run() {
                if (isBindView()) {
                    mViewRef.get().onRefreshPhoneName(null != phoneName ? phoneName : "null");
                    mViewRef.get().onRefreshPhoneNumber(null != phoneNumber ? phoneNumber : "null");
                }
            }
        });
    }

    public void inputPhoneNumber(String number) {
        mInputPhoneNumber += number;
        mViewRef.get().onRefreshInputField(mInputPhoneNumber);
    }

    public void removeAllInputPhoneNumber() {
        mInputPhoneNumber = "";
        mViewRef.get().onRefreshInputField(mInputPhoneNumber);
    }

    public void removeSinglePhoneNumber() {
        if (null != mInputPhoneNumber && mInputPhoneNumber.length() > 1) {
            mInputPhoneNumber = mInputPhoneNumber.substring(0, mInputPhoneNumber.length() - 1);
            mViewRef.get().onRefreshInputField(mInputPhoneNumber);
        } else {
            mInputPhoneNumber = "";
            if (isBindView()) {
                mViewRef.get().onRefreshInputField(mInputPhoneNumber);
            }
        }
    }

    /**
     * 打电话
     */
    public void reqDialCall() {
        if (isBindView()) {
            int hfpState = BTRoutingCommandManager.getInstance().getHfpState();
            Logutil.i(TAG, "reqDialCall() ...mInputPhoneNumber = " + mInputPhoneNumber + " , hfpState = " + hfpState);
            BTRoutingCommandManager.getInstance().reqHfpDialCall(mInputPhoneNumber);
        }
    }

    /**
     * 挂断当前电话
     */
    public void reqHfpTerminateCurrentCall() {
        if (isBindView()) {
            int hfpState = BTRoutingCommandManager.getInstance().getHfpState();
            Logutil.i(TAG, "reqHfpTerminateCurrentCall()  hfpState = " + hfpState);
            BTRoutingCommandManager.getInstance().reqHfpTerminateCurrentCall();
        }
    }

    @Override
    public void onDetachView() {
        BTRoutingCommandManager.getInstance().unregisterCallback(mOnServiceStateListener);
        BTRoutingCommandManager.getInstance().unregisterHfpCallback(mISmkCallbackHfp);
        super.onDetachView();
    }
}
