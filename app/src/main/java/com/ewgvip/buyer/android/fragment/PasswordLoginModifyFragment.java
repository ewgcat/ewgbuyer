package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
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
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 用户修改密码功能
 */
public class PasswordLoginModifyFragment extends Fragment {

    private EditText oldpaw;
    private EditText newpaw;
    private EditText repeatpassword;
    private String msg;
    private View rootView;
    private BaseActivity mActivity;

    @Override
    public void onDetach() {
        super.onDetach();
        this.oldpaw = null;
        this.newpaw = null;
        this.repeatpassword = null;
        this.msg = null;
        this.rootView = null;
        
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_password_login_modify, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("登录密码");
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


        Button button = (Button) rootView.findViewById(R.id.confirm_password);
        oldpaw = (EditText) rootView.findViewById(R.id.oldpassword);
        newpaw = (EditText) rootView.findViewById(R.id.newpassword);
        repeatpassword = (EditText) rootView.findViewById(R.id.repeatpassword);
        // 提交密码
        button.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                String oldpassword = oldpaw.getText().toString().trim();
                String newpassword = newpaw.getText().toString().trim();
                String repassword = repeatpassword.getText().toString().trim();
                boolean flag = true;
                msg = "";
                if (flag && oldpassword.equals("")) {
                    flag = false;
                    msg = "原密码不能为空！";
                }
                if (flag && newpassword.equals("")) {
                    flag = false;
                    msg = "新密码不能为空！";
                }
                if (flag & !oldpassword.equals("")) {
                    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(oldpassword);
                    if (m.find()) {
                        flag = false;
                        msg = "原密码不能包含特殊字符！";
                    }
                }
                if (flag & !newpassword.equals("")) {
                    String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
                    Pattern p = Pattern.compile(regEx);
                    Matcher m = p.matcher(newpassword);
                    if (m.find()) {
                        flag = false;
                        msg = "新密码不能包含特殊字符！";
                    }
                }
                if (flag && !newpassword.equals(repassword)) {
                    flag = false;
                    msg = "两次输入新密码不一致！";
                }
                if (flag) {
                    // 通过Activity验证，进行修改密码
                    // 弹出注册进度，发送注册请求并返回结果
                    // 注册进度条
                    Map paraMap = mActivity.getParaMap();
                    paraMap.put("new_password", newpassword);
                    paraMap.put("old_password", oldpassword);
                    mActivity.showProcessDialog();
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity, CommonUtil.getAddress(mActivity)
                            + "/app/buyer/password_modify.htm",
                            result -> {
                                try {
                                    if (result.get("code").toString().equals("100")) {
                                        msg = "修改成功！";
                                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.remove("user_id");
                                        editor.remove("token");
                                        editor.commit();
                                        mActivity.finish();
                                        Toast.makeText(mActivity, "请重新登录", Toast.LENGTH_SHORT).show();
                                        mActivity.go_login();
                                    } else if (result.get("code").toString().equals("-100")) {
                                        msg = "原密码错误！";
                                    } else if (result.get("code").toString().equals("-200")) {
                                        msg = "用户信息不正确！";
                                    }
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {

                                }
                                mActivity.hideProcessDialog(0);
                            }, error -> mActivity.hideProcessDialog(1), paraMap);
                    mRequestQueue.add(request);
                } else {
                    // 验证提示
                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                }
            }
        });
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

}



