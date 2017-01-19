package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FreeEvaluateListAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
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
 * 零元购评价列表
 */
public class FreeEvaluateListFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;

    private List list;
    private int begincount = 0;
    private int selectcount = 8;
    private ListView actualListView;
    private PullToRefreshListView mPullRefreshListView;
    private FreeEvaluateListAdapter mAdapter;
    private String id;


    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.list = null;
        this.actualListView = null;
        this.mPullRefreshListView = null;
        this.mAdapter = null;
        this.id = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar,
                container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("使用体验");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        Bundle bundle = getArguments();
        id = bundle.getString("id");

        list = new ArrayList();
        mActivity.showProcessDialog();
        init();
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.DISABLED);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> new GetDataTask().execute());
        actualListView = mPullRefreshListView.getRefreshableView();
        mAdapter = new FreeEvaluateListAdapter(mActivity, list);
        actualListView.setAdapter(mAdapter);
        return rootView;
    }

    private void init() {

        Map paraMap = new HashMap();
        paraMap.put("id", id);
        paraMap.put("begincount", begincount);
        paraMap.put("selectcount", selectcount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/free_logs.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("eva_list");
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("user_name", obj.get("user_name"));
                            map.put("evaluate_time", obj.get("evaluate_time"));
                            map.put("use_experience", obj.get("use_experience"));
                            map.put("user_photo", obj.get("user_photo"));
                            list.add(map);
                        }
                        mAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    begincount += selectcount;
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            // 这里可以写查询事件
            init();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息
            mAdapter.notifyDataSetChanged();
            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }
}
