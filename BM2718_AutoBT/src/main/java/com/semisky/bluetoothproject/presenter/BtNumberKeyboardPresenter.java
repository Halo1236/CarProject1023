package com.semisky.bluetoothproject.presenter;

import com.semisky.bluetoothproject.presenter.viewInterface.NumberKeyboardInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtNumberKeyboardPresenter extends BasePresenter<NumberKeyboardInterface> {

    private BtBaseUiCommandMethod btBaseUiCommandMethod;

    public BtNumberKeyboardPresenter() {
        btBaseUiCommandMethod = BtBaseUiCommandMethod.getInstance();
    }

    public void reqHfpDialCall(String number){
        btBaseUiCommandMethod.reqHfpDialCall(number);
    }
}
