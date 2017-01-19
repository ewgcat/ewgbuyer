package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
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

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SpreadDrawoutMoneyFragment extends Fragment {

    @BindView(R.id.et_please_money)
    EditText etPleaseMoney;
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_spread_drawout, container, false);

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.iv_back, R.id.bt_confirm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_confirm:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.hide_keyboard(view);
                    commitDrawoutmoney();
                }
                break;
        }
    }

    private void commitDrawoutmoney() {
        Map paraMap = mActivity.getParaMap();

        Editable text = etPleaseMoney.getText();
        String cash_amount = text.toString().trim();
        if (TextUtils.isEmpty(cash_amount)){
            Toast.makeText(mActivity,"请输入提现金额",Toast.LENGTH_SHORT).show();
            return;
        }
        paraMap.put("cash_amount",cash_amount);
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress()
                + "/app/buyer/promotion_income_withdrawal_save.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String code = jsonObject.getString("code");
                    if ("100".equals(code)){
                        Toast.makeText(mActivity,"提现成功",Toast.LENGTH_SHORT).show();
                        getFragmentManager().popBackStack();
                        getFragmentManager().popBackStack();
                    }else if ("-100".equals(code)){
                        Toast.makeText(mActivity,"提现失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
