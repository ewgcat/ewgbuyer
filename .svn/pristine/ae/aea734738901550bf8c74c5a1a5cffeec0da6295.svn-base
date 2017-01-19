package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

/*
    商品订单详情页面
 */
public class OrderIntegralDetailFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Map paramap;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.paramap = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_order_integral_detail, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("订单详情");//设置标题
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }

        });
        setHasOptionsMenu(false);//设置菜单可用

        Bundle bundle = getArguments();
        paramap = mActivity.getParaMap();
        paramap.put("oid", bundle.get("oid"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order_detail.htm",
                data -> {
                    try {
                        Button button = (Button) rootView.findViewById(R.id.button);
                        TextView tv = (TextView) rootView.findViewById(R.id.mobilePhone);
                        tv.setText(data.getString("receiver_tel"));
                        tv = (TextView) rootView.findViewById(R.id.receiver);
                        tv.setText(data.getString("receiver_name"));
                        tv = (TextView) rootView.findViewById(R.id.address);
                        tv.setText(data.getString("receiver_address"));

                        JSONArray arr = data.getJSONArray("goods_list");
                        LinearLayout goodslayout = (LinearLayout) rootView.findViewById(R.id.order_search_item);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject jo = arr.getJSONObject(i);
                            RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.order_normal_goods, null);
                            TextView tv1 = (TextView) layout.findViewById(R.id.name);
                            tv1.setText(jo.getString("goods_name"));
                            tv1 = (TextView) layout.findViewById(R.id.count);
                            tv1.setText("×" + jo.getString("goods_count"));
                            SimpleDraweeView img = (SimpleDraweeView) layout.findViewById(R.id.img);
                            BaseActivity.displayImage(jo.getString("goods_img"), img);
                            goodslayout.addView(layout);
                        }

                        tv = (TextView) rootView.findViewById(R.id.paymentMethods);
                        tv.setText(data.getString("payType"));

                        tv = (TextView) rootView.findViewById(R.id.orderNumber);
                        tv.setText(data.getString("order_id"));
                        tv = (TextView) rootView.findViewById(R.id.order_integral);
                        tv.setText("兑换积分：" + data.getString("order_total_price"));
                        tv = (TextView) rootView.findViewById(R.id.order_trans_fee);
                        tv.setText("运费：￥" + mActivity.moneytodouble(data.getString("ship_price")));

                        tv = (TextView) rootView.findViewById(R.id.orderStatus);
                        int status = Integer.parseInt(data.get("igo_status").toString());

                        if (status == -1) {
                            tv.setText("已取消");
                        }
                        if (status == 0) {
                            tv.setText("待付款");
                            button.setText("去支付");
                            button.setVisibility(View.VISIBLE);
                            final String pay_method = data.getString("payType");

                            final Bundle bundle1 = new Bundle();
                            bundle1.putString("totalPrice", data.getString("ship_price"));
                            bundle1.putString("order_id", data.getString("oid"));
                            bundle1.putString("order_num", data.getString("order_id"));
                            bundle1.putString("type", "integral");
                            button.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    mActivity.go_pay(bundle1, "payonline");
                                }
                            });
                            rootView.findViewById(R.id.order_cancle).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.order_cancle).setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    // method stub
                                    new AlertDialog.Builder(mActivity).setTitle("你确定要取消订单吗？")
                                            .setPositiveButton("确定", (dialog, which) -> integral_order_cancel())
                                            .setNegativeButton("取消", null)
                                            .show();
                                }

                            });
                        }
                        if (status == 20)// 已付款
                            tv.setText("支付成功，待发货");
                        if (status == 30) {
                            tv.setText("已发货");
                            rootView.findViewById(R.id.express).setVisibility(View.VISIBLE);
                            tv = (TextView) rootView.findViewById(R.id.express_company_name);
                            tv.setText("物流公司：" + data.getString("express_company_name"));
                            tv = (TextView) rootView.findViewById(R.id.shipCode);
                            tv.setText("物流单号：" + data.getString("shipCode"));

                            button.setText("确认收货");
                            button.setVisibility(View.VISIBLE);
                            button.setOnClickListener(v -> {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    new AlertDialog.Builder(mActivity)
                                            .setTitle("您是否确已经收到兑换的礼品?")
                                            .setPositiveButton("确定", (dialog, which) -> integral_order_complete())
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            });
                        }
                        if (status == 40) {
                            tv.setText("已收货完成");
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu,true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
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
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void integral_order_cancel() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order_cancel.htm",
                result -> {
                    try {
                        if (result.getString("ret").equals("true")) {
                            getFragmentManager().popBackStack();
                            Toast.makeText(mActivity, "取消积分兑换成功，积分已返还",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "取消积分兑换失败",
                                    Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    void integral_order_complete() {

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order_complete.htm",
                result -> {
                    try {
                        if (result.getString("ret").equals("true")) {
                            getFragmentManager().popBackStack();
                            Toast.makeText(mActivity, "确认收货成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mActivity, "确认收货失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }



}
