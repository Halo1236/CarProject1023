package com.semisky.bluetoothproject.responseinterface;

/**
 * Created by chenhongrui on 2018/8/16
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtSettingResponse {

    /**
     * 蓝牙连接状态回调
     *
     * @param address   address
     * @param prevState STATE_NOT_INITIALIZED</b> (int) 100
     *                  STATE_READY</b>			  (int) 110
     *                  STATE_CONNECTING</b>	  (int) 120
     *                  STATE_CONNECTED</b>		  (int) 140
     * @param newState
     */
    void onHfpStateChanged(String address, int prevState, int newState);
}
