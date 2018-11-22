package com.semisky.bluetoothproject.view.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.adapter.KeyboardGradViewAdapter;
import com.semisky.bluetoothproject.constant.BtConstant;
import com.semisky.bluetoothproject.manager.BtMiddleSettingManager;
import com.semisky.bluetoothproject.presenter.BtNumberKeyboardPresenter;
import com.semisky.bluetoothproject.presenter.viewInterface.NumberKeyboardInterface;
import com.semisky.bluetoothproject.utils.Logger;

/**
 * Created by chenhongrui on 2018/8/1
 * <p>
 * 内容摘要: 数字键盘fragment
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class BtNumberKeyboardFragment extends BaseFragment<NumberKeyboardInterface,
        BtNumberKeyboardPresenter> implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = Logger.makeTagLog(BtNumberKeyboardFragment.class);

    private TextView tvCallNumber;

    @Override
    protected BtNumberKeyboardPresenter createPresenter() {
        return new BtNumberKeyboardPresenter();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            fragmentHide();
        } else {
            fragmentShow();
        }
    }

    protected void fragmentHide() {

    }

    protected void fragmentShow() {
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_number_fragment));
    }

    @Override
    public void onResume() {
        super.onResume();
        BtMiddleSettingManager.getInstance().setAppStatusInForeground(getString(R.string.bt_number_fragment));
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void initView(View view) {
        GridView gvKeyboard = view.findViewById(R.id.gvKeyboard);
        tvCallNumber = view.findViewById(R.id.tvCallNumber);
        ImageButton ibDelete = view.findViewById(R.id.ibDelete);
        ImageButton ibCall = view.findViewById(R.id.ibCall);

        ibCall.setOnClickListener(this);
        ibDelete.setOnClickListener(this);
        ibDelete.setOnLongClickListener(this);

        gvKeyboard.setAdapter(new KeyboardGradViewAdapter(getContext(), BtConstant.ArrayList.keyboardData));
        gvKeyboard.setOnItemClickListener(new AdapterItemClickListener());
        gvKeyboard.setOnItemLongClickListener(new AdapterItemLongClickListener());
    }

    @Override
    protected int getResourceId() {
        return R.layout.number_keyboard_fragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ibDelete:
                String amount = tvCallNumber.getText().toString().trim();
                if (amount.length() > 0) {
                    tvCallNumber.setText(amount.substring(0, amount.length() - 1));
                }
                break;
            case R.id.ibCall:
                String number = tvCallNumber.getText().toString().trim();
                if (!number.isEmpty()) {
                    mPresenter.reqHfpDialCall(number);
                    tvCallNumber.setText("");
                }
                break;
        }

    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == R.id.ibDelete) {
            tvCallNumber.setText("");
        }
        return true;
    }

    private class AdapterItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            TextView item = view.findViewById(R.id.tvKeyboardItem);
            String amount = tvCallNumber.getText().toString().trim();
            String number = item.getText().toString().trim();
            if (number.equals("0+")) {
                number = "0";
            }
            amount = amount + number;
            tvCallNumber.setText(amount);
        }
    }

    private class AdapterItemLongClickListener implements AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            if (position == 7) {
                String amount = tvCallNumber.getText().toString().trim();
                String number = "+";
                amount = amount + number;
                tvCallNumber.setText(amount);
            }
            return true;
        }
    }
}
