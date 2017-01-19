package com.ewgvip.buyer.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/6.
 */
public class StorePager1Adapter extends RecyclerView.Adapter<StorePager1Adapter.ViewHolder> {

    List list;
    BaseActivity mActivity;

    public StorePager1Adapter(BaseActivity mActivity, List list) {
        this.mActivity = mActivity;
        this.list = list;
    }

    public int getItemViewType(int position) {
        final Map map = (Map) list.get(position);
        if (map.containsKey("title"))
            return 0;
        else
            return 1;
    }

    @Override
    public StorePager1Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_title, parent, false);
            holder = new ViewHolder(v, 0);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_floor, parent, false);
            holder = new ViewHolder(v, 1);
        }
        return holder;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onBindViewHolder(StorePager1Adapter.ViewHolder holder, int position) {


        final Map map = (Map) list.get(position);

        if (getItemViewType(position) == 0)
            holder.title.setText(map.get("title").toString());
        else {
            final Map map1 = (Map) map.get("goods1");
            BaseActivity.displayImage(map1.get("goods_main_photo").toString(), holder.goods_img1);
            holder.goods_name1.setText(map1.get("goods_name").toString());
            holder.goods_price1.setText("￥" + map1.get("goods_current_price").toString());
            holder.goods1.requestFocus();
            holder.goods1.setFocusable(true);
            holder.goods1.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_goods(map1.get("id") + "");
                }
            });

            if (map.containsKey("goods2")) {
                final Map map2 = (Map) map.get("goods2");
                holder.goods2.setVisibility(View.VISIBLE);
                BaseActivity.displayImage(map2.get("goods_main_photo").toString(), holder.goods_img2);
                holder.goods_name2.setText(map2.get("goods_name").toString());
                holder.goods_price2.setText("￥" + map2.get("goods_current_price").toString());
                holder.goods2.requestFocus();
                holder.goods2.setFocusable(true);
                holder.goods2.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                        mActivity.go_goods(map2.get("id") + "");
                    }
                });

            } else {
                holder.goods2.setVisibility(View.INVISIBLE);
            }

        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View convertView;
        private TextView title;
        private LinearLayout goods1;
        private LinearLayout goods2;
        private SimpleDraweeView goods_img1;
        private SimpleDraweeView goods_img2;
        private TextView goods_name1;
        private TextView goods_name2;
        private TextView goods_price1;
        private TextView goods_price2;


        public ViewHolder(View v, int i) {
            super(v);
            convertView = v;
            if (i == 0) {
                title = (TextView) v.findViewById(R.id.title);
            } else {
                goods1 = (LinearLayout) v.findViewById(R.id.goods1);
                goods2 = (LinearLayout) v.findViewById(R.id.goods2);
                goods_img1 = (SimpleDraweeView) v.findViewById(R.id.goods_img1);
                goods_img2 = (SimpleDraweeView) v.findViewById(R.id.goods_img2);
                goods_name1 = (TextView) v.findViewById(R.id.goods_name1);
                goods_name2 = (TextView) v.findViewById(R.id.goods_name2);
                goods_price1 = (TextView) v.findViewById(R.id.goods_price1);
                goods_price2 = (TextView) v.findViewById(R.id.goods_price2);
            }
        }
    }
}
