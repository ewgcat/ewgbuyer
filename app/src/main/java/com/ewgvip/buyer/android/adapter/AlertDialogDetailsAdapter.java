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
public class AlertDialogDetailsAdapter extends BaseAdapter {


    private String[] items;
    private Context context;
    LayoutInflater inflater;

    public AlertDialogDetailsAdapter(Context context, String[] items) {
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
            convertView = inflater.inflate(R.layout.alert_dialog_button_details, null);
            holder = new ViewHolder();
//            holder.text = (TextView) convertView.findViewById(R.id.button);
            holder.tv_order_pay = (TextView) convertView.findViewById(R.id.tv_order_pay);
            holder.tv_wei_pay = (TextView) convertView.findViewById(R.id.tv_wei_pay);
            holder.tv_deliver_price = (TextView) convertView.findViewById(R.id.tv_deliver_price);
            holder.tv_dingpay_time = (TextView) convertView.findViewById(R.id.tv_dingpay_time);
            holder.tv_weipay_startime = (TextView) convertView.findViewById(R.id.tv_weipay_startime);
            holder.tv_weipay_stoptime = (TextView) convertView.findViewById(R.id.tv_weipay_stoptime);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

//        holder.text.setText(items[position]);

        holder.tv_order_pay.setText(items[0]+".0");
        holder.tv_wei_pay.setText(items[1]);
        holder.tv_dingpay_time.setText(items[2]);
        holder.tv_weipay_startime.setText(items[2]);
        holder.tv_weipay_stoptime .setText(items[3]);
        holder.tv_deliver_price.setText(items[4]);
        return convertView;
    }

    static class ViewHolder {
        //        TextView text;
        private TextView tv_order_pay;
        private TextView tv_wei_pay;
        private TextView tv_deliver_price;
        private TextView tv_dingpay_time;
        private TextView tv_weipay_startime;
        private TextView tv_weipay_stoptime;
    }
}
