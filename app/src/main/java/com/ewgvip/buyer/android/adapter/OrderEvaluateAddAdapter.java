package com.ewgvip.buyer.android.adapter;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.fragment.OrderEvaluateCompleteFragment;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Map;

/**
 * @author zhangzhiyong  .
 * @Description
 * @date 2016/1/28
 */
public class OrderEvaluateAddAdapter extends BaseAdapter {
    BaseActivity mActivity;
    LayoutInflater inflater;
    private List list;
    OrderEvaluateCompleteFragment orderEvaluateCompleteFragment;
    Dialog myDialog;

    public OrderEvaluateAddAdapter(BaseActivity mActivity, List list, OrderEvaluateCompleteFragment orderEvaluateCompleteFragment) {
        this.mActivity = mActivity;
        this.list = list;
        this.orderEvaluateCompleteFragment = orderEvaluateCompleteFragment;
        inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public Object getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_order_evaluate_complete, null);
            holder = new ViewHolder();
            holder.tv_evaluate_value = (TextView) convertView.findViewById(R.id.tv_evaluate_value);
            holder.tv_evaluate_date = (TextView) convertView.findViewById(R.id.tv_evaluate_date);
            holder.tv_evaluate_add_goods_name = (TextView) convertView.findViewById(R.id.tv_evaluate_add_goods_name);
            holder.iv_evaluate_add = (SimpleDraweeView) convertView.findViewById(R.id.iv_evaluate_add);
            holder.layout_evaluate_add = (LinearLayout) convertView.findViewById(R.id.layout_evaluate_add);
            holder.btn_evaluate_add = (Button) convertView.findViewById(R.id.btn_evaluate_add);
            holder.btn_evaluate_edit = (Button) convertView.findViewById(R.id.btn_evaluate_edit);
            holder.btn_evaluate_delete = (Button) convertView.findViewById(R.id.btn_evaluate_delete);
            holder.priceView = (TextView) convertView.findViewById(R.id.goodslist_price);
            holder.goodslist_evaluate = (TextView) convertView.findViewById(R.id.goodslist_evaluate);
            holder.discount_type = (TextView) convertView.findViewById(R.id.discount_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Map map1 = (Map) list.get(position);
        if ((map1.get("evaluate_buyer_val") + "").equals("1")) {
            holder.tv_evaluate_value.setText("好评");
        } else if ((map1.get("evaluate_buyer_val") + "").equals("0")) {
            holder.tv_evaluate_value.setText("中评");
        } else if ((map1.get("evaluate_buyer_val") + "").equals("-1")) {
            holder.tv_evaluate_value.setText("差评");
        }
        BaseActivity.displayImage(map1.get("goods_main_photo") + "", holder.iv_evaluate_add);
        if ((map1.get("evaluate_able") + "").equals("1")) {
            holder.layout_evaluate_add.setVisibility(View.VISIBLE);
        } else if ((map1.get("evaluate_able") + "").equals("0")) {
            holder.layout_evaluate_add.setVisibility(View.GONE);
        }
        if ((map1.get("evaluate_add_able") + "").equals("1")) {
            holder.btn_evaluate_add.setVisibility(View.VISIBLE);
            holder.btn_evaluate_add.setOnClickListener(view -> {
                final SerializableMap serializableMap = new SerializableMap(map1);
                Bundle bundle = new Bundle();
                bundle.putInt("position", position);
                bundle.putSerializable("serializableMap", serializableMap);
                mActivity.go_order_evaluate_add(bundle, mActivity.getCurrentfragment());
            });
        } else if ((map1.get("evaluate_add_able") + "").equals("0")) {
            holder.btn_evaluate_add.setVisibility(View.GONE);
        }
        holder.tv_evaluate_add_goods_name.setText(map1.get("goods_name") + "");
        holder.tv_evaluate_date.setText(map1.get("addTime") + "");
        holder.btn_evaluate_edit.setOnClickListener(view -> {
            final SerializableMap serializableMap = new SerializableMap(map1);
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            bundle.putSerializable("serializableMap", serializableMap);
            mActivity.go_order_evaluate_edit(bundle, mActivity.getCurrentfragment());
        });
        holder.btn_evaluate_delete.setOnClickListener(view -> myDialog = MyDialog.showAlert(mActivity, "系统提示", "是否删除该条评论", "确定", view1 -> {
            Map map11 = mActivity.getParaMap();
            map11.put("evaluate_id", map1.get("evaluate_id") + "");
            RequestQueue requestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/evaluate_del.htm", response -> {
                list.remove(map1);
                OrderEvaluateAddAdapter.this.notifyDataSetChanged();
                Toast.makeText(mActivity, "删除成功", Toast.LENGTH_SHORT).show();
                myDialog.dismiss();
            }, error -> Toast.makeText(mActivity, "网络超时", Toast.LENGTH_SHORT).show(), map11);
            requestQueue.add(request);
        }, "取消", dialogInterface -> {

        }));
        return convertView;
    }

    private static class ViewHolder {
        private SimpleDraweeView iv_evaluate_add;
        private TextView tv_evaluate_value;
        private TextView tv_evaluate_date;
        private TextView tv_evaluate_add_goods_name;
        private TextView priceView;
        private TextView goodslist_evaluate;
        private TextView discount_type;
        private LinearLayout layout_evaluate_add;
        private Button btn_evaluate_add;
        private Button btn_evaluate_edit;
        private Button btn_evaluate_delete;
    }
}
