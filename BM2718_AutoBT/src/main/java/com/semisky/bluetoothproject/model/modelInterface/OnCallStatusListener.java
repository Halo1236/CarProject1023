package com.semisky.bluetoothproject.model.modelInterface;

/**
 * Created by chenhongrui on 2018/10/19
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface OnCallStatusListener {
    //来电
    void callStatusIncomming(int id);

    //去电
    void callStatusDialing(int id);

    //正常拨通
    void callStatusActive(int id);

    //挂断
    void callStatusTerminated();

}
