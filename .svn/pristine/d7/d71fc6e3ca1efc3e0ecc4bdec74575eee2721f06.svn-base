package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 零元试用
 */
public class FreeTabFragment extends Fragment {
    private TabLayout tablayout;
    private View rootView;
    private BaseActivity mActivity;
    private FragmentTabPagerAdapter orderAssertPagerAdapter;
    private List<android.support.v4.app.Fragment> fragment_list;
    private ViewPager viewPager;
    private List<String> title_list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;

        this.orderAssertPagerAdapter = null;
        this.fragment_list = null;
        this.viewPager = null;
        this.title_list = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("0元试用");
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
        setHasOptionsMenu(false);


        tablayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        title_list = new ArrayList<String>();
        fragment_list = new ArrayList();
        mActivity.showProcessDialog();
        getList();
        return rootView;
    }

    private void getList() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/freeclass.htm",
                result -> {
                    try {
                        JSONArray arr = result.getJSONArray("freeclass_list");
                        title_list.add("全部");
                        fragment_list.add(FreeListFragment.getInstance(""));
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject obj = arr.getJSONObject(i);
                            title_list.add(obj.get("class_name") + "");
                            fragment_list.add(FreeListFragment.getInstance(obj.get("class_id") + ""));
                        }
                        orderAssertPagerAdapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
                        viewPager.setAdapter(orderAssertPagerAdapter);
                        tablayout.setupWithViewPager(viewPager);
                        viewPager.setOffscreenPageLimit(arr.length());
                        mActivity.hideProcessDialog(1);
                    } catch (Exception e) {
                    }
                }, error -> mActivity.hideProcessDialog(1), null);
        mRequestQueue.add(request);
    }
}
