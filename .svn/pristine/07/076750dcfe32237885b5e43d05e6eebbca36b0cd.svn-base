package com.ewgvip.buyer.android.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;

/**
 * Created by Administrator on 2015/12/22.
 */
public class AlertDialogShowAdapter extends BaseAdapter {


    private String[] items;
    private Context context;
    LayoutInflater inflater;

    public AlertDialogShowAdapter(Context context, String[] items) {
        this.items = items;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 1;
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
            convertView = inflater.inflate(R.layout.alert_dialog_button_show, null);
            holder = new ViewHolder();
//            holder.text = (TextView) convertView.findViewById(R.id.button);
            holder.tv_details_invoice_1 = (TextView) convertView.findViewById(R.id.tv_details_invoice_1);
            holder.tv_details_invoice_2 = (TextView) convertView.findViewById(R.id.tv_details_invoice_2);
            holder.tv_details_invoice_3 = (TextView) convertView.findViewById(R.id.tv_details_invoice_3);
            holder.tv_details_invoice_4 = (TextView) convertView.findViewById(R.id.tv_details_invoice_4);
            holder.tv_details_invoice_5 = (TextView) convertView.findViewById(R.id.tv_details_invoice_5);
            holder.tv_details_invoice_0 = (TextView) convertView.findViewById(R.id.tv_details_invoice_0);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_details_invoice_1.setText(items[1]);
        holder.tv_details_invoice_2.setText(items[2]);
        holder.tv_details_invoice_3.setText(items[3]);
        holder.tv_details_invoice_4.setText(items[4]);
        holder.tv_details_invoice_5.setText(items[5]);
        holder.tv_details_invoice_0.setText(items[0]);
//        holder.text.setText(items[position]);
        return convertView;
    }

    static class ViewHolder {
//        TextView text;
private TextView tv_details_invoice_1, tv_details_invoice_2, tv_details_invoice_3, tv_details_invoice_4, tv_details_invoice_5, tv_details_invoice_0;
    }
}
