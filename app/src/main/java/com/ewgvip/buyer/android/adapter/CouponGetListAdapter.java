package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.CouponInformation;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2015/10/10
 * 优惠券展示ListView列表适配器
 */
public class CouponGetListAdapter extends BaseAdapter {

    /**
     * 环境变量
     */
    BaseActivity mActivity;
    /**
     * 保存传入的参数数据
     */
    private List list;
    /**
     * 打气筒对象
     */
    private LayoutInflater inflater;

    /**
     * 重写构造器
     */
    public CouponGetListAdapter(BaseActivity mActivity, List list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public int getCount() {
        return 1 + list.size();
    }

    @Override
    public Object getItem(int position) {

        return null;
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (position == 0) {
            return inflater.inflate(R.layout.fragment_coupon_get_listview_title, null);
        }
        View view = null;
        ViewHolder viewHolder = null;
        CouponInformation couponInformation = (CouponInformation) list.get(position - 1);
        if (convertView != null && convertView instanceof LinearLayout) {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        } else {
            view = inflater.inflate(R.layout.item_listview_coupon, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_coupon_pic = (SimpleDraweeView) view.findViewById(R.id.iv_coupon_pic);
            viewHolder.tv_coupon_name = (TextView) view.findViewById(R.id.tv_coupon_name);
            viewHolder.tv_coupon_time = (TextView) view.findViewById(R.id.tv_coupon_time);
            viewHolder.tv_coupon_amount = (TextView) view.findViewById(R.id.tv_coupon_amount);
            viewHolder.tv_coupon_order_amount = (TextView) view.findViewById(R.id.tv_coupon_order_amount);
            viewHolder.tv_coupon_surplus_amount = (TextView) view.findViewById(R.id.tv_coupon_surplus_amount);
            view.setTag(viewHolder);
        }
        if (couponInformation != null) {
            BaseActivity.displayImage(couponInformation.getCoupon_pic(), viewHolder.iv_coupon_pic);
            viewHolder.tv_coupon_name.setText(couponInformation.getCoupon_name());
            viewHolder.tv_coupon_time.setText(couponInformation.getCoupon_begin_time() + " 至 " + couponInformation.getCoupon_end_time());
            viewHolder.tv_coupon_amount.setText(couponInformation.getCoupon_amount());
            viewHolder.tv_coupon_order_amount.setText("满" + couponInformation.getCoupon_order_amount() + "使用");
            viewHolder.tv_coupon_surplus_amount.setText("剩余" + couponInformation.getCoupon_surplus_amount() + "张");
        }
        return view;
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private SimpleDraweeView iv_coupon_pic;
        private TextView tv_coupon_name;
        private TextView tv_coupon_time;
        private TextView tv_coupon_amount;
        private TextView tv_coupon_order_amount;
        private TextView tv_coupon_surplus_amount;
    }

}
