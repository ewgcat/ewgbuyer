package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/*
    提交订单
 */
public class IntegralCartFragment2 extends Fragment {

    private double all_shipfee;
    private Bundle bundle;
    private BaseActivity mActivity;
    private View rootView;
    private String cartids;
    private String addr_id;
    private String goods_total_price;
    private String order_total_price;

    @Override
    public void onDetach() {
        super.onDetach();
        this.bundle = null;
        this.rootView = null;
        this.cartids = null;
        this.addr_id = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_integral_cart2, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("填写订单");//设置标题

        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });

        bundle = getArguments();
        cartids = bundle.getString("cartids");
        int all_integral = bundle.getInt("all_integral");
        all_shipfee = bundle.getDouble("all_shipfee");
        goods_total_price = bundle.getString("goods_total_price");
        order_total_price = bundle.getString("order_total_price");
        TextView tv = (TextView) rootView.findViewById(R.id.price);
        tv.setText("所需总积分：" + all_integral
                + "\n商品总金额：￥" + mActivity.moneytodouble(goods_total_price)
                + "\n运费：￥" + mActivity.moneytodouble(all_shipfee+"")
                + "\n订单总金额：￥" + mActivity.moneytodouble(order_total_price));

        initAddress();

        rootView.findViewById(R.id.manageRecieverInfo).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Bundle bundle1 =new Bundle();
                        bundle1.putInt("type",1);
                        mActivity.go_address_list(bundle1, mActivity.getCurrentfragment());
                    }
                });

        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        order_submit();
                    }
                });
        return rootView;
    }


    protected void order_submit() {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("cart_ids", cartids);
        EditText et = (EditText) rootView.findViewById(R.id.apply_reason);
        paramap.put("igo_msg", et.getText().toString());
        paramap.put("addr_id", addr_id);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/exchange2.htm",
                result -> {
                    try {
                        String code = result.get("code").toString();
                        if (code.equals("-100")) {
                            Toast.makeText(mActivity, "购物车为空", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("-200")) {
                            Toast.makeText(mActivity, "积分不足", Toast.LENGTH_SHORT).show();
                        } else if (code.equals("-300")) {
                            if (result.has("ig_name")){
                                Toast.makeText(mActivity, "商品 "+result.getString("ig_name")+"已下架", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            String order_sn = result.get("order_sn").toString();
                            String order_id = result.get("order_id").toString();
                            bundle = new Bundle();
                            bundle.putString("totalPrice", order_total_price);
                            bundle.putString("order_id", order_id);
                            bundle.putString("order_num", order_sn);
                            bundle.putString("type", "integral");
                            if (code.equals("100")) {
                                mActivity.go_pay(bundle, "payonline");
                            } else if (code.equals("200")) {
                                mActivity.go_success(bundle);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    private void initAddress() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/address_default.htm",
                result -> {
                    try {
                        if (!result.has("trueName")) {
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.GONE);
                        } else {
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.GONE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.VISIBLE);
                            TextView tx = (TextView) rootView.findViewById(R.id.receiver);
                            tx.setText(result.getString("trueName"));
                            tx = (TextView) rootView.findViewById(R.id.mobilePhone);
                            addr_id = result.get("addr_id").toString();
                            if(result.has("mobile")){
                                if(!result.get("mobile").toString().equals("")) {
                                    tx.setText(result.getString("mobile"));
                                }
                            }

                            if(result.has("telephone")) {
                                if(!result.get("telephone").toString().equals("")){
                                    tx.setText(result.getString("telephone"));
                                }
                            }
                            tx = (TextView) rootView.findViewById(R.id.address);
                            tx.setText(result.getString("area") + result.getString("areaInfo"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), mActivity.getParaMap());
        mRequestQueue.add(request);

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        if (preferences.contains("payresult")) {
            String payresult = preferences.getString("payresult", "");
            Editor editor = preferences.edit();
            editor.putString("paytype", "");
            editor.putString("payresult", "");
            editor.putString("type", "");
            editor.putString("order_id", "");
            editor.putString("order_num", "");
            editor.commit();

            mActivity.hideProcessDialog(1);
            if (payresult.equals("success")) {
                mActivity.go_success(bundle);
            } else if (payresult.equals("fail")) {
                pay_fail();
                Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
            } else if (payresult.equals("cancle")) {
                pay_fail();
                Toast.makeText(mActivity, "支付已取消", Toast.LENGTH_SHORT).show();
            } else if (payresult.equals("error")) {
                pay_fail();
                Toast.makeText(mActivity, "支付出错", Toast.LENGTH_SHORT).show();
            }
        }
    }

    void pay_fail() {
        Bundle order_bundle = new Bundle();
        order_bundle.putString("oid", bundle.getString("order_id"));
        mActivity.go_index();
        mActivity.go_usercenter();
        mActivity.go_order_integral_list();
        mActivity.go_order_integral_detail(order_bundle);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==Cart2Fragment.ADDRESS){
            initAddress();
        }
    }
}
