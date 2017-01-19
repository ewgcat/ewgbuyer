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
 * Author  zhangzhiyong
 * Date   2016/3/16 14:58
 * Description:
 */
public class InventoryDialogAdapter extends BaseAdapter {
    BaseActivity baseActivity;
    List dataList;
    int type;

    public InventoryDialogAdapter(BaseActivity baseActivity, List dataList, int type) {
        this.baseActivity = baseActivity;
        this.dataList = dataList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = null;
        if (view == null) {
            view = LayoutInflater.from(baseActivity).inflate(R.layout.item_inventory_dialog, null, false);
            holder = new Holder();
            holder.iv_item_inventory = (SimpleDraweeView) view.findViewById(R.id.iv_item_inventory);
            holder.tv_item_inventory_name = (TextView) view.findViewById(R.id.tv_item_inventory_name);
            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }
        Map map = (Map) dataList.get(i);
        if (type == 0) {
            BaseActivity.displayImage(map.get("goods_main_photo") + "", holder.iv_item_inventory);
            holder.tv_item_inventory_name.setText(map.get("goods_name") + "");
        }
        if (type == 1) {
            BaseActivity.displayImage(map.get("img") + "", holder.iv_item_inventory);
            holder.tv_item_inventory_name.setText(map.get("name") + "");
        }
        return view;
    }

    class Holder {
        SimpleDraweeView iv_item_inventory;
        TextView tv_item_inventory_name;
    }
}
