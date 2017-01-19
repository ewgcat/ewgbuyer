package com.ewgvip.buyer.android.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券tab切换
 */
public class CouponsUserTabFragment extends android.app.Fragment {
    private View rootView;
    private BaseActivity mActivity;
    private FragmentTabPagerAdapter orderAssertPagerAdapter;
    private List<Fragment> fragment_list;
    private ViewPager viewPager;
    private List<String> title_list;


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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("优惠券");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        TabLayout tablayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        tablayout.setTabMode(TabLayout.MODE_FIXED);

        title_list = new ArrayList<String>();
        title_list.add("未使用");
        title_list.add("已使用");
        title_list.add("已过期");

        fragment_list = new ArrayList();
        fragment_list.add(CouponsUserListFragment.getInstance("0"));
        fragment_list.add(CouponsUserListFragment.getInstance("1"));
        fragment_list.add(CouponsUserListFragment.getInstance("-1"));
        orderAssertPagerAdapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
        viewPager.setAdapter(orderAssertPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);
        return rootView;
    }
}
