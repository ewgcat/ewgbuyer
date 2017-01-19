package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * 领取优惠券确认领取输入验证码页面
 */
public class CouponsGetFragment extends Fragment {

    private static final String AUTH_CODE = "0123456789";
    private BaseActivity mActivity;
    private View rootView;
    private SharedPreferences sharedPreferences;
    private RequestQueue mRequestQueue;
    private SimpleDraweeView iv_coupon_pic;
    private TextView tv_coupon_name;
    private TextView tv_coupon_time;
    private TextView tv_coupon_amount;
    private TextView tv_coupon_order_amount;
    private EditText et_commit_number;
    private SimpleDraweeView tv_commit_number;
    private Button b_commit_number;
    private String commit_number;
    private String coupon_id;


    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.sharedPreferences = null;
        this.mRequestQueue = null;
        this.iv_coupon_pic = null;
        this.tv_coupon_name = null;
        this.tv_coupon_time = null;
        this.tv_coupon_amount = null;
        this.tv_coupon_order_amount = null;
        this.et_commit_number = null;
        this.tv_commit_number = null;
        this.b_commit_number = null;
        this.commit_number = null;
        this.coupon_id = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coupon_get_commit, container, false);
        initView();
        initData();
        initListener();
        getData();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.get_coupon_commit_title));//设置标题
        mActivity=(BaseActivity)getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单不可用
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
    /**
     * 初始化试图
     */
    private void initView() {
        iv_coupon_pic = (SimpleDraweeView) rootView.findViewById(R.id.iv_coupon_pic);
        tv_coupon_name = (TextView) rootView.findViewById(R.id.tv_coupon_name);
        tv_coupon_time = (TextView) rootView.findViewById(R.id.tv_coupon_time);
        tv_coupon_amount = (TextView) rootView.findViewById(R.id.tv_coupon_amount);
        tv_coupon_order_amount = (TextView) rootView.findViewById(R.id.tv_coupon_order_amount);
        et_commit_number = (EditText) rootView.findViewById(R.id.et_commit_number);
        tv_commit_number = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.tv_commit_number);
        b_commit_number = (Button) rootView.findViewById(R.id.b_commit_number);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mActivity = (BaseActivity) getActivity();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        coupon_id = getArguments().getString("coupon_id");
        String iv_coupon_pic_string = getArguments().getString("iv_coupon_pic");
        String tv_coupon_name_string = getArguments().getString("tv_coupon_name");
        String tv_coupon_time_string = getArguments().getString("tv_coupon_time");
        String tv_coupon_amount_string = getArguments().getString("tv_coupon_amount");
        String tv_coupon_order_amount_string = getArguments().getString("tv_coupon_order_amount");
        BaseActivity.displayImage(iv_coupon_pic_string, iv_coupon_pic);
        getData();
        tv_coupon_name.setText(tv_coupon_name_string);
        tv_coupon_time.setText(tv_coupon_time_string);
        tv_coupon_amount.setText(tv_coupon_amount_string);
        tv_coupon_order_amount.setText(tv_coupon_order_amount_string);
    }

    /**
     * 初始化监听器事件
     */
    private void initListener() {
        b_commit_number.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                String inputNumberString = et_commit_number.getText().toString();
                if (!"".equals(inputNumberString)) {
                    if (inputNumberString.equals(commit_number)) {
                        commitCoupon();
                        mActivity.hide_keyboard(view);
                    } else {
                        Toast.makeText(mActivity, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(mActivity, "验证码为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tv_commit_number.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                getData();
            }
        });
    }

    /**
     * 提交确认领取优惠券信息
     */
    private void commitCoupon() {
        String address = getResources().getString(R.string.http_url);
        String url = address + "/app/capture_coupon.htm";
        String user_id = sharedPreferences.getString("user_id", "");
        Map paraMap = new HashMap();
        paraMap.put("user_id", user_id);
        paraMap.put("coupon_id", coupon_id);
        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, url,
                result -> {
                    try {
                        String statusString = result.has("status") ? result.getString("status") : "";
                        if ("1".equals(statusString)) {
                            Bundle bundle = new Bundle();
                            JSONArray goodsDataArray = result.getJSONArray("goodsData");
                            bundle.putString("couponGoodsData", goodsDataArray.toString());
                            mActivity.go_success_coupon(bundle,CouponsGetFragment.this);
                        } else {
                            Toast.makeText(mActivity, getResources().getString(R.string.get_coupon_commit_fail), Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(mActivity, getResources().getString(R.string.parse_fail), Toast.LENGTH_SHORT).show();
                    }
                }, error -> {Log.i("test",error.toString());}, paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }

    /**
     * 设置验证码
     */
    private void getData() {
        String address = getResources().getString(R.string.http_url);
        String url = address + "/app_verify.htm";
        commit_number = getNumber();
        url += "?verify_str=" + commit_number;
        BaseActivity.displayImage(url, tv_commit_number);
    }

    /**
     * 随机验证码
     *
     * @return
     */
    private String getNumber() {
        String string = "";
        int index;
        for (int i = 0; i < 4; i++) {
            index = new Random().nextInt(AUTH_CODE.length());
            string += AUTH_CODE.charAt(index);
        }
        return string;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (getTargetFragment() == null)
            return;
        getTargetFragment().onActivityResult(getTargetRequestCode(),0, null);
        getFragmentManager().popBackStack();
    }
}
