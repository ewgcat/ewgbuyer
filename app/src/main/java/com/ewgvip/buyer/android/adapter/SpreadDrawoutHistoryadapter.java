package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SpreadDrawoutHistoryadapter extends BaseAdapter{
    BaseActivity mActivity;
    ArrayList<String> list;

    public SpreadDrawoutHistoryadapter(BaseActivity mActivity,ArrayList<String> list) {
        this.mActivity=mActivity;
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public String getItem(int i) {
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
            convertView=View.inflate(mActivity, R.layout.item_spread_drawout_history,null);
            viewHodler=new ViewHodler();
            viewHodler.tv_history=(TextView)convertView.findViewById(R.id.tv_history);
            convertView.setTag(viewHodler);
        }else {
            viewHodler= (ViewHodler) convertView.getTag();
        }

        viewHodler.tv_history.setText(list.get(i));
        return convertView;
    }


    private static class ViewHodler {
        private TextView tv_history;
    }

}
