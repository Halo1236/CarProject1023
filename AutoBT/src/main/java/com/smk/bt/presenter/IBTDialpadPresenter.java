package com.smk.bt.presenter;

public interface IBTDialpadPresenter {
    void loadDiapadContant();
    void handlerDialpadTextContent(String text);
    void handlerOneByOneDeleteInputboxContent();
    void deleteAllInputboxContent();
    void handlerDialing();
}
