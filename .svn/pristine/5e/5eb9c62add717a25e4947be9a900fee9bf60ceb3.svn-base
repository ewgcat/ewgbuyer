package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
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
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.CouponGetListAdapter;
import com.ewgvip.buyer.android.models.CouponInformation;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 领取优惠券页面
 */
public class CouponsGetIndexFragment extends Fragment {


    private BaseActivity mActivity;
    private View rootView;
    private SharedPreferences sharedPreferences;
    private ListView lv_coupon;
    private List<CouponInformation> couponInformationList = new ArrayList<CouponInformation>();
    private CouponGetListAdapter getCouponListViewAdapter;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.sharedPreferences = null;
        this.lv_coupon = null;
        this.couponInformationList = null;
        this.getCouponListViewAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coupon_get_index, container, false);
        mActivity=(BaseActivity)getActivity();

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(mActivity.getResources().getString(R.string.getcoupon));//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        initView();
        initData();
        initListener();



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
        lv_coupon = (ListView) rootView.findViewById(R.id.lv_coupon);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mActivity = (BaseActivity) getActivity();
        sharedPreferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        getCouponListViewAdapter = new CouponGetListAdapter(mActivity, couponInformationList);
        lv_coupon.setAdapter(getCouponListViewAdapter);
        fillCouponListViewData();
    }

    /**
     * 初始化监听器事件
     */
    private void initListener() {
        /**
         * 优惠券点击事件领取优惠卷
         */
        lv_coupon.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                return;
            } else {
                CouponInformation couponInformation = couponInformationList.get(position - 1);
                Bundle bundle = new Bundle();
                bundle.putString("coupon_id", couponInformation.getCoupon_id());
                bundle.putString("iv_coupon_pic", couponInformation.getCoupon_pic());
                bundle.putString("tv_coupon_name", couponInformation.getCoupon_name());
                bundle.putString("tv_coupon_time", couponInformation.getCoupon_begin_time() + " 至 " + couponInformation.getCoupon_end_time());
                bundle.putString("tv_coupon_amount", couponInformation.getCoupon_amount());
                bundle.putString("tv_coupon_order_amount", "满" + couponInformation.getCoupon_order_amount() + "使用");
                bundle.putString("capture_status", couponInformation.getCoupon_status());
                mActivity.go_commit_coupon(bundle,CouponsGetIndexFragment.this);
            }
        });
    }

    /**
     * 填充优惠券列表数据
     */
    private void fillCouponListViewData() {
        mActivity.showProcessDialog();
        Map paraMap = mActivity.getParaMap();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/store_coupon.htm",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try {
                            parseCouponInformationJsonResponse(result);
                            if (couponInformationList.size() == 0) {
                                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                                    if (FastDoubleClickTools.isFastDoubleClick()) {
                                        fillCouponListViewData();
                                    }
                                });
                                lv_coupon.setVisibility(View.GONE);
                            } else {
                                lv_coupon.setVisibility(View.VISIBLE);
                                rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            }
                            getCouponListViewAdapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mActivity.hideProcessDialog(0);
                        }
                        mActivity.hideProcessDialog(0);
                    }
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    lv_coupon.setVisibility(View.GONE);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        couponInformationList.clear();
                        fillCouponListViewData();
                    });
                }, paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }

    /**
     * 解析数据优惠券数据
     */
    private void parseCouponInformationJsonResponse(JSONObject result) throws JSONException {
        JSONArray couponDataArray = result.getJSONArray("data");
        if (null != couponDataArray) {
            for (int i = 0; i < couponDataArray.length(); i++) {
                JSONObject jsonObjectItem = couponDataArray.getJSONObject(i);
                CouponInformation couponInformation = new CouponInformation();
                String coupon_id_string = jsonObjectItem.has("coupon_id") ? jsonObjectItem.getString("coupon_id") : "";
                String coupon_pic_string = jsonObjectItem.has("coupon_pic") ? jsonObjectItem.getString("coupon_pic") : "";
                String coupon_name_string = jsonObjectItem.has("coupon_name") ? jsonObjectItem.getString("coupon_name") : "";
                String coupon_begin_time_string = jsonObjectItem.has("coupon_begin_time") ? jsonObjectItem.getString("coupon_begin_time") : "";
                String coupon_end_time_string = jsonObjectItem.has("coupon_end_time") ? jsonObjectItem.getString("coupon_end_time") : "";
                String coupon_amount_string = jsonObjectItem.has("coupon_amount") ? jsonObjectItem.getString("coupon_amount") : "";
                String coupon_order_amount_string = jsonObjectItem.has("coupon_order_amount") ? jsonObjectItem.getString("coupon_order_amount") : "";
                String coupon_surplus_amount_string = jsonObjectItem.has("coupon_surplus_amount") ? jsonObjectItem.getString("coupon_surplus_amount") : "";
                String copture_status_string = jsonObjectItem.has("capture_status") ? jsonObjectItem.getString("capture_status") : "";
                couponInformation.setCoupon_id(coupon_id_string);
                couponInformation.setCoupon_pic(coupon_pic_string);
                couponInformation.setCoupon_name(coupon_name_string);
                couponInformation.setCoupon_begin_time(coupon_begin_time_string);
                couponInformation.setCoupon_end_time(coupon_end_time_string);
                couponInformation.setCoupon_amount(coupon_amount_string);
                couponInformation.setCoupon_order_amount(coupon_order_amount_string);
                couponInformation.setCoupon_surplus_amount(coupon_surplus_amount_string);
                couponInformation.setCoupon_status(copture_status_string);
                couponInformationList.add(couponInformation);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0) {
            couponInformationList.clear();
            initView();
            initData();
            initListener();
        }
    }
}
