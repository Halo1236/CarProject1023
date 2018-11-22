package com.smk.dialogdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.util.TypedValue;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MultiTitleDialog extends Dialog {

    private Handler _handler = new Handler(Looper.getMainLooper());

    private TextView
            tv_title_one,
            tv_title_two;
    private RelativeLayout root_layout;

    private boolean mEnableTimeoutRunTask = false;// 是否执行超时任务
    private long mDelayTime = 0;

    public MultiTitleDialog(Context context) {
        super(context, R.style.OneTitleDialog_Theme);
        View contentView = View.inflate(context, R.layout.dialog_multi_title, null);
        setContentView(contentView);
        initViews();
    }

    // 初始化控件
    private void initViews() {
        root_layout = (RelativeLayout) findViewById(R.id.root_layout);
        tv_title_one = (TextView) findViewById(R.id.tv_title_one);
        tv_title_two = (TextView) findViewById(R.id.tv_title_two);
        // 默认标题颜色
        tv_title_one.setTextColor(Color.WHITE);
        tv_title_two.setTextColor(Color.WHITE);
        // 设置默认控件显示状态
        tv_title_two.setVisibility(View.GONE);
        // 设置默认控件位置
        RelativeLayout.LayoutParams titleTwoLayoutParams = (RelativeLayout.LayoutParams) tv_title_two.getLayoutParams();
        titleTwoLayoutParams.addRule(RelativeLayout.BELOW, R.id.tv_title_one);
        tv_title_two.setLayoutParams(titleTwoLayoutParams);
    }

    private Runnable mTimeoutRunTaskRunnable = new Runnable() {
        @Override
        public void run() {
            MultiTitleDialog.this.dismiss();
        }
    };

    /**
     * 设置弹窗全局背景
     *
     * @param resId
     * @return
     */
    public MultiTitleDialog setBackgroudResourceByRootLayout(int resId) {
        root_layout.setBackgroundResource(resId);
        return this;
    }

    /**
     * 设置第二标题显示状态
     *
     * @param visibility
     * @return
     */
    public MultiTitleDialog setVisibilityBySecondTitle(int visibility) {
        tv_title_two.setVisibility(View.VISIBLE);
        return this;
    }

    /**
     * 设置弹窗尺寸
     *
     * @param width  宽
     * @param height
     * @return 高
     */
    public MultiTitleDialog setRootLayoutSize(int width, int height) {
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) root_layout.getLayoutParams();
        rlParams.width = width;
        rlParams.height = height;
        root_layout.setLayoutParams(rlParams);
        return this;
    }


    /**
     * 设置首个弹窗标题内容
     *
     * @param title 标题内容
     * @return
     */
    public MultiTitleDialog setTextByFirstTitle(String title) {
        tv_title_one.setText(title);
        return this;
    }

    /**
     * 设置第二个弹窗标题内容
     *
     * @param title 标题内容
     * @return
     */
    public MultiTitleDialog setTextBySecondTitle(String title) {
        tv_title_two.setText(title);
        return this;
    }

    /**
     * 设置首个标题字体尺寸
     *
     * @param size 字体尺寸
     * @return
     */
    public MultiTitleDialog setTextSizeByFirstTitle(int size) {
        tv_title_one.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    /**
     * 设置第二个标题字体尺寸
     *
     * @param size 字体尺寸
     * @return
     */
    public MultiTitleDialog setTextSizeBySecondTitle(int size) {
        tv_title_two.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        return this;
    }

    /**
     * 设置首个标题字体颜色
     *
     * @param color
     * @return
     */
    public MultiTitleDialog setTextColorByFirstTitle(int color) {
        tv_title_one.setTextColor(color);
        return this;
    }

    /**
     * 设置第二个标题字体颜色
     *
     * @param color
     * @return
     */
    public MultiTitleDialog setTextColorBySecondTitle(int color) {
        tv_title_two.setTextColor(color);
        return this;
    }

    /**
     * 设置首个标题控件在布局中水平垂直居中
     *
     * @param enable
     * @return
     */
    public MultiTitleDialog enableCenterInParentByFirstTitle(boolean enable) {
        if (!enable) {
            return this;
        }
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv_title_one.getLayoutParams();
        rlParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        tv_title_one.setLayoutParams(rlParams);
        return this;
    }

    /**
     * 设置首个标题控件在布局中水平居中
     *
     * @param enable
     * @return
     */
    public MultiTitleDialog enableCenterHorizontalByFirstTitle(boolean enable) {
        if (!enable) {
            return this;
        }
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv_title_one.getLayoutParams();
        rlParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_title_one.setLayoutParams(rlParams);
        return this;
    }

    /**
     * 设置首个标题控件在布局中水平居中
     *
     * @param enable
     * @return
     */
    public MultiTitleDialog enableCenterHorizontalBySecondTitle(boolean enable) {
        if (!enable) {
            return this;
        }
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv_title_two.getLayoutParams();
        rlParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        tv_title_two.setLayoutParams(rlParams);
        return this;
    }

    /**
     * 设置首个标题控件与父布局顶部间距
     *
     * @param marginTop
     * @return
     */
    public MultiTitleDialog setMarginTopByFirstTitle(int marginTop) {
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv_title_one.getLayoutParams();
        rlParams.topMargin = marginTop;
        tv_title_one.setLayoutParams(rlParams);
        return this;
    }

    /**
     * 设置第二个标题控件与第一个标题顶部间距
     *
     * @param marginTop
     * @return
     */
    public MultiTitleDialog setMarginTopBySecondTitle(int marginTop) {
        RelativeLayout.LayoutParams rlParams = (RelativeLayout.LayoutParams) tv_title_two.getLayoutParams();
        rlParams.topMargin = marginTop;
        tv_title_two.setLayoutParams(rlParams);
        return this;
    }

    /**
     * 设置显示超时自动关闭弹窗开关状态
     *
     * @param enable
     * @return
     */
    public MultiTitleDialog enableShowTimeoutToDismiss(boolean enable) {
        this.mEnableTimeoutRunTask = enable;
        return this;
    }

    /**
     * 设置显示超时自动关闭弹窗时间
     *
     * @param delayTime
     * @return
     */
    public MultiTitleDialog setTimeout(long delayTime) {
        this.mDelayTime = delayTime;
        return this;
    }

    @Override
    public void show() {
        super.show();
        if (mEnableTimeoutRunTask && mDelayTime >= 1000) {
            _handler.removeCallbacksAndMessages(null);
            _handler.postDelayed(mTimeoutRunTaskRunnable, mDelayTime);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        _handler.removeCallbacksAndMessages(null);
    }
}
