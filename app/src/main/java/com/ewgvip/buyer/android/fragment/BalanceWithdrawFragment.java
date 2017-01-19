package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.BankCardItem;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * 提现
 */
public class BalanceWithdrawFragment extends Fragment {

    private int b;
    private BaseActivity mActivity;
    private View rootView;
    private EditText et_card_number, et_money;
    private Button mbt_confirm;
    private PopupWindow popupWindow;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.et_card_number = null;
        this.et_money = null;
        this.mbt_confirm = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_balance_withdraw2, container, false);


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("提现");//设置标题
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

        rootView.findViewById(R.id.ll_select_bankcard).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()){
                showPopwindow();
            }
        });
        et_card_number = (EditText) rootView.findViewById(R.id.et_card_number);
        et_money = (EditText) rootView.findViewById(R.id.et_please_money);
        mbt_confirm = (Button) rootView.findViewById(R.id.bt_confirm);

        getBankCards();
        /**
         * 提现功能
         */

        mbt_confirm.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(view);
                String account = et_card_number.getText().toString().trim();
                String money = et_money.getText().toString().trim();
                withDrawCashToBankCard(account, money);
            }
        });

        return rootView;
    }

    public void withDrawCashToBankCard(String account, String money) {
        if (account.equals("") || money.equals("")) {
            Toast.makeText(mActivity, "账号或或金额不能为空！", Toast.LENGTH_SHORT).show();
        } else {
            String money_type = "^\\d*(\\.\\d{1,2})*+$";
            if (!money.matches(money_type)) {
                Toast.makeText(mActivity, "输入的金额格式有误！整数后最多两位小数", Toast.LENGTH_SHORT).show();
            } else {
                mActivity.showProcessDialog();
                Map paramap = mActivity.getParaMap();
                //后台默认只能提现到银行卡
                if (bankcardlist.size()>0){
                    paramap.put("cash_payment",bankcardlist.get(selectCardPosition).id);
                }else {
                    Toast.makeText(getActivity(), "请先去绑定银行卡", Toast.LENGTH_SHORT).show();
                }

                paramap.put("cash_amount", money);
                paramap.put("cash_account", account);

                RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                Request<JSONObject> request = new NormalPostRequest(
                        mActivity, mActivity.getAddress() + "/app/buyer/cash_save.htm",
                        result -> {
                            try {
                                b = result.getInt("status");
                                if (result.has("msg")){
                                    String  msg = result.getString("msg");
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                                    if (b == 1) {
                                        Intent intent=new Intent();
                                        getFragmentManager().popBackStack();
                                        if (getTargetFragment()!=null){
                                            getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
                                        }
                                    }

                                }else {
                                    if (b == 1) {
                                        Toast.makeText(getActivity(), "提交成功！", Toast.LENGTH_SHORT).show();Intent intent=new Intent();
                                        getFragmentManager().popBackStack();
                                        if (getTargetFragment()!=null){
                                            getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
                                        }
                                    }
                                    if (b == 0) {
                                        Toast.makeText(getActivity(), "提交失败！提现金额不能大于余额", Toast.LENGTH_SHORT).show();
                                    }
                                    if (b == -1) {
                                        Toast.makeText(getActivity(), "提交失败！提现金额不能小于70元", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mActivity.hideProcessDialog(0);
                        }, error -> {
                            Log.i("test",error.toString());
                            mActivity.hideProcessDialog(1);
                        }, paramap);
                mRequestQueue.add(request);
            }
        }
    }

    private ArrayList<BankCardItem> bankcardlist = new ArrayList<BankCardItem>();

    void getBankCards() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/bankcard.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    JSONArray bankList = jsonObject.getJSONArray("bankList");
                    int length = bankList.length();
                    if (length == 0) {
                        return;
                    } else {
                        for (int i = 0; i < length; i++) {
                            JSONObject jsonObject1 = bankList.getJSONObject(i);
                            bankcardlist.add(new BankCardItem(jsonObject1.getString("id"), jsonObject1.getString("card_number"), jsonObject1.getString("bank_name")));
                        }
                        et_card_number.setText(bankcardlist.get(0).card_number);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class BankCardListAdapter extends BaseAdapter {

        ArrayList<BankCardItem> list;

        public BankCardListAdapter(ArrayList<BankCardItem> list) {
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = View.inflate(mActivity, R.layout.bank_card_item, null);
            }
            TextView tv_bank_name = (TextView) convertView.findViewById(R.id.tv_bank_name);
            TextView tv_card_number = (TextView) convertView.findViewById(R.id.tv_card_number);
            BankCardItem bankCardItem = list.get(position);
            tv_bank_name.setText(bankCardItem.bank_name);
            tv_card_number.setText(bankCardItem.card_number);
            return convertView;
        }
    }

    private int selectCardPosition = 0;

    private void showPopwindow() {

        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.bank_card_list_pop_window, null);


        ListView bank_card_listview = (ListView) contentView.findViewById(R.id.bank_card_listview);
        View tv = contentView.findViewById(R.id.tv);
        View tv_select = contentView.findViewById(R.id.tv_select);
        if (bankcardlist.isEmpty()){
            tv.setVisibility(View.VISIBLE);
            tv_select.setVisibility(View.INVISIBLE);
            bank_card_listview.setVisibility(View.INVISIBLE);
            contentView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(v);
                    popupWindow.dismiss();
                }
            });
        }else {

            tv.setVisibility(View.INVISIBLE);
            tv_select.setVisibility(View.VISIBLE);
            bank_card_listview.setVisibility(View.VISIBLE);

            bank_card_listview.setAdapter(new BankCardListAdapter(bankcardlist));
            bank_card_listview.setOnItemClickListener((parent, view, position, id) -> {
                if (bankcardlist.size()>0){
                    selectCardPosition=position;
                    et_card_number.setText(bankcardlist.get(selectCardPosition).card_number);
                }

                popupWindow.dismiss();
            });
        }


        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT+400;

        popupWindow = new PopupWindow(contentView, width, height, true);
        popupWindow.setAnimationStyle(R.style.popWindow_animation);

        popupWindow.setTouchable(true);

        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // 设置好参数之后再show
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     *
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
