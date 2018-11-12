package com.semisky.btcarkit.service.natives;

public interface IFscBwNative {

    int openSerial();
    int closeSerial();
    int sendCMD(String cmd);
    int[] recvCMD();
}
