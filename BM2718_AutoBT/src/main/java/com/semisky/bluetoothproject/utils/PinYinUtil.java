package com.semisky.bluetoothproject.utils;

import android.nfc.Tag;

import com.github.promeg.pinyinhelper.Pinyin;
import com.semisky.bluetoothproject.entity.ContactsEntity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

public class PinYinUtil {
    private static final String TAG = Logger.makeTagLog(PinYinUtil.class);

    public static int pintinToASCII(String fullName,String number) {
        if (null == fullName || "".equals(fullName)) {
            String pinyin = HanziToPinyin.getInstance().getPinYin(number);
            return pinyin.charAt(0) + 500;
        } else {
//            String pinyin = Pinyin.toPinyin(fullName.trim().charAt(0)).toLowerCase();
            String pinyin = HanziToPinyin.getInstance().getPinYin(fullName);
            if (Pinyin.isChinese(fullName.trim().charAt(0))) {
                return pinyin.charAt(0);
            }else if (isInteger(pinyin.charAt(0)+"")){
                return pinyin.charAt(0) + 500;
            }else if(isAZ(pinyin)){
                return pinyin.charAt(0) + 1000;
            }
            return pinyin.charAt(0) + 2000;
        }
    }
    /**
     * @return 是整数返回true,否则返回false
     */

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

/**
 * 判断一个字符串的首字符是否为字母
 */

    public static boolean isAZ(String s){
        char   c   =   s.charAt(0);
        int   i   =(int)c;
        if((i>=65&&i<=90)||(i>=97&&i<=122)){
            return true;
        }else{
            return false;
        }
    }

    /***
     * 排序
     * @param contactsData
     */
    public static void listSort(List<ContactsEntity> contactsData) {
        Collections.sort(contactsData, new Comparator<ContactsEntity>() {
            @Override
            public int compare(ContactsEntity o1, ContactsEntity o2) {
                if (o1.getOrderASCII() > o2.getOrderASCII()) {
                    return 1;
                } else if (o1.getOrderASCII() < o2.getOrderASCII()) {
                    return -1;
                } else if (o1.getOrderASCII() == o2.getOrderASCII()) {
                    return 0;
                }
                return 0;
            }
        });
    }
}
