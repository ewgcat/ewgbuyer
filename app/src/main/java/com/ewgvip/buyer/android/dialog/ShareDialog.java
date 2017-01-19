package com.ewgvip.buyer.android.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.fragment.GoodsFragment;
import com.ewgvip.buyer.android.fragment.MyPosterFragment;
import com.ewgvip.buyer.android.fragment.MySpreadFragment;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:41
 * Description:  分享对话框
 */
public class ShareDialog {


    public static Dialog show_share_dialog(final BaseActivity mActivity, final GoodsFragment fragment,  final MySpreadFragment mySpreadFragment,final MyPosterFragment myPosterFragment) {

        final Dialog dlg = new Dialog(mActivity, R.style.AlertDialog);
        LayoutInflater inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.dialog_share, null);
        layout.setMinimumWidth(mActivity.getScreenWidth());

        Window w = dlg.getWindow();
        WindowManager.LayoutParams lp = w.getAttributes();
        lp.gravity = Gravity.BOTTOM;
        dlg.onWindowAttributesChanged(lp);
        dlg.setCanceledOnTouchOutside(true);
        dlg.setContentView(layout);
        dlg.show();


        //微信分享
        layout.findViewById(R.id.ll_wx_share).setOnClickListener(view -> {
            if(fragment!=null) {
                fragment.share_to_wechat(0);
            }else if (mySpreadFragment!=null){
                mySpreadFragment.share_to_wechat(0);
            }else if (myPosterFragment!=null){
                myPosterFragment.share_to_wechat(0);
            }
        });
        //微信朋友圈分享
        layout.findViewById(R.id.ll_wx_friend).setOnClickListener(view -> {
            if(fragment!=null) {
                fragment.share_to_wechat(1);
            }else if (mySpreadFragment!=null){
                mySpreadFragment.share_to_wechat(1);
            }else if (myPosterFragment!=null){
                myPosterFragment.share_to_wechat(1);
            }
        });



        //QQ分享
        layout.findViewById(R.id.ll_qq_share).setOnClickListener(view -> {
            if(fragment!=null) {
                fragment.qq_share(0);
            }else if (mySpreadFragment!=null){
                mySpreadFragment.qq_share(0);
            }else if (myPosterFragment!=null){
                myPosterFragment.qq_share(0);
            }

        });
        //QQ空间分享
        layout.findViewById(R.id.ll_qq_qzone_share).setOnClickListener(view -> {
            if(fragment!=null) {
                fragment.qq_share(1);
            }else if (mySpreadFragment!=null){
                mySpreadFragment.qq_share(1);
            }else if (myPosterFragment!=null){
                myPosterFragment.qq_share(1);
            }

        });
        layout.findViewById(R.id.share_cancel).setOnClickListener(view -> dlg.dismiss());


        return dlg;
    }

}
