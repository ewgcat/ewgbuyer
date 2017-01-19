package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 购物卡充值
 */
public class BalanceRechargeCardFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_acount_bank)
    EditText etAcountBank;

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
        rootView = inflater.inflate(R.layout.fragment_balance_rechange_card, container, false);
        ButterKnife.bind(this, rootView);

        toolbar.setTitle("充值");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        return rootView;
    }

    //购物卡充值到余额
    public void cardRechargeToBalance() {
        String bankAccount = etAcountBank.getText().toString().trim();
        if (TextUtils.isEmpty(bankAccount)) {
            Toast.makeText(mActivity, "请输入卡号", Toast.LENGTH_SHORT).show();
            return;
        }
        Map paramap = mActivity.getParaMap();
        paramap.put("card_sn", bankAccount);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/recharge_card_save.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String status = jsonObject.getString("status");
                    if ("1".equals(status)) {
                        Toast.makeText(getActivity(), "充值成功", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        getFragmentManager().popBackStack();
                        if (getTargetFragment() != null) {
                            getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
                        }
                    }
                    if ("0".equals(status)) {
                        Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();
                    }
                    String userbalance = jsonObject.getString("user_balance");
                    mActivity.setCache("userbalance", userbalance);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点击事件监听
    @OnClick(R.id.bt_recharge)
    public void onClick(View view) {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            mActivity.hide_keyboard(view);
            cardRechargeToBalance();
        }
    }



    //菜单图文混合
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);
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
