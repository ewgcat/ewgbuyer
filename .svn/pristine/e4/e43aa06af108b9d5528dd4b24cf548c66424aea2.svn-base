package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.fragment.Cart1Fragment;
import com.ewgvip.buyer.android.fragment.Cart2Fragment;


/**
 * Description:  购物车,处理整个购物过程
 */
public class CartActivity extends BaseActivity {

    /**
     * 根据购物车的不同步骤跳转不同页面
     *
     * @param savedInstanceState
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null && bundle.containsKey("cart_ids")) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Cart2Fragment cartFragment = new Cart2Fragment();
            cartFragment.setArguments(bundle);
            fragmentTransaction.replace(R.id.index_top, cartFragment);
            fragmentTransaction.commit();
        } else {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new Cart1Fragment();
            fragmentTransaction.replace(R.id.index_top, fragment);
            fragmentTransaction.commit();
        }
    }
}
