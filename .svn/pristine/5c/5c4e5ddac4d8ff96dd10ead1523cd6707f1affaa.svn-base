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

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ComplainTabFragment extends Fragment {


    private List title_list;//TabHost数据
    private BaseActivity mActivity;
    private View rootView;
    //投诉切换数据表
    private TabLayout tabLayout;
    private ViewPager vp_concern;
    private List fragment_list;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.title_list = null;
        this.tabLayout = null;
        this.vp_concern = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.user_center_my_complain));//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        vp_concern = (ViewPager) rootView.findViewById(R.id.viewpager);
        title_list = new ArrayList();
        fragment_list = new ArrayList();
        title_list.add(getResources().getString(R.string.goods_list));
        title_list.add(getResources().getString(R.string.my_complain));
        fragment_list.add(new ComplainListFragment());
        fragment_list.add(new ComplainStatusFragment());
        FragmentTabPagerAdapter adapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
        vp_concern.setAdapter(adapter);
        tabLayout.post(() -> {
            if (vp_concern.getAdapter()!=null&&vp_concern.getAdapter().getCount()>0){
                tabLayout.setupWithViewPager(vp_concern);
            }
        });
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        title_list.clear();
        title_list.add(getResources().getString(R.string.waiting_evaluate));
        title_list.add(getResources().getString(R.string.finished));
        fragment_list.clear();
        fragment_list.add(new ComplainListFragment());
        fragment_list.add(new ComplainStatusFragment());
        FragmentTabPagerAdapter adapter = new FragmentTabPagerAdapter(mActivity.getSupportFragmentManager(), fragment_list, title_list);
        vp_concern.setAdapter(adapter);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
