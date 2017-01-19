package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.PayActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * 购物车第三步,在线支付部分
 */
public class PayOnlineFragment extends Fragment implements OnClickListener {
    private PayActivity mActivity;
    private View rootView;
    private Bundle bundle;
    private String payType;
    private CheckBox checkBox_alipay;
    private CheckBox checkBox_weixinpay;
    private CheckBox checkBox_pay_balance;
    private CheckBox checkBox_uppay;
    private CheckBox temp;
    private TextView tv;
    public boolean isPay_balance = false;
    private String payType1;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.bundle = null;
        this.payType = null;
        this.checkBox_alipay = null;
        this.checkBox_weixinpay = null;
        this.checkBox_pay_balance = null;
        this.checkBox_uppay = null;
        this.temp = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (PayActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_pay_online, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("在线支付");
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        bundle = getArguments();
        payType1 = bundle.getString("payType");
        initView();
        initPayMethod();



        return rootView;
    }

    //初始化UI界面
    public void initView() {
        tv = (TextView) rootView.findViewById(R.id.orderNumber);
        tv.setText(bundle.getString("order_num"));
        tv = (TextView) rootView.findViewById(R.id.orderAmount);
        if (bundle.getString("order_money")!=null&&!TextUtils.isEmpty(mActivity.moneytodouble(bundle.getString("order_money")).trim())){
            tv.setText("￥" + mActivity.moneytodouble(bundle.getString("order_money")));
        }else {
            tv.setText("￥" + mActivity.moneytodouble(bundle.getString("totalPrice")));
        }

        checkBox_alipay = (CheckBox) rootView.findViewById(R.id.checkBox_alipay);
        checkBox_weixinpay = (CheckBox) rootView.findViewById(R.id.checkBox_weixinpay);
        checkBox_pay_balance = (CheckBox) rootView.findViewById(R.id.checkBox_pay_balance);
        checkBox_uppay = (CheckBox) rootView.findViewById(R.id.checkBox_uppay);
        rootView.findViewById(R.id.alipay).setOnClickListener(this);
        rootView.findViewById(R.id.weixinpay).setOnClickListener(this);
        rootView.findViewById(R.id.uppay).setOnClickListener(this);
        rootView.findViewById(R.id.pay_balance).setOnClickListener(v -> {
            if (isPay_balance == false) {
                isPay_balance = true;
                checkBox_pay_balance.setChecked(true);
                payType = "balance";
            } else if (isPay_balance = true) {
                isPay_balance = false;
                checkBox_pay_balance.setChecked(false);
                if (temp == checkBox_alipay) {
                    payType = "alipay_app";
                } else if (temp == checkBox_weixinpay) {
                    payType = "wx_app";
                } else if (temp == checkBox_uppay) {
                    payType = "uppay";
                } else {
                    payType = "";
                }

            }

        });

        rootView.findViewById(R.id.order_submit).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (payType != null && !TextUtils.isEmpty(payType.trim())) {
                    order_pay();
                } else {
                    Toast.makeText(mActivity, "请选择支付方式", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //请求支付方式数据，并更新UI界面
    public void initPayMethod() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/query_all_payment_online.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    JSONArray payment_list = jsonObject.getJSONArray("datas");
                    int payment_length = payment_list.length();

                    if (payType1 == null) {
                        payType1 = "";
                        for (int i = 0; i < payment_length; i++) {
                            JSONObject obj = payment_list.getJSONObject(i);
                            if (obj.getString("pay_mark").equals("alipay_app")) {
                                rootView.findViewById(R.id.alipay).setVisibility(View.VISIBLE);
                            } else if (obj.getString("pay_mark").equals("balance")) {
                                if ("predeposit".equals(bundle.getString("type"))) {
                                    rootView.findViewById(R.id.pay_balance).setVisibility(View.GONE);
                                } else {
                                    rootView.findViewById(R.id.pay_balance).setVisibility(View.VISIBLE);
                                }
                            } else if (obj.getString("pay_mark").equals("wx_app")) {
                                rootView.findViewById(R.id.weixinpay).setVisibility(View.VISIBLE);
                            } else if (obj.getString("pay_mark").equals("unionpay")) {
                                rootView.findViewById(R.id.uppay).setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        Log.i("test","payType1=="+payType1);
                        if (payType1.equals("alipay_app")) {
                            rootView.findViewById(R.id.pay_balance).setVisibility(View.GONE);
                            rootView.findViewById(R.id.weixinpay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.uppay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.alipay).setVisibility(View.VISIBLE);
                            payType = "alipay_app";
                            checkBox_alipay.setChecked(true);
                        } else if (payType1.equals("wx_app")) {
                            rootView.findViewById(R.id.pay_balance).setVisibility(View.GONE);
                            rootView.findViewById(R.id.uppay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.alipay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.weixinpay).setVisibility(View.VISIBLE);
                            payType = "wx_app";
                            checkBox_weixinpay.setChecked(true);
                        } else if (payType1.equals("uppay")) {
                            rootView.findViewById(R.id.pay_balance).setVisibility(View.GONE);
                            rootView.findViewById(R.id.weixinpay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.alipay).setVisibility(View.GONE);
                            rootView.findViewById(R.id.uppay).setVisibility(View.VISIBLE);
                            payType = "uppay";
                            checkBox_uppay.setChecked(true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //提交充值余额订单请求
    protected void order_pay() {
        Log.i("test", "isPay_balance==" + isPay_balance + "  payType==" + payType);
        String type=bundle.getString("type","goods");
        bundle.putString("type",type);
        Log.i("test", "type==" + type );
        if (payType.equals("balance") || payType.equals("预存款支付")) {
            if (temp == checkBox_alipay) {
                bundle.putString("pay_type_radio", "ali_pay");
            } else if (temp == checkBox_weixinpay) {
                bundle.putString("pay_type_radio", "wx_pay");
            } else if (temp == checkBox_uppay) {
                bundle.putString("pay_type_radio", "uppay");
            }
            mActivity.go_balance(bundle);
        }
        if (payType.equals("alipay_app") || payType.equals("支付宝支付")) {
            mActivity.go_alipay(bundle);
        }
        if (payType.equals("wx_app") || payType.equals("微信支付")) {
            mActivity.go_weixinpay(bundle);
        }
        if (payType.equals("uppay") || payType.equals("银联支付")) {
            mActivity.go_uppay(bundle);
        }
    }

    //选择支付方式点击监听
    @Override
    public void onClick(View v) {
        if (temp != null) {
            temp.setChecked(false);
        }

        switch (v.getId()) {
            case R.id.alipay:
                if (temp == checkBox_alipay) {
                    checkBox_alipay.setChecked(false);
                    temp = null;
                } else {
                    checkBox_alipay.setChecked(true);
                    temp = checkBox_alipay;
                    if (isPay_balance == false) {
                        payType = "alipay_app";
                    } else if (isPay_balance == true) {
                        payType = "balance";
                    }
                }
                break;
            case R.id.weixinpay:

                if (temp == checkBox_weixinpay) {
                    checkBox_weixinpay.setChecked(false);
                    temp = null;
                } else {
                    checkBox_weixinpay.setChecked(true);
                    temp = checkBox_weixinpay;
                    if (isPay_balance == false) {
                        payType = "wx_app";
                    } else if (isPay_balance == true) {
                        payType = "balance";
                    }
                }

                break;
            case R.id.uppay:
                if (temp == checkBox_uppay) {
                    checkBox_uppay.setChecked(false);
                    temp = null;
                } else {
                    checkBox_uppay.setChecked(true);
                    temp = checkBox_uppay;
                    if (isPay_balance == false) {
                        payType = "uppay";
                    } else if (isPay_balance == true) {
                        payType = "balance";
                    }
                }

                break;
            default:
                break;
        }
    }

    //菜单图文混合
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //菜单图文混合
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    //菜单选点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}