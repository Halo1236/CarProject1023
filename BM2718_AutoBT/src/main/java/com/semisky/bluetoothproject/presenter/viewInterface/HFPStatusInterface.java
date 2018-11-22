package com.semisky.bluetoothproject.presenter.viewInterface;

/**
 * Created by chenhongrui on 2018/8/8
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface HFPStatusInterface {

    /**
     * 断开中
     */
    void stateDisconnecting(String address);

    /**
     * 已连接
     */
    void stateConnected(String address);

    /**
     * 连接中
     */
    void stateConnecting(String address);

    /**
     * 已断开
     */
    void stateDisconnect(String address);
}
