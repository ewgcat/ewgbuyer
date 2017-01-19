package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.ConcernGoodsAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
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
 * 我的收藏商品列表
 */
public class ConcernGoodsFragment extends Fragment {


    private final int selectCount = 10;
    private List list_goods = new ArrayList();//商品数据
    private List list_dialog = new ArrayList();//商品数据
    private View rootView;
    private BaseActivity mActivity;
    private PullToRefreshListView mlv_list_goods;
    private ConcernGoodsAdapter adapter_goods;
    private TextView nodata_refresh;


    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.list_goods = null;
        this.mlv_list_goods = null;
        this.adapter_goods = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_listview_single, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();

        mlv_list_goods = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        mlv_list_goods.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        nodata_refresh = (TextView) rootView.findViewById(R.id.nodata_refresh);

        adapter_goods = new ConcernGoodsAdapter(mActivity, list_goods);
        mlv_list_goods.setAdapter(adapter_goods);


        //商品页面获取数据操作   *mlv_list_goods为商品的ListView
        mlv_list_goods.setOnLastItemVisibleListener(() -> refresh_good());

        //商品页面下拉刷新操作
        mlv_list_goods.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

                new AsyncTask<Void, Void, String[]>() {
                    @Override
                    protected String[] doInBackground(Void... voids) {
                        String[] str = {};
                        list_goods.clear();
                        refresh_good();
                        return str;
                    }

                    @Override
                    protected void onPostExecute(String[] result) {
                        super.onPostExecute(result);


                    }
                }.execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            }
        });
        //重新加载数据
        nodata_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    new AsyncTask<Void, Void, String[]>() {
                        @Override
                        protected String[] doInBackground(Void... voids) {
                            String[] str = {};
                            list_goods.clear();
                            refresh_good();
                            return str;
                        }

                        @Override
                        protected void onPostExecute(String[] result) {
                            super.onPostExecute(result);
                        }
                    }.execute();
                }
            }
        });
        refresh_good();
        return rootView;
    }

    //刷新商品操作
    public void refresh_good() {

        Map paramap = mActivity.getParaMap();
        paramap.put("beginCount", list_goods.size());
        paramap.put("endCount", selectCount);

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/favorite_goods.htm",
                result -> {
                    try {
                        JSONArray map_list = result.getJSONArray("favorite_list");
                        int lenght = map_list.length();
                        for (int i = 0; i < lenght; i++) {
                            JSONObject obj = map_list.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("id", obj.get("id"));
                            map.put("goods_id", obj.get("goods_id"));
                            map.put("big_photo", obj.get("big_photo"));
                            map.put("goods_inventory", obj.get("goods_inventory"));
                            map.put("photo_ext", obj.get("photo_ext"));
                            map.put("addTime", obj.get("addTime"));
                            map.put("name", obj.get("name"));
                            map.put("good_price", obj.get("good_price"));
                            list_goods.add(map);
                        }
                        adapter_goods.notifyDataSetChanged();
                        mlv_list_goods.onRefreshComplete();
                        if (adapter_goods.getCount() == 0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            mlv_list_goods.setVisibility(View.GONE);
                        } else {
                            mlv_list_goods.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        }
                        mActivity.hideProcessDialog(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

}
