package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.DataClean;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.tv_cachesize;
import static com.ewgvip.buyer.android.R.id.tv_current_version;

/**
 * 用户设置
 */
public class SettingFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(tv_cachesize)
    TextView tvCachesize;
    @BindView(tv_current_version)
    TextView tvCurrentVersion;
    @BindView(R.id.exit)
    Button exit;

    View rootView;
    private BaseActivity mActivity;
    private static SettingFragment fragment = null;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.mActivity = null;
        this.fragment = null;
    }

    public SettingFragment() {
    }

    //静态工厂方法
    public static SettingFragment getInstance() {
        if (fragment == null) {
            fragment = new SettingFragment();
        }
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle("设置");
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单图标不显示


        tvCurrentVersion.setText(CommonUtil.getAppVersionName(mActivity));
        tvCachesize.setText(DataClean.getTotalCacheSize(mActivity));
        if (mActivity.islogin()) {
            exit.setVisibility(View.VISIBLE);
        } else {
            exit.setVisibility(View.GONE);
        }

        return rootView;
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            tvCachesize.setText(DataClean.getTotalCacheSize(mActivity));
        }
    }


    @OnClick({R.id.help, R.id.copyright, tv_cachesize, R.id.cleanCache, R.id.about, R.id.exit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.help:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", WebFragment.HELP);
                    mActivity.go_web(bundle);
                }
                break;
            case R.id.copyright:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("type", WebFragment.COPYRIGHT);
                    mActivity.go_web(bundle);
                }
                break;
            case R.id.cleanCache:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    DataClean.clearAllCache(mActivity);
                    tvCachesize.setText(DataClean.getTotalCacheSize(mActivity));
                    Toast.makeText(mActivity, "缓存清理完毕", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exit:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    // 清空登录信息,历史记录，解绑推送
                    SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                    Map paraMap = new HashMap();
                    String user_id = preferences.getString("user_id", "");
                    if (user_id != null && !user_id.equals("")) {
                        paraMap.put("user_id", user_id);
                    }
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.remove("user_id");
                    editor.remove("token");
                    if (preferences.contains("inventory_ids")) {
                        editor.remove("inventory_ids");
                    }
                    editor.commit();
                    IndexNavigatiorFragment.setbadge(0);
                    UserCenterFragment.setbadge(0);
                    mActivity.onBackPressed();
                    RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/push_unbind.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
                        @Override
                        public void onNext(JSONObject jsonObject) {
                            DataClean.clearAllCache(mActivity);
                        }
                    });
                }
                break;

            case R.id.about:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_about();
                }
                break;
        }
    }

    //菜单图文混合
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

    //菜单图文混合
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

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


