package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
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
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ViewPagerFragmentAllRefresh;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 咨询页面
 */
public class ConsultTabFragment extends Fragment {


    private List<String> title_list = new ArrayList<String>();//TabHost数据
    private List<android.support.v4.app.Fragment> fragment_list;
    private ViewPagerFragmentAllRefresh viewPagerFragmentAllRefresh;
    private BaseActivity mActivity;
    private View rootView;
    private ConsultResultFragment fragment1;
    private ConsultResultFragment fragment2;
    //店铺首页数据
    private TabLayout tabLayout;
    private ViewPager vp_concern;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.title_list = null;
        this.tabLayout = null;
        this.vp_concern = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.user_center_my_consult));//设置标题
        final BaseActivity mActivity = (BaseActivity) getActivity();
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
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        title_list.add(getResources().getString(R.string.unresultconsult));
        title_list.add(getResources().getString(R.string.resultconsult));
        fragment_list = new ArrayList();
        ViewPagerFragmentAllRefresh viewPagerFragmentAllRefresh = i -> {
            if(i == 1){
                fragment1.refreshData();
            }else if(i == 0){
                fragment2.refreshData();
            }
        };
        fragment1 = new ConsultResultFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putInt("flag", 0);
        fragment1.setArguments(bundle1);
        fragment1.setViewPagerFragmentAllRefresh(viewPagerFragmentAllRefresh);
        fragment2 = new ConsultResultFragment();
        fragment_list.add(fragment1);
        Bundle bundle2 = new Bundle();
        bundle2.putInt("flag", 1);
        fragment2.setArguments(bundle2);
        fragment2.setViewPagerFragmentAllRefresh(viewPagerFragmentAllRefresh);
        fragment_list.add(fragment2);
        vp_concern.setAdapter(new ConsultPagerAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), title_list, fragment_list));


        //解决tablayout的延迟执行的bug
        tabLayout.post(() -> {
            if (vp_concern.getAdapter()!=null&&vp_concern.getAdapter().getCount()>0){
                tabLayout.setupWithViewPager(vp_concern);
            }
        });
        vp_concern.setCurrentItem(0);
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


    private class ConsultPagerAdapter extends FragmentStatePagerAdapter {

        private List<String> title_list;
        private List<android.support.v4.app.Fragment> fragment_list;

        public ConsultPagerAdapter(FragmentManager fm, List<String> title_list, List<android.support.v4.app.Fragment> fragment_list) {
            super(fm);
            this.title_list = title_list;
            this.fragment_list = fragment_list;
        }

        public android.support.v4.app.Fragment getItem(int position) {
            android.support.v4.app.Fragment fragment = fragment_list.get(position);
//            Bundle bundle = new Bundle();
//            if (position == 0) {
//                bundle.putInt("flag", 0);
//            }
//            if (position == 1) {
//                bundle.putInt("flag", 1);
//            }
//            fragment = new ConsultResultFragment();
//            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return title_list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return title_list.get(position);
        }
    }
}
