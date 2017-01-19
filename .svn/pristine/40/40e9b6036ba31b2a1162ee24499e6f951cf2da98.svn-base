package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.SpreadTeamManageInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/7.
 */
public class SpreadTeamManageInfoadapter extends BaseAdapter {

    BaseActivity mActivity;
    ArrayList<SpreadTeamManageInfo> list;
    public SpreadTeamManageInfoadapter(BaseActivity mActivity, ArrayList<SpreadTeamManageInfo> list) {
        this.mActivity=mActivity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
       ViewHodler viewHodler = null;
        if (convertView==null){
            convertView=View.inflate(mActivity, R.layout.spread_team_item,null);
            viewHodler=new ViewHodler();

            viewHodler.tv_rank=(TextView)convertView.findViewById(R.id.tv_rank);
            viewHodler.tv_team_username=(TextView)convertView.findViewById(R.id.tv_team_username);
            viewHodler.tv_grade_name=(TextView)convertView.findViewById(R.id.tv_grade_name);


            convertView.setTag(viewHodler);

        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
        SpreadTeamManageInfo spreadTeamManageInfo = list.get(i);
        viewHodler.tv_rank.setText(spreadTeamManageInfo.rank);
        viewHodler.tv_team_username.setText(spreadTeamManageInfo.team_username);
        viewHodler.tv_grade_name.setText("当前等级： "+spreadTeamManageInfo.grade_name);

        return convertView;
    }


    private static class ViewHodler {
        private TextView tv_team_username;
        private TextView tv_grade_name;
        private TextView tv_rank;
    }
}
