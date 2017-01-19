package com.ewgvip.buyer.android;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:42
 * Description:  项目application,只负责初始化
 */
public class MyApplication extends MultiDexApplication {

    public void onCreate() {
        Context context = getApplicationContext();
        Fresco.initialize(context);
        CrashReport.initCrashReport(context, "02c6f41f69", false);
        super.onCreate();


    }


}
