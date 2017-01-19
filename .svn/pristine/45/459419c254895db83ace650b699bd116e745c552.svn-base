package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/14.  余额里账单详情的Adapter
 */
public class BillingAdapter extends BaseAdapter {
    List list;
    BaseActivity mActivity;
    LayoutInflater inflater;

    public BillingAdapter(List list, BaseActivity mActivity) {
        this.list = list;
        this.mActivity = mActivity;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = null;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.item_detaillist, null);
            holder.tv_billingdate = (TextView) view.findViewById(R.id.textview_detaildate);
            holder.tv_billingname = (TextView) view.findViewById(R.id.textview_detailname);
            holder.tv_billingvalue = (TextView) view.findViewById(R.id.textview_detailvalue);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final Map map = (Map) list.get(i);
        holder.tv_billingname.setText(map.get("type").toString());
        holder.tv_billingdate.setText(map.get("time").toString());
        holder.tv_billingvalue.setText(map.get("amount").toString());

        return view;
    }

    public static class ViewHolder {
        TextView tv_billingname;
        TextView tv_billingdate;
        TextView tv_billingvalue;
    }
}
