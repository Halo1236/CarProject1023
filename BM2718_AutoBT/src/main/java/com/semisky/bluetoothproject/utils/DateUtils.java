package com.semisky.bluetoothproject.utils;

import android.text.TextUtils;

import com.semisky.bluetoothproject.model.BtStatusModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class DateUtils {

    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
    // HH时mm分ss秒，
    // strTime的时间格式必须要与formatType的时间格式相同
    public static Date stringToDate(String strTime, String formatType) {
        if (TextUtils.isEmpty(strTime)) {
            return null;
        }
        SimpleDateFormat formatter = new SimpleDateFormat(formatType, BtStatusModel.getInstance().getLocalStats());
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
