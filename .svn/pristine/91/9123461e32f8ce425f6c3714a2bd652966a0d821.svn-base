package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/20.
 */
public class ActivityGoodsListAdapter extends RecyclerView.Adapter<ActivityGoodsListAdapter.ViewHolder> {

    ArrayList<HashMap<String,ArrayList>> list;
    BaseActivity mActivity;
    LayoutInflater inflater;

    public ActivityGoodsListAdapter(BaseActivity mActivity, ArrayList<HashMap<String,ArrayList>> list) {
        this.list = list;
        this.mActivity = mActivity;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = null;
        if (viewType == 0) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salespmlayout1, parent, false);
            holder = new ViewHolder(v, 0);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_salespmlayout, parent, false);
            holder = new ViewHolder(v,1);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Map map = (Map) list.get(position);
        if (getItemViewType(position) == 0){
            holder.ll_selespm_title.setVisibility(View.VISIBLE);
            Map map0 = (Map) map.get("title");
            holder.iv_salespm.setImageURI(Uri.parse(map0.get("activity_image").toString()));
            holder.tv_salesstarttime.setText(map0.get("beginTime").toString());
            holder.tv_salesstoptime.setText(map0.get("endTime").toString());
        } else {
            holder.ll_selespm_.setVisibility(View.VISIBLE);
            if (map.containsKey("goods2")) {
                final Map map1 = (Map) map.get("goods2");
                final int gooodID = Integer.parseInt(map1.get("id").toString());
                holder.iv_salesgood.setImageURI(Uri.parse(map1.get("goods_picture").toString()));
                holder.tv_salespmname.setText(map1.get("goods_name").toString());
                holder.tv_salesvalues.setText("￥" + map1.get("goods_price").toString());
                holder.ll_selaspm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            mActivity.go_goods(gooodID + "");
                        }
                    }
                });


                final Map map2= (Map) map.get("goods1");
                final int gooodID_ = Integer.parseInt(map2.get("id").toString());
                holder.iv_salesgood_.setImageURI(Uri.parse(map2.get("goods_picture").toString()));
                holder.tv_salespmname_.setText(map2.get("goods_name").toString());
                holder.tv_salesvalues_.setText("￥" + map2.get("goods_price").toString());
                holder.ll_selespm_.setOnClickListener(view -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_goods(gooodID_ + "");
                    }
                });

            }else {
                final Map map1 = (Map) map.get("goods1");
                final int gooodID = Integer.parseInt(map1.get("id").toString());
                holder.iv_salesgood.setImageURI(Uri.parse(map1.get("goods_picture").toString()));
                holder.tv_salespmname.setText(map1.get("goods_name").toString());
                holder.tv_salesvalues.setText("￥" + map1.get("goods_price").toString());
                holder.ll_selaspm.setOnClickListener(view -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_goods(gooodID + "");
                    }
                });

                holder.ll_selespm_.setVisibility(View.INVISIBLE);
            }
        }
    }

    public int getItemViewType(int position) {
        final Map map = (Map) list.get(position);
        if (map.containsKey("title"))
            return 0;
        else
            return 1;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }






    public static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayout ll_selespm_title;
        private SimpleDraweeView iv_salespm;
        private TextView tv_salesstarttime;
        private TextView tv_salesstoptime;
        private SimpleDraweeView iv_salesgood;
        private SimpleDraweeView iv_salesgood_;
        private TextView tv_salespmname;
        private TextView tv_salesvalues;
        private TextView tv_salespmname_;
        private TextView tv_salesvalues_;
        private LinearLayout ll_selaspm;
        private LinearLayout ll_selespm_;
        public View convertView;

        public ViewHolder(View view, int i) {
            super(view);
            convertView = view;
            if (i == 0) {
                //促销活动标题布局加载
                ll_selespm_title = (LinearLayout) view.findViewById(R.id.ll_selespm_title);
                iv_salespm = (SimpleDraweeView) view.findViewById(R.id.iv_salespm);
                tv_salesstarttime = (TextView) view.findViewById(R.id.tv_salesstarttime);
                tv_salesstoptime = (TextView) view.findViewById(R.id.tv_salesstoptime);

            } else {
                //促销内容加载
                iv_salesgood = (SimpleDraweeView) view.findViewById(R.id.iv_salesgood);
                iv_salesgood_ = (SimpleDraweeView) view.findViewById(R.id.iv_salesgood_);
                tv_salespmname = (TextView) view.findViewById(R.id.tv_salespmname);
                tv_salesvalues = (TextView) view.findViewById(R.id.tv_salesvalues);
                tv_salespmname_ = (TextView) view.findViewById(R.id.tv_salespmname_);
                tv_salesvalues_ = (TextView) view.findViewById(R.id.tv_salesvalues_);
                ll_selaspm = (LinearLayout) view.findViewById(R.id.ll_salespm);
                ll_selespm_ = (LinearLayout) view.findViewById(R.id.ll_selespm_);
            }
        }
    }




    public void addAll(List list) {
        int lastIndex = this.list.size();
        if (this.list.addAll(list)) {
            notifyItemRangeInserted(lastIndex, list.size());
        }
    }

    public void remove(int position) {
        if(this.list.size() > position) {
            list.remove(position);
            notifyItemRemoved(position);
        }

    }

    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }


}
