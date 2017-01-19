package com.ewgvip.buyer.android.utils;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;

/**
 * 店铺首页滚动事件监听器
 */
public interface ScrollListener {
    boolean setOnTouchListener(View view, MotionEvent motionEvent, LinearLayoutManager mLayoutManager);
    void setScrollListener(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager mLayoutManager);
    void setScrollListener(RecyclerView recyclerView, int dx, int dy ,Fragment fragment, LinearLayoutManager mLayoutManager, boolean isLoadingMore, String orderBy, String orderType);
}
