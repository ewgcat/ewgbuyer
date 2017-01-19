package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: FreeListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description: 0元购
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author lixiaoyang
 * @version 1.0
 * @date 2015-1-14
 */
public class FreeEvaluateListAdapter extends BaseAdapter {
    BaseActivity mActivity;
    LayoutInflater inflater;
    private List list;

    public FreeEvaluateListAdapter(BaseActivity mActivity, List list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_free_evaluate, null);
            holder = new ViewHolder();
            holder.username = (TextView) convertView.findViewById(R.id.username);
            holder.time = (TextView) convertView.findViewById(R.id.time);
            holder.user_photo = (SimpleDraweeView) convertView.findViewById(R.id.user_photo);
            holder.evaluate = (TextView) convertView.findViewById(R.id.evaluate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Map map = (Map) list.get(position);
        holder.username.setText(map.get("user_name").toString());
        holder.time.setText(map.get("evaluate_time").toString());
        holder.evaluate.setText(map.get("use_experience").toString());
        if (!map.get("user_photo").equals("")) {
            BaseActivity.displayImage(map.get("user_photo").toString(), holder.user_photo);
        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView username;
        public TextView time;
        public TextView evaluate;
        public SimpleDraweeView user_photo;
    }

}
