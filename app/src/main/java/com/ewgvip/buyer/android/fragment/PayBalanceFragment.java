package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预付款支付功能
 */

public class PayBalanceFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_order_num)
    TextView tvOrderNum;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.balance)
    TextView balance;
    @BindView(R.id.balance_pay)
    EditText balancePay;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.ll_balance_pay)
    LinearLayout llBalancePay;
    @BindView(R.id.submit)
    Button submit;
    private BaseActivity mActivity;
    private String order_id;
    private Bundle bundle;
    private String totalPrice;
    private String order_num;
    private String pay_type_radio;

    @Override
    public void onDetach() {
        super.onDetach();

        this.order_id = null;
        this.bundle = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        final View view = inflater.inflate(R.layout.fragment_pay_balance, container, false);
        ButterKnife.bind(this, view);

        //设置标题
        toolbar.setTitle("预存款支付");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        bundle = getArguments();
        pay_type_radio = bundle.getString("pay_type_radio", "");
        totalPrice = bundle.getString("totalPrice");
        order_id = bundle.getString("order_id");
        order_num = bundle.getString("order_num");
        //订单号
        tvOrderNum.setText(order_num);
        //订单总金额
        tvTotalPrice.setText(mActivity.moneytodouble(totalPrice));



        initView();


        return view;
    }

    //支付预存款金额
    public void initView() {
        mActivity.showProcessDialog();
        Map paraMap = mActivity.getParaMap();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, CommonUtil.getAddress(mActivity) + "/app/buyer/index.htm",
                result -> {
                    try {
                        if (result.get("ret").toString().equals("true")) {
                            mActivity.setCache("userbalance", result.get("balance") + "");
                            mActivity.setCache("integral", result.get("integral") + "");
                            //预存款可用余额

                            balance.setText("￥" + result.get("balance") + "");

                            if (result.has("balance") && result.getString("balance") != null) {
                                String moneytodouble = mActivity.moneytodouble(result.get("balance") + "");
                                double v = Double.parseDouble(moneytodouble);
                                double vtotalPrice = Double.parseDouble(mActivity.moneytodouble(totalPrice));
                                if (v >= 0 && v >= vtotalPrice) {
                                    balancePay.setText(mActivity.moneytodouble(totalPrice));
                                } else if (v > 0 && v < vtotalPrice) {
                                    balancePay.setText(moneytodouble);
                                } else if (v == 0) {
                                    llBalancePay.setVisibility(View.GONE);
                                    CommonUtil.showSafeToast(mActivity, "您的预存款余额为0，请充值余额或选取其他支付方式");}
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
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

    @OnClick(R.id.submit)
    public void onClick() {
        if (FastDoubleClickTools.isFastDoubleClick()) {

            if (View.VISIBLE == llBalancePay.getVisibility()) {
                if (password.getText().toString().trim().length() == 0) {
                    Toast.makeText(mActivity, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                String sbalance_pay = balancePay.getText().toString().trim();
                int length = sbalance_pay.length();
                if (length == 0) {
                    Toast.makeText(mActivity, "预付款支付金额不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Double.parseDouble(mActivity.moneytodouble(sbalance_pay)) > Double.parseDouble(mActivity.moneytodouble(totalPrice))) {
                    Toast.makeText(mActivity, "预付款支付金额不能大于订单总金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(pay_type_radio.trim()) &&
                        Double.parseDouble(mActivity.moneytodouble(sbalance_pay)) < Double.parseDouble(mActivity.moneytodouble(totalPrice))) {
                    CommonUtil.getHandler().postDelayed(() -> {
                        Toast.makeText(mActivity, "预付款不足，请选择支付宝或微信和预存款组合支付，或者预存预付款，重新支付", Toast.LENGTH_SHORT).show();
                        mActivity.onBackPressed();
                    }, 1000);
                    return;
                }
                if (Double.parseDouble(mActivity.moneytodouble(sbalance_pay)) > 0 &&
                        Double.parseDouble(mActivity.moneytodouble(sbalance_pay)) <= Double.parseDouble(mActivity.moneytodouble(totalPrice))) {

                    mActivity.showProcessDialog();
                    Map paramap = mActivity.getParaMap();
                    paramap.put("password", password.getText().toString());
                    paramap.put("order_id", order_id);
                    paramap.put("balance_pay", sbalance_pay);
                    if (!TextUtils.isEmpty(pay_type_radio.trim())) {
                        paramap.put("pay_type_radio", pay_type_radio);
                    }
                    paramap.put("type", bundle.getString("type", "goods"));
                    if (bundle.getString("type").equals("life")) {
                        paramap.put("type", "group");
                    }

                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(mActivity,
                            mActivity.getAddress() + "/app/buyer/pay_balance.htm",
                            result -> {
                                Log.i("test", "result==" + result.toString());
                                int code = 0;
                                try {
                                    code = result.getInt("code");
                                    if (code == 50) {
                                        String needpay = result.getString("needpay");
                                        String pay_type_radio1 = result.getString("pay_type_radio");
                                        Bundle bundle1 = new Bundle();
                                        if ("ali_pay".equals(pay_type_radio1)) {
                                            bundle1.putString("payType", "alipay_app");
                                        }
                                        if ("wx_pay".equals(pay_type_radio1)) {
                                            bundle1.putString("payType", "wx_app");
                                        }
                                        if ("uppay".equals(pay_type_radio1)) {
                                            bundle1.putString("payType", "uppay");
                                        }
                                        if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                                            bundle1.putString("isLevelUpVip", "true");
                                        }
                                        bundle1.putString("order_id", order_id);
                                        bundle1.putString("order_num", order_num);
                                        bundle1.putString("order_type", bundle.getString("type", "goods"));
                                        bundle1.putString("order_money", needpay);
                                        String type = bundle.getString("type", "goods");
                                        bundle1.putString("type", type);
                                        mActivity.go_pay(bundle1, "payonline");

                                    }
                                    //预存款支付完订单
                                    if (code == 100) {
                                        if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                                            mActivity.go_usercenterfromLevelUpVip();
                                        } else if (bundle.getString("isRechargeCash") != null && "true".equals(bundle.getString("isRechargeCash"))) {
                                            mActivity.go_balance_detail();
                                        } else {
                                            mActivity.go_success(bundle);
                                        }

                                    }
                                    if (code == -100) {
                                        Toast.makeText(mActivity, "用户信息错误", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == -200) {
                                        Toast.makeText(mActivity, "订单信息错误", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == -300) {
                                        Toast.makeText(mActivity, "密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == -400) {
                                        Toast.makeText(mActivity, "预存款余额不足", Toast.LENGTH_SHORT).show();
                                    }
                                    // 订单已经支付成功
                                    if (code == -500) {
                                        if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                                            mActivity.go_usercenterfromLevelUpVip();
                                        } else if (bundle.getString("isRechargeCash") != null && "true".equals(bundle.getString("isRechargeCash"))) {
                                            mActivity.go_balance_detail();
                                        } else {
                                            mActivity.go_success(bundle);
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                mActivity.hideProcessDialog(0);
                            }, error -> mActivity.hideProcessDialog(1), paramap);
                    mRequestQueue.add(request);
                }
            }
        }
    }
}
