/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ewgvip.buyer.android.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.pulltorefresh.ILoadingLayout;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

    static final String LOG_TAG = "PullToRefresh-LoadingLayout";

    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    protected final View refresh_img;
    protected final Mode mMode;
    protected final Orientation mScrollDirection;
    private FrameLayout mInnerLayout;
    private boolean mUseIntrinsicAnimation;
    private CharSequence mPullLabel;
    private CharSequence mRefreshingLabel;
    private CharSequence mReleaseLabel;

    public LoadingLayout(Context context, final Mode mode, final Orientation scrollDirection, TypedArray attrs) {
        super(context);
        mMode = mode;
        mScrollDirection = scrollDirection;

        switch (scrollDirection) {
            case HORIZONTAL:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_horizontal, this);
                break;
            case VERTICAL:
            default:
                LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header_vertical, this);
                break;
        }

        mInnerLayout = (FrameLayout) findViewById(R.id.fl_inner);
        refresh_img = mInnerLayout.findViewById(R.id.refresh_img);


        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mInnerLayout.getLayoutParams();

        switch (mode) {
            case PULL_FROM_END:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.TOP : Gravity.LEFT;

                // Load in labels

                break;

            case PULL_FROM_START:
            default:
                lp.gravity = scrollDirection == Orientation.VERTICAL ? Gravity.BOTTOM : Gravity.RIGHT;

                // Load in labels
                mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
                mRefreshingLabel = context.getString(R.string.pull_to_refresh_refreshing_label);
                mReleaseLabel = context.getString(R.string.pull_to_refresh_release_label);
                break;
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderBackground)) {
            Drawable background = attrs.getDrawable(R.styleable.PullToRefresh_ptrHeaderBackground);
            if (null != background) {
                ViewCompat.setBackground(this, background);
            }
        }

        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrHeaderTextAppearance, styleID);
            setTextAppearance(styleID.data);
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance)) {
            TypedValue styleID = new TypedValue();
            attrs.getValue(R.styleable.PullToRefresh_ptrSubHeaderTextAppearance, styleID);
            setSubTextAppearance(styleID.data);
        }

        // Text Color attrs need to be set after TextAppearance attrs
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderTextColor);
            if (null != colors) {
                setTextColor(colors);
            }
        }
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrHeaderSubTextColor)) {
            ColorStateList colors = attrs.getColorStateList(R.styleable.PullToRefresh_ptrHeaderSubTextColor);
            if (null != colors) {
                setSubTextColor(colors);
            }
        }

        // Try and get defined drawable from Attrs
        Drawable imageDrawable = null;
        if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawable)) {
            imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawable);
        }

        // Check Specific Drawable from Attrs, these overrite the generic
        // drawable attr above
        switch (mode) {
            case PULL_FROM_START:
            default:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableStart)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableStart);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableTop)) {
                    Utils.warnDeprecation("ptrDrawableTop", "ptrDrawableStart");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableTop);
                }
                break;

            case PULL_FROM_END:
                if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableEnd)) {
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableEnd);
                } else if (attrs.hasValue(R.styleable.PullToRefresh_ptrDrawableBottom)) {
                    Utils.warnDeprecation("ptrDrawableBottom", "ptrDrawableEnd");
                    imageDrawable = attrs.getDrawable(R.styleable.PullToRefresh_ptrDrawableBottom);
                }
                break;
        }

        // If we don't have a user defined drawable, load the default
        if (null == imageDrawable) {
            imageDrawable = context.getResources().getDrawable(getDefaultDrawableResId());
        }

        // Set Drawable, and save width/height
        setLoadingDrawable(imageDrawable);

        reset();
    }

    public final void setHeight(int height) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.height = height;
        requestLayout();
    }

    public final void setWidth(int width) {
        ViewGroup.LayoutParams lp = getLayoutParams();
        lp.width = width;
        requestLayout();
    }

    public final int getContentSize() {
        switch (mScrollDirection) {
            case HORIZONTAL:
                return mInnerLayout.getWidth();
            case VERTICAL:
            default:
                return mInnerLayout.getHeight();
        }
    }

    public final void hideAllViews() {
        if (View.VISIBLE == refresh_img.getVisibility()) {
            refresh_img.setVisibility(View.INVISIBLE);
        }
    }

    public final void onPull(float scaleOfLayout) {
        if (!mUseIntrinsicAnimation) {
            onPullImpl(scaleOfLayout);
        }
    }

    public final void pullToRefresh() {

        // Now call the callback
        pullToRefreshImpl();
    }

    public final void refreshing() {

        if (mUseIntrinsicAnimation) {

        } else {
            // Now call the callback
            refreshingImpl();
        }

    }

    public final void releaseToRefresh() {

        // Now call the callback
        releaseToRefreshImpl();
    }

    public final void reset() {


        if (mUseIntrinsicAnimation) {

        } else {
            // Now call the callback
            resetImpl();
        }

    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        setSubHeaderText(label);
    }

    public final void setLoadingDrawable(Drawable imageDrawable) {
        // Set Drawable

        mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

        // Now call the callback
        onLoadingDrawableSet(imageDrawable);
    }

    public void setPullLabel(CharSequence pullLabel) {
        mPullLabel = pullLabel;
    }

    public void setRefreshingLabel(CharSequence refreshingLabel) {
        mRefreshingLabel = refreshingLabel;
    }

    public void setReleaseLabel(CharSequence releaseLabel) {
        mReleaseLabel = releaseLabel;
    }

    @Override
    public void setTextTypeface(Typeface tf) {
    }

    public final void showInvisibleViews() {
        if (View.INVISIBLE == refresh_img.getVisibility()) {
            refresh_img.setVisibility(View.VISIBLE);
        }

    }

    /**
     * Callbacks for derivative Layouts
     */

    protected abstract int getDefaultDrawableResId();

    protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

    protected abstract void onPullImpl(float scaleOfLayout);

    protected abstract void pullToRefreshImpl();

    protected abstract void refreshingImpl();

    protected abstract void releaseToRefreshImpl();

    protected abstract void resetImpl();

    private void setSubHeaderText(CharSequence label) {

    }

    private void setSubTextAppearance(int value) {

    }

    private void setSubTextColor(ColorStateList color) {

    }

    private void setTextAppearance(int value) {

    }

    private void setTextColor(ColorStateList color) {

    }

}
