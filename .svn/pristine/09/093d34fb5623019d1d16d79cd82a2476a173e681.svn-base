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
import com.ewgvip.buyer.android.dialog.ShareDialog;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 商品详情tab管理页
 */
public class GoodsContainerFragment extends Fragment {

    private BaseActivity mActivity;
    private ViewPager viewPager;
    private Bundle bundle;
    private GoodsFragment goodsFragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_goods_container, container,
                false);
        mActivity = (BaseActivity) getActivity();

        bundle = getArguments();
        final String goods_id = bundle.getString("goods_id");


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle(null);
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        final TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.goods_tablayout);
        tabLayout.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) rootView.findViewById(R.id.goods_viewpager);




        List<String> title_list = new ArrayList<String>();
        title_list.add(getResources().getString(R.string.goods));
        title_list.add(getResources().getString(R.string.detail));
        title_list.add(getResources().getString(R.string.evaluate));

        List<Fragment> fragment_list = new ArrayList();
        goodsFragment = GoodsFragment.getInstance(goods_id, GoodsContainerFragment.this);

        fragment_list.add(goodsFragment);
        fragment_list.add(GoodsDetailFragment.getInstance(goods_id));
        fragment_list.add(GoodsEvaluateFragment.getInstance(goods_id));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            viewPager.setAdapter(new BasePagerAdapter(getChildFragmentManager(), fragment_list, title_list));
        }
        //解决tablayout的延迟执行的bug
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                if (viewPager.getAdapter()!=null&&viewPager.getAdapter().getCount()>0){
                    tabLayout.setupWithViewPager(viewPager);
                }
            }
        });



        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        viewPager.setOffscreenPageLimit(3);

        return rootView;
    }

    public void go_evaluate() {
        viewPager.setCurrentItem(2, true);
    }

    public void go_index() {
        viewPager.setCurrentItem(0, true);
    }

    public int get_index() {
        return viewPager.getCurrentItem();
    }

    //绘制菜单内容
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_goods, menu);
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
        }else if (id == R.id.action_share) {
            ShareDialog.show_share_dialog(mActivity,goodsFragment,null, null);
            return true;
        } else if (id == R.id.action_cart) {
            mActivity.go_cart();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
