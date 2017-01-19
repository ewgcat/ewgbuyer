package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
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
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 评价管理
 */
public class OrderEvaluateTabFragment extends Fragment {


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
        try {

            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");

            childFragmentManager.setAccessible(true);

            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {

            throw new RuntimeException(e);

        } catch (IllegalAccessException e) {

            throw new RuntimeException(e);

        }
        this.rootView = null;
        this.orderAssertPagerAdapter = null;
        this.fragment_list = null;
        this.viewPager = null;
        this.title_list = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("订单评价");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        TabLayout tablayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tablayout.setTabMode(TabLayout.MODE_FIXED);
        title_list = new ArrayList<String>();
        title_list.add(getResources().getString(R.string.waiting_evaluate));
        title_list.add(getResources().getString(R.string.evaluate_finished));

        fragment_list = new ArrayList();
        fragment_list.add(OrderListFragment.getInstance("40", 0));
        fragment_list.add(OrderEvaluateCompleteFragment.getInstance());
        orderAssertPagerAdapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
        viewPager.setAdapter(orderAssertPagerAdapter);
        tablayout.setupWithViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle.getInt("current") == 0) {
            title_list.clear();
            title_list.add(getResources().getString(R.string.waiting_evaluate));
            title_list.add(getResources().getString(R.string.finished));
            fragment_list.clear();
            fragment_list.add(OrderListFragment.getInstance("40", 0));
            fragment_list.add((OrderEvaluateCompleteFragment.getInstance()));
            orderAssertPagerAdapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
            viewPager.setAdapter(orderAssertPagerAdapter);
        } else if (bundle.getInt("current") == 1) {
            fragment_list.get(bundle.getInt("current")).onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
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
}
