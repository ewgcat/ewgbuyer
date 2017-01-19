package com.ewgvip.buyer.android.utils;

/**
 * Created by lixiaoyang on 12/17/15.
 */
public class FastDoubleClickTools {
    private static long lastClickTime;

    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return false;
        }
        lastClickTime = time;
        return true;
    }
}
