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
import android.widget.Button;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 积分签到
 */
public class IntegralSignFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private Button b_commit_number;

    @Override
    public void onDetach() {
        super.onDetach();
        
        this.rootView = null;
        this.b_commit_number = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_integral_sign, container, false);
        b_commit_number = (Button) rootView.findViewById(R.id.b_commit_number);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("签到");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        //设置导航点击事件
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);

        getSignStatus();
        b_commit_number.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Map paraMap = mActivity.getParaMap();
                Map map = new HashMap();
                map.put("userId", paraMap.get("user_id"));
                Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/sign_integral.htm",
                        result -> {
                            try {
                                String code = result.getString("code");
                                if ("200".equals(code)) {
                                    b_commit_number.setEnabled(false);
                                    b_commit_number.setClickable(false);
                                    b_commit_number.setText("已签到");
                                    b_commit_number.setTextColor(getResources().getColor(R.color.signintegral));
                                    b_commit_number.setBackgroundResource(R.drawable.button_unuse);
                                } else if ("500".equals(code)) {
                                }
                            } catch (Exception e) {
                            }
                            mActivity.hideProcessDialog(0);
                        }, error -> mActivity.hideProcessDialog(1), map);
                mRequestQueue.add(request);
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

    /**
     * 签到状态
     */
    private void getSignStatus() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        Map map = new HashMap();
        map.put("userId", paraMap.get("user_id"));
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/get_sign_status.htm",
                result -> {
                    try {
                        String code = result.getString("code");
                        if ("200".equals(code)) {
                            b_commit_number.setText(getResources().getString(R.string.sign_right_now));
                            b_commit_number.setEnabled(true);
                            b_commit_number.setClickable(true);
                            b_commit_number.setTextColor(getResources().getColor(R.color.sign_integral_right_now));
                            b_commit_number.setBackgroundResource(R.drawable.coupon_button_normal);
                        } else if ("500".equals(code)) {
                            b_commit_number.setText("已签到");
                            b_commit_number.setEnabled(false);
                            b_commit_number.setClickable(false);
                            b_commit_number.setTextColor(getResources().getColor(R.color.signintegral));
                            b_commit_number.setBackgroundResource(R.drawable.button_unuse);
                        }
                    } catch (Exception e) {
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), map);
        mRequestQueue.add(request);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getSignStatus();
        }
    }


}
