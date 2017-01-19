package com.ewgvip.buyer.android.activity;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.fragment.GroupLifeCartFragment;
import com.ewgvip.buyer.android.fragment.PayAfterFragment;
import com.ewgvip.buyer.android.fragment.PayOnlineFragment;
import com.ewgvip.buyer.android.models.PayResult;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.unionpay.UPPayAssistEx;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理支付,支付方式分为在线支付和预存款支付
 */
public class PayActivity extends BaseActivity {

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private static final int SDK_UPPAY_FLAG = 3;

    // 微信支付
    private PayReq req;

    private String pay_type = "";
    private Bundle bundle;

    /**
     * 处理支付宝和银联支付的回调
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);
                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000"则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        showProcessDialog();
                        // 执行订单同步回调
                        final Bundle bundle = msg.getData();
                        String order_id = bundle.getString("order_id");
                        String order_num = bundle.getString("order_num");
                        String isLevelUpVip = bundle.getString("isLevelUpVip");
                        String isRechargeCash = bundle.getString("isRechargeCash");
                        Log.i("test","isLevelUpVip="+isLevelUpVip);
                        Log.i("test", "isLevelUpVip=" + isLevelUpVip);
                        Map params = getParaMap();
                        params.put("order_no", order_id);
                        params.put("out_trade_no", order_num);
                        String[] arr = resultInfo.split("&");
                        String sign = arr[arr.length - 1];
                        sign = sign.replace("sign=", "");
                        sign = sign.replace("\"", "");
                        params.put("sign", sign);
                        params.put("order_type", bundle.getString("type", "goods"));
                        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
                        Request<JSONObject> request = new NormalPostRequest(getApplicationContext(), getAddress() + "/app/alipay_return.htm",
                                jsonObject -> {
                                    String msg1 = "支付成功";
                                    if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                                        msg1 = "升级成功";
                                        go_usercenterfromLevelUpVip();
                                    } else if (bundle.getString("isRechargeCash") != null && "true".equals(bundle.getString("isRechargeCash"))) {
                                        msg1 = "充值成功";
                                        go_balance_detail();
                                    } else {
                                        go_success(bundle);
                                    }
                                    PayActivity.this.bundle = null;
                                    Toast.makeText(PayActivity.this, msg1, Toast.LENGTH_SHORT).show();
                                }, error -> {
                                    Log.i("test",error.toString());
                                    hideProcessDialog(1);
                                }, params);
                        mRequestQueue.add(request);

                    } else {
                        hideProcessDialog(0);
                        // 判断resultStatus 为非“9000"则代表可能支付失败
                        // “8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            go_success(bundle);
                            PayActivity.this.bundle = null;
                            Toast.makeText(PayActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败,请重试", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj, Toast.LENGTH_SHORT).show();
                    break;
                }
                case SDK_UPPAY_FLAG: {
                    UPPayAssistEx.startPayByJAR(PayActivity.this, com.unionpay.uppay.PayActivity.class, "", "", msg.obj + "", Constants.UPPAY_NORMAL);
                    PayActivity.this.bundle = msg.getData();
                }
                default:
                    break;
            }
        }

    };



    //初始化订单支付页面
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Intent intent = getIntent();
        String paytype = intent.getStringExtra("paytype");
        Bundle bundle = intent.getExtras();
        showProcessDialog();
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        android.app.Fragment fragment = null;
        if (paytype.equals("payonline")) {
            fragment = new PayOnlineFragment();
        } else if (paytype.equals("payafter")) {
            fragment = new PayAfterFragment();
        } else if (paytype.equals("group")) {
            fragment = new GroupLifeCartFragment();
        }
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.index_top, fragment);
        fragmentTransaction.commit();
    }

    //支付宝支付
    public void go_alipay(final Bundle bundle) {
        this.bundle = bundle;
        final String order_id = bundle.getString("order_id");
        Map paramap = getParaMap();
        paramap.put("order_id", order_id);
        paramap.put("order_type", bundle.getString("type", "goods"));
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(
                getApplicationContext(), getAddress() + "/app/buyer/alipay_sign.htm",
                jsonObject -> {
                    try {
                        if (!TextUtils.isEmpty(jsonObject.toString()) && jsonObject.toString().contains("code") && jsonObject.getInt("code") == 100) {
                            if (jsonObject.toString().contains("orderInfo") && !TextUtils.isEmpty(jsonObject.getString("orderInfo"))) {
                                final String orderInfo = jsonObject.getString("orderInfo");
                                Runnable payRunnable = () -> {
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(PayActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(orderInfo, true);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    msg.setData(bundle);
                                    mHandler.sendMessage(msg);
                                };
                                // 必须异步调用
                                Thread payThread = new Thread(payRunnable);
                                payThread.start();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test",error.toString());
                    hideProcessDialog(1);
                }, paramap);
        mRequestQueue.add(request);

    }

    //微信支付  不要更改请求方式
    public void go_weixinpay(final Bundle bundle) {
        pay_type = "wx_app";
        this.bundle = bundle;
        showProcessDialog();
        Map params = new HashMap();
        params.put("id", bundle.getString("order_id"));
        params.put("type", bundle.getString("type", "goods"));
        if ("life".equals(bundle.getString("type"))) {
            params.put("type", "group");
        }
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(
                getApplicationContext(), getAddress() + "/app/pay/wx_pay.htm",
                result -> {
                    try {
                        Log.i("test", result.toString());
                        if (result.getString("msg").equals("success")) {
                            if (mwxapi == null) {
                                mwxapi = WXAPIFactory.createWXAPI(PayActivity.this, Constants.WECHAT_API_KEY);
                            }
                            mwxapi.registerApp(result.getString("appId"));
                            req = new PayReq();
                            req.appId = result.getString("appId");
                            req.partnerId = result.getString("partnerId");
                            req.prepayId = result.getString("prepayId");
                            req.packageValue = result.getString("packageValue");
                            req.nonceStr = result.getString("nonceStr");
                            req.timeStamp = result.getString("timeStamp");
                            req.sign = result.getString("sign");
                            mwxapi.sendReq(req);
                        } else {
                            Toast.makeText(PayActivity.this, "发起支付失败，请重试", Toast.LENGTH_SHORT).show();
                            hideProcessDialog(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test",error.toString());
                    hideProcessDialog(1);
                }, params);
        mRequestQueue.add(request);
    }

    //处理微信支付的回调
    @Override
    protected void onResume() {
        super.onResume();
        hideProcessDialog(0);
        if (pay_type.equals("wx_app")) {
            String payresult = getCache("payresult");
            if (payresult.equals("success")) {
                String msg = "支付成功";
                setCache("payresult", "");
                if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                    msg = "升级成功";
                    go_usercenterfromLevelUpVip();
                } else if (bundle.getString("isRechargeCash") != null && "true".equals(bundle.getString("isRechargeCash"))) {
                    msg = "充值成功";
                    go_balance_detail();
                } else {
                    go_success(bundle);
                }
                PayActivity.this.bundle = null;
                Toast.makeText(PayActivity.this, msg, Toast.LENGTH_SHORT).show();
            } else if (payresult.equals("fail")) {
                Toast.makeText(PayActivity.this, "支付失败,请重试", Toast.LENGTH_SHORT).show();
            } else if (payresult.equals("cancle")) {
                Toast.makeText(PayActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //银联支付
    public void go_uppay(final Bundle bundle) {
        this.bundle = bundle;
        Map params = getParaMap();
        String order_id = bundle.getString("order_id");
        params.put("order_id", order_id);
        params.put("order_type", bundle.getString("type", "goods"));
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        Request<JSONObject> request = new NormalPostRequest(
                getApplicationContext(), getAddress() + "/app/buyer/union_pay.htm",
                jsonObject -> {
                    try {
                        if (jsonObject.getInt("code") == 100) {
                            Message msg = new Message();
                            msg.what = SDK_UPPAY_FLAG;
                            msg.obj = jsonObject.getString("tn");
                            msg.setData(bundle);
                            mHandler.sendMessage(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test",error.toString());
                    hideProcessDialog(1);
                }, params);
        mRequestQueue.add(request);
    }

    //银联支付回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        String msg = "";
        String str = data.getExtras().getString("pay_result");
        if (str.equalsIgnoreCase("success")) {
            msg = "支付成功!";
            if (bundle.getString("isLevelUpVip") != null && "true".equals(bundle.getString("isLevelUpVip"))) {
                go_usercenterfromLevelUpVip();
                msg = "升级成功！";
            } else if (bundle.getString("isRechargeCash") != null && "true".equals(bundle.getString("isRechargeCash"))) {
                go_balance_detail();
                msg = "充值成功！";
            } else {
                go_success(bundle);
            }
            PayActivity.this.bundle = null;
        } else if (str.equalsIgnoreCase("fail")) {
            msg = "支付失败!";
        } else if (str.equalsIgnoreCase("cancel")) {
            msg = "支付取消!";
        }
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
