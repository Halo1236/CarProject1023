package com.semisky.bluetoothproject.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.semisky.bluetoothproject.R;
import com.semisky.bluetoothproject.entity.ContactsEntity;
import com.semisky.bluetoothproject.utils.Logger;

import java.util.List;

/**
 * Created by chenhongrui on 2018/8/14
 * <p>
 * 内容摘要:
 * 版权所有：Semisky
 * 修改内容：
 * 修改日期
 */
public class ContactsListItemAdapter extends ArrayAdapter<ContactsEntity> {

    private static final String TAG = Logger.makeTagLog(ContactsListItemAdapter.class);

    private int resourceID;
    private Context context;
    private List<ContactsEntity> ListData;

    public void setList(List<ContactsEntity> list) {
        this.ListData = list;
        notifyDataSetChanged();
    }

    public ContactsListItemAdapter(@NonNull Context context, int resource, @NonNull List<ContactsEntity> objects) {
        super(context, resource, objects);
        ListData = objects;
        resourceID = resource;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final ContactsEntity data = ListData.get(position);

        View view;
        ViewHolder viewHolder;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.ivCallContactIcon = view.findViewById(R.id.ivCallContactIcon);
            viewHolder.tvCallContactName = view.findViewById(R.id.tvCallContactName);
            viewHolder.tvCallContactNumber = view.findViewById(R.id.tvCallContactNumber);

            view.setTag(viewHolder);

        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.ivCallContactIcon.setBackgroundResource(R.drawable.icon_contact_item);

        String fullName = data.getLastName() + data.getMiddleName() + data.getFirstName();
        if (fullName.equals("")) {
            viewHolder.tvCallContactName.setText(data.getNumber());
        } else {
            viewHolder.tvCallContactName.setText(fullName);
        }

        viewHolder.tvCallContactNumber.setText(data.getNumber());
        viewHolder.tvCallContactNumber.setSelected(true);
        viewHolder.tvCallContactName.setSelected(true);

        return view;
    }

    private class ViewHolder {
        TextView tvCallContactName, tvCallContactNumber;
        ImageView ivCallContactIcon;
    }
}
