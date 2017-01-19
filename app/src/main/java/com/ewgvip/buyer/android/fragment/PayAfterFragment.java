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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 货到付款
 */

public class PayAfterFragment extends Fragment {

    private BaseActivity mActivity;

    @Override
    public void onDetach() {
        super.onDetach();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_pay_payafter, container, false);
        mActivity = (BaseActivity) getActivity();


        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        toolbar.setTitle("货到付款");
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        final Bundle bundle = getArguments();
        String priceSubmit = bundle.getString("totalPrice");
        final String order_id = bundle.getString("order_id");
        final String order_num = bundle.getString("order_num");
        TextView tv = (TextView) view.findViewById(R.id.textView1);
        tv.setText(order_num);
        tv = (TextView) view.findViewById(R.id.textView2);
        tv.setText("￥" + mActivity.moneytodouble(priceSubmit));
        view.findViewById(R.id.submit).setOnClickListener(
                v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.showProcessDialog();
                        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                        String user_id = preferences.getString("user_id", "");
                        String token = preferences.getString("token", "");
                        Map paramap = new HashMap<String, String>();
                        paramap.put("user_id", user_id);
                        paramap.put("token", token);
                        paramap.put("order_id", order_id);
                        EditText et = (EditText) view.findViewById(R.id.editText);
                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/pay_payafter.htm",
                                result -> {
                                    try {

                                        int code = result.getInt("code");
                                        if (code == 100) {
                                            bundle.putString("pay_type", "payafter");
                                            mActivity.go_success(bundle);
                                        }
                                        if (code == -100) {
                                            Toast.makeText(mActivity, "用户信息错误", Toast.LENGTH_SHORT).show();
                                        }
                                        if (code == -200) {
                                            Toast.makeText(mActivity, "订单信息错误", Toast.LENGTH_SHORT).show();
                                        }
                                        if (code == -300) {
                                            Toast.makeText(mActivity, "订单支付方式信息错误", Toast.LENGTH_SHORT).show();
                                        }
                                        if (code == -400) {
                                            Toast.makeText(mActivity, "系统未开启该支付功能，订单不可支付", Toast.LENGTH_SHORT).show();
                                        }

                                    } catch (Exception e) {
                                    }
                                    mActivity.hideProcessDialog(0);
                                }, error -> mActivity.hideProcessDialog(1), paramap);
                        mRequestQueue.add(request);
                    }
                });
        return view;
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