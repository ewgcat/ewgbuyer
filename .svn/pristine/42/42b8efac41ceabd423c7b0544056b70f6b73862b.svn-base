package com.ewgvip.buyer.android.receiver;

/**
 * Created by Administrator on 2016/8/13.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.ewgvip.buyer.android.utils.CommonUtil;

import java.io.File;


public class InitApkBroadCastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String downloadPath = Environment.getExternalStorageDirectory() + "/ewgvip/";
            final File file = new File(downloadPath);
            CommonUtil.delete(file);
            Log.i("test", "ACTION_PACKAGE_ADDED");
        }

        if (Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) {
            Log.i("test", "ACTION_PACKAGE_REMOVED");
        }

        if (Intent.ACTION_PACKAGE_REPLACED.equals(intent.getAction())) {
            Log.i("test","ACTION_PACKAGE_REPLACED");
            String downloadPath = Environment.getExternalStorageDirectory() + "/ewgvip/";
            final File file = new File(downloadPath);
            CommonUtil.delete(file);
        }
    }

}
