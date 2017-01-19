package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.layout.MyWebViewClient;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.webviewiframe.MyTask;

import java.lang.reflect.InvocationTargetException;

/**
 * 商品详情,以web形式显示
 */
public class GoodsDetailFragment extends Fragment {


    private WebView webview;


    public static GoodsDetailFragment getInstance(String param1) {
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        Bundle args = new Bundle();
        args.putString("goods_id", param1);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        BaseActivity mActivity= (BaseActivity)getActivity();
        View rootView = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        Bundle bundle = getArguments();
        String goods_id = bundle.getString("goods_id");
        final SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");


        webview = (WebView) rootView.findViewById(R.id.webDetail);
        webview.setWebViewClient(new MyWebViewClient(mActivity));
        WebSettings webviewSettings = webview.getSettings();
        webviewSettings.setJavaScriptEnabled(true);
        webviewSettings.setUseWideViewPort(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        MyTask myTask = new MyTask(mActivity, webview);
        myTask.execute(CommonUtil.getAddress(getActivity()) + "/app/goods_introduce.htm?id=" + goods_id+"&user_id="+user_id);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            webview.getClass().getMethod("onPause").invoke(webview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            webview.getClass().getMethod("onResume").invoke(webview, (Object[]) null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }



}
