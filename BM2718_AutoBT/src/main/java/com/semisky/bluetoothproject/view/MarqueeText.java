package com.semisky.bluetoothproject.view;

import android.content.Context;
import android.util.AttributeSet;

/**
 * Created by chenhongrui on 2018/10/12
 * <p>
 * 内容摘要: 走马灯设置
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class MarqueeText extends android.support.v7.widget.AppCompatTextView {

    public MarqueeText(Context context) {
        super(context);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
