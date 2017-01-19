package com.ewgvip.buyer.android.layout;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class MyViewPager extends ViewPager {

    private float xDown;// 记录手指按下时的横坐标。
    private float xMove;// 记录手指移动时的横坐标。
    private float yDown;// 记录手指按下时的纵坐标。
    private float yMove;// 记录手指移动时的纵坐标。

    public MyViewPager(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = ev.getX();
                yDown = ev.getY();
                this.getParent().requestDisallowInterceptTouchEvent(true);
                break;
            case MotionEvent.ACTION_MOVE:
                xMove = ev.getX();
                yMove = ev.getY();
                if (Math.abs(xMove - xDown) > Math.abs(yMove - yDown)) {
                    this.getParent().requestDisallowInterceptTouchEvent(true);
                } else {
                    this.getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                this.getParent().requestDisallowInterceptTouchEvent(false);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }
}