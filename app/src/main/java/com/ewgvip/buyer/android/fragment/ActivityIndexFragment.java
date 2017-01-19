package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.ActivityIndexListAdapter;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 促销活动列表首页
 */
public class ActivityIndexFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private ActivityIndexListAdapter salesPromotionAdapter;
    private List list_salesPromotion = new ArrayList();
    private PullToRefreshListView lv_list_salespromotion;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.salesPromotionAdapter = null;
        this.list_salesPromotion = null;
        this.lv_list_salespromotion = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("促销活动");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        //设置菜单不可用
        setHasOptionsMenu(false);
        mActivity.showProcessDialog();

        lv_list_salespromotion = (PullToRefreshListView) rootView.findViewById(R.id.listview);
        lv_list_salespromotion.setMode(PullToRefreshBase.Mode.DISABLED);
        salesPromotionAdapter = new ActivityIndexListAdapter(mActivity, list_salesPromotion);
        lv_list_salespromotion.setAdapter(salesPromotionAdapter);


        /**
         * 点击任一项跳转到促销详情页
         */
        lv_list_salespromotion.setOnItemClickListener((adapterView, view, position, l) -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                Map map = (Map) list_salesPromotion.get(position - 1);
                String id = map.get("id").toString();
                Bundle bundle = new Bundle();
                bundle.putString("id", id);
                mActivity.go_salespm(bundle);
            }

        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh_sales();
    }

    /**
     * 刷新促销活动
     */
    public void refresh_sales() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/getAllActivityNav.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    JSONArray map_list = jsonObject.getJSONArray("result");
                    int lenght = map_list.length();
                    for (int i = 0; i < lenght; i++) {
                        JSONObject obj = map_list.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("id", obj.getString("id"));
                        map.put("ac_title", obj.get("ac_title"));
                        map.put("time_desc", obj.get("time_desc"));
                        map.put("picture", obj.get("picture"));
                        map.put("ac_beginTime", obj.get("ac_beginTime"));
                        map.put("ac_endTime", obj.get("ac_endTime"));
                        list_salesPromotion.add(map);
                    }
                    if (salesPromotionAdapter!=null){
                        salesPromotionAdapter.notifyDataSetChanged();
                    }

                    if (list_salesPromotion.size() == 0) {
                        rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                        lv_list_salespromotion.setVisibility(View.GONE);
                        rootView.findViewById(R.id.nodata_refresh).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                refresh_sales();
                            }
                        });
                    } else {
                        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        lv_list_salespromotion.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
}
