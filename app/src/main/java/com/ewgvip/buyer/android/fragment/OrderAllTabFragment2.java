package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.adapter.OrderListAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表tab
 *

 */
public class OrderAllTabFragment2 extends Fragment {

    public static boolean REFRESH = true;
    public static int POSITION = 0;
    public static boolean flag = true;
    public static boolean SUCCESS = false;
    private static OrderAllTabFragment2 fragment;
    List<String> title_list;
    private View rootView;
    private BaseActivity mActivity;
    private TabLayout tabLayout2;
    private ViewPager viewPager2;


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
    }


    //静态工厂方法
    public static OrderAllTabFragment2 getInstance() {
        if (fragment == null) {
            fragment = new OrderAllTabFragment2();
        }
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tablist2, container, false);
        mActivity = (BaseActivity) getActivity();

        tabLayout2 = (TabLayout) rootView.findViewById(R.id.tablayout2);
        viewPager2 = (ViewPager) rootView.findViewById(R.id.viewpager2);

        return rootView;
    }



    @Override
    public void onResume() {
        super.onResume();
        if (REFRESH) {
            flag = !SUCCESS;
            init();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        REFRESH = false;
    }

    public void init() {
        title_list = new ArrayList<String>();
        title_list.add(getResources().getString(R.string.all));
        title_list.add(getResources().getString(R.string.waitingpay));
        title_list.add(getResources().getString(R.string.waitingship));
        title_list.add(getResources().getString(R.string.waitingreceive));
        title_list.add(getResources().getString(R.string.finished));
        List fragment_list = new ArrayList();
        fragment_list.add(OrderListFragment.getInstance("", 0));
        fragment_list.add(OrderListFragment.getInstance("10", 1));
        fragment_list.add(OrderListFragment.getInstance("20", 2));
        fragment_list.add(OrderListFragment.getInstance("30", 3));
        fragment_list.add(OrderListFragment.getInstance("42", 4));
        tabLayout2.setTabMode(TabLayout.MODE_FIXED);
        tabLayout2.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager2.setOffscreenPageLimit(5);
        viewPager2.setAdapter(new FragmentTabPagerAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), fragment_list, title_list));
        tabLayout2.setupWithViewPager(viewPager2);
        for (int i = 0; i < tabLayout2.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout2.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        viewPager2.setCurrentItem(OrderListAdapter.CURRENT);
    }

    public View getTabView(int position) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.order_all_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_order_all_tab);
        tv.setText(title_list.get(position));
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        OrderListAdapter.CURRENT = 0;
        POSITION = 0;
        flag = true;
        REFRESH = true;
        SUCCESS = false;
    }

}
