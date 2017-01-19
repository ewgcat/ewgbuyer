package com.ewgvip.buyer.android.layout;

import android.net.http.SslError;
import android.os.Bundle;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ewgvip.buyer.android.activity.BaseActivity;

/**
 * <p>
 * Title: MyWebViewClient.java
 * </p>
 * <p/>
 * <p>
 * Description:
 * 覆写WebViewClient,实现webview打开url失败的错误提醒及webview中的url均不在系统内置浏览器中打开新页面。
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author lixiaoyang
 * @version 1.0
 * @date 2014-7-17
 */
public class MyWebViewClient extends WebViewClient {
    BaseActivity mActivity;
    public MyWebViewClient(BaseActivity mActivity) {
        super();
        this.mActivity=mActivity;
    }

    // 重写错误页面。
    @Override
    public void onReceivedError(WebView view, int errorCode,
                                String description, String failingUrl) {

        view.getSettings().setDefaultTextEncodingName("UTF-8");
        super.onReceivedError(view, errorCode, description, failingUrl);
        String errorHtml = "<div style='padding-top:200px;text-align:center;color:#666;'>未打开无线网络</div>";
        view.loadDataWithBaseURL("", errorHtml, "text/html", "UTF-8", "");
    }


    // 重写点击链接不在浏览器中打开
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        if (url.contains("http://www.ewgvip.com/wap/login.htm")){
            mActivity.finish();
            return true;
        }
        if (url.contains("/wap/buyer/vip_level_up.htm")){
            Bundle bundle = new Bundle();
            bundle.putString("next_to_ui", "go_vip_level_up");
            mActivity.go_activite_vip(bundle);
            return true;
        }
        if (url.contains("index.htm")){
            mActivity.go_index();
            return true;
        }
        return super.shouldOverrideUrlLoading(view,url);

    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        view.getSettings().setBlockNetworkImage(false);
    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        handler.proceed();
    }
}
