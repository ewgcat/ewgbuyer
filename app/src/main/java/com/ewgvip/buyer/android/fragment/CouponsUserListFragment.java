package com.ewgvip.buyer.android.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.CouponsAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
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
 * 优惠券列表fragment
 */
public class CouponsUserListFragment extends Fragment {

    private int beginCount = 0;//开始查询位置
    private int selectCount = 20;//每次查询数量
    private BaseActivity mActivity;
    private ViewGroup rootView;
    private String status = "0";//优惠券类别0未使用1已使用-1已过期
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private CouponsAdapter mAdapter;//优惠券列表适配器
    private List<Map> couponsList;//优惠券信息集合

    //静态工厂方法
    public static Fragment getInstance(String status) {

        Fragment fragment = new CouponsUserListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("status", status);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.status = null;
        this.mPullRefreshListView = null;
        this.actualListView = null;
        this.mAdapter = null;
        this.couponsList = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_listview_order, container, false);
        mActivity = (BaseActivity) getActivity();
        couponsList = new ArrayList<>();
        Bundle bundle = getArguments();
        status = bundle.getString("status");
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.DISABLED);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> new GetDataTask().execute());
        mPullRefreshListView.setDividerDrawable(null);
        actualListView = mPullRefreshListView.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);
        getList(status);
        mAdapter = new CouponsAdapter(mActivity, couponsList, "1");
        actualListView.setAdapter(mAdapter);
        mActivity.showProcessDialog();
        return rootView;
    }

    private void getList(final String status) {
        Map paramap = mActivity.getParaMap();
        paramap.put("user_id", mActivity.getParaMap().get("user_id"));
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);
        paramap.put("status", status);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/coupon.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("coupon_list");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("coupon_beginTime", oj.getString("coupon_beginTime"));
                            map.put("coupon_endTime", oj.getString("coupon_endTime"));
                            map.put("coupon_sn", oj.getString("coupon_sn"));
                            map.put("coupon_id", oj.getInt("coupon_id"));
                            map.put("coupon_name", oj.getString("coupon_name"));
                            map.put("coupon_status", oj.getString("coupon_status"));
                            map.put("coupon_amount", oj.getString("coupon_amount"));
                            map.put("coupon_info", oj.getString("coupon_info"));
                            map.put("store_name", oj.getString("store_name"));
                            map.put("coupon_addTime", oj.getString("coupon_addTime"));
                            map.put("coupon_order_amount", oj.getInt("coupon_order_amount"));
                            couponsList.add(map);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // 添加新加载的信息
                    mAdapter.notifyDataSetChanged();
                    mPullRefreshListView.onRefreshComplete();
                    if (beginCount == 0 && couponsList.size() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                            couponsList.clear();
                            beginCount = 0;
                            getList(status);
                        });
                        mPullRefreshListView.setVisibility(View.GONE);
                    } else {
                        mPullRefreshListView.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    mPullRefreshListView.setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        couponsList.clear();
                        beginCount = 0;
                        getList(status);
                    });
                    rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                }, paramap);
        mRequestQueue.add(request);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            // 这里可以写查询事件
            getList(status);
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

}
