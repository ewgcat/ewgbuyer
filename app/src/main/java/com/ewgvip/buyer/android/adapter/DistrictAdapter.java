package com.ewgvip.buyer.android.adapter;

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
 * 下拉地区
 * Created by lgx on 2015/10/24.
 */
public class DistrictAdapter extends BaseAdapter {
    GroupShoppingFragment groupShoppingFragment;
    private BaseActivity mActivity;
    private List<Map> districts;
    private LayoutInflater inflater;
    private int selectedPosition = -1;

    public DistrictAdapter(BaseActivity mActivity, List<Map> districts, GroupShoppingFragment groupShoppingFragment) {
        this.mActivity = mActivity;
        this.districts = districts;
        this.groupShoppingFragment = groupShoppingFragment;
        inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return districts.size();
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
        Map map = districts.get(position);
        viewHolder.textView.setText(map.get("areaName").toString());
        viewHolder.relativeLayout.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                groupShoppingFragment.setArea(position);
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
