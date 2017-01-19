package com.ewgvip.buyer.android.activity;

import android.app.Fragment;

import com.ewgvip.buyer.android.fragment.UCAccountSecurityFragment;

public class AccountSecurityActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new UCAccountSecurityFragment();
    }

}
