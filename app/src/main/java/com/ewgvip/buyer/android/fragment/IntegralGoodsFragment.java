package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog.Builder;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.layout.MyWebViewClient;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/*
    商品详情页面
 */
public class IntegralGoodsFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private String goods_id;
    private ImageButton pikerMinus;
    private ImageButton pickerPlus;
    private EditText et;
    private int goods_count = 1;
    private int max_count;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.goods_id = null;
        this.pikerMinus = null;
        this.pickerPlus = null;
        this.et = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_integral_goods, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("商品详情");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        mActivity.showProcessDialog();
        Bundle bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        Map paraMap = new HashMap();
        paraMap.put("ig_id", goods_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_goods.htm",
                result -> {
                    try {
                        rootView.findViewById(R.id.layout_goods_imgs).setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, mActivity.getScreenWidth()));
                        SimpleDraweeView img = (SimpleDraweeView) rootView.findViewById(R.id.ig_img);
                        BaseActivity.displayImage(result.getString("ig_goods_img"), img);
                        TextView textview = (TextView) rootView.findViewById(R.id.goods_name);
                        textview.setText(result.getString("ig_goods_name"));
                        textview = (TextView) rootView.findViewById(R.id.current_Price);
                        textview.setText(result.getString("ig_goods_integral"));
                        textview = (TextView) rootView.findViewById(R.id.needpaymoney);
                        if (result.has("ig_goods_pay_price")&&!TextUtils.isEmpty(result.getString("ig_goods_pay_price"))){
                            textview.setText("+"+result.getString("ig_goods_pay_price")+"元");
                        }
                        textview = (TextView) rootView.findViewById(R.id.goods_Price);
                        textview.setText("￥" + result.getString("ig_goods_price"));
                        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线

                        textview = (TextView) rootView.findViewById(R.id.ig_time);
                        textview.setText(result.getString("ig_time"));
                        textview = (TextView) rootView.findViewById(R.id.ig_limit_type);
                        textview.setText(result.getString("ig_limit_count"));


                        textview = (TextView) rootView.findViewById(R.id.freight);
                        textview.setText(result.getString("transfee"));
                        textview = (TextView) rootView.findViewById(R.id.inventory);
                        textview.setText(result.getString("ig_goods_count"));

                        max_count = result.getInt("ig_goods_count");
                        if (result.has("ig_limit_count_choose")) {
                            max_count = result.getInt("ig_limit_count_choose");
                        }

                        pikerMinus = (ImageButton) rootView.findViewById(R.id.pikerMinus);
                        pickerPlus = (ImageButton) rootView.findViewById(R.id.pickerPlus);
                        et = (EditText) rootView.findViewById(R.id.editCount);

                        et.addTextChangedListener(new TextWatcher() {
                            @Override
                            public void onTextChanged(CharSequence s, int start, int before, int count) {
                            }
                            @Override
                            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                            }
                            @Override
                            public void afterTextChanged(Editable s) {
                                if (s.toString().length() > 0)
                                    goods_count = Integer.parseInt(s.toString());
                                if (goods_count < 1) {
                                    goods_count = 1;
                                }
                                if (goods_count > max_count) {
                                    goods_count = max_count;

                                }
                                if (goods_count == 1) {
                                    pikerMinus.setClickable(false);
                                    pikerMinus.setImageResource(R.mipmap.minusgray);
                                } else {
                                    pikerMinus.setClickable(true);
                                    pikerMinus.setImageResource(R.mipmap.minus);
                                }
                                if (goods_count == max_count) {
                                    pickerPlus.setClickable(false);
                                    pickerPlus.setImageResource(R.mipmap.plusgray);
                                } else {
                                    pickerPlus.setClickable(true);
                                    pickerPlus.setImageResource(R.mipmap.plus);
                                }
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
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mActivity.hideProcessDialog(0);
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

        //立即兑换
        rootView.findViewById(R.id.add_to_car).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        addToCart();
                    }
                });

        rootView.findViewById(R.id.goto_car).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        if (mActivity.islogin()) {
                            mActivity.go_integral_cart();
                        } else {
                            mActivity.go_login();
                        }
                    }
                });

        WebView webview = (WebView) rootView.findViewById(R.id.webDetail);
        webview.setWebViewClient(new MyWebViewClient(mActivity));
        WebSettings webviewSettings = webview.getSettings();
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setUseWideViewPort(true);
        webviewSettings.setSupportZoom(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webview.loadUrl(mActivity.getAddress() + "/app/integral_introduce.htm?id=" + goods_id);

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

    protected void addToCart() {

        if (mActivity.islogin()) {
            mActivity.showProcessDialog();
            Map paramap = new HashMap();
            SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
            String user_id = preferences.getString("user_id", "");
            String token = preferences.getString("token", "");
            String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
            paramap.put("user_id", user_id);
            paramap.put("token", token);
            paramap.put("id", goods_id);
            paramap.put("exchange_count", et.getText().toString());

            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/exchange1.htm",
                    result -> {
                        try {
                            int exchange_status = result.getInt("exchange_status");
                            if (exchange_status == 0) {
                                Builder builder = new Builder(mActivity)
                                        .setTitle("添加成功！")
                                        .setMessage("商品已成功加入购物车")
                                        .setPositiveButton("去购物车", (dialog, which) -> {
                                            mActivity.go_integral_cart();
                                            dialog.dismiss();// 关闭对话框
                                        })
                                        .setNegativeButton("再逛逛", (dialog, which) -> {
                                            dialog.dismiss();// 关闭对话框
                                        });
                                builder.create().show();// 创建对话框并且显示该对话框
                            } else {
                                Toast.makeText(mActivity,result.getString("exchange_info"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        mActivity.hideProcessDialog(0);
                    }, error -> mActivity.hideProcessDialog(1), paramap);
            mRequestQueue.add(request);
        } else {
            mActivity.go_login();
        }
    }


}
