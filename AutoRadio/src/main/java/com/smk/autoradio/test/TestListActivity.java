package com.smk.autoradio.test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.smk.autoradio.R;
import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.model.IRadioDataModel;
import com.smk.autoradio.model.RadioDataModel;

import java.util.List;

public class TestListActivity extends Activity {
    private IRadioDataModel mRadioDataModel;
    private TestListAdapter mTestListAdapter;
    private ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_list);

        mRadioDataModel = new RadioDataModel();
        mTestListAdapter = new TestListAdapter(this);
        lv = (ListView)findViewById(R.id.lv);
        lv.setAdapter(mTestListAdapter);

        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TestListActivity.this.finish();
            }
        });
        findViewById(R.id.btn_query_fm_full_search_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChannelInfo> channelInfoList = mRadioDataModel.getAllFullSearchChannelList(RadioConst.CHANNEL_TYPE_FM);
                mTestListAdapter.update(channelInfoList);
                Toast.makeText(TestListActivity.this, "查询FM全搜频道数量："+(null != channelInfoList?channelInfoList.size():0), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_query_am_full_search_channel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChannelInfo> channelInfoList = mRadioDataModel.getAllFullSearchChannelList(RadioConst.CHANNEL_TYPE_AM);
                mTestListAdapter.update(channelInfoList);
                Toast.makeText(TestListActivity.this, "查询AM全搜频道数量："+(null != channelInfoList?channelInfoList.size():0), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_query_fm_favorite_list_lab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChannelInfo> channelInfoList = mRadioDataModel.getAllFavoriteChannelList(RadioConst.CHANNEL_TYPE_FM);
                mTestListAdapter.update(channelInfoList);
                Toast.makeText(TestListActivity.this, "查询FM收藏频道数量："+(null != channelInfoList?channelInfoList.size():0), Toast.LENGTH_SHORT).show();
            }
        });
        findViewById(R.id.btn_query_am_favorite_list_lab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<ChannelInfo> channelInfoList = mRadioDataModel.getAllFavoriteChannelList(RadioConst.CHANNEL_TYPE_AM);
                mTestListAdapter.update(channelInfoList);
                Toast.makeText(TestListActivity.this, "查询AM收藏频道数量："+(null != channelInfoList?channelInfoList.size():0), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRadioDataModel = null;
    }
}
