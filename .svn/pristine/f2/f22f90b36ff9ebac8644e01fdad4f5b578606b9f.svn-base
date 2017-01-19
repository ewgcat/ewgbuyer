package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.OnClick;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: LeftListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description: 分类左列表适配器
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
 * @date 2014-7-22
 */
public class GoodsclassFirstAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<Map<String, Object>> list;
    private LayoutInflater inflater;
    private int iSelect;
    private OnClick onClick;


    public GoodsclassFirstAdapter(MainActivity mActivity, List<Map<String, Object>> first_list) {
        this.mActivity = mActivity;
        this.list = first_list;
        inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.iSelect = 0;
    }

    public void setSelectedPosition(int position) {
        this.iSelect = position;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Map<String, Object> getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.goods_type, null);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.textview);
            holder.layout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Map<String, Object> map = getItem(position);
        holder.textView.setText(map.get("className").toString());
        holder.textView.setTag(map.get("id").toString());
        if (position == iSelect) {
            holder.layout.setBackgroundResource(R.drawable.good_type_list_press);
            holder.textView.setTextColor(Color.RED);
        } else {
            holder.layout.setBackgroundResource(R.drawable.goods_type_list_normal);
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.layout.setOnClickListener(v -> {

            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (null != onClick) {
                    onClick.setClickListener(position);
                }
            }
        });
        return convertView;
    }

    public void setOnClickListener(OnClick onClick) {
        if (null != onClick) {
            this.onClick = onClick;
        }
    }

    public final class ViewHolder {
        public RelativeLayout layout;
        public TextView textView;
    }
}
