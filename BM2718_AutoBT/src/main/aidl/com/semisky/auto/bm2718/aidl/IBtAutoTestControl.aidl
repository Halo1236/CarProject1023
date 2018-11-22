// IBtAutoTestControl.aidl
package com.semisky.auto.bm2718.aidl;

// Declare any non-default types here with import statements

interface IBtAutoTestControl {

    void musicLast();

    void musicNext();

    void musicPlay();

    void musicPause();

    void callAnswer();

    void callHangup();

    void callPhone(String number);

    String getBTAddress();
}
