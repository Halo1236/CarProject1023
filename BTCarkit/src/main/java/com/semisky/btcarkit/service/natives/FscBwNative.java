package com.semisky.btcarkit.service.natives;

public class FscBwNative implements IFscBwNative {
    static {
        System.loadLibrary("fscbwnative");
    }

    public native int openBwSerial();

    public native int closeBwSerial();

    public native int sendCommand(String command);

    public native int[] recvResponse();


    @Override
    public int openSerial() {
        return openBwSerial();
    }

    @Override
    public int closeSerial() {
        return closeBwSerial();
    }

    @Override
    public int sendCMD(String cmd) {
        return sendCommand(cmd);
    }

    @Override
    public int[] recvCMD() {
        return recvResponse();
    }
}
