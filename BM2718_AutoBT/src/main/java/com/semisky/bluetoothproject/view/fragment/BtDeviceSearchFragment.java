package com.semisky.bluetoothproject.view.fragment;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.semisky.bluetoothproject.MainActivity;
import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.adapter.DeviceListItemAdapter;
import com.semisky.bluetoothproject.entity.DevicesListEntity;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.model.BtBackCarModel;
import com.semisky.bluetoothproject.model.BtKeyModel;
import com.semisky.bluetoothproject.model.modelInterface.OnBackCarStateChangeListener;
import com.semisky.bluetoothproject.presenter.BtDeviceSearchPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.DeviceSearchInterface;
import com.semisky.bluetoothproject.utils.BtSPUtil;
import com.semisky.bluetoothproject.utils.Logger;
import com.semisky.bluetoothproject.view.BtMessageDialog;
import com.semisky.bluetoothproject.view.DialogView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import static android.util.Log.d;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要: 蓝牙设备搜索view
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtDeviceSearchFragment extends BaseFragment<DeviceSearchInterface, BtDeviceSearchPresenter>
        implements DeviceSearchInterface, CompoundButton.OnCheckedChangeListener {

    private static final String TAG = Logger.makeTagLog(BtDeviceSearchFragment.class);

    private LinkedList<DevicesListEntity> searchDevicesData = new LinkedList<>();
    private ArrayList<DevicesListEntity> pairedDevicesData = new ArrayList<>();
    private List<DevicesListEntity> cacheDevicesData = new ArrayList<>();

    private DeviceListItemAdapter deviceListItemAdapter;

    private Switch BtOpenSwitch;
    private RelativeLayout refreshLayout, rlNoneDevices;

    private BtMessageDialog btMessageDialog;
    private ListView deviceList;

    /**
     * 搜索设备中
     */
    private boolean isSearchDevice;

    /**
     * 设备连接中
     */
    private boolean isConnectDevice;

    /**
     * 蓝牙是否打开
     */
    private boolean isBtOpen;

    @Override
    protected BtDeviceSearchPresenter createPresenter() {
        return new BtDeviceSearchPresenter();
    }

    @Override
    protected void loadData() {
        Logger.d(TAG, "loadData: ");
        btMessageDialog = new BtMessageDialog(getActivity());
        mPresenter.initListener();
        checkSwitch();
        initKeyListener();
        initCarBackListener();
    }

    @Override
    protected void initView(View view) {
        deviceList = view.findViewById(R.id.lsDeviceList);
        refreshLayout = view.findViewById(R.id.rl_refresh_view);
        rlNoneDevices = view.findViewById(R.id.rlNoneDevices);

        BtOpenSwitch = view.findViewById(R.id.BtOpenSwitch);
        BtOpenSwitch.setOnCheckedChangeListener(this);

        Locale locale = getResources().getConfiguration().locale;
        String language = locale.getLanguage();
        if (language.contains("zh")) {
            BtOpenSwitch.setTrackResource(R.drawable.btn_set_switch_selector);
        } else {
            BtOpenSwitch.setTrackResource(R.drawable.btn_eng_set_switch_selector);
        }

        deviceListItemAdapter = new DeviceListItemAdapter(mContent,
                R.layout.item_device_list_view, searchDevicesData);
        deviceList.setAdapter(deviceListItemAdapter);

        initListener();

        refreshLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRefreshView();
                isSearchDevice = true;
            }
        });
    }

    private void initListener() {
        DeviceListItemAdapter.onDevicesConnectListener listener = new DeviceListItemAdapter.onDevicesConnectListener() {

            @Override
            public void onConnectClick(String address) {
                mPresenter.reqBtConnectHfpA2dp(address);
            }

            @Override
            public void onReqBtUnpair(final String address, final int position) {
                final DialogView dialogView = new DialogView(getContext());
                dialogView.createTitleDialog(getString(R.string.cx62_bt_dialog_del_title),
                        getString(R.string.cx62_bt_dialog_del_message), new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mPresenter.reqBtUnpair(address);
                                pairedDevicesRemove(position);
                                deviceListItemAdapter.removeItem(position);
                                checkListData();
                                dialogView.dismiss();
                            }
                        });

                dialogView.show();
            }
        };

        deviceListItemAdapter.setOnDevicesConnectListener(listener);
    }

    private void pairedDevicesRemove(int position) {
        pairedDevicesData.remove(position);
        BtSPUtil.getInstance().putPairedDevicesData(getContext(), pairedDevicesData);
    }

    private void checkListData() {
        int count = deviceListItemAdapter.getCount();
        if (count == 0) {
            deviceHandler.sendEmptyMessage(ONCE_DEVICE_FOUND);
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            fragmentHide();
        } else {
            fragmentShow();
        }
    }

    private final static int BT_STATUS_OPEN = 0x01;
    private final static int BT_STATUS_CLOSE = 0x02;
    private final static int DEVICE_FOUND = 0x03;
    private final static int SHOW_DEVICE_FOUND = 0x04;
    private final static int ONCE_DEVICE_FOUND = 0x05;
    private final static int SET_FLAG = 0x06;
    private final static int SHOW_PAIRED_DEVICES = 0x07;
    private final static int LOAD_PAIRED_DEVICES = 0x08;
    private final static int AUTO_LOAD_DEVICE_FOUND = 0x09;

    public Handler deviceHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case BT_STATUS_OPEN:
                    refreshLayout.setVisibility(View.VISIBLE);
                    rlNoneDevices.setVisibility(View.VISIBLE);
                    BtOpenSwitch.setChecked(true);
                    showSettingView(true);
                    break;
                case BT_STATUS_CLOSE:
                    refreshLayout.setVisibility(View.GONE);
                    rlNoneDevices.setVisibility(View.GONE);
                    BtOpenSwitch.setChecked(false);
                    showSettingView(false);
                    break;
                case DEVICE_FOUND:
                    if (isSearchDevice) {
                        notifyListData();
                    }
                    break;
                case SHOW_DEVICE_FOUND:
                    deviceList.setVisibility(View.VISIBLE);
                    rlNoneDevices.setVisibility(View.GONE);
                    break;
                case ONCE_DEVICE_FOUND:
                    deviceList.setVisibility(View.GONE);
                    rlNoneDevices.setVisibility(View.VISIBLE);
                    break;
                case SET_FLAG:
                    isSearchDevice = false;
                    break;

                case SHOW_PAIRED_DEVICES:
                    deviceListItemAdapter.setList(searchDevicesData);
                    break;

                case LOAD_PAIRED_DEVICES:
                    pairedDevicesData = BtSPUtil.getInstance().getPairedDevicesData(getContext());
                    Logger.d(TAG, "onAdapterDiscoveryFinished: pairedDevicesData size " + pairedDevicesData.size());
                    if (pairedDevicesData.size() > 0) {
                        for (int i = 0; i < pairedDevicesData.size(); i++) {
                            searchDevicesData.addFirst(pairedDevicesData.get(i));
                        }
                    }

                    //可能会返回多个设备 根据address去重
                    cleanRepeatForList();

                    //按时间戳排序
                    pairedDevicesDataSort();

                    if (searchDevicesData.size() > 0) {
                        showDeviceFound();
                        notifyListData();
                    } else {
                        onceDeviceFound();
                    }

                    dismissDialog();
                    isSearchDevice = false;
                    break;

                case AUTO_LOAD_DEVICE_FOUND:
                    notifyListData();
                    break;
            }
            return false;
        }
    });

    private void notifyListData() {
        searchDevicesData.addAll(cacheDevicesData);
        cacheDevicesData.clear();
        deviceListItemAdapter.setList(searchDevicesData);
    }

    private void showDeviceFound() {
        deviceList.setVisibility(View.VISIBLE);
        rlNoneDevices.setVisibility(View.GONE);
    }

    private void onceDeviceFound() {
        deviceList.setVisibility(View.GONE);
        rlNoneDevices.setVisibility(View.VISIBLE);
    }

    private void showSettingView(boolean status) {
        final MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            if (status) {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.refreshView(true);
                    }
                });
            } else {
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.refreshView(false);
                    }
                });
            }
        }
    }

    protected void fragmentHide() {

    }

    protected void fragmentShow() {
        Logger.d(TAG, "fragmentShow: ");
        mPresenter.initListener();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_connect_fragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d(TAG, "onResume: ");
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_connect_fragment));
    }

    public void checkSwitch() {
        //蓝牙是否已开启
        mPresenter.setBtSwitch();
    }

    private void clickRefreshView() {
        searchDevicesData.clear();
        cacheDevicesData.clear();
        deviceListItemAdapter.clear();
        mPresenter.startBtDiscovery();
    }

    @Override
    protected int getResourceId() {
        return R.layout.drvice_search_fragment;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy: ");
        dismissDialog();
    }

    private void initKeyListener() {
        btMessageDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dismissDialog();
                    if (getActivity() != null) {
                        getActivity().finish();
                    }
                }
                return false;
            }
        });

        BtKeyModel.CancelSearchInterface cancelSearchInterface = new BtKeyModel.CancelSearchInterface() {
            @Override
            public void cancelSearchDevice() {
                dismissDialog();
            }
        };

        BtKeyModel.getInstance().setCancelSearchInterface(cancelSearchInterface);
    }

    private void initCarBackListener() {
        OnBackCarStateChangeListener onBackCarStateChangeListener = new OnBackCarStateChangeListener() {
            @Override
            public void onBackCarEnter() {
                //进入倒车打断搜索
                if (isSearchDevice)
                    dismissDialog();
            }

            @Override
            public void onBackCarQuit() {

            }
        };

        BtBackCarModel.getInstance().setOnCarStateChangeListener(onBackCarStateChangeListener);
    }

    private void dismissDialog() {
        try {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (btMessageDialog != null && btMessageDialog.isShowing()) {
                        Logger.d(TAG, "btMessageDialog: dismiss");
                        btMessageDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            Logger.e(TAG, "dismissDialog: " + e.getLocalizedMessage());
        }

    }

    @Override
    public void onAdapterDiscoveryStarted() {
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initCancelButtonView(getString(R.string.cx62_bt_search_start),
                                new BtMessageDialog.ClickCancelListener() {
                                    @Override
                                    public void clickCancel() {
                                        btMessageDialog.dismiss();
                                    }
                                }, new BtMessageDialog.DialogDisMissListener() {
                                    @Override
                                    public void dialogDisMissCallback(DialogInterface dialog) {
                                        mPresenter.cancelBtDiscovery();
                                    }
                                });
                        btMessageDialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void onAdapterDiscoveryFinished() {
        if (isSearchDevice) {
            Logger.d(TAG, "onAdapterDiscoveryFinished: ");
            deviceHandler.removeMessages(LOAD_PAIRED_DEVICES);
            deviceHandler.sendEmptyMessage(LOAD_PAIRED_DEVICES);
        }
    }

    private void pairedDevicesDataSort() {
        if (searchDevicesData.size() > 0) {
            Collections.sort(searchDevicesData, new Comparator<DevicesListEntity>() {
                @Override
                public int compare(DevicesListEntity o1, DevicesListEntity o2) {
                    //按照时间戳进行降序
                    return Long.compare(o2.getTimestamp(), o1.getTimestamp());
                }
            });
        }

        d(TAG, "排序后 ----------------------: ");

        for (DevicesListEntity devicesListEntity : searchDevicesData) {
            d(TAG, devicesListEntity.getDeviceName() + "---" + devicesListEntity.getTimestamp());
        }

    }

    private void cleanRepeatForList() {
        if (searchDevicesData.size() > 0) {
            Logger.d(TAG, "cleanRepeatForList: " + searchDevicesData.size());
            for (int i = 0; i < searchDevicesData.size(); i++) {
                for (int j = 0; j < searchDevicesData.size(); j++) {
                    if (i != j && searchDevicesData.get(i).getAddress().equals(searchDevicesData.get(j).getAddress())) {
                        searchDevicesData.remove(searchDevicesData.get(j));
                    }
                }
            }
        }
    }

    @Override
    public void onDeviceFound(String address, String name, byte category) {
        Logger.d(TAG, "onDeviceFound: " + name + " cacheDevicesData " + cacheDevicesData.size());
        if (cacheDevicesData.size() > 0) {
            for (DevicesListEntity devicesListEntity : cacheDevicesData) {
                if (!address.equals(devicesListEntity.getAddress())) {
                    cacheDevicesData.add(new DevicesListEntity(address, name, DevicesListEntity.DeviceConnectStatus.NOT_CONNECT, 1));
                }
            }
        } else {
            cacheDevicesData.add(new DevicesListEntity(address, name, DevicesListEntity.DeviceConnectStatus.NOT_CONNECT, 1));
        }

        if (cacheDevicesData.size() == 1) {
            deviceHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        } else {
            deviceHandler.sendEmptyMessage(ONCE_DEVICE_FOUND);
        }

        deviceHandler.sendEmptyMessage(AUTO_LOAD_DEVICE_FOUND);
    }

    @Override
    public void onPairedDevices(int elements, String[] address, String[] name, int[] supportProfile, byte[] category) {

    }

    @Override
    public void stateDisconnecting(String address) {

    }

    @Override
    public void stateConnected(final String address) {
        isConnectDevice = false;
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initMessageView(getString(R.string.cx62_bt_dialog_connect_success));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(1500);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                btMessageDialog.dismiss();
                            }
                        }).start();
                    }
                }
            });
        }

        refreshMainView();
        mPresenter.unregisterListener();
    }

    @Override
    public void stateConnecting(String address) {
        isConnectDevice = true;
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initOnlyMessageDialog(getString(R.string.cx62_bt_dialog_connecting)
                                , new BtMessageDialog.DialogDisMissListener() {
                                    @Override
                                    public void dialogDisMissCallback(DialogInterface dialog) {
//                                    if (isConnectDevice) { 只有在ACC OFF才断开
//                                        mPresenter.reqBtDisconnectAll();
//                                        BtHfpModel.getInstance().removeCallBackRunnable();
//                                    }
                                    }
                                });
                        btMessageDialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void stateDisconnect(String address) {
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initMessageView(getString(R.string.cx62_bt_dialog_connect_fail));
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    btMessageDialog.dismiss();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            });
        }
    }

    @Override
    public void isBtConnect(boolean status) {
        Logger.d(TAG, "isBtConnect: " + status);
        isBtOpen = status;
        BtOpenSwitch.setChecked(status);
        if (status) {
            deviceHandler.sendEmptyMessage(ONCE_DEVICE_FOUND);
            showPairedDevices();
        } else {
            btStatusClose();
        }
    }

    @Override
    public void btStatusOpen() {
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initMessageView(getString(R.string.cx62_bt_dialog_bt_open_success));
                        btMessageDialog.show();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Thread.sleep(2000);
                                    btMessageDialog.dismiss();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            });
        }

        deviceHandler.sendEmptyMessage(BT_STATUS_OPEN);
        showPairedDevices();
    }

    private void showPairedDevices() {
        pairedDevicesData = BtSPUtil.getInstance().getPairedDevicesData(getContext());
        Logger.d(TAG, "showPairedDevices: " + pairedDevicesData.size());

        if (pairedDevicesData.size() > 0) {
            searchDevicesData.clear();
            searchDevicesData.addAll(pairedDevicesData);

            deviceHandler.sendEmptyMessage(SHOW_PAIRED_DEVICES);
            deviceHandler.sendEmptyMessage(SHOW_DEVICE_FOUND);
        } else {
            deviceHandler.sendEmptyMessage(ONCE_DEVICE_FOUND);

        }
    }

    @Override
    public void btStatusOpening() {
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initMessageView(getString(R.string.cx62_bt_dialog_bt_opening));
                        btMessageDialog.show();
                    }
                }
            });
        }
    }

    @Override
    public void btStatusClose() {
        Logger.d(TAG, "btStatusClose: ");
        FragmentActivity activity = getActivity();
        if (activity != null && isAdded()) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isAdded()) {
                        btMessageDialog.initCancelConfirmButtonView(getString(R.string.cx62_bt_dialog_bt_close_tip),
                                new BtMessageDialog.ClickConfirmListener() {
                                    @Override
                                    public void clickConfirm() {
                                        mPresenter.setBtEnable(true);
                                    }
                                });

                        btMessageDialog.show();
                    }
                }
            });
        }


        deviceHandler.sendEmptyMessage(BT_STATUS_CLOSE);

    }

    @Override
    public void btStatusClosing() {

    }

    private void refreshMainView() {
        final MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mainActivity.initView();
                }
            });
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            if (!isBtOpen) {
                mPresenter.setBtEnable(true);
            }
        } else {
            mPresenter.setBtEnable(false);
            searchDevicesData.clear();
            cacheDevicesData.clear();
            deviceListItemAdapter.clear();
        }
    }

    public void dismissDialogFragment() {
        dismissDialog();
    }
}
