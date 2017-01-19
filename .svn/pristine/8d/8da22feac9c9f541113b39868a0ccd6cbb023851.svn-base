package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.my_authorization_img;

/**
 * 我的授权书
 */
public class MyAuthorizationFragment extends Fragment {


    @BindView(my_authorization_img)
    ImageView myAuthorizationImg;

    private BaseActivity mActivity;
    private View rootView;
    private String urlString;
    private static final int QUIT_INTERVAL = 2000;
    private long lastclickTime;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        urlString=null;
        mActivity=null;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_authorization, container, false);
        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }


    //初始授权书
    private void init() {
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/auth_license.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    myAuthorizationImg.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(jsonObject.get("auth_license").toString())) {
                        urlString = jsonObject.get("auth_license").toString();
                        Log.i("test","url1="+urlString);
                        CommonUtil.loadIntoUseFitWidth(mActivity, urlString, R.mipmap.errorloadimage, myAuthorizationImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //获取新的授权书
    public void getNewAuthorization() {
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/resetAuthlicense.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    myAuthorizationImg.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(jsonObject.get("resetAuth_license").toString())) {
                        urlString = jsonObject.get("resetAuth_license").toString();
                        Log.i("test","url2="+urlString);
                        CommonUtil.loadIntoUseFitWidth(mActivity, urlString, R.mipmap.errorloadimage, myAuthorizationImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    @OnClick({R.id.iv_back, R.id.bt_get_my_authorization_img})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()){
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.bt_get_my_authorization_img:  long clickTime = System.currentTimeMillis();
                if (clickTime -  lastclickTime > QUIT_INTERVAL) {
                    lastclickTime=clickTime;
                    myAuthorizationImg.setVisibility(View.INVISIBLE);
                    getNewAuthorization();
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getFocus();
    }

    //主界面获取焦点
    private void getFocus() {
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                Intent intent = new Intent();
                getFragmentManager().popBackStack();
                if (getTargetFragment()!=null){
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }

}
