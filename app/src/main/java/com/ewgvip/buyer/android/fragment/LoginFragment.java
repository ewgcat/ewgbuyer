package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.ForgetPasswordActivity;
import com.ewgvip.buyer.android.activity.LoginActivity;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * 登录页面
 */
public class LoginFragment extends Fragment {

    private String result_code = "";
    private String result = "";
    private TextView textview_login_regist, textview_login_forgetpassword;
    private EditText edittext_login_username, edittext_login_password;
    private Button button_login;
    private ProgressDialog pd;
    private LoginActivity mActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login, container, false);
        mActivity = (LoginActivity) getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("登录");//设置标题

        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(v);
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        textview_login_regist = (TextView) rootView.findViewById(R.id.textview_login_regist);
        textview_login_forgetpassword = (TextView) rootView.findViewById(R.id.textview_login_forgetpassword);
        button_login = (Button) rootView.findViewById(R.id.button_login);
        edittext_login_username = (EditText) rootView.findViewById(R.id.edittext_login_username);
        edittext_login_password = (EditText) rootView.findViewById(R.id.edittext_login_password);

        edittext_login_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && edittext_login_password.getText().toString().length() > 0) {
                    button_login.setEnabled(true);
                } else {
                    button_login.setEnabled(false);
                }
            }
        });
        edittext_login_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0 && edittext_login_username.getText().toString().length() > 0) {
                    button_login.setEnabled(true);
                } else {
                    button_login.setEnabled(false);
                }
            }
        });
        textview_login_regist.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(view);
                mActivity.go_regist(null);
            }
        });
        textview_login_forgetpassword.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(view);
                startActivity(new Intent(getActivity(), ForgetPasswordActivity.class));
            }
        });


        button_login.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(view);
                String userName = edittext_login_username.getText().toString().trim();
                String password = edittext_login_password.getText().toString().trim();
                user_login(userName, password);
            }
        });
        rootView.findViewById(R.id.textview_login_wechatlogin).setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(view);
                mActivity.showProcessDialog();
                if (mActivity.mwxapi == null) {
                    mActivity.mwxapi = WXAPIFactory.createWXAPI(mActivity, Constants.WECHAT_API_KEY);
                }
                if (!mActivity.mwxapi.isWXAppInstalled()) {
                    Toast.makeText(mActivity, "您还未安装微信客户端", Toast.LENGTH_SHORT).show();
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                req.state = "wechat_sdk_demo_test";
                req.state = getString(R.string.app_name);
                mActivity.mwxapi.sendReq(req);
            }
        });

        return rootView;
    }




    void   user_login(String userName, String password) {
        if (password.equals("")) {
            Toast.makeText(getActivity(), "密码不能为空！", Toast.LENGTH_SHORT).show();
            return ;
        }
        if (userName.equals("")) {
            Toast.makeText(getActivity(), "用户名不能为空！", Toast.LENGTH_SHORT).show();
            return;
        }

        // 登录进度条
        pd = ProgressDialog.show(getActivity(), null, "登录中…");

        // 100,登陆成功,-100账号不存在，-200,密码不正确，-300登录失败,0无法链接服务器
        Map paraMap = new HashMap();
        paraMap.put("userName", userName);
        paraMap.put("password", password);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), getResources().getString(R.string.http_url) + "/app/iskyshop_user_login.htm", json_result -> {
                    try {

                        result_code = (String) json_result.get("code");
                        if (result_code.equals("100")) {// 登录成功
                            // 保存登录信息
                            SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("userName", (String) json_result.get("userName"));
                            editor.putString("user_id", (String) json_result.get("user_id"));
                            editor.putString("token", (String) json_result.get("token"));
                            editor.putString("verify", (String) json_result.get("verify"));
                            editor.commit();
                            //开启百度推送
                            String apikey = mActivity.getCache("apikey");
                            if (apikey.equals("")) {
                                apikey = Constants.BAIDU_PUSH_API_KEY;
                            }
                            PushManager.startWork(mActivity.getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, apikey);
                            //结束登录界面
                            getActivity().finish();
                            Toast.makeText(mActivity, "登录成功！", Toast.LENGTH_SHORT).show();
                        }else if (result_code.equals("-100")) {
                            Toast.makeText(mActivity, "账号不存在！", Toast.LENGTH_SHORT).show();
                        }else if (result_code.equals("-200")) {
                            Toast.makeText(mActivity,  "密码不正确！", Toast.LENGTH_SHORT).show();
                        }else if (result_code.equals("-300")) {
                            Toast.makeText(mActivity,  "登录失败！", Toast.LENGTH_SHORT).show();
                        }

                            pd.dismiss();// 关闭登录进度条
                    } catch (Exception e) {
                        e.printStackTrace();
                        pd.dismiss();
                    }
                }, error -> pd.dismiss(), paraMap);
        mRequestQueue.add(request);


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu,true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     * @param menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        getFragmentManager().popBackStack();
        getActivity().finish();
    }
}
