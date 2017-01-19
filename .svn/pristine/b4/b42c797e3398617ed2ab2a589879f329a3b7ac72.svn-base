package com.ewgvip.buyer.android.fragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.OrderListAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
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
 * A simple {@link Fragment} subclass.
 * 订单查询页面
 */
public class OrderListFragment extends Fragment {
    public static int NUM = 1;//requestcode常量
    private int beginCount = 0;
    private int selectCount = 10;
    private BaseActivity mActivity;
    private ViewGroup rootView;
    private String status = "all";
    private int current = 0;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private List<Map> orderList = new ArrayList();//订单
    private OrderListAdapter mAdapter;//订单适配器




    //静态工厂方法
    public static Fragment getInstance(String order_status, int current) {

        Fragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", order_status);
        bundle.putInt("current", current);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_listview_order, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Bundle bundle = getArguments();
        status = bundle.getString("type");
        current = bundle.getInt("current");
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);

        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                new GetDataTask().execute();
            }
        });
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                orderList.clear();
                beginCount = 0;
                new GetDataTask().execute();
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        getList(status);

        mAdapter = new OrderListAdapter(mActivity, orderList, status, current);
        actualListView.setAdapter(mAdapter);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void getList(String orderBy) {
        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);
        paramap.put("order_status", orderBy);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_order.htm",
                result -> {
                    try {
                        if (result==null){
                            return;
                        }
                        JSONArray arr = result.getJSONArray("order_list");
                        int length = arr.length();
                        if (length > 0) {
                            for (int i = 0; i < length; i++) {
                                JSONObject oj = arr.getJSONObject(i);
                                Map map = new HashMap();
                                map.put("advance_din",oj.has("advance_din")?oj.get("advance_din")+"":"");
                                map.put("advance_type",oj.has("advance_type")?oj.get("advance_type")+"":"");
                                map.put("advance_wei",oj.has("advance_din")?oj.get("advance_wei")+"":"");
                                map.put("status",oj.has("status")?oj.get("status")+"":"");
                                map.put("order_special",oj.has("order_special")?oj.get("order_special")+"":"");
                                map.put("order_num", oj.getString("order_num"));
                                map.put("order_status", oj.getInt("order_status"));
                                map.put("order_id", oj.getString("order_id"));
                                map.put("addTime", oj.getString("addTime"));
                                map.put("order_total_price", oj.getString("order_total_price"));
                                map.put("promotion_flag", oj.getString("promotion_flag"));
                                JSONArray imgarr = oj.getJSONArray("photo_list");
                                int imglength = imgarr.length();
                                List imglist = new ArrayList();
                                for (int j = 0; j < imglength; j++) {
                                    imglist.add(imgarr.getString(j));
                                }
                                map.put("photo_list", imglist);

                                JSONArray name_list = oj.getJSONArray("name_list");
                                List namelist = new ArrayList();
                                for (int j = 0; j < name_list.length(); j++) {
                                    namelist.add(name_list.getString(j));
                                }
                                map.put("name_list", namelist);
                                JSONArray price_list = oj.getJSONArray("price_list");

                                List pricelist = new ArrayList();
                                for (int j = 0; j < price_list.length(); j++) {
                                    pricelist.add(price_list.getString(j));
                                }
                                map.put("price_list", pricelist);

                                JSONArray gsp_list = oj.getJSONArray("gsp_list");

                                List gsplist = new ArrayList();
                                for (int j = 0; j < gsp_list.length(); j++) {
                                    gsplist.add(gsp_list.getString(j));
                                }
                                map.put("gsp_list", gsplist);

                                JSONArray count_list = oj.getJSONArray("count_list");

                                List countlist = new ArrayList();
                                for (int j = 0; j < count_list.length(); j++) {
                                    countlist.add(count_list.getString(j));
                                }
                                map.put("count_list", countlist);

                                JSONArray id_list = oj.getJSONArray("id_list");

                                List idlist = new ArrayList();
                                for (int j = 0; j < id_list.length(); j++) {
                                    idlist.add(id_list.getString(j));
                                }
                                map.put("id_list", idlist);

                                orderList.add(map);
                            }
                            // 添加新加载的信息
                            mAdapter.update(orderList);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mPullRefreshListView.onRefreshComplete();

                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                orderList.clear();
                                beginCount = 0;
                                mActivity.showProcessDialog();
                                new GetDataTask().execute();
                            }
                        });
                        mPullRefreshListView.setVisibility(View.GONE);
                    } else {
                        mPullRefreshListView.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    if (current == 0) {
                        if (OrderAllTabFragment.flag) {
                            mPullRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(AbsListView absListView, int i) {

                                }

                                @Override
                                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                                    OrderAllTabFragment.POSITION = i;
                                }
                            });
                        } else {
                            if (OrderAllTabFragment.POSITION > beginCount) {
                                getList(status);
                            } else {
                                actualListView.setSelection(OrderAllTabFragment.POSITION);
                                mPullRefreshListView.setOnScrollListener(new AbsListView.OnScrollListener() {
                                    @Override
                                    public void onScrollStateChanged(AbsListView absListView, int i) {

                                    }

                                    @Override
                                    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                                        OrderAllTabFragment.POSITION = i;
                                    }
                                });
                            }
                        }
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            orderList.clear();
                            beginCount = 0;
                            mActivity.showProcessDialog();
                            new GetDataTask().execute();
                        }
                    });
                    mPullRefreshListView.setVisibility(View.GONE);
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
