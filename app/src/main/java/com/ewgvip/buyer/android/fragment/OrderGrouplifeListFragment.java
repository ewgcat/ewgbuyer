package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    生活购订单
 */
public class OrderGrouplifeListFragment extends Fragment {
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);
        mActivity = (BaseActivity) getActivity();
        orderList = new ArrayList<Map>();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("生活购订单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用

        mPullRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mPullRefreshListView.setMode(Mode.DISABLED);
        mPullRefreshListView
                .setOnLastItemVisibleListener(() -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        new GetDataTask().execute();
                    }
                });

        actualListView = mPullRefreshListView.getRefreshableView();
        // Need to use the Actual ListView when registering for Context Menu
        registerForContextMenu(actualListView);
        mAdapter = new OrderSearchListAdapter(mActivity, orderList);
        actualListView.setAdapter(mAdapter);
        getList();
        return rootView;
    }

    private void refresh(final int refresh) {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("beginCount", refresh);
        paramap.put("selectCount", 1);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");
                        JSONObject oj = arr.getJSONObject(0);
                        Map map = orderList.get(refresh);
                        map.put("order_num", oj.has("order_num") ? oj.get("order_num") + "" : "");
                        map.put("order_status", oj.has("order_status") ? oj.get("order_status") + "" : "");
                        map.put("order_id", oj.has("order_id") ? oj.get("order_id") + "" : "");
                        map.put("addTime", oj.has("addTime") ? oj.get("addTime") + "" : "");
                        map.put("order_total_price", oj.has("order_total_price") ? oj.get("order_total_price") + "" : "");

                        map.put("goods_name", oj.has("goods_name") ? oj.get("goods_name") + "" : "");
                        map.put("goods_img", oj.has("goods_img") ? oj.get("goods_img") + "" : "");
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
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("order_num", oj.has("order_num") ? oj.get("order_num") + "" : "");
                            map.put("order_status", oj.has("order_status") ? oj.get("order_status") + "" : "");
                            map.put("order_id", oj.has("order_id") ? oj.get("order_id") + "" : "");
                            map.put("addTime", oj.has("addTime") ? oj.get("addTime") + "" : "");
                            map.put("order_total_price", oj.has("order_total_price") ? oj.get("order_total_price") + "" : "");
                            map.put("goods_count", oj.has("goods_count") ? oj.get("goods_count") + "" : "");
                            map.put("goods_name", oj.has("goods_name") ? oj.get("goods_name") + "" : "");
                            map.put("goods_img", oj.has("goods_img") ? oj.get("goods_img") + "" : "");
                            orderList.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
                    mAdapter.notifyDataSetChanged();
                    mActivity.hideProcessDialog(0);
                    mPullRefreshListView.onRefreshComplete();
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                        orderList.clear();
                        beginCount = 0;
                        getList();
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
            getList();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
//            // 添加新加载的信息
//            mAdapter.notifyDataSetChanged();
//
//            // Call onRefreshComplete when the list has been refreshed.
//            mPullRefreshListView.onRefreshComplete();

            super.onPostExecute(result);
        }
    }

    class OrderSearchListAdapter extends BaseAdapter {
        Context context;
        LayoutInflater inflater;
        List<Map> mylist;
        String str_payment = "";

        public OrderSearchListAdapter(Context context, List<Map> list) {
            this.context = context;
            mylist = list;
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
                holder.img_list = (SimpleDraweeView) convertView.findViewById(R.id.img);
                holder.goods_name = (TextView) convertView.findViewById(R.id.goods_name);
                holder.order_time = (TextView) convertView.findViewById(R.id.order_time);
                holder.goods_order_num = (TextView) convertView.findViewById(R.id.goods_order_num);
                holder.rl_button = (RelativeLayout) convertView.findViewById(R.id.rl_button);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int index = position;
            final Map map = mylist.get(position);
            int status = Integer.parseInt(map.get("order_status") + "");

            holder.goods_name.setText(map.get("goods_name") + "");
            BaseActivity.displayImage(map.get("goods_img") + "", holder.img_list);

            final String order_total_price = map.get("order_total_price") + "";
            final String order_num = map.get("order_num") + "";
            final String order_id = map.get("order_id") + "";

            holder.orderNumber.setText("订  单  号:" + map.get("order_num") + "");
            holder.order_time.setText("下单时间:" + map.get("addTime") + "");
            holder.orderPrice.setText("总价：￥" + map.get("order_total_price") + "");
            holder.goods_order_num.setText("数量：" + map.get("goods_count") + "");
            String buttonText = "";
            String str = "";
            convertView.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    current = index;
                    mActivity.go_order_life(order_id);
                }
            });
            final Map paramap = mActivity.getParaMap();
            paramap.put("order_id", order_id);
            if (status == 0) {
                str = "已取消";
            }
            if (status == 10) {
                str = "待付款";
                buttonText = "立即支付";
                final Bundle bundle1 = new Bundle();
                bundle1.putString("totalPrice", order_total_price);
                bundle1.putString("order_id", order_id);
                bundle1.putString("order_num", map.get("order_num") + "");
                bundle1.putString("type", "life");
                holder.button.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        current = index;
                        mActivity.go_pay(bundle1, "payonline");
                    }
                });
                holder.order_reset.setOnClickListener(view -> {
                    final Map paramap1 = mActivity.getParaMap();
                    paramap1.put("oid", order_id);
                    new AlertDialog.Builder(mActivity)
                            .setTitle("您确定要取消订单吗?")
                            .setPositiveButton("确定", (dialog, which) -> {
                                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/grouplife_order_cancel.htm",
                                                result -> {
                                                    try {
                                                        if (result.get("ret").toString().equals("true")) {
                                                            Toast.makeText(mActivity, "取消订单成功", Toast.LENGTH_SHORT).show();
                                                            map.put("order_status",0);
                                                            mAdapter.notifyDataSetChanged();
                                                        }
                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }

                                                }, error -> mActivity.hideProcessDialog(1), paramap1);
                                        mRequestQueue.add(request);
                                    }).setNegativeButton("取消", null).show();
                });
                holder.order_reset.setVisibility(View.VISIBLE);
                holder.rl_button.setVisibility(View.VISIBLE);
                holder.button.setVisibility(View.VISIBLE);
            } else {
                holder.button.setVisibility(View.GONE);
                holder.rl_button.setVisibility(View.GONE);
            }
            if (status == 0) {
                str = "已取消";
            }
            if (status == 10) {
                str = "待付款";
                holder.rl_button.setVisibility(View.VISIBLE);
            }
            if (status == 20)
                str = "已付款";
            if (status == 30) {
                str = "已付款";
            }
            if (status == 50)
                str = "交易完毕";
            if (status == 65)
                str = "不可评价";
            holder.button.setText(buttonText);
            holder.orderStatus.setText(str);

            return convertView;
        }

        public class ViewHolder {
            public TextView orderNumber;
            public TextView orderPrice;
            public TextView orderStatus;
            public Button button;
            public SimpleDraweeView img_list;
            public TextView goods_name;
            public TextView order_time;
            public TextView goods_order_num;
            public RelativeLayout rl_button;
            public Button order_reset;
        }
    }
}
