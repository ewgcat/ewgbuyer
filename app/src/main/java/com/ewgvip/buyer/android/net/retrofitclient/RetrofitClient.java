package com.ewgvip.buyer.android.net.retrofitclient;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.net.apiservice.BaseApiService;
import com.ewgvip.buyer.android.net.converterfactory.JSONObjectConverterFactory;
import com.ewgvip.buyer.android.net.cookie.NovateCookieManger;
import com.ewgvip.buyer.android.net.download.CallBack;
import com.ewgvip.buyer.android.net.httpsfactroy.HttpsFactroy;
import com.ewgvip.buyer.android.net.interceptor.BaseInterceptor;
import com.ewgvip.buyer.android.net.interceptor.CaheInterceptor;
import com.ewgvip.buyer.android.net.subsrciber.DownSubscriber;
import com.ewgvip.buyer.android.utils.FileUtils;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class RetrofitClient {

    private static final int DEFAULT_TIMEOUT = 10;
    private BaseApiService apiService;
    private static OkHttpClient okHttpClient;
    public static String baseUrl = BaseApiService.Base_URL;
    private static Context mContext;
    private static Retrofit retrofit;
    private Cache cache = null;
    private File httpCacheDirectory;
    private static int[] certificates = {R.raw.ewgvipssl};

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .baseUrl(baseUrl);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
            .addNetworkInterceptor(new HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.HEADERS))
            .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);




    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }

    public static RetrofitClient getInstance(Context context) {
        if (context != null) {
            mContext = context;
        }
        return SingletonHolder.INSTANCE;
    }

    public static RetrofitClient getInstance(Context context, String url) {
        if (context != null) {
            mContext = context;
        }

        return new RetrofitClient(context, url);
    }

    public static RetrofitClient getInstance(Context context, String url, Map headers) {
        if (context != null) {
            mContext = context;
        }
        return new RetrofitClient(context, url, headers);
    }


    private RetrofitClient(Context context) {

        this(context, baseUrl, null);
    }

    private RetrofitClient(Context context, String url) {

        this(context, url, null);
    }

    private RetrofitClient(Context context, String url, Map headers) {

        if (TextUtils.isEmpty(url)) {
            url = baseUrl;
        }

        if (httpCacheDirectory == null&&context!=null) {
            File cacheDir = context.getCacheDir();
            if (!cacheDir.exists()) {
                try {
                    cacheDir= FileUtils.createSDDir("ewgvip_cache");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            httpCacheDirectory = new File(cacheDir, "ewgvip_cache");
        }

        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }


        try {

            okHttpClient = new OkHttpClient.Builder()
                    .addNetworkInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .cookieJar(new NovateCookieManger(context))
                    .cache(cache)
                    .addInterceptor(new BaseInterceptor(headers))
                    .addInterceptor(new CaheInterceptor(context))
                    .addNetworkInterceptor(new CaheInterceptor(context))
                    .hostnameVerifier(new AllowAllHostnameVerifier())
                    .sslSocketFactory(HttpsFactroy.getSSLSocketFactory(context , certificates), Platform.get().trustManager(HttpsFactroy.getSSLSocketFactory(context , certificates)))
                    // 这里你可以根据自己的机型设置同时连接的个数和时间，我这里8个，和每个保持时间为10s
                    .connectionPool(new ConnectionPool(8, 10, TimeUnit.SECONDS))
                    .build();

            retrofit = new Retrofit.Builder()
                    .client(okHttpClient)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(JSONObjectConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(url)
                    .build();



        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }






    /**
     * create BaseApi  defalte ApiManager
     *
     * @return ApiManager
     */
    public RetrofitClient createBaseApi() {
        apiService = create(BaseApiService.class);
        return this;
    }

    /**
     * create you ApiService
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     */
    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    // get请求
    public void getJSONObject(String url,Map parameters, Subscriber<JSONObject> subscriber) {
        if (parameters == null) { //无参
            apiService.executeJSONObjectGet1(url)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else if (parameters != null){//有参
            apiService.executeJSONObjectGet2(url, parameters)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    //post请求
    public void postJSONObject(String url,Map parameters, Subscriber<JSONObject> subscriber) {
        if (parameters == null) { // 无参
            apiService.executeJSONObjectPost1(url)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {//有参
            apiService.executeJSONObjectPost2(url, parameters)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }
    //get请求
    public void getResponseBody(String url,Map parameters, Subscriber<ResponseBody> subscriber) {
        if (parameters == null) { //无参
            apiService.executeResponseBodyGet1(url)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else if (parameters != null){//有参
            apiService.executeResponseBodyGet2(url, parameters)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }


    // post请求
    public void postResponseBody(String url,Map parameters, Subscriber<ResponseBody> subscriber) {
        if (parameters == null) { // 无参
            apiService.executeResponseBodyPost1(url)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        } else {//有参
            apiService.executeResponseBodyPost2(url, parameters)
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);
        }
    }

    //TODO 上传
    public void upload(String url, RequestBody requestBody, Subscriber<ResponseBody> subscriber) {
        apiService.upLoadFile(url, requestBody)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(subscriber);
    }

    //TODO 下载
    public void download(String url,Context context,final CallBack callBack) {
        apiService.downloadFilePOST(url)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe(new DownSubscriber<ResponseBody>(context,callBack));
    }


    public static <T> T execute(Observable<T> observable, Subscriber<T> subscriber) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);

        return null;
    }




}
