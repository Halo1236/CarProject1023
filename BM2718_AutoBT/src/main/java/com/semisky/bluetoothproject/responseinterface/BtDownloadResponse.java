package com.semisky.bluetoothproject.responseinterface;

import com.nforetek.bt.aidl.NfPbapContact;

/**
 * Created by chenhongrui on 2018/8/29
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public interface BtDownloadResponse {

    /**
     * 下载状态回调
     *
     * @param address
     * @param prevState
     * @param newState
     * @param reason    <p><blockquote><b>REASON_DOWNLOAD_FULL_CONTENT_COMPLETED</b>	(int) 1
     *                  <br><b>REASON_DOWNLOAD_FAILED</b>								(int) 2
     *                  <br><b>REASON_DOWNLOAD_TIMEOUT</b>								(int) 3
     *                  <br><b>REASON_DOWNLOAD_USER_REJECT</b>							(int) 4</blockquote>
     * @param counts
     */
    void onPbapStateChanged(String address, int prevState, int newState, int reason, int counts);

    /**
     * 通话记录数据
     * @param contact
     */
    void retPbapDownloadedContact(NfPbapContact contact);

    /**
     * 通讯录数据
     *
     * @param address    Bluetooth MAC address of remote device.
     * @param firstName  means first name of this contact.
     * @param middleName contact middle name.
     * @param lastName   contact last name.
     * @param number     number mean the number of this call log.
     * @param type       possible storage type are:
     *                   <br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
     *                   <br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
     *                   <br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
     *                   <br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8</blockquote>
     * @param timestamp  call log timestamp ex: 20101010T101010Z means 2015/10/10 10:10:10
     */
    void retPbapDownloadedCallLog(String address, String firstName, String middleName,
                                  String lastName, String number, int type, String timestamp);
}
