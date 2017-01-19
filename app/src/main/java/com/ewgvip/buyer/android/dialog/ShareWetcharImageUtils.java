package com.ewgvip.buyer.android.dialog;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Administrator on 2016/8/6.
 */
public class ShareWetcharImageUtils {

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 50, output);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到output中

        int options = 100;
        while (output.toByteArray().length / 1024 > 30) { // 循环判断如果压缩后图片是否大于(maxkb)30kb,大于继续压缩
            output.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            bmp.compress(Bitmap.CompressFormat.JPEG, options, output);// 这里压缩options%，把压缩后的数据存放到baos中
        }

        if (needRecycle) {
            bmp.recycle();
        }
        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static Bitmap getBitmapFromURL(String urlPath) {
        InputStream is = null;
        HttpURLConnection conn = null;
        Bitmap bitmap=null;
        try {
            URL url = new URL(urlPath);
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true);
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(6000);
            is = conn.getInputStream();
            if (conn.getResponseCode() == 200) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;

                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                byte[] result = baos.toByteArray();
                bitmap= BitmapFactory.decodeByteArray(result, 0, result.length);
                return bitmap;
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.disconnect();
        }
        return bitmap;
    }

    public static String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }



}
