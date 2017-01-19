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
import com.ewgvip.buyer.android.models.VipCommissionItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MyCommissionListAdapter extends BaseAdapter {

    BaseActivity mActivity;
    private ArrayList<VipCommissionItem> list;

    public MyCommissionListAdapter(BaseActivity mActivity, ArrayList<VipCommissionItem> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

   public void update(ArrayList<VipCommissionItem> list){
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
        if (convertView==null){
            convertView=View.inflate(mActivity, R.layout.vip_reward_item,null);
            viewHodler=new ViewHodler();
            viewHodler.photo= (ImageView) convertView.findViewById(R.id.photo);
            viewHodler.userName=(TextView)convertView.findViewById(R.id.userName);
            viewHodler.commission_amount=(TextView)convertView.findViewById(R.id.reward_money);
            viewHodler.add_time=(TextView)convertView.findViewById(R.id.add_time);
            viewHodler.commission_status=(TextView)convertView.findViewById(R.id.tv_vip_reward_state);

            convertView.setTag(viewHodler);

        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
        viewHodler.commission_status.setTextColor(Color.RED);
        VipCommissionItem commissionItem = list.get(position);
        Glide.with(mActivity).load(Uri.parse(commissionItem.photo)).into(viewHodler.photo);
        viewHodler.userName.setText(commissionItem.userName);
        String commission_status = commissionItem.commission_status;
        String status="";
        if ("1".equals(commission_status)){
            status="已返佣";
        }else if ("2".equals(commission_status)){
            status="未返佣";
        }
        viewHodler.commission_status.setText("￥"+commissionItem.commission_amount);


        viewHodler.commission_amount.setText("返佣状态："+status);
        viewHodler.add_time.setText("返佣时间："+commissionItem.addTime);

        return convertView;
    }


    private static class ViewHodler {
        private ImageView photo;
        private TextView userName;
        private TextView add_time;
        private TextView commission_amount;
        private TextView commission_status;
    }


}
