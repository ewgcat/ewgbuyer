package com.ewgvip.buyer.android.wxapi;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.contants.Constants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:44
 * Description:  微信分享,登录的回调处理
 */
public class WXEntryActivity extends Activity implements IWXAPIEventHandler {

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
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            int result = 0;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = R.string.share_success;
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.share_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.share_fail;
                    break;
                default:
                    result = R.string.unknown_mistake;
                    break;
            }
            finish();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        } else if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
            SendAuth.Resp resp1 = (SendAuth.Resp) resp;
            int result = 0;
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = R.string.auth_success;
                    if (getString(R.string.app_name).equals(((SendAuth.Resp) resp).state)) {

                        SharedPreferences preferences = getSharedPreferences("user",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("wechat_code", ((SendAuth.Resp) resp).code);

                        editor.commit();
                    }
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = R.string.auth_cancel;
                    break;
                case BaseResp.ErrCode.ERR_AUTH_DENIED:
                    result = R.string.auth_deny;
                    break;
                default:
                    result = R.string.unknown_mistake;
                    break;
            }

            finish();
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
        }
    }


}