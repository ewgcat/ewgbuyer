package com.ewgvip.buyer.android.activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.fragment.*;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;
import com.ewgvip.buyer.android.volley.VolleyError;
import com.ewgvip.buyer.android.volley.toolbox.Volley;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.tauth.Tencent;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static android.text.TextUtils.isEmpty;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:22
 * Description:  所有的activity的基类,
 * 包括:页面跳转的方法,
 * 工具方法,
 * 没有自己的生命周期
 */
public class BaseActivity extends AppCompatActivity {

    public static String DEVICE_ID = null;
    private static String uuid;
    public Tencent mTencent;
    public IWXAPI mwxapi;
    private SharedPreferences preferences;
    private AlertDialog pd;// 加载中遮罩
    /**
     * 处理遮罩对话框的隐藏
     */
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    if (pd != null && pd.isShowing()) {
                        hideProcessDialog(1);
                    }
                    break;

                default:
                    break;
            }
        }
    };


/**
 * 工具方法区start
 */

    /**
     * 获取设备id
     *
     * @param context
     * @return
     */
    public static String getDeviceID(Context context) {
        if (DEVICE_ID == null) {
            DEVICE_ID = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        //Log.i("test","读取设备DEVICE_ID=="+DEVICE_ID);
        return DEVICE_ID;

    }


    /**
     * deviceID的组成为：渠道标志+识别符来源标志+hash后的终端识别符
     * <p>
     * 渠道标志为：
     * 1，andriod（a）
     * <p>
     * 识别符来源标志：
     * 1， wifi mac地址（wifi）；
     * 2， IMEI（imei）；
     * 3， 序列号（sn）；
     * 4， id：随机码。若前面的都取不到时，则随机生成一个随机码，需要缓存。
     *
     * @param context
     * @return
     */
    public static String getDeviceId(Context context) {

        StringBuilder deviceId = new StringBuilder();
        // 渠道标志
        deviceId.append("a");
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!isEmpty(wifiMac)) {
                deviceId.append("wifi");
                deviceId.append(wifiMac);
                // Log.i("test", "deviceId=="+deviceId.toString());
                return deviceId.toString();
            }
            //IMEI（imei）
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            if (!isEmpty(imei)) {
                deviceId.append("imei");
                deviceId.append(imei);
                // Log.i("test", "deviceId=="+deviceId.toString());
                return deviceId.toString();
            }
            //序列号（sn）
            String sn = tm.getSimSerialNumber();
            if (!isEmpty(sn)) {
                deviceId.append("sn");
                deviceId.append(sn);
                //Log.i("test", "deviceId=="+deviceId.toString());
                return deviceId.toString();
            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = getUUID(context);
            if (!isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                //Log.i("test", "deviceId=="+deviceId.toString());
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(getUUID(context));
        }
        //Log.i("test", "deviceId=="+deviceId.toString());
        return deviceId.toString();
    }

    /**
     * 得到全局唯一UUID
     */
    public static String getUUID(Context context) {
        SharedPreferences mShare = context.getSharedPreferences("sysCacheMap", Context.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        Log.i("test", "UUID==" + uuid);
        return uuid;
    }


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 显示图片
     *
     * @param url 图片url
     * @param img 显示图片的控件
     */
    public static void displayImage(String url, SimpleDraweeView img) {
        Uri uri = Uri.parse(url);

        int width = 0;
        int height = 0;
        ViewGroup.LayoutParams layoutParams = img.getLayoutParams();
        if (layoutParams != null) {
            width = layoutParams.width;
            height = layoutParams.height;
        }
        if (width > 0 && height > 0) {
            ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(new ResizeOptions(width, height))
                    .build();
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(uri)
                    .setTapToRetryEnabled(true)
                    .setOldController(img.getController())
                    .setImageRequest(request).setAutoPlayAnimations(true)
                    .build();
            img.setController(controller);
        } else {
            img.setImageURI(uri);
        }

    }

    /**
     * MD5加密方法
     *
     * @param s
     * @return
     */
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 推送的处理
     *
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        Bundle bundle = intent.getExtras();
        do_push(bundle);
    }

    /**
     * 根据推送的内容,做不同处理
     *
     * @param bundle
     */
    public void do_push(Bundle bundle) {
        if (bundle != null && bundle.containsKey("action_type")) {
            View bottom_tab = findViewById(R.id.index_bottom);
            bottom_tab.setVisibility(View.GONE);
            String action_type = bundle.getString("action_type");
            String action_value = bundle.getString("action_value");
            if (action_type.equals("web")) {
                bundle.putString("url", action_value);
                go_web(bundle);
            } else if (action_type.equals("order")) {
                FragmentTransaction fragmentTransaction = getFragmentManager()
                        .beginTransaction();
                Fragment fragment = new OrderAllTabFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putInt("index", 3);
                fragment.setArguments(bundle1);
                fragmentTransaction.hide(getCurrentfragment());
                fragmentTransaction.add(R.id.index_top, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                OrderAllTabFragment.REFRESH = true;
                go_order_normal(action_value, fragment, 3);
            } else if (action_type.equals("goods")) {
                go_goods(action_value);
            }
        }
    }

    /**
     * post方式发送请求，paraMap为参数map,形式为{'id':5,'type':1},当paraMap为null时表示没有参数
     */
    public String sendPost(String url, Map paraMap) throws IOException {
        HttpPost httpRequest = null;
        List<NameValuePair> params = null;
        HttpResponse httpResponse;
        String result = "";
        /* 建立HttpPost连接 */
        httpRequest = new HttpPost(getAddress() + url);
        SharedPreferences preferences = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String verify = preferences.getString("verify", "");
        httpRequest.setHeader("verify", verify);// 设置头文件
        /* Post运作传送变数必须用NameValuePair[]阵列储存 */
        params = new ArrayList<NameValuePair>();
        if (paraMap != null) {
            Iterator it = paraMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pairs = (Map.Entry) it.next();
                String val = "";
                if (pairs.getValue() != null) {
                    val = pairs.getValue().toString();
                }
                params.add(new BasicNameValuePair(pairs.getKey().toString(),
                        val));
            }
        }
        try {
            // 发出HTTP request
            httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
            // 取得HTTP response
            httpResponse = new DefaultHttpClient().execute(httpRequest);
            // 若状态码为200
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                // 取出回应字串
                result = EntityUtils.toString(httpResponse.getEntity());
            }
        } catch (Exception e) {
            result = "Exception";
        }
        return result;
    }


    /**
     * 页面切换时遮罩对话框
     */
    public void showProcessDialog() {
        if (pd == null || !pd.isShowing()) {
            pd = new AlertDialog.Builder(this, R.style.dialog_translucent).create();
            pd.setCancelable(false);
            pd.show();

            View view = getLayoutInflater().inflate(R.layout.dialog_loading, null);
            pd.setContentView(view);

            Window mWindow = pd.getWindow();
            WindowManager.LayoutParams lp = mWindow.getAttributes();

            mWindow.setGravity(Gravity.CENTER);
            mWindow.setAttributes(lp);
            mHandler.sendEmptyMessageDelayed(1, 10000);
        }

    }

    /**
     * 关闭遮罩
     *
     * @param flag
     */
    public void hideProcessDialog(int flag) {
        if (pd != null && pd.isShowing()) {
            if (flag == 0) {
                mHandler.removeMessages(1);
                pd.dismiss();
            } else if (flag == 1) {
                //Toast.makeText(BaseActivity.this, "网络请求失败,请检查您的网络设置", Toast.LENGTH_SHORT).show();
                pd.dismiss();
            }
        }
    }


    /**
     * 设置缓存
     *
     * @param key
     * @param value
     */
    public void setCache(String key, String value) {
        if (preferences == null) {
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public String getCache(String key) {
        if (preferences == null) {
            preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        return preferences.contains(key) ? preferences.getString(key, "") : "";
    }


    /**
     * 获取屏幕宽度
     *
     * @return
     */
    public int getScreenWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        return screenW;
    }

    /**
     * 判断是否登录
     *
     * @return
     */
    public boolean islogin() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        if (!user_id.equals("") && !token.equals("")) {
            return true;
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            if (preferences.contains("inventory_ids")) {
                editor.remove("inventory_ids");
            }
            return false;
        }
    }


    /**
     * 检测sd卡
     *
     * @return
     */
    public boolean hasSdcard() {

        String status = Environment.getExternalStorageState();

        return status.equals(Environment.MEDIA_MOUNTED);

    }

    /**
     * 常用的参数map
     *
     * @return
     */
    public Map getParaMap() {
        SharedPreferences preferences = getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        //放在请求头中
        String verify = preferences.getString("verify", "");
        String device_id = getDeviceID(this);
        if (device_id == null) {
            device_id = getDeviceId(this);
        }
        Map paraMap = new HashMap();
        paraMap.put("device_type", "Android");
        if (device_id != null && !device_id.equals("")) {
            paraMap.put("device_id", device_id);
        }
        paraMap.put("user_id", user_id);
        paraMap.put("token", token);
        paraMap.put("verify", verify);
        return paraMap;
    }


    /**
     * 隐藏键盘
     *
     * @param v
     */
    public void hide_keyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }

    /**
     * 上次点击返回键的时间
     */
    private long lastBackPressed;
    /**
     * 两次点击的间隔时间
     */
    private static final int QUIT_INTERVAL = 2000;

    /**
     * 返回键点击事件,左上角返回事件
     */
    @Override
    public void onBackPressed() {
        android.app.Fragment fragment = getCurrentfragment();
        if (fragment != null && fragment instanceof GoodsContainerFragment) {
            GoodsContainerFragment f = (GoodsContainerFragment) fragment;
            int i = f.get_index();
            if (i > 0)
                f.go_index();
            else
                my_back_pressed();
        } else if (SuccessFragment.flag) {
            if (this instanceof BaseActivity) {
                FragmentManager fragmentManager = getFragmentManager();
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 0) {
                    go_index();
                }
            }
            SuccessFragment.flag = false;
        } else {
            long backPressed = System.currentTimeMillis();
            if (backPressed - lastBackPressed > QUIT_INTERVAL) {
                lastBackPressed = backPressed;
                my_back_pressed();
            } else {
                finish();
                System.exit(0);
            }

        }


    }

    /**
     * 自定义的返回键处理
     */
    public void my_back_pressed() {
        FragmentManager fragmentManager = getFragmentManager();
        int count = fragmentManager.getBackStackEntryCount();
        if (count == 0) {
            if (this instanceof MainActivity && !(getCurrentfragment() instanceof IndexFragment)) {
                go_index();
            } else {
                if (isDestroyed()) {
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                } else {
                    super.onBackPressed();
                }
            }

        } else {
            fragmentManager.popBackStack();
            if (count == 1 && this instanceof MainActivity) {
                findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                hideAllIndex(fragmentTransaction);
                fragmentTransaction.commit();
                IndexNavigatiorFragment.getInstance().set_index_top();
            }
        }
    }

    /**
     * 工具方法区end
     */

    /**
     * 页面跳转区start
     */

    /**
     * 获取当前显示的fragment
     *
     * @return
     */
    public Fragment getCurrentfragment() {
        Fragment current_fragment = getFragmentManager().findFragmentById(R.id.index_top);
        if (current_fragment != null && current_fragment.isHidden()) {
            //首页Fragment显示
            Fragment fragment = IndexFragment.getInstance();
            if (!fragment.isHidden()) {
                current_fragment = fragment;
            } else {
                //商品分类Fragment显示
                fragment = GoodsClassFragment.getInstance();
                if (!fragment.isHidden()) {
                    current_fragment = fragment;
                } else {
                    //购物车Fragment显示
                    fragment = Cart1Fragment.getInstance();
                    if (!fragment.isHidden()) {
                        current_fragment = fragment;
                    } else {
                        //订单Fragment显示
                        fragment = OrderAllTabFragment2.getInstance();

                        if (!fragment.isHidden()) {
                            current_fragment = fragment;
                        } else {
                            //用户中心Fragment显示
                            fragment = UserCenterFragment.getInstance();
                            if (!fragment.isHidden()) {
                                current_fragment = fragment;
                            }
                        }
                    }
                }
            }

        }
        return current_fragment;
    }


    /**
     * 首页导航切换页面时的特殊处理
     *
     * @param fragmentTransaction
     */
    public void hideAllIndex(FragmentTransaction fragmentTransaction) {
        Fragment fragment = IndexFragment.getInstance();
        if (fragment.isAdded())
            fragmentTransaction.hide(fragment);
        fragment = GoodsClassFragment.getInstance();
        if (fragment.isAdded())
            fragmentTransaction.hide(fragment);
        fragment = Cart1Fragment.getInstance();
        if (fragment.isAdded())
            fragmentTransaction.hide(fragment);
        fragment = OrderAllTabFragment2.getInstance();
        if (fragment.isAdded())
            fragmentTransaction.hide(fragment);
        fragment = UserCenterFragment.getInstance();
        if (fragment.isAdded())
            fragmentTransaction.hide(fragment);
    }


    //启动页面
    public void go_welcome() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new WelcomeFragment();
        fragmentTransaction.replace(R.id.index_top, fragment);
        fragmentTransaction.commit();
    }


    //跳转首页
    public void go_index() {

        if (this instanceof MainActivity) {
            FragmentManager fragmentManager = getFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                for (int i = 0; i < count; i++)
                    fragmentManager.popBackStack();
                IndexNavigatiorFragment.getInstance().set_index();
            }
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = IndexFragment.getInstance();
            if (fragment.isAdded()) {
                findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
                hideAllIndex(fragmentTransaction);
                fragmentTransaction.show(fragment);
                IndexNavigatiorFragment.getInstance().set_index();
            } else {
                fragmentTransaction.replace(R.id.index_top, fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("action", MainActivity.INDEX);
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    //跳转首页
    public void go_usercenterfromLevelUpVip() {

        if (this instanceof MainActivity) {
            FragmentManager fragmentManager = getFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                for (int i = 0; i < count; i++)
                    fragmentManager.popBackStack();
                IndexNavigatiorFragment.getInstance().set_index();
            }
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = UserCenterFragment.getInstance();
            if (fragment.isAdded()) {
                findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
                hideAllIndex(fragmentTransaction);
                fragmentTransaction.show(fragment);
                IndexNavigatiorFragment.getInstance().set_user_center();
            } else {
                fragmentTransaction.replace(R.id.index_top, fragment);
            }
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("action", "usercenter");
            intent.putExtras(bundle);
            startActivity(intent);
        }


    }

    //跳转商品分类页
    public void go_gc() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = GoodsClassFragment.getInstance();
        hideAllIndex(fragmentTransaction);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.index_top, fragment);
        }
        fragmentTransaction.commit();
    }

    //跳转购物车页
    public void go_cart1() {
        if (this instanceof MainActivity) {
            FragmentManager fragmentManager = getFragmentManager();
            int count = fragmentManager.getBackStackEntryCount();
            if (count > 0) {
                for (int i = 0; i < count; i++)
                    fragmentManager.popBackStack();
                IndexNavigatiorFragment.getInstance().set_cart();
            }
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = Cart1Fragment.getInstance();
            if (fragment.isAdded()) {
                findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
                hideAllIndex(fragmentTransaction);
                fragmentTransaction.show(fragment);
                IndexNavigatiorFragment.getInstance().set_cart();
            } else {
                fragmentTransaction.add(R.id.index_top, fragment);
            }
            fragmentTransaction.commit();
        } else {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("action", "cart");
            intent.putExtras(bundle);
            startActivity(intent);
        }

    }

    //跳转品牌列表
    public void go_gb() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        BrandFragment fragment = new BrandFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转用户中心
    public void go_usercenter() {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = UserCenterFragment.getInstance();
        hideAllIndex(fragmentTransaction);
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.index_top, fragment);
        }
        fragmentTransaction.commit();
    }

    // 底部点击跳转到订单列表
    public void go_orders_all() {
        if (islogin()) {
            if (this instanceof MainActivity) {
                FragmentManager fragmentManager = getFragmentManager();
                int count = fragmentManager.getBackStackEntryCount();
                if (count > 0) {
                    for (int i = 0; i < count; i++)
                        fragmentManager.popBackStack();
                    IndexNavigatiorFragment.getInstance().set_order();
                }
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                Fragment fragment = OrderAllTabFragment2.getInstance();
                if (fragment.isAdded()) {
                    findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
                    hideAllIndex(fragmentTransaction);
                    fragmentTransaction.show(fragment);
                    IndexNavigatiorFragment.getInstance().set_order();
                } else {
                    fragmentTransaction.add(R.id.index_top, fragment);
                }
                fragmentTransaction.commit();
            } else {
                Intent intent = new Intent();
                intent.setClass(this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("action", "order");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        } else {
            IndexNavigatiorFragment.getInstance().set_user_center();
            go_login();
        }
    }

    //跳转促销活动列表
    public void go_activity_list() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new ActivityIndexFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //商品详情页
    public void go_goods(String id) {
        if (this instanceof MainActivity) {
            if (getCurrentfragment() instanceof GoodsContainerFragment) {
            } else {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                Bundle bundle = new Bundle();
                bundle.putString("goods_id", id);
                Fragment goodsFragment = new GoodsContainerFragment();
                goodsFragment.setArguments(bundle);
                fragmentTransaction.hide(getCurrentfragment());
                fragmentTransaction.add(R.id.index_top, goodsFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commitAllowingStateLoss();
            }
        } else {
            Intent intent = new Intent();
            intent.setClass(this, MainActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("goods_id", id);
            bundle.putString("action", "DETAIL");
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    //店铺详情页
    public void go_store(String id) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("store_id", id);
        Fragment goodsFragment = new StoreIndexFragment();
        goodsFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, goodsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转登录页
    public void go_login() {
        startActivity(new Intent(BaseActivity.this, LoginActivity.class));
    }

    //跳转注册页
    public void go_regist(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new RegistFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转我的关注页
    public void go_myconcern(Bundle bundle) {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new ConcernTabFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转查看物流
    //就是跳转到已发货未收货订单列表
    public void go_logistics() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new LogisticsFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转充值页面,直接使用支付宝或者微信支付来购买预存款
    public void go_recharge() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new BalanceRechargeCashFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }


    //跳转充值中心
    public void go_rechargecenter() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new BalanceRechargeCashFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }


    //跳转免费抢券
    public void go_getCoupon() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new CouponsGetIndexFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转消息列表
    public void go_message_list() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new MessageListFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转设置页
    public void go_setting() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = SettingFragment.getInstance();
        fragmentTransaction.hide(getCurrentfragment());
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.index_top, fragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //获取服务器url
    public String getAddress() {
        return CommonUtil.getAddress(getApplicationContext());
    }

    //跳转商品列表
    public void go_goodslist(String key, String value, String titleword) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(key, value);
        bundle.putString("titleword", titleword);
        GoodsListFragment goodsListFragment = new GoodsListFragment();
        goodsListFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, goodsListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转到特定品牌商品详情界面
    public void go_brand_goods(String brandinfo) {
        String gb_id = brandinfo.split("[|]")[0];
        String brandName = brandinfo.split("[|]")[1];
        //Log.i("BaseActivity",brandName+" == "+gb_id);
        go_goodslist("gb_id", gb_id + "", brandName);
    }

    //由搜索页跳转商品列表
    public void go_goodslist(String searchword) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("searchword", searchword);
        GoodsListFragment goodsListFragment = new GoodsListFragment();
        goodsListFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, goodsListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //由店铺页跳转商品列表
    public void go_store_goodslist(String store_id, String searchword) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("searchword", searchword);
        bundle.putString("store_id", store_id);
        GoodsListFragment goodsListFragment = new GoodsListFragment();
        goodsListFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, goodsListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转二维码扫描页
    public void go_scan_code() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        QRScanFragment scanCodeFragment = new QRScanFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, scanCodeFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转购物车第二步,填写订单页
    public void go_cart2(String select_ids) {

        Bundle bundle = new Bundle();
        bundle.putString("cart_ids", select_ids);
        Intent intent = new Intent(BaseActivity.this, CartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    //跳转支付
    public void go_pay(Bundle bundle, String paytype) {
        Intent intent = new Intent(BaseActivity.this, PayActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("paytype", paytype);
        startActivity(intent);
    }


    //跳转填写订单时选择优惠券
    public void go_cart_coupon(Bundle bundle, Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Cart2CouponsFragment fragment = new Cart2CouponsFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, Cart2Fragment.COUPONS);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转填写订单时商品清单列表
    public void go_cart_goods_list(Bundle bundle) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Cart2GoodsListFragment fragment = new Cart2GoodsListFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转选择支付方式和配送方式
    public void go_cart_trans_pay(Bundle bundle, Fragment target_fragment) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Cart2TransPayFragment fragment = new Cart2TransPayFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, Cart2Fragment.TRANS_AND_PAY);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转选择发票信息
    public void go_cart_invoice(Bundle bundle, Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Cart2InvoiceFragment fragment = new Cart2InvoiceFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, Cart2Fragment.INVOICE);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转零元购首页
    public void go_free_index() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new FreeTabFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //跳转零元购商品详情页
    public void go_free_goods(String id) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", id);
        FreeDetailFragment freeGoodsFragment = new FreeDetailFragment();
        freeGoodsFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, freeGoodsFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转零元购商品申请页
    public void go_free_apply(String id) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", id);
        FreeApplyFragment freeApplyFragmen = new FreeApplyFragment();
        freeApplyFragmen.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, freeApplyFragmen);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转成功页面
    public void go_success(Bundle bundle) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        SuccessFragment successFragment = new SuccessFragment();
        successFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, successFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转积分商品购物车
    public void go_integral_cart() {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        IntegralCartFragment1 integralCartFragment = new IntegralCartFragment1();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, integralCartFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转积分商品
    public void go_integral_order(String cartids, int all_integral,
                                  double all_shipfee, String goods_total_price, String order_total_price) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString("cartids", cartids);
        bundle.putInt("all_integral", all_integral);
        bundle.putDouble("all_shipfee", all_shipfee);
        bundle.putString("goods_total_price", goods_total_price);
        bundle.putString("order_total_price", order_total_price);
        IntegralCartFragment2 integralOrderFragment = new IntegralCartFragment2();
        integralOrderFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, integralOrderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转搜索页
    public void go_search() {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        searchFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, searchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转店铺搜索页
    public void go_search(String store_id) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        SearchFragment searchFragment = new SearchFragment();
        Bundle bundle = new Bundle();
        bundle.putString("store_id", store_id);
        searchFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, searchFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转零元购评价页
    public void go_free_evaluate(String id) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        FreeEvaluateListFragment freeEvaluateListFragment = new FreeEvaluateListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        freeEvaluateListFragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, freeEvaluateListFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转预存款支付页面
    public void go_balance(final Bundle bundle) {
        RequestQueue mRequestQueue = Volley.newRequestQueue(BaseActivity.this);
        Request<JSONObject> request = new NormalPostRequest(BaseActivity.this,
                getAddress() + "/app/buyer/pay_password.htm",
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject result) {
                        try {
                            int code = result.getInt("code");
                            if (code == -300) {
                                Toast.makeText(BaseActivity.this,
                                        "您还没有设置支付密码，请先设置", Toast.LENGTH_SHORT).show();
                                go_change_pay_password();
                            } else {
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                Fragment fragment;
                                fragment = new PayBalanceFragment();
                                fragment.setArguments(bundle);
                                fragmentTransaction.hide(getCurrentfragment());
                                fragmentTransaction.add(R.id.index_top, fragment);
                                fragmentTransaction.addToBackStack(null);
                                fragmentTransaction.commit();
                            }

                        } catch (Exception e) {
                        }
                        hideProcessDialog(0);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProcessDialog(1);
            }
        }, getParaMap());
        mRequestQueue.add(request);

    }

    //跳转到支付密码修改密码
    public void go_change_pay_password() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PasswordPayModifyFragment fragment = new PasswordPayModifyFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转到激活VIP界面
    public void go_activite_vip(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        VIPActivationFragment fragment = new VIPActivationFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转到登录密码修改密码
    public void go_change_password() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        PasswordLoginModifyFragment fragment = new PasswordLoginModifyFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转货到付款
    public void go_payafter(Bundle bundle) {

        Intent intent = new Intent(BaseActivity.this, PayActivity.class);
        intent.putExtras(bundle);
        intent.putExtra("paytype", "payafter");
        startActivity(intent);
    }

    //跳转生活购购物车
    public void go_group_life_cart1(final Bundle bundle) {

        if (islogin()) {
            RequestQueue mRequestQueue = Volley.newRequestQueue(BaseActivity.this);
            Request<JSONObject> request = new NormalPostRequest(
                    BaseActivity.this,
                    getAddress() + "/app/buyer/hasphone.htm",
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject result) {

                            try {
                                int code = result.getInt("code");
                                if (code == 100) {
                                    String mobile = result.getString("mobile");
                                    bundle.putString("mobile", mobile);
                                    go_pay(bundle, "group");
                                } else {
                                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                    Fragment fragment;
                                    fragment = new BoundPhoneFragment();

                                    fragment.setArguments(bundle);
                                    fragmentTransaction.hide(getCurrentfragment());
                                    fragmentTransaction.add(R.id.index_top,
                                            fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }

                            } catch (Exception e) {
                            }

                            hideProcessDialog(0);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    hideProcessDialog(1);
                }
            }, getParaMap());
            mRequestQueue.add(request);

        } else {
            go_login();
        }
    }

    //跳转绑定手机
    public void go_bound_phone() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new BoundPhoneFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转生活购订单列表
    public void go_order_group_list() {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            OrderGrouplifeListFragment orderGrouplifeListFragment = new OrderGrouplifeListFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, orderGrouplifeListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转积分订单列表
    public void go_order_integral_list() {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            OrderIntegralListFragment orderIntegralListFragment = new OrderIntegralListFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, orderIntegralListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转零元购订单列表
    public void go_order_free_list() {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            OrderFreeListFragment orderFreeListFragment = new OrderFreeListFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, orderFreeListFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //退货列表
    public void go_order_goods_return_list() {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            OrderReturnTabFragment fragment = new OrderReturnTabFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }


    //跳转订单详情页
    public void go_order_normal(String id, Fragment target_fragment, int current) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderNormalDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putInt("current", current);
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, OrderListFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    //跳转生活购订单详情
    public void go_order_life(String id) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderGrouplifeDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转商品退货申请第一步
    public void go_goods_return_apply(Bundle bundle, Fragment target_fragment) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        OrderGoodsReturn1Fragment fragment = new OrderGoodsReturn1Fragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 2);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转填写退货物流
    public void go_goods_return_trans(Bundle bundle, Fragment target_fragment) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        OrderGoodsReturn2Fragment fragment = new OrderGoodsReturn2Fragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 1);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转生活购退款页
    public void go_grouplife_return_apply(Bundle bundle, Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderGrouplifeReturnFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 0);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转零元购详情
    public void go_order_free_detail(Bundle bundle) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderFreeDetailFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转积分订单详情
    public void go_order_integral_detail(Bundle bundle) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderIntegralDetailFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转商品组合选择页面
    public void go_goods_combine(Bundle bundle) {


        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new GoodsCombineFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转聊天页面
    public void go_chat(Bundle bundle) {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new ChatFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());

            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }

    }

    //预存款管理页面
    public void go_balance_detail() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new BalanceFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //余额充值
    public void go_balance_recharge(BalanceFragment balanceFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new BalanceRechargeCardFragment();
        fragment.setTargetFragment(balanceFragment, 0);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //提现
    public void go_balance_getCash(Bundle bundle, BalanceFragment balanceFragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new BalanceWithdrawFragment();
        fragment.setTargetFragment(balanceFragment, 0);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //余额详情页
    public void go_balance_billingdetails(Bundle bundle) {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new BalanceDetailListFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());

            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转二维码登录确认页面
    public void go_qr_login(String qr_session_id) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new QRLoginFragment();
        Bundle bundle = new Bundle();
        bundle.putString("qr_id", qr_session_id);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 跳转到优惠券领取页面
     */
    public void go_commit_coupon(Bundle bundle, Fragment target_fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new CouponsGetFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 0);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * 跳转到优惠券领取成功推荐优惠券可以购买的商品
     */
    public void go_success_coupon(Bundle bundle, Fragment target_fragment) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new CouponsGetSuccessFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 0);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * 跳转到积分明细
     */
    public void go_integral_detail() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new IntegralDetailListFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转积分商城首页
    public void go_integral_index() {

        if (islogin()) {

            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            IntegralIndexFragment fragment = new IntegralIndexFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转积分商品页
    public void go_integral_goods(String ig_id) {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new IntegralGoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putString("goods_id", ig_id);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //店铺首页
    public void go_store_index(Bundle bundle) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new StoreIndexFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //查询待评价订单
    public void go_order2evaluate() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new OrderEvaluateTabFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //团购
    public void go_group_shopping() {
        Intent intent = new Intent(BaseActivity.this, GroupShoppingActivity.class);
        intent.putExtra("flag", 1);
        startActivity(intent);
    }

    /**
     * 跳转到用户足迹商品的展示列表
     */
    public void go_footpoint() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new FootprintsFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 跳转到用户信息的修改页面
     */
    public void go_user_center_information_change() {

        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new UCInformationModify();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }


    //跳转到促销内容页
    public void go_salespm(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new ActivityGoodsListFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转web页
    public void go_web(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new WebFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转web页
    public void go_web2(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new WebFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 跳转到足迹商品相似商品页面
     */
    public void go_foot_point_same_like(Bundle bundle) {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        Fragment fragment = new SimilarGoodsFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    /**
     * 跳转到积分商城签到
     */
    public void go_sign_integral_goods() {
        if (islogin()) {

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            Fragment fragment = new IntegralSignFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //定位
    public void go_maplocation(Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new GroupCityChooseFragment();
        fragment.setTargetFragment(target_fragment, GroupShoppingFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.index_top, fragment);
        }
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commitAllowingStateLoss();
    }

    //订单列表按状态展示
    public void go_orders_all(Bundle bundle) {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new OrderAllTabFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //安全中心
    public void go_account_security() {
        if (islogin()) {
            startActivity(new Intent(this, AccountSecurityActivity.class));
        } else {
            go_login();
        }
    }

    /**
     * 跳转到商品订单配送时间和自已地点选择页面
     */
    public void go_goods_extract_address_select(Bundle bundle, Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Cart2PickupSiteFragment fragment = new Cart2PickupSiteFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //跳转订单评价页面
    public void go_order_evaluate(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderEvaluateFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, OrderListFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转选择晒单图片页面
    public void go_order_evaluate_photo(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new PhotoUploadFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, OrderEvaluateFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());

        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转选择聊天图片页面
    public void go_select_photo_for_chat(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new PhotoUploadFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, ChatFragment.CHAT_PHOTO);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * 跳转到优惠券页面
     */
    public void go_coupons() {
        if (islogin()) {
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            Fragment fragment = new CouponsUserTabFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 跳转地址管理页面
     */
    public void go_address_list(Bundle bundle, Fragment target_fragment) {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new AddressListFragment();
            fragment.setArguments(bundle);
            fragment.setTargetFragment(target_fragment, Cart2Fragment.ADDRESS);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 跳转新建/编辑地址页面
     */
    public void go_address_edit(Bundle bundle, Fragment target_fragment) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new AddressEditFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, AddressListFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    /**
     * 跳转零元购商品详情
     */
    public void go_zero_use_detail(Bundle bundle) {

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new FreeDetailFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //启动购物车activity
    public void go_cart() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new Cart1Fragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //跳转到用户信息修改用户头像选择相册图片的功能
    public void go_select_photo_for_user_center_information_change_fragment(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new PhotoUploadFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, UCInformationModify.SELECT_PHOTO);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //用户咨询和投诉功能activity
    public void go_service_center() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new UCServiceCenterFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //VIP中心  activity
    public void go_vip_center() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = VIPCenterFragment.getInstance();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }


    /**
     * 用户咨询
     */
    public void go_consult_center() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new ConsultTabFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 用户投诉
     */
    public void go_complain_center() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new ComplainTabFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 用户投诉提交页面
     */
    public void go_complain_commit(Bundle bundle) {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new ComplainCommitFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 用户投诉提交页面
     */
    public void go_complain_theme(Bundle bundle, Fragment target_fragment) {
        if (islogin()) {
            showProcessDialog();
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new ComplainThemeFragment();
            fragment.setArguments(bundle);
            fragment.setTargetFragment(target_fragment, 0);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    /**
     * 用户投诉提交页面
     */
    public void go_complain_detail(Bundle bundle) {
        if (islogin()) {
            showProcessDialog();
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new ComplainDetailFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转选择聊天图片页面
    public void go_select_photo_for_complain_commit(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new PhotoUploadFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 1);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转团购商品
    public void go_group_goods(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new GroupLifeFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转退货取消订单
    public void go_goods_reset(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderGoodsReturnResetFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 0);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转关于
    public void go_about() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new AboutFragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //我的钱包
    public void go_wallet() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new UCWalletFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转地图
    public void go_map() {
        startActivity(new Intent().setClass(BaseActivity.this, MapActivity.class));
    }

    //跳转到我的投诉提交详情的页面
    public void go_my_complain(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new ComplainCommitFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, ComplainListFragment.NUM);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转订单追加评价页面
    public void go_order_evaluate_add(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderEvaluateAddFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 1);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转订单评价详情页面
    public void go_order_evaluate_details(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderEvaluateDetailsFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转订单评价修改页面
    public void go_order_evaluate_edit(Bundle bundle, Fragment target_fragment) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new OrderEvaluateEditFragment();
        fragment.setArguments(bundle);
        fragment.setTargetFragment(target_fragment, 1);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }


    //跳转到中国馆
    public void go_china_store(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new WebFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转到国际馆
    public void go_globe_store(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new WebFragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    /**
     * 将金钱小数位设置size
     *
     * @param money 金额
     * @return 返回小数点后两位价格并且改变小数点后数字的size
     */
    public String moneytodouble(String money) {
        //字符串为空则直接返回，不为空则转换
        if (!isEmpty(money)) {
            DecimalFormat fnum = new DecimalFormat("##0.00");
            String formatmoney = fnum.format(Double.valueOf(money));
            return formatmoney;
        } else {
            return "";
        }
    }

    /**
     * 菜单图文混合显示
     *
     * @param menu
     * @param enable
     */
    public void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> mclass = Class.forName("android.support.v7.view.menu.MenuBuilder");
            Method m = mclass.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);
            // MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 跳转到海报
    public void go_my_poster() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new MyPosterFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_my_vip_leader() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipLeaderFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_my_spread() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MySpreadFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void go_my_authorization() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyAuthorizationFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }


    public void go_vip_level_up() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipLevelUpFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void go_my_vip_reward_manage() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipRewardManageFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_my_commission() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyCommissionFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void go_my_vip_team_manage() {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipTeamManageFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    public void go_to_team2(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipTeam2Fragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void go_to_team3(Bundle bundle) {
        FragmentTransaction fragmentTransaction = getFragmentManager()
                .beginTransaction();
        Fragment fragment = new MyVipTeam3Fragment();
        fragment.setArguments(bundle);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //跳转我的红包
    public void go_my_red_package() {

        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager()
                    .beginTransaction();
            Fragment fragment = new MyRedPackageFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //跳转红包余额
    public void go_my_red_package_balance(Bundle bundle) {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new MyRedPackageBalanceFragment();
            fragment.setArguments(bundle);
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    public void go_to_my_bank_card() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new MyBankCardFragment();

            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }

    }

    public void go_to_my_pay_password_check() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new MyBankPayPasswordCheckFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }

    }

    //绑定银行卡
    public void go_to_bind_new_bank_card() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new BindNewBankCardFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }

    }


    //激活界面扫描邀请码
    public void go_to_qrscan2fragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        QRScan2Fragment scanCode2Fragment = new QRScan2Fragment();
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, scanCode2Fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //兑换会员
    public void go_exchangevip() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new ExchangeVipFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //体验会员
    public void go_tiyanvip() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new TiYanVipFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }
    //推广中心
    public void go_spreadcenter() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new SpreadCenterFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //卡牌管理
    public void go_card_level_manage() {
        if (islogin()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            Fragment fragment = new SpreadCardLevelManageFragment();
            fragmentTransaction.hide(getCurrentfragment());
            fragmentTransaction.add(R.id.index_top, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            go_login();
        }
    }

    //推广海报
    public void go_spread_poster() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new MyPosterFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //推广团队管理
    public void go_spread_team_manage() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new SpreadTeamManageFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    //我的推广码
    public void go_my_spread_code() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new SpreadCodeFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //推广提现
    public void go_drawout_money() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new SpreadDrawoutMoneyFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    //提现记录
    public void go_drawout_history() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment fragment = new SpreadDrawoutHistoryFragment();
        fragment.setTargetFragment(getCurrentfragment(), 100);
        fragmentTransaction.hide(getCurrentfragment());
        fragmentTransaction.add(R.id.index_top, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
