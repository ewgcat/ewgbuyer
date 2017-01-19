package com.ewgvip.buyer.android.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.GoodsListAdapter2;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.utils.ScrollListener;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品分类搜索功能
 */
public class StorePager2Fragment extends Fragment {

    private View rootView;
    private BaseActivity mActivity;
    private String orderBy = "goods_salenum";
    private String orderByType = "desc";
    private int beginCount = 0;
    private int selectCount = 20;
    private List goods = new ArrayList();
    private String store_id;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoadingMore = true;
    private ScrollListener scrollListener;

    public static StorePager2Fragment getInstance(String store_id) {
        StorePager2Fragment fragment = new StorePager2Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_pager2, container, false);
        mActivity = (BaseActivity) getActivity();

        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.popularity)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sales_volume)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.price)).setIcon(R.mipmap.priceno));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.newgoods)));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();

                    switch (position) {
                        case 0:
                            orderBy = "goods_click";
                            break;
                        case 1:
                            orderBy = "goods_salenum";
                            break;
                        case 2:
                            orderBy = "goods_current_price";
                            break;
                        case 3:
                            orderBy = "goods_seller_time";
                            break;
                        default:
                            orderBy = "goods_click";
                            break;
                    }
                    if (position == 2) {
                        if (orderByType.equals("asc")) {
                            orderByType = "desc";
                            tab.setIcon(R.mipmap.pricedown);
                        } else {
                            orderByType = "asc";
                            tab.setIcon(R.mipmap.priceup);
                        }
                    } else {
                        orderByType = "desc";
                    }
                    goods.clear();
                    beginCount = 0;

                    mActivity.showProcessDialog();
                    init(orderBy, orderByType);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();
                    if (position == 2) {
                        tab.setIcon(R.mipmap.priceno);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();
                    if (position == 2) {
                        if (orderByType.equals("asc")) {
                            orderByType = "desc";
                            tab.setIcon(R.mipmap.pricedown);
                        } else {
                            orderByType = "asc";
                            tab.setIcon(R.mipmap.priceup);
                        }
                        goods.clear();
                        beginCount = 0;
                        mActivity.showProcessDialog();
                        init(orderBy, orderByType);
                    }
                }
            }
        });

        //列表数据
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.goods_list_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        //设置布局管理器
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new GoodsListAdapter2(goods, mActivity);
        mRecyclerView.setAdapter(mAdapter);
        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //添加分割线
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mActivity)
                        .color(R.color.divgray)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.zero)
                        .build());
        mRecyclerView.setOnTouchListener((view, motionEvent) -> {
            boolean result = false;
            if (scrollListener != null) {
                result = scrollListener.setOnTouchListener(view, motionEvent, mLayoutManager);
                return result;
            } else {
                return result;
            }
        });

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (scrollListener != null){
                    scrollListener.setScrollListener(recyclerView, dx, dy, mLayoutManager);
                }
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 10 表示剩下10个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 10 && dy > 0) {
                    if (isLoadingMore) {
                        init(orderBy, orderByType);
                        isLoadingMore = false;
                    }
                }
            }
        });
        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            store_id = bundle.getString("store_id");
            if (bundle.getString("orderBy") != null
                    && !bundle.getString("orderBy").equals("")) {
                orderBy = bundle.getString("orderBy");
            }
        }
    }

    @Override
    public void onResume() {
        init(orderBy, orderByType);
        super.onResume();
    }

    public void init(String orderBy, String orderType) {
        Map paraMap = new HashMap();
        paraMap.put("store_id", store_id);
        paraMap.put("orderBy", orderBy);
        paraMap.put("orderType", orderType);
        paraMap.put("beginCount", beginCount);
        paraMap.put("selectCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        NormalPostRequest request = new NormalPostRequest(mActivity, mActivity.getAddress()
                + "/app/goods_list.htm", result -> {
                    try {
                        if (0 == beginCount) {
                            goods.clear();
                        }
                        JSONArray nameList = result.getJSONArray("goods_list");// 获取JSONArray
                        int length = nameList.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = nameList.getJSONObject(i);
                            Map goods_map = new HashMap();
                            goods_map.put("id", oj.getInt("id"));
                            goods_map.put("goods_main_photo", oj.getString("goods_main_photo"));
                            goods_map.put("goods_name", oj.getString("goods_name"));
                            goods_map.put("goods_current_price", oj.getString("goods_current_price"));
                            goods_map.put("goods_salenum", oj.getString("goods_salenum"));
                            goods_map.put("status", oj.getString("status"));
                            goods_map.put("evaluate", oj.getString("evaluate"));
                            goods.add(goods_map);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (goods.size() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        rootView.findViewById(R.id.nodata_refresh).setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.GONE);
                    } else {
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                    beginCount += 20;
                    mAdapter.notifyDataSetChanged();
                    mActivity.hideProcessDialog(0);
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nodata_refresh).setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                }, paraMap);
        mRequestQueue.add(request);
    }

    public void setScrollListener(ScrollListener scrollListener) {
        if (null != scrollListener) {
            this.scrollListener = scrollListener;
        }
    }



}
