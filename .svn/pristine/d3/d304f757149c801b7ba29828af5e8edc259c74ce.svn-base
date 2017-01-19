package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.EwgMessage;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * <p>
 * Title: MessageListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description: 消息列表适配器
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author hezeng
 * @version 1.0
 * @date 2014-7-30
 */
public class MessageListAdapter extends BaseAdapter {

    BaseActivity activity;
    LayoutInflater layoutInflater;
    ArrayList<EwgMessage> messageArrayList;

    public MessageListAdapter(BaseActivity activity, ArrayList<EwgMessage> messageArrayList) {
        if (messageArrayList != null) {
            this.activity = activity;
            this.messageArrayList = messageArrayList;

        }
        layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return messageArrayList.size();

    }

    @Override
    public Object getItem(int position) {

        return getItem(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_massage_list, null);
            viewHolder = new ViewHolder();
            viewHolder.messageTitle = (TextView) convertView.findViewById(R.id.messageTitle);
            viewHolder.messageTime = (TextView) convertView.findViewById(R.id.messageTime);
            viewHolder.messageContent = (TextView) convertView.findViewById(R.id.messageContent);
            viewHolder.messageStatus = (TextView) convertView.findViewById(R.id.messageStatus);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final EwgMessage ewgMessage = messageArrayList.get(position);
        viewHolder.messageTitle.setText(ewgMessage.fromUser);
        viewHolder.messageTime.setText(ewgMessage.addTime);
        if (ewgMessage.status.equals("0")){
            String content = ewgMessage.content;
            String subcontent ="";
            if (content.length()>20){
                subcontent = content.substring(0,20);
                viewHolder.messageContent.setText(subcontent+"...");
            }else {
                viewHolder.messageContent.setText(content+"...");
            }

            viewHolder.messageStatus.setText("点击阅读");
            viewHolder.messageStatus.setTextColor(activity.getResources().getColor(R.color.red));
        }else {
            viewHolder.messageContent.setText(ewgMessage.content);
            viewHolder.messageStatus.setText("已阅读");
            viewHolder.messageStatus.setTextColor(activity.getResources().getColor(R.color.dark_gray_border));
            viewHolder.messageContent.setTextColor(activity.getResources().getColor(R.color.dark_gray_border));
        }

        final ViewHolder finalViewHolder = viewHolder;
        convertView.setOnClickListener(v -> {
            if (ewgMessage.status.equals("0")) {
                finalViewHolder.messageContent.setText(ewgMessage.content);;
                finalViewHolder.messageStatus.setText("已阅读");
                finalViewHolder.messageStatus.setTextColor(activity.getResources().getColor(R.color.dark_gray_border));
                finalViewHolder.messageContent.setTextColor(activity.getResources().getColor(R.color.dark_gray_border));
                //发送请求
                sendMessageReadStatus(ewgMessage);
            }
        });
        return convertView;
    }

    private void sendMessageReadStatus(EwgMessage ewgMessage) {
        Map paramap   = activity.getParaMap();
        paramap.put("id", ewgMessage.id);
        RetrofitClient.getInstance(activity,null,activity.getParaMap()).createBaseApi().postJSONObject(activity.getAddress() + "/app/buyer/buyer_message_receive.htm",
                paramap, new BaseSubscriber<JSONObject>(activity) {
            @Override
            public void onNext(JSONObject jsonObject) {
            }
        });
    }

    public static class ViewHolder {
        public TextView messageTitle;
        public TextView messageTime;
        public TextView messageContent;
        public TextView messageStatus;
    }

}
