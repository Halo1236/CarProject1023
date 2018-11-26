package com.semisky.bluetoothproject.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.entity.CallLogEntity;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.List;

/**
 * Created by chenhongrui on 2017/12/19
 * <p>
 * 内容摘要： List adapter
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class CallLogListItemAdapter extends ArrayAdapter<CallLogEntity> {

    private static final String TAG = Logger.makeTagLog(CallLogListItemAdapter.class);

    private int resourceID;
    private Context context;
    private List<CallLogEntity> ListData;

    public void setList(List<CallLogEntity> list) {
        this.ListData = list;
        notifyDataSetChanged();
        Log.d(TAG, "setList: " + list.size());
    }

    public CallLogListItemAdapter(@NonNull Context context, int resource, @NonNull List<CallLogEntity> objects) {
        super(context, resource, objects);
        ListData = objects;
        resourceID = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final CallLogEntity data = ListData.get(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivCallIconType = view.findViewById(R.id.ivCallIcon);
            viewHolder.tvCallLogName = view.findViewById(R.id.tvCallLogName);
            viewHolder.tvCallLogNumber = view.findViewById(R.id.tvCallLogNumber);
            viewHolder.tvCallTime = view.findViewById(R.id.tvCallTime);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        switch (data.getType()) {
            case CallLog.Calls.MISSED_TYPE:
                viewHolder.ivCallIconType.setBackgroundResource(R.drawable.icon_call_type_misscall);
                break;
            case CallLog.Calls.INCOMING_TYPE:
                viewHolder.ivCallIconType.setBackgroundResource(R.drawable.icon_call_type_incoming);
                break;
            case CallLog.Calls.OUTGOING_TYPE:
                viewHolder.ivCallIconType.setBackgroundResource(R.drawable.icon_call_type_callin);
                break;
            default:
                viewHolder.ivCallIconType.setBackgroundResource(R.drawable.icon_call_type_misscall);
                break;
        }

        String fullName = data.getLastName() + data.getMiddleName() + data.getFirstName();
        if (fullName.equals("")) {
            viewHolder.tvCallLogName.setText(data.getNumber());
//            viewHolder.tvCallLogName.setText(R.string.cx62_bt_unknown);
        } else {
            viewHolder.tvCallLogName.setText(fullName);
        }

        viewHolder.tvCallLogNumber.setText(data.getNumber());
        if (data.isTimeIsToday()) {
            viewHolder.tvCallTime.setText(stringNull(data.getCallTime()));
        } else {
            viewHolder.tvCallTime.setText(stringNull(data.getCallData()) + " " + stringNull(data.getCallTime()));
        }
        viewHolder.tvCallLogName.setSelected(true);
        viewHolder.tvCallLogNumber.setSelected(true);


        return view;
    }

    private class ViewHolder {
        TextView tvCallLogName, tvCallLogNumber, tvCallTime;
        ImageView ivCallIconType;
    }

    private String stringNull(String str) {
        if (null == str) {
            return "";
        }
        return str;
    }
}
