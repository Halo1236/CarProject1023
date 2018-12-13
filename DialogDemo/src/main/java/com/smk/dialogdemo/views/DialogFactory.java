package com.smk.dialogdemo.views;

import android.content.Context;
import android.view.View;

public class DialogFactory {


    /**
     * 两个标题，两个按键 样式弹窗
     *
     * @param ctx
     * @param firstTitle
     * @param secondTitle
     * @param firstBtnCallback
     * @param secondBtnCallback
     * @return
     */
    public static SmartDialog createTwoTitleTwoButtonThemeDialog(Context ctx,
                                                                 String firstTitle,
                                                                 String secondTitle,
                                                                 String fistBtnText,
                                                                 String secondBtnText,
                                                                 int globalDialogBg,
                                                                 int firstBtnBg,
                                                                 int secondBtnBg,
                                                                 View.OnClickListener firstBtnCallback,
                                                                 View.OnClickListener secondBtnCallback) {
        SmartDialog smartDialog = new SmartDialog(ctx);
        smartDialog
                .setRootLayoutBackground(globalDialogBg)
                .setRootLayoutSize(500, 260)
                .setButtonBackground(SmartDialog.BUTTON_FIRST, firstBtnBg)
                .setButtonBackground(SmartDialog.BUTTON_SECOND, secondBtnBg)
                .setTitleMargin(SmartDialog.TITLE_FIRST, SmartDialog.MARGIN_TOP, 45)
                .setTitleMargin(SmartDialog.TITLE_SECOND, SmartDialog.MARGIN_TOP, 25)
                .setButtonLayoutMargin(SmartDialog.MARGIN_BOTTOM, 25)
                .setButtonLayoutAlignParent(SmartDialog.ALIGN_BOTTOM)
                .setButtonAlignParent(SmartDialog.BUTTON_SECOND, SmartDialog.ALIGN_RIGHT)
                .setButtonMargin(SmartDialog.BUTTON_FIRST, SmartDialog.MARGIN_LEFT, 62)
                .setButtonMargin(SmartDialog.BUTTON_SECOND, SmartDialog.MARGIN_RIGHT, 62)
                .setTitleTextSize(SmartDialog.TITLE_FIRST, 28)
                .setTitleTextSize(SmartDialog.TITLE_SECOND, 22)
                .setButtonTextSize(SmartDialog.BUTTON_FIRST, 26)
                .setButtonTextSize(SmartDialog.BUTTON_SECOND, 26)
                .setTitleText(SmartDialog.TITLE_FIRST, firstTitle)
                .setTitleText(SmartDialog.TITLE_SECOND, secondTitle)
                .setButtonText(SmartDialog.BUTTON_FIRST, fistBtnText)
                .setButtonText(SmartDialog.BUTTON_SECOND, secondBtnText)
                .setButtonCallback(SmartDialog.BUTTON_FIRST, firstBtnCallback)
                .setButtonCallback(SmartDialog.BUTTON_SECOND, secondBtnCallback);
        return smartDialog;
    }

    /**
     * 一个标题，两个按键 样式弹窗
     *
     * @param ctx
     * @param title
     * @param fistBtnText
     * @param secondBtnText
     * @param firstBtnCallback
     * @param secondBtnCallback
     * @return
     */
    public static SmartDialog createOneTitleTwoButtonThemeDialog(Context ctx,
                                                                 String title,
                                                                 String fistBtnText,
                                                                 String secondBtnText,
                                                                 int globalBg,
                                                                 int firstBtnBg,
                                                                 int secondBtnBg,
                                                                 View.OnClickListener firstBtnCallback,
                                                                 View.OnClickListener secondBtnCallback) {
        SmartDialog smartDialog = new SmartDialog(ctx);
        smartDialog
                .setRootLayoutBackground(globalBg)
                .setRootLayoutSize(500, 260)
                .setButtonBackground(SmartDialog.BUTTON_FIRST, firstBtnBg)
                .setButtonBackground(SmartDialog.BUTTON_SECOND, secondBtnBg)
                .setTitleMargin(SmartDialog.TITLE_FIRST, SmartDialog.MARGIN_TOP, 45)
                .setButtonLayoutMargin(SmartDialog.MARGIN_BOTTOM, 25)
                .setButtonLayoutAlignParent(SmartDialog.ALIGN_BOTTOM)
                .setTitleVisibility(SmartDialog.TITLE_SECOND, View.GONE)//隐藏第二个标题
                .setButtonAlignParent(SmartDialog.BUTTON_SECOND, SmartDialog.ALIGN_RIGHT)// 第二按钮右对齐
                .setButtonMargin(SmartDialog.BUTTON_FIRST, SmartDialog.MARGIN_LEFT, 62)// 第一个按钮距左62px
                .setButtonMargin(SmartDialog.BUTTON_SECOND, SmartDialog.MARGIN_RIGHT, 62)// 第二个按钮距右62px
                .setTitleTextSize(SmartDialog.TITLE_FIRST, 28)
                .setButtonTextSize(SmartDialog.BUTTON_FIRST, 26)
                .setButtonTextSize(SmartDialog.BUTTON_SECOND, 26)
                .setTitleText(SmartDialog.TITLE_FIRST, title)
                .setButtonText(SmartDialog.BUTTON_FIRST, fistBtnText)
                .setButtonText(SmartDialog.BUTTON_SECOND, secondBtnText)
                .setButtonCallback(SmartDialog.BUTTON_FIRST, firstBtnCallback)
                .setButtonCallback(SmartDialog.BUTTON_SECOND, secondBtnCallback);
        return smartDialog;
    }

    /**
     * 一个标题，一个按键样式弹窗
     *
     * @param ctx
     * @param title
     * @param btnText
     * @param btnCallback
     * @return
     */
    public static SmartDialog createOneTitleOneButtonThemeDialog(Context ctx
            , String title
            , String btnText
            ,int globalDialogBg
            ,int btnBg
            , View.OnClickListener btnCallback) {
        SmartDialog smartDialog = new SmartDialog(ctx);
        smartDialog
                .setRootLayoutBackground(globalDialogBg)
                .setRootLayoutSize(500, 260)
                .setButtonBackground(SmartDialog.BUTTON_SECOND, btnBg)
                .setTitleMargin(SmartDialog.TITLE_FIRST, SmartDialog.MARGIN_TOP, 45)
                .setButtonLayoutMargin(SmartDialog.MARGIN_BOTTOM, 25)
                .setButtonLayoutAlignParent(SmartDialog.ALIGN_BOTTOM)
                .setButtonVisibility(SmartDialog.BUTTON_FIRST, View.GONE)// 第一个按键隐藏
                .setTitleVisibility(SmartDialog.TITLE_SECOND, View.GONE)// 第二标题隐藏
                .setButtonAlignParent(SmartDialog.BUTTON_SECOND, SmartDialog.ALIGN_CENTER_HORIZONTAL)
                .setTitleTextSize(SmartDialog.TITLE_FIRST, 28)
                .setButtonTextSize(SmartDialog.BUTTON_SECOND, 26)
                .setTitleText(SmartDialog.TITLE_FIRST, title)
                .setButtonText(SmartDialog.BUTTON_SECOND, btnText)
                .setButtonCallback(SmartDialog.BUTTON_SECOND, btnCallback);
        return smartDialog;
    }

    /**
     * 二个标题样式弹窗
     *
     * @param ctx
     * @param firstTitle
     * @param secondTitle
     * @return
     */
    public static SmartDialog createTwoTitleThemeDialog(Context ctx
            , String firstTitle
            , String secondTitle
            ,int gloabDialogBg) {
        SmartDialog smartDialog = new SmartDialog(ctx);
        smartDialog
                .setRootLayoutBackground(gloabDialogBg)
                .setRootLayoutSize(500, 260)
                .setTitleMargin(SmartDialog.TITLE_SECOND, SmartDialog.MARGIN_TOP, 25)
                .setTitleLayoutAlignParent(SmartDialog.ALIGN_CENTER_IN_PARENT)// 标题布局水平垂直居中
                .setButtonLayoutVisibility(View.GONE)// 按键布局隐藏
                .setTitleTextSize(SmartDialog.TITLE_FIRST, 28)
                .setTitleTextSize(SmartDialog.TITLE_SECOND, 22)
                .setTitleText(SmartDialog.TITLE_FIRST, firstTitle)
                .setTitleText(SmartDialog.TITLE_SECOND, secondTitle);
        return smartDialog;
    }

    /**
     * 一个标题样式弹窗
     *
     * @param ctx
     * @param title
     * @return
     */
    public static SmartDialog createOneTitleThemeDialog(Context ctx, String title,int globalBg) {
        SmartDialog smartDialog = new SmartDialog(ctx);
        smartDialog
                .setRootLayoutBackground(globalBg)
                .setRootLayoutSize(500, 260)
                .setTitleLayoutAlignParent(SmartDialog.ALIGN_CENTER_IN_PARENT)
                .setButtonLayoutVisibility(View.GONE)
                .setTitleVisibility(SmartDialog.TITLE_SECOND, View.GONE)
                .enableTimeoutDismiss(true)
                .setTimeout(2000)
                .setTitleTextSize(SmartDialog.TITLE_FIRST, 28)
                .setTitleText(SmartDialog.TITLE_FIRST, title);
        return smartDialog;
    }

    /**
     * 服务顶部小窗
     * @param ctx
     * @return
     */
    public static SmallDialog createSmallDialog(Context ctx){
        SmallDialog smallDialog = new SmallDialog(ctx);
        return smallDialog;
    }
}
