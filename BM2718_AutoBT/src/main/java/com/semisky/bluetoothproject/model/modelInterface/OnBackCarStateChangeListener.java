package com.semisky.bluetoothproject.model.modelInterface;

/**
 * /**
 * 倒车状态
 * 进入倒车 true,退出倒车 false
 */
public interface OnBackCarStateChangeListener {

    void onBackCarEnter();

    void onBackCarQuit();
}
