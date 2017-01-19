package com.ewgvip.buyer.android.activity;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.contants.Constants;
import com.ewgvip.buyer.android.fragment.Cart1Fragment;
import com.ewgvip.buyer.android.fragment.GoodsClassFragment;
import com.ewgvip.buyer.android.fragment.IndexFragment;
import com.ewgvip.buyer.android.fragment.IndexNavigatiorFragment;
import com.ewgvip.buyer.android.fragment.OrderAllTabFragment2;
import com.ewgvip.buyer.android.fragment.SuccessFragment;
import com.ewgvip.buyer.android.fragment.UserCenterFragment;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

/**
 * 主Activity，只负责管理fragment
 */
public class MainActivity extends BaseActivity implements AMapLocationListener, IUiListener {

    public static final String INDEX = "INDEX";
    private LocationManagerProxy mLocationManagerProxy;



    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 100:
                int isHasNoPermission = 0;
                if ((grantResults.length > 0)) {

                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            isHasNoPermission += 1;
                        }
                    }
                }
                if (isHasNoPermission > 0) {
                    CommonUtil.showSafeToast(this, "需要去设置手动添加必要的权限，才可以使用应用");
                    finish();
                } else {
                    go_welcome();
                }
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_main);

        //推送开始
        PushManager.startWork(getApplicationContext(), PushConstants.LOGIN_TYPE_API_KEY, Constants.BAIDU_PUSH_API_KEY);

        // Push: 设置自定义的通知样式，具体API介绍见用户手册，如果想使用系统默认的可以不加这段代码
        // 请在通知推送界面中，高级设置->通知栏样式->自定义样式，选中并且填写值：1，
        // 与下方代码中 PushManager.setNotificationBuilder(this, 1, cBuilder)中的第二个参数对应
        Resources resource = this.getResources();
        String pkgName = this.getPackageName();
        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
                resource.getIdentifier("notification_custom_builder", "layout", pkgName),
                resource.getIdentifier("notification_icon", "id", pkgName),
                resource.getIdentifier("notification_title", "id", pkgName),
                resource.getIdentifier("notification_text", "id", pkgName));
        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
        cBuilder.setStatusbarIcon(this.getApplicationInfo().icon);
        cBuilder.setLayoutDrawable(resource.getIdentifier("simple_notification_icon", "drawable", pkgName));
        // 推送高级设置，通知栏样式设置为下面的ID
        PushManager.setNotificationBuilder(this, 1, cBuilder);


        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
        mLocationManagerProxy.setGpsEnable(false);


        String[] permissions = {Manifest.permission.READ_PHONE_STATE};
        for (int i = 0; i < permissions.length; i++) {
            int permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissions, 100);
                return;
            } else {
                go_welcome();
            }
        }


    }



    @Override
    protected void onResume() {
        super.onResume();
        Fragment fragment = getCurrentfragment();
        if (fragment != null && getFragmentManager().getBackStackEntryCount() == 0 && (fragment instanceof IndexNavigatiorFragment || fragment instanceof IndexFragment || fragment instanceof GoodsClassFragment || fragment instanceof Cart1Fragment || fragment instanceof OrderAllTabFragment2 || fragment instanceof UserCenterFragment)) {
            findViewById(R.id.index_bottom).setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onPause() {
        super.onPause();
        //停止定位服务
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
    }




    @Override
    protected void onNewIntent(Intent intent) {

        super.onNewIntent(intent);

        Bundle bundle = intent.getExtras();
        if (bundle != null) {

            if (bundle.containsKey("paytype") && bundle.getString("paytype").equals("wxpay")) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SuccessFragment successFragment = new SuccessFragment();
                successFragment.setArguments(bundle);
                fragmentTransaction.add(R.id.index_top, successFragment);
                fragmentTransaction.commit();
            }
            if (bundle.containsKey("action")) {
                String action = bundle.getString("action");
                if (action.equals(INDEX))
                    go_index();
                if (action.equals("cart"))
                    go_cart1();
                if (action.equals("DETAIL"))
                    go_goods(bundle.getString("goods_id"));
                if (action.equals("order"))
                    go_orders_all();
                if (action.equals("usercenter"))
                    go_usercenterfromLevelUpVip();

            }
        }


    }


    @Override
    public void onAttachFragment(Fragment fragment) {
        if (getCurrentfragment() instanceof IndexFragment) {
            View top_tab = findViewById(R.id.index_top);
            if (top_tab != null) {
                top_tab.setBackgroundResource(R.color.toolbar_color);
                top_tab.invalidate();
            }
        }
        View bottom_tab = findViewById(R.id.index_bottom);
        if (getFragmentManager().getBackStackEntryCount() == 0 && (fragment instanceof IndexNavigatiorFragment || fragment instanceof IndexFragment || fragment instanceof GoodsClassFragment || fragment instanceof Cart1Fragment || fragment instanceof OrderAllTabFragment2 || fragment instanceof UserCenterFragment)) {
            if (bottom_tab != null && !bottom_tab.isShown())
                bottom_tab.setVisibility(View.VISIBLE);
        } else {
            if (bottom_tab != null && bottom_tab.isShown())
                bottom_tab.setVisibility(View.GONE);
        }
        super.onAttachFragment(fragment);
    }


    //获取位置信息
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {

            Double geoLat = aMapLocation.getLatitude();
            Double geoLng = aMapLocation.getLongitude();
            String province = aMapLocation.getProvince();
            String city = aMapLocation.getCity();
            String district = aMapLocation.getDistrict();

            SharedPreferences preferences = getSharedPreferences("user",
                    Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("geoLat", geoLat + "");
            editor.putString("geoLng", geoLng + "");
            editor.putString("province", province);
            editor.putString("city", city);
            editor.putString("district", district);
            editor.commit();
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    //加载底部导航
    public void go_index_navigator() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = IndexNavigatiorFragment.getInstance();
        fragmentTransaction.replace(R.id.index_bottom, fragment);
        if (isFinishing()) {
            if (isDestroyed()) {
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            } else {
                fragmentTransaction.commitAllowingStateLoss();
            }
        } else {
            fragmentTransaction.commitAllowingStateLoss();
        }

        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            if (uri != null) {
                String type = uri.getQueryParameter("open_type");
                String value = uri.getQueryParameter("value");
                if (type != null && type.equals("goods")) {
                    go_goods(value);

                    View bottom_tab = findViewById(R.id.index_bottom);
                    bottom_tab.setVisibility(View.GONE);
                }
            }
        }
        Bundle bundle = intent.getExtras();
        do_push(bundle);
    }

    @Override
    public void onComplete(Object o) {
        Toast.makeText(MainActivity.this, "分享成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(UiError uiError) {
        Toast.makeText(MainActivity.this, "分享失败", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel() {
        Toast.makeText(MainActivity.this, "分享取消", Toast.LENGTH_SHORT).show();
    }


    /**
     * qq的回调处理
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (null != mTencent) {
            Tencent.onActivityResultData(requestCode, resultCode, data, this);
        }


    }


}