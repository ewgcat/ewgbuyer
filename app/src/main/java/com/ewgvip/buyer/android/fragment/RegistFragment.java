package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 注册页面
 */
public class RegistFragment extends Fragment {
    private boolean agree = true;
    private String type = "normal";
    private String phone = "";
    private String msg = "";
    private View rootview;
    private Button registbtu;
    private CheckBox agreeCheckbox;

    private TextView textView_agree;

    private ProgressDialog pd;
    private EditText phonenumber;
    private EditText verification_code;
    private EditText regist_passwordconfirm;
    private Button get_verification_code;
    private BaseActivity mActivity;
    private int second = 60;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (second > 0) {
                get_verification_code.setText(--second + "秒后重新发送");
                get_verification_code.setEnabled(false);
                mHandler.sendEmptyMessageDelayed(1, 1000);
            } else {
                second = 60;
                get_verification_code.setEnabled(true);
                get_verification_code.setText("重新发送验证码");
            }
        }
    };
    private EditText regist_second_password;
    private RequestQueue mRequestQueue;
    private String invitationCode;

    @Override
    public void onDetach() {
        super.onDetach();
        this.type = null;
        this.phone = null;
        this.msg = null;
        this.rootview = null;
        this.registbtu = null;
        this.agreeCheckbox = null;

        this.textView_agree = null;

        this.pd = null;
        this.phonenumber = null;
        this.verification_code = null;
        this.get_verification_code = null;
        this.regist_passwordconfirm = null;

        this.mHandler = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_regist, container, false);
        mActivity = (BaseActivity) getActivity();
        mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Toolbar toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("注册");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
        setHasOptionsMenu(true);
        //根据注册来源判断是否显示推荐人信息

        LinearLayout ll_vipleader_info = (LinearLayout) rootview.findViewById(R.id.ll_vipleader_info);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getString("invitationCode") != null) {

            ll_vipleader_info.setVisibility(View.VISIBLE);
            final SimpleDraweeView vipleader_img = (SimpleDraweeView) rootview.findViewById(R.id.vipleader_img);
            final TextView tv_vipleader_truename = (TextView) rootview.findViewById(R.id.tv_vipleader_truename);
            final TextView tv_vipleader_invitation_code = (TextView) rootview.findViewById(R.id.tv_vipleader_invitation_code);


            invitationCode = bundle.getString("invitationCode");
            Map map = new HashMap();
            map.put("invitation_code", invitationCode);
            Request<JSONObject> requestInvitationCode = new NormalPostRequest(mActivity, CommonUtil.getAddress(mActivity) + "/app/vip_my_parent_info.htm", response -> {
                        try {
                            String ret = response.get("ret").toString();
                            if (ret.equals("1")) {
                                String trueName = response.get("true_name").toString();
                                String photoUrl = response.get("photo_url").toString();
                                tv_vipleader_truename.setText("姓名：" + trueName);
                                tv_vipleader_invitation_code.setText("邀请码：" + invitationCode);
                                vipleader_img.setImageURI(Uri.parse(photoUrl));

                            } else if (ret.equals("0")) {
                                CommonUtil.showSafeToast(mActivity, "找不到这个邀请码的用户");
                            } else if (ret.equals("-1")) {
                                CommonUtil.showSafeToast(mActivity, "未识别该二维码");
                            } else if (ret.equals("-2")) {
                                CommonUtil.showSafeToast(mActivity, "这个邀请码对应多个用户");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {Log.i("test",error.toString());}, map);
            mRequestQueue.add(requestInvitationCode);


        } else {
            ll_vipleader_info.setVisibility(View.GONE);
        }

        //设置菜单可用
        registbtu = (Button) rootview.findViewById(R.id.button_regist);
        agreeCheckbox = (CheckBox) rootview.findViewById(R.id.checkbox_regist);
        textView_agree = (TextView) rootview.findViewById(R.id.textview_regist_agree);

        phonenumber = (EditText) rootview.findViewById(R.id.regist_phonenumber);
        regist_passwordconfirm = (EditText) rootview.findViewById(R.id.mobile_password);
        regist_second_password = (EditText) rootview.findViewById(R.id.mobile_second_password);
        verification_code = (EditText) rootview.findViewById(R.id.verification_code);
        get_verification_code = (Button) rootview.findViewById(R.id.get_verification_code);
        get_verification_code.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                final String mobile = phonenumber.getText().toString();
                if (!mobile.matches("1[3|4|5|7|8|][0-9]{9}")) {
                    Toast.makeText(mActivity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                } else {
                    Map paraMap = new HashMap();
                    paraMap.put("mobile", mobile);
                    RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/send_register_code.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
                        @Override
                        public void onNext(JSONObject jsonObject) {
                            int code;
                            try {
                                Log.i("test", jsonObject.toString());
                                code = jsonObject.getInt("ret");
                                if (code == 100) {
                                    second = 60;
                                    mHandler.sendEmptyMessageDelayed(1, 0);
                                    phone = mobile;
                                } else if (code == 200) {
                                    Toast.makeText(mActivity, "短信发送失败", Toast.LENGTH_SHORT).show();
                                } else if (code == 300) {
                                    Toast.makeText(mActivity, "商城未开启短信服务", Toast.LENGTH_SHORT).show();
                                } else if (code == 500) {
                                    Toast.makeText(mActivity, "此手机号已注册", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });

        // 同意注册协议
        agreeCheckbox.setOnCheckedChangeListener((arg0, arg1) -> {
            if (arg1) {
                registbtu.setBackgroundResource(R.drawable.rounded_red);
                registbtu.setTextColor(mActivity.getResources().getColor(R.color.white));
                agree = true;
            } else {
                registbtu.setBackgroundResource(R.drawable.rounded_grey);
                registbtu.setEnabled(false);
                registbtu.setClickable(false);
                registbtu.setTextColor(getResources().getColor(R.color.signintegral));
                registbtu.setBackgroundResource(R.drawable.button_unuse);
                agree = false;
            }
        });

        //查看注册协议
        textView_agree.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                // 发送请求查询注册协议，添加到TextView中
                Bundle bundle1 = new Bundle();
                bundle1.putString("type", WebFragment.REGIST_DOC);
                mActivity.go_web(bundle1);
            }

        });

        // 注册按钮
        registbtu.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(arg0);

                final String v_phone = phonenumber.getText().toString().trim();
                String v_code = verification_code.getText().toString().trim();
                final String regist_passwordconfirm_pwd = regist_passwordconfirm.getText().toString().trim();
                String regist_second_pwd = regist_passwordconfirm.getText().toString().trim();

                Map paraMap = new HashMap();
                boolean flag = true;
                msg = "";
                if (!agree) {
                    flag = false;
                }

                //手机号注册  验证输入框的信息是否符合格式
                if (flag && v_phone.equals("")) {
                    flag = false;
                    msg = "手机号不能为空！";
                }
                if (flag && !v_phone.matches("1[3|4|5|7|8|][0-9]{9}")) {
                    flag = false;
                    msg = "请输入正确的手机号码！";
                }
                if (flag && phone.equals("")) {
                    flag = false;
                    msg = "请发送验证码！";
                }
                if (flag && v_code.equals("")) {
                    flag = false;
                    msg = "验证码不能为空！";
                }
                if (flag && !phone.equals(v_phone)) {
                    flag = false;
                    msg = "验证码发送后请勿修改手机！";
                }
                if (flag && regist_passwordconfirm_pwd.equals("")) {
                    flag = false;
                    msg = "密码不能为空！";
                }
                if (flag && regist_second_pwd.equals("")) {
                    flag = false;
                    msg = "重复密码不能为空！";
                }
                if (!regist_passwordconfirm_pwd.equals(regist_second_pwd)) {
                    flag = false;
                    msg = "两次输入的密码不相同，请重新输入";
                }

                if (flag) {
                    // 弹出注册进度，发送注册请求并返回结果
                    pd = ProgressDialog.show(mActivity, null, "用户信息提交中…");
                    // 注册进度条
                    paraMap.clear();

                    Bundle bundle12 = getArguments();
                    if (bundle12 != null && bundle12.containsKey("third_type")) {
                        paraMap.put("third_type", bundle12.get("third_type"));
                        paraMap.put("third_info", bundle12.get("third_info"));
                    }
                    //手机号注册
                    paraMap.put("mobile", v_phone);
                    paraMap.put("verify_code", v_code);
                    paraMap.put("password", regist_passwordconfirm_pwd);
                    paraMap.put("invitation_code", invitationCode);
                    paraMap.put("type", "mobile");
                    paraMap.put("device_type", "android");


                    //http://12306.iok.la//app/register_finish.htm?mobile=18986170640&verify_code=717322&password=123456
                    RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity, CommonUtil.getAddress(mActivity)
                            + "/app/register_finish.htm",
                            result -> {
                                try {
                                Log.i("test",result.toString());
                                    msg = "无法链接服务器！";
                                    int code = result.getInt("code");
                                    if (code == 100) {
                                        mHandler.removeMessages(1);
                                        msg = "注册成功！";
                                        if (getArguments() == null) {
                                            mActivity.setCache("user_id",result.getString("user_id"));
                                            mActivity.setCache("verify",result.getString("verify"));
                                            mActivity.setCache("token",result.getString("token"));
                                            //注册成功，立即自动登录,跳转到是否成为e会员
                                            auto_login(v_phone, regist_passwordconfirm_pwd);
                                        }else {

                                            if (getArguments() .getString("invitationCode") != null){
                                                mActivity.setCache("user_id",result.getString("user_id"));
                                                mActivity.setCache("verify",result.getString("verify"));
                                                mActivity.setCache("token",result.getString("token"));
                                                //注册成功，立即自动登录,跳转到是否成为e会员
                                                auto_login(v_phone, regist_passwordconfirm_pwd);
                                            }else {
                                                SharedPreferences preferences=mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                                String wechat_code=preferences.getString("wechat_code","");
                                                if (wechat_code.length()>0){
                                                    preferences.edit().remove("wechat_code").commit();
                                                    third_login("wechat",wechat_code);
                                                }
                                            }
                                        }

                                    } else if (code == -100) {
                                        msg = "验证码错误！";
                                    } else if (code == -200) {
                                        msg = "用户名已存在";
                                    }

                                    if (!msg.equals("")) {
                                        Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
                                    }

                                } catch (Exception e) {
                                }
                                pd.dismiss();

                            }, error -> pd.dismiss(), paraMap);
                    mRequestQueue1.add(request);
                } else {
                    Toast.makeText(mActivity,
                            msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootview;
    }


    public void third_login(final String type, final String value) {
        Map para = new HashMap();
        para.put("type", type);
        para.put("value", value);
        RetrofitClient.getInstance(mActivity).createBaseApi().postJSONObject(getResources().getString(R.string.http_url) + "/app/app_third_login.htm", para, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    int code = jsonObject.getInt("code");
                    if (code == 100) {
                        // 保存登录信息
                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userName", (String) jsonObject.get("userName"));
                        editor.putString("user_id", (String) jsonObject.get("user_id"));
                        editor.putString("token", (String) jsonObject.get("token"));
                        editor.putString("verify", (String) jsonObject.get("verify"));
                        editor.commit();

                        Toast.makeText(mActivity, "登录成功", Toast.LENGTH_SHORT).show();
                       getActivity().finish();
                    } else{
                        Toast.makeText(mActivity, "登录失败,请用手机号登录", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }





    private void auto_login(String v_phone, String regist_passwordconfirm_pwd) {
        Map paraMap = new HashMap();
        paraMap.put("userName", v_phone);
        paraMap.put("password", regist_passwordconfirm_pwd);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(getResources().getString(R.string.http_url) + "/app/iskyshop_user_login.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String result_code = (String) jsonObject.get("code");
                    if (result_code.equals("100")) {// 登录成功
                        Toast.makeText(mActivity, "自动登录成功", Toast.LENGTH_SHORT).show();
                        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("userName", (String) jsonObject.get("userName"));
                        editor.putString("user_id", (String) jsonObject.get("user_id"));
                        editor.putString("token", (String) jsonObject.get("token"));
                        editor.putString("verify", (String) jsonObject.get("verify"));
                        editor.putString("MyLeaderInvitationCode", invitationCode);
                        editor.commit();

//                        if (PushManager.isPushEnabled(mActivity)) {
//                            PushManager.stopWork(mActivity);
//                            PushManager.resumeWork(mActivity);
//                        } else {
//                            String apikey = mActivity.getCache("apikey");
//                            if (apikey.equals("")) {
//                                apikey = Constants.BAIDU_PUSH_API_KEY;
//                            }
//                            PushManager.startWork(mActivity.getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, apikey);
//                        }
                        //登录成功跳跳转到是否成为e会员界面
                        Bundle bundle = new Bundle();
                        bundle.putString("type", WebFragment.JOIN_VIP);
                        mActivity.go_web2(bundle);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * 菜单图文混合
     *
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();
        getFragmentManager().popBackStack();
        if (getTargetFragment()!=null){
            getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
        }
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
