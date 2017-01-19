package com.ewgvip.buyer.android.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.RedPackageItem;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MyRedPackageListAdapter extends BaseAdapter {

    private static final int TYPE_REDPACKAGE = 0;
    private static final int TYPE_MESSAGE = 1;
    int TYPE_COUNT = 2;

    BaseActivity mActivity;

    private ArrayList<RedPackageItem> list;

    public MyRedPackageListAdapter(BaseActivity mActivity, ArrayList<RedPackageItem> list) {
        this.mActivity = mActivity;
        this.list = list;

    }


    @Override
    public int getItemViewType(int position) {

        String type = list.get(position).type;
        switch (type) {
            case "redpackage":
                return TYPE_REDPACKAGE;
            case "message":
                return TYPE_MESSAGE;
            default:
                return TYPE_REDPACKAGE;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RedPackageItem redPackageItem = list.get(position);

        View redpackageView = null;
        View messageView = null;
        int currentType = getItemViewType(position);
        if (currentType == TYPE_REDPACKAGE) {
            RedpackageViewHolder redpackageHolder = null;
            if (convertView == null) {
                redpackageHolder = new RedpackageViewHolder();
                redpackageView = LayoutInflater.from(mActivity).inflate(R.layout.item_red_package, null);
                redpackageHolder.wishing = (TextView) redpackageView.findViewById(R.id.wishing);
                redpackageHolder.ll_receive_state = (LinearLayout) redpackageView.findViewById(R.id.ll_receive_state);
                redpackageView.setTag(redpackageHolder);
                convertView = redpackageView;
            } else {
                redpackageHolder = (RedpackageViewHolder) convertView.getTag();
            }
            redpackageHolder.wishing.setText(redPackageItem.wishing);
            String receive_state = redPackageItem.receive_state;
            if ("RECEIVED".equals(receive_state)) {
                redpackageHolder.ll_receive_state.setVisibility(View.VISIBLE);
            } else {
                redpackageHolder.ll_receive_state.setVisibility(View.INVISIBLE);
            }


        } else if (currentType == TYPE_MESSAGE) {
            MessageViewHolder messageViewHolder = null;
            if (convertView == null) {
                messageViewHolder = new MessageViewHolder();
                messageView = LayoutInflater.from(mActivity).inflate(R.layout.item_redpackage_sms, null);
                messageViewHolder.iv_sms_headicon = (SimpleDraweeView) messageView.findViewById(R.id.iv_sms_headicon);
                messageViewHolder.tv_sms = (TextView) messageView.findViewById(R.id.tv_sms);
                messageView.setTag(messageViewHolder);
                convertView = messageView;
            } else {
                messageViewHolder = (MessageViewHolder) convertView.getTag();
            }

            if (TextUtils.isEmpty(redPackageItem.wx_head_img)) {
                redPackageItem.wx_head_img = mActivity.getCache("user_image_photo_url");
            }
            messageViewHolder.iv_sms_headicon.setImageURI(Uri.parse(redPackageItem.wx_head_img));
            messageViewHolder.tv_sms.setText(redPackageItem.message);

        }


        return convertView;

    }

    class RedpackageViewHolder {

        public TextView wishing;
        public LinearLayout ll_receive_state;
    }

    class MessageViewHolder {
        public SimpleDraweeView iv_sms_headicon;
        public TextView tv_sms;
    }

}
