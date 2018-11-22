package com.smk.dialogdemo;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "test";

    private Button btn_click_me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_click_me).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showOneTitleDialog();
            }
        });
    }


    private MessageDialog messageDialog;

    /**
     * title
     * message
     * bttton-是
     * button-否
     */
    private void showDialog1() {
        if (null == messageDialog) {
            messageDialog = new MessageDialog(this);
            messageDialog
                    .setTitle("确定删除")
                    .setMessage("删除后，该设备所有信息将一并删除。")
                    .setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("test", "Click Confirm Event ....");
                        }
                    })
                    .setNegativeButton("否", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Log.i("test", "Click Cancel Event ....");
                        }
                    });

        }

        if (!messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

    /**
     * title
     * bttton-是
     * button-否
     */
    private void showDialog2() {
        if (null == messageDialog) {
            messageDialog = new MessageDialog(this);
            messageDialog
                    .setTitle("蓝牙功能已关闭，是否开启？")
                    .setPositiveButton("是", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    })
                    .setNegativeButton("否", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
        }

        if (!messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

    /**
     * title
     * button-取消
     */
    private void showDialog3() {
        if (null == messageDialog) {
            messageDialog = new MessageDialog(this);
            messageDialog
                    .setTitle("设备连接中...")
                    .setNegativeButton("取消", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
        }

        if (!messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

    /**
     * title
     * button-取消
     */
    private void showDialog4() {
        if (null == messageDialog) {
            messageDialog = new MessageDialog(this);
            messageDialog
                    .setTitle("同步失败！")
                    .setMessage("请\"设置\"，手动同步。");
        }

        if (!messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

    /**
     * title
     */
    private void showDialog5() {
        if (null == messageDialog) {
            messageDialog = new MessageDialog(this);
            messageDialog
                    .setTitle("同步成功！")
                    .enableMessageTextSpace(false);
        }

        if (!messageDialog.isShowing()) {
            messageDialog.show();
        }
    }

    /**
     * TitleMessageTwoButtonDialog
     * TitleTwoButtonDialog
     * TitleOneButtonDialog
     * TitleMessageDialog
     * TitleDialog
     */



    private MultiTitleDialog multiTitleDialog;

    private void showOneTitleDialog() {
        if (null == multiTitleDialog) {
            multiTitleDialog = new MultiTitleDialog(this);
            multiTitleDialog
                    .setBackgroudResourceByRootLayout(R.drawable.dialog_global_bg)
                    .setRootLayoutSize(500, 260)
                    .setTextByFirstTitle("同步失败！")
                    .setTextSizeByFirstTitle(28)
                    .setTextColorByFirstTitle(Color.BLUE)
                    .enableCenterHorizontalByFirstTitle(true)
                    .setMarginTopByFirstTitle(44)
//                    .enableCenterInParentByFirstTitle(true)
                    .setVisibilityBySecondTitle(View.VISIBLE)
                    .setTextBySecondTitle("请\"设置\"，手动同步。")
                    .setTextSizeBySecondTitle(26)
                    .setTextColorBySecondTitle(Color.RED)
                    .enableCenterHorizontalBySecondTitle(true)
                    .setMarginTopBySecondTitle(26)
                    .enableShowTimeoutToDismiss(true)
                    .setTimeout(1500);

        }

        if (!multiTitleDialog.isShowing()) {
            multiTitleDialog.show();
        }
    }
}
