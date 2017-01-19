package com.ewgvip.buyer.android.fragment;


import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.DistrictAdapter;
import com.ewgvip.buyer.android.adapter.GroupGoodsListAdapter;
import com.ewgvip.buyer.android.adapter.GroupShoppingAdapter;
import com.ewgvip.buyer.android.adapter.GroupShoppingNextClassAdapter;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 团购
 *
 * @author lgx
 *         A simple {@link Fragment} subclass.
 */
public class GroupShoppingFragment extends Fragment {
    public static final int NUM = 1;//用于判断requestcode
    public static PopupWindow popArea;//地址选择popupwindow
    public static PopupWindow popClass;//分类选择popupwindow
    public static PopupWindow popPrice;//排序选择popupwindow
    public static boolean areaShow = true;//地址选择popupwindow是否显示
    public static boolean classShow = true;//分类选择popupwindow是否显示
    public static boolean priceShow = true;//排序选择popupwindow是否显示
    private View rootView;
    private BaseActivity mActivity;
    private SharedPreferences preferences;
    private TextView groupDistrict, groupClassify, groupPrice, showLocalArea;
    private List<Map> districts;
    private ListView lvDistricts;//地区
    private int flag;
    private DistrictAdapter districtAdapter;//地区
    private GroupShoppingAdapter groupShoppingAdapter;//团购
    private GroupShoppingNextClassAdapter groupShoppingNextClassAdapter;//下一級分類
    private ListView lvLeft, lvRight;
    private List<Map> lifeList;//生活惠
    private View viewArea, viewClass, viewPrice;
    private String orderBy = "";//排序
    private String gc_id = "";//分类Id
    private String type = "life";
    private String orderType = "";
    private int beginCount = 0;
    private int selectCount = 10;
    private String cityId = "";
    private String areaName = "";
    private PullToRefreshListView mPullRefreshListView;
    private ListView actualListView;
    private GroupGoodsListAdapter mAdapter;
    private List<Map> goods;
    private ImageButton ib_refresh;
    private String geoLat = "";
    private String geoLng = "";
    private View view_liner;//分割线
    private String areaTitle = "";//地址选择title
    private String classTitle = "";//分类选择title
    private int current = 0;

    public GroupShoppingFragment() {        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            flag = bundle.getInt("flag");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_group_shopping, container, false);
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("团购");//设置标题
        mActivity = (BaseActivity) getActivity();
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        toolbar.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                checkTrue();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用

        goods = new ArrayList();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        geoLat = preferences.getString("geoLat", "");
        geoLng = preferences.getString("geoLng", "");
        ib_refresh = (ImageButton) rootView.findViewById(R.id.ib_refresh);
        ib_refresh.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.showProcessDialog();
                showLocation();
            }
        });
        view_liner = rootView.findViewById(R.id.view_divider);
        groupDistrict = (TextView) rootView.findViewById(R.id.groupDistrict);
        groupClassify = (TextView) rootView.findViewById(R.id.groupClassify);
        groupPrice = (TextView) rootView.findViewById(R.id.groupPrice);
        showLocalArea = (TextView) rootView.findViewById(R.id.showLocalArea);
        initPopup();
        final RelativeLayout rlArea = (RelativeLayout) rootView.findViewById(R.id.rlArea);
        final RelativeLayout rlClass = (RelativeLayout) rootView.findViewById(R.id.rlClass);
        final RelativeLayout rlPrice = (RelativeLayout) rootView.findViewById(R.id.rlPrice);
        rlClass.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (classShow) {
                    checkTrue();
                    classShow = false;
                    popClass.showAsDropDown(view_liner);
                    lvLeft = (ListView) viewClass.findViewById(R.id.lvLeft);
                    lvRight = (ListView) viewClass.findViewById(R.id.lvRight);
                    View group_transport = viewClass.findViewById(R.id.group_transport);
                    group_transport.setOnClickListener(view -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            popClass.dismiss();
                            checkTrue();
                        }
                    });
                    lifeList = new ArrayList<Map>();
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/group_class.htm?type=life",
                            result -> {
                                try {
                                    JSONObject obj = result.getJSONObject("result");
                                    if (obj.has("gc_life")) {
                                        JSONArray lifeJA = obj.getJSONArray("gc_life");
                                        int lCount = lifeJA.length();
                                        for (int i = 0; i < lCount; i++) {
                                            JSONObject lifeJB = lifeJA.getJSONObject(i);
                                            Map lifeMap = new HashMap();
                                            lifeMap.put("id", lifeJB.getString("id"));
                                            lifeMap.put("name", lifeJB.getString("name"));
                                            lifeList.add(lifeMap);
                                        }
                                    }
                                    groupShoppingAdapter = new GroupShoppingAdapter(mActivity, lifeList, GroupShoppingFragment.this);
                                    lvLeft.setAdapter(groupShoppingAdapter);
                                    setDefault(lifeList);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mActivity.hideProcessDialog(0);
                            }, error -> mActivity.hideProcessDialog(1), null);
                    mRequestQueue.add(request);
                } else {
                    classShow = true;
                }
            }
        });

        rlPrice.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (priceShow) {
                    checkTrue();
                    priceShow = false;
                    popPrice.showAsDropDown(view_liner);
                    RelativeLayout rl_distance = (RelativeLayout) viewPrice.findViewById(R.id.rl_distance);
                    RelativeLayout rl_popular = (RelativeLayout) viewPrice.findViewById(R.id.rl_popular);
                    RelativeLayout rl_sale = (RelativeLayout) viewPrice.findViewById(R.id.rl_sale);
                    View group_transport = viewPrice.findViewById(R.id.group_transport);
                    group_transport.setOnClickListener(view -> {
                        popPrice.dismiss();
                        checkTrue();
                    });
                    rl_distance.setOnClickListener(v1 -> {
                        orderBy = "离我最近";
                        popPrice.dismiss();
                        goods.clear();
                        beginCount = 0;
                        mActivity.showProcessDialog();
                        init("0", orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
                        mPullRefreshListView.onRefreshComplete();
                        groupPrice.setText(orderBy);
                    });
                    rl_popular.setOnClickListener(v12 -> {
                        orderBy = "人气最高";
                        popPrice.dismiss();
                        goods.clear();
                        beginCount = 0;
                        mActivity.showProcessDialog();
                        init("1", orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
                        mPullRefreshListView.onRefreshComplete();
                        groupPrice.setText(orderBy);
                    });
                    rl_sale.setOnClickListener(v13 -> {
                        orderBy = "经济实惠";
                        popPrice.dismiss();
                        goods.clear();
                        beginCount = 0;
                        mActivity.showProcessDialog();
                        init("2", orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
                        mPullRefreshListView.onRefreshComplete();
                        groupPrice.setText(orderBy);
                    });
                } else {
                    priceShow = true;
                }
            }
        });
        mPullRefreshListView = (PullToRefreshListView) rootView
                .findViewById(R.id.goods_listview);
        mPullRefreshListView.setVisibility(View.VISIBLE);
        rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
        mPullRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        // 到底刷新
        mPullRefreshListView.setOnLastItemVisibleListener(() -> init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId));
        actualListView = mPullRefreshListView.getRefreshableView();
        registerForContextMenu(actualListView);
        mAdapter = new GroupGoodsListAdapter(mActivity, goods);
        actualListView.setAdapter(mAdapter);
        actualListView.setOnItemClickListener((parent, view, position, id) -> {
            Bundle bundle = new Bundle();
            bundle.putString("id", goods.get(position - 1).get("id").toString());
            bundle.putString("geoLng", geoLng);
            bundle.putString("geoLat", geoLat);
            mActivity.go_group_goods(bundle);
        });
        if (flag == 1) {
            showLocation();
        }
        if (flag == 3) {
            cityId = preferences.getString("cityId", "");
            areaName = preferences.getString("areaName", "");
            showLocalArea.setText(areaName);
            goods.clear();
            beginCount = 0;
            orderBy = "";
            orderType = "";
            mActivity.showProcessDialog();
            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
        }

        if (null == preferences.getString("districtName", "").toString() || TextUtils.isEmpty(preferences.getString("districtName", "").toString())) {
            groupDistrict.setText("地区");
        } else {
            groupDistrict.setText(preferences.getString("districtName", "").toString());
        }

        rlArea.setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (areaShow) {
                    checkTrue();
                    areaShow = false;
                    popArea.showAsDropDown(view_liner);
                    lvDistricts = (ListView) viewArea.findViewById(R.id.listView);
                    View group_transport = viewArea.findViewById(R.id.group_transport);
                    group_transport.setOnClickListener(view -> {
                        popArea.dismiss();
                        checkTrue();
                    });
                    Map paraMap = new HashMap();
                    paraMap.put("cityId", cityId);
                    RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
                    Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/getAreaByCity.htm",
                            result -> {
                                try {
                                    districts = new ArrayList<Map>();
                                    if (result.getInt("code") == 200) {
                                        JSONArray arrays = result.getJSONArray("result");
                                        int length = arrays.length();
                                        for (int i = 0; i < length; i++) {
                                            JSONObject jbt = arrays.getJSONObject(i);
                                            Map areaMap = new HashMap();
                                            areaMap.put("id", jbt.getString("id"));
                                            areaMap.put("areaName", jbt.getString("areaName"));
                                            districts.add(areaMap);
                                        }
                                        districtAdapter = new DistrictAdapter(mActivity, districts, GroupShoppingFragment.this);
                                        lvDistricts.setAdapter(districtAdapter);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                mActivity.hideProcessDialog(0);
                            }, error -> mActivity.hideProcessDialog(1), paraMap);
                    mRequestQueue.add(request);
                } else {
                    areaShow = true;
                }
            }
        });
        return rootView;
    }


    //初始化popupwindow
    public void initPopup() {
        LayoutInflater mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        viewArea = mLayoutInflater.inflate(R.layout.areaselect, null);
        popArea = new PopupWindow(viewArea, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popArea.setBackgroundDrawable(new BitmapDrawable());
        popArea.setFocusable(false);
        popArea.setOutsideTouchable(true);
        viewClass = mLayoutInflater.inflate(R.layout.groupgoodclass, null);
        popClass = new PopupWindow(viewClass, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        popClass.setBackgroundDrawable(new BitmapDrawable());
        popClass.setFocusable(false);
        popClass.setOutsideTouchable(true);
        viewPrice = mLayoutInflater.inflate(R.layout.life, null);
        popPrice = new PopupWindow(viewPrice, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        popPrice.setBackgroundDrawable(new BitmapDrawable());
        popPrice.setFocusable(false);
        popPrice.setOutsideTouchable(true);
    }

    //地址popupwindow点击操作
    public void setArea(int position) {
        Map districMap = districts.get(position);
        areaTitle = districMap.get("areaName").toString();
        groupDistrict.setText(areaTitle);
        popArea.dismiss();
        beginCount = 0;
        mActivity.showProcessDialog();
        if (position == 0) {
            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
        } else {
            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, districMap.get("id").toString());
        }
        mPullRefreshListView.onRefreshComplete();

    }

    //分类popupwindow左侧点击操作
    public void setLeftClass(final int position, List<Map> lifeList) {
        groupShoppingAdapter.setSelectedPosition(position);
        groupShoppingAdapter.notifyDataSetChanged();
        lvLeft.setSelection(position);
        final List<Map> rightList = new ArrayList();
        Map map = lifeList.get(position);
        String goodId = map.get("id").toString();
        classTitle = map.get("name").toString();
        if (goodId.equals("-1")) {
            current = 0;
            lvRight.setVisibility(View.GONE);
            viewClass.findViewById(R.id.noData).setVisibility(View.VISIBLE);
            gc_id = null;
            popClass.dismiss();
            goods.clear();
            beginCount = 0;
            mActivity.showProcessDialog();
            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
            mPullRefreshListView.onRefreshComplete();
            groupClassify.setText(classTitle);
        } else {

            viewClass.findViewById(R.id.noData).setVisibility(View.GONE);
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/getNextClass.htm?id=" + goodId,
                    result -> {
                        try {
                            JSONArray localArrays = result.getJSONArray("result");
                            int iCount = localArrays.length();
                            if (iCount > 0) {
                                lvRight.setVisibility(View.VISIBLE);
                                viewClass.findViewById(R.id.noData).setVisibility(View.GONE);
                                for (int i = 0; i < localArrays.length(); i++) {
                                    Map map1 = new HashMap();
                                    JSONObject nextClassJson = (JSONObject) localArrays.get(i);
                                    map1.put("id", nextClassJson.getString("id"));
                                    map1.put("name", nextClassJson.getString("name"));
                                    rightList.add(map1);
                                }
                                groupShoppingNextClassAdapter = new GroupShoppingNextClassAdapter(mActivity, rightList, position, GroupShoppingFragment.this);
                                lvRight.setVisibility(View.VISIBLE);
                                lvRight.setAdapter(groupShoppingNextClassAdapter);
                            } else {
                                lvRight.setVisibility(View.GONE);
                                viewClass.findViewById(R.id.noData).setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mActivity.hideProcessDialog(0);
                    }, error -> {
                        mActivity.hideProcessDialog(1);
                        viewClass.findViewById(R.id.noData).setVisibility(View.VISIBLE);
                        lvRight.setVisibility(View.GONE);
                    }, null);
            mRequestQueue.add(request);
        }
    }

    //分类popupwindow右侧点击操作
    public void setRightClass(int position, List<Map> rightList, int parent) {
        current = parent;
        Map map = rightList.get(position);
        gc_id = map.get("id").toString();
        popClass.dismiss();
        goods.clear();
        beginCount = 0;
        mActivity.showProcessDialog();
        init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
        mPullRefreshListView.onRefreshComplete();
        groupClassify.setText(classTitle);
    }

    //数据获取操作
    private void init(String orderBy, String orderType, String gc_id, String type, final int beginCount, final int selectCount, String lng, String lat, String cityId) {
        Map map = new HashMap();
        if (orderBy != null) {
            map.put("orderBy", orderBy);
        }
        if (orderType != null) {
            map.put("orderType", orderType);
        }
        if (gc_id != null) {
            map.put("gc_id", gc_id);
        }
        if (type != null) {
            map.put("type", type);
        }
        map.put("begincount", beginCount + "");
        map.put("selectcount", selectCount + "");
        if (lng != null) {
            map.put("lng", lng);
        }
        if (lat != null) {
            map.put("lat", lat);
        }
        if (cityId != null) {
            map.put("cityId", cityId);
        }
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        NormalPostRequest request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/group_index.htm",
                result -> {
                    try {
                        Log.e("result:团购",result.toString());
                        if (0 == beginCount) {
                            goods.clear();
                        }
                        JSONArray nameList = result.getJSONArray("grouplist");// 获取JSONArray
                        int length = nameList.length();
                        for (int i = 0; i < length; i++) {// 遍历JSONArray
                            JSONObject oj = nameList.getJSONObject(i);
                            Map goods_map = new HashMap();
                            goods_map.put("id", oj.getInt("id"));
                            goods_map.put("goods_main_photo", oj.getString("gg_img"));
                            goods_map.put("goods_name", oj.getString("gg_name"));
                            goods_map.put("goods_current_price", oj.getString("gg_price"));
                            goods_map.put("goods_salenum", oj.getString("gg_selled_count"));
                            goods_map.put("status", "");
                            goods_map.put("evaluate", "");
                            goods.add(goods_map);
                        }
    //                     nodata.setVisibility(View.GONE);
                        if (length == 0) {
                            // 无数据
                            if (goods.size() == 0) {
                                rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                mPullRefreshListView.setVisibility(View.GONE);
                            }
                        } else {
                            mPullRefreshListView.setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            mAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (beginCount == 0) {
                        actualListView.setSelection(1);
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), map);
        mRequestQueue.add(request);
        this.beginCount += selectCount;
        checkTrue();
    }

    public void checkTrue() {
        areaShow = true;
        priceShow = true;
        classShow = true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == NUM) {
            checkTrue();
            groupDistrict.setText("地区");
            groupClassify.setText("分类");
            groupPrice.setText("排序");
            current = 0;
            cityId = preferences.getString("cityId", "");
            areaName = preferences.getString("areaName", "");
            showLocalArea.setText(areaName);
            goods.clear();
            beginCount = 0;
            orderBy = "";
            orderType = "";
            gc_id = "";
            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setDefault(List<Map> lifeList) {
        groupShoppingAdapter.setSelectedPosition(current);
        groupShoppingAdapter.notifyDataSetInvalidated();
        Map map = lifeList.get(current);
        String goodId = map.get("id").toString();
        if (goodId.equals("-1")) {
            gc_id = null;
            lvRight.setVisibility(View.GONE);
            viewClass.findViewById(R.id.noData).setVisibility(View.GONE);
        } else {
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/getNextClass.htm?id=" + goodId,
                    result -> {
                        try {
                            final List<Map> rightList = new ArrayList();
                            JSONArray localArrays = result.getJSONArray("result");
                            int iCount = localArrays.length();
                            if (iCount > 0) {
                                lvRight.setVisibility(View.VISIBLE);
                                viewClass.findViewById(R.id.noData).setVisibility(View.GONE);
                                for (int i = 0; i < localArrays.length(); i++) {
                                    Map map1 = new HashMap();
                                    JSONObject nextClassJson = (JSONObject) localArrays.get(i);
                                    map1.put("id", nextClassJson.getString("id"));
                                    map1.put("name", nextClassJson.getString("name"));
                                    rightList.add(map1);
                                }
                                groupShoppingNextClassAdapter = new GroupShoppingNextClassAdapter(mActivity, rightList, current, GroupShoppingFragment.this);
                                lvRight.setAdapter(groupShoppingNextClassAdapter);
                            } else {
                                lvRight.setVisibility(View.GONE);
                                viewClass.findViewById(R.id.noData).setVisibility(View.VISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        mActivity.hideProcessDialog(0);
                    }, error -> {
                        mActivity.hideProcessDialog(1);
                        lvRight.setVisibility(View.GONE);
                        viewClass.findViewById(R.id.noData).setVisibility(View.VISIBLE);
                    }, null);
            mRequestQueue.add(request);
        }
    }

    private void showLocation() {
        mActivity.showProcessDialog();
        Map paraMap = new HashMap();
        paraMap.put("lat", geoLat);
        paraMap.put("lng", geoLng);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/getAreaByGgrphy.htm",
                result -> {
                    try {
                        if (result.getInt("code") == 404) {
                            showLocalArea.setText("系统繁忙，稍后重试");
                            mActivity.hideProcessDialog(0);
                        } else if (result.getInt("code") == 200) {
                            JSONObject data = result.getJSONObject("result");
                            areaName = data.getString("areaName");
                            cityId = data.getString("areaId");
                            showLocalArea.setText(areaName);
                            init(orderBy, orderType, gc_id, type, beginCount, selectCount, geoLng, geoLat, cityId);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search, menu);
        mActivity.setIconEnable(menu,true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     * @param menu
     */
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass() == MenuBuilder.class) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", boolean.class);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (Exception e) {
                }
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }else if(id==R.id.action_map){

            mActivity.go_maplocation(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
