package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
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
    零元购订单页面
 */
public class OrderFreeListFragment extends Fragment {
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
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("零元购订单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用

        orderList = new ArrayList<Map>();
        getList();
        mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.DISABLED);
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
        return rootView;
    }

    private void refresh(final int refresh) {
        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("beginCount", current);
        paramap.put("selectCount", 1);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/free_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");
                        JSONObject oj = arr.getJSONObject(0);
                        if(orderList.size()!=0) {
                            Map map = orderList.get(refresh);
                            map.put("evaluate_status", oj.get("evaluate_status"));
                            map.put("apply_status", oj.get("apply_status"));
                            map.put("order_id", oj.get("oid"));
                            map.put("goods_img", oj.get("goods_img"));
                            map.put("goods_name", oj.get("goods_name"));
                        }
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
        paramap.put("beginCount", beginCount);
        paramap.put("selectCount", selectCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/free_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("evaluate_status",oj.has("evaluate_status")? oj.get("evaluate_status"):"");
                            map.put("apply_status", oj.has("apply_status")?oj.get("apply_status"):"");
                            map.put("order_id",oj.has("oid")? oj.get("oid"):"");
                            map.put("goods_img",oj.has("goods_img")?oj.get("goods_img"):"");
                            map.put("goods_name",oj.has("goods_name")?oj.get("goods_name"):"");
                            map.put("addTime",oj.has("addTime")?oj.get("addTime"):"");
                            orderList.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();

                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                            orderList.clear();
                            beginCount = 0;
                            getList();
                        });
                        mPullRefreshListView.setVisibility(View.GONE);
                    } else {
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        mPullRefreshListView.setVisibility(View.VISIBLE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    if(rootView!=null) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                            orderList.clear();
                            beginCount = 0;
                            getList();
                        });

                    mPullRefreshListView.setVisibility(View.GONE);
                    }
                }, paramap);
        mRequestQueue.add(request);

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
            // return goodsName.size();
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
                convertView = inflater.inflate(R.layout.order_grouplife_list_item, null);
                holder = new ViewHolder();
                holder.orderNumber = (TextView) convertView.findViewById(R.id.order_sn);
                holder.orderPrice = (TextView) convertView.findViewById(R.id.order_price);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
                holder.button = (Button) convertView.findViewById(R.id.order_button);
                holder.order_reset = (Button) convertView.findViewById(R.id.order_reset);
                holder.img = (SimpleDraweeView) convertView.findViewById(R.id.img);
                holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
                holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.order_time.setVisibility(View.GONE);
            final int index = position;
            final Map map = mylist.get(position);
            holder.orderNumber.setText(map.get("addTime").toString());
            holder.order_reset.setVisibility(View.GONE);
            int apply_status = Integer.parseInt(map.get("apply_status").toString());
            if (apply_status == 0) {
                holder.orderStatus.setText("待审核");
            } else if (apply_status == 5) {
                holder.orderStatus.setText("审核通过");
            } else if (apply_status == -5) {
                holder.orderStatus.setText("申请失败");
            }
            int status = Integer.parseInt(map.get("evaluate_status").toString());
            if (status == 0) {
                holder.orderPrice.setText("未评价");
            } else if (status == 1) {
                holder.orderPrice.setText("已评价");
            } else if (status == 2) {
                holder.orderPrice.setText("审核未通过");
            }
            holder.button.setText("详情");

            holder.goods_name.setText(map.get("goods_name").toString());
            BaseActivity.displayImage(map.get("goods_img").toString(), holder.img);
            convertView.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    current = index;
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", map.get("order_id").toString());
                    mActivity.go_order_free_detail(bundle);
                }
            });
            holder.button.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    current = index;
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", map.get("order_id").toString());
                    mActivity.go_order_free_detail(bundle);
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public TextView orderNumber;
            public TextView orderPrice;
            public TextView orderStatus;
            public Button button;
            public SimpleDraweeView img;
            public TextView goods_name;
            public Button order_reset;
            private TextView order_time;
        }
    }
}
