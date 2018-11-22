package com.semisky.bluetoothproject.model.modelInterface;

/**
 * Created by chenhongrui on 2017/8/24
 * <p>
 * 内容摘要：监听蓝牙焦点丢失
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface OnBtAudioEventListener {
    //丢失焦点
    void onAudioLess();

    //重获焦点
    void onAudioRequest();

    //暂时失去焦点
    void onAudioLossTransient();
}
