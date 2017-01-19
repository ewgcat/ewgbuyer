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
import com.ewgvip.buyer.android.adapter.FreeGoodsListAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 零元试用切换页面
 */
public class FreeListFragment extends Fragment {
    //开始查询位置
    private int beginCount = 0;
    //每次查询数量
    private int selectCount = 20;
    private BaseActivity mActivity;
    private ViewGroup rootView;
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    //零元购列表适配器
    private FreeGoodsListAdapter mAdapter;
    //零元购信息集合
    private List<Map> zeroList;
    private Bundle bundle;

    public FreeListFragment() {
    }

    //静态工厂方法
    public static Fragment getInstance(String class_id) {
        Fragment fragment = new FreeListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("class_id", class_id);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_listview_order, container, false);
        mActivity = (BaseActivity) getActivity();
        zeroList = new ArrayList<>();
        bundle = getArguments();
        mActivity.showProcessDialog();
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> new GetDataTask().execute());

        actualListView = mPullRefreshListView.getRefreshableView();
        registerForContextMenu(actualListView);
        getZeroUse(bundle.getString("class_id"));
        mAdapter = new FreeGoodsListAdapter(mActivity, zeroList);
        actualListView.setAdapter(mAdapter);
        actualListView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle bundle1 = new Bundle();
            bundle1.putString("id", zeroList.get(i - 1).get("free_id") + "");
            mActivity.go_zero_use_detail(bundle1);
        });
        return rootView;
    }

    private void getZeroUse(String class_id) {

        Map paraMap = new HashMap();
        paraMap.put("class_id", class_id);
        paraMap.put("begincount", beginCount);
        paraMap.put("selectcount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_index.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("free_list");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("free_count", obj.get("free_count"));
                            map.put("free_name", obj.get("free_name"));
                            map.put("free_price", obj.get("free_price"));
                            map.put("free_acc", obj.get("free_acc"));
                            map.put("free_id", obj.get("free_id"));
                            zeroList.add(map);
                        }
                    } catch (Exception e) {
                    }
                    mPullRefreshListView.onRefreshComplete();
                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                    } else {
                        mPullRefreshListView.setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                    mAdapter.notifyDataSetChanged();
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            String[] str = {};
            getZeroUse(bundle.getString("class_id"));
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }
}
