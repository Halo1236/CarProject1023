package com.semisky.testjni2;

public class XJni {
    static {
        System.loadLibrary("xjni");
    }

    public native String getStr(String str);
}
