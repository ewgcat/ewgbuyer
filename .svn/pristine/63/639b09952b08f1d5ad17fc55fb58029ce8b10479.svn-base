package com.ewgvip.buyer.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.fragment.GroupShoppingFragment;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.List;
import java.util.Map;

/**
 * 团购商品二级分类的下一级分类
 * Created by lgx on 2015/10/27.
 */
public class GroupShoppingNextClassAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<Map> rightList;
    private int current;
    private LayoutInflater inflater;
    private GroupShoppingFragment groupShoppingFragment;

    public GroupShoppingNextClassAdapter(BaseActivity mActivity, List<Map> rightList, int position, GroupShoppingFragment groupShoppingFragment) {
        this.mActivity = mActivity;
        this.rightList = rightList;
        this.current = position;
        this.groupShoppingFragment = groupShoppingFragment;
        inflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return rightList.size();
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
            convertView = inflater.inflate(R.layout.groupshopping_next_class_item_layout, null);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Map map = rightList.get(position);
        viewHolder.textView.setText(map.get("name").toString());
        viewHolder.textView.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                groupShoppingFragment.setRightClass(position, rightList, current);
            }
        });
        return convertView;
    }

    private static class ViewHolder {
        private TextView textView;

    }

}
