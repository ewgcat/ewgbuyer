package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.layout.MyWebViewClient;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/20.
 */
public class FreeDetailFragment extends Fragment {
    private MainActivity mActivity;
    private View rootView;
    private String goods_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_freegoods_details, container,
                false);
        mActivity = (MainActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("试用商品详情");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用

        rootView.findViewById(R.id.layout_goods_imgs).setLayoutParams(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mActivity.getScreenWidth()));

        Bundle bundle = getArguments();
        goods_id = bundle.getString("id");

        Map paraMap = new HashMap();
        paraMap.put("id", goods_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_view.htm",
                result -> {
                    try {
                        SimpleDraweeView iv_goods_img = (SimpleDraweeView) rootView.findViewById(R.id.iv_goods_img);
                        BaseActivity.displayImage(result.getString("free_acc"), iv_goods_img);
                        TextView textview = (TextView) rootView.findViewById(R.id.goods_name);
                        textview.setText(result.getString("free_name"));
                        textview = (TextView) rootView.findViewById(R.id.current_Price);
                        textview.setText("￥" + result.getString("free_price"));
                        textview.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
                        textview = (TextView) rootView.findViewById(R.id.ig_time);
                        textview.setText(result.getString("default_count"));
                        textview = (TextView) rootView.findViewById(R.id.ig_limit_type);
                        textview.setText(result.getString("current_count"));
                        textview = (TextView) rootView.findViewById(R.id.ig_user_Level);
                        textview.setText(result.getString("apply_count"));
                        //将截止时间只显示年月日
                        String[] strings=result.getString("endTime").split(" ");
                        textview = (TextView) rootView.findViewById(R.id.free_time);
                        textview.setText(strings[0]);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

        paraMap.clear();
        paraMap.put("id", goods_id);
        paraMap.put("begincount", 0);
        paraMap.put("selectcount", 2);
        Request<JSONObject> request2 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_logs.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("eva_list");
                        if (arr.length() == 0) {
                            rootView.findViewById(R.id.hot_evaluate).setVisibility(View.GONE);
                        } else {
                            rootView.findViewById(R.id.more).setOnClickListener(v -> {
                                        mActivity.go_free_evaluate(goods_id);
                                    });
                            JSONObject obj = arr.getJSONObject(0);
                            TextView tv = (TextView) rootView.findViewById(R.id.username1);
                            tv.setText(obj.getString("user_name"));
                            tv = (TextView) rootView.findViewById(R.id.time1);
                            tv.setText(obj.getString("evaluate_time"));
                            tv = (TextView) rootView.findViewById(R.id.evaluate1);
                            tv.setText(obj.getString("use_experience"));
                            if (arr.length() > 1) {
                                obj = arr.getJSONObject(1);
                                tv = (TextView) rootView.findViewById(R.id.username2);
                                tv.setText(obj.getString("user_name"));
                                tv = (TextView) rootView.findViewById(R.id.time2);
                                tv.setText(obj.getString("evaluate_time"));
                                tv = (TextView) rootView.findViewById(R.id.evaluate2);
                                tv.setText(obj.getString("use_experience"));
                            } else {
                                rootView.findViewById(R.id.hot_evaluate2).setVisibility(View.GONE);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request2);

        rootView.findViewById(R.id.add_to_car).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        if (mActivity.islogin()) {
                            RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                            Request<JSONObject> request1 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_apply.htm",
                                    result -> {
                                        try {
                                            int code = result.getInt("code");
                                            if (code == 100) {
                                                mActivity.go_free_apply(goods_id);
                                            } else {
                                                Toast.makeText(mActivity, "您有尚未评价的0元购或您申请过此0元购", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }, error -> mActivity.hideProcessDialog(1), mActivity.getParaMap());
                            mRequestQueue1.add(request1);
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        webview.loadUrl(mActivity.getAddress() + "/app/free_introduce.htm?id=" + goods_id);
        return rootView;
    }
}
