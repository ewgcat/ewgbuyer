package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;

import com.ewgvip.buyer.android.R;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:38
 * Description:  单fragment的基类,实现一个方法就可以
 */
public abstract class SingleFragmentActivity extends BaseActivity {
    protected abstract Fragment createFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        FragmentManager fm = getFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.index_top);
        if (fragment == null) {
            fragment = createFragment();
            fragment.setArguments(getIntent().getExtras());
            fm.beginTransaction().replace(R.id.index_top, fragment).commit();
        }
    }
}
