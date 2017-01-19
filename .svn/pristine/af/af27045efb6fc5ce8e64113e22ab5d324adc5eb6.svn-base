package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
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

import java.lang.reflect.Method;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包,各种资产的入口
 */
public class UCWalletFragment extends Fragment {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private BaseActivity mActivity;
    private View rootView;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.mActivity = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_wallet, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle("我的钱包");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
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

        return rootView;
    }


    @OnClick({R.id.balance, R.id.integral, R.id.coupons, R.id.free, R.id.group_life})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.balance:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_balance_detail();
                }
                break;
            case R.id.integral:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_integral_detail();
                }
                break;
            case R.id.coupons:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_coupons();
                }
                break;
            case R.id.free:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order_free_list();
                }
                break;
            case R.id.group_life:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_order_group_list();
                }
                break;
        }
    }

    //菜单选点点击事件
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    //菜单选点点击事件
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
