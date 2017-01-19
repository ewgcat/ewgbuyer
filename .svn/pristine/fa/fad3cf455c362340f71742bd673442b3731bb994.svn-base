package com.ewgvip.buyer.android.wxapi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ewgvip.buyer.android.contants.Constants;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:42
 * Description:  微信的回调接收
 */
public class AppRegister extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        final IWXAPI api = WXAPIFactory.createWXAPI(context, Constants.WECHAT_API_KEY);
        api.registerApp(Constants.WECHAT_API_KEY);
    }
}