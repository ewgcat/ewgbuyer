package com.ewgvip.buyer.android.fragment;


import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.layout.MyWebViewClient;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshWebView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

/**
 * 网页
 */
@SuppressLint({"JavascriptInterface", "SetJavaScriptEnabled"})
public class WebFragment extends Fragment {

    public static final String JOIN_VIP = "/wap/buyer/vip-privilege.htm";
    public static final String JOIN_VIP_DOC = "/app/vip_join_doc.htm";
    public static final String REGIST_DOC = "/app/register_doc.htm";
    public static final String FORGET_PASSWORD = "/app/forget1.htm";
    public static final String COPYRIGHT = "/app/get_software_doc.htm";
    public static final String HELP = "/app/get_help_doc.htm";
    public static final String CHINA_STORE = "/app/topics_1.htm";
    public static final String GLOBLE_STORE = "/app/topics_2.htm";
    private View rootview;
    private PullToRefreshWebView mPullRefreshWebView;
    private WebView webview;
    private String type = "";
    private String address = "";
    private String url = "";
    private BaseActivity mActivity;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mActivity = (BaseActivity) getActivity();
        address = CommonUtil.getAddress(getActivity());
        type = bundle.getString("type", "");
        //如果为激活界面
        if (type.equals(JOIN_VIP)){
            rootview = inflater.inflate(R.layout.fragment_web3, container, false);
            webview = (WebView) rootview.findViewById(R.id.webview);
            String url = address + JOIN_VIP;
            webview.setWebViewClient(new  MyWebViewClient(mActivity));
            WebSettings webviewSettings = webview.getSettings();
            webviewSettings.setJavaScriptEnabled(true);
            webviewSettings.setUseWideViewPort(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webview.loadUrl(url);
        }else if (type.equals(JOIN_VIP_DOC)){
            rootview = inflater.inflate(R.layout.fragment_web2, container, false);
            webview = (WebView) rootview.findViewById(R.id.webview);
            rootview.findViewById(R.id.iv_back).setOnClickListener(v -> mActivity.onBackPressed());
            String url = address + JOIN_VIP_DOC;
            webview.setWebViewClient(new  MyWebViewClient(mActivity));
            WebSettings webviewSettings = webview.getSettings();
            webviewSettings.setJavaScriptEnabled(true);
            webviewSettings.setUseWideViewPort(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webview.loadUrl(url);
            //左边返回箭头
            rootview.findViewById(R.id.iv_back).setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(v);
                }
            });
        }else{
            rootview = inflater.inflate(R.layout.fragment_web, container, false);
            Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
            //设置toolbar
            mActivity.setSupportActionBar(toolbar);
            //设置导航图标
            toolbar.setNavigationIcon(R.mipmap.nav_arrow);
            toolbar.setNavigationOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                }
            });
            toolbar.setTitle("专题");//设置标题
            if (type.equals(REGIST_DOC)) {
                url = address + REGIST_DOC;
                toolbar.setTitle("《注册协议》");//设置标题
            }
            if (type.equals(FORGET_PASSWORD)) {
                url = address + FORGET_PASSWORD;
                toolbar.setTitle("找回密码");//设置标题
            }
            if (type.equals(COPYRIGHT)) {
                url = address + COPYRIGHT;
                toolbar.setTitle("软件使用许可说明");//设置标题
            }
            if (type.equals(HELP)) {
                url = address + HELP;
                toolbar.setTitle("使用帮助");//设置标题
            }
            if (type.equals("cloud_purchase")) {
                toolbar.setTitle("图文详情");//设置标题
            }
            if (type.equals(CHINA_STORE)) {
                url = address + CHINA_STORE;
                toolbar.setTitle("中国馆");//设置标题
            }
            if (type.equals(GLOBLE_STORE)) {
                url = address+GLOBLE_STORE;
                toolbar.setTitle("全球馆");
            }
            if (bundle.containsKey("url")) {
                url = bundle.getString("url");
            }
            mPullRefreshWebView = (PullToRefreshWebView) rootview.findViewById(R.id.web_content);
            mPullRefreshWebView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
            webview = mPullRefreshWebView.getRefreshableView();
            MyWebViewClient client = new MyWebViewClient(mActivity);
            webview.setWebViewClient(client);
            mPullRefreshWebView.setOnRefreshListener(refreshView -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    webview.loadUrl(url);
                }
            });
            WebSettings webviewSettings = webview.getSettings();
            webviewSettings.setJavaScriptEnabled(true);
            webviewSettings.setUseWideViewPort(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webviewSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            webview.addJavascriptInterface(this, "jsInterface");
            if (url.indexOf("http") < 0) {
                url = "http://" + url;
            }
            if (url.indexOf("?") > 0) {
                if (url.contains("device_type=iOS")){
                    url=  url.replace("device_type=iOS","device_type=Android");
                }else {
                    url += "&device_type=Android";
                }
            } else {
                url += "?device_type=Android";
            }
            webview.loadUrl(url);
        }
        return rootview;
    }



    @JavascriptInterface
    public void finish_activity() {
        getActivity().finish();
    }

    @JavascriptInterface
    public void gotoGoods(String goods_id) {
        mActivity.go_goods(goods_id);
    }

    @JavascriptInterface
    public void gotoGoodsBrand(String brandinfo) {
        mActivity.go_brand_goods(brandinfo);
    }

    @JavascriptInterface
    public void  gotoGoodsClass(String goodclassinfo){
        String[] split = goodclassinfo.split("[|]");
        mActivity.go_goodslist("gc_id", split[0], split[1]);
    }
    @JavascriptInterface
    public void  gotoActivity(String goodactivityinfo){
        String[] split = goodactivityinfo.split("[|]");
        Bundle bundle=new Bundle();
        bundle.putString("id",split[0]);
        mActivity.go_salespm(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                getFragmentManager().popBackStack();
                if (getTargetFragment()!=null){
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }

}
