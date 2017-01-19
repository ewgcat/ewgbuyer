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
 * Title: GoodsListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description: 商品列表适配器
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
 * @author hezeng
 * @version 1.0
 * @date 2014-7-29
 */
public class GoodsListAdapter extends BaseAdapter {
    BaseActivity mActivity;
    LayoutInflater inflater;
    private List list;

    public GoodsListAdapter(BaseActivity mActivity, List list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_list, null);
            holder = new ViewHolder();
            holder.titleView = (TextView) convertView.findViewById(R.id.goodslist_title);
            holder.imageView = (SimpleDraweeView) convertView.findViewById(R.id.goodslist_img);
            holder.priceView = (TextView) convertView.findViewById(R.id.goodslist_price);
            holder.goodslist_evaluate = (TextView) convertView.findViewById(R.id.goodslist_evaluate);
            holder.discount_type = (TextView) convertView.findViewById(R.id.discount_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map map = (Map) list.get(position);
        holder.titleView.setText(map.get("goods_name").toString());
        holder.titleView.setTag(map.get("id"));
        holder.priceView.setText("￥" + mActivity.moneytodouble( map.get("goods_current_price").toString()));
        BaseActivity.displayImage(map.get("goods_main_photo").toString(), holder.imageView);
        holder.goodslist_evaluate.setText(map.get("goods_salenum").toString() + "人已买");
        holder.goodslist_evaluate.setTextSize(12);
        holder.goodslist_evaluate.setTextColor(mActivity.getResources().getColor(R.color.group_shopping_sale));
        if (map.get("status").toString().length() > 0) {
            holder.discount_type.setText(map.get("status").toString());
            holder.discount_type.setVisibility(View.VISIBLE);
        } else {
            holder.discount_type.setVisibility(View.GONE);
        }
        return convertView;
    }

    private static class ViewHolder {
        private SimpleDraweeView imageView;
        private TextView titleView;
        private TextView priceView;
        private TextView goodslist_evaluate;
        private TextView discount_type;
    }

}
