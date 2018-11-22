package com.smk.bt.views.custom;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.smk.bt.R;

/**
 * Created by LiuYong on 2018/10/12.
 */

public class BTBottombar extends LinearLayout implements OnClickListener {
    public static final int TAB_DEVICE_LIST = 0;
    public static final int TAB_DIALPAD = 1;
    public static final int TAB_CONSTRACTS_LIST = 2;
    public static final int TAB_CALLLOG_LIST = 3;
    public static final int TAB_MUSIC = 4;
    public static final int TAB_SETTINGS = 5;

    private ImageView
            iv_list_tab_keyboard,
            iv_list_tab_contracts,
            iv_list_tab_callrecord,
            iv_list_tab_music,
            iv_list_tab_settings,
            iv_list_tab_history,
            iv_list_tab_settings2;
    private View
            tab_bt_connected_layout,
            tab_bt_disconnected_layout;
    private OnTabChangeListener mOnTabChangeListener;

    public interface OnTabChangeListener {
        void onTabChange(int tabFlag);
    }

    public BTBottombar(Context context) {
        this(context, null);
    }

    public BTBottombar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BTBottombar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setContentView(context);
        initViews();
    }

    private void setContentView(Context ctx) {
        LayoutInflater.from(ctx).inflate(R.layout.custom_bt_bottom_bar, this);
    }

    private void initViews() {

        iv_list_tab_keyboard = (ImageView) findViewById(R.id.iv_list_tab_keyboard);
        iv_list_tab_contracts = (ImageView) findViewById(R.id.iv_list_tab_contracts);
        iv_list_tab_callrecord = (ImageView) findViewById(R.id.iv_list_tab_callrecord);
        iv_list_tab_music = (ImageView) findViewById(R.id.iv_list_tab_music);
        iv_list_tab_settings = (ImageView) findViewById(R.id.iv_list_tab_settings);

        iv_list_tab_history = (ImageView) findViewById(R.id.iv_list_tab_history);
        iv_list_tab_settings2 = (ImageView) findViewById(R.id.iv_list_tab_settings2);

        tab_bt_connected_layout = findViewById(R.id.tab_bt_connected_layout);
        tab_bt_disconnected_layout = findViewById(R.id.tab_bt_disconnected_layout);

        iv_list_tab_keyboard.setOnClickListener(this);
        iv_list_tab_contracts.setOnClickListener(this);
        iv_list_tab_callrecord.setOnClickListener(this);
        iv_list_tab_music.setOnClickListener(this);
        iv_list_tab_settings.setOnClickListener(this);

        iv_list_tab_history.setOnClickListener(this);
        iv_list_tab_settings2.setOnClickListener(this);
    }

    public void setOnTabChangeListener(OnTabChangeListener l) {
        this.mOnTabChangeListener = l;
    }

    public void unregisterCallback() {
        this.mOnTabChangeListener = null;
    }

    @Override
    public void onClick(View v) {
        int tabFlag = -1;
        switch (v.getId()) {
            case R.id.iv_list_tab_history:
                tabFlag = TAB_DEVICE_LIST;
                break;
            case R.id.iv_list_tab_keyboard:
                tabFlag = TAB_DIALPAD;
                break;
            case R.id.iv_list_tab_contracts:
                tabFlag = TAB_CONSTRACTS_LIST;
                break;
            case R.id.iv_list_tab_callrecord:
                tabFlag = TAB_CALLLOG_LIST;
                break;
            case R.id.iv_list_tab_music:
                tabFlag = TAB_MUSIC;
                break;
            case R.id.iv_list_tab_settings:
                tabFlag = TAB_SETTINGS;
                break;
        }

        if (tabFlag != -1) {
            if (null != mOnTabChangeListener) {
                mOnTabChangeListener.onTabChange(tabFlag);
            }
        }
    }

    /**
     * {@link BTBottombar#TAB_DEVICE_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CALLLOG_LIST} and
     * {@link BTBottombar#TAB_MUSIC} and
     * {@link BTBottombar#TAB_SETTINGS} and
     *
     * @param tabFlag
     * @param enable
     * @return
     */
    public BTBottombar onEnableShowTabWith(int tabFlag, boolean enable) {
        switch (tabFlag) {
            case TAB_DEVICE_LIST:
                iv_list_tab_history.setVisibility(enable ? VISIBLE : GONE);
                break;
            case TAB_DIALPAD:
                iv_list_tab_keyboard.setVisibility(enable ? VISIBLE : GONE);
                break;
            case TAB_CONSTRACTS_LIST:
                iv_list_tab_contracts.setVisibility(enable ? VISIBLE : GONE);
                break;
            case TAB_CALLLOG_LIST:
                iv_list_tab_callrecord.setVisibility(enable ? VISIBLE : GONE);
                break;
            case TAB_MUSIC:
                iv_list_tab_music.setVisibility(enable ? VISIBLE : GONE);
                break;
            case TAB_SETTINGS:
                iv_list_tab_settings.setVisibility(enable ? VISIBLE : GONE);
                break;
        }

        return this;
    }

    /**
     * {@link BTBottombar#TAB_DEVICE_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CALLLOG_LIST} and
     * {@link BTBottombar#TAB_MUSIC} and
     * {@link BTBottombar#TAB_SETTINGS} and
     *
     * @param tabFlag
     * @param resId
     * @return
     */
    public BTBottombar setImageResourceWith(int tabFlag, int resId) {
        switch (tabFlag) {
            case TAB_DEVICE_LIST:
                iv_list_tab_history.setImageResource(resId);
                break;
            case TAB_DIALPAD:
                iv_list_tab_keyboard.setImageResource(resId);
                break;
            case TAB_CONSTRACTS_LIST:
                iv_list_tab_contracts.setImageResource(resId);
                break;
            case TAB_CALLLOG_LIST:
                iv_list_tab_callrecord.setImageResource(resId);
                break;
            case TAB_MUSIC:
                iv_list_tab_music.setImageResource(resId);
                break;
            case TAB_SETTINGS:
                iv_list_tab_settings.setImageResource(resId);
                break;
        }
        return this;
    }

    /**
     * {@link BTBottombar#TAB_DEVICE_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CALLLOG_LIST} and
     * {@link BTBottombar#TAB_MUSIC} and
     * {@link BTBottombar#TAB_SETTINGS} and
     *
     * @param tabFlag
     * @param resId
     * @return
     */
    public BTBottombar setBackgroundResourceWith(int tabFlag, int resId) {
        switch (tabFlag) {
            case TAB_DEVICE_LIST:
                iv_list_tab_history.setBackgroundResource(resId);
                break;
            case TAB_DIALPAD:
                iv_list_tab_keyboard.setBackgroundResource(resId);
                break;
            case TAB_CONSTRACTS_LIST:
                iv_list_tab_contracts.setBackgroundResource(resId);
                break;
            case TAB_CALLLOG_LIST:
                iv_list_tab_callrecord.setBackgroundResource(resId);
                break;
            case TAB_MUSIC:
                iv_list_tab_music.setBackgroundResource(resId);
                break;
            case TAB_SETTINGS:
                iv_list_tab_settings.setBackgroundResource(resId);
                break;
        }
        return this;
    }

    /**
     * {@link BTBottombar#TAB_DEVICE_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CONSTRACTS_LIST} and
     * {@link BTBottombar#TAB_CALLLOG_LIST} and
     * {@link BTBottombar#TAB_MUSIC} and
     * {@link BTBottombar#TAB_SETTINGS} and
     *
     * @param tabFlag
     * @param enable
     * @return
     */
    public BTBottombar setSelectedWith(int tabFlag, boolean enable) {
        switch (tabFlag) {
            case TAB_DEVICE_LIST:
                iv_list_tab_history.setSelected(enable);
                break;
            case TAB_DIALPAD:
                iv_list_tab_keyboard.setSelected(enable);
                break;
            case TAB_CONSTRACTS_LIST:
                iv_list_tab_contracts.setSelected(enable);
                break;
            case TAB_CALLLOG_LIST:
                iv_list_tab_callrecord.setSelected(enable);
                break;
            case TAB_MUSIC:
                iv_list_tab_music.setSelected(enable);
                break;
            case TAB_SETTINGS:
                iv_list_tab_settings.setSelected(enable);
                break;
        }
        return this;
    }

    /**
     * 显示蓝牙已连接的导航栏
     *
     * @param enable
     * @return
     */
    public BTBottombar onShowBTConnectedBottombarEnable(boolean enable) {
        tab_bt_connected_layout.setVisibility(enable ? VISIBLE : GONE);
        tab_bt_disconnected_layout.setVisibility(enable ? GONE : VISIBLE);
        return this;
    }


}
