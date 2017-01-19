package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
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
import com.ewgvip.buyer.android.adapter.MessageListAdapter;
import com.ewgvip.buyer.android.models.EwgMessage;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 消息列表页面
 */
public class MessageListFragment extends Fragment {
    ArrayList<EwgMessage> messageArrayList = new ArrayList<>();


    private Map paramap = new HashMap();
    private int beginCount = 0;
    private int selectCount = 10;
    private View rootView;
    private BaseActivity mActivity;
    private PullToRefreshListView mPullRefreshListView;
    private MessageListAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("消息列表");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        // Set a listener to be invoked when the list should be refreshed.
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                messageArrayList.clear();
                beginCount = 0;
                new GetDataTask().execute();
            }
        });
        // Add an end-of-list listener
        mPullRefreshListView.setOnLastItemVisibleListener(() -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                new GetDataTask().execute();
            }
        });

        ListView actualListView = mPullRefreshListView.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);

        mAdapter = new MessageListAdapter(mActivity, messageArrayList);
        actualListView.setAdapter(mAdapter);
        actualListView.setDivider(null);
        getList();

        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                messageArrayList.clear();
                beginCount = 0;
                new GetDataTask().execute();
            }
        });

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     *
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    void getList() {
        if (messageArrayList.size() % selectCount == 0) {
            paramap = mActivity.getParaMap();
            paramap.put("beginCount", beginCount);
            paramap.put("selectCount", selectCount);

            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/message.htm",
                    result -> {
                        try {
                            JSONArray msg_list = result.getJSONArray("msg_list");
                            int lenght = msg_list.length();
                            for (int i = 0; i < lenght; i++) {
                                JSONObject obj = msg_list.getJSONObject(i);
                                EwgMessage ewgMessage = new EwgMessage(
                                        obj.getString("fromUser"),
                                        obj.getString("addTime"),
                                        obj.getString("content"),
                                        obj.getString("id"),
                                        obj.getString("status"));
                                messageArrayList.add(ewgMessage);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        // 添加新加载的信息
                        mAdapter.notifyDataSetChanged();

                        // Call onRefreshComplete when the list has been refreshed.
                        mPullRefreshListView.onRefreshComplete();

                        if (beginCount == 0) {
                            if (mAdapter.getCount() == 0) {
                                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                mPullRefreshListView.setVisibility(View.GONE);
                            } else {
                                rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                                mPullRefreshListView.setVisibility(View.VISIBLE);
                            }
                        }

                        beginCount += selectCount;
                        mActivity.hideProcessDialog(0);
                    }, error -> {
                        mPullRefreshListView.onRefreshComplete();
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                        mActivity.hideProcessDialog(1);
                    }, paramap);
            mRequestQueue.add(request);
        }
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            // 这里可以写查询事件
            getList();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            super.onPostExecute(result);
        }
    }

}
