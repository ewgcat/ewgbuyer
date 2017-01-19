package com.ewgvip.buyer.android.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.adapter.ConsultListAdapter;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *咨询界面
 */
public class ConsulListActivity extends BaseActivity {
    private String goods_id;
    private int beginCount = 0;
    private int selectCount = 10;
    private List<Map<String, String>> consult = new ArrayList<Map<String, String>>();
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView mPullRefreshListView;
    private ConsultListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle("购买咨询");//设置标题
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        toolbar.hideOverflowMenu();
        Intent intent = getIntent();
        goods_id = intent.getStringExtra("id");
        mPullRefreshListView = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) findViewById(R.id.consulting_listview);
        /**
         * 下拉刷新
         */
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            beginCount = 0;
            consult.clear();
            new GetDataTask().execute();
        });

        /**
         * 底部更新
         */
        mPullRefreshListView.setOnLastItemVisibleListener(() -> getData());

    }

    private void getData() {
        Map paraMap = new HashMap();
        paraMap.put("id", goods_id);
        paraMap.put("beginCount", beginCount);
        paraMap.put("selectCount", selectCount);
        RequestQueue mRequestQueue = Volley.newRequestQueue(ConsulListActivity.this);
        Request<JSONObject> request = new NormalPostRequest(
                ConsulListActivity.this, getAddress()
                + "/app/goods_consult.htm",
                result -> {
                    try {
                        JSONArray consult_list = result
                                .getJSONArray("consult_list");
                        int length = consult_list.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = consult_list.getJSONObject(i);
                            Map<String, String> map = new HashMap<String, String>();
                            map.put("content", oj.getString("content"));
                            map.put("consult_user",
                                    oj.getString("consult_user"));
                            map.put("addTime", oj.getString("addTime"));
                            if (oj.getBoolean("reply")) {
                                map.put("reply_content",
                                        oj.getString("reply_content"));
                                map.put("reply_user",
                                        oj.getString("reply_user"));
                                map.put("reply_time",
                                        oj.getString("reply_time"));
                            }
                            consult.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mPullRefreshListView.setVisibility(View.VISIBLE);
                    findViewById(R.id.nodata).setVisibility(View.GONE);
                    if (null == mAdapter) {
                        mAdapter = new ConsultListAdapter(ConsulListActivity.this, consult);
                        mPullRefreshListView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                        mPullRefreshListView.onRefreshComplete();
                    }
                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        mPullRefreshListView.setVisibility(View.GONE);
                        findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    }
                    beginCount += selectCount;
                    mPullRefreshListView.onRefreshComplete();
                }, error -> {
                }, paraMap);
        mRequestQueue.add(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        consult.clear();
        beginCount = 0;
        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_consult, menu);
        return true;
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_index) {
            go_index();
            return true;
        }
        if (id == R.id.action_send) {
            Intent intent2 = new Intent(ConsulListActivity.this,
                    ConsultAddActivity.class);
            intent2.putExtra("id", goods_id);
            startActivity(intent2);
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            getData();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息
            super.onPostExecute(result);
        }
    }
}
