// ISmkCallbackA2dp.aidl
package com.semisky.btcarkit.aidl;

// Declare any non-default types here with import statements

interface ISmkCallbackA2dp {
    void onA2dpStateChanged(int oldState,int newState);
}
