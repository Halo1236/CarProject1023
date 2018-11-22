package com.semisky.bluetoothproject.presenter.viewInterface;

import com.semisky.bluetoothproject.entity.ContactsEntity;

import java.util.List;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface ContactsInterface {
    /**
     * 开始下载
     */
    void onDownloadStart();

    /**
     * 下载失败
     */
    void onDownloadFailed();
    /**
     * 下载完成
     */
    void onDownloadCompleted(List<ContactsEntity> contactsData);

    /**
     * 下载中
     */
    void onDownloading(List<ContactsEntity> contactsData);

    /**
     * 根据当前状态判断通话记录下载完成后是否通知开始下载联系人
     */
    void onRecordDownloadCompleted();

    void showNoneData();
}
