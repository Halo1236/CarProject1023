package com.semisky.btcarkit.view.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.semisky.btcarkit.R;
import com.semisky.btcarkit.prenster.BTDialpadPresenter;
import com.semisky.btcarkit.utils.Logutil;

public class BTDialpadFragment extends BaseFragment<IBTDialpadView, BTDialpadPresenter<IBTDialpadView>> implements IBTDialpadView, View.OnClickListener, View.OnLongClickListener {

    private TextView tv_input_number, tv_hfp_state, tv_phoneName, tv_phoneNumber;
    private Button
            btn_del,
            btn_num_1,
            btn_num_4,
            btn_num_7,
            btn_key_asterisk,
            btn_num_2,
            btn_num_5,
            btn_num_8,
            btn_key_0_add_sign,
            btn_num_3,
            btn_num_6,
            btn_num_9,
            btn_key_pound_sign, btn_req_dial, btn_req_hung_up;


    @Override
    protected String getTagLog() {
        return Logutil.makeTagLog(BTDialpadFragment.class);
    }

    @Override
    protected BTDialpadPresenter<IBTDialpadView> createPresenter() {
        return new BTDialpadPresenter();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_dialpad;
    }

    @Override
    protected void initView() {
        tv_input_number = (TextView) mContentView.findViewById(R.id.tv_input_number);
        tv_hfp_state = (TextView) mContentView.findViewById(R.id.tv_hfp_state);
        tv_phoneName = (TextView) mContentView.findViewById(R.id.tv_phoneName);
        tv_phoneNumber = (TextView) mContentView.findViewById(R.id.tv_phoneNumber);

        btn_del = (Button) mContentView.findViewById(R.id.btn_del);
        btn_num_1 = (Button) mContentView.findViewById(R.id.btn_num_1);
        btn_num_4 = (Button) mContentView.findViewById(R.id.btn_num_4);
        btn_num_7 = (Button) mContentView.findViewById(R.id.btn_num_7);
        btn_key_asterisk = (Button) mContentView.findViewById(R.id.btn_key_asterisk);

        btn_num_2 = (Button) mContentView.findViewById(R.id.btn_num_2);
        btn_num_5 = (Button) mContentView.findViewById(R.id.btn_num_5);
        btn_num_8 = (Button) mContentView.findViewById(R.id.btn_num_8);
        btn_key_0_add_sign = (Button) mContentView.findViewById(R.id.btn_key_0_add_sign);

        btn_num_3 = (Button) mContentView.findViewById(R.id.btn_num_3);
        btn_num_6 = (Button) mContentView.findViewById(R.id.btn_num_6);
        btn_num_9 = (Button) mContentView.findViewById(R.id.btn_num_9);
        btn_key_pound_sign = (Button) mContentView.findViewById(R.id.btn_key_pound_sign);

        btn_req_dial = (Button) mContentView.findViewById(R.id.btn_req_dial);
        btn_req_hung_up = (Button) mContentView.findViewById(R.id.btn_req_hung_up);
    }

    @Override
    protected void setListener() {
        btn_del.setOnClickListener(this);
        btn_del.setOnLongClickListener(this);


        btn_num_1.setOnClickListener(this);
        btn_num_4.setOnClickListener(this);
        btn_num_7.setOnClickListener(this);
        btn_key_asterisk.setOnClickListener(this);

        btn_num_2.setOnClickListener(this);
        btn_num_5.setOnClickListener(this);
        btn_num_8.setOnClickListener(this);
        btn_key_0_add_sign.setOnClickListener(this);
        btn_key_0_add_sign.setOnLongClickListener(this);

        btn_num_3.setOnClickListener(this);
        btn_num_6.setOnClickListener(this);
        btn_num_9.setOnClickListener(this);
        btn_key_pound_sign.setOnClickListener(this);

        btn_req_dial.setOnClickListener(this);
        btn_req_hung_up.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del:
                if (isBindPresenter()) {
                    mPresenter.removeSinglePhoneNumber();
                }
                break;
            case R.id.btn_num_1:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_1.getText().toString());
                }
                break;
            case R.id.btn_num_4:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_4.getText().toString());
                }
                break;
            case R.id.btn_num_7:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_7.getText().toString());
                }
                break;
            case R.id.btn_key_asterisk:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_key_asterisk.getText().toString());
                }
                break;
            case R.id.btn_num_2:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_2.getText().toString());
                }
                break;
            case R.id.btn_num_5:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_5.getText().toString());
                }
                break;
            case R.id.btn_num_8:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_8.getText().toString());
                }
                break;
            case R.id.btn_key_0_add_sign:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber("0");
                }
                break;
            case R.id.btn_num_3:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_3.getText().toString());
                }
                break;
            case R.id.btn_num_6:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_6.getText().toString());
                }
                break;
            case R.id.btn_num_9:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_num_9.getText().toString());
                }
                break;
            case R.id.btn_key_pound_sign:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber(btn_key_pound_sign.getText().toString());
                }
                break;
            case R.id.btn_req_dial:

                if (isBindPresenter()) {
                    mPresenter.reqDialCall();
                }
                break;
            case R.id.btn_req_hung_up:
                if (isBindPresenter()) {
                    mPresenter.reqHfpTerminateCurrentCall();
                }
                break;
        }
    }

    @Override
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btn_del:
                if(isBindPresenter()){
                    mPresenter.removeAllInputPhoneNumber();
                }
                break;
            case R.id.btn_key_0_add_sign:
                if (isBindPresenter()) {
                    mPresenter.inputPhoneNumber("+");
                }
                break;
        }
        return true;
    }

    @Override
    public void onRefreshInputField(String number) {
        tv_input_number.setText(number);
    }

    @Override
    public void onRefreshHfpState(String hfpState) {
        String text = getString(R.string.bt_dialpad_hfp_state_text);
        tv_hfp_state.setText(text + hfpState);
    }

    @Override
    public void onRefreshPhoneName(String phoneName) {
        String text = getString(R.string.bt_dialpad_call_phone_name_text);
        tv_phoneName.setText(text + phoneName);
    }

    @Override
    public void onRefreshPhoneNumber(String phoneNumber) {
        String text = getString(R.string.bt_dialpad_call_phone_number_text);
        tv_phoneNumber.setText(text + phoneNumber);
    }
}
