package com.semisky.btcarkit;

import android.app.Activity;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.semisky.btcarkit.aidl.ISmkCallbackBluetooth;
import com.semisky.btcarkit.service.at_cmd.ApiCommandTable;
import com.semisky.btcarkit.service.at_cmd.ApiResponseTable;
import com.semisky.btcarkit.service.manager.BTRoutingCommandManager;
import com.semisky.btcarkit.service.natives.FscBwNative;
import com.semisky.btcarkit.utils.Logutil;

public class MainActivity extends Activity implements View.OnClickListener, BTRoutingCommandManager.OnServiceStateListener {
    private static final String TAG = Logutil.makeTagLog(MainActivity.class);
    private Button btn_click_me, btn_send_cmd, btn_read_cmd, btn_parse_cmd;
    private FscBwNative mFscBwNative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        this.mFscBwNative = new FscBwNative();
        this.btn_click_me = (Button) findViewById(R.id.btn_click_me);
        this.btn_send_cmd = (Button) findViewById(R.id.btn_send_cmd);
        this.btn_read_cmd = (Button) findViewById(R.id.btn_read_cmd);
        this.btn_parse_cmd = (Button) findViewById(R.id.btn_parse_cmd);
        this.btn_click_me.setOnClickListener(this);
        this.btn_send_cmd.setOnClickListener(this);
        this.btn_read_cmd.setOnClickListener(this);
        this.btn_parse_cmd.setOnClickListener(this);

        BTRoutingCommandManager
                .getInstance()
                .onAttatch(this)
                .registerCallback(this)
                .startService()
                .bindService();
    }

    int[] mData = null;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click_me:
                int num = mFscBwNative.openBwSerial();
                boolean isBtEnable = BTRoutingCommandManager.getInstance().isBtEnable();
                Toast.makeText(this, "BT STATE：" + isBtEnable, Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_send_cmd:
                Toast.makeText(this, "发送AT指令", Toast.LENGTH_LONG).show();
                if(!BTRoutingCommandManager.getInstance().isBtEnable()){
                    BTRoutingCommandManager.getInstance().setBtEnable(true);
                }else {
                    BTRoutingCommandManager.getInstance().setBtEnable(false);
                }
//                mFscBwNative.sendCommand(ApiCommandTable.make(ApiCommandTable.AVRCP_STATE,null));
                break;
            case R.id.btn_read_cmd:
//                Toast.makeText(this,"读AT指令"+len,Toast.LENGTH_LONG).show();
                break;
            case R.id.btn_parse_cmd:
                break;
        }
    }


    /*private void readData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int num = mFscBwNative.openBwSerial();
                Logutil.i(TAG, "=================read openBwSerial() ....");
                try {
                    while (true) {
                        mData = mFscBwNative.recvResponse();
                        int len = null != mData ? mData.length : 0;
                        Logutil.i(TAG, "=================read data=" + len);
                        Thread.sleep(1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }*/

    @Override
    public void onServiceConnected() {
    }


}
