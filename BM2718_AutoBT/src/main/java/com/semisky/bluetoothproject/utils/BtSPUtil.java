package com.semisky.bluetoothproject.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.entity.DevicesListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名称 :   bluetooth
 * 创建人  :   betta
 * 日期    :   2018/1/4 0004
 * 描述   :   SharedPreferences管理类
 */

public class BtSPUtil {

    private final String TAG = getClass().getSimpleName();

    private static final String SHARE_FILE_NAME = "BT_SharedPreferences";
    private static final String SHARE_SETTING_FILE_NAME = "BT_Setting_SharedPreferences";
    private static final String BT_SAVE_DATA_NAME = "BT_Save_Data_SharedPreferences";

    private static BtSPUtil mSharedPreferencesUtil = null;

    private BtSPUtil() {
    }

    public static BtSPUtil getInstance() {
        synchronized (BtSPUtil.class) {
            if (mSharedPreferencesUtil == null) {
                mSharedPreferencesUtil = new BtSPUtil();
            }
        }
        return mSharedPreferencesUtil;
    }

    private SharedPreferences getSP(Context ctx) {
        return ctx.getSharedPreferences(SHARE_FILE_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences getSettingSP(Context context) {
        return context.getSharedPreferences(SHARE_SETTING_FILE_NAME, Context.MODE_PRIVATE);
    }

    private SharedPreferences getSaveDataSP(Context context) {
        return context.getSharedPreferences(BT_SAVE_DATA_NAME, Context.MODE_PRIVATE);
    }

    /**
     * 用于保存集合
     *
     * @param key  key
     * @param list 集合数据
     * @return 保存结果
     */
    public <T> boolean putListData(Context ctx, String key, List<T> list) {
        boolean result;
        String type = list.get(0).getClass().getSimpleName();
        SharedPreferences.Editor editor = getSP(ctx).edit();
        JsonArray array = new JsonArray();
        try {
            switch (type) {
                case "Boolean":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Boolean) list.get(i));
                    }
                    break;
                case "Long":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Long) list.get(i));
                    }
                    break;
                case "Float":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Float) list.get(i));
                    }
                    break;
                case "String":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((String) list.get(i));
                    }
                    break;
                case "Integer":
                    for (int i = 0; i < list.size(); i++) {
                        array.add((Integer) list.get(i));
                    }
                    break;
                default:
                    Gson gson = new Gson();
                    for (int i = 0; i < list.size(); i++) {
                        JsonElement obj = gson.toJsonTree(list.get(i));
                        array.add(obj);
                    }
                    break;
            }
            editor.putString(key, array.toString());
            result = true;
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        editor.apply();
        return result;
    }

    /**
     * 保存Int数据的方法
     */
    public void putIntData(Context context, String key, final int freq) {
        getSP(context).edit().putInt(key, freq).apply();
    }

    /**
     * 得到int保存数据的方法
     */
    public int getIntData(Context context, String key, int defValue) {
        return getSP(context).getInt(key, defValue);
    }

    /**
     * 保存boolean数据的方法
     */
    public void putBooleanData(Context context, String key, boolean freq) {
        getSP(context).edit().putBoolean(key, freq).apply();
    }


    /**
     * 得到boolean保存数据的方法
     */
    public boolean getBooleanData(Context context, String key, boolean freq) {
        return getSP(context).getBoolean(key, freq);
    }

    /**
     * 获取保存的List
     *
     * @param key key
     * @return 对应的Lis集合
     */
    public <T> List<T> getListData(Context ctx, String key, Class<T> cls) {
        List<T> list = new ArrayList<>();
        String json = getSP(ctx).getString(key, "");
        if (!json.equals("") && json.length() > 0) {
            Gson gson = new Gson();
            JsonArray array = new JsonParser().parse(json).getAsJsonArray();
            for (JsonElement elem : array) {
                list.add(gson.fromJson(elem, cls));
            }
        }
        return list;
    }

    //删除对应的List
    public void deleListdata(Context ctx, String key) {
        SharedPreferences.Editor editor = getSP(ctx).edit();
        editor.remove(key);
        editor.apply();
    }

    /**
     * 保存蓝牙连接是否打开状态-luoyin
     */
    public boolean setBTSwitchStateSP(Context ctx, boolean state) {
        return getSP(ctx).edit().putBoolean(BtConstant.SettingState.BT_SWITCH_STATE, state).commit();
    }

    /**
     * 获取蓝牙连接是否打开-luoyin
     *
     * @return 默认为true
     */
    public boolean getBTSwitchStateSP(Context ctx) {
        return getSP(ctx).getBoolean(BtConstant.SettingState.BT_SWITCH_STATE, true);
    }

    /**
     * 保存是否自动连接状态-luoyin
     */
    public boolean setAutoConnStateSP(Context ctx, boolean state) {
        return getSettingSP(ctx).edit().putBoolean(BtConstant.SettingState.AUTO_CONN_STATE, state).commit();
    }

    /**
     * 获取是否自动连接-luoyin
     *
     * @return 默认为false
     */
    public boolean getAutoConnStateSP(Context ctx) {
        return getSettingSP(ctx).getBoolean(BtConstant.SettingState.AUTO_CONN_STATE, true);
    }

    /**
     * 保存是否自动接听状态-luoyin
     */
    public boolean setAutoAnswerStateSP(Context ctx, boolean state) {
        return getSettingSP(ctx).edit().putBoolean(BtConstant.SettingState.AUTO_ANSWER_STATE, state).commit();
    }

    /**
     * 获取是否自动接听-luoyin
     *
     * @return 默认为false
     */
    public boolean getAutoAnswerStateSP(Context ctx) {
        return getSettingSP(ctx).getBoolean(BtConstant.SettingState.AUTO_ANSWER_STATE, false);
    }

    /**
     * 保存是否来电播报状态-luoyin
     */
    public boolean setAutoReadStateSP(Context ctx, boolean state) {
        return getSettingSP(ctx).edit().putBoolean(BtConstant.SettingState.AUTO_READ_STATE, state).commit();
    }

    /**
     * 获取是否来电播报-luoyin
     *
     * @return 默认为false
     */
    public boolean getAutoReadStateSP(Context ctx) {
        return getSettingSP(ctx).getBoolean(BtConstant.SettingState.AUTO_READ_STATE, false);
    }

    /**
     * 保存是否自动同步通讯录状态-luoyin
     */
    public boolean setAutoSyncBookStateSP(Context ctx, boolean state) {
        return getSettingSP(ctx).edit().putBoolean(BtConstant.SettingState.AUTO_SYNC_BOOK_STATE, state).commit();
    }

    /**
     * 获取是否自动同步通讯录-luoyin
     *
     * @return 默认为false
     */
    public boolean getAutoSyncBookStateSP(Context ctx) {
        return getSettingSP(ctx).getBoolean(BtConstant.SettingState.AUTO_SYNC_BOOK_STATE, false);
    }

    /**
     * 保存配对码
     */
    public boolean setBTConnCodeStateSP(Context ctx, boolean state) {
        return getSP(ctx).edit().putBoolean(BtConstant.SettingState.BT_CONN_CODE, state).commit();
    }

    /**
     * 获取配对码
     *
     * @return 默认为false
     */
    public String getBTConnCodeStateSP(Context ctx) {
        return getSP(ctx).getString(BtConstant.SettingState.BT_CONN_CODE, "0000");
    }


    /**
     * 保存上一个连接设备的蓝牙地址-luoyin
     */
    public boolean setLastConnectAddressSP(Context ctx, String address) {
        return getSP(ctx).edit().putString(BtConstant.SettingState.LAST_CONNECT_ADDRESS, address).commit();
    }

    /**
     * 获取上一个连接设备的蓝牙地址-luoyin
     *
     * @return 默认为""
     */
    public String getLastConnectAddressSP(Context ctx) {
        return getSP(ctx).getString(BtConstant.SettingState.LAST_CONNECT_ADDRESS, "");
    }

    public boolean isNewDevices(Context context, String address) {
        String lastConnectAddressSP = getLastConnectAddressSP(context);
        return !lastConnectAddressSP.equals(address);
    }

    /**
     * 保存是否已经同步通讯录状态-luoyin
     */
    public boolean setSyncContactsStateSP(Context ctx, boolean state) {
        return getSP(ctx).edit().putBoolean(BtConstant.SettingState.SYNC_CONTACTS_STATE, state).commit();
    }

    /**
     * 获取是否已经同步通讯录-luoyin
     *
     * @return 默认为false
     */
    public boolean getSyncContactsStateSP(Context ctx) {
        return getSP(ctx).getBoolean(BtConstant.SettingState.SYNC_CONTACTS_STATE, false);
    }

    /**
     * 保存是否已经同步通话记录状态-luoyin
     */
    public boolean setSyncCallLogStateSP(Context ctx, boolean state) {
        return getSP(ctx).edit().putBoolean(BtConstant.SettingState.SYNC_CALLLOG_STATE, state).commit();
    }

    /**
     * 获取是否已经同步通话记录-luoyin
     *
     * @return 默认为false
     */
    public boolean getSyncCallLogStateSP(Context ctx) {
        return getSP(ctx).getBoolean(BtConstant.SettingState.SYNC_CALLLOG_STATE, false);
    }

    /**
     * 是否手动同步了通话记录以及通讯录
     */
    public boolean isSyncContactAndCallLog(Context context) {
        return getSyncCallLogStateSP(context) && getSyncContactsStateSP(context);
    }

    public void clean(Context ctx) {
        getSP(ctx).edit().clear().apply();
    }

    public void cleanSetting(Context ctx) {
        getSP(ctx).edit().clear().apply();
    }

    /**
     * 获取是否来自通话记录页面-tyz
     *
     * @return 默认为false
     */
    public boolean getSyncCallLogPageSP(Context ctx) {
        return getSP(ctx).getBoolean(BtConstant.SettingState.SYNC_CALLLOGPAGE_STATE, false);
    }

    /**
     * 保存是否来自通话记录页面-tyz
     */
    public boolean setSyncCallLogPageSP(Context ctx, boolean state) {
        return getSP(ctx).edit().putBoolean(BtConstant.SettingState.SYNC_CALLLOGPAGE_STATE, state).commit();
    }

    /**
     * 存储历史配对设备
     *
     * @param context
     * @param data
     */
    public void putPairedDevicesData(Context context, ArrayList<DevicesListEntity> data) {
        SharedPreferences sp = getSaveDataSP(context);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("CollectionSize", data.size());
        for (int i = 0; i < data.size(); i++) {
            editor.putString("item_address" + i, data.get(i).getAddress());
            editor.putString("item_name" + i, data.get(i).getDeviceName());
            editor.putLong("item_timestamp" + i, data.get(i).getTimestamp());
        }
        //提交
        editor.apply();
    }

    /**
     * 获取历史配对设备
     *
     * @param context
     */
    public ArrayList<DevicesListEntity> getPairedDevicesData(Context context) {
        SharedPreferences sp = getSaveDataSP(context);
        ArrayList<DevicesListEntity> arrayList = new ArrayList<>();
        int listSize = sp.getInt("CollectionSize", 0);
        for (int i = 0; i < listSize; i++) {
            String address = sp.getString("item_address" + i, "");
            String name = sp.getString("item_name" + i, "");
            long timestamp = sp.getLong("item_timestamp" + i, 0);
            arrayList.add(new DevicesListEntity(address, name, DevicesListEntity.DeviceConnectStatus.CONNECTED, timestamp));
        }
        return arrayList;
    }
}
