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
import android.widget.EditText;
import android.widget.TextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.BindView;

import static com.ewgvip.buyer.android.R.id.bt_recharge;
import static com.ewgvip.buyer.android.R.id.et_please_money;

/**
 * 现金充值余额
 */
public class BalanceRechargeCashFragment extends Fragment {
    @BindView(et_please_money)
    EditText etPleaseMoney;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.balance)
    TextView balance;
    private View rootView;
    private BaseActivity mActivity;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView=null;
        this.mActivity=null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_balance_recharge_cash, container, false);
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

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    //初始化可用余额
    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/index.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    if (jsonObject.get("ret").toString().equals("true")) {
                        balance.setText("￥" + jsonObject.get("balance") + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //充值现金到余额
    private void rechargeCashToBalance(String pd_amount) {
        Map paramap = mActivity.getParaMap();
        paramap.put("pd_amount", pd_amount);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/predeposit_save.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    if (jsonObject.getString("code").equals("100")) {
                        Bundle pay_bundle = new Bundle();
                        pay_bundle.putString("totalPrice", jsonObject.get("order_amount").toString());
                        pay_bundle.putString("order_id", jsonObject.get("order_id").toString());
                        pay_bundle.putString("order_num", jsonObject.get("order_sn").toString());
                        pay_bundle.putString("type", "predeposit");
                        pay_bundle.putString("isRechargeCash", "true");
                        mActivity.go_pay(pay_bundle, "payonline");
                    }
                    if (jsonObject.getString("code").equals("-100")) {
                        Toast.makeText(getActivity(), "充值失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点击事件监听
    @OnClick(bt_recharge)
    public void onClick(View view) {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            mActivity.hide_keyboard(view);
            String pd_amount = etPleaseMoney.getText().toString().trim();
            String reg = "^\\d+(.\\d{1,2})?$";
            Pattern pattern = Pattern.compile(reg);
            Matcher matcher = pattern.matcher(pd_amount.toLowerCase());
            if (!matcher.find()) {
                Toast.makeText(mActivity, "请输入正确的金额", Toast.LENGTH_SHORT).show();
            } else {
                rechargeCashToBalance(pd_amount);
            }
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
