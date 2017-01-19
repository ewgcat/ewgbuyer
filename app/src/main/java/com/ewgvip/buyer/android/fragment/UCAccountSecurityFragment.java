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
import android.widget.RelativeLayout;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Method;

/**
 * 账户安全管理,包括各种密码的修改
 */
public class UCAccountSecurityFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private RelativeLayout loginPassword;
    private RelativeLayout paypassword;
    private RelativeLayout phone_verification;


    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.loginPassword = null;
        this.paypassword = null;
        this.phone_verification = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_account_security, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("账户安全");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        loginPassword = (RelativeLayout) rootView.findViewById(R.id.loginPassword);
        paypassword = (RelativeLayout) rootView.findViewById(R.id.paypassword);
        phone_verification = (RelativeLayout) rootView.findViewById(R.id.phone_verification);

        //跳转修改登录密码
        loginPassword.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_change_password();
            }
        });
        //跳转修改支付密码
        paypassword.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_change_pay_password();
            }
        });
        //跳转到绑定手机
        phone_verification.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_bound_phone();
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

}
