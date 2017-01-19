package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.adapter.BillingAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
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
 * 预存款明细列表
 */
public class BalanceDetailListFragment extends Fragment {


    boolean b = false;
    private BillingAdapter adapterBilling;
    private BillingAdapter adapterBilling_insert;
    private BillingAdapter adapterBilling_out;
    private List list_billings = new ArrayList();
    private List list_billings_insert = new ArrayList();
    private List list_billings_out = new ArrayList();
    private BaseActivity mActivity;
    private View rootView;
    private PullToRefreshListView mlv_billinglist;
    private TextView tv_changeway;
    private ImageView iv_threecorn;
    private PopupWindow popupWindow;
    private ListView popupListView;
    private List stringList = new ArrayList();
    private PopupWindowListViewAdapter popupWindowListViewAdapter;
    private int selectCount = 20;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.adapterBilling = null;
        this.adapterBilling_insert = null;
        this.adapterBilling_out = null;
        this.list_billings = null;
        this.list_billings_insert = null;
        this.list_billings_out = null;
        this.mlv_billinglist = null;
        this.tv_changeway = null;
        this.iv_threecorn = null;
        this.popupWindow = null;
        this.popupListView = null;
        this.stringList = null;
        this.popupWindowListViewAdapter = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_billing_details, container, false);
        mActivity = (BaseActivity) getActivity();

        stringList.add("全部");
        stringList.add("转入");
        stringList.add("转出");
        mlv_billinglist = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mlv_billinglist.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        tv_changeway = (TextView) rootView.findViewById(R.id.tv_changeway_all);
        iv_threecorn = (ImageView) rootView.findViewById(R.id.iv_threecorn);

        popupListView = new ListView(mActivity);
        popupListView.setVerticalScrollBarEnabled(false);
        popupListView.setBackgroundResource(R.drawable.rounded_grey_popup);
        popupListView.setDividerHeight(1);

        popupWindowListViewAdapter = new PopupWindowListViewAdapter(mActivity, stringList);
        popupListView.setAdapter(popupWindowListViewAdapter);
        popupListView.setOnItemClickListener((adapterView, view, i, l) -> {
            popupWindow.dismiss();
            String price = (String) stringList.get(i);
            tv_changeway.setText(price);

            if ("全部".equals(price)) {
                refresh_billings();
                adapterBilling = new BillingAdapter(list_billings, mActivity);
                mlv_billinglist.setAdapter(adapterBilling);

            } else if ("转入".equals(price)) {

                refresh_billings_insert();
                adapterBilling_insert = new BillingAdapter(list_billings_insert, mActivity);
                mlv_billinglist.setAdapter(adapterBilling_insert);
            } else if ("转出".equals(price)) {
                refresh_billings_out();
                adapterBilling_out = new BillingAdapter(list_billings_out, mActivity);
                mlv_billinglist.setAdapter(adapterBilling_out);
            }
        });

        tv_changeway.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                iv_threecorn.setImageResource(R.mipmap.up_change);
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(popupListView, tv_changeway.getWidth() * 2, tv_changeway.getHeight() * 3 - 44);
                    popupWindow.setOnDismissListener(() -> iv_threecorn.setImageResource(R.mipmap.downchange));
                }
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.showAsDropDown(tv_changeway, 0, 0);
            }
        });

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("");//设置标题

        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        adapterBilling = new BillingAdapter(list_billings, mActivity);
        mlv_billinglist.setAdapter(adapterBilling);


        /**
         * 控件的刷新操作
         */
        mlv_billinglist.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... voids) {
                        String[] str = {};
                        list_billings.clear();
                        refresh_billings();
                        return str;
                    }

                    @Override
                    protected void onPostExecute(String[] result) {
                        super.onPostExecute(result);

                        adapterBilling.notifyDataSetChanged();
                        mlv_billinglist.onRefreshComplete();
                    }
                }.execute();
            }
        });
        refresh_billings();
        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //账单更新数据
    public void refresh_billings() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", list_billings.size());
        paramap.put("endCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                mActivity, getAddress() + "/app/buyer/predeposit_log.htm",
                result -> {
                    try {
                        JSONArray map_list = result.getJSONArray("data");
                        int lenght = map_list.length();
                        for (int i = 0; i < lenght; i++) {
                            JSONObject obj = map_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("type", obj.get("type")+"");
                            map.put("time", obj.get("time")+"");
                            map.put("amount", obj.get("amount")+"");

                            list_billings.add(map);

                        }
                        if (adapterBilling!=null) {
                            adapterBilling.notifyDataSetChanged();
                        }

                        mlv_billinglist.onRefreshComplete();

                        if(list_billings.size()==0)
                        {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.listview).setVisibility(View.GONE);
                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> refresh_billings());
                        }else {
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            rootView.findViewById(R.id.listview).setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test",error.toString()), paramap);
        mRequestQueue.add(request);

    }

    /**
     * 刷新账单的转入
     */
    public void refresh_billings_insert() {
        list_billings_insert.clear();
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", list_billings_insert.size());
        paramap.put("endCount", selectCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                getActivity(), getAddress() + "/app/buyer/predeposit_log.htm",
                result -> {
                    try {
                        JSONArray map_list = result.getJSONArray("data");
                        int lenght = map_list.length();
                        for (int i = 0; i < lenght; i++) {
                            JSONObject obj = map_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("type", obj.get("type")+"");
                            map.put("time", obj.get("time")+"");
                            map.put("amount", obj.get("amount")+"");
                            if (obj.getInt("amount") > 0) {
                                list_billings_insert.add(map);
                            }
                        }
                        if (adapterBilling_insert!=null) {
                            adapterBilling_insert.notifyDataSetChanged();
                        }

                        mlv_billinglist.onRefreshComplete();
                        if(list_billings_insert.size()==0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.listview).setVisibility(View.GONE);
                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    refresh_billings();
                                }
                            });
                        }else {
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            rootView.findViewById(R.id.listview).setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test",error.toString()), paramap);
        mRequestQueue.add(request);
    }

    /**
     *刷新账单转出
     */
    public void refresh_billings_out() {
        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", list_billings_out.size());
        paramap.put("endCount", selectCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                getActivity(), getAddress() + "/app/buyer/predeposit_log.htm",
                result -> {
                    try {
                        JSONArray map_list = result.getJSONArray("data");
                        int lenght = map_list.length();

                        for (int i = 0; i < lenght; i++) {
                            JSONObject obj = map_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("type", obj.getString("type"));
                            map.put("time", obj.get("time"));
                            map.put("amount", obj.get("amount"));
                            if (obj.getInt("amount") < 0) {
                                list_billings_out.add(map);
                            }
                        }
                        if (adapterBilling_out!=null) {
                            adapterBilling_out.notifyDataSetChanged();
                        }

                        mlv_billinglist.onRefreshComplete();
                        if(list_billings_out.size()==0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.listview).setVisibility(View.GONE);
                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> {
                                if (FastDoubleClickTools.isFastDoubleClick()){
                                    refresh_billings();
                                }
                            });
                        }else {
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            rootView.findViewById(R.id.listview).setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.i("test",error.toString()), paramap);
        mRequestQueue.add(request);

    }

    //获取地址
    private String getAddress() {
        String address = this.getResources().getString(R.string.http_url);
        return address;
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class PopupViewHolder {
        private TextView tv_changeway_item;
        /*private ImageView iv_integral_screen_item;*/
    }

    /**
     * 弹出框下拉列表的适配器
     */
    private class PopupWindowListViewAdapter extends BaseAdapter {

        /**
         * 环境变量
         */
        BaseActivity mActivity;
        /**
         * 保存传入的参数数据
         */
        private List list;
        /**
         * 打气筒对象
         */
        private LayoutInflater inflaterAdapter;

        /**
         * 重写构造器
         */
        public PopupWindowListViewAdapter(BaseActivity mActivity, List list) {
            this.mActivity = mActivity;
            this.list = list;
            inflaterAdapter = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            PopupViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (PopupViewHolder) view.getTag();
            } else {
                view = inflaterAdapter.inflate(R.layout.popupitem_changeway, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 48));
                view.setLayoutParams(lp);
                viewHolder = new PopupViewHolder();
                viewHolder.tv_changeway_item = (TextView) view.findViewById(R.id.tv_changeway_item);
                view.setTag(viewHolder);
            }
            viewHolder.tv_changeway_item.setText((String) stringList.get(position));
            return view;
        }
    }

}



