package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.TransActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    评价列表进入的订单详情页面
 */
public class OrderNormalDetailFragment extends Fragment {
    RelativeLayout transitem;
    private BaseActivity mActivity;
    private View rootView;
    private Map paramap;
    private int current;
    private Dialog myDialog;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.paramap = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_normal_detail, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("订单详情");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }

        });
        setHasOptionsMenu(false);//设置菜单可用

        transitem = (RelativeLayout) inflater.inflate(R.layout.item_trans, null).findViewById(R.id.trans_item);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     *
     * @param menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (OrderAllTabFragment.REFRESH) {
            init();
        } else {
            OrderAllTabFragment.REFRESH = true;
        }
    }

    public void init() {
        mActivity.showProcessDialog();
        Bundle bundle = getArguments();
        final String order_id = bundle.getString("id");
        current = bundle.getInt("current", 0);
        paramap = mActivity.getParaMap();
        paramap.put("order_id", order_id);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_order_view.htm",
                result -> {
                    try {



                        int status = result.getInt("order_status");
                        final String order_total_price = result.getString("order_total_price");
                        final String order_num = result.getString("order_num");
                        Button orderDetail_state_first_red = (Button) rootView.findViewById(R.id.orderDetail_state_first_red);
                        Button orderDetail_state_first_white = (Button) rootView.findViewById(R.id.orderDetail_state_first_white);
                        Button orderDetail_state_sec = (Button) rootView.findViewById(R.id.orderDetail_state_sec);
                        final RelativeLayout layout_order_detail_bottom = (RelativeLayout) rootView.findViewById(R.id.layout_order_detail_bottom);
                        orderDetail_state_first_red.setVisibility(View.GONE);
                        orderDetail_state_first_white.setVisibility(View.GONE);
                        orderDetail_state_sec.setVisibility(View.GONE);
                        layout_order_detail_bottom.setVisibility(View.GONE);
                        String str = "";
                        if (result.has("promotion_flag")) {
                            String promotion_flag = result.getString("promotion_flag");
                            if (promotion_flag.equals("false")) {
                                orderDetail_state_sec.setVisibility(View.VISIBLE);
                                orderDetail_state_first_white.setVisibility(View.GONE);
                            } else if (promotion_flag.equals("true")) {
                                orderDetail_state_sec.setVisibility(View.GONE);
                                orderDetail_state_first_white.setVisibility(View.VISIBLE);
                            }
                        }


                        if (status == 0) {
                            str = "已取消";
                        }
                        if (status == 10) {
                            str = "待付款";
                            layout_order_detail_bottom.setVisibility(View.VISIBLE);
                            orderDetail_state_first_red.setVisibility(View.VISIBLE);
//                                if (result.has("order_special")) {
                            if ((result.get("order_special") + "").equals("advance")) {
                                orderDetail_state_first_red.setText("定金支付");
                                orderDetail_state_first_red.setOnClickListener(new OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        if (FastDoubleClickTools.isFastDoubleClick()) {
                                            mActivity.showProcessDialog();
                                            Map map1 = mActivity.getParaMap();
                                            map1.put("order_id", order_id);
                                            RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                            Request<JSONObject> request1 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/advance_order_pay_verify.htm",
                                                    code_result -> {
                                                        try {
                                                            int code = code_result.getInt("code");
                                                            if (code == 100) {
                                                                OrderAllTabFragment.REFRESH = false;
                                                                try {
                                                                    togoPay(order_id, result.get("advance_din") + "", order_num);
                                                                } catch (JSONException e) {
                                                                }
                                                            }
                                                            if (code == -100) {
                                                                Toast.makeText(mActivity, "下单已超出30分钟，订单失效", Toast.LENGTH_SHORT).show();
                                                                init();
                                                            }
                                                            if (code == -300) {
                                                                Toast.makeText(mActivity, "未到尾款支付时间", Toast.LENGTH_SHORT).show();
                                                            }
                                                            if (code == -500) {
                                                                Toast.makeText(mActivity, "超出尾款支付时间，订单失效", Toast.LENGTH_SHORT).show();
                                                                init();
                                                            }
                                                            mActivity.hideProcessDialog(0);
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }
                                                    },
                                                    error -> mActivity.hideProcessDialog(1), map1);
                                            mRequestQueue1.add(request1);

                                        }
                                    }
                                });
//                                    }
                            } else {
                                orderDetail_state_first_red.setText("立即支付");
                                orderDetail_state_first_red.setOnClickListener(v -> {
                                    if (FastDoubleClickTools.isFastDoubleClick()) {
                                        OrderAllTabFragment.REFRESH = false;
                                        togoPay(order_id, order_total_price, order_num);
                                    }
                                });
                            }
                            orderDetail_state_sec.setVisibility(View.VISIBLE);
                            orderDetail_state_sec.setText("取消订单");
                            orderDetail_state_sec.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    new AlertDialog.Builder(mActivity)
                                            .setTitle("你确定要取消订单吗？")
                                            .setPositiveButton(
                                                    "确定",
                                                    (dialog, which) -> goods_order_cancel())
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                        }
                        if (status == 11) {
                            layout_order_detail_bottom.setVisibility(View.VISIBLE);
//                                if (!(result.has("advance_wei") ? result.get("advance_wei") + "" : "").equals("")) {
                            str = "已付定金";
                            orderDetail_state_first_red.setVisibility(View.VISIBLE);
                            orderDetail_state_first_red.setText("尾款支付");
                            orderDetail_state_first_red.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    mActivity.showProcessDialog();
                                    Map map1 = mActivity.getParaMap();
                                    map1.put("order_id", order_id);
                                    RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                    Request<JSONObject> request1 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/advance_order_pay_verify.htm",
                                            code_result -> {
                                                try {
                                                    int code = code_result.getInt("code");
                                                    if (code == 100) {
                                                        OrderAllTabFragment.REFRESH = false;
                                                        try {
                                                            togoPay(order_id, result.get("advance_wei") + "", order_num);
                                                        } catch (JSONException e) {
                                                        }
                                                    }
                                                    if (code == -100) {
                                                        Toast.makeText(mActivity, "下单已超出30分钟，订单失效", Toast.LENGTH_SHORT).show();
                                                        init();
                                                    }
                                                    if (code == -300) {
                                                        Toast.makeText(mActivity, "未到尾款支付时间", Toast.LENGTH_SHORT).show();
                                                    }
                                                    if (code == -500) {
                                                        Toast.makeText(mActivity, "超出尾款支付时间，订单失效", Toast.LENGTH_SHORT).show();
                                                        init();
                                                    }
                                                    mActivity.hideProcessDialog(0);
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            },
                                            error -> mActivity.hideProcessDialog(1), map1);
                                    mRequestQueue1.add(request1);

                                }
                            });
                            orderDetail_state_sec.setVisibility(View.VISIBLE);
                            orderDetail_state_sec.setText("取消订单");
                            orderDetail_state_sec.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {

                                    new AlertDialog.Builder(mActivity)
                                            .setTitle("你确定要取消订单吗？")
                                            .setPositiveButton(
                                                    "确定",
                                                    (dialog, which) -> goods_order_cancel())
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                        }
                        if (status == 16) {// 货到付款
                            str = "待发货";
                        }
                        if (status == 20) {// 已付款
                            str = "待发货";
                            layout_order_detail_bottom.setVisibility(View.VISIBLE);
                            orderDetail_state_sec.setVisibility(View.GONE);
                            orderDetail_state_first_white.setVisibility(View.VISIBLE);
                            orderDetail_state_first_white.setText("申请退款");
                            orderDetail_state_first_white.setOnClickListener(v -> {

                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    new AlertDialog.Builder(mActivity).setTitle("你确定要申请退款吗？").setPositiveButton("确定",
                                            new DialogInterface.OnClickListener() {

                                                @Override
                                                public void onClick(
                                                        DialogInterface dialog,
                                                        int which) {
                                                    RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                                    Request<JSONObject> request1 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/order_form_refund.htm",
                                                            result1 -> {
                                                                try {
                                                                    int code = result1.getInt("code");
                                                                    if (code == 100) {
                                                                        Toast.makeText(mActivity, "申请成功", Toast.LENGTH_SHORT).show();
                                                                        if (mActivity.getCurrentfragment() == null)
                                                                            return;
                                                                        if (!SuccessFragment.flag) {
                                                                            Bundle bundle1 = new Bundle();
                                                                            bundle1.putInt("current", current);
                                                                            Intent intent = new Intent();
                                                                            intent.putExtras(bundle1);
                                                                            mActivity.getCurrentfragment().onActivityResult(mActivity.getCurrentfragment().getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                                                                            OrderAllTabFragment.REFRESH = true;
                                                                        } else {
                                                                            init();
                                                                            layout_order_detail_bottom.setVisibility(View.GONE);
                                                                        }
                                                                    }
                                                                    if (code == -100) {
                                                                    }
                                                                    mActivity.hideProcessDialog(0);
                                                                } catch (JSONException e) {
                                                                    e.printStackTrace();
                                                                }
                                                            },
                                                            error -> mActivity.hideProcessDialog(1), paramap);
                                                    mRequestQueue1.add(request1);
                                                }
                                            }).setNegativeButton("取消", null).show();
                                }
                            });
                        }
                        if (status == 30) {
                            str = "待收货";
                            layout_order_detail_bottom.setVisibility(View.VISIBLE);

                            orderDetail_state_first_white.setVisibility(View.VISIBLE);
                            orderDetail_state_first_white.setText("确认收货");
                            orderDetail_state_first_white.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {

                                    new AlertDialog.Builder(mActivity)
                                            .setTitle("您是否确定已经收货？")
                                            .setPositiveButton(
                                                    "确定",
                                                    (dialog, which) -> goods_order_cofirm())
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                        }
                        if (status == 40) {
                            str = "已收货";
                            layout_order_detail_bottom.setVisibility(View.VISIBLE);
                            orderDetail_state_sec.setVisibility(View.GONE);
                            orderDetail_state_first_white.setText("立即评价");
                            orderDetail_state_first_white.setVisibility(View.VISIBLE);
                            orderDetail_state_first_white.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {

                                    Bundle bundle1 = new Bundle();
                                    bundle1.putString("order_id", order_id);
                                    mActivity.go_order_evaluate(bundle1, mActivity.getCurrentfragment());
                                }
                            });
                        }
                        if (status == 50) {
                            str = "已评价";
                        }
                        if (status == 65) {
                            str = "自动评价";
                        }
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
                        TextView tv = (TextView) rootView.findViewById(R.id.orderStatus);
                        tv.setText(str);
                        tv = (TextView) rootView.findViewById(R.id.orderNumber);
                        tv.setText(result.getString("order_num"));
                        tv = (TextView) rootView.findViewById(R.id.addtime);
                        tv.setText(result.getString("addTime"));

                        tv = (TextView) rootView.findViewById(R.id.orderAmount);
                        tv.setText("￥" + result.getString("order_total_price"));
                        tv = (TextView) rootView.findViewById(R.id.totalMerchandise);
                        tv.setText("￥" + result.getString("goods_price"));

                        tv = (TextView) rootView.findViewById(R.id.freight);
                        if (result.has("ship_price") && !TextUtils.isEmpty(result.getString("ship_price"))) {
                            tv.setText(result.getString("ship_price"));
                        } else {
                            tv.setText("+￥0.00");
                        }
                        tv = (TextView) rootView.findViewById(R.id.coupons);
                        if (result.has("coupon_price") && !TextUtils.isEmpty(result.getString("coupon_price"))) {
                            tv.setText(result.getString("coupon_price"));
                        } else {
                            tv.setText("-￥0.00");
                        }



//                            if (Double.parseDouble(result
//                                    .getString("reduce_amount")) > 0) {
//                                rootView.findViewById(R.id.reduce_tag)
//                                        .setVisibility(View.VISIBLE);
                        tv = (TextView) rootView
                                .findViewById(R.id.reduce_amount);
                        tv.setText("-￥"
                                + result.getString("reduce_amount"));
//                            } else {
//                                rootView.findViewById(R.id.reduce_tag)
//                                        .setVisibility(View.GONE);
//                            }

                        tv = (TextView) rootView.findViewById(R.id.username);
                        tv.setText(result.getString("receiver_Name"));
                        tv = (TextView) rootView.findViewById(R.id.userphone);
                        if (result.has("receiver_telephone") && !TextUtils.isEmpty(result.getString("receiver_telephone"))) {
                            tv.setText(result.getString("receiver_telephone"));
                        } else if (result.has("receiver_telephone") && !TextUtils.isEmpty(result.getString("receiver_telephone"))) {
                            tv.setText(result.getString("receiver_mobile"));
                        }

                        tv = (TextView) rootView.findViewById(R.id.address);
                        tv.setText("收货地址: " + result.getString("receiver_area") + result.getString("receiver_area_info"));
                        if (result.has("payType")) {
                            tv = (TextView) rootView
                                    .findViewById(R.id.paymentMethods);
                            if (status == 11) {
                                tv.setText("已付定金");
                            } else {
                                tv.setText(result.getString("payType"));
                            }
                        }
//                            tv = (TextView) rootView
//                                    .findViewById(R.id.invoiceTile);
//                            tv.setText(result.getString("invoiceType"));
//                            tv = (TextView) rootView
//                                    .findViewById(R.id.invoiceTile);
//                            tv.setText("抬头:" + result.getString("invoice"));


                        JSONArray arr = result.getJSONArray("goods_list");
                        Log.i("test",arr.toString());
                        int length = arr.length();
                        LinearLayout goodslayout = (LinearLayout) rootView
                                .findViewById(R.id.goods);
                        goodslayout.removeAllViews();
                        goodslayout.setOrientation(LinearLayout.VERTICAL);

                        for (int i = 0; i < length; i++) {
                            final JSONObject jo = arr.getJSONObject(i);
                            View layout = LayoutInflater.from(mActivity).inflate(R.layout.order_normal_single_goods, null);
                            if (i == 0) {
                                layout.findViewById(R.id.tv_order_detail_title).setVisibility(View.VISIBLE);
                            } else {
                                layout.findViewById(R.id.tv_order_detail_title).setVisibility(View.GONE);
                            }
                            TextView tv1 = (TextView) layout.findViewById(R.id.goods_name);
                            tv1.setText(jo.getString("goods_name"));
                            tv1 = (TextView) layout.findViewById(R.id.goods_count);
                            tv1.setText("×" + jo.getString("goods_count"));
                            tv1 = (TextView) layout.findViewById(R.id.goods_gsp);
                            tv1.setText(jo.getString("goods_gsp_val"));
                            tv1 = (TextView) layout.findViewById(R.id.goods_price);
                            tv1.setText("￥" + jo.getString("goods_price"));
                            SimpleDraweeView img = (SimpleDraweeView) layout.findViewById(R.id.goods_img);

                            BaseActivity.displayImage(jo.getString("goods_mainphoto_path"), img);
                            layout.setOnClickListener(view -> {
                                try {
                                    mActivity.go_goods(jo.get("goods_id") + "");
                                } catch (Exception e) {
                                }
                            });
                            if ((result.get("order_special") + "").equals("advance")) {
                                layout.findViewById(R.id.ll_advance_details).setVisibility(View.VISIBLE);
                                TextView tv_advance_details = (TextView) layout.findViewById(R.id.tv_advance_details);
                                tv_advance_details.setText("定金: ¥" + result.get("advance_din") + "   尾款: ¥" + result.get("advance_wei"));
                            } else {
                                layout.findViewById(R.id.ll_advance_details).setVisibility(View.GONE);
                            }
                            if ((jo.get("goods_type") + "").equals("combin")) {
                                layout.findViewById(R.id.ll_combin_details).setVisibility(View.VISIBLE);
                                Button bt_details = (Button) layout.findViewById(R.id.bt_details);
                                bt_details.setText("套装详情");
                                bt_details.setOnClickListener(view -> {
                                    try {
                                        JSONArray jsonArray = jo.getJSONArray("combin_info");
                                        List dataList = new ArrayList();
                                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i1);
                                            Map map = new HashMap();
                                            map.put("img", jsonObject.get("img"));
                                            map.put("id", jsonObject.get("id"));
                                            map.put("name", jsonObject.get("name"));
                                            dataList.add(map);
                                        }
                                        myDialog = MyDialog.showAlert(mActivity, "套装详情", dataList, "", view12 -> {

                                        }, "确定", view1 -> myDialog.dismiss(), 1);
                                    } catch (Exception e) {
                                    }
                                });
                            } else {
                                layout.findViewById(R.id.ll_combin_details).setVisibility(View.GONE);
                            }
                            goodslayout.addView(layout);
                        }
                        final List dataList = new ArrayList();
                        if (result.has("gif_info")) {
                            JSONArray jsonArray = result.getJSONArray("gif_info");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("img", jsonObject.get("img"));
                                map.put("id", jsonObject.get("id"));
                                map.put("name", jsonObject.get("name"));
                                dataList.add(map);
                            }
                        }
                        LinearLayout suitlayout = (LinearLayout) rootView.findViewById(R.id.suit);
                        if (dataList.size() == 0) {
                            suitlayout.setVisibility(View.GONE);
                        } else {
                            suitlayout.setVisibility(View.VISIBLE);
                        }
                        suitlayout.setOrientation(LinearLayout.VERTICAL);
                        for (int j = 0; j < dataList.size(); j++) {
                            Map map = (Map) dataList.get(j);
                            View layout = LayoutInflater.from(mActivity).inflate(R.layout.order_normal_single_goods, null);
                            if (j == 0) {
                                TextView tv_order_detail_title = (TextView) layout.findViewById(R.id.tv_order_detail_title);
                                tv_order_detail_title.setVisibility(View.VISIBLE);
                                tv_order_detail_title.setText("赠品清单");
                            } else {
                                layout.findViewById(R.id.tv_order_detail_title).setVisibility(View.GONE);
                            }
                            TextView tv1 = (TextView) layout.findViewById(R.id.goods_name);
                            tv1.setText(map.get("name") + "");

                            SimpleDraweeView img = (SimpleDraweeView) layout.findViewById(R.id.goods_img);
                            BaseActivity.displayImage(map.get("img") + "", img);
                            suitlayout.addView(layout);
                        }

                        LinearLayout translayout = (LinearLayout) rootView.findViewById(R.id.trans_layout);
                        translayout.removeAllViews();
                        JSONArray trans_list = result.getJSONArray("trans_list");
                        int translenght = trans_list.length();
                        RelativeLayout rl_trans = (RelativeLayout) rootView.findViewById(R.id.rl_trans);
                        for (int i = 0; i < length; i++) {

                            final JSONObject tran = trans_list.getJSONObject(i);

                            rl_trans.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    try {
                                        OrderAllTabFragment.REFRESH = false;
                                        startActivity(new Intent(mActivity, TransActivity.class).putExtra("order_id", tran.get("train_order_id") + ""));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            if (tran.has("shipCode")) {
                                rootView.findViewById(R.id.notrans).setVisibility(View.GONE);

                                if (translenght > 1) {
                                    rootView.findViewById(R.id.manytrans).setVisibility(View.VISIBLE);
                                }
                                RelativeLayout transitem1 = (RelativeLayout) LayoutInflater.from(mActivity).inflate(R.layout.item_trans, null).findViewById(R.id.trans_item);
                                if (i == 0) {
                                    transitem1.findViewById(R.id.div).setVisibility(View.GONE);
                                }
                                TextView tv1 = (TextView) transitem1.findViewById(R.id.transID);
                                tv1.setText(tran.getString("shipCode"));
                                tv1 = (TextView) transitem1.findViewById(R.id.transCompany);
                                tv1.setText(tran.getString("express_company"));
                                tv1 = (TextView) transitem1.findViewById(R.id.transTime);
                                tv1.setText(tran.getString("shipTime"));

                                tv1 = (TextView) transitem1.findViewById(R.id.transSearch);
                                tv1.setOnClickListener(v -> {
                                    if (FastDoubleClickTools.isFastDoubleClick()) {
                                        try {
                                            OrderAllTabFragment.REFRESH = false;
                                            startActivity(new Intent(mActivity, TransActivity.class).putExtra("order_id", tran.get("train_order_id") + ""));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                });
                                translayout.addView(transitem1);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getTargetFragment() == null)
            return;
        Bundle bundle = new Bundle();
        bundle.putInt("current", current);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getTargetFragment().onActivityResult(getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
        getFragmentManager().popBackStack();
    }

    void goods_order_cancel() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                mActivity,
                mActivity.getAddress() + "/app/buyer/goods_order_cancel.htm",
                result -> {
                    try {
                        int code = result.getInt("code");
                        if (code == 100) {
                            Toast.makeText(mActivity, "取消订单成功", Toast.LENGTH_SHORT).show();
                            if (getTargetFragment() == null)
                                return;
                            Bundle bundle = new Bundle();
                            bundle.putInt("current", current);
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                            getFragmentManager().popBackStack();
                        }
                        if (code == -100) {
                            Toast.makeText(mActivity, "用户信息不正确", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -200) {
                            Toast.makeText(mActivity, "订单信息不正确", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    void goods_order_cofirm() {
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                mActivity,
                mActivity.getAddress() + "/app/buyer/goods_order_cofirm.htm",
                result -> {
                    try {
                        int code = result.getInt("code");
                        if (code == 100) {
                            Toast.makeText(mActivity, "确认收货成功", Toast.LENGTH_SHORT).show();
                            if (getTargetFragment() == null)
                                return;
                            Bundle bundle = new Bundle();
                            bundle.putInt("current", current);
                            Intent intent = new Intent();
                            intent.putExtras(bundle);
                            getTargetFragment().onActivityResult(getTargetRequestCode(), OrderEvaluateFragment.NUM, intent);
                            getFragmentManager().popBackStack();
                        }
                        if (code == -100) {
                            Toast.makeText(mActivity, "用户信息不正确", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    private void togoPay(String order_id, String priceSubmit, String order_num) {

        Bundle pay_bundle = new Bundle();
        pay_bundle.putString("totalPrice", priceSubmit);
        pay_bundle.putString("order_id", order_id);
        pay_bundle.putString("order_num", order_num);
        pay_bundle.putString("type", "goods");
        mActivity.go_pay(pay_bundle, "payonline");

    }


}
