package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.dialog.ShareDialog;
import com.ewgvip.buyer.android.dialog.ShareWetcharImageUtils;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;
import com.tencent.connect.share.QQShare;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.tauth.Tencent;

/**
 *
 */
public class MySpreadFragment extends Fragment {

    private MySpreadFragment fragment;
    private MainActivity mActivity;
    private View rootView;
    private String qrcode_img_path;



    public MySpreadFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) this.getActivity();
        rootView = inflater.inflate(R.layout.fragment_my_spread, container, false);
        initView();
        fragment  = this;
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

        //右边导航标题
        TextView tv_right_title = (TextView) rootView.findViewById(R.id.tv_right_title);
        tv_right_title.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_index();
            }
        });
        //我的二维码:wecvfeww
        TextView tv_my_qrcode = (TextView) rootView.findViewById(R.id.tv_my_qrcode);

        //我的二维码图片
        SimpleDraweeView iv_qrcode = (SimpleDraweeView) rootView.findViewById(R.id.iv_qrcode);


        final SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        String invitation_code = preferences.getString("invitation_code", "");
        qrcode_img_path = preferences.getString("qrcode_img_path", "");
        tv_my_qrcode.setText("我的邀请码：" + invitation_code);
        iv_qrcode.setImageURI(Uri.parse(qrcode_img_path));
        Button bt_share = (Button) rootView.findViewById(R.id.bt_share);
        bt_share.setOnClickListener(v -> ShareDialog.show_share_dialog(mActivity, null,  fragment, null));

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
        if (qrcode_img_path != null && !qrcode_img_path.equals("")) {
            wxShareType = type;
            new MyTask().execute(qrcode_img_path);
        } else {
            Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            share_wx(thumb, type);
        }
    }

    private void share_wx(Bitmap thumb, int type) {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = qrcode_img_path;
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "下载e网购app,扫码注册成为会员哦！";
        msg.description = "快来扫码成为e网购会员哦！";
        if (thumb != null) {
            msg.thumbData = ShareWetcharImageUtils. bmpToByteArray(thumb, false);
        }
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = ShareWetcharImageUtils.buildTransaction("webpage");
        req.message = msg;
        req.scene = type == 0 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        mActivity.mwxapi.sendReq(req);
    }

    class MyTask extends AsyncTask<String, Void,Bitmap> {
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
        params.putString(QQShare.SHARE_TO_QQ_TITLE, "二维码分享");
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, "快来扫码成为e网购会员哦！");
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL,  qrcode_img_path);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, qrcode_img_path);
        params.putString(QQShare.SHARE_TO_QQ_APP_NAME, getString(R.string.app_name));
        if (type == 0) {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_ITEM_HIDE);
        } else {
            params.putInt(QQShare.SHARE_TO_QQ_EXT_INT, QQShare.SHARE_TO_QQ_FLAG_QZONE_AUTO_OPEN);
        }
        mActivity.mTencent.shareToQQ(mActivity, params,mActivity);

    }


}
