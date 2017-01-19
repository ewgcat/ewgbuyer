package com.ewgvip.buyer.android.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeAddress;
import com.amap.api.services.geocoder.GeocodeQuery;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.ewgvip.buyer.android.R;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:35
 * Description:  地图定位类
 */
public class MapActivity extends BaseActivity implements GeocodeSearch.OnGeocodeSearchListener {

    private GeocodeSearch geocoderSearch;
    private AMap aMap;
    private MapView mapView;
    private Marker geoMarker;
    private Marker regeoMarker;
    private LatLonPoint latLonPoint;
    private BaseActivity mActivity;
    private Toolbar toolbar;

    /*
        将LatLonPoint转为LatLng类型
     */
    public static LatLng convertToLatLng(LatLonPoint latLonPoint) {
        return new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        mActivity = MapActivity.this;
        mActivity.showProcessDialog();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());
        mActivity.findViewById(R.id.nodata).setVisibility(View.GONE);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();
        Double gai_lat = getIntent().getDoubleExtra("gai_lat", 0);
        Double gai_lng = getIntent().getDoubleExtra("gai_lng", 0);
        latLonPoint = new LatLonPoint(gai_lat, gai_lng);
        getAddress(latLonPoint);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
            geoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
            regeoMarker = aMap.addMarker(new MarkerOptions().anchor(0.5f, 0.5f)
                    .icon(BitmapDescriptorFactory
                            .defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置同步逆地理编码请求
    }

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        mActivity.hideProcessDialog(0);
        if (rCode == 0) {
            if (result != null && result.getRegeocodeAddress() != null && result.getRegeocodeAddress().getFormatAddress() != null) {
                mapView.setVisibility(View.VISIBLE);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(convertToLatLng(latLonPoint), 15));
                regeoMarker.setPosition(convertToLatLng(latLonPoint));
            } else {
                //未搜索到数据
                refresh();
            }
        } else if (rCode == 27) {
            refresh();
            //网络异常
        } else if (rCode == 32) {
            refresh();
            //key验证无效
        } else {
            refresh();
            //未知错误，请稍后重试!错误码为+rCode
        }
    }

    public void refresh() {
        mActivity.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
        mActivity.findViewById(R.id.nodata_refresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mActivity.showProcessDialog();
                getAddress(latLonPoint);
            }
        });
    }

    /**
     * 响应地理编码
     */
    public void getLatlon(final String name) {
        GeocodeQuery query = new GeocodeQuery(name, "010");// 第一个参数表示地址，第二个参数表示查询城市，中文或者中文全拼，citycode、adcode，
        geocoderSearch.getFromLocationNameAsyn(query);// 设置同步地理编码请求
    }

    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult result, int rCode) {
        if (rCode == 0) {
            if (result != null && result.getGeocodeAddressList() != null
                    && result.getGeocodeAddressList().size() > 0) {
                GeocodeAddress address = result.getGeocodeAddressList().get(0);
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        convertToLatLng(address.getLatLonPoint()), 15));
                geoMarker.setPosition(convertToLatLng(address
                        .getLatLonPoint()));
            } else {
            }
        } else if (rCode == 27) {
        } else if (rCode == 32) {
        } else {
        }
    }


}
