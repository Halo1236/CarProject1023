package com.semisky.btcarkit.service.thread;

import com.semisky.btcarkit.service.at_cmd.IApiResponseTable;
import com.semisky.btcarkit.service.natives.IFscBwNative;
import com.semisky.btcarkit.utils.Logutil;

public class CmdHandlerRunnable implements Runnable {
    private static final String TAG = Logutil.makeTagLog(CmdHandlerRunnable.class);
    private IApiResponseTable mIApiResponseTable;
    private IFscBwNative mFscBwNative;
    private volatile int[] mData;
    private volatile boolean mIsInterupt = false;
    private volatile boolean mIsRunning = false;

    public void onAttach(IApiResponseTable responseTable) {
        this.mIApiResponseTable = responseTable;
    }

    public void onAttach(IFscBwNative fscBwNative) {
        this.mFscBwNative = fscBwNative;
    }

    public void prepare() {
        this.mIsInterupt = false;
    }

    public void stop() {
        this.mIsInterupt = true;
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    @Override
    public void run() {
        Logutil.i(TAG, "############### CmdHandlerRunnable.run()###################");
        this.mIsRunning = true;
        int state = mFscBwNative.openSerial();
        notifySerialStateChanged(state);

        while (!mIsInterupt) {
            if (null != mIApiResponseTable) {
                this.mData = mFscBwNative.recvCMD();

                Logutil.i(TAG, "recv data len : " + (null != mData ? mData.length : 0));

                if (null != mData && mData.length > 0) {
                    mIApiResponseTable.onDataParse(this.mData);
                }
            }
        }
        this.mIsRunning = false;
    }


    public interface OnSerialStateListener {
        void onStateChanged(int state);
    }

    private OnSerialStateListener mOnSerialStateListener;

    public void registerListener(OnSerialStateListener l) {
        this.mOnSerialStateListener = l;
    }

    void notifySerialStateChanged(int state) {
        if (null != mOnSerialStateListener) {
            this.mOnSerialStateListener.onStateChanged(state);
        }
    }

}
