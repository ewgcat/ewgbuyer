package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 卡兑换会员
 */
public class ExchangeVipFragment extends Fragment {

    @BindView(R.id.et_card)
    EditText etCard;
    @BindView(R.id.et_password)
    EditText etPassword;

    private BaseActivity mActivity;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
        this.etCard = null;
        this.etPassword = null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_exchangevip, container, false);
        mActivity = (BaseActivity) getActivity();
        ButterKnife.bind(this, rootview);
        return rootview;
    }

    private void exchangevip() {
        Map map = mActivity.getParaMap();
        String card = etCard.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (TextUtils.isEmpty(card)) {
            CommonUtil.showSafeToast(mActivity, "请输入卡号");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            CommonUtil.showSafeToast(mActivity, "请输入密码");
            return;
        }
        map.put("app", "android");
        map.put("cardno", card);
        map.put("password", password);
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/vip_card_exchange.htm", map, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    Log.i("test",jsonObject.toString());
                    String result = jsonObject.getString("code");
                    if ("100".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, "用户升级成功");
                        getFragmentManager().popBackStack();
                    } else if ("200".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, " 用户未登录");
                    } else if ("300".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, "用户未激活");
                    } else if ("400".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, "卡密信息错误或无效");
                    } else if ("500".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, "不支持卡类型");
                    } else if ("600".equals(result)) {
                        CommonUtil.showSafeToast(mActivity, "用户当前等级不符合升级要求");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @OnClick({R.id.back, R.id.cash_levelup_vip, R.id.bt_exchangevip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                if (FastDoubleClickTools.isFastDoubleClick()){
                    getFragmentManager().popBackStack();
                    mActivity.hide_keyboard(view);
                }

                break;
            case R.id.cash_levelup_vip:
                if (FastDoubleClickTools.isFastDoubleClick()){
                    mActivity.go_vip_level_up();
                }
                break;
            case R.id.bt_exchangevip:
                if (FastDoubleClickTools.isFastDoubleClick()){
                    mActivity.hide_keyboard(view);
                    exchangevip();
                }
                break;
        }
    }




}
