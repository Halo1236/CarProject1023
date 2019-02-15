package com.smk.autoradio.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.smk.autoradio.R;
import com.smk.autoradio.aidl.ChannelInfo;
import com.smk.autoradio.constants.RadioConst;
import com.smk.autoradio.model.IRadioDataModel;
import com.smk.autoradio.model.RadioDataModel;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends Activity implements View.OnClickListener {
    private IRadioDataModel mRadioDataModel;

    private Button
            btn_enter_list,
            btn_batch_add_fm_channel,
            btn_batch_add_am_channel,
            btn_favorite_fm_channel,
            btn_favorite_am_channel,
            btn_del_full_search_fm_channel,
            btn_del_full_search_am_channel,
            btn_del_favorite_fm_channel,
            btn_del_favorite_am_channel,
            btn_del_all_full_search,
            btn_del_all_favorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mRadioDataModel = new RadioDataModel();

        btn_enter_list = (Button) findViewById(R.id.btn_enter_list);
        btn_batch_add_fm_channel = (Button) findViewById(R.id.btn_batch_add_fm_channel);
        btn_batch_add_am_channel = (Button) findViewById(R.id.btn_batch_add_am_channel);
        btn_favorite_fm_channel = (Button) findViewById(R.id.btn_favorite_fm_channel);
        btn_favorite_am_channel = (Button) findViewById(R.id.btn_favorite_am_channel);
        btn_del_full_search_fm_channel = (Button) findViewById(R.id.btn_del_full_search_fm_channel);
        btn_del_full_search_am_channel = (Button) findViewById(R.id.btn_del_full_search_am_channel);
        btn_del_favorite_fm_channel = (Button) findViewById(R.id.btn_del_favorite_fm_channel);
        btn_del_favorite_am_channel = (Button) findViewById(R.id.btn_del_favorite_am_channel);
        btn_del_all_full_search = (Button) findViewById(R.id.btn_del_all_full_search);
        btn_del_all_favorite = (Button) findViewById(R.id.btn_del_all_favorite);

        btn_enter_list.setOnClickListener(this);
        btn_batch_add_fm_channel.setOnClickListener(this);
        btn_batch_add_am_channel.setOnClickListener(this);
        btn_favorite_fm_channel.setOnClickListener(this);
        btn_favorite_am_channel.setOnClickListener(this);
        btn_del_full_search_fm_channel.setOnClickListener(this);
        btn_del_full_search_am_channel.setOnClickListener(this);
        btn_del_favorite_fm_channel.setOnClickListener(this);
        btn_del_favorite_am_channel.setOnClickListener(this);
        btn_del_all_full_search.setOnClickListener(this);
        btn_del_all_favorite.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_enter_list://进入列表
                Intent intent = new Intent();
                intent.setClass(this, TestListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_batch_add_fm_channel://批量存储FM频道
                if (null != mRadioDataModel) {
                    long result = mRadioDataModel.addBatchSearchChannelToDB(getFullSearchFmChannelList());
                    Toast.makeText(TestActivity.this, "批量存储FM频道:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_batch_add_am_channel://批量存储AM频道
                if (null != mRadioDataModel) {
                    long result = mRadioDataModel.addBatchSearchChannelToDB(getFullSearchAmChannelList());
                    Toast.makeText(TestActivity.this, "批量存储AM频道:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_favorite_fm_channel://收藏FM频道
                if (null != mRadioDataModel) {
                    ChannelInfo info = getFmFavoriteChannelInfo();
//                    boolean isFavorite = mRadioDataModel.isFavorite(info.getChannelType(), info.getChannel());
//                    if (!isFavorite) {
                        long result = mRadioDataModel.addSingleChannelInfoToFavorite(getFmFavoriteChannelInfo());
                        Toast.makeText(TestActivity.this, "存储收藏FM频道:" + result, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestActivity.this, "当前FM频道已收藏！！！", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
            case R.id.btn_favorite_am_channel://收藏AM频道
                if (null != mRadioDataModel) {
                    ChannelInfo info = getAmFavoriteChannelInfo();
//                    boolean isFavorite = mRadioDataModel.isFavorite(info.getChannelType(), info.getChannel());
//                    if (!isFavorite) {
                        long result = mRadioDataModel.addSingleChannelInfoToFavorite(getAmFavoriteChannelInfo());
                        Toast.makeText(TestActivity.this, "存储收藏AM频道:" + result, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(TestActivity.this, "当前AM频道已收藏！！！", Toast.LENGTH_SHORT).show();
//                    }
                }
                break;
            case R.id.btn_del_full_search_fm_channel:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteFullSeachChannel(RadioConst.CHANNEL_TYPE_FM);
                    Toast.makeText(TestActivity.this, "删除FM全搜:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_del_full_search_am_channel:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteFullSeachChannel(RadioConst.CHANNEL_TYPE_AM);
                    Toast.makeText(TestActivity.this, "删除AM全搜:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_del_favorite_fm_channel:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteFavoriteChannel(RadioConst.CHANNEL_TYPE_FM);
                    Toast.makeText(TestActivity.this, "删除FM收藏:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_del_favorite_am_channel:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteFavoriteChannel(RadioConst.CHANNEL_TYPE_AM);
                    Toast.makeText(TestActivity.this, "删除AM收藏:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_del_all_full_search:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteAllFullSeachChannel();
                    Toast.makeText(TestActivity.this, "删除所有频道类型全搜清单:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_del_all_favorite:
                if (null != mRadioDataModel) {
                    int result = mRadioDataModel.deleteAllFavoriteChannel();
                    Toast.makeText(TestActivity.this, "删除所有频道类型收藏清单:" + result, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRadioDataModel = null;
    }


    // FM频道模拟数据
    private List<ChannelInfo> getFullSearchFmChannelList() {
        List<ChannelInfo> list = new ArrayList<ChannelInfo>();
        list.add(new ChannelInfo(8750, RadioConst.CHANNEL_TYPE_FM, 80, false));
        list.add(new ChannelInfo(8755, RadioConst.CHANNEL_TYPE_FM, 82, false));
        list.add(new ChannelInfo(8760, RadioConst.CHANNEL_TYPE_FM, 130, false));
        list.add(new ChannelInfo(8765, RadioConst.CHANNEL_TYPE_FM, 81, false));
        list.add(new ChannelInfo(8770, RadioConst.CHANNEL_TYPE_FM, 83, false));
        list.add(new ChannelInfo(8775, RadioConst.CHANNEL_TYPE_FM, 84, false));
        return list;
    }

    //AM频道模拟数据
    private List<ChannelInfo> getFullSearchAmChannelList() {
        List<ChannelInfo> list = new ArrayList<ChannelInfo>();
        list.add(new ChannelInfo(531, RadioConst.CHANNEL_TYPE_AM, 80, false));
        list.add(new ChannelInfo(540, RadioConst.CHANNEL_TYPE_AM, 82, false));
        list.add(new ChannelInfo(549, RadioConst.CHANNEL_TYPE_AM, 130, false));
        list.add(new ChannelInfo(558, RadioConst.CHANNEL_TYPE_AM, 81, false));
        list.add(new ChannelInfo(567, RadioConst.CHANNEL_TYPE_AM, 83, false));
        list.add(new ChannelInfo(573, RadioConst.CHANNEL_TYPE_AM, 84, false));
        return list;
    }

    // 获取FM收藏模拟数据
    private ChannelInfo getFmFavoriteChannelInfo() {
        return new ChannelInfo(8770, RadioConst.CHANNEL_TYPE_FM, -1, true);
    }

    // 获取AM收藏模拟数据
    private ChannelInfo getAmFavoriteChannelInfo() {
        return new ChannelInfo(573, RadioConst.CHANNEL_TYPE_AM, -1, true);
    }
}
