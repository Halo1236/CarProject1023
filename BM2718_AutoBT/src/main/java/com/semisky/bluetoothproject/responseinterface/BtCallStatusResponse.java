package com.semisky.bluetoothproject.responseinterface;

import com.nforetek.bt.aidl.NfHfpClientCall;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtCallStatusResponse {

    /**
     * 蓝牙电话call回调
     *
     * @param address address
     * @param call    call
     */
    void onHfpCallChanged(String address, NfHfpClientCall call);

}
