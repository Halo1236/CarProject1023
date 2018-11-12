package com.semisky.btcarkit.service.callbacks;

import com.semisky.btcarkit.aidl.BTHfpClientCall;

public interface IFscCallbackHfp {
    /**
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_CALL_STATE_OUTGOING_CALL}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_CALL_STATE_INCOMING_CALL}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_CALL_STATE_ACTIVE_CALL}
     *
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_STATE_UNSUPPORTED}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_STATE_STANDBY}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_STATE_CONNECTING}
     * <br>{@link com.semisky.btcarkit.constant.FscBTConst#HFP_STATE_CONNECTED}
     * @param address
     * @param oldState
     * @param newState
     */
    void onHfpStateChanged(String address, int oldState, int newState);

    void onHfpCallStateChanged(String address, BTHfpClientCall call);
}
