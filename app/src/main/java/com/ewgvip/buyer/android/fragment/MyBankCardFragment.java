package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.MyBankCardListAdapter;
import com.ewgvip.buyer.android.models.BankCardItem;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的银行卡
 */
public class MyBankCardFragment extends Fragment {
    @BindView(R.id.listview)
    ListView listview;
    private ArrayList<BankCardItem> list = new ArrayList<>();
    private BaseActivity mActivity;
    private View rootView;
    private SharedPreferences preferences;
    private MyBankCardListAdapter adapter;
    private PopupWindow popupWindow;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.list = null;
        this.adapter = null;
        this.popupWindow = null;
        this.preferences = null;
        this.mActivity = null;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            init();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        rootView = inflater.inflate(R.layout.fragment_my_bank_card, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new MyBankCardListAdapter(mActivity, list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener((parent, view, position, id) -> {
            BankCardItem cardItem = list.get(position);
            showPopupWindow(cardItem);
        });
        init();
        return rootView;
    }

    //初始化银行卡
    private void init() {
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/bankcard.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    Log.i("test", jsonObject.toString());
                    list.clear();
                    if (jsonObject.get("ret").toString().equals("1")) {
                        String pay_password = jsonObject.get("pay_password").toString();
                        preferences.edit().putString("pay_password", pay_password).commit();
                        String bankList = jsonObject.get("bankList").toString();
                        JSONArray jsonArray = new JSONArray(bankList);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                            String addTime = jsonObject1.get("addTime").toString();
                            String bank_code = jsonObject1.get("bank_code").toString();
                            String bank_img = jsonObject1.get("bank_img").toString();
                            String bank_name = jsonObject1.get("bank_name").toString();
                            String card_number = jsonObject1.get("card_number").toString();
                            String id = jsonObject1.get("id").toString();
                            String mobile = jsonObject1.get("mobile").toString();
                            String user_name = jsonObject1.get("user_name").toString();

                            BankCardItem bankCardItem = new BankCardItem(addTime, bank_code, bank_img, bank_name, card_number, id, mobile, user_name);
                            list.add(bankCardItem);
                        }
                        adapter.update(list);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //解绑银行卡
    private void removeBindBankCard(final BankCardItem cardItem) {
        Map paraMap = mActivity.getParaMap();
        paraMap.put("id", cardItem.id);
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/remove_bankcard.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    Log.i("test", jsonObject.toString());
                    String ret = jsonObject.get("ret").toString();
                    if (ret.equals("1")) {
                        CommonUtil.showSafeToast(mActivity, "解绑成功");
                        list.remove(cardItem);
                        adapter.update(list);
                    } else if (ret.equals("0")) {
                        CommonUtil.showSafeToast(mActivity, "解绑失败");
                    } else if (ret.equals("-1")) {
                        CommonUtil.showSafeToast(mActivity, "用户ID异常");
                    } else if (ret.equals("-2")) {
                        CommonUtil.showSafeToast(mActivity, "银行卡ID错误");
                    } else if (ret.equals("-3")) {
                        CommonUtil.showSafeToast(mActivity, "只能解绑自己银行卡");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //点击事件
    @OnClick({R.id.iv_back, R.id.bt_add_new_bank_card})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_add_new_bank_card:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    String pay_password = preferences.getString("pay_password", "");
                    if (TextUtils.isEmpty(pay_password)) {
                        mActivity.go_change_pay_password();
                    } else {
                        mActivity.go_to_my_pay_password_check();
                    }
                }
                break;
        }
    }

    //点击条目弹出PopupWindow
    private void showPopupWindow(final BankCardItem cardItem) {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(R.layout.bank_card_delete_popupwindow, null);
        final View removeBind = contentView.findViewById(R.id.tv_remove_bind_bank_card);
        final View cancle = contentView.findViewById(R.id.tv_cancle);
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(contentView, width, height, true);
        popupWindow.setAnimationStyle(R.style.popWindow_animation);
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
                float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
                // View view;
                RectF removeBindRectF = calcViewScreenLocation(removeBind);
                RectF cancleRectF = calcViewScreenLocation(cancle);
                if (cancleRectF.contains(x, y)) {
                    popupWindow.dismiss();
                }
                if (removeBindRectF.contains(x, y)) {
                    popupWindow.dismiss();
                    removeBindBankCard(cardItem);
                }
                return false;
            }
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.LTGRAY));
        // 设置好参数之后再show
        popupWindow.showAtLocation(rootView, Gravity.BOTTOM, 0, 0);

    }

    //计算指定的 View 在屏幕中的坐标。
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

}
