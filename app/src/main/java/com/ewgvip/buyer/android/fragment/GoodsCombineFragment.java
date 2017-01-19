package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.CombineAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 组合配件选择页面
 */
public class GoodsCombineFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private Bundle bundle;
    private String goods_id;
    private CombineAdapter combineAdapter;
    private PullToRefreshListView combine_listview;
    private PullToRefreshListView combine_listview_part;
    private Map<String, String> map;
    private RequestQueue mRequestQueue;
    private int checkedbax_visible = 0;
    private String goods_img, goods_name,goods_count;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.bundle = null;
        this.goods_id = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_combine, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("组合套装");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用



        bundle = getArguments();
        goods_id = bundle.getString("goods_id");
        goods_img = bundle.getString("goods_img");
        goods_name = bundle.getString("goods_name");
        goods_count = bundle.getString("count");
        map = mActivity.getParaMap();
        map.put("id", goods_id);
        mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();

        combine_listview = (PullToRefreshListView) rootView.findViewById(R.id.combine_listview);
        combine_listview.setMode(PullToRefreshBase.Mode.DISABLED);
        combine_listview_part = (PullToRefreshListView) rootView.findViewById(R.id.combine_listview_part);
        combine_listview_part.setMode(PullToRefreshBase.Mode.DISABLED);

        //组合套装
        rootView.findViewById(R.id.combine_suit).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        TextView tv = (TextView) rootView.findViewById(R.id.combine_suit);
                        tv.setTextColor(getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.rounded_red_left);
                        tv = (TextView) rootView.findViewById(R.id.combine_part);
                        tv.setTextColor(getResources().getColor(R.color.red));
                        tv.setBackgroundResource(R.drawable.rounded_white_right);
                        combine_listview.setVisibility(View.VISIBLE);
                        combine_listview_part.setVisibility(View.GONE);
                        checkedbax_visible = 0;
                    }
                });

        //组合配件按钮  请求
        rootView.findViewById(R.id.combine_part).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        TextView tv = (TextView) rootView.findViewById(R.id.combine_suit);
                        tv.setTextColor(getResources().getColor(R.color.red));
                        tv.setBackgroundResource(R.drawable.rounded_white_left);
                        tv = (TextView) rootView.findViewById(R.id.combine_part);
                        tv.setTextColor(getResources().getColor(R.color.white));
                        tv.setBackgroundResource(R.drawable.rounded_red_right);
                        checkedbax_visible = 1;
                        combine_listview.setVisibility(View.GONE);
                        combine_listview_part.setVisibility(View.VISIBLE);
                        Request<JSONObject> request2 = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/get_goods_parts.htm",
                                result -> {
                                    try {
                                        List combinepart_list = new ArrayList();
                                        JSONArray plan_list = result.getJSONArray("plan_list");
                                        for (int i = 0; i < plan_list.length(); i++) {
                                            JSONObject plan = plan_list.getJSONObject(i);
                                            Map<String, Object> plan_map = new HashMap<String, Object>();
                                            plan_map.put("plan_price", plan.get("plan_price"));
                                            plan_map.put("all_price", plan.get("all_price"));
                                            plan_map.put("status", 0);

                                            JSONArray goods_arr = plan.getJSONArray("goods_list");
                                            List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
                                            for (int j = 0; j < goods_arr.length(); j++) {
                                                JSONObject suit_goods = goods_arr.getJSONObject(j);
                                                Map<String, Object> goods = new HashMap<String, Object>();
                                                goods.put("goods_name", suit_goods.get("goods_name"));
                                                goods.put("goods_img", suit_goods.get("goods_img"));
                                                goods.put("goods_id", suit_goods.get("goods_id"));
                                                goods_list.add(goods);
                                            }
                                            plan_map.put("goods_img", goods_img);
                                            plan_map.put("goods_id", goods_id);
                                            plan_map.put("goods_name", goods_name);
                                            plan_map.put("goods_count", goods_count);
                                            plan_map.put("goods_list", goods_list);
                                            combinepart_list.add(plan_map);
                                        }

                                        combineAdapter = new CombineAdapter(mActivity, combinepart_list, checkedbax_visible);
                                        combine_listview_part.setAdapter(combineAdapter);

                                        if (combinepart_list.size() == 0) {
                                            rootView.findViewById(R.id.nodata_combin).setVisibility(View.VISIBLE);
                                            combine_listview_part.setVisibility(View.GONE);
                                        } else {
                                            rootView.findViewById(R.id.nodata_combin).setVisibility(View.GONE);
                                            combine_listview_part.setVisibility(View.VISIBLE);
                                      }

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    mActivity.hideProcessDialog(0);
                                }, error -> mActivity.hideProcessDialog(1), map);
                        mRequestQueue.add(request2);
                    }
                });


        //组合套装请求
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/get_goods_suits.htm",
                result -> {
                    try {
                        Log.i("test",result.toString());
                        List combine_list = new ArrayList();
                        JSONArray plan_list = result.getJSONArray("plan_list");
                        for (int i = 0; i < plan_list.length(); i++) {
                            JSONObject plan = plan_list.getJSONObject(i);
                            Map<String, Object> plan_map = new HashMap<String, Object>();
                            plan_map.put("plan_price", plan.get("plan_price"));
                            plan_map.put("all_price", plan.get("all_price"));
                            plan_map.put("status", 0);

                            JSONArray goods_arr = plan.getJSONArray("goods_list");
                            List<Map<String, Object>> goods_list = new ArrayList<Map<String, Object>>();
                            for (int j = 0; j < goods_arr.length(); j++) {
                                JSONObject suit_goods = goods_arr.getJSONObject(j);
                                Map<String, Object> goods = new HashMap<String, Object>();
                                goods.put("goods_name", suit_goods.get("goods_name"));
                                goods.put("goods_img", suit_goods.get("goods_img"));
                                goods.put("goods_id", suit_goods.get("goods_id"));
                                goods_list.add(goods);
                            }
                            plan_map.put("goods_img", goods_img);
                            plan_map.put("goods_name", goods_name);
                            plan_map.put("goods_id", goods_id);
                            plan_map.put("goods_count", goods_count);
                            plan_map.put("goods_list", goods_list);
                            combine_list.add(plan_map);
                        }
                        combineAdapter = new CombineAdapter(mActivity, combine_list, checkedbax_visible);
                        combine_listview.setAdapter(combineAdapter);

                        if (combine_list.size() == 0) {
                            rootView.findViewById(R.id.nodata_combin).setVisibility(View.VISIBLE);
                            combine_listview.setVisibility(View.GONE);
                        } else {
                            rootView.findViewById(R.id.nodata_combin).setVisibility(View.GONE);
                            combine_listview.setVisibility(View.VISIBLE);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), map);
        mRequestQueue.add(request);


        return rootView;
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu,true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
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
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


//
}
