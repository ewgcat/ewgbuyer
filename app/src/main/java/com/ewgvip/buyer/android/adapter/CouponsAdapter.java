package com.ewgvip.buyer.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/17.
 * 优惠券列表
 */
public class CouponsAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<Map> couponsList;//优惠券信息集合
    private String type;//0从订单页1个人中心

    public CouponsAdapter(BaseActivity mActivity, List<Map> couponsList, String type) {
        this.mActivity = mActivity;
        this.couponsList = couponsList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return couponsList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_coupons, null);
            viewHolder = new ViewHolder();
            viewHolder.layout_item_coupons = (RelativeLayout) view.findViewById(R.id.layout_item_coupons);
            viewHolder.tv_item_coupons_rmb = (TextView) view.findViewById(R.id.tv_item_coupons_rmb);
            viewHolder.tv_item_coupons_price = (TextView) view.findViewById(R.id.tv_item_coupons_price);
            viewHolder.tv_item_coupons_name = (TextView) view.findViewById(R.id.tv_item_coupons_name);
            viewHolder.tv_item_coupons_limit = (TextView) view.findViewById(R.id.tv_item_coupons_limit);
            viewHolder.tv_item_coupons_date = (TextView) view.findViewById(R.id.tv_item_coupons_date);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (i == couponsList.size() - 1) {
            viewHolder.layout_item_coupons.setVisibility(View.VISIBLE);
            viewHolder.layout_item_coupons.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_getCoupon();
                }
            });
        } else {
            viewHolder.layout_item_coupons.setVisibility(View.GONE);
        }
        if (couponsList.get(i).get("coupon_status").equals("0")) {
            if (type.equals("0")) {
                viewHolder.layout_item_coupons.setVisibility(View.GONE);
            } else {
            }
        } else {
            viewHolder.tv_item_coupons_rmb.setTextColor(mActivity.getResources().getColor(R.color.coupons_gray));
            viewHolder.tv_item_coupons_price.setTextColor(mActivity.getResources().getColor(R.color.coupons_gray));
            viewHolder.tv_item_coupons_name.setTextColor(mActivity.getResources().getColor(R.color.coupons_gray));
            viewHolder.tv_item_coupons_limit.setTextColor(mActivity.getResources().getColor(R.color.coupons_gray));
            viewHolder.tv_item_coupons_date.setTextColor(mActivity.getResources().getColor(R.color.coupons_gray));
        }
        viewHolder.tv_item_coupons_price.setText(couponsList.get(i).get("coupon_amount") + "");
        viewHolder.tv_item_coupons_name.setText(couponsList.get(i).get("store_name") + "");
        viewHolder.tv_item_coupons_limit.setText("满" + couponsList.get(i).get("coupon_order_amount") + "元可用");
        viewHolder.tv_item_coupons_date.setText(couponsList.get(i).get("coupon_beginTime") + " 至 " + couponsList.get(i).get("coupon_endTime"));
        return view;
    }

    class ViewHolder {
        RelativeLayout layout_item_coupons;
        TextView tv_item_coupons_rmb;//¥标志
        TextView tv_item_coupons_price;//优惠券价格
        TextView tv_item_coupons_name;//店铺名称
        TextView tv_item_coupons_limit;//使用限制
        TextView tv_item_coupons_date;//截止日期
    }
}
