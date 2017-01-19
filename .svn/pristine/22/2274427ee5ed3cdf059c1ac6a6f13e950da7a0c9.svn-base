package com.ewgvip.buyer.android.wxapi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;

import com.ewgvip.buyer.android.contants.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:45
 * Description:  微信支付的回调处理
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constants.WECHAT_API_KEY);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        finish();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        SharedPreferences preferences = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        Editor editor = preferences.edit();
        String str = "error";
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            if (resp.errCode == 0) {// 成功
                // 展示成功页面
                str = "success";
            } else if (resp.errCode == -1) {// 错误
                // 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。
                str = "fail";
            } else if (resp.errCode == -2) {// 用户取消
                // 无需处理。发生场景：用户不支付了，点击取消，返回APP。
                str = "cancle";
            }
        }
        editor.putString("payresult", str);
        if (editor.commit()) {
            finish();
        }
    }
}