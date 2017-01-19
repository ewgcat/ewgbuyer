package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.OnClick;

import java.util.List;

/**
 * 商品分类筛选适配器
 * Created by lgx on 2015/10/16.
 */
public class GoodsSelectAdapter extends BaseAdapter{
    private BaseActivity mActivity;
    private List<String> sNames;
    private LayoutInflater inflater;
    private OnClick onClick;
    private int iPosition;

    public GoodsSelectAdapter(BaseActivity mActivity, List<String> sNames) {
        this.mActivity = mActivity;
        this.sNames = sNames;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        iPosition=0;
    }


    @Override
    public int getCount() {
        return sNames.size();
    }

    @Override
    public String getItem(int position) {
        return sNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_select, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tvName.setText(getItem(position));
        if(iPosition==position){
            holder.tvName.setTextColor(Color.RED);
        }else{
            holder.tvName.setTextColor(Color.BLACK);
        }
        holder.tvName.setOnClickListener(v -> {
            if(null!=onClick){
                onClick.setClickListener(position);
            }
        });
        return convertView;
    }

    public void setOnClickListener(OnClick onClick){
        if(null!=onClick){
            this.onClick=onClick;
        }
    }

    public void setSelectPosition(int iPosition){
        this.iPosition=iPosition;
    }

    public final class ViewHolder {
        public TextView tvName;
    }

}
