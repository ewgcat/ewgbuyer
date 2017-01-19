package com.ewgvip.buyer.android.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/22.
 */
public class AlertDialogAdapter extends BaseAdapter {


    private String[] items;
    private Context context;
    LayoutInflater inflater;

    public AlertDialogAdapter(Context context, String[] items) {
        this.items = items;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alert_dialog_button, null);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.button);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(items[position]);
        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}
