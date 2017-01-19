package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    积分商品订单
 */
public class OrderIntegralListFragment extends Fragment {
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

//        this.rootView = null;
//        this.orderList = null;
//        this.actualListView = null;
//        this.mPullRefreshListView = null;
//        this.mAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container,
                false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("积分商品订单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
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

    private void refresh(final int refresh) {

        Map paramap = mActivity.getParaMap();
        paramap.put("order_cat", 0);
        paramap.put("beginCount", refresh);
        paramap.put("selectCount", 1);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");

                        JSONObject oj = arr.getJSONObject(0);
                        Map map = orderList.get(refresh);
                        map.put("order_id", oj.getString("order_id"));
                        map.put("order_status", oj.getInt("igo_status"));
                        map.put("payType", oj.getString("payType"));
                        map.put("oid", oj.getString("oid"));
                        map.put("ship_price", oj.getString("ship_price"));
                        map.put("addTime", oj.getString("addTime"));
                        map.put("order_total_price", oj.getString("order_total_price"));
                        JSONArray imgarr = oj.getJSONArray("goods_list");
                        int imglength = imgarr.length();
                        List imglist = new ArrayList();
                        List namelist = new ArrayList();
                        for (int j = 0; j < imglength; j++) {
                            imglist.add(imgarr.getJSONObject(j).getString("goods_img"));
                            namelist.add(imgarr.getJSONObject(j).getString("goods_name"));
                        }
                        map.put("photo_list", imglist);
                        map.put("namelist", namelist);
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
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                mActivity.getAddress() + "/app/buyer/integral_order.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("order_list");
                        int length = arr.length();
                        for (int i = 0; i < length; i++) {
                            JSONObject oj = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("order_id", oj.getString("order_id"));
                            map.put("order_status", oj.getInt("igo_status"));
                            map.put("payType", oj.getString("payType"));
                            map.put("oid", oj.getString("oid"));
                            map.put("ship_price",
                                    oj.getString("ship_price"));
                            map.put("addTime", oj.getString("addTime"));
                            map.put("order_total_price", oj.getString("order_total_price"));
                            JSONArray imgarr = oj.getJSONArray("goods_list");
                            int imglength = imgarr.length();
                            List photo_list = new ArrayList();
                            List name_list = new ArrayList();
                            List count_list = new ArrayList();
                            for (int j = 0; j < imglength; j++) {
                                photo_list.add(imgarr.getJSONObject(j).getString("goods_img"));
                                name_list.add(imgarr.getJSONObject(j).getString("goods_name"));
                                count_list.add(imgarr.getJSONObject(j).getString("goods_count"));
                            }
                            map.put("photo_list", photo_list);
                            map.put("name_list", name_list);
                            map.put("count_list", count_list);
                            orderList.add(map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mAdapter.notifyDataSetChanged();

                    if (beginCount == 0 && mAdapter.getCount() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        mPullRefreshListView.setVisibility(View.GONE);
                    }
                    beginCount += selectCount;
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

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
        String str_payment = "";
        String payType = "";

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
                convertView = inflater.inflate(R.layout.order_integral_list_item, null);
                holder = new ViewHolder();
                holder.orderNumber = (TextView) convertView.findViewById(R.id.order_sn);
                holder.orderPrice = (TextView) convertView.findViewById(R.id.order_price);
                holder.orderStatus = (TextView) convertView.findViewById(R.id.order_status);
                holder.button = (Button) convertView.findViewById(R.id.order_button);
                holder.order_goods = (LinearLayout) convertView.findViewById(R.id.order_goods);
                holder.order_button2 = (Button) convertView.findViewById(R.id.order_button2);   //取消订单
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final int index = position;
            final Map map = mylist.get(position);
            holder.orderNumber.setText("订单号:" + map.get("order_id").toString());
            holder.orderPrice.setText("消耗积分：" + map.get("order_total_price").toString());
            int status = Integer.parseInt(map.get("order_status").toString());

            List<String> imglist = (List<String>) map.get("photo_list");
            List<String> name_list = (List<String>) map.get("name_list");
            List<String> count_list = (List<String>) map.get("count_list");
            holder.order_goods.removeAllViews();
            for (int i = 0; i < imglist.size(); i++) {
                View view = inflater.inflate(R.layout.order_normal_single_goods, null);
                SimpleDraweeView img = (SimpleDraweeView) view.findViewById(R.id.goods_img);
                img.setImageURI(Uri.parse(imglist.get(i)));
                TextView name = (TextView) view.findViewById(R.id.goods_name);
                name.setText(name_list.get(i));
                name = (TextView) view.findViewById(R.id.goods_count);
                name.setText("×" + count_list.get(i));

                holder.order_goods.addView(view);
            }

            String buttonText = "";
            payType = map.get("payType").toString();
            final String order_id = map.get("order_id").toString();
            final String oid = map.get("oid").toString();
            String str = "";
            final Map paramap = mActivity.getParaMap();
            paramap.put("order_id", order_id);
            paramap.put("oid", oid);
            final Map paramapreset = mActivity.getParaMap();
            paramapreset.put("oid", oid);

            holder.button.setText(buttonText);
            holder.orderStatus.setText(str);
            holder.button.setVisibility(View.GONE);
            holder.order_button2.setVisibility(View.GONE);
            if (status == -1) {
                holder.orderStatus.setText("已取消");
            }
            if (status == 0) {
                holder.orderStatus.setText("待付款");
                holder.button.setText("去支付");
                holder.order_button2.setVisibility(View.VISIBLE);
                holder.order_button2.setText("取消订单");
                holder.button.setVisibility(View.VISIBLE);
                final String pay_method = map.get("payType").toString();

                final Bundle bundle1 = new Bundle();

                //支付页，将order_id已order_number存储
                bundle1.putString("totalPrice", map.get("ship_price").toString());
                bundle1.putString("order_id", map.get("oid").toString());
                bundle1.putString("order_num", map.get("order_id").toString());
                bundle1.putString("type", "integral");
                holder.button.setOnClickListener(new OnClickListener() {
                    String paytype;

                    @Override
                    public void onClick(View v) {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            current = index;
                            mActivity.go_pay(bundle1, "payonline");
                        }
                    }
                });

                holder.order_button2.setOnClickListener(view -> {
                    new AlertDialog.Builder(mActivity)
                            .setTitle("您确定要取消订单吗?")
                            .setPositiveButton("确定", (dialog, which) -> {
                                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order_cancel.htm",
                                        result -> {
                                            try {
                                                if (result.get("ret").toString().equals("true")) {
                                                    Toast.makeText(mActivity, "取消订单成功", Toast.LENGTH_SHORT).show();
                                                    map.put("order_status", -1);
                                                    mAdapter.notifyDataSetChanged();
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }, error -> mActivity.hideProcessDialog(1), paramapreset);
                                mRequestQueue.add(request);

                            }).setNegativeButton("取消", null).show();
                });
            }


            if (status == 20)// 已付款
                holder.orderStatus.setText("待发货");
            if (status == 30) {
                holder.orderStatus.setText("已发货");
                holder.button.setText("确认收货");
                holder.button.setVisibility(View.VISIBLE);
                holder.button.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        new AlertDialog.Builder(mActivity)
                                .setTitle("您是否确已经收到兑换的礼品?")
                                .setPositiveButton("确定",
                                        (dialog, which) -> {
                                            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                                            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integral_order_complete.htm",
                                                    result -> {
                                                        try {
                                                            if (result.getString("ret").equals("true")) {
                                                                Toast.makeText(mActivity, "确认收货成功", Toast.LENGTH_SHORT).show();
                                                                refresh(index);
                                                            } else {
                                                                Toast.makeText(mActivity, "确认收货失败", Toast.LENGTH_SHORT).show();
                                                            }
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    },
                                                    error -> mActivity.hideProcessDialog(1), paramap);
                                            mRequestQueue.add(request);

                                        }).setNegativeButton("取消", null).show();
                    }

                });
            }
            if (status == 40) {
                holder.orderStatus.setText("已收货完成");
            }

            holder.order_goods.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    current = index;
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", map.get("oid").toString());
                    mActivity.go_order_integral_detail(bundle);
                }
            });

            convertView.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    current = index;
                    Bundle bundle = new Bundle();
                    bundle.putString("oid", map.get("oid").toString());
                    mActivity.go_order_integral_detail(bundle);
                }
            });

            return convertView;
        }

        public class ViewHolder {
            public TextView orderNumber;
            public TextView orderPrice;
            public TextView orderStatus;
            public Button button;
            public LinearLayout order_goods;
            private Button order_button2;
        }
    }
}
