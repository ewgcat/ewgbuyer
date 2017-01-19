package com.ewgvip.buyer.android.net.subsrciber;


import android.util.Log;
import android.widget.Toast;

import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.networkutil.NetworkUtil;

import rx.Subscriber;


public abstract class BaseSubscriber<T> extends Subscriber<T> {
    private BaseActivity activity;


    public BaseSubscriber(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onError(Throwable e) {
        Log.i("test", "onError=="+e.getMessage());
        Log.i("test", "网络请求失败,请检查您的网络设置");
        Toast.makeText(activity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();
        activity.hideProcessDialog(1);

    }


    @Override
    public void onStart() {
        super.onStart();

        Log.i("test", "onStart==请求开始");
        activity.showProcessDialog();

        if (!NetworkUtil.isNetworkAvailable(activity)) {
            Toast.makeText(activity, "无网络，读取缓存数据", Toast.LENGTH_SHORT).show();
            onCompleted();
        }

    }



    @Override
    public void onCompleted() {

        Log.i("test", "onCompleted==请求结束");
        activity.hideProcessDialog(0);
    }


}
