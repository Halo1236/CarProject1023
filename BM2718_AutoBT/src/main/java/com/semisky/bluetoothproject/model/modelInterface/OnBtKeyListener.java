package com.semisky.bluetoothproject.model.modelInterface;

/**
 * Created by chenhongrui on 2018/2/27
 * <p>
 * 内容摘要
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface OnBtKeyListener {

    /**
     * 接听电话
     */
    void connectCall();

    /**
     * 挂断电话
     */
    void hangupCall();

    /**
     * 上一首
     */
    void previousSong();

    /**
     * 下一首
     */
    void nextSong();
}
