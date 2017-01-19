package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Map;

/**
 * 活动首页列表适配器
 */
public class ActivityIndexListAdapter extends BaseAdapter {

    BaseActivity mActivity;
    List list;
    LayoutInflater inflater;

    public ActivityIndexListAdapter(BaseActivity mActivity, List list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = inflater.inflate(R.layout.item_salespromotion, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_salespromotion = (SimpleDraweeView) view.findViewById(R.id.iv_salespromotion);
            viewHolder.tv_salesname = (TextView) view.findViewById(R.id.tv_salesname);
            viewHolder.tv_overtime = (TextView) view.findViewById(R.id.tv_overtime);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        Map map = (Map) list.get(i);
        BaseActivity.displayImage(map.get("picture").toString(), viewHolder.iv_salespromotion);
        viewHolder.tv_salesname.setText(map.get("ac_title").toString());
        String time = map.get("time_desc").toString();
        if (time.contains("-")) {
            viewHolder.tv_overtime.setText("还有" + time.substring(1) + "天开始");
        } else if (time.equals("已经结束")) {
            viewHolder.tv_overtime.setText(time);
        } else {
            viewHolder.tv_overtime.setText("还有" + time + "天结束");
        }

        return view;
    }

    public static class ViewHolder {
        private SimpleDraweeView iv_salespromotion;
        private TextView tv_salesname;
        private TextView tv_overtime;
    }
}
