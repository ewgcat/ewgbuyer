package com.ewgvip.buyer.android.webviewiframe;

import android.app.Activity;
import android.os.AsyncTask;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by Administrator on 2016/9/3.
 */
public class MyTask extends AsyncTask<String, Integer, StringBuffer> {

    private WebView webview;// 用于显示HTML代码
    private StringBuffer sbHTML;// 用于储存HTML代码
    Activity activity;
    private String htmlContent;

    public MyTask(Activity activity, WebView edtHTTP) {
        this.activity = activity;
        this.webview = edtHTTP;
        sbHTML = new StringBuffer();
    }

    /**
     * doInBackground方法内部执行后台任务,不可在此方法内修改UI
     */
    @Override
    protected StringBuffer doInBackground(String... params) {
        // 初始化HTTP的客户端
        HttpClient hc = new DefaultHttpClient();
        // 实例化HttpGet对象
        HttpGet hg = new HttpGet(params[0]);

        try {
            // 让HTTP客户端已Get的方式请求数据，并把所得的数据赋值给HttpResponse的对象
            HttpResponse hr = hc.execute(hg);
            // 使用缓存的方式读取所返回的数据
            BufferedReader br = new BufferedReader(new InputStreamReader(hr.getEntity().getContent()));

            // 读取网页所返回的HTML代码
            String line = "";
            sbHTML = new StringBuffer();
            while ((line = br.readLine()) != null) {
                sbHTML.append(line);
            }

            return sbHTML;
        } catch (IOException e) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webview.loadData("获取网页HTML代码出错！！！","text/html", "utf-8");
                }
            });

        }
        return null;
    }

    /**
     * onPostExecute方法用于在执行完后台任务后更新UI,显示结果
     */
    @Override
    protected void onPostExecute(StringBuffer result) {
        // 判断是否为null，若不为null，则在页面显示HTML代码
        if (result != null) {
            htmlContent = result.toString();
            initedtHTTPData();

        }
        super.onPostExecute(result);
    }


    private void initedtHTTPData() {

        webview.getSettings().setJavaScriptEnabled(true); // 开启Javascript支持

        webview.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);// 滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上

        webview.getSettings().setLoadsImagesAutomatically(true);// 设置可以自动加载图片

        webview.getSettings().setAppCacheEnabled(true);// 应用可以有缓存

        webview.getSettings().setDomStorageEnabled(true);// 设置可以使用localStorage

        webview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);// 优先使用缓存

        webview.getSettings().setAppCacheMaxSize(10 * 1024 * 1024);// 缓存最多可以有10M

        webview.getSettings().setAllowFileAccess(true);// 可以读取文件缓存(manifest生效)

        webview.getSettings().setPluginState(WebSettings.PluginState.ON);

        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // 加速edtHTTP加载的方法

        webview.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH); // 提高渲染的优先级



        //判断html中是否包含视频<iframe width="300" height="150">标签

        if(htmlContent.indexOf("iframe") == -1 &&htmlContent.indexOf("IFRAME") == -1){

            // 设置加载进来的页面自适应手机屏幕

            webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        }else{

            webview.setWebChromeClient(new WebChromeClient()); // chrom


        }

        //采用javascript控制width和height标签值

        String javascript="<script type='text/javascript'>"  +

                "var y=document.getElementsByTagName('img');"  +

                "for(var i=0;i<y.length;i++){"  +

                "y[i].setAttribute('width','100%');"
                +

                "y[i].removeAttribute('height');"
                +

                "y[i].style.width='100%';"
                +

                "var str = y[i].getAttribute('style');"  +

                "str = str.replace(/height\\b\\s*\\:\\s*\\d+\\px;?/ig, '');"  +

                "y[i].setAttribute('style',str);}</script>";



        //html拼接

        htmlContent = htmlContent+javascript;

        webview.loadDataWithBaseURL("about:blank", htmlContent, "text/html", "utf-8", null);

    }
}
