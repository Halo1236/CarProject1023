package com.smk.bt.presenter;

import com.smk.bt.constant.BTConst;
import com.smk.bt.views.fragment.IBTDialpadView;

import java.util.Arrays;

public class BTDialpadPresenter<V extends IBTDialpadView> extends BasePresenter<V> implements IBTDialpadPresenter {
    private String mInputboxTextContent = "";
    @Override
    public void loadDiapadContant() {
        if(isBindView()){
            mViewRfr.get().onUpdateDialpadContent(Arrays.asList(BTConst.BT_DIALPAD_CONTANT));
        }
    }

    @Override
    public void handlerDialpadTextContent(String text) {
        if(!isBindView()){
            return;
        }
        mInputboxTextContent+=text;
        mViewRfr.get().onChangeInputboxTextContent(mInputboxTextContent);
    }

    @Override
    public void handlerOneByOneDeleteInputboxContent() {
        if(!isBindView()){
            return;
        }
        if(null == mInputboxTextContent){
            return;
        }
        if(mInputboxTextContent.length() > 1){
            mInputboxTextContent = mInputboxTextContent.substring(0,mInputboxTextContent.length()-1);
        }else{
            mInputboxTextContent = "";
        }
        mViewRfr.get().onChangeInputboxTextContent(mInputboxTextContent);

    }

    @Override
    public void deleteAllInputboxContent() {
        if(!isBindView()){
            return;
        }
        mInputboxTextContent = "";
        mViewRfr.get().onChangeInputboxTextContent(mInputboxTextContent);
    }

    @Override
    public void handlerDialing() {
        if(!isBindView()){
            return;
        }
    }
}
