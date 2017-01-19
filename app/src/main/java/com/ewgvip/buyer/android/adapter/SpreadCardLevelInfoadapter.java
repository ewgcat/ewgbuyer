package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.SpreadCardLevelInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/7.
 */
public class SpreadCardLevelInfoadapter extends BaseAdapter{
    BaseActivity mActivity;
    ArrayList<SpreadCardLevelInfo> list;
    public SpreadCardLevelInfoadapter(BaseActivity mActivity, ArrayList<SpreadCardLevelInfo> list) {
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
            convertView=View.inflate(mActivity, R.layout.spread_card_item,null);
            viewHodler=new ViewHodler();

            viewHodler.tv_card_code=(TextView)convertView.findViewById(R.id.tv_card_code);
            viewHodler.tv_grade_name=(TextView)convertView.findViewById(R.id.tv_grade_name);
            viewHodler.tv_card_no=(TextView)convertView.findViewById(R.id.tv_card_no);
            viewHodler.tv_expressly_password=(TextView)convertView.findViewById(R.id.tv_expressly_password);

            convertView.setTag(viewHodler);

        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }
        SpreadCardLevelInfo spreadCardLevelInfo = list.get(i);
        viewHodler.tv_card_code.setText(spreadCardLevelInfo.card_code);
        viewHodler.tv_grade_name.setText("当前等级： "+spreadCardLevelInfo.grade_name);
        viewHodler.tv_card_no.setText("账号： "+ spreadCardLevelInfo.card_no);
        ViewHodler finalViewHodler = viewHodler;
        viewHodler.tv_expressly_password.setOnClickListener(view -> finalViewHodler.tv_expressly_password.setText("密码："+spreadCardLevelInfo.expressly_password));


        return convertView;
    }


    private static class ViewHodler {
        private TextView tv_card_code;
        private TextView tv_grade_name;
        private TextView tv_card_no;
        private TextView tv_expressly_password;
    }
}
