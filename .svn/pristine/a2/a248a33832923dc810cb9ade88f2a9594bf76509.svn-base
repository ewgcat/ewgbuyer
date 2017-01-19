package com.ewgvip.buyer.android.adapter;

import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.VipRewardItem;
import com.ewgvip.buyer.android.volley.RequestQueue;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/16.
 */
public class MyVipRewardListadapter extends BaseAdapter {

    private BaseActivity mActivity;
    private ArrayList<VipRewardItem> list;
    private RequestQueue mRequestQueue;

    public MyVipRewardListadapter(BaseActivity mActivity, ArrayList<VipRewardItem> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

    public void update(ArrayList<VipRewardItem> list) {
        this.list = list;
        notifyDataSetChanged();
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
        ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.vip_reward_item, null);
            viewHodler = new ViewHodler();
            viewHodler.photo = (ImageView) convertView.findViewById(R.id.photo);
            viewHodler.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHodler.reward_money = (TextView) convertView.findViewById(R.id.reward_money);
            viewHodler.add_time = (TextView) convertView.findViewById(R.id.add_time);
            viewHodler.vip_reward_state = (TextView) convertView.findViewById(R.id.tv_vip_reward_state);

            convertView.setTag(viewHodler);

        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        viewHodler.vip_reward_state.setBackgroundColor(Color.RED);
        final VipRewardItem vipRewardItem = list.get(position);
        final String vipRewardItemid = vipRewardItem.id;


        Glide.with(mActivity).load(Uri.parse(vipRewardItem.photo)).into(viewHodler.photo);
        viewHodler.userName.setText(vipRewardItem.userName);
        viewHodler.vip_reward_state.setText(vipRewardItem.vip_reward_state);
        viewHodler.reward_money.setText("奖励金额：￥" + vipRewardItem.reward_money);
        viewHodler.add_time.setText("奖励时间：" + vipRewardItem.add_time);

        if ("待领取".equals(vipRewardItem.vip_reward_state)) {
            viewHodler.vip_reward_state.setBackgroundColor(Color.RED);
        } else if (("已领取".equals(vipRewardItem.vip_reward_state))||("已发放".equals(vipRewardItem.vip_reward_state))) {
            viewHodler.vip_reward_state.setBackgroundColor(Color.LTGRAY);
        }
        return convertView;
    }



    private static class ViewHodler {
        private ImageView photo;
        private TextView userName;
        private TextView add_time;
        private TextView reward_money;
        private TextView vip_reward_state;
    }
}
