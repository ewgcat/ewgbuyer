package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONObject;

import java.lang.reflect.Method;


import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.BindView;

import static com.ewgvip.buyer.android.R.id.tv_fixed_balance_amount;
import static com.ewgvip.buyer.android.R.layout.fragment_balance;


/**
 * 余额管理首页
 */
public class BalanceFragment extends Fragment {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_balance_amount)
    TextView tvBalanceAmount;
    @BindView(tv_fixed_balance_amount)
    TextView tvFixedBalanceAmount;
    private View rootView;
    private BaseActivity mActivity;


    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.tvFixedBalanceAmount = null;
        this.tvBalanceAmount = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(fragment_balance, container, false);
        ButterKnife.bind(this, rootView);
        toolbar.setTitle("账户余额");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单不可用
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    //初始化余额信息
    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/index.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String balance = jsonObject.getString("balance");
                    String freezeBlance = jsonObject.getString("freezeBlance");
                    tvBalanceAmount.setText(mActivity.moneytodouble(balance));
                    tvFixedBalanceAmount.setText(mActivity.moneytodouble(freezeBlance));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        init();
    }

    //菜单图文混合
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main_billing, menu);
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
        if (id == R.id.iv_billingDetail) {
            Bundle bundle = new Bundle();
            mActivity.go_balance_billingdetails(bundle);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @OnClick({R.id.tv_getCash, R.id.tv_my_bank_card
            , R.id.tv_recharge_card, R.id.tv_recharge})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_getCash://提现
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    Bundle bundle = new Bundle();
                    mActivity.go_balance_getCash(bundle, BalanceFragment.this);
                }
                break;
            case R.id.tv_my_bank_card://我的银行卡
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_to_my_bank_card();
                }
                break;
            case R.id.tv_recharge_card:   //充值卡充值
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_balance_recharge(BalanceFragment.this);
                }
                break;
            case R.id.tv_recharge://现金充值
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_rechargecenter();
                }
                break;
        }
    }
}
