package com.semisky.bluetoothproject.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by chenhongrui on 2018/8/13
 * <p>
 * 内容摘要: 通话记录
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CallLogEntity extends LitePalSupport {

    /**
     * 数据库表ID
     */
    private int id;

    /**
     * Bluetooth MAC address of remote device.
     */
    private String address;

    /**
     * means first name of this contact.
     */
    private String firstName;

    /**
     * contact middle name.
     */
    private String middleName;

    /**
     * contact last name.
     */
    private String lastName;

    /**
     * number mean the number of this call log.
     */
    private String number;

    /**
     * type possible storage type are:
     * <br><b>PBAP_STORAGE_MISSED_CALLS</b>		(int) 5
     * <br><b>PBAP_STORAGE_RECEIVED_CALLS</b>		(int) 6
     * <br><b>PBAP_STORAGE_DIALED_CALLS</b>		(int) 7
     * <br><b>PBAP_STORAGE_CALL_LOGS</b>			(int) 8
     */
    private int type;

    /**
     * call log timestamp ex: 20101010T101010Z means 2015/10/10 10:10:10
     */
    private String timestamp;

    /**
     * 时间戳
     */
    private long myTimestamp;

    /**
     * time is today
     */
    private boolean timeIsToday;

    /**
     * 通话记录时分秒
     */
    private String callTime;

    /**
     * 通话记录年月
     */
    private String callData;

    public CallLogEntity(String address, String firstName, String middleName, String lastName, String number, String timestamp) {
        this.address = address;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.number = number;
        this.timestamp = timestamp;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTimeIsToday() {
        return timeIsToday;
    }

    public void setTimeIsToday(boolean timeIsToday) {
        this.timeIsToday = timeIsToday;
    }

    public String getCallTime() {
        return callTime;
    }

    public void setCallTime(String callTime) {
        this.callTime = callTime;
    }

    public String getCallData() {
        return callData;
    }

    public void setCallData(String callData) {
        this.callData = callData;
    }

    public long getMyTimestamp() {
        return myTimestamp;
    }

    public void setMyTimestamp(long myTimestamp) {
        this.myTimestamp = myTimestamp;
    }
}
