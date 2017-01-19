package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
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
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.adapter.OrderListAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单列表tab
 *
 * @author lgx
 *         A simple {@link Fragment} subclass.
 */
public class OrderAllTabFragment extends Fragment {
    public static boolean REFRESH = true;
    public static int POSITION = 0;
    public static boolean flag = true;
    public static boolean SUCCESS = false;
    List<String> title_list;
    private View rootView;
    private BaseActivity mActivity;
    private ViewPager viewPager;
    private TabLayout tabLayout;

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

    public OrderAllTabFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("我的订单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);

        if (getArguments()==null){
            OrderListAdapter.CURRENT =0;
        }else {
            OrderListAdapter.CURRENT = getArguments().getInt("index", 0);
        }

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        OrderListAdapter.CURRENT = data.getIntExtra("current", 0);
        flag = false;
        init();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.setOffscreenPageLimit(5);
        viewPager.setAdapter(new FragmentTabPagerAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), fragment_list, title_list));

        //解决tablayout的延迟执行的bug
        tabLayout.post(() -> {
            if (viewPager.getAdapter()!=null&&viewPager.getAdapter().getCount()>0){
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        viewPager.setCurrentItem(OrderListAdapter.CURRENT);
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
