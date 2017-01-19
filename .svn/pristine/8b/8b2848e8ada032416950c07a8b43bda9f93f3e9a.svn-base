package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.PayActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Map;

public class GroupLifeCartFragment extends Fragment implements OnClickListener {
    PayActivity mActivity;
    View rootView;
    Bundle bundle;
    String mobile;
    private ImageButton pikerMinus;
    private ImageButton pickerPlus;
    private EditText et;
    private int goods_count;
    private TextView all_price;
    private double price;
    private CheckBox checkBox_pay_balance;
    private CheckBox checkBox_alipay;
    private CheckBox checkBox_weixinpay;
    private CheckBox checkBox_unionpay;
    private CheckBox temp;
    private String pay_method;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_group_cart1, container, false);
        mActivity = (PayActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("提交订单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置导航点击事件
            @Override
            public void onClick(View v) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                }
            }
        });
        bundle = getArguments();
        TextView tv = (TextView) rootView.findViewById(R.id.goods_name);
        tv.setText(bundle.getString("gg_name"));
        tv = (TextView) rootView.findViewById(R.id.price);
        tv.setText(mActivity.moneytodouble(bundle.getString("gg_price")));
        price = Double.parseDouble(bundle.getString("gg_price"));
        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.img);
        BaseActivity.displayImage(bundle.getString("gg_img"), img);
        mobile = bundle.getString("mobile");
        goods_count = 1;
        pikerMinus = (ImageButton) rootView.findViewById(R.id.pikerMinus);
        pickerPlus = (ImageButton) rootView.findViewById(R.id.pickerPlus);
        et = (EditText) rootView.findViewById(R.id.editCount);
        all_price = (TextView) rootView.findViewById(R.id.group_sum);
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.toString().length() > 0) {
                    goods_count = Integer.parseInt(charSequence.toString());
                } else {
                    goods_count = 0;
                }
                if (goods_count < 1) {
                    goods_count = 1;
                    et.setText(goods_count + "");
                }
                if (goods_count == 1) {
                } else {
                    et.setSelection(et.getText().toString().length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (goods_count == 1) {
                    pikerMinus.setClickable(false);
                    //pikerMinus.setImageResource(R.drawable.minusgray);
                } else {
                    pikerMinus.setClickable(true);
                    //pikerMinus.setImageResource(R.drawable.minus);
                }
                all_price.setText("总计：￥" + (mActivity.moneytodouble(new BigDecimal(price * goods_count).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() + "")));

            }
        });
        pikerMinus.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (goods_count > 1) {
                    goods_count--;
                    et.setText(goods_count + "");
                }

            }
        });
        pickerPlus.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                goods_count++;
                et.setText(goods_count + "");
            }
        });
        et.setText("" + goods_count);

        checkBox_pay_balance = (CheckBox) rootView.findViewById(R.id.checkBox_pay_balance);
        checkBox_alipay = (CheckBox) rootView.findViewById(R.id.checkBox_alipay);
        checkBox_weixinpay = (CheckBox) rootView.findViewById(R.id.checkBox_weixinpay);
        checkBox_unionpay = (CheckBox) rootView.findViewById(R.id.checkBox_pay_union);

        tv = (TextView) rootView.findViewById(R.id.phone);
        tv.setText(mobile);
        rootView.findViewById(R.id.change_bound_phone).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_bound_phone();
            }
        });

        // 设置支付方式

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/query_all_payment_online.htm",
                result -> {
                    try {
                        JSONArray payment_list = result.getJSONArray("datas");
                        int payment_length = payment_list.length();
                        for (int i = 0; i < payment_length; i++) {
                            JSONObject obj = payment_list.getJSONObject(i);
                            if (obj.getString("pay_mark").equals("alipay_app")) {
                                rootView.findViewById(R.id.alipay).setVisibility(View.VISIBLE);
                            } else if (obj.getString("pay_mark").equals("balance")) {
                                rootView.findViewById(R.id.pay_balance).setVisibility(View.VISIBLE);
                            } else if (obj.getString("pay_mark").equals("wx_app")) {
                                rootView.findViewById(R.id.weixinpay).setVisibility(View.VISIBLE);
                            } else if (obj.getString("pay_mark").equals("unionpay")) {
                                rootView.findViewById(R.id.pay_union).setVisibility(View.VISIBLE);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), mActivity.getParaMap());
        mRequestQueue.add(request);

        rootView.findViewById(R.id.alipay).setOnClickListener(this);
        rootView.findViewById(R.id.weixinpay).setOnClickListener(this);
        rootView.findViewById(R.id.pay_balance).setOnClickListener(this);
        rootView.findViewById(R.id.pay_union).setOnClickListener(this);
        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        if (pay_method != null && !pay_method.equals("")) {
                            order_submit();
                        } else {
                            Toast.makeText(mActivity, "请选择支付方式", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            TextView tv = (TextView) rootView.findViewById(R.id.phone);
            tv.setText(mobile);
        }
        super.onHiddenChanged(hidden);
    }

    protected void order_submit() {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("group_id", bundle.get("id"));
        paramap.put("order_count", goods_count);
        paramap.put("pay_method", pay_method);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/group_life_order_save.htm",
                result -> {
                    try {

                        String code = result.get("code").toString();
                        if (code.equals("-100")) {
                            Toast.makeText(mActivity, "商品参数错误", Toast.LENGTH_SHORT).show();
                        } else {
                            String order_sn = result.get("order_sn").toString();
                            String order_id = result.get("order_id").toString();
                            if (code.equals("100")) {
                                bundle = new Bundle();
                                bundle.putString("totalPrice", result.get("total_price").toString());
                                bundle.putString("order_id", order_id);
                                bundle.putString("order_num", order_sn);
                                bundle.putString("type", "life");
                                if (pay_method.equals("alipay_app")) {
                                    mActivity.go_alipay(bundle);
                                }
                                if (pay_method.equals("balance")) {
                                    mActivity.go_balance(bundle);
                                }
                                if (pay_method.equals("wx_app")) {
                                    mActivity.go_weixinpay(bundle);
                                }
                                if (pay_method.equals("unionpay")) {
                                    mActivity.go_uppay(bundle);
                                }

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

    @Override
    public void onClick(View v) {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            if (temp != null) {
                temp.setChecked(false);
            }
            switch (v.getId()) {
                case R.id.alipay:
                    checkBox_alipay.setChecked(true);
                    temp = checkBox_alipay;
                    pay_method = "alipay_app";
                    break;
                case R.id.weixinpay:
                    checkBox_weixinpay.setChecked(true);
                    temp = checkBox_weixinpay;
                    pay_method = "wx_app";
                    break;
                case R.id.pay_balance:
                    checkBox_pay_balance.setChecked(true);
                    temp = checkBox_pay_balance;
                    pay_method = "balance";
                    break;
                case R.id.pay_union:
                    checkBox_unionpay.setChecked(true);
                    temp = checkBox_unionpay;
                    pay_method = "unionpay";
                    break;

                default:
                    break;
            }
        }
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
        mActivity.go_index();
        mActivity.go_usercenter();
        mActivity.go_order_group_list();
        mActivity.go_order_life(bundle.getString("order_id"));
    }
}
