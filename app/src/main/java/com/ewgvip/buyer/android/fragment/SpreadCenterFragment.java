package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SpreadCenterFragment extends Fragment {


    @BindView(R.id.vip_user_img)
    SimpleDraweeView vipUserImg;
    @BindView(R.id.tv_user_name)
    TextView tvUserName;
    @BindView(R.id.tv_spread_level)
    TextView tvSpreadLevel;
    @BindView(R.id.tv_spread_income)
    TextView tvSpreadIncome;
    @BindView(R.id.tv_all_income)
    TextView tvAllIncome;
    @BindView(R.id.tv_drawout_money)
    TextView tvDrawoutMoney;
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_spread_center, container, false);
        ButterKnife.bind(this, rootView);
        initData();
        return rootView;
    }

    private void initData() {
        String urlString = mActivity.getCache("user_image_photo_url");
        vipUserImg.setImageURI(Uri.parse(urlString));
        String user_name = mActivity.getCache("user_name");
        tvUserName.setText(user_name);
        String grade_name = mActivity.getCache("grade_name");
        tvSpreadLevel.setText(grade_name);
        String promotion_income = mActivity.getCache("promotion_income");
        tvSpreadIncome.setText("￥"+promotion_income);
        String amount_income = mActivity.getCache("amount_income");
        tvAllIncome.setText("￥"+amount_income);
        String extract_cash = mActivity.getCache("extract_cash");
        tvDrawoutMoney.setText("￥"+extract_cash);
    }


    @OnClick({R.id.iv_back, R.id.rl_my_spread_card_manange
            , R.id.rl_spread_team_manange, R.id.rl_my_spread_code
            , R.id.rl_spread_poster, R.id.rl_drawout_money
            , R.id.rl_drawout_history})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.rl_my_spread_card_manange:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_card_level_manage();
                }
                break;
            case R.id.rl_spread_team_manange:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_spread_team_manage();
                }
                break;
            case R.id.rl_my_spread_code:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_my_spread_code();
                }
                break;
            case R.id.rl_spread_poster:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_spread_poster();
                }
                break;
            case R.id.rl_drawout_money:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_drawout_money();
                }
                break;
            case R.id.rl_drawout_history:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_drawout_history();
                }
                break;
        }
    }
}
