package com.ewgvip.buyer.android.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.TransActivity;
import com.ewgvip.buyer.android.fragment.OrderAllTabFragment;
import com.ewgvip.buyer.android.fragment.OrderEvaluateFragment;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;
import com.ewgvip.buyer.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/23.
 */
public class OrderListAdapter extends BaseAdapter {
    public static int CURRENT = 0;
    private LayoutInflater inflater;
    private List<Map> mylist;
    private String str_payment = "";
    private BaseActivity mActivity;
    private String status;
    private int current = 0;
    private AlertDialog selectAlertDialog;

    public OrderListAdapter(BaseActivity mActivity, List<Map> list, String status, int current) {
        this.mActivity = mActivity;
        mylist = list;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.status = status;
        this.current = current;
    }

    public void  update(List<Map> list){
        this.mylist=list;
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {

        return mylist.size();
    }

    @Override
    public Object getItem(int position) {

        return mylist.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.order_normal_list_item, null);
            holder = new ViewHolder();
            holder.order_info = (TextView) convertView.findViewById(R.id.order_info);
            holder.orderPrice = (TextView) convertView.findViewById(R.id.order_price);
            holder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
            holder.button = (Button) convertView.findViewById(R.id.order_button);
            holder.button2 = (Button) convertView.findViewById(R.id.order_button2);
            holder.order_goods = (LinearLayout) convertView.findViewById(R.id.order_goods);
            holder.tv_order_price = (TextView) convertView.findViewById(R.id.tv_order_price);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final int index = position;
        final Map map = mylist.get(position);
//        holder.orderNumber
//                .setText("订单号:" + map.get("order_num").toString());

        String double_money = Double.valueOf(map.get("order_total_price").toString()) + "";
        if (double_money.indexOf(".") != -1) {
            //获取小数点的位置
            int num = 0;
            num = double_money.indexOf(".");

            //获取小数点后面的数字 是否有两位 不足两位补足两位
            String dianAfter = double_money.substring(0, num + 1);
            String afterData = double_money.replace(dianAfter, "");
            if (afterData.length() < 2) {
                afterData = afterData + "0";
            } else {
                afterData = afterData;
            }
            double_money = double_money.substring(0, num) + "." + afterData.substring(0, 2);
        }

        SpannableStringBuilder style = new SpannableStringBuilder(
                double_money);
        style.setSpan(new AbsoluteSizeSpan(BaseActivity.dp2px(mActivity,14)),
                0, double_money.length() - 2,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        style.setSpan(new AbsoluteSizeSpan(BaseActivity.dp2px(mActivity,12)),
                double_money.length() - 2, double_money.length(),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        holder.tv_order_price.setVisibility(View.VISIBLE);
        holder.orderPrice.setText(style);
        final int status = Integer.parseInt(map.get("order_status").toString());
        // 订单状态,0为订单取消，10为已提交待付款，16为货到付款，20为已付款待发货，30为已发货待收货，40为已收货
        // 50买家评价完毕 ,65订单不可评价，到达设定时间，系统自动关闭订单相互评价功能

        List<String> imglist = (List<String>) map.get("photo_list");
        List<String> name_list = (List<String>) map.get("name_list");
        List<String> price_list = (List<String>) map.get("price_list");
        List<String> gsp_list = (List<String>) map.get("gsp_list");
        List<String> count_list = (List<String>) map.get("count_list");
        final List<String> id_list = (List<String>) map.get("id_list");
        holder.order_goods.removeAllViews();

        for (int i = 0; i < imglist.size(); i++) {
            View view = inflater.inflate(R.layout.order_normal_single_goods, null);
            SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.goods_img);
            img.setImageURI(Uri.parse(imglist.get(i)));

            TextView name = (TextView) view.findViewById(R.id.goods_name);
            name.setText(name_list.get(i));
            TextView goods_count = (TextView) view.findViewById(R.id.goods_count);
            goods_count.setText(count_list.get(i));
            TextView goods_gsp = (TextView) view.findViewById(R.id.goods_gsp);
            goods_gsp.setText(gsp_list.get(i));
            TextView goods_price = (TextView) view.findViewById(R.id.goods_price);
            goods_price.setText(price_list.get(i));
            holder.order_goods.addView(view);
        }

        final String order_id = map.get("order_id").toString();
        convertView.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                OrderAllTabFragment.REFRESH = true;
                mActivity.go_order_normal(order_id, mActivity.getCurrentfragment(), current);
                CURRENT = current;
            }
        });

        holder.order_goods.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                OrderAllTabFragment.REFRESH = true;
                mActivity.go_order_normal(order_id, mActivity.getCurrentfragment(), current);
                CURRENT = current;
            }
        });

        final String order_total_price = map.get("order_total_price").toString();
        final String order_num = map.get("order_num").toString();
        final String promotion_flag = map.get("promotion_flag").toString();

        holder.button.setVisibility(View.GONE);
        holder.button2.setVisibility(View.GONE);
        holder.order_info.setVisibility(View.GONE);
        String str = "";
        final Map paramap = mActivity.getParaMap();
        paramap.put("order_id", order_id);
        if (status == 11) {
//            if ((map.get("advance_wei") + "").equals("")) {
//                holder.button.setVisibility(View.VISIBLE);
//                holder.button2.setVisibility(View.GONE);
//                holder.button.setText("取消订单");
//                holder.button.setOnClickListener(new View.OnClickListener() {
//
//                    @Override
//                    public void onClick(View v) {
//                        if (FastDoubleClickTools.isFastDoubleClick()) {
//
//                            OrderAllTabFragment.REFRESH = false;
//                            new AlertDialog.Builder(
//                                    mActivity)
//                                    .setTitle(
//                                            "你确定要取消订单吗？")
//                                    .setPositiveButton(
//                                            "确定",
//                                            new DialogInterface.OnClickListener() {
//
//                                                @Override
//                                                public void onClick(
//                                                        DialogInterface dialog,
//                                                        int which) {
//                                                    goods_order_cancel(order_id);
//
//                                                }
//                                            })
//                                    .setNegativeButton(
//                                            "取消",
//                                            null)
//                                    .show();
//                        }
//                    }
//                });
//            } else {
            holder.button.setVisibility(View.VISIBLE);
            holder.button2.setVisibility(View.VISIBLE);
            holder.button2.setText("取消订单");
            holder.button2.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    OrderAllTabFragment.REFRESH = false;
                    new AlertDialog.Builder(
                            mActivity)
                            .setTitle(
                                    "你确定要取消订单吗？")
                            .setPositiveButton(
                                    "确定",
                                    (dialog, which) -> goods_order_cancel(order_id))
                            .setNegativeButton(
                                    "取消",
                                    null)
                            .show();
                }
            });
            holder.button.setText("尾款支付");
            holder.button.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {

                    mActivity.showProcessDialog();
                    Map map1 = mActivity.getParaMap();
                    map1.put("order_id", paramap.get("order_id"));
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity,
                            mActivity
                                    .getAddress()
                                    + "/app/buyer/advance_order_pay_verify.htm",
                            result -> {
                                try {
                                    Log.e("++", result + "");
                                    int code = result.getInt("code");
                                    if (code == 100) {
                                        OrderAllTabFragment.REFRESH = false;
                                        CURRENT = current;
                                        togoPay(order_id, map.get("advance_wei") + "", order_num);
                                    }
                                    if (code == -100) {
                                        Toast.makeText(mActivity, "下单已超出30分钟，订单失效", Toast.LENGTH_SHORT).show();
                                        map.put("order_status", 0);
                                        OrderListAdapter.this.notifyDataSetChanged();
                                    }
                                    if (code == -300) {
                                        Toast.makeText(mActivity, "未到尾款支付时间", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == -500) {
                                        Toast.makeText(mActivity, "超出尾款支付时间，订单失效", Toast.LENGTH_SHORT).show();
                                        map.put("order_status", 0);
                                        OrderListAdapter.this.notifyDataSetChanged();
                                    }
                                    mActivity.hideProcessDialog(0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            },
                            error -> mActivity
                                    .hideProcessDialog(1), map1);
                    mRequestQueue.add(request);
                }
            });
//            }
            str = "已付定金";
        }
        if (status == 0)
            str = "已取消";
        if (status == 10) {
            holder.button.setVisibility(View.VISIBLE);
            str = "待付款";
            if ((map.get("order_special") + "").equals("advance")) {
                holder.button.setText("定金支付");
                holder.button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            mActivity.showProcessDialog();
                            Map map1 = mActivity.getParaMap();
                            map1.put("order_id", paramap.get("order_id"));
                            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                            Request<JSONObject> request = new NormalPostRequest(
                                    mActivity,
                                    mActivity
                                            .getAddress()
                                            + "/app/buyer/advance_order_pay_verify.htm",
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(
                                                JSONObject result) {
                                            try {
                                                Log.e("++", result + "");
                                                int code = result
                                                        .getInt("code");
                                                if (code == 100) {
                                                    OrderAllTabFragment.REFRESH = false;
                                                    CURRENT = current;
                                                    togoPay(order_id, map.get("advance_din") + "", order_num);
                                                }
                                                if (code == -100) {
                                                    Toast.makeText(mActivity, "下单已超出30分钟，订单失效", Toast.LENGTH_SHORT).show();
                                                    map.put("order_status", 0);
                                                    OrderListAdapter.this.notifyDataSetChanged();
                                                }
                                                if (code == -300) {
                                                    Toast.makeText(mActivity, "未到尾款支付时间", Toast.LENGTH_SHORT).show();
                                                }
                                                if (code == -500) {
                                                    Toast.makeText(mActivity, "超出尾款支付时间，订单失效", Toast.LENGTH_SHORT).show();
                                                    map.put("order_status", 0);
                                                    OrderListAdapter.this.notifyDataSetChanged();
                                                }
                                                mActivity.hideProcessDialog(0);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    },
                                    new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(
                                                VolleyError error) {
                                            mActivity
                                                    .hideProcessDialog(1);
                                        }
                                    }, map1);
                            mRequestQueue.add(request);

                        }
                    }
                });
                holder.order_info.setVisibility(View.VISIBLE);
                holder.order_info.setText("（请在30分钟内支付定金）");
            } else {
                holder.button.setText("立即支付");
                holder.button.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {

                            OrderAllTabFragment.REFRESH = false;
                            CURRENT = current;
                            togoPay(order_id, order_total_price, order_num);
                        }
                    }
                });
            }
            if ("true".equals(promotion_flag)){
                holder.button2.setVisibility(View.VISIBLE);
                holder.button2.setText("取消订单");
                holder.button2.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {

                            OrderAllTabFragment.REFRESH = false;
                            new AlertDialog.Builder(mActivity)
                                    .setTitle("你确定要取消订单吗？")
                                    .setPositiveButton("确定", (dialog, which) -> goods_order_cancel(order_id))
                                    .setNegativeButton("取消", null)
                                    .show();
                        }
                    }
                });
            }else {
                holder.button2.setVisibility(View.INVISIBLE);
            }

        }
        if (status == 16)
            str = "待发货";
        if (status == 20) {
            str = "待发货";
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("申请退款");
            holder.button.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    new AlertDialog.Builder(mActivity).setTitle("你确定要申请退款吗？").setPositiveButton("确定",
                            (dialog, which) -> {
                                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/order_form_refund.htm", new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject result) {
                                                try {
                                                    int code = result.getInt("code");
                                                    if (code == 100) {
                                                        Toast.makeText(mActivity, "申请成功", Toast.LENGTH_SHORT).show();
                                                        if (mActivity.getCurrentfragment() == null)
                                                            return;
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt("current", current);
                                                        Intent intent = new Intent();
                                                        intent.putExtras(bundle);
                                                        mActivity.getCurrentfragment().onActivityResult(mActivity.getCurrentfragment().getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                                                        CURRENT = current;
                                                        OrderAllTabFragment.REFRESH = true;
                                                    }
                                                    if (code == -100) {
                                                    }
                                                    mActivity.hideProcessDialog(0);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        error -> mActivity.hideProcessDialog(1), paramap);
                                mRequestQueue.add(request);
                            }).setNegativeButton("取消", null).show();
                }
            });
        }
        if (status == 30) {
            str = "待收货";
            holder.button2.setVisibility(View.VISIBLE);
            holder.button2.setText("查看物流");
            holder.button2.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    OrderAllTabFragment.REFRESH = false;
                    mActivity.startActivity(new Intent(mActivity, TransActivity.class).putExtra("order_id", order_id));
                }
            });
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("确认收货");
            holder.button.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    OrderAllTabFragment.REFRESH = false;
                    new AlertDialog.Builder(mActivity)
                            .setTitle("您是否确定已经收货？")
                            .setPositiveButton("确定", (dialog, which) -> {
                                mActivity.showProcessDialog();
                                paramap.put("order_id", order_id);
                                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_order_cofirm.htm",
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject result) {
                                                try {
                                                    int code = result.getInt("code");
                                                    if (code == 100) {
                                                        if (mActivity.getCurrentfragment() == null)
                                                            return;
                                                        Bundle bundle = new Bundle();
                                                        bundle.putInt("current", current);
                                                        Intent intent = new Intent();
                                                        intent.putExtras(bundle);
                                                        mActivity.getCurrentfragment().onActivityResult(mActivity.getCurrentfragment().getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                                                        CURRENT = current;
                                                        OrderAllTabFragment.REFRESH = true;
                                                    }
                                                    if (code == -100) {
                                                    }
                                                    mActivity.hideProcessDialog(0);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(
                                                    VolleyError error) {
                                                mActivity
                                                        .hideProcessDialog(1);
                                            }
                                        }, paramap);
                                mRequestQueue.add(request);

                            }).setNegativeButton("取消", null).show();

                }
            });
        }
        if (status == 40) {
            str = "已收货";
            holder.button.setVisibility(View.VISIBLE);
            holder.button.setText("我要评价");
            holder.button.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                        Bundle bundle = new Bundle();
                        bundle.putString("order_id", map.get("order_id").toString());
                        mActivity.go_order_evaluate(bundle, mActivity.getCurrentfragment());
                        OrderAllTabFragment.REFRESH = false;
                    }
                }
            });
        }
        if (status == 50) {
            str = "已评价";
        }
        if (status == 65)
            str = "自动评价";
        if (status == 21) {
            str = "已申请退款";
        }
        if (status == 22) {
            str = "退款中";
        }
        if (status == 25) {
            str = "退款完成";
        }
        if (status == 35) {
            str = "自提点已收货";
        }
        holder.orderStatus.setText(str);

        return convertView;
    }

    private void togoPay(String order_id, String priceSubmit,
                         String order_num) {

        Bundle pay_bundle = new Bundle();
        pay_bundle.putString("totalPrice", priceSubmit);
        pay_bundle.putString("order_id", order_id);
        pay_bundle.putString("order_num", order_num);
        pay_bundle.putString("type", "goods");
        mActivity.go_pay(pay_bundle, "payonline");
    }

    void goods_order_cancel(String order_id) {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("order_id", order_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                mActivity,
                mActivity.getAddress() + "/app/buyer/goods_order_cancel.htm",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try {

                            int code = result.getInt("code");
                            if (code == 100) {
                                Toast.makeText(mActivity, "取消订单成功",
                                        Toast.LENGTH_SHORT).show();
                                if (mActivity.getCurrentfragment() == null)
                                    return;
                                Bundle bundle = new Bundle();
                                bundle.putInt("current", current);
                                Intent intent = new Intent();
                                intent.putExtras(bundle);
                                mActivity.getCurrentfragment().onActivityResult(mActivity.getCurrentfragment().getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                                CURRENT = current;
                                OrderAllTabFragment.REFRESH = true;
                            }
                            if (code == -100) {
                                Toast.makeText(mActivity, "用户信息不正确",
                                        Toast.LENGTH_SHORT).show();
                            }
                            if (code == -200) {
                                Toast.makeText(mActivity, "订单信息不正确",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mActivity.hideProcessDialog(0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mActivity.hideProcessDialog(1);
            }
        }, paramap);
        mRequestQueue.add(request);
    }

    public class ViewHolder {
        public TextView order_info;
        public TextView orderPrice;
        public TextView orderStatus;
        public Button button;
        public Button button2;
        public LinearLayout order_goods;
        public  TextView tv_order_price;
    }
}
