package com.semisky.btcarkit.view.fragment;

public interface IBTDialpadView {
    void onRefreshInputField(String number);
    void onRefreshHfpState(String hfpState);
    void onRefreshPhoneName(String phoneName);
    void onRefreshPhoneNumber(String phoneNumber);

}
