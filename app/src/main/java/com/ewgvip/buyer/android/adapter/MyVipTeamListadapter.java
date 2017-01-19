package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.TeamItem;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/16.
 */
public class MyVipTeamListadapter extends BaseAdapter {

    private BaseActivity mActivity;
    private ArrayList<TeamItem> list;


    public MyVipTeamListadapter(BaseActivity mActivity, ArrayList<TeamItem> list) {
        this.mActivity = mActivity;
        this.list = list;
    }



    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.size()>position?list.get(position):null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_team_manage_teamlist, null);
            viewHodler = new ViewHodler();
            viewHodler.photo= (ImageView) convertView.findViewById(R.id.photo);
            viewHodler.tv_userName= (TextView) convertView.findViewById(R.id.tv_userName);
            viewHodler.tv_team_count= (TextView) convertView.findViewById(R.id.tv_team_count);
            viewHodler.add_time= (TextView) convertView.findViewById(R.id.add_time);
            viewHodler.iv_team_user_level_img= (ImageView) convertView.findViewById(R.id.iv_team_user_level_img);
            viewHodler.tv_team_vip_level_name= (TextView) convertView.findViewById(R.id.tv_team_vip_level_name);
            convertView.setTag(viewHodler);

        } else {
            viewHodler = (ViewHodler) convertView.getTag();
        }
        if (list.size()>position){
            TeamItem teamItem = list.get(position);
            Glide.with(mActivity).load(teamItem.photo_url).into(viewHodler.photo);
            viewHodler.tv_userName.setText(teamItem.userName);
            viewHodler.tv_team_count.setText(teamItem.child_size+"人");
            viewHodler.add_time.setText(teamItem.addTime);
            Glide.with(mActivity).load(mActivity.getAddress()+teamItem.gradeName.icon).into(viewHodler.iv_team_user_level_img);
            viewHodler.tv_team_vip_level_name.setText(teamItem.gradeName.name);
        }


        return convertView;
    }






    private static class ViewHodler {
        private ImageView photo;
        private ImageView iv_team_user_level_img;
        private TextView tv_userName;
        private TextView add_time;
        private TextView tv_team_count;
        private TextView tv_team_vip_level_name;
    }
}
