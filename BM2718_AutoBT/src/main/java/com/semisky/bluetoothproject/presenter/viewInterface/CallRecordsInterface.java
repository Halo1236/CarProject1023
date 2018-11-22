package com.semisky.bluetoothproject.presenter.viewInterface;

import com.semisky.bluetoothproject.entity.CallLogEntity;

import java.util.List;

/**
 * Created by chenhongrui on 2018/8/10
 * <p>
 * 内容摘要: 下载通讯录
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface CallRecordsInterface {
    /**
     * 开始下载
     */
    void onDownloadStart();
    /**
     * 下载失败
     */
    void downloadFailed();

    /**
     * 下载完成
     */
    void downloadCompleted(List<CallLogEntity> callLogData);

    /**
     * 下载中
     */
    void downloading(List<CallLogEntity> callLogData);
    /**
     * 拨号后更新新的通话记录
     */
    void downloadingAfterDial(CallLogEntity callLogData);
    /**
     * 根据当前状态判断联系人下载完成后是否通知开始下载通话记录
     */
    void contactsDownloadCompleted();

    void showNoneData();

}
