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
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * 商城平台扫二维码登录功能
 */

public class QRLoginFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Bundle bundle;

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.bundle = null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_qr_login, container,
                false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("扫码登录");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);


        //设置菜单可用
        bundle = getArguments();
        final String qr_id = bundle.getString("qr_id");
        rootView.findViewById(R.id.yes).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                        Map paraMap = mActivity.getParaMap();
                        paraMap.put("qr_id", qr_id);
                        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/buyer/app_qr_login.htm",
                                result -> {
                                    try {
                                        if (result.getString("ret").equals("100")) {
                                            mActivity.go_index();
                                        }
                                    } catch (Exception e) {
                                    }
                                    mActivity.hideProcessDialog(0);
                                }, error -> mActivity.hideProcessDialog(1), paraMap);
                        mRequestQueue.add(request);
                    }

                });

        rootView.findViewById(R.id.cancle).setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_index();
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