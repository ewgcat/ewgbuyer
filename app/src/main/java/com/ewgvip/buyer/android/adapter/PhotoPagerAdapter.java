package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.List;

/**
 * Created by Administrator on 2015/11/7.
 * 预览图片加载
 */
public class PhotoPagerAdapter extends BaseAdapter {
    Context context;//上下文
    List<String> list;//图片预览URL集合

    public PhotoPagerAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {

        return list==null?0:list.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {


        Holder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_photo_gallery, null);
            holder = new Holder();
            holder.iv_item_gallery = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_gallery);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        BaseActivity.displayImage(list.get(position).toString(), holder.iv_item_gallery);
        return convertView;
    }

    class Holder {
        SimpleDraweeView iv_item_gallery;
    }
}
