package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 支付密码修改功能
 */
public class PasswordPayModifyFragment extends Fragment {

    private EditText login_password;
    private EditText pay_password;
    private EditText re_password;
    private String msg = "";
    private BaseActivity mActivity;
    private View rootView;
    private ProgressDialog pd;
    private Button get_verification_code;
    private int second = 60;
    private String phonenumber;

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {

            if (second > 0) {
                get_verification_code.setText(--second + "秒后重新发送");
                mHandler.sendEmptyMessageDelayed(1, 1000);
            } else {
                get_verification_code.setEnabled(true);
                get_verification_code.setText("重新发送验证码");
            }
        }
    };


    @Override
    public void onDetach() {
        super.onDetach();
        this.login_password = null;
        this.pay_password = null;
        this.re_password = null;
        this.msg = null;

        this.rootView = null;
        this.pd = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_password_pay_modify, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("支付密码");
        mActivity = (BaseActivity) getActivity();
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
        //设置菜单可用
        setHasOptionsMenu(true);


        get_verification_code = (Button) rootView
                .findViewById(R.id.get_verification_code);
        get_verification_code.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                getMobilePassword();
            }
        });
        Button confirm_password = (Button) rootView.findViewById(R.id.confirm_password);
        login_password = (EditText) rootView.findViewById(R.id.login_password);
        pay_password = (EditText) rootView.findViewById(R.id.pay_password);
        re_password = (EditText) rootView.findViewById(R.id.repeatpassword);
        msg = "请求参数错误！";
        confirm_password.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                String login_psw = login_password.getText().toString().trim();
                final String pay_psw = pay_password.getText().toString().trim();
                String re_psw = re_password.getText().toString().trim();
                boolean flag = true;
                msg = "";
                if (flag && login_psw.equals("")) {
                    flag = false;
                    msg = "手机验证码不能为空！";
                }
                if (flag && pay_psw.equals("")) {
                    flag = false;
                    msg = "支付密码不能为空！";
                }
                if (flag && pay_psw.length() < 6) {
                    flag = false;
                    msg = "支付密码至少为6位字符！";
                }
                if (flag && login_psw.equals(pay_psw)) {
                    flag = false;
                    msg = "支付密码不可与登录密码相同！";
                }
                if (flag & !login_psw.equals("")) {
                    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(login_psw);
                    if (m.find()) {
                        flag = false;
                        msg = "手机验证码不能包含特殊字符！";
                    }
                }
                if (flag & !pay_psw.equals("")) {
                    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(pay_psw);
                    if (m.find()) {
                        flag = false;
                        msg = "支付密码不能包含特殊字符！";
                    }
                }
                if (flag && !pay_psw.equals(re_psw)) {
                    flag = false;
                    msg = "两次输入支付密码不一致！";
                }
                if (flag) {
                    // 通过Activity验证，
                    pd = ProgressDialog.show(getActivity(),
                            null, "信息提交中…");
                    // 注册进度条
                    Map paraMap = mActivity.getParaMap();
                    paraMap.put("mobile", phonenumber);
                    paraMap.put("new_password", pay_psw);
                    paraMap.put("code", login_psw);
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            getActivity(), CommonUtil.getAddress(mActivity)
                            + "/app/buyer/pay_password_modify.htm",
                            result -> {
                                try {
                                    // 关闭登录进度条
                                    pd.dismiss();
                                    if (result.get("code").toString().equals("100")) {
                                        mActivity.hide_keyboard(arg0);
                                      SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);

                                        preferences.edit().putString("pay_password",mActivity.md5(pay_psw)).commit();
                                        mHandler.removeMessages(1);
                                        getFragmentManager().popBackStack();
                                        msg = "支付密码设置成功！";
                                    }
                                    if (result.get("code").toString()
                                            .equals("-100")) {
                                        msg = "支付密码设置失败！";
                                    }
                                    Toast.makeText(getActivity(), msg,
                                            Toast.LENGTH_SHORT).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }, error -> {
                            }, paraMap);
                    mRequestQueue.add(request);
                } else {
                    // 验证提示
                    Toast.makeText(getActivity(), msg,
                            Toast.LENGTH_SHORT).show();
                }
            }
        });


        mActivity.showProcessDialog();
        Map paraMap = mActivity.getParaMap();
        RequestQueue mRequestQueue = Volley.newRequestQueue(mActivity);
        Request<JSONObject> request = new NormalPostRequest(
                mActivity, CommonUtil.getAddress(mActivity)
                + "/app/buyer/hasphone.htm",
                result -> {
                    try {
                        if (result.get("code").toString().equals("100")) {
                            EditText editText = (EditText) rootView.findViewById(R.id.phonenumber);
                            phonenumber = result.get("mobile") + "";
                            editText.setText(phonenumber);
                            editText.setEnabled(false);
                            Toast.makeText(mActivity, "请使用绑定手机获取验证码", Toast.LENGTH_SHORT).show();
                        } else if (result.get("code").toString().equals("-100")) {
                            Toast.makeText(mActivity, "请输入手机号码获取验证码", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
        return rootView;
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

    private void getMobilePassword() {
        Map paraMap = mActivity.getParaMap();
        if (phonenumber != null) {
            paraMap.put("use", "pay_password");
        } else {
            EditText editText = (EditText) rootView.findViewById(R.id.phonenumber);
            final String mobile = editText.getText().toString();
            if (mobile.matches(Constants.phone_reg)) {
                paraMap.put("mobile", mobile);
                paraMap.put("use", "binding_mobile");
                phonenumber = mobile;
            } else {
                Toast.makeText(mActivity, "请输入正确的手机号码",
                        Toast.LENGTH_SHORT).show();
            }
        }
        if (paraMap.containsKey("use")) {
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(
                    getActivity(), CommonUtil.getAddress(mActivity)
                    + "/app/buyer/verifyCode_send.htm",
                    result -> {
                        try {
                            Log.i("test",result.toString());
                            if (result != null) {
                                if (result.get("code").toString()
                                        .equals("100")) {
                                    second = 60;
                                    get_verification_code.setEnabled(false);
                                    mHandler.sendEmptyMessageDelayed(
                                            1, 0);
                                    Toast.makeText(mActivity,
                                            "修改支付密码的验证码已发送到你的手机，请注意查收！",
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (result.get("code").toString()
                                        .equals("200")) {
                                    Toast.makeText(mActivity,
                                            "短信发送失败",
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (result.get("code").toString()
                                        .equals("300")) {
                                    Toast.makeText(mActivity,
                                            "系统没开启短信",
                                            Toast.LENGTH_SHORT).show();
                                }
                                if (result.get("code").toString()
                                        .equals("400")) {
                                    Toast.makeText(mActivity,
                                            "此手机号已绑定其他用户",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> {

                    }, paraMap);
            mRequestQueue.add(request);
        }
    }
}
