package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.CouponsAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填写订单时,选择优惠券
 */
public class Cart2CouponsFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater
                .inflate(R.layout.fragment_cart2_coupons, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("优惠券");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        mActivity.showProcessDialog();
        final Bundle bundle = getArguments();
        Map paramap = mActivity.getParaMap();
        paramap.put("store_ids", bundle.get("store_ids"));
        paramap.put("order_goods_price", bundle.get("order_goods_price"));
        final List<Map> coupon_list = new ArrayList<Map>();
        final PullToRefreshListView mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.coupons_list);
        mPullRefreshListView.setMode(Mode.DISABLED);
        final ListView coupons_listview = mPullRefreshListView
                .getRefreshableView();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/goods_cart2_coupon.htm",
                result -> {
                    try {
                        JSONArray jsonlist = result.getJSONArray("coupon_list");
                        int length = jsonlist.length();
                        if (length == 0)
                            rootView.findViewById(R.id.nocoupons).setVisibility(View.VISIBLE);
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = jsonlist.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("coupon_beginTime", oj.getString("coupon_beginTime"));
                            map.put("coupon_endTime", oj.getString("coupon_endTime"));
                            map.put("coupon_sn", oj.getString("coupon_sn"));
                            map.put("coupon_id", oj.getInt("coupon_id"));
                            map.put("coupon_name", oj.getString("coupon_name"));
                            map.put("coupon_status", "0");
                            map.put("coupon_amount", oj.getString("coupon_amount"));
                            map.put("coupon_info", oj.getString("coupon_info"));
                            map.put("store_name", oj.getString("store_name"));
                            map.put("coupon_addTime", oj.getString("coupon_addTime"));
                            map.put("coupon_order_amount", oj.getInt("coupon_order_amount"));
                            coupon_list.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    coupons_listview.setAdapter(new CouponsAdapter(
                            mActivity, coupon_list, "0"));
                    if (coupon_list.size() == 0) {
                        rootView.findViewById(R.id.nocoupons)
                                .setVisibility(View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

        coupons_listview.setOnItemClickListener((arg0, arg1, arg2, arg3) -> {
            if (getTargetFragment() == null)
                return;
            Bundle b = new Bundle();
            b.putString("coupons_id", coupon_list.get(arg2 - 1).get("coupon_id") + "");
            b.putString("coupon_amount", coupon_list.get(arg2 - 1).get("coupon_amount") + "");
            Intent i = new Intent();
            i.putExtras(b);
            getTargetFragment().onActivityResult(getTargetRequestCode(), Cart2Fragment.COUPONS, i);
            getFragmentManager().popBackStack();
        });
        return rootView;
    }
}
