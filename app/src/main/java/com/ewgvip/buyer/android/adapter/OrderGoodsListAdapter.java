package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: OrderGoodsListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description:
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author lixiaoyang
 * @version 1.0
 * @date 2014-8-21
 */
public class OrderGoodsListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List myList;


    public OrderGoodsListAdapter(Context context, List myList) {
        this.myList = myList;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return myList.size();
    }

    @Override
    public Object getItem(int position) {

        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_normal_goods, null);
            holder = new ViewHolder();
            holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.count = (TextView) convertView.findViewById(R.id.count);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.view=convertView.findViewById(R.id.view);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map mymap = (Map) myList.get(position);

        holder.name.setText(mymap.get("goods_name").toString());
        holder.price.setText(mymap.get("goods_price").toString());
        String str = "";
        if (mymap.containsKey("goods_spec")) {
            str += mymap.get("goods_spec").toString();
        }
        if (mymap.containsKey("goods_count")
                && mymap.get("goods_count").toString().length() > 0) {
            str += "×" + mymap.get("goods_count").toString();
        }
        holder.count.setText(str);


        BaseActivity.displayImage(mymap.get("goods_main_photo").toString(),
                holder.img);

        if(!mymap.get("goods_price").toString().equals("组合配件")||mymap.get("goods_price").toString().equals("赠品"))
        {
            if(position!=0)
            {
                holder.view.setVisibility(View.VISIBLE);
            }

        }else {
            holder.view.setVisibility(View.GONE);
        }

        return convertView;
    }

    public static class ViewHolder {
        public SimpleDraweeView img;
        public TextView name;
        public TextView count;
        public TextView price;
        public  View view;
    }

}
