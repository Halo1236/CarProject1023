package com.semisky.bluetoothproject.presenter;

import android.os.Handler;
import android.os.Message;

import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.manager.BtServiceManager;
import com.semisky.bluetoothproject.model.BtContactsDownloadModel;
import com.semisky.bluetoothproject.presenter.viewInterface.ContactsInterface;
import com.semisky.bluetoothproject.utils.Logger;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by chenhongrui on 2018/8/7
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtTelephoneContactPresenter extends BasePresenter<ContactsInterface> {

    private static final String TAG = Logger.makeTagLog(BtTelephoneContactPresenter.class);

    private BtServiceManager btServiceManager;
    private BtContactsDownloadModel btContactsDownloadModel;
    private RefreshViewHandler refreshViewHandler;

    public BtTelephoneContactPresenter() {
        if (refreshViewHandler != null) {
            refreshViewHandler = new RefreshViewHandler(this);
        }
        btServiceManager = BtServiceManager.getInstance();
        btContactsDownloadModel = BtContactsDownloadModel.getInstance();
    }

    public void initListener() {
        btContactsDownloadModel.setContactsInterface(getViewRfr());
    }

    private final static int SHOW_DEVICE_FOUND = 0x01;
    private final static int ONCE_DEVICE_FOUND = 0x02;
    private final static int DOWN_LOAD_COMPLETED = 0x03;
    private final static int DOWN_LOADING = 0x04;

    private static class RefreshViewHandler extends Handler {

        WeakReference<BtTelephoneContactPresenter> reference;

        RefreshViewHandler(BtTelephoneContactPresenter reference) {
            this.reference = new WeakReference<>(reference);
        }

        @Override
        public void handleMessage(Message msg) {
            BtTelephoneContactPresenter presenter = reference.get();
            switch (msg.what) {
                case SHOW_DEVICE_FOUND:

                    break;
                case ONCE_DEVICE_FOUND:
                    presenter.getViewRfr().showNoneData();
                    break;
                case DOWN_LOAD_COMPLETED:
//                    presenter.getViewRfr().onDownloadCompleted((List<ContactsEntity>)msg.obj);
                    break;
                case DOWN_LOADING:

                    break;

                default:

                    break;
            }
        }
    }

    public void unregisterListener() {
        btContactsDownloadModel.unSetContactsInterface();
    }

    public void downloadedConnect() {
        btServiceManager.reqPbapDownloadConnect();
    }

    public void reqHfpDialCall(String number) {
        btServiceManager.reqHfpDialCall(number);
    }

    private void sendMessageNoneData() {
        if (refreshViewHandler != null) {
            refreshViewHandler.sendEmptyMessage(ONCE_DEVICE_FOUND);
        }
    }

    private void sendMessageShowData() {
        if (refreshViewHandler != null) {
            refreshViewHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        }
    }

}
