package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class TransListAdapter extends BaseAdapter {
    Context context;
    LayoutInflater inflater;
    List<Map> myList;

    public TransListAdapter(Context context, List<Map> list) {
        this.context = context;
        this.myList = list;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return myList.size();
    }

    @Override
    public Object getItem(int position) {

        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_translist, null);
            holder = new ViewHolder();
            holder.img = (ImageView) convertView.findViewById(R.id.trans_img);
            holder.info = (TextView) convertView.findViewById(R.id.textView1);
            holder.time = (TextView) convertView.findViewById(R.id.textView2);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == 0) {
         holder.img.setImageResource(R.mipmap.logistics_over);
            holder.time.setTextColor(Color.rgb(255,102,0));
            holder.info.setTextColor(Color.rgb(255,102,0));

        }else {
          holder.img.setImageResource(R.mipmap.logistics_round);
            holder.time.setTextColor(Color.rgb(91,91,91));
            holder.info.setTextColor(Color.rgb(91,91,91));
        }

            Map map = myList.get(position);

        holder.info.setText(map.get("content").toString());
            holder.time.setText(map.get("time").toString());

         //   holder.img.setImageResource(Integer.parseInt(map.get("img").toString()));
        return convertView;
    }

    public static class ViewHolder {
        public ImageView img;
        public TextView info;
        public TextView time;
    }

}
