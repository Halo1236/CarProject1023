package com.semisky.bluetoothproject.entity;

import org.litepal.crud.LitePalSupport;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要: 联系人
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class ContactsEntity extends LitePalSupport {

    /**
     * 数据库表ID
     */
    private int id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String fullName;

    private String number;
    private int order;

    public int getOrderASCII() {
        return order;
    }

    public void setOrderASCII(int order) {
        this.order = order;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
