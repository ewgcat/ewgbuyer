package com.ewgvip.buyer.android.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.fragment.GroupShoppingFragment;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.List;
import java.util.Map;

/**
 * 团购分类适配器
 * Created by lgx on 2015/10/27.
 */
public class GroupShoppingAdapter extends BaseAdapter {
    GroupShoppingFragment groupShoppingFragment;
    private BaseActivity mActivity;
    private List<Map> groupShopping;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    public GroupShoppingAdapter(BaseActivity mActivity, List<Map> groupShopping, GroupShoppingFragment groupShoppingFragment) {
        this.mActivity = mActivity;
        this.groupShopping = groupShopping;
        this.groupShoppingFragment = groupShoppingFragment;
        inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return groupShopping.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.groupshopping_class_item, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.relativeLayout);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map map = groupShopping.get(position);
        viewHolder.textView.setText(map.get("name").toString());
        if (selectedPosition == position) {
            viewHolder.textView.setTextColor(Color.RED);
            viewHolder.relativeLayout.setBackgroundColor(mActivity.getResources().getColor(R.color.white));
        } else {
            viewHolder.textView.setTextColor(mActivity.getResources().getColor(R.color.textdarkgray));
            viewHolder.relativeLayout.setBackgroundColor(Color.TRANSPARENT);
        }
        viewHolder.relativeLayout.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                groupShoppingFragment.setLeftClass(position, groupShopping);
            }
        });
        return convertView;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    private static class ViewHolder {
        private TextView textView;
        private RelativeLayout relativeLayout;

    }
}
