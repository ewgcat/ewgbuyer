package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.fragment.GroupShoppingFragment;


/**
 * 团购页面
 */
public class GroupShoppingActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        Bundle bundle = new Bundle();
        bundle.putInt("flag", getIntent().getIntExtra("flag", 1));
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new GroupShoppingFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.index_top, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (GroupShoppingFragment.popArea != null && GroupShoppingFragment.popArea.isShowing()) {
            GroupShoppingFragment.popArea.dismiss();
            GroupShoppingFragment.areaShow = true;
            return true;
        }
        if (GroupShoppingFragment.popClass != null && GroupShoppingFragment.popClass.isShowing()) {
            GroupShoppingFragment.popClass.dismiss();
            GroupShoppingFragment.classShow = true;
            return true;
        }
        if (GroupShoppingFragment.popPrice != null && GroupShoppingFragment.popPrice.isShowing()) {
            GroupShoppingFragment.popPrice.dismiss();
            GroupShoppingFragment.priceShow = true;
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
