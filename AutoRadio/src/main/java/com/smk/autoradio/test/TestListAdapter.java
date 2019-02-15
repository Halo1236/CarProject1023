package com.smk.autoradio.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.smk.autoradio.R;
import com.smk.autoradio.aidl.ChannelInfo;

import java.util.ArrayList;
import java.util.List;

public class TestListAdapter extends BaseAdapter {
    private Context mCtx;
    private List<ChannelInfo> mList;

    public TestListAdapter(Context ctx){
        this.mCtx = ctx;
        this.mList = new ArrayList<ChannelInfo>();
    }

    public void update(List<ChannelInfo> list){
        if(null != list){
            mList.clear();
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(null == convertView){
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.test_list_item,null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }

        holder.tv_channel.setText(mList.get(position).getChannel()+"");
        return convertView;
    }


    class ViewHolder{
        private TextView tv_channel;
        ViewHolder(View v){
            this.tv_channel = (TextView) v.findViewById(R.id.tv_channel);
        }
    }
}
