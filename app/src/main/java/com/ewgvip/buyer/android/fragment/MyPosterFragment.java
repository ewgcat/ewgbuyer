package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.dialog.ShareDialog;
import com.ewgvip.buyer.android.dialog.ShareWetcharImageUtils;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.ewgvip.buyer.android.R.id.my_poster_img;

/**
 * 我的海报
 */
public class MyPosterFragment extends Fragment {

    @BindView(my_poster_img)
    ImageView myPosterImg;
    private MainActivity mActivity;
    private View rootView;
    private String urlString;
    private MyPosterFragment fragment;
    private static final int QUIT_INTERVAL = 2000;
    private long lastclickTime;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.mActivity = null;
        this.fragment = null;
        this.urlString = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_poster, container, false);
        ButterKnife.bind(this, rootView);
        fragment = this;
        init();
        return rootView;
    }


    //初始化海报
    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/poster.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    myPosterImg.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(jsonObject.get("poster_url").toString())) {
                        urlString = jsonObject.get("poster_url").toString();
                        CommonUtil.loadIntoUseFitWidth(mActivity, urlString, R.mipmap.errorloadimage, myPosterImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //获取新海报
    public void getNewPoster() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/buyer/resetPoster.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    myPosterImg.setVisibility(View.VISIBLE);
                    if (!TextUtils.isEmpty(jsonObject.get("resetPoster_url").toString())) {
                        urlString = jsonObject.get("resetPoster_url").toString();
                        CommonUtil.loadIntoUseFitWidth(mActivity, urlString, R.mipmap.errorloadimage, myPosterImg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }



    //点击事件监听
    @OnClick({R.id.iv_back, R.id.tv_share, R.id.bt_get_poster})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.onBackPressed();
                    mActivity.hide_keyboard(view);
                }
                break;
            case R.id.tv_share:
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    ShareDialog.show_share_dialog(mActivity, null, null, fragment);
                }

                break;
            case R.id.bt_get_poster:

                long clickTime = System.currentTimeMillis();
                if (clickTime - lastclickTime > QUIT_INTERVAL) {
                    lastclickTime=clickTime;
                    myPosterImg.setVisibility(View.INVISIBLE);
                    getNewPoster();
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
                if (getTargetFragment() != null) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 100, intent);
                }
                return true;
            }
            return false;
        });
    }


    //微信分享
    int wxShareType = 0;//0微信好友分享1微信朋友圈分享

    //微信分享
    public void share_to_wechat(final int type) {
        if (mActivity.mwxapi == null) {
            mActivity.mwxapi = WXAPIFactory.createWXAPI(mActivity, Constants.WECHAT_API_KEY);
        }
        if (!mActivity.mwxapi.isWXAppInstalled()) {
            Toast.makeText(mActivity, "您还未安装微信客户端,无法进行分享", Toast.LENGTH_SHORT).show();
            return;
        }
        if (urlString != null && !urlString.equals("")) {
            wxShareType = type;
            new MyTask().execute(urlString);
        } else {
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            share_wx(thumb, type);
        }
    }

    private void share_wx(Bitmap thumb, int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = urlString;

        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "下载e网购优品,扫码注册成为会员哦！";
        msg.description = "快来扫码成为e网购会员哦！";
        if (thumb != null) {
            msg.thumbData = ShareWetcharImageUtils.bmpToByteArray(thumb, false);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareWetcharImageUtils.buildTransaction("webpage");
        req.message = msg;
        req.scene =
                type == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        mActivity.mwxapi.sendReq(req);
    }


    class MyTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            return ShareWetcharImageUtils.getBitmapFromURL(strings[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            share_wx(bitmap, wxShareType);
            super.onPostExecute(bitmap);
        }
    }

    //QQ分享
    public void qq_share(int type) {
        if (mActivity.mTencent == null) {
            mActivity.mTencent = Tencent.createInstance(Constants.QQ_API_KEY, mActivity.getApplicationContext());
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "我的海报");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "快来扫码成为e网购会员哦！");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, urlString);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, urlString);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        if (type == 0) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        mActivity.mTencent.shareToQQ(mActivity, params, mActivity);
    }


}
