package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.models.SerializableMap;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车第二部,填写订单
 */
public class Cart2Fragment extends Fragment {
    public static final int TRANS_AND_PAY = 1;
    public static final int INVOICE = 2;
    public static final int COUPONS = 3;
    public static final int ADDRESS = 4;
    Dialog myDialog;
    private BaseActivity mActivity;
    private View rootView;
    private Map<String, String> transValueMap = new HashMap();
    private Map<String, String> transStore_idValueMap = new HashMap();
    private String order_addr_id = "";
    private String invoiceType = "普通发票";
    private String invoiceTitle = "个人";
    private String cart_ids_temp = "";
    private String store_id_temp = "";
    private String payName = "在线支付";
    private String order_goods_price_temp = "0";
    private String coupons_id = "";
    private String coupon_amount = "0";
    private String priceSubmit = "0";
    private Bundle bundle;
    private Boolean vatInvoice_boolran = false;
    private boolean inventory = false;//库存验证
    private String[] invoiceString = null;
    private String delivery_time = "";
    private boolean hasDefaut_address;
    private boolean hasCard;
    private boolean needCardAddress;
    Cart2Fragment mCart2Fragment;
    private String addressId;
    private RequestQueue mRequestQueue;
    boolean isSelfPickup = false;//默认不自提
    private String promotion_flag;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.transValueMap = null;
        this.transStore_idValueMap = null;
        this.invoiceType = null;
        this.invoiceTitle = null;
        this.cart_ids_temp = null;
        this.store_id_temp = null;
        this.payName = null;
        this.order_goods_price_temp = null;
        this.coupons_id = null;
        this.coupon_amount = null;
        this.priceSubmit = null;
        this.bundle = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart2, container, false);
        mActivity = (BaseActivity) getActivity();
        mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        mCart2Fragment = this;
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.fillOrder));//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });

        rootView.findViewById(R.id.manageRecieverInfo).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                Bundle bundle1 = new Bundle();
                bundle1.putInt("type", 1);
                mActivity.go_address_list(bundle1, mActivity.getCurrentfragment());
            }
        });

        bundle = getArguments();
        invoiceString = new String[7];
        initData();
        return rootView;
    }

    //初始化数据
    void initData() {
        Map paramap = mActivity.getParaMap();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/buyer/address_default.htm",
                result -> {
                    try {
                        if (!result.has("trueName")) {
                            //默认地址不存在
                            hasDefaut_address = false;
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.GONE);
                            transStore_idValueMap.clear();
                            payName = "";
                            TextView tv = (TextView) rootView.findViewById(R.id.paymentMethods);
                            tv.setText("请选择支付方式");
                            tv = (TextView) rootView.findViewById(R.id.deliveryMethods);
                            tv.setText("请选择配送方式");
                            calculatePrice();
                        } else {
                            //有真实姓名，就有默认地址
                            hasDefaut_address = true;
                            //判断有无身份证
                            hasCard = !result.get("card").toString().equals("");
                            //TODO 添加身份证显示
                            if (hasCard) {
                                rootView.findViewById(R.id.card).setVisibility(View.VISIBLE);
                            } else {
                                rootView.findViewById(R.id.card).setVisibility(View.INVISIBLE);
                            }
                            rootView.findViewById(R.id.norecieverInfo).setVisibility(View.GONE);
                            rootView.findViewById(R.id.recieverInfo).setVisibility(View.VISIBLE);
                            TextView tx = (TextView) rootView.findViewById(R.id.receiver);
                            tx.setText(result.getString("trueName"));
                            tx = (TextView) rootView.findViewById(R.id.mobilePhone);
                            String telephone = "没填手机号";
                            if (result.has("telephone") && !TextUtils.isEmpty(result.getString("telephone"))) {
                                telephone = result.getString("telephone");
                            } else if (result.has("mobile") && !TextUtils.isEmpty(result.getString("mobile"))) {
                                telephone = result.getString("mobile");
                            }
                            tx.setText(telephone);
                            tx = (TextView) rootView.findViewById(R.id.address);
                            tx.setText(result.getString("area") + result.getString("areaInfo"));
                            if (!order_addr_id.equals("" + result.getInt("addr_id"))) {
                                order_addr_id = "" + result.getInt("addr_id");
                            }
                            Map paramap1 = mActivity.getParaMap();
                            paramap1.put("cart_ids", bundle.getString("cart_ids"));
                            getOrderGoodsInfo(paramap1);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paramap);
        mRequestQueue.add(request);
    }


    //请求订单商品信息，成功后请求订单详情信息
    public void getOrderGoodsInfo(Map paramap) {
        Request<JSONObject> request2 = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/goods_cart2_goodsInfo.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("goods_list");
                        int length = arr.length();
                        String goods_ids = "";
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = arr.getJSONObject(i);
                            String channel_type = jo.get("channel_type").toString();
                            if ("1".equals(channel_type) || "2".equals(channel_type)) {
                                needCardAddress = true;
                                break;
                            }
                        }
                        for (int i = 0; i < length; i++) {
                            JSONObject jo = arr.getJSONObject(i);
                            goods_ids += jo.getString("goods_id") + ",";
                        }
                        bundle.putString("goods_ids", goods_ids);
                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                        String user_id = preferences.getString("user_id", "");
                        String token = preferences.getString("token", "");
                        bundle.putString("user_id", user_id);
                        bundle.putString("token", token);
                        rootView.findViewById(R.id.coupons).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                mActivity.go_cart_coupon(bundle, Cart2Fragment.this);
                            }
                        });

                        //TODO 支付配送方式
                        rootView.findViewById(R.id.payAndDeliverMethod).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {

                                if ("".equals(order_addr_id)) {
                                    Toast.makeText(mActivity, "请先填写收货人信息", Toast.LENGTH_SHORT).show();
                                } else {
                                    bundle.putString("cart_ids_temp", cart_ids_temp);
                                    bundle.putString("order_addr_id", order_addr_id);
                                    bundle.putString("store_id_temp", store_id_temp);
                                    mActivity.go_cart_trans_pay(bundle, Cart2Fragment.this);
                                }
                            }
                        });
                        //TODO 提交订单
                        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                orderSubmit();
                            }
                        });

                        rootView.findViewById(R.id.cartlist).setOnClickListener(v -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                bundle.putString("cart_ids_temp", cart_ids_temp);
                                mActivity.go_cart_goods_list(bundle);
                            }
                        });
                        Map paramap1 = mActivity.getParaMap();
                        paramap1.put("cart_ids", bundle.getString("cart_ids"));// 提交的购物车id
                        getOrderDetail(paramap1);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paramap);
        mRequestQueue.add(request2);
    }

    //请求订单详情信息，成功后请求支付方式和运送方式
    public void getOrderDetail(Map paramap) {
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart2.htm",
                order_result -> {
                    try {

                        if (order_result.has("promotion_flag")){
                            promotion_flag = order_result.getString("promotion_flag");
                            if (promotion_flag.equals("false")){
                                rootView.findViewById(R.id.ll_promotion_code).setVisibility(View.INVISIBLE);
                            }else  if (promotion_flag.equals("true")){
                                rootView.findViewById(R.id.ll_promotion_code).setVisibility(View.VISIBLE);
                                if (order_result.has("promotion_code")){
                                    String promotion_code = order_result.getString("promotion_code");
                                    EditText et_promotion_code = (EditText) rootView.findViewById(R.id.et_promotion_code);
                                    et_promotion_code.setText(promotion_code);
                                }
                            }
                        }
                        // 根据已选择购物车设置订单详情
                        vatInvoice_boolran = order_result.has("vatInvoice");
                        if (order_result.has("vatInvoice")) {
                            JSONObject jsonObject = order_result.getJSONObject("vatInvoice");
                            invoiceString[0] = jsonObject.get("companyName").toString();
                            invoiceString[1] = jsonObject.get("registerAddress").toString();
                            invoiceString[2] = jsonObject.get("registerPhone").toString();
                            invoiceString[3] = jsonObject.get("registerbankAccount").toString();
                            invoiceString[4] = jsonObject.get("registerbankName").toString();
                            invoiceString[5] = jsonObject.get("taxpayerCode").toString();
                            invoiceString[6] = jsonObject.get("vat_id").toString();
                        }
                        cart_ids_temp = order_result.getString("cart_ids");
                        store_id_temp = order_result.getString("store_ids");
                        order_goods_price_temp = order_result.getString("order_goods_price");
                        getPaytypeAndTrans(cart_ids_temp, store_id_temp);
                        calculatePrice();

                        TextView order_price = (TextView) rootView.findViewById(R.id.order_price);
                        order_price.setText("¥" + mActivity.moneytodouble(order_goods_price_temp));// 订单商品总价钱
                        TextView goods_price = (TextView) rootView.findViewById(R.id.goods_price);
                        goods_price.setText("¥" + mActivity.moneytodouble(order_goods_price_temp));// 订单总价钱
                        if (order_result.has("reduce")) {
                            TextView reduce = (TextView) rootView.findViewById(R.id.reduce);
                            reduce.setText("-¥" + mActivity.moneytodouble(order_result.getString("reduce")));// 满减金额
                            goods_price.setText("¥" + mActivity.moneytodouble(order_result.getString("before")));// 满减前总价
                        } else {
                            rootView.findViewById(R.id.reduce).setVisibility(View.GONE);
                            rootView.findViewById(R.id.reduce_tag).setVisibility(View.GONE);
                        }
                        bundle.putString("store_ids", store_id_temp);
                        bundle.putString("order_goods_price", order_goods_price_temp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paramap);
        mRequestQueue.add(request);
    }


    //获取支付方式和运送方式
    private void getPaytypeAndTrans(String cart_ids_temp, String store_id_temp) {
        Map paramap = mActivity.getParaMap();
        paramap.put("cart_ids", cart_ids_temp);
        paramap.put("store_ids", store_id_temp);
        paramap.put("addr_id", order_addr_id);
//        Log.i("test","cart_ids=="+cart_ids_temp+",store_ids=="+store_id_temp+",addr_id=="+order_addr_id);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/goods_cart2_cartsTrans.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    //Log.i("test",jsonObject.toString());
                    Map<String, String> shopmoneyMap = new HashMap<String, String>();
                    if (jsonObject.has("trans_list") && jsonObject.get("trans_list") != null) {
                        JSONArray trans_list = jsonObject.getJSONArray("trans_list");
                        int trans_length = trans_list.length();
                        for (int i = 0; i < trans_length; i++) {
                            JSONObject oj = trans_list.getJSONObject(i);
                            JSONArray transInfo_list = oj.getJSONArray("transInfo_list");
                            int transInfo_length = transInfo_list.length();
                            List<String> deliverthods = new ArrayList<String>();
                            final List<String> delivervalues = new ArrayList<String>();
                            for (int j = 0; j < transInfo_length; j++) {
                                JSONObject jo = transInfo_list.getJSONObject(j);
                                String key = jo.getString("key");
                                String value = jo.getString("value");
                                if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                                    transValueMap.put(key, value);
                                    // 快递方式 价格 map
                                    deliverthods.add(key);
                                    delivervalues.add(value);
                                }
                            }

                            for (int m = 0; m < deliverthods.size(); m++) {
                                transStore_idValueMap.put(oj.getString("store_id"), deliverthods.get(m));
                            }
                            String str = "";
                            for (int m = 0; m < deliverthods.size(); m++) {
                                str += deliverthods.get(m);
                            }
                            shopmoneyMap.put(oj.getString("store_name"), str);
                        }
                        String trans_pay = "";
                        for (String str : shopmoneyMap.keySet()) {
                            trans_pay += str + "：" + shopmoneyMap.get(str) + "\n";
                        }
                        if (trans_pay.length() > 1) {
                            trans_pay = trans_pay.substring(0, trans_pay.length() - 1);
                        }

                        TextView tv = (TextView) rootView.findViewById(R.id.deliveryMethods);
                        if (TextUtils.isEmpty(trans_pay)) {
                            tv.setText("自提");
                            isSelfPickup = true;
                        } else {
                            tv.setText(trans_pay);
                            isSelfPickup = false;
                        }
                        calculatePrice();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点击提交订单按钮触发方法
    protected void orderSubmit() {
        calculatePrice();
        final Map paramap = mActivity.getParaMap();
        TextView tv = (TextView) rootView.findViewById(R.id.deliveryMethods);
        String trim = tv.getText().toString().trim();
        String delivery_type = "0";
        paramap.put("delivery_id", "");
        if (trim.equals("自提")) {//自提
            delivery_type = "1";
            paramap.put("delivery_id", addressId);
        } else {//快递运送
            //确认收货地址
            if (order_addr_id.length() == 0) {
                if (!needCardAddress) {
                    new AlertDialog.Builder(mActivity).setMessage("请添加收货地址")
                            .setPositiveButton("立即去添加", (dialog, which) -> {
                                Bundle bundle1 = new Bundle();
                                bundle1.putInt("type", 1);
                                mActivity.go_address_list(bundle1, mActivity.getCurrentfragment());
                            })
                            .setNegativeButton("待会去", (dialog, which) -> {

                            }).create().show();
                } else
                    new AlertDialog.Builder(mActivity).setMessage("您当前购买的商品包含了保税仓发货或者是海外直邮的产品，请添加有身份证信息的收货地址")
                            .setPositiveButton("立即去验证", (dialog, which) -> {
                                Bundle bundle12 = new Bundle();
                                bundle12.putInt("type", 1);
                                mActivity.go_address_list(bundle12, mActivity.getCurrentfragment());
                            })
                            .setNegativeButton("待会去", (dialog, which) -> {
                            }).create().show();
                return;
            } else {
                if (needCardAddress && !hasCard) {
                    new AlertDialog.Builder(mActivity).setMessage("您当前购买的商品包含了保税仓发货或者是海外直邮的产品，需要身份证信息")
                            .setPositiveButton("立即去完善收货信息", (dialog, which) -> {
                                Bundle bundle13 = new Bundle();
                                bundle13.putInt("type", 1);
                                mActivity.go_address_list(bundle13, mActivity.getCurrentfragment());
                            })
                            .setNegativeButton("待会去", (dialog, which) -> {
                            }).create().show();
                    return;
                }
            }
        }
        View view = rootView.findViewById(R.id.ll_promotion_code);
        if (view.getVisibility()==View.VISIBLE){
            EditText et_promotion_code = (EditText) rootView.findViewById(R.id.et_promotion_code);
            Editable text = et_promotion_code.getText();
            String promotion_code = text.toString().trim();
            if (!TextUtils.isEmpty(promotion_code)){
                paramap.put("promotion_code", promotion_code);
            }else {
                Toast.makeText(mActivity,"请填入导师推广码",Toast.LENGTH_SHORT).show();
                return;
            }
        }

        paramap.put("cart_ids", cart_ids_temp);
        paramap.put("store_id", store_id_temp);
        paramap.put("cart_ids", cart_ids_temp);
        paramap.put("addr_id", order_addr_id);
        paramap.put("order_type", "android");
        paramap.put("delivery_type", delivery_type);
        paramap.put("coupon_id", coupons_id);

        if (transStore_idValueMap.keySet().size() == 0 && !trim.equals("自提")) {
            Toast.makeText(mActivity, "请填写付款方式及配送信息", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (payName.equals("货到付款")) {
                paramap.put("payType", "payafter");
            } else {
                paramap.put("payType", "");
            }
            for (String str : transStore_idValueMap.keySet()) {
                paramap.put("trans_" + str, transStore_idValueMap.get(str));
                paramap.put("ship_price_" + str, transValueMap.get(transStore_idValueMap.get(str)));
                if (transStore_idValueMap.get(str).contains("[")) {
                    paramap.put("transport_" + str, transStore_idValueMap.get(str).substring(0, transStore_idValueMap.get(str).indexOf("[")));
                } else {
                    paramap.put("transport_" + str, transStore_idValueMap.get(str));
                }
            }
        }
//        if (invoiceType.equals("普通发票")) {
//            paramap.put("invoiceType", 0);
//        }
//        if (invoiceType.equals("专用发票")) {
//            paramap.put("invoiceType", 1);
//        }
//        paramap.put("invoice", invoiceTitle);

        //验证提交商品是否失效
        inventoryCommitOrderIsSuccess(paramap);


    }

    //验证提交商品是否失效，然后提交订单并跳到支付界面
    private void inventoryCommitOrderIsSuccess(final Map paramap) {
        Map map = mActivity.getParaMap();
        map.put("app_cart_ids", cart_ids_temp);
        map.put("address_id", order_addr_id);
        mActivity.showProcessDialog();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/goods_cart2_inventory.htm",
                result -> {
                    try {
                        String code = result.get("code") + "";
                        if (code.equals("100")) {
                            //提交订单并跳到支付界面
                            commitOrderAndGoPay(paramap);
                        } else {
                            JSONArray jsonArray = result.getJSONArray("error_data");
                            final List dataList = new ArrayList();
                            for (int i = 0; i < jsonArray.length(); i++) {
                                Map map1 = new HashMap();
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                map1.put("goods_name", jsonObject.get("goods_name") + "");
                                map1.put("goods_main_photo", jsonObject.get("goods_main_photo") + "");
                                map1.put("cart_id", jsonObject.get("cart_id") + "");
                                dataList.add(map1);
                            }
                            myDialog = MyDialog.showAlert(mActivity, "以下商品库存不足", dataList, "更改收货地址", new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myDialog.dismiss();
                                    Bundle bundle1 = new Bundle();
                                    bundle1.putInt("type", 1);
                                    mActivity.go_address_list(bundle1, mActivity.getCurrentfragment());
                                }
                            }, "返回购物车", new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    myDialog.dismiss();
                                    String ids = "";
                                    for (int i = 0; i < dataList.size(); i++) {
                                        Map map1 = (Map) dataList.get(i);
                                        ids += map1.get("cart_id") + ",";
                                    }
                                    if (mActivity.getCache("inventory_ids").equals("")) {
                                        mActivity.setCache("inventory_ids", ids.substring(0, ids.length() - 1));
                                    } else {
                                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                        mActivity.setCache("inventory_ids", preferences.getString("inventory_ids", "") + "," + ids.substring(0, ids.length() - 1));
                                    }
                                    mActivity.go_cart1();
                                }
                            }, 0);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), map);
        mRequestQueue.add(request);
    }

    //提交订单并跳到支付界面
    public void commitOrderAndGoPay(Map paramap) {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/goods_cart3.htm",
                result -> {
                    try {
                        if (result.getBoolean("verify")) {
                            String payType = result.getString("payType");
                            String order_id = result.getString("order_id");
                            String order_num = result.getString("order_num");
                            Bundle pay_bundle = new Bundle();
                            pay_bundle.putString("totalPrice", priceSubmit);
                            pay_bundle.putString("order_id", order_id);
                            pay_bundle.putString("order_num", order_num);
                            pay_bundle.putString("type", "goods");

                            if (payType.equals("payafter") && payName.equals("货到付款"))
                                mActivity.go_pay(pay_bundle, "payafter");
                            else {
                                mActivity.go_pay(pay_bundle, "payonline");
                            }
                            mActivity.finish();
                        } else {
                            Toast.makeText(mActivity, "订单提交失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paramap);
        mRequestQueue.add(request);
    }


    //获取默认地址
    private void getDefaultAddress(boolean isSelfPickup) {
        if (!isSelfPickup) {
            Map paramap = mActivity.getParaMap();
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/address_default.htm",
                    result -> {
                        try {
                            if (!result.has("trueName")) {
                                //默认地址不存在
                                hasDefaut_address = false;
                                rootView.findViewById(R.id.norecieverInfo).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.recieverInfo).setVisibility(View.GONE);
                                transStore_idValueMap.clear();
                                payName = "";
                                TextView tv = (TextView) rootView.findViewById(R.id.paymentMethods);
                                tv.setText("请选择支付方式");
                                tv = (TextView) rootView.findViewById(R.id.deliveryMethods);
                                tv.setText("请选择配送方式");
                                calculatePrice();
                            } else {
                                rootView.findViewById(R.id.receiver).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.card).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.mobilePhone).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.card).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.address).setVisibility(View.VISIBLE);
                                //有真实姓名，就有默认地址
                                hasDefaut_address = true;
                                //判断有无身份证
                                hasCard = !result.get("card").toString().equals("");
                                //TODO 添加身份证显示
                                if (hasCard) {
                                    rootView.findViewById(R.id.card).setVisibility(View.VISIBLE);
                                } else {
                                    rootView.findViewById(R.id.card).setVisibility(View.INVISIBLE);
                                }
                                rootView.findViewById(R.id.norecieverInfo).setVisibility(View.GONE);
                                rootView.findViewById(R.id.recieverInfo).setVisibility(View.VISIBLE);
                                TextView tx = (TextView) rootView.findViewById(R.id.receiver);
                                tx.setText(result.getString("trueName"));
                                tx = (TextView) rootView.findViewById(R.id.mobilePhone);
                                String telephone = "没填手机号";
                                if (result.has("telephone") && !TextUtils.isEmpty(result.getString("telephone"))) {
                                    telephone = result.getString("telephone");
                                } else if (result.has("mobile") && !TextUtils.isEmpty(result.getString("mobile"))) {
                                    telephone = result.getString("mobile");
                                }
                                tx.setText(telephone);
                                tx = (TextView) rootView.findViewById(R.id.address);
                                tx.setText(result.getString("area") + result.getString("areaInfo"));
                                if (!order_addr_id.equals("" + result.getInt("addr_id"))) {
                                    order_addr_id = "" + result.getInt("addr_id");
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.i("test", error.toString()), paramap);
            mRequestQueue.add(request);
        }

    }


    //选择自提以后，修改地址为自提点地址
    private void getSelfPickupAddress() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        paraMap.put("addr_id", order_addr_id);
        paraMap.put("beginCount", 0);
        paraMap.put("selectCount", 100);
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart2_delivery.htm",
                result -> {
                    try {
                        rootView.findViewById(R.id.receiver).setVisibility(View.GONE);
                        rootView.findViewById(R.id.card).setVisibility(View.GONE);
                        rootView.findViewById(R.id.mobilePhone).setVisibility(View.GONE);
                        rootView.findViewById(R.id.card).setVisibility(View.GONE);
                        rootView.findViewById(R.id.address).setVisibility(View.GONE);
                        if (!TextUtils.isEmpty(result.toString()) && result.has("data") && !TextUtils.isEmpty(result.getString("data"))) {
                            JSONArray arr = result.getJSONArray("data");
                            int length = arr.length();
                            if (length > 0) {
                                for (int i = 0; i < length; i++) {
                                    JSONObject jsonObject = arr.getJSONObject(i);
                                    String id = jsonObject.getString("id");
                                    if (id.equals(addressId)) {
                                        rootView.findViewById(R.id.address).setVisibility(View.VISIBLE);
                                        TextView address = (TextView) rootView.findViewById(R.id.address);
                                        if (jsonObject.has("name") && !TextUtils.isEmpty(jsonObject.getString("name")) && jsonObject.has("addr") && !TextUtils.isEmpty(jsonObject.getString("addr"))) {
                                            address.setText("您选择在   " + jsonObject.getString("name") + "    自提购买的商品  \n" + "详细地址为：   " + jsonObject.getString("addr"));
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test", error.toString()), paraMap);
        mRequestQueue.add(request);
    }

    //计算订单金额
    private void calculatePrice() {
        double freight = 0;
        for (String str : transStore_idValueMap.keySet()) {
            freight += Double.parseDouble(transValueMap.get(transStore_idValueMap.get(str)));
        }
        TextView tv = (TextView) rootView.findViewById(R.id.freight);
        tv.setText("+￥" + mActivity.moneytodouble(freight + ""));
        tv = (TextView) rootView.findViewById(R.id.coupons_amount);
        tv.setText("-￥" + mActivity.moneytodouble(coupon_amount));
        double coupon = Double.parseDouble(coupon_amount);
        double allprice = Double.parseDouble(order_goods_price_temp) + freight - coupon;
        TextView goods_price = (TextView) rootView.findViewById(R.id.order_price);
        goods_price.setText("¥" + mActivity.moneytodouble(allprice + ""));
        priceSubmit = "" + allprice;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            this.onPause();
        } else {
            this.onResume();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isSelfPickup) {
            getDefaultAddress(isSelfPickup);
            getPaytypeAndTrans(cart_ids_temp, store_id_temp);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == TRANS_AND_PAY) {
            Bundle b = data.getExtras();
            delivery_time = b.getString("delivery_time");
            SerializableMap serializableMap = (SerializableMap) b.getSerializable("transValueMap");
            transValueMap = serializableMap.getMap();
            serializableMap = (SerializableMap) b.getSerializable("transStore_idValueMap");
            transStore_idValueMap = serializableMap.getMap();
            payName = b.getString("paytype");
            String trans_pay = b.getString("trans_pay");
            TextView tv = (TextView) rootView.findViewById(R.id.paymentMethods);
            tv.setText(payName);
            tv = (TextView) rootView.findViewById(R.id.deliveryMethods);
            if (TextUtils.isEmpty(trans_pay)) {
                tv.setText("自提");
                addressId = b.getString("addressId");
                isSelfPickup = true;
                getSelfPickupAddress();
            } else {
                tv.setText(trans_pay);
                isSelfPickup = false;
            }
            calculatePrice();
        } else if (requestCode == INVOICE) {
//            Bundle b = data.getExtras();
//            invoiceType = b.getString("invoiceType");
//            invoiceTitle = b.getString("invoiceTitle");
//            TextView tv = (TextView) rootView.findViewById(R.id.invoiceType);
//            tv.setText(invoiceType);
//            tv = (TextView) rootView.findViewById(R.id.invoiceTitle);
//            tv.setText(invoiceTitle);
        } else if (requestCode == COUPONS) {
            Bundle b = data.getExtras();
            coupons_id = b.getString("coupons_id");
            coupon_amount = b.getString("coupon_amount");
            calculatePrice();
        } else if (requestCode == ADDRESS) {
            isSelfPickup = false;
            getDefaultAddress(isSelfPickup);
        }
    }

}
