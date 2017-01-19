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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.UserIntegrationListViewAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 积分明细列表
 */
public class IntegralDetailListFragment extends Fragment {


    // 页面视图
    @BindView(R.id.listview)
    PullToRefreshListView lv_integration;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.nodata_refresh)
    TextView nodataRefresh;
    @BindView(R.id.nodata)
    LinearLayout nodata;

    private BaseActivity mActivity;
    private View rootView;
    // 积分数据
    private List jsonIntegrationList = new ArrayList();
    // 适配器
    private UserIntegrationListViewAdapter userIntegrationListViewAdapter;
    private int selectCount = 20;
    private int beginCount = 0;


    @Override
    public void onDetach() {
        super.onDetach();
        mActivity = null;
        rootView = null;
        jsonIntegrationList = null;
        userIntegrationListViewAdapter = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_listview_with_toolbar, container, false);
        ButterKnife.bind(this, rootView);
        nodata.setVisibility(View.GONE);
        toolbar.setTitle("积分明细");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        lv_integration.setMode(PullToRefreshBase.Mode.DISABLED);
        lv_integration.setOnLastItemVisibleListener(() -> loadData());

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        mActivity.showProcessDialog();
        Map paraMap = mActivity.getParaMap();
        paraMap.put("beginCount", beginCount);
        paraMap.put("selectCount", selectCount);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/integrallog_list.htm",
                result -> {
                    try {
                        String integral_all = "";
                        if (result.has("integral_all")) {
                            integral_all = result.getString("integral_all");
                        }
                        JSONArray jsonArray = result.getJSONArray("data");
                        if (null != jsonArray) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                                Map map = new HashMap();
                                String type_string = jsonObjectItem.has("type") ? jsonObjectItem.getString("type") : "";
                                String time_string = jsonObjectItem.has("time") ? jsonObjectItem.getString("time") : "";
                                String integral_string = jsonObjectItem.has("integral") ? jsonObjectItem.getString("integral") : "";
                                if ("vip".equals(type_string)) {
                                    type_string="推广会员赠送";
                                }
                                map.put("type", type_string);
                                map.put("time", time_string);
                                map.put("integral", integral_string);
                                jsonIntegrationList.add(map);
                            }
                        }
                        if (userIntegrationListViewAdapter == null) {
                            userIntegrationListViewAdapter = new UserIntegrationListViewAdapter(mActivity, jsonIntegrationList, integral_all);
                            lv_integration.setAdapter(userIntegrationListViewAdapter);
                        } else {
                            userIntegrationListViewAdapter.notifyDataSetChanged();
                        }
                        if (userIntegrationListViewAdapter.getCount() <= 0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            lv_integration.setVisibility(View.GONE);
                        } else {
                            lv_integration.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                        }
                        mActivity.hideProcessDialog(0);
                        beginCount += selectCount;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test", error.toString());
                    mActivity.hideProcessDialog(1);
                    rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                    lv_integration.setVisibility(View.GONE);
                }, paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }


    @OnClick({R.id.nodata_refresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.nodata_refresh:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    loadData();
                }
                break;
            default:
                break;
        }
    }

    //菜单
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    //菜单图文混合
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