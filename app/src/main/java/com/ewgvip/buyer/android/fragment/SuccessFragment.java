package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.lang.reflect.Method;

/**
 * 成功页面,处理各种成功之后的跳转
 */
public class SuccessFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    public static boolean flag = false;
    private String type;
    private String id;



    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_success, container, false);
        mActivity.hideProcessDialog(0);
        OrderAllTabFragment.REFRESH = true;
        OrderAllTabFragment.SUCCESS = true;
        flag = true;
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("下单成功");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_index();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        Bundle bundle = getArguments();
        type = bundle.getString("type");
        id = bundle.getString("order_id");

        if (!TextUtils.isEmpty(type)) {
            if (type.equals("free")) {
                TextView textView = (TextView) rootView.findViewById(R.id.pay_success);
                textView.setText("申请成功");

            } else {
                TextView textView = (TextView) rootView.findViewById(R.id.order_info);
                textView.setText("您的订单号" + bundle.getString("order_num"));
            }

            if (bundle.containsKey("pay_type") && bundle.getString("pay_type").equals("payafter")) {
                TextView textView = (TextView) rootView.findViewById(R.id.pay_success);
                textView.setText("货到付款提交成功，等待发货！");
            }

            Button button = (Button) rootView.findViewById(R.id.success);
            button.setOnClickListener(v -> {
                if (FastDoubleClickTools.isFastDoubleClick()){
                    v.setClickable(false);
                    Bundle order_bundle = new Bundle();
                    order_bundle.putString("oid", id);
                    if ("goods".equals(type)) {
                        mActivity.go_order_normal(id, mActivity.getCurrentfragment(), 0);
                    } else if ("life".equals(type)) {
                        mActivity.go_order_group_list();
                        mActivity.go_order_life(id);
                    } else if ("integral".equals(type)) {
                        mActivity.go_order_integral_list();
                        mActivity.go_order_integral_detail(order_bundle);
                    } else if ("free".equals(type)) {
                        mActivity.go_order_free_list();
                        mActivity.go_order_free_detail(order_bundle);
                    } else if ("payonline".equals(type)) {
                        mActivity.go_order_normal(id, mActivity.getCurrentfragment(), 0);
                    }
                }

            });

        }


        return rootView;
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
