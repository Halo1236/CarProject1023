package com.semisky.bluetoothproject.presenter;

import com.semisky.bluetoothproject.manager.BtServiceManager;
import com.semisky.bluetoothproject.model.BtContactsDownloadModel;
import com.semisky.bluetoothproject.presenter.viewInterface.CallRecordsInterface;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtCallRecordsPresenter extends BasePresenter<CallRecordsInterface> {

    private BtContactsDownloadModel btContactsDownloadModel;
    private BtServiceManager btServiceManager;

    public BtCallRecordsPresenter() {
        btContactsDownloadModel = BtContactsDownloadModel.getInstance();
        btServiceManager = BtServiceManager.getInstance();
    }

    public void initListener() {
        btContactsDownloadModel.setCallRecordsInterface(getViewRfr());
    }

    public void unregisterListener() {
        btContactsDownloadModel.unSetCallRecordsInterface();
    }

    public void downloadedCallLog() {
        btServiceManager.reqPbapDownloadedCallLog();
    }

    public void reqHfpDialCall(String number) {
        btServiceManager.reqHfpDialCall(number);
    }
}
