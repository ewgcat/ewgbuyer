package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.models.GoodsClassSecond;
import com.ewgvip.buyer.android.models.GoodsClassThird;
import com.ewgvip.buyer.android.utils.DensityUtil;
import com.ewgvip.buyer.android.utils.OnClickParameterListener;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: RightListAdapter.java
 * </p>
 * <p>
 * <p>
 * Description: 商品分类右侧适配器
 * </p>
 * <p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author lgx
 * @version 1.0
 * @date 2014-7-22
 */
public class GoodsclassSecondAdapter extends BaseAdapter {
    private BaseActivity mActivity;
    private List<GoodsClassSecond> list;
    private LayoutInflater inflater;
    private final static String TAG = "GoodsclassSecondAdapter";
    private Map<Integer, LinearLayout> map;
    private WindowManager windowManager; // 窗口管理器
    private Point point; // 当前的宽和高
    private int iWidth, iSize, iMaxSize;
    private GoodsClassThird goodsClassThird;
    private GoodsClassSecond goodsClassSecond;
    private OnClickParameterListener onClick;

    public GoodsclassSecondAdapter(MainActivity mActivity, List<GoodsClassSecond> list) {
        this.mActivity = mActivity;
        this.list = list;
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        windowManager = mActivity.getWindowManager();
        point = new Point();
        windowManager.getDefaultDisplay().getSize(point);
        iWidth = (point.x - DensityUtil.dip2px(mActivity, 140)) / 3;
        getMaxSize();
    }

    public void setClickListener(OnClickParameterListener onClick) {
        if (null != onClick) {
            this.onClick = onClick;
        }
    }

    @Override
    public int getCount() {

        return list.size();
    }

    @Override
    public GoodsClassSecond getItem(int position) {

        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolderSecond viewHolder = null;
        goodsClassSecond = getItem(position);
        if (convertView == null) {
            getMaxSize();
            convertView = inflater.inflate(R.layout.goods_type_second, null);
            viewHolder = new ViewHolderSecond();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.linearLayout);
            viewHolder.listImages = new SimpleDraweeView[iMaxSize];
            viewHolder.listLayouts = new LinearLayout[iMaxSize];
            viewHolder.listTextViews = new TextView[iMaxSize];
            viewHolder.layouts = new LinearLayout[iSize];
            for (int i = 0; i < iSize; i++) {
                int iLeft = i * 3, iCenter = iLeft + 1, iRight = iCenter + 1;
                if (iLeft < iMaxSize) {
                    viewHolder.linearLeft = getLinearLayoutChild();
                    viewHolder.imgLeft = getImageView();
                    viewHolder.tvLeft = getTextView();
                    viewHolder.linearMain = getLinearLayout();
                    viewHolder.linearLeft.addView(viewHolder.imgLeft);
                    viewHolder.linearLeft.addView(viewHolder.tvLeft);
                    viewHolder.linearMain.addView(viewHolder.linearLeft);
                    viewHolder.listImages[iLeft] = viewHolder.imgLeft;
                    viewHolder.listTextViews[iLeft] = viewHolder.tvLeft;
                    viewHolder.listLayouts[iLeft] = viewHolder.linearLeft;
                }
                if (iCenter < iMaxSize) {
                    viewHolder.linearCenter = getLinearLayoutChild();
                    viewHolder.imgCenter = getImageView();
                    viewHolder.tvCenter = getTextView();
                    viewHolder.linearCenter.addView(viewHolder.imgCenter);
                    viewHolder.linearCenter.addView(viewHolder.tvCenter);
                    viewHolder.linearMain.addView(viewHolder.linearCenter);
                    viewHolder.listImages[iCenter] = viewHolder.imgCenter;
                    viewHolder.listTextViews[iCenter] = viewHolder.tvCenter;
                    viewHolder.listLayouts[iCenter] = viewHolder.linearCenter;
                }
                if (iRight < iMaxSize) {
                    viewHolder.linearRight = getLinearLayoutChild();
                    viewHolder.imgRight = getImageView();
                    viewHolder.tvRight = getTextView();
                    viewHolder.linearRight.addView(viewHolder.imgRight);
                    viewHolder.linearRight.addView(viewHolder.tvRight);
                    viewHolder.linearMain.addView(viewHolder.linearRight);
                    viewHolder.listImages[iRight] = viewHolder.imgRight;
                    viewHolder.listTextViews[iRight] = viewHolder.tvRight;
                    viewHolder.listLayouts[iRight] = viewHolder.linearRight;
                }
                viewHolder.layouts[i] = viewHolder.linearMain;
                viewHolder.linearLayout.addView(viewHolder.linearMain);
            }
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolderSecond) convertView.getTag();
        }
        viewHolder.tvName.setText(goodsClassSecond.getName());
        try {
            for (int i = 0; i < iSize; i++) {
                getLinearLayout(i, viewHolder, goodsClassSecond.getList(),
                        position);
            }
        } catch (Exception ex) {
        }
        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
    }

    private void getMaxSize() {
        if (list.size() > 0) {
            this.iMaxSize = list.get(list.size() - 1).getMaxSize();
            this.iSize = (int) Math.ceil((double) iMaxSize / 3);
        } else {
            this.iMaxSize = 0;
            this.iSize = 0;
        }
    }

    private final class ViewHolderSecond {
        public TextView tvName, tvLeft, tvCenter, tvRight;
        public LinearLayout linearLayout, linearLeft, linearCenter,
                linearRight, linearMain;
        public SimpleDraweeView imgLeft, imgCenter, imgRight;
        public SimpleDraweeView[] listImages;
        public TextView[] listTextViews;
        public LinearLayout[] layouts, listLayouts;
    }

    private SimpleDraweeView getImageView() {
        SimpleDraweeView imageView = new SimpleDraweeView(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                DensityUtil.dip2px(mActivity, 80));
        layoutParams.gravity = Gravity.CENTER;
        imageView.setLayoutParams(layoutParams);
        GenericDraweeHierarchyBuilder builder =
                new GenericDraweeHierarchyBuilder(mActivity.getResources());
        GenericDraweeHierarchy hierarchy = builder
                .setFadeDuration(300)
                .setPlaceholderImage(mActivity.getResources().getDrawable(R.mipmap.ic_stub))
                .setFailureImage(mActivity.getResources().getDrawable(R.mipmap.ic_stub), ScalingUtils.ScaleType.CENTER_INSIDE)
                .setActualImageScaleType(ScalingUtils.ScaleType.CENTER_CROP)
                .build();
        imageView.setHierarchy(hierarchy);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        return imageView;
    }

    private TextView getTextView() {
        TextView tView = new TextView(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iWidth,
                DensityUtil.dip2px(mActivity, 20));
        layoutParams.gravity = Gravity.CENTER;
        tView.setLayoutParams(layoutParams);
        tView.setSingleLine(true);
        tView.setTextColor(Color.BLACK);
        tView.setTextSize(12);
        tView.setGravity(Gravity.CENTER);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        return tView;
    }

    private LinearLayout getLinearLayout() {
        LinearLayout layout = new LinearLayout(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.bottomMargin = DensityUtil.dip2px(mActivity, 10);
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        return layout;
    }

    private LinearLayout getLinearLayoutChild() {
        LinearLayout layout = new LinearLayout(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(iWidth,
                DensityUtil.dip2px(mActivity, 100));
        layoutParams.leftMargin = DensityUtil.dip2px(mActivity, 10);
        layoutParams.gravity = Gravity.CENTER;
        layout.setLayoutParams(layoutParams);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setWeightSum(1);
        layout.setClickable(true);
        return layout;
    }

    public void getLinearLayout(int iPosition,
                                ViewHolderSecond viewHolderSecond, List<GoodsClassThird> list,
                                final int iParentPosition) {
        int iCount = list.size();
        final int iLeft = iPosition * 3, iCenter = iLeft + 1, iRight = iCenter + 1;
        viewHolderSecond.linearLeft = viewHolderSecond.listLayouts[iLeft];
        viewHolderSecond.tvLeft = viewHolderSecond.listTextViews[iLeft];
        viewHolderSecond.imgLeft = viewHolderSecond.listImages[iLeft];
        viewHolderSecond.linearMain = viewHolderSecond.layouts[iPosition];
        if (iLeft < iCount) {
            goodsClassThird = list.get(iLeft);
        /*	mActivity.displayImage(goodsClassThird.getUrl(),
					viewHolderSecond.imgLeft);*/
            viewHolderSecond.imgLeft.setImageURI(Uri.parse(goodsClassThird.getUrl()));
            viewHolderSecond.tvLeft.setText(goodsClassThird.getName());
            viewHolderSecond.linearLeft
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (null != onClick) {
                                onClick.onClick(iLeft, iParentPosition);
                            }
                        }
                    });
            viewHolderSecond.linearLeft.setVisibility(View.VISIBLE);
            viewHolderSecond.tvLeft.setVisibility(View.VISIBLE);
            viewHolderSecond.imgLeft.setVisibility(View.VISIBLE);
            viewHolderSecond.linearMain.setVisibility(View.VISIBLE);
        } else {
            viewHolderSecond.linearLeft.setVisibility(View.GONE);
            viewHolderSecond.tvLeft.setVisibility(View.GONE);
            viewHolderSecond.imgLeft.setVisibility(View.GONE);
            viewHolderSecond.linearMain.setVisibility(View.GONE);

        }

        if (iCenter < iCount) {
            viewHolderSecond.linearCenter = viewHolderSecond.listLayouts[iCenter];
            viewHolderSecond.tvCenter = viewHolderSecond.listTextViews[iCenter];
            viewHolderSecond.imgCenter = viewHolderSecond.listImages[iCenter];
            goodsClassThird = list.get(iCenter);
            viewHolderSecond.tvCenter.setText(goodsClassThird.getName());
		/*	mActivity.displayImage(goodsClassThird.getUrl(),
					viewHolderSecond.imgCenter);*/
            viewHolderSecond.imgCenter.setImageURI(Uri.parse(goodsClassThird.getUrl()));
            viewHolderSecond.linearCenter
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (null != onClick) {
                                onClick.onClick(iCenter, iParentPosition);
                            }
                        }
                    });
            viewHolderSecond.linearCenter.setVisibility(View.VISIBLE);
            viewHolderSecond.tvCenter.setVisibility(View.VISIBLE);
            viewHolderSecond.imgCenter.setVisibility(View.VISIBLE);
        }

        if (iRight < iCount) {
            viewHolderSecond.linearRight = viewHolderSecond.listLayouts[iRight];
            viewHolderSecond.tvRight = viewHolderSecond.listTextViews[iRight];
            viewHolderSecond.imgRight = viewHolderSecond.listImages[iRight];
            goodsClassThird = list.get(iRight);
            viewHolderSecond.tvRight.setText(goodsClassThird.getName());
			/*mActivity.displayImage(goodsClassThird.getUrl(),
					viewHolderSecond.imgRight);*/
            viewHolderSecond.imgRight.setImageURI(Uri.parse(goodsClassThird.getUrl()));
            viewHolderSecond.linearRight
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            if (null != onClick) {
                                onClick.onClick(iRight, iParentPosition);
                            }
                        }
                    });
            viewHolderSecond.linearRight.setVisibility(View.VISIBLE);
            viewHolderSecond.tvRight.setVisibility(View.VISIBLE);
            viewHolderSecond.imgRight.setVisibility(View.VISIBLE);
        }
    }
}
