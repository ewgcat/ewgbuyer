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

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.FragmentTabPagerAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单退款列表tab
 */
public class OrderReturnTabFragment extends Fragment {

    private View rootView;
    private BaseActivity mActivity;

    private  TabLayout tabLayout;
    private  ViewPager viewPager;

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

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle("订单退货");//设置标题
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//设置导航点击事件
            @Override
            public void onClick(View v) {
                if (FastDoubleClickTools.isFastDoubleClick()) {

                    mActivity.onBackPressed();
                }
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        tabLayout= (TabLayout) rootView.findViewById(R.id.tablayout);
        viewPager= (ViewPager) rootView.findViewById(R.id.viewpager);

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        List<String> title_list = new ArrayList<String>();
        title_list.add("商品退货");
        title_list.add("团购退款");

        List fragment_list = new ArrayList();
        fragment_list.add(new OrderGoodsReturnListFragment());
        fragment_list.add(new OrderGroupLifeReturnListFragment());

        viewPager.setAdapter(new FragmentTabPagerAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), fragment_list, title_list));
        //解决tablayout的延迟执行的bug
        tabLayout.post(() -> {
            if (viewPager.getAdapter()!=null&&viewPager.getAdapter().getCount()>0){
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    private void  init(){
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        List<String> title_list = new ArrayList<String>();
        title_list.add("商品退货");
        title_list.add("团购退款");
        List fragment_list = new ArrayList();
        fragment_list.add(new OrderGoodsReturnListFragment());
        fragment_list.add(new OrderGroupLifeReturnListFragment());

        viewPager.setAdapter(new FragmentTabPagerAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), fragment_list, title_list));
        tabLayout.setupWithViewPager(viewPager);
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



}
