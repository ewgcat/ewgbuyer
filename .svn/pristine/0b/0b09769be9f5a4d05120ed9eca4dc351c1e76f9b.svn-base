package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.fragment.WebFragment;


/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:34
 * Description:  忘记密码,处理找回密码的流程
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("type", WebFragment.FORGET_PASSWORD);

        Fragment fragment = new WebFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
