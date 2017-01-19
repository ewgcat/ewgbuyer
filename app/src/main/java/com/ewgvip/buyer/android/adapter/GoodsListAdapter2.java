package com.ewgvip.buyer.android.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/29.
 */
public class GoodsListAdapter2 extends RecyclerView.Adapter<GoodsListAdapter2.ViewHolder> {
    private List mDataset;
    private BaseActivity mActivity;

    // Provide a suitable constructor (depends on the kind of dataset)
    public GoodsListAdapter2(List myDataset, BaseActivity mActivity) {
        this.mDataset = myDataset;
        this.mActivity = mActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods_list, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final Map map = (Map) mDataset.get(position);
        holder.titleView.setText(map.get("goods_name").toString());
        holder.titleView.setTag(map.get("id"));
        holder.priceView.setText("￥" + map.get("goods_current_price"));
        MainActivity.displayImage(map.get("goods_main_photo").toString(), holder.imageView);
       // holder.goodslist_evaluate.setText(map.get("evaluate").toString());

        if (map.get("status").toString().length() > 0) {
            holder.discount_type.setText(map.get("status").toString());
            holder.discount_type.setVisibility(View.VISIBLE);
        } else {
            holder.discount_type.setVisibility(View.GONE);
        }

        holder.convertView.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_goods(map.get("id") + "");
            }
        });
        holder.convertView.setOnLongClickListener(v -> false);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public View convertView;
        public SimpleDraweeView imageView;
        public TextView titleView;
        public TextView priceView;
      //  public TextView goodslist_evaluate;
        public TextView discount_type;


        public ViewHolder(View v) {
            super(v);
            convertView = v;
            titleView = (TextView) convertView.findViewById(R.id.goodslist_title);
            imageView = (SimpleDraweeView) convertView.findViewById(R.id.goodslist_img);
            priceView = (TextView) convertView.findViewById(R.id.goodslist_price);
          //  goodslist_evaluate = (TextView) convertView.findViewById(R.id.goodslist_evaluate);
            discount_type = (TextView) convertView.findViewById(R.id.discount_type);
        }
    }
}