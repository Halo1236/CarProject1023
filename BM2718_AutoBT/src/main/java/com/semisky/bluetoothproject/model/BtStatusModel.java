package com.semisky.bluetoothproject.model;

import android.util.Log;

import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.CallNameActive;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.Locale;

import static com.semisky.bluetoothproject.constant.BtConstant.CountryLocal.COUNTRY_CN;
import static com.semisky.bluetoothproject.constant.BtConstant.CountryLocal.COUNTRY_EN;
import static com.semisky.bluetoothproject.constant.BtConstant.CountryLocal.COUNTRY_SA;
import static com.semisky.bluetoothproject.constant.BtConstant.CountryLocal.COUNTRY_US;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要: 蓝牙基本参数
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtStatusModel {

    private static final String TAG = Logger.makeTagLog(BtStatusModel.class);

    private static BtStatusModel instance;

    private BtStatusModel() {

    }

    public static BtStatusModel getInstance() {
        if (instance == null) {
            synchronized (BtStatusModel.class) {
                if (instance == null) {
                    instance = new BtStatusModel();
                }
            }
        }
        return instance;
    }

    /**
     * 蓝牙是否打开
     */
    private boolean isBtOpen;

    /**
     * 当前连接的手机address
     */
    private String address;

    /**
     * 当前连接的手机的蓝牙名称
     */
    private String phoneName;

    /**
     * 当前拨打电话名字
     */
    private String callName;

    /**
     * 是否是第三方通话
     */
    private boolean isThirdParty;

    /**
     * 当前拨打电话号码
     */
    private String callNumber;

    /**
     * 当前是同步通讯录还是联系人
     */
    private syncStatus syncForStatus = syncStatus.NULL;

    /**
     * 是否是新的设备
     */
    private boolean isNewDevice;

    /**
     * 蓝牙是否连接
     */
    private boolean isBTConnect;

    /**
     * 当前电话状态
     */
    private BtConstant.CallStatus callStatus;

    /**
     * 是否正在通话中
     */
    private boolean isCallPhone;

    /**
     * 当前显示的fragment
     */
    private BtConstant.FragmentFlag fragmentFlag = BtConstant.FragmentFlag.KEYBOARD;

    /**
     * 导航是否在前台
     */
    private boolean isNaviView;

    /**
     * 是否处于ACC off状态
     */
    private boolean isAccOff;

    /**
     * ACCoN 是否需要恢复蓝牙音乐
     */
    private boolean isAccOnRecoverMusic;

    public boolean isAccOnRecoverMusic() {
        return isAccOnRecoverMusic;
    }

    public void setAccOnRecoverMusic(boolean accOnRecoverMusic) {
        isAccOnRecoverMusic = accOnRecoverMusic;
    }

    private CallNameActive firstCallInformation, secondCallInformation;

    public CallNameActive getFirstCallInformation() {
        return firstCallInformation;
    }

    public void setFirstCallInformation(CallNameActive firstCallInformation) {
        this.firstCallInformation = firstCallInformation;
    }

    public CallNameActive getSecondCallInformation() {
        return secondCallInformation;
    }

    public void setSecondCallInformation(CallNameActive secondCallInformation) {
        this.secondCallInformation = secondCallInformation;
    }

    public boolean isAccOff() {
        return isAccOff;
    }

    public void setAccOff(boolean accOff) {
        isAccOff = accOff;
    }

    public boolean isCallPhone() {
        return isCallPhone;
    }

    public void setCallPhone(boolean callPhone) {
        isCallPhone = callPhone;
    }

    public boolean isNaviView() {
        Log.d(TAG, "isNaviView: " + isNaviView);
        return isNaviView;
    }

    public void setNaviView(boolean naviView) {
        Log.d(TAG, "setNaviView: " + naviView);
        isNaviView = naviView;
    }

    public BtConstant.FragmentFlag getFragmentFlag() {
        return fragmentFlag;
    }

    public void setFragmentFlag(BtConstant.FragmentFlag fragmentFlag) {
        this.fragmentFlag = fragmentFlag;
    }

    public BtConstant.CallStatus getCallStatus() {
        return callStatus;
    }

    public boolean isThirdParty() {
        return isThirdParty;
    }

    public void setThirdParty(boolean thirdParty) {
        this.isThirdParty = thirdParty;
    }

    public void setCallStatus(BtConstant.CallStatus callStatus, String callNumber) {
        this.callNumber = callNumber;
        this.callStatus = callStatus;
    }

    public boolean isBTConnect() {
        Log.d(TAG, "isBTConnect: " + isBTConnect);
        return isBTConnect;
    }

    public void setBTConnect(boolean BTConnect) {
        isBTConnect = BTConnect;
    }

    public boolean isNewDevice() {
        return isNewDevice;
    }

    public void setNewDevice(boolean newDevice) {
        isNewDevice = newDevice;
    }

    public syncStatus getSyncForStatus() {
        return syncForStatus;
    }

    public void setSyncForStatus(syncStatus syncForStatus) {
        this.syncForStatus = syncForStatus;
    }

    public enum syncStatus {
        NULL,
        ALL_IN_CONTACT,//同步联系人and通话记录
        ALL_IN_RECORD,
        PERMISSION,//请求权限
        CONTACT,//联系人
        RECORD_LOADING_WAIT_CONTACT,//正在下载通话记录,并等待下载联系人
        RECORD,//通话记录
        CONTACT_LOADING_WAIT_RECORD,//正在下载联系人,并等待下载通话记录
        ONCE_RECORD_CONTACT,//正在下载联系人的时候接听电话
        ONCE_RECORD//更新最近一条通话记录
    }

    /**
     * 当前国家选择
     */
    private int CountryStats = 1;

    public Locale getLocalStats() {
        switch (getCountryStats()) {
            case COUNTRY_CN:
                return Locale.CHINESE;
            case COUNTRY_EN:
                return Locale.ENGLISH;
            case COUNTRY_US:
                return Locale.US;
            case COUNTRY_SA:
                return Locale.CANADA;
            default:
                return Locale.CHINESE;
        }
    }

    public int getCountryStats() {
        return CountryStats;
    }

    public void setCountryStats(int countryStats) {
        CountryStats = countryStats;
    }

    public boolean isBtOpen() {
        return isBtOpen;
    }

    public void setBtOpen(boolean btOpen) {
        isBtOpen = btOpen;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneName() {
        return phoneName;
    }

    public void setPhoneName(String phoneName) {
        this.phoneName = phoneName;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public String getCallNumber() {
        return callNumber;
    }

    public void setCallNumber(String callNumber) {
        this.callNumber = callNumber;
    }
}
