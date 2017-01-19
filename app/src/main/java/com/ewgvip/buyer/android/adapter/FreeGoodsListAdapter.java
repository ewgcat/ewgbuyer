package com.ewgvip.buyer.android.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/18.
 */
public class FreeGoodsListAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<Map> zeroUseList;//优惠券信息集合

    public FreeGoodsListAdapter(BaseActivity mActivity, List<Map> zeroUseList) {
        this.mActivity = mActivity;
        this.zeroUseList = zeroUseList;
    }

    @Override
    public int getCount() {
        return zeroUseList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            view = LayoutInflater.from(mActivity).inflate(R.layout.item_zero_use, null);
            viewHolder = new ViewHolder();
            viewHolder.iv_item_zero_use = (SimpleDraweeView) view.findViewById(R.id.iv_item_zero_use);
            viewHolder.tv_item_zero_use_count = (TextView) view.findViewById(R.id.tv_item_zero_use_count);
            viewHolder.tv_item_zero_use_name = (TextView) view.findViewById(R.id.tv_item_zero_use_name);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        ViewGroup.LayoutParams params = viewHolder.iv_item_zero_use.getLayoutParams();
        params.width = mActivity.getScreenWidth();
        params.height = params.width * 222 / 543;
        viewHolder.iv_item_zero_use.setLayoutParams(params);
        BaseActivity.displayImage(zeroUseList.get(i).get("free_acc").toString(), viewHolder.iv_item_zero_use);
        viewHolder.tv_item_zero_use_name.setText(zeroUseList.get(i).get("free_name") + "");
        viewHolder.tv_item_zero_use_count.setText(zeroUseList.get(i).get("free_count") + "件");
        return view;
    }

    class ViewHolder {
        SimpleDraweeView iv_item_zero_use;
        TextView tv_item_zero_use_count;
        TextView tv_item_zero_use_name;
    }
}
