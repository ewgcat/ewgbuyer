package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.ActivityGoodsListAdapter;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.github.jdsjlzx.recyclerview.ProgressStyle;
import com.github.jdsjlzx.util.RecyclerViewStateUtils;
import com.github.jdsjlzx.view.LoadingFooter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 促销列表
 */
public class ActivityGoodsListFragment extends Fragment {
    private View rootView;
    private BaseActivity mActivity;
    private ActivityGoodsListAdapter activityGoodsListAdapter;
    private ArrayList list = new ArrayList<>();
    private Bundle bundle;
    private int pageNum = 1;
    private int pageSize = 20;
    private LRecyclerView mRecyclerView;
    private LRecyclerViewAdapter mLRecyclerViewAdapter;
    private boolean isRefresh=true;
    private int currentCount;
    private int newCount;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.activityGoodsListAdapter = null;
        this.bundle = null;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_activity_goods, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("促销活动");
        mActivity = (BaseActivity) getActivity();
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);

        mRecyclerView = (LRecyclerView) rootView.findViewById(R.id.recyclerview);

        activityGoodsListAdapter = new ActivityGoodsListAdapter(mActivity, list);
        mLRecyclerViewAdapter = new LRecyclerViewAdapter(activityGoodsListAdapter);
        mRecyclerView.setAdapter(mLRecyclerViewAdapter);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setArrowImageView(R.drawable.ic_pulltorefresh_arrow);
        mRecyclerView.setLScrollListener(new LRecyclerView.LScrollListener() {

            @Override
            public void onRefresh() {
                RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.Normal);
                activityGoodsListAdapter.clear();
                mLRecyclerViewAdapter.notifyDataSetChanged();
                list.clear();
                pageNum = 1;
                if (isRefresh) {
                    isRefresh = false;
                    refresh_list_salespm();
                }

            }

            @Override
            public void onScrollUp() {
            }

            @Override
            public void onScrollDown() {
            }

            @Override
            public void onBottom() {
                if (list.size() > currentCount) {
                    RecyclerViewStateUtils.setFooterViewState2(mActivity, mRecyclerView, 6, LoadingFooter.State.Loading, null);
                    pageNum++;
                    refresh_list_salespm();

                } else {
                    //the end
                    RecyclerViewStateUtils.setFooterViewState2(mActivity, mRecyclerView, 6, LoadingFooter.State.TheEnd, null);
                    mLRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onScrolled(int distanceX, int distanceY) {
            }

            @Override
            public void onScrollStateChanged(int state) {
            }
        });
        mRecyclerView.setPullRefreshEnabled(true);
        //获取上页传入的id值

        refresh_list_salespm();
        return rootView;
    }


    //刷新促销列表
    public void refresh_list_salespm() {
        mActivity.showProcessDialog();
        bundle = getArguments();
        String balace = bundle.getString("id", "");
        int baleces = Integer.parseInt(balace);
        Map paramap = mActivity.getParaMap();
        paramap.put("actId", baleces);
        paramap.put("pageNum", pageNum);
        paramap.put("pageSize", pageSize);
        currentCount = list.size();
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(
                getActivity(), CommonUtil.getAddress(mActivity) + "/app/getActivityGoods.htm",
                result -> {
                    try {
                        if (result.getInt("code") == 200) {
                            if (list.size() == 0) {
                                //标题
                                JSONObject obj = result.getJSONObject("result_activtiy");
                                Map map = new HashMap();
                                map.put("activity_image", obj.getString("activity_image"));
                                map.put("beginTime", obj.getString("beginTime"));
                                map.put("endTime", obj.getString("endTime"));
                                Map titlemap = new HashMap();
                                titlemap.put("title", map);
                                list.add(titlemap);
                            }


                            //商品
                            JSONArray map_list = result.getJSONArray("result_goodslist");
                            int lenght = map_list.length();

                            for (int i = lenght - 1; i >= 0; i = i - 2) {
                                Map line_map = new HashMap();
                                JSONObject obj_1 = map_list.getJSONObject(i);
                                Map map1 = new HashMap();
                                map1.put("id", obj_1.getString("id"));
                                map1.put("goods_price", obj_1.get("goods_price"));
                                map1.put("goods_picture", obj_1.get("goods_picture"));
                                map1.put("goods_name", obj_1.get("goods_name"));
                                map1.put("goods_salenum", obj_1.get("goods_salenum"));
                                line_map.put("goods1", map1);

                                if (i - 1 >= 0) {
                                    JSONObject obj_2 = map_list.getJSONObject(i - 1);
                                    Map map2 = new HashMap();
                                    map2.put("id", obj_2.getInt("id"));
                                    map2.put("goods_price", obj_2.get("goods_price"));
                                    map2.put("goods_picture", obj_2.get("goods_picture"));
                                    map2.put("goods_name", obj_2.get("goods_name"));
                                    map2.put("goods_salenum", obj_2.get("goods_salenum"));
                                    line_map.put("goods2", map2);
                                }

                                list.add(line_map);
                            }
                            newCount = list.size();
                            if (lenght == 0) {
                                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                mRecyclerView.setVisibility(View.GONE);

                            } else {
                                rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                                mRecyclerView.setVisibility(View.VISIBLE);
                            }
                            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
                            if (state== LoadingFooter.State.Loading){
                                if ((currentCount+10)==newCount){
                                    RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.Normal);
                                }else {
                                    RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.TheEnd);
                                }
                            }
                            mRecyclerView.refreshComplete();
                            mLRecyclerViewAdapter.notifyDataSetChanged();
                        }else {
                            LoadingFooter.State state = RecyclerViewStateUtils.getFooterViewState(mRecyclerView);
                            if (state== LoadingFooter.State.Loading){
                                RecyclerViewStateUtils.setFooterViewState(mRecyclerView,LoadingFooter.State.TheEnd);
                            }
                            mRecyclerView.refreshComplete();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        mActivity.hideProcessDialog(0);
                    }
                }, error -> {
                    Log.i("test", error.toString());
                    mActivity.hideProcessDialog(0);
                }, paramap);
        mRequestQueue.add(request);
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
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
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


}
