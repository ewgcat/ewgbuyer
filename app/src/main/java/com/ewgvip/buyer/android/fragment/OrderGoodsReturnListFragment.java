package com.ewgvip.buyer.android.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
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
    退款/售后
 */
public class OrderGoodsReturnListFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private List orderList;
    private int beginCount = 0;
    private int selectCount = 20;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_order, container,
                false);
        mActivity = (BaseActivity) getActivity();
        orderList = new ArrayList();
        mActivity.showProcessDialog();
        getList();
        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.PULL_FROM_START);
        //下拉刷新
        mPullRefreshListView.setOnRefreshListener(refreshView -> {
            orderList.clear();
            beginCount = 0;
            refreshgetData();
        });
        //底部加载
        mPullRefreshListView.setOnLastItemVisibleListener(() -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (beginCount < orderList.size()) {
                    new GetDataTask().execute();
                }
            }
        });
        actualListView = mPullRefreshListView.getRefreshableView();
        registerForContextMenu(actualListView);
        mAdapter = new OrderSearchListAdapter(mActivity, orderList);
        actualListView.setAdapter(mAdapter);
        return rootView;
    }

    private void refresh(final int refresh) {

        Map paramap = mActivity.getParaMap();
        paramap.put("begin_count", refresh);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return.htm",
                result -> {
                    try {
                        Log.i("test", result.toString());
                        JSONArray arr = result.getJSONArray("datas");
                        JSONObject oj = arr.getJSONObject(0);
                        Map map = (Map) orderList.get(refresh);
                        map.put("oid", oj.has("oid") ? oj.get("oid") + "" : "");
                        map.put("order_id", oj.has("order_id") ? oj.get("order_id") + "" : "");
                        map.put("addTime", oj.has("addTime") ? oj.get("addTime") + "" : "");
                        List goods_list = new ArrayList();
                        JSONArray goods_maps = oj.getJSONArray("goods_maps");
                        for (int j = 0; j < goods_maps.length(); j++) {
                            JSONObject goods_oj = goods_maps.getJSONObject(j);
                            Map goods_map = new HashMap();
                            goods_map.put("goods_id", goods_oj.has("goods_id") ? goods_oj.get("goods_id") + "" : "");
                            goods_map.put("goods_name", goods_oj.has("goods_name") ? goods_oj.get("goods_name") + "" : "");
                            goods_map.put("goods_img", goods_oj.has("goods_img") ? goods_oj.get("goods_img") + "" : "");
                            goods_map.put("goods_gsp_ids", goods_oj.has("goods_gsp_ids") ? goods_oj.get("goods_gsp_ids") + "" : "");
                            goods_map.put("return_can", goods_oj.has("return_can") ? goods_oj.get("return_can") + "" : "");
                            goods_map.put("return_mark", goods_oj.has("return_mark") ? goods_oj.get("return_mark") + "" : "");
                            goods_list.add(goods_map);
                        }

                        map.put("goods_list", goods_list);

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
        paramap.put("begin_count", beginCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/goods_return.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("datas");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("oid", oj.has("oid") ? oj.get("oid") + "" : "");
                            map.put("order_id", oj.has("order_id") ? oj.get("order_id") + "" : "");
                            map.put("addTime", oj.has("addTime") ? oj.get("addTime") + "" : "");

                            List goods_list = new ArrayList();
                            JSONArray goods_maps = oj.getJSONArray("goods_maps");
                            for (int j = 0; j < goods_maps.length(); j++) {
                                JSONObject goods_oj = goods_maps.getJSONObject(j);
                                Map goods_map = new HashMap();
                                goods_map.put("goods_id", goods_oj.has("goods_id") ? goods_oj.get("goods_id") + "" : "");
                                goods_map.put("goods_name", goods_oj.has("goods_name") ? goods_oj.get("goods_name") + "" : "");
                                goods_map.put("goods_img", goods_oj.has("goods_img") ? goods_oj.get("goods_img") + "" : "");
                                goods_map.put("goods_gsp_ids", goods_oj.has("goods_gsp_ids") ? goods_oj.get("goods_gsp_ids") + "" : "");
                                goods_map.put("return_can", goods_oj.has("return_can") ? goods_oj.get("return_can") + "" : "");
                                goods_map.put("return_mark", goods_oj.has("return_mark") ? goods_oj.get("return_mark") + "" : "");
                                goods_list.add(goods_map);
                            }
                            map.put("goods_list", goods_list);
                            orderList.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();

                    mPullRefreshListView.onRefreshComplete();
                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(
                                View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    private void refreshgetData() {
        getList();
        actualListView = mPullRefreshListView.getRefreshableView();
        registerForContextMenu(actualListView);
        mAdapter = new OrderSearchListAdapter(mActivity, orderList);
        actualListView.setAdapter(mAdapter);

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
        public View getView(final int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.order_goods_return_list_item, null);
                holder = new ViewHolder();
                holder.order_sn = (TextView) convertView.findViewById(R.id.order_sn);
                holder.order_addTime = (TextView) convertView.findViewById(R.id.order_price);
                holder.order_goods = (LinearLayout) convertView.findViewById(R.id.return_list);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int index = position;
            final Map map = (Map) mylist.get(position);
            holder.order_sn.setText("订单号:" + map.get("order_id").toString());
            holder.order_addTime.setText("下单时间："
                    + map.get("addTime") + "");

            holder.order_goods.removeAllViews();
            List return_goods_list = (List) map.get("goods_list");
            for (int i = 0; i < return_goods_list.size(); i++) {
                final Map object = (Map) return_goods_list.get(i);
                View return_goods = inflater.inflate(R.layout.return_goods, null);
                TextView tv = (TextView) return_goods.findViewById(R.id.goods_name);
                tv.setText(object.get("goods_name") + "");
                SimpleDraweeView iv_goods_icon = (SimpleDraweeView) return_goods.findViewById(R.id.iv_goods_icon);
                BaseActivity.displayImage(object.get("goods_img") + "", iv_goods_icon);
                Button order_button = (Button) return_goods.findViewById(R.id.order_button);
                order_button.setText(object.get("return_mark") + "");
                Log.i("test", object.get("return_mark") + "");
                if ((object.get("return_mark") + "").equals("已申请")) {
                    order_button.setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("goods_id", object.get("goods_id") + "");
                            bundle.putString("goods_gsp_ids", object.get("goods_gsp_ids") + "");
                            bundle.putString("oid", map.get("oid") + "");
                            bundle.putString("goods_name", object.get("goods_name") + "");
                            bundle.putString("goods_img", object.get("goods_img") + "");
                            mActivity.go_goods_reset(bundle, mActivity.getCurrentfragment());
                        }
                    });
                }

                if ((object.get("return_mark") + "").equals("填写退货物流")) {
                    order_button.setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("goods_id", object.get("goods_id") + "");
                            bundle.putString("goods_gsp_ids", object.get("goods_gsp_ids") + "");
                            bundle.putString("oid", map.get("oid") + "");
                            bundle.putString("goods_name", object.get("goods_name") + "");
                            bundle.putString("goods_img", object.get("goods_img") + "");
                            current = index;
                            mActivity.go_goods_return_trans(bundle, mActivity.getCurrentfragment());
                        }
                    });
                }


                if (Boolean.parseBoolean(object.get("return_can") + "")) {
                    order_button.setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            Bundle bundle = new Bundle();
                            bundle.putString("goods_id", object.get("goods_id") + "");
                            bundle.putString("goods_gsp_ids", object.get("goods_gsp_ids") + "");
                            bundle.putString("oid", map.get("oid") + "");
                            bundle.putString("goods_name", object.get("goods_name") + "");
                            bundle.putString("goods_img", object.get("goods_img") + "");
                            current = index;
                            mActivity.go_goods_return_apply(bundle, mActivity.getCurrentfragment());
                        }
                    });
                }

                holder.order_goods.addView(return_goods);
            }

            return convertView;
        }

        public class ViewHolder {
            public TextView order_addTime;
            public TextView order_sn;
            public LinearLayout order_goods;
        }
    }

}
