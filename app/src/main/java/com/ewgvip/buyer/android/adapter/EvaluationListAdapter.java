package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.PhotoShowActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: EvaluationListAdapter.java
 * </p>
 * <p/>
 * <p>
 * Description: 评论适配器
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
 * @date 2014-8-6
 */

public class EvaluationListAdapter extends BaseAdapter {

    private Context context;//上下文
    private List<Map> eva_list;//评价数据集合
    private LayoutInflater inflater;

    public EvaluationListAdapter(Context context, List<Map> eva_list) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.eva_list = eva_list;
    }

    @Override
    public int getCount() {

        return eva_list.size();
    }

    @Override
    public Object getItem(int position) {

        return position;
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_goods_evaluate, null);
            holder = new ViewHolder();
            holder.tv_item_evaluate_user = (TextView) convertView.findViewById(R.id.tv_item_evaluate_user);
            holder.tv_item_evaluate_time = (TextView) convertView.findViewById(R.id.tv_item_evaluate_time);
            holder.tv_item_evaluate_content = (TextView) convertView.findViewById(R.id.tv_item_evaluate_content);
            holder.iv_item_evaluate_user_img = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_evaluate_user_img);
            holder.layout_item_evaluate_add_content = (LinearLayout) convertView.findViewById(R.id.layout_item_evaluate_add_content);
            holder.layout_item_add_evaluate_photos = (LinearLayout) convertView.findViewById(R.id.layout_item_add_evaluate_photos);
            holder.layout_item_evaluate_photos = (LinearLayout) convertView.findViewById(R.id.layout_item_evaluate_photos);
            holder.tv_item_evaluate_add_content = (TextView) convertView.findViewById(R.id.tv_item_evaluate_add_content);
            holder.iv_item_evaluate_img1 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_evaluate_img1);
            holder.iv_item_evaluate_img2 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_evaluate_img2);
            holder.iv_item_evaluate_img3 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_evaluate_img3);
            holder.iv_item_evaluate_img4 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_evaluate_img4);
            holder.iv_item_add_evaluate_img1 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_add_evaluate_img1);
            holder.iv_item_add_evaluate_img2 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_add_evaluate_img2);
            holder.iv_item_add_evaluate_img3 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_add_evaluate_img3);
            holder.iv_item_add_evaluate_img4 = (SimpleDraweeView) convertView.findViewById(R.id.iv_item_add_evaluate_img4);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tv_item_evaluate_user.setText(eva_list.get(position).get("user").toString());
        SpannableString contentString = null;
        if (eva_list.get(position).get("content").toString().equals("")) {
            contentString = new SpannableString("评价：" + "买家很懒，没有评价...");
        } else {
            contentString = new SpannableString("评价：" + eva_list.get(position).get("content").toString());
        }
        contentString.setSpan(new TextAppearanceSpan(context, R.style.evaluate_text), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.tv_item_evaluate_content.setText(contentString);
        holder.tv_item_evaluate_time.setText(eva_list.get(position).get("addTime").toString());
        //判断是否有追加评价
        if (eva_list.get(position).get("add_content") != null) {
            holder.layout_item_evaluate_add_content.setVisibility(View.VISIBLE);
            SpannableString addContentString = new SpannableString("追加评价：" + eva_list.get(position).get("add_content").toString());
            addContentString.setSpan(new TextAppearanceSpan(context, R.style.evaluate_text), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tv_item_evaluate_add_content.setText(addContentString);
        } else {
            holder.layout_item_evaluate_add_content.setVisibility(View.GONE);
        }
        final JSONArray jsonArray = (JSONArray) eva_list.get(position).get("evaluate_photos");
        //判断是否有晒单图片
        if (jsonArray.length() > 0) {
            holder.layout_item_evaluate_photos.setVisibility(View.VISIBLE);
            try {
                switch (jsonArray.length()) {
                    case 1:
                        holder.iv_item_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(0).toString(), holder.iv_item_evaluate_img1);
                        holder.iv_item_evaluate_img2.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img3.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 0);
                            }
                        });
                        break;
                    case 2:
                        holder.iv_item_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(0).toString(), holder.iv_item_evaluate_img1);
                        holder.iv_item_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(1).toString(), holder.iv_item_evaluate_img2);
                        holder.iv_item_evaluate_img3.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    toPhoto(jsonArray, 0);
                                }
                            }
                        });
                        holder.iv_item_evaluate_img2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (FastDoubleClickTools.isFastDoubleClick()) {
                                    toPhoto(jsonArray, 1);
                                }
                            }
                        });
                        break;
                    case 3:
                        holder.iv_item_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(0).toString(), holder.iv_item_evaluate_img1);
                        holder.iv_item_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(1).toString(), holder.iv_item_evaluate_img2);
                        holder.iv_item_evaluate_img3.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(2).toString(), holder.iv_item_evaluate_img3);
                        holder.iv_item_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 0);
                            }
                        });
                        holder.iv_item_evaluate_img2.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 1);
                            }
                        });
                        holder.iv_item_evaluate_img3.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 2);
                            }
                        });
                        break;
                    case 4:
                        holder.iv_item_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(0).toString(), holder.iv_item_evaluate_img1);
                        holder.iv_item_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(1).toString(), holder.iv_item_evaluate_img2);
                        holder.iv_item_evaluate_img3.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(2).toString(), holder.iv_item_evaluate_img3);
                        holder.iv_item_evaluate_img4.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray.get(3).toString(), holder.iv_item_evaluate_img4);
                        holder.iv_item_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 0);
                            }
                        });
                        holder.iv_item_evaluate_img2.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 1);
                            }
                        });
                        holder.iv_item_evaluate_img3.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 2);
                            }
                        });
                        holder.iv_item_evaluate_img4.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray, 3);
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.layout_item_evaluate_photos.setVisibility(View.GONE);
        }
        //判断是否返回eva_list是否有add_evaluate_photos追加晒单属性
        if (eva_list.get(position).get("add_evaluate_photos") != null) {
            final JSONArray jsonArray2 = (JSONArray) eva_list.get(position).get("add_evaluate_photos");
            holder.layout_item_add_evaluate_photos.setVisibility(View.VISIBLE);
            try {
                switch (jsonArray2.length()) {
                    case 1:
                        holder.iv_item_add_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(0).toString(), holder.iv_item_add_evaluate_img1);
                        holder.iv_item_add_evaluate_img2.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img3.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 0);
                            }
                        });
                        break;
                    case 2:
                        holder.iv_item_add_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(0).toString(), holder.iv_item_add_evaluate_img1);
                        holder.iv_item_add_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(1).toString(), holder.iv_item_add_evaluate_img2);
                        holder.iv_item_add_evaluate_img3.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 0);
                            }
                        });
                        holder.iv_item_add_evaluate_img2.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 1);
                            }
                        });
                        break;
                    case 3:
                        holder.iv_item_add_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(0).toString(), holder.iv_item_add_evaluate_img1);
                        holder.iv_item_add_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(1).toString(), holder.iv_item_add_evaluate_img2);
                        holder.iv_item_add_evaluate_img3.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(2).toString(), holder.iv_item_add_evaluate_img3);
                        holder.iv_item_add_evaluate_img4.setVisibility(View.GONE);
                        holder.iv_item_add_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 0);
                            }
                        });
                        holder.iv_item_add_evaluate_img2.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 1);
                            }
                        });
                        holder.iv_item_evaluate_img3.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 2);
                            }
                        });
                        break;
                    case 4:
                        holder.iv_item_add_evaluate_img1.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(0).toString(), holder.iv_item_add_evaluate_img1);
                        holder.iv_item_add_evaluate_img2.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(1).toString(), holder.iv_item_add_evaluate_img2);
                        holder.iv_item_add_evaluate_img3.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(2).toString(), holder.iv_item_add_evaluate_img3);
                        holder.iv_item_add_evaluate_img4.setVisibility(View.VISIBLE);
                        BaseActivity.displayImage(jsonArray2.get(3).toString(), holder.iv_item_add_evaluate_img4);
                        holder.iv_item_add_evaluate_img1.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 0);
                            }
                        });
                        holder.iv_item_add_evaluate_img2.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 1);
                            }
                        });
                        holder.iv_item_add_evaluate_img3.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 2);
                            }
                        });
                        holder.iv_item_add_evaluate_img4.setOnClickListener(view -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                toPhoto(jsonArray2, 3);
                            }
                        });
                        break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.layout_item_add_evaluate_photos.setVisibility(View.GONE);
        }
        BaseActivity.displayImage(eva_list.get(position).get("user_img").toString(), holder.iv_item_evaluate_user_img);
        return convertView;
    }

    public void toPhoto(JSONArray jsonArray, int position) {
        Intent intent = new Intent(context, PhotoShowActivity.class);
        ArrayList<String> list = new ArrayList<String>();
        for (int j = 0; j < jsonArray.length(); j++) {
            try {
                list.add(jsonArray.get(j).toString());
            } catch (Exception e) {
            }
        }
        intent.putExtra("list", list);
        intent.putExtra("position", position);
        context.startActivity(intent);
    }

    public static class ViewHolder {
        public TextView tv_item_evaluate_user;
        public TextView tv_item_evaluate_time;
        public TextView tv_item_evaluate_content;
        public SimpleDraweeView iv_item_evaluate_user_img;
        public LinearLayout layout_item_evaluate_add_content;
        public LinearLayout layout_item_add_evaluate_photos;
        public TextView tv_item_evaluate_add_content;
        public LinearLayout layout_item_evaluate_photos;
        public SimpleDraweeView iv_item_evaluate_img1;
        public SimpleDraweeView iv_item_evaluate_img2;
        public SimpleDraweeView iv_item_evaluate_img3;
        public SimpleDraweeView iv_item_evaluate_img4;
        public SimpleDraweeView iv_item_add_evaluate_img1;
        public SimpleDraweeView iv_item_add_evaluate_img2;
        public SimpleDraweeView iv_item_add_evaluate_img3;
        public SimpleDraweeView iv_item_add_evaluate_img4;
    }
}
