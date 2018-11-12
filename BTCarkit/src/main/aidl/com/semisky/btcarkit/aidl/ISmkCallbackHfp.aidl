// ISmkCallbackHfp.aidl
package com.semisky.btcarkit.aidl;
import com.semisky.btcarkit.aidl.BTHfpClientCall;

// Declare any non-default types here with import statements

interface ISmkCallbackHfp {
        /**
        *   <br>{ BTConst#HFP_STATE_NOT_INITIALIZED}
        *   <br>{ BTConst#HFP_STATE_READY}
        *   <br>{ BTConst#HFP_STATE_CONNECTING}
        *   <br>{ BTConst#HFP_STATE_CONNECTED}
        * */
        void onHfpStateChanged(String address, int oldState, int newState);
        void onHfpCallStateChanged(String address,in BTHfpClientCall call);
}
