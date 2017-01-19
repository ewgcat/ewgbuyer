/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.ewgvip.buyer.android.pulltorefresh.internal;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Mode;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase.Orientation;

@SuppressLint("ViewConstructor")
public class FlipLoadingLayout extends LoadingLayout {

	static final int FLIP_ANIMATION_DURATION = 150;

	private final Animation mRotateAnimation, mResetRotateAnimation;

	public FlipLoadingLayout(Context context, final Mode mode,
			final Orientation scrollDirection, TypedArray attrs) {
		super(context, mode, scrollDirection, attrs);

		final int rotateAngle = mode == Mode.PULL_FROM_START ? -180 : 180;

		mRotateAnimation = new RotateAnimation(0, rotateAngle,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		mRotateAnimation.setFillAfter(true);

		mResetRotateAnimation = new RotateAnimation(rotateAngle, 0,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
		mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
		mResetRotateAnimation.setFillAfter(true);
	}

	@Override
	protected void onLoadingDrawableSet(Drawable imageDrawable) {
		if (null != imageDrawable) {
			final int dHeight = imageDrawable.getIntrinsicHeight();
			final int dWidth = imageDrawable.getIntrinsicWidth();

			/**
			 * We need to set the width/height of the ImageView so that it is
			 * square with each side the size of the largest drawable dimension.
			 * This is so that it doesn't clip when rotated.
			 */

		}
	}

	@Override
	protected void onPullImpl(float scaleOfLayout) {
		// NO-OP
	}

	@Override
	protected void pullToRefreshImpl() {
		// Only start reset Animation, we've previously show the rotate anim

	}

	@Override
	protected void refreshingImpl() {
	}

	@Override
	protected void releaseToRefreshImpl() {

	}

	@Override
	protected void resetImpl() {

	}

	@Override
	protected int getDefaultDrawableResId() {
		return R.mipmap.default_ptr_flip;
	}

	private float getDrawableRotationAngle() {
		float angle = 0f;
		switch (mMode) {
			case PULL_FROM_END :
				if (mScrollDirection == Orientation.HORIZONTAL) {
					angle = 90f;
				} else {
					angle = 180f;
				}
				break;

			case PULL_FROM_START :
				if (mScrollDirection == Orientation.HORIZONTAL) {
					angle = 270f;
				}
				break;

			default :
				break;
		}

		return angle;
	}

}
