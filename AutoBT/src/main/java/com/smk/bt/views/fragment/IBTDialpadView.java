package com.smk.bt.views.fragment;

import java.util.List;

public interface IBTDialpadView {

    void onUpdateDialpadContent(List<String> data);
    void onChangeInputboxTextContent(String str);
}
