package com.ewgvip.buyer.android.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    退款订单
 */
public class OrderGroupLifeReturnListFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private List<Map> orderList;
    private int beginCount = 0;
    private int selectCount = 10;
    private ListView actualListView;
    private int current = -1;
    private PullToRefreshListView mPullRefreshListView;
    private OrderSearchListAdapter mAdapter;

    @Override
    public void onDetach() {
        super.onDetach();
        
        this.rootView = null;
        this.orderList = null;
        this.actualListView = null;
        this.mPullRefreshListView = null;
        this.mAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_order, container, false);
        mActivity = (BaseActivity) getActivity();
        orderList = new ArrayList<Map>();
        mActivity.showProcessDialog();
        getList();
        mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            beginCount=0;
            orderList.clear();
            refreshData();
        });
        mPullRefreshListView.setOnLastItemVisibleListener(() -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        new GetDataTask().execute();
                    }
                });

        actualListView = mPullRefreshListView.getRefreshableView();

        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);
        mAdapter = new OrderSearchListAdapter(mActivity, orderList);
        actualListView.setAdapter(mAdapter);

        View v = inflater.inflate(R.layout.order_return_list_header, null);
        actualListView.addHeaderView(v);

        return rootView;
    }

    private void refresh(final int refresh) {
        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("begin_count", refresh);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_refund.htm",
                result -> {
                    try {

                        JSONArray arr = result.getJSONArray("datas");
                        int length = arr.length();
                        JSONObject oj = arr.getJSONObject(0);
                        Map map = orderList.get(refresh);
                        map.put("group_goods_name", oj.getString("group_goods_name"));
                        map.put("group_status", oj.get("group_status"));
                        map.put("group_goods_price", oj.getString("group_goods_price"));
                        map.put("group_id", oj.getString("group_id"));
                        map.put("group_goods_img", oj.getString("group_goods_img"));
                        map.put("group_sn", oj.getString("group_sn"));
                        map.put("refund_msg", oj.getString("refund_msg"));
                        map.put("group_addTime", oj.getString("group_addTime"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    @Override
    public void onHiddenChanged(boolean hidden) {

        if (!hidden && current > -1) {
            refresh(current);
        }
        super.onHiddenChanged(hidden);
    }

    void getList() {
        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("begin_count", beginCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_refund.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("datas");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("group_goods_name", oj.getString("group_goods_name"));
                            map.put("group_status", oj.get("group_status"));
                            map.put("group_goods_price", oj.getString("group_goods_price"));
                            map.put("group_id", oj.getString("group_id"));
                            map.put("group_goods_img", oj.getString("group_goods_img"));
                            map.put("group_sn", oj.getString("group_sn"));
                            map.put("refund_msg", oj.getString("refund_msg"));
                            map.put("group_addTime", oj.getString("group_addTime"));
                            orderList.add(map);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();
                    mPullRefreshListView.onRefreshComplete();
                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }
    private void refreshData(){
        getList();
        mAdapter = new OrderSearchListAdapter(mActivity, orderList);
        actualListView.setAdapter(mAdapter);
    }

    private String getAddress() {
        String address = this.getResources().getString(R.string.http_url);
        return address;
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
            // 添加新加载的信息
            mAdapter.notifyDataSetChanged();

            // Call onRefreshComplete when the list has been refreshed.
            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    class OrderSearchListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<Map> mylist;

        public OrderSearchListAdapter(Context context, List<Map> list) {
            this.context = context;
            mylist = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mylist.size();
        }

        @Override
        public Object getItem(int position) {

            return mylist.get(position);
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

              ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.order_group_life_return_list_item, null);
                holder = new ViewHolder();
                holder.order_sn = (TextView) convertView.findViewById(R.id.order_sn);
                holder.order_addTime = (TextView) convertView.findViewById(R.id.order_price);
                holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img);
                holder.order_button = (Button) convertView.findViewById(R.id.order_button);
                holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int index = position;
            final Map map = mylist.get(position);
            holder.order_sn.setText("消费码:" + map.get("group_sn").toString());
            holder.order_addTime.setText("下单时间：" + map.get("group_addTime").toString());
            holder.goods_name.setText(map.get("group_goods_name").toString());

            BaseActivity.displayImage(map.get("group_goods_img").toString(), holder.img);

            holder.order_button.setText(map.get("refund_msg").toString());
            final Button view=holder.order_button;
            if (map.get("refund_msg").toString().equals("申请退款")) {
                holder.order_button.setBackgroundResource(R.drawable.frame_red_round_white_bg);
                holder.order_button.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            if("申请退款".equals(view.getText().toString().trim())) {
                                current = index;
                                Bundle bundle = new Bundle();
                                bundle.putString("oid", map.get("group_id").toString());
                                bundle.putString("goods_name", map.get("group_goods_name").toString());
                                bundle.putString("goods_img", map.get("group_goods_img").toString());
                                mActivity.go_grouplife_return_apply(bundle,mActivity.getCurrentfragment());
                            }
                        }
                    }
                });

            } else {
                holder.order_button.setBackgroundColor(getResources().getColor(R.color.white));
            }

            return convertView;
        }

        public class ViewHolder {
            public TextView order_addTime;
            public TextView order_sn;
            public SimpleDraweeView img;
            public Button order_button;
            public TextView goods_name;
        }
    }
}