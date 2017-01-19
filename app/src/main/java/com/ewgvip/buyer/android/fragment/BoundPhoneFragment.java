package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
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
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 绑定手机
 */
public class BoundPhoneFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;

    private Button get_verification_code;
    private Button yes;
    private EditText phonenumber;
    private EditText verification_code;
    private String phone;
    private int second = 60;

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

        this.rootView = null;
        this.get_verification_code = null;
        this.yes = null;
        this.phonenumber = null;
        this.verification_code = null;
        this.phone = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        mHandler.removeMessages(1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_bound_phone, container,
                false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("手机验证");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        get_verification_code = (Button) rootView.findViewById(R.id.get_verification_code);
        yes = (Button) rootView.findViewById(R.id.yes);
        phonenumber = (EditText) rootView.findViewById(R.id.phonenumber);
        verification_code = (EditText) rootView.findViewById(R.id.verification_code);
        get_verification_code.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                final String mobile = phonenumber.getText().toString();
                if (mobile.matches(Constants.phone_reg)) {
                    Map paraMap = mActivity.getParaMap();
                    paraMap.put("mobile", mobile);
                    paraMap.put("use", "binding_mobile");
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity, mActivity.getAddress()
                            + "/app/buyer/verifyCode_send.htm",
                            result -> {
                                try {
                                    int code = result.getInt("code");
                                    if (code == 100) {
                                        second = 60;
                                        mHandler.sendEmptyMessageDelayed(1, 0);
                                        get_verification_code.setEnabled(false);
                                        phone = mobile;
                                        Toast.makeText(mActivity, "已发送短信验证码信息", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == 200) {
                                        Toast.makeText(mActivity, "短信发送失败", Toast.LENGTH_SHORT).show();
                                    }
                                    if (code == 300) {
                                        Toast.makeText(mActivity, "系统没开启短信", Toast.LENGTH_SHORT).show();
                                    }
                                    if (result.get("code").toString().equals("400")) {
                                        Toast.makeText(mActivity, "此手机号已绑定其他用户", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, error -> mActivity.hideProcessDialog(1), paraMap);
                    mRequestQueue.add(request);
                } else {
                    Toast.makeText(mActivity, "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                }
            }
        });

        yes.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                String mobile = phonenumber.getText().toString();
                String mobile_verify_code = verification_code.getText().toString();
                if (phone != null && phone.equals(mobile)) {
                    Map paraMap = mActivity.getParaMap();
                    paraMap.put("mobile", mobile);
                    paraMap.put("mobile_verify_code", mobile_verify_code);
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(
                            mActivity, mActivity.getAddress()
                            + "/app/buyer/account_mobile_save.htm",
                            result -> {
                                try {
                                    int code = result.getInt("code");
                                    if (code == 100) {
                                        mActivity.hide_keyboard(v);
                                        mHandler.removeMessages(1);
                                        getFragmentManager().popBackStack();
                                        Bundle bundle = getArguments();
                                        if (bundle != null && bundle.containsKey("gg_price")) {
                                            mActivity.go_group_life_cart1(bundle);
                                        }
                                    }
                                    if (code == -100) {
                                        Toast.makeText(mActivity, "验证码错误", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, error -> mActivity.hideProcessDialog(1), paraMap);
                    mRequestQueue.add(request);

                } else {
                    Toast.makeText(mActivity, "请输入手机号码，获取并填写验证码", Toast.LENGTH_SHORT).show();
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
