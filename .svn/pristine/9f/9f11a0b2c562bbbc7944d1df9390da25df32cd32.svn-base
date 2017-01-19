package com.ewgvip.buyer.android.net.subsrciber;

import android.content.Context;

import com.ewgvip.buyer.android.net.download.CallBack;
import com.ewgvip.buyer.android.net.download.DownLoadManager;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/8/15.
 */
public class DownSubscriber<ResponseBody> extends Subscriber<ResponseBody> {
    CallBack callBack;
    Context mContext;

    public DownSubscriber(Context context, CallBack callBack) {
        this.mContext = context;
        this.callBack = callBack;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (callBack != null) {
            callBack.onStart();
        }
    }

    @Override
    public void onCompleted() {
        if (callBack != null) {
            callBack.onCompleted();
        }
    }

    @Override
    public void onError(Throwable e) {
        if (callBack != null) {
            callBack.onError(e);
        }
    }

    @Override
    public void onNext(ResponseBody responseBody) {
        DownLoadManager.getInstance(callBack).writeResponseBodyToDisk(mContext, (okhttp3.ResponseBody) responseBody);
    }
}

