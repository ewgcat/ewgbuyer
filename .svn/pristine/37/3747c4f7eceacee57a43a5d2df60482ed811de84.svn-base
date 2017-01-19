package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Build;
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
import com.ewgvip.buyer.android.adapter.BasePagerAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 我的关注页面
 */
public class ConcernTabFragment extends Fragment {


    private BaseActivity mActivity;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_tablist, container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle("我的关注");//设置标题
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用
        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        final ViewPager vp_concern = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        List<String> title_list = new ArrayList();
        title_list.add("商品");
        title_list.add("店铺");
        List<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new ConcernGoodsFragment());
        fragmentList.add(new ConcernStoresFragment());
        Bundle bundle = getArguments();
        int position = bundle.getInt("position");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            vp_concern.setAdapter(new BasePagerAdapter(getChildFragmentManager(), fragmentList, title_list));
        } else {
            vp_concern.setAdapter(new BasePagerAdapter(getFragmentManager(), fragmentList, title_list));
        }

        //解决tablayout的延迟执行的bug
        tabLayout.post(() -> {
            if (vp_concern.getAdapter()!=null&&vp_concern.getAdapter().getCount()>0){
                tabLayout.setupWithViewPager(vp_concern);
            }
        });
        vp_concern.setCurrentItem(position);
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

}
