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
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SpreadCodeFragment extends Fragment {


    @BindView(R.id.tv_my_promotion_code)
    TextView tvMyPromotionCode;
    @BindView(R.id.iv_parent_img)
    SimpleDraweeView ivParentImg;
    @BindView(R.id.tv_parent_name_i)
    TextView tvParentNameI;
    @BindView(R.id.tv_parent_promotion_code)
    TextView tvParentPromotionCode;
    @BindView(R.id.tv_parent_name)
    TextView tvParentName;
    @BindView(R.id.tv_parent_truename)
    TextView tvParentTruename;
    @BindView(R.id.tv_parent_wechat_code)
    TextView tvParentWechatCode;
    @BindView(R.id.tv_parent_qq_code)
    TextView tvParentQqCode;
    @BindView(R.id.tv_parent_mobile)
    TextView tvParentMobile;
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_spread_code, container, false);

        init();

        ButterKnife.bind(this, rootView);
        return rootView;
    }

    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress()
                + "/app/buyer/promotion_my_parent.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    String my_promotion_code = jsonObject.getString("my_promotion_code");
                    String parent_img = jsonObject.getString("parent_img");
                    String parent_name = jsonObject.getString("parent_name");
                    String parent_truename = jsonObject.getString("parent_truename");
                    String parent_promotion_code = jsonObject.getString("parent_promotion_code");
                    String parent_wechat_code = jsonObject.getString("parent_wechat_code");
                    String parent_qq_code = jsonObject.getString("parent_qq_code");
                    String parent_mobile = jsonObject.getString("parent_mobile");

                    tvMyPromotionCode.setText("我的推广码："+my_promotion_code);
                    tvParentNameI.setText(parent_name);
                    tvParentPromotionCode.setText(parent_promotion_code);
                    tvParentName.setText(parent_truename);
                    tvParentTruename.setText(parent_truename);
                    tvParentWechatCode.setText(parent_wechat_code);
                    tvParentQqCode.setText(parent_qq_code);
                    tvParentMobile.setText(parent_mobile);
                    ivParentImg.setImageURI(Uri.parse(parent_img));


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            mActivity.onBackPressed();
        }
    }


}
