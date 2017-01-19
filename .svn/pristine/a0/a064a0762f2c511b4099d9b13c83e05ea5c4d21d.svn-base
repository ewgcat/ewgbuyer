package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyBankPayPasswordCheckFragment extends Fragment {

    private static MyBankPayPasswordCheckFragment fragment;
    private BaseActivity mActivity;
    private View rootView;
    private SharedPreferences preferences;
    private String pay_password;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }


    public MyBankPayPasswordCheckFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity)getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_pay_password_check, container, false);
        initView();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        pay_password = preferences.getString("pay_password", "");
        return rootView;
    }

    private void initView() {
        //返回键
        ImageView iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        iv_back.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
                mActivity.hide_keyboard(v);
            }
        });
       final EditText et_my_pay_password= (EditText) rootView.findViewById(R.id.et_my_pay_password);
        //验证支付密码
        rootView.findViewById(R.id.bt_check_my_pay_passord).setOnClickListener(v -> {
            String mypaypassword = et_my_pay_password.getText().toString().trim();
            if (TextUtils.isEmpty(mypaypassword)){
                CommonUtil.showSafeToast(mActivity,"请输入支付密码");
            }else {
                String md5_mypaypassword = mActivity.md5(mypaypassword);
                if (md5_mypaypassword.equals(pay_password)){
                    mActivity.go_to_bind_new_bank_card();
                }else {
                    CommonUtil.showSafeToast(mActivity,"输入的支付密码错误，请重新输入");
                }
            }
        });
    }




}
