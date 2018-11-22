package com.semisky.bluetoothproject.presenter.viewInterface;

/**
 * Created by chenhongrui on 2018/9/3
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BTContactAndRecordInterface {
    /**
     * 通讯录下载失败
     */
    void contactDownloadFail();

    /**
     * 通讯录开始下载
     */
    void contactDownloadStart();

    /**
     * 通讯录下载完成
     */
    void contactDownloadCompleted();

    /**
     * 通话记录下载失败
     */
    void callRecordDownloadFail();

    /**
     * 通话记录开始下载
     */
    void callRecordDownloadStart();

    /**
     * 通话记录下载完成
     */
    void callRecordDownloadCompleted();

    /**
     * 下载通讯录和通话记录，通讯录开始下载完成
     */
    void callAllForContactStart();

    /**
     * 下载通讯录和通话记录，通讯录下载完成
     */
    void callAllForContactCompleted();

    /**
     * 下载通讯录和通话记录，通话记录开始下载完成
     */
    void callAllForRecordStart();

    /**
     * 下载通讯录和通话记录，通话记录下载完成
     */
    void callAllForRecordCompleted();

    /**
     * 获取权限成功
     */
    void callAccessPermissions();
}
