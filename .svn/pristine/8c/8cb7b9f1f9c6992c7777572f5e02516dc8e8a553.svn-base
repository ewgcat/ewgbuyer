package com.ewgvip.buyer.android.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.dialog.DownloadDialog;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.Map;

/**
 * 欢迎页面，所有的初始化工作全在这里进行;向服务器请求首页的数据，有启动广告则显示广告
 */
public class WelcomeFragment extends Fragment {

    private static final int GOTO_MAIN_ACTIVITY = 0;
    private static final int GOTO_AD = 1;
    private static final int DOWN_APK = 2;
    private static final int DOWN_APK_COMPLETED = 3;

    private MainActivity mActivity;
    private View rootView;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case GOTO_MAIN_ACTIVITY:

                    mActivity.go_index();
                    mActivity.go_index_navigator();

                    final WindowManager.LayoutParams attrs = mActivity.getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mActivity.getWindow().setAttributes(attrs);
                    mActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
                    break;
                case GOTO_AD:
                    Bundle bundle = msg.getData();
                    String img_url = bundle.getString("img_url");
                    SimpleDraweeView simpleDraweeView = (SimpleDraweeView) rootView.findViewById(R.id.img);
                    BaseActivity.displayImage(img_url, simpleDraweeView);
                    mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 3000);
                    break;
                case DOWN_APK:
                    //显示下载进度条
                    int size = msg.arg1;
                    int downSize = msg.arg2;
                    int progress = 100 * downSize / size;

                    DecimalFormat fnum = new DecimalFormat("##0.00");
                    String currentSize = fnum.format((float) downSize / 1024 / 1024);
                    String totalSize = fnum.format((float) size / 1024 / 1024);
                    String precent = fnum.format((float) 100 * downSize / size);
                    downloadDialog.show();
                    downloadDialog.setProgress(progress, currentSize + "M" + "/" + totalSize + "M  " + precent + "%");

                    break;
                case DOWN_APK_COMPLETED:
                    //下载完成，隐藏进度条
                    downloadDialog.setProgress(100, "下载完成");
                    downloadDialog.dismiss();


                    //下载完成，安装
                    CommonUtil.installApkFromLocalPath(mActivity, apkname);
                    break;
                default:
                    break;
            }
        }

    };
    private DownloadDialog downloadDialog;
    private SharedPreferences preferences;
    SharedPreferences.Editor editor;
    String apkname;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_welcome, container, false);
        mActivity = (MainActivity) getActivity();
        downloadDialog = new DownloadDialog(mActivity);
        downloadDialog.setCancelable(false);
        //false 弹框外点击无效
        downloadDialog.setCanceledOnTouchOutside(false);
        preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor = preferences.edit();

        initUpdateApp();
        initData();
        return rootView;
    }


    //版本更新
    private void initUpdateApp() {
        final String currentVersionName = CommonUtil.getAppVersionName(mActivity);

        /*
         *app/vjson.htm
         *type 是否强制更新1更新0不更新2建议更新
         *vjson 版本号
         *remark 说明
         *apk_path app下载路径
         *是否强制 1强制更新,0不强制更新 2没有更新
         * */
        Map paraMap = mActivity.getParaMap();
        paraMap.put("type", "android");
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/vjson.htm", jsonObject -> {
            try {
                String s = jsonObject.toString();
                if (!s.contains("type") || !s.contains("vjson") || !s.contains("remark") || !s.contains("apk_path")) {
                    return;
                }
                String type = jsonObject.get("type").toString();
                final String versionName = jsonObject.get("vjson").toString();
                String remark = jsonObject.get("remark").toString();
                final String apk_path = jsonObject.get("apk_path").toString();

                //获得当前版本
                boolean isNewVersion = CommonUtil.checkNewVersionApp(currentVersionName, versionName);
                if (!isNewVersion) {
                    return;
                }
                if (TextUtils.isEmpty(apk_path)) {
                    return;
                }
                apkname = getResources().getString(R.string.app_name) + versionName + ".apk";
                if ("1".equals(type)) {//强制更新
                    new AlertDialog.Builder(mActivity).setTitle("更新").setMessage("修复首页显示不全的问题").setPositiveButton("立即更新", (dialog, which) -> {
                        //下载-->安装
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                downloadApktoappDir(apk_path, apkname);
                            }
                        }).start();

                    }).setNegativeButton("取消", (dialog, which) -> {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mActivity.startActivity(intent);
                        Process.killProcess(Process.myPid());
                    }).create().show();

                } else if ("0".equals(type)) {//提示更新，供用户选择
                    new AlertDialog.Builder(mActivity).setTitle("版本更新").setMessage(remark).setPositiveButton("立即更新", (dialog, which) -> {
                        //下载-->安装
                        new Thread(() -> {
                            downloadApktoappDir(apk_path, apkname);
                        }).start();
                    }).setNegativeButton("暂不更新", (dialog, which) -> {
                    }).create().show();

                } else if ("2".equals(type)) {//没有更新
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Log.i("test", error.toString());
            Toast.makeText(mActivity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();

        }, paraMap);
        mRequestQueue.add(request);

    }

    //下载apk
    public void downloadApktoappDir(final String path, final String apkname) {
        new Thread(() -> {
            URL url;
            FileOutputStream fos = null;
            BufferedInputStream bis = null;
            InputStream is = null;
            try {
                url = new URL(path);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                // 获取到文件的大小
                int size = conn.getContentLength();
                is = conn.getInputStream();
                String downloadPath = Environment.getExternalStorageDirectory() + "/ewgvip/";
                File file = new File(downloadPath);
                if (!file.exists()) {
                    file.mkdirs();
                }

                fos = new FileOutputStream(downloadPath + apkname);
                bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    // 获取当前下载量
                    total += len;

                    Message msg = Message.obtain();
                    if (total < size) {
                        msg.what = 2;
                        msg.arg1 = size;
                        msg.arg2 = total;
                        mHandler.sendMessage(msg);
                    } else if (total >= size) {
                        msg.what = 3;
                        mHandler.sendMessage(msg);
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fos != null) {
                        fos.close();
                        if (bis != null) {
                            bis.close();
                            if (is != null) {
                                is.close();
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //初始化数据
    private void initData() {
        getIndexAds();
        getViewFlipperData();
        getIndexFloors();
        getIndexNav();
    }

    //app首页幻灯广告
    private void getIndexAds() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/index_ad.htm",
                result -> mActivity.setCache("indexAds", result.toString()), error -> {
                    Log.i("test", error.toString());
                    Toast.makeText(mActivity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();

                }, null);
        mRequestQueue.add(request);
    }

    //公告
    private void getViewFlipperData() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/information.htm",
                result -> mActivity.setCache("mFlipperData", result.toString()), error -> {
                    Log.i("test", error.toString());
                    Toast.makeText(mActivity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();

                }, null);
        mRequestQueue.add(request);
    }

    //导航
    private void getIndexNav() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/launch_ad.htm", jsonObject -> {
            try {
                if (jsonObject.getInt("code_nav") == 100) {
                    if (jsonObject.has("index_navigator") && jsonObject.get("index_navigator").equals("true")) {
                        for (int i = 1; i <= 8; i++) {
                            editor.putString("index_" + i, jsonObject.getString("index_" + i));
                        }
                    } else {
                        for (int i = 1; i <= 8; i++) {
                            editor.remove("index_" + i);
                        }
                    }
                    if (jsonObject.has("navigator_bar") && jsonObject.get("navigator_bar").equals("true")) {
                        for (int i = 1; i <= 5; i++) {
                            editor.putString("nav_" + i + "a", jsonObject.getString("nav_" + i + "a"));
                            editor.putString("nav_" + i + "b", jsonObject.getString("nav_" + i + "b"));
                        }
                    } else {
                        for (int i = 1; i <= 5; i++) {
                            editor.remove("nav_" + i + "a");
                            editor.remove("nav_" + i + "b");
                        }
                    }
                } else {
                    for (int i = 1; i <= 8; i++) {
                        editor.remove("index_" + i);
                    }
                    for (int i = 1; i <= 5; i++) {
                        editor.remove("nav_" + i + "a");
                        editor.remove("nav_" + i + "b");
                    }
                }
                editor.commit();
            } catch (JSONException e) {
            }
            try {
                //有广告加载广告
                if (jsonObject.getInt("code") == 100) {
                    Bundle bundle = new Bundle();
                    bundle.putString("img_url", jsonObject.getString("img_url"));
                    Message msg = new Message();
                    msg.what = GOTO_AD;
                    msg.setData(bundle);
                    //1秒后广告
                    mHandler.sendMessageDelayed(msg, 2000);
                    //无广告
                } else {
                    //1秒后首页
                    mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 2000);
                }
            } catch (JSONException e) {
                mHandler.sendEmptyMessageDelayed(GOTO_MAIN_ACTIVITY, 2000);
            }

        }, error -> {
            Log.i("test", error.toString());
            Toast.makeText(mActivity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();

        }, mActivity.getParaMap());
        mRequestQueue.add(request);
    }

    //楼层
    private void getIndexFloors() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/index_floor.htm",
                result -> mActivity.setCache("indexFloors", result.toString()), error -> {
                    Log.i("test", error.toString());
                    Toast.makeText(mActivity, "服务器更新中，请稍后再使用app", Toast.LENGTH_SHORT).show();

                    new Handler().postDelayed(() -> Process.killProcess(Process.myPid()),3000);

                }, null);
        mRequestQueue.add(request);
    }
}
