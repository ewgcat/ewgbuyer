package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.adapter.ConcernStoresAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
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
 * A simple {@link Fragment} subclass.关注店铺
 */
public class ConcernStoresFragment extends Fragment {


    private BaseActivity mActivity;
    private List list_store = new ArrayList();//店铺数据
    private int selectCount = 10;
    private View rootView;
    private PullToRefreshListView mlv_list;
    private ConcernStoresAdapter adapter_store;
    private TextView nodata_refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listview_single, container, false);
        mActivity = (MainActivity) getActivity();

        nodata_refresh = (TextView) rootView.findViewById(R.id.nodata_refresh);

        mlv_list = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mlv_list.setMode(PullToRefreshBase.Mode.PULL_FROM_START);


        adapter_store = new ConcernStoresAdapter(mActivity, list_store);
        mlv_list.setAdapter(adapter_store);
        /**
         * 店铺页面获取数据
         */
        mlv_list.setOnLastItemVisibleListener(() -> refresh());
        //店鋪頁面下拉刷新操作
        mlv_list.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... voids) {
                        String[] str = {};
                        list_store.clear();
                        refresh();
                        return str;

                    }

                    @Override
                    protected void onPostExecute(String[] result) {
                        super.onPostExecute(result);
                    }
                }.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });

        //重新加载数据
        nodata_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... voids) {
                        String[] str = {};
                        list_store.clear();
                        refresh();
                        return str;
                    }

                    @Override
                    protected void onPostExecute(String[] result) {
                        super.onPostExecute(result);
                    }
                }.execute();
                }
            }
        });
        refresh();
        return rootView;
    }

    //刷新店铺的方法    下拉刷新时必须清空list_goods里的东西
    public void refresh() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", list_store.size());
        paramap.put("endCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/favorite_store.htm",
                result -> {
                    try {
                        JSONArray map_list = result.getJSONArray("favorite_list");
                        int lenght = map_list.length();
                        for (int i = 0; i < lenght; i++) {
                            JSONObject obj = map_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("id", obj.getString("id"));
                            map.put("store_id", obj.getString("store_id"));
                            map.put("store_photo", obj.getString("store_photo"));
                            map.put("store_name", obj.getString("store_name"));
                            map.put("favorite_count", obj.getString("favorite_count"));
                            list_store.add(map);
                        }
                        adapter_store.notifyDataSetChanged();
                        mlv_list.onRefreshComplete();

                        if (adapter_store.getCount() == 0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            mlv_list.setVisibility(View.GONE);
                        } else {
                            mlv_list.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {Log.i("test",error.toString());}, paramap);
        mRequestQueue.add(request);
    }


}
