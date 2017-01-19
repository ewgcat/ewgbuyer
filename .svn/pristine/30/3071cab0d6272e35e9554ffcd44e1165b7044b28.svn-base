package com.ewgvip.buyer.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

/**
 * Created by Administrator on 2015/12/4.
 */
public class GroupEvaluateAdapter extends BaseAdapter {
    private BaseActivity mActivity;

    public GroupEvaluateAdapter(BaseActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_group_evaluate, null);
        }
        return view;
    }

    class ViewHolder {
        SimpleDraweeView iv_item_group_life;
        TextView tv_item_group_evaluate_name;
        TextView tv_item_group_evaluate_date;
        TextView tv_item_group_evaluate_info;
    }
}
