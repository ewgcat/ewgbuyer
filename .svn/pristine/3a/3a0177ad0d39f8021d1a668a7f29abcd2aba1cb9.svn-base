package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.fragment.LoginFragment;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:34
 * Description:  处理登录和注册的操作
 */
public class LoginActivity extends BaseActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Intent intent = getIntent();
        if (intent.hasExtra("msg")) {
            Toast.makeText(this, intent.getStringExtra("msg"), Toast.LENGTH_SHORT).show();
        }

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new LoginFragment();
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProcessDialog(0);
        SharedPreferences preferences=getSharedPreferences("user", Context.MODE_PRIVATE);
        String wechat_code=preferences.getString("wechat_code","");
        if (wechat_code.length()>0){
            Log.i("test","wechat_code=="+wechat_code);
            preferences.edit().remove("wechat_code").commit();
            third_login("wechat",wechat_code);
        }
    }

    public void third_login(final String type, final String value) {
        showProcessDialog();
        Map para = new HashMap();
        para.put("type", type);
        para.put("value", value);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(this).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(this,
                getResources().getString(R.string.http_url) + "/app/app_third_login.htm", json_result -> {
                    try {
                        int code = json_result.getInt("code");
                        Log.i("test","json_result=="+json_result.toString());
                        if (code == 100) {
                            // 保存登录信息
                            SharedPreferences preferences1 = getSharedPreferences(
                                    "user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences1.edit();
                            editor.putString("userName",
                                    (String) json_result.get("userName"));
                            editor.putString("user_id",
                                    (String) json_result.get("user_id"));
                            editor.putString("token",
                                    (String) json_result.get("token"));
                            editor.putString("verify",
                                    (String) json_result.get("verify"));
                            editor.commit();
                            //开启百度推送
                            String apikey = getCache("apikey");
                            if (apikey.equals("")) {
                                apikey = Constants.BAIDU_PUSH_API_KEY;
                            }
                            PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, apikey);
                            Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (code == -100) {
                            Bundle bundle = new Bundle();
                            if (type.equals("wechat")) {
                                bundle.putString("third_info", json_result.get("unionid") + "");
                                bundle.putString("third_type", type);
                            } else {
                                bundle.putString("third_info", value);
                                bundle.putString("third_type", type);
                            }

                            go_regist(bundle);
                        } else {
                            Toast.makeText(LoginActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        hideProcessDialog(0);
                    }
                }, error -> Log.i("test",error.toString()), para);
        mRequestQueue.add(request);
    }
}
