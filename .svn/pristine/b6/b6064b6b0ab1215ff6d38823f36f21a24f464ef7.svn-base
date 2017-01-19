package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.GoodsListAdapter2;
import com.ewgvip.buyer.android.adapter.GoodsSelectAdapter;
import com.ewgvip.buyer.android.utils.DensityUtil;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: lixiaoyang
 * Date: 1/14/16 12:00
 * Description:  商品列表
 */
public class GoodsListFragment extends Fragment {

    boolean isLoadingMore = true;
    private BaseActivity mActivity;
    private View rootView;
    private String orderBy = "goods_salenum";
    private String orderByType = "desc";
    private String keyword = "";
    private String queryType = "";
    private String selfFilter = "";
    private String inventoryFilter = "";
    private String payafterFilter = "";
    private String gc_id = "", gb_id = "", titleword = "", sBuildingId = "";
    private int beginCount = 0;
    private int selectCount = 20;
    private List goods = new ArrayList();
    private View nodata;
    private ImageView imgBack;
    private String store_id, sWhere;
    private DrawerLayout drawerLayout, drawerSub; // 抽屉效果
    private TextView tvSelect, tvCancel, tvSubmit, tvBrand;
    private ListView listView;
    private List<Map<String, Object>> selectList;
    private Handler handler;
    private Drawable drawableRight;
    private LinearLayout linearSelect;
    private List<String> sNames;
    private GoodsSelectAdapter goodsSelectAdapter;
    private Button btnClear;
    private Request<JSONObject> request;
    private int iFirstSelect, iFirstPosition;
    private RelativeLayout relativeNoData;
    private ScrollView scrollView;
    private TextView[] textViews;
    private SparseArray<String[]> sparseArray;
    private String[] sContents;
    private View fab;//回到顶部按钮
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private CheckBox cbox_goods_self_filter;
    private CheckBox cbox_goods_payafter_filter;
    private CheckBox cbox_goods_inventory_filter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_list, container, false);
        mActivity = (BaseActivity) getActivity();
        final Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());

        //排序方式切换
        TabLayout tabLayout = (TabLayout) rootView.findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.sales_volume)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.popularity)));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.price)).setIcon(R.mipmap.priceno));
        tabLayout.addTab(tabLayout.newTab().setText(getString(R.string.newgoods)));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();

                    switch (position) {
                        case 0:
                            orderBy = "goods_salenum";
                            break;
                        case 1:
                            orderBy = "goods_click";
                            break;
                        case 2:
                            orderBy = "goods_current_price";
                            break;
                        case 3:
                            orderBy = "goods_seller_time";
                            break;
                        default:
                            orderBy = "goods_click";
                            break;
                    }

                    if (position == 2) {
                        if (orderByType.equals("asc")) {
                            orderByType = "desc";
                            tab.setIcon(R.mipmap.pricedown);
                        } else {
                            orderByType = "asc";
                            tab.setIcon(R.mipmap.priceup);
                        }
                    } else {
                        orderByType = "desc";
                    }
                    goods.clear();
                    beginCount = 0;

                    mActivity.showProcessDialog();
                    //TODO
                    init(orderBy, orderByType, beginCount, selectCount, gc_id, keyword, gb_id, queryType, selfFilter, inventoryFilter, payafterFilter);
                }
            }


            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();
                    if (position == 2) {
                        tab.setIcon(R.mipmap.priceno);
                    }
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if (beginCount > 0) {
                    int position = tab.getPosition();
                    if (position == 2) {
                        if (orderByType.equals("asc")) {
                            orderByType = "desc";
                            tab.setIcon(R.mipmap.pricedown);
                        } else {
                            orderByType = "asc";
                            tab.setIcon(R.mipmap.priceup);
                        }
                        goods.clear();
                        beginCount = 0;

                        mActivity.showProcessDialog();
                        //TODO
                        init(orderBy, orderByType, beginCount, selectCount, gc_id, keyword, gb_id, queryType, selfFilter, inventoryFilter, payafterFilter);
                    }
                }
            }
        });


        imgBack = (ImageView) rootView.findViewById(R.id.imgBack);
        relativeNoData = (RelativeLayout) rootView.findViewById(R.id.relativeNoData);
        linearSelect = (LinearLayout) rootView.findViewById(R.id.linearSelect);
        drawerLayout = (DrawerLayout) rootView.findViewById(R.id.drawer_layout);
        drawerSub = (DrawerLayout) rootView.findViewById(R.id.drawer_sub);
        listView = (ListView) rootView.findViewById(R.id.listViewSub);
        tvSelect = (TextView) rootView.findViewById(R.id.tvSelect);
        tvCancel = (TextView) rootView.findViewById(R.id.tvCancel);
        tvSubmit = (TextView) rootView.findViewById(R.id.tvSubmit);
        tvBrand = (TextView) rootView.findViewById(R.id.tvBrand);
        nodata = rootView.findViewById(R.id.nodata);
        btnClear = (Button) rootView.findViewById(R.id.btnClear);
        scrollView = (ScrollView) rootView.findViewById(R.id.scrollView);
        cbox_goods_self_filter = (CheckBox) rootView.findViewById(R.id.cbox_goods_self_filter);
        cbox_goods_payafter_filter = (CheckBox) rootView.findViewById(R.id.cbox_goods_payafter_filter);
        cbox_goods_inventory_filter = (CheckBox) rootView.findViewById(R.id.cbox_goods_inventory_filter);
        cbox_goods_self_filter.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                selfFilter = "1";
            } else {
                selfFilter = "0";
            }
        });
        cbox_goods_inventory_filter.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                inventoryFilter = "1";
            } else {
                inventoryFilter = "0";
            }
        });
        cbox_goods_payafter_filter.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                payafterFilter = "1";
            } else {
                payafterFilter = "0";
            }
        });

        //回到顶部按钮
        fab = rootView.findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        fab.setOnClickListener(v -> {
            fab.setVisibility(View.GONE);
            mLayoutManager.scrollToPositionWithOffset(0, 0);
        });


        //列表数据
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.goods_list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        //设置布局管理器
        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new GoodsListAdapter2(goods, mActivity);
        mRecyclerView.setAdapter(mAdapter);

        //设置Item增加、移除动画
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //添加分割线
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(mActivity)
                        .color(R.color.divgray)
                        .sizeResId(R.dimen.divider)
                        .marginResId(R.dimen.leftmargin, R.dimen.zero)
                        .build());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItem > 2) {
                    fab.setVisibility(View.VISIBLE);
                } else {
                    fab.setVisibility(View.GONE);
                }

                int lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
                int totalItemCount = mLayoutManager.getItemCount();
                //lastVisibleItem >= totalItemCount - 10 表示剩下10个item自动加载，各位自由选择
                // dy>0 表示向下滑动
                if (lastVisibleItem >= totalItemCount - 10 && dy > 0) {
                    if (isLoadingMore) {
                        //TODO
                        init(orderBy, orderByType, beginCount, selectCount, gc_id, keyword,
                                gb_id, queryType, selfFilter, inventoryFilter, payafterFilter);
                        isLoadingMore = false;
                    }
                }

            }
        });


        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (null != bundle) {
            gc_id = bundle.getString("gc_id");
            gb_id = bundle.getString("gb_id");
            // String title = bundle.getString("title");
            queryType = bundle.getString("queryType");
            keyword = bundle.getString("searchword");
            store_id = bundle.getString("store_id");
            titleword = bundle.getString("titleword");
            if (bundle.getString("orderBy") != null
                    && !bundle.getString("orderBy").equals("")) {
                orderBy = bundle.getString("orderBy");

            }
        }
    }


    @Override
    public void onResume() {


        onInit();
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener((v, keyCode, event) -> {
            if (event.getAction() == KeyEvent.ACTION_UP
                    && keyCode == KeyEvent.KEYCODE_BACK) {
                if (drawerSub.isDrawerVisible(Gravity.RIGHT)) {
                    drawerSub.closeDrawer(Gravity.RIGHT);
                    return true;
                } else if (drawerLayout.isDrawerVisible(Gravity.RIGHT)) {
                    drawerLayout.closeDrawer(Gravity.RIGHT);
                    return true;
                }

            }
            return false;
        });

        super.onResume();
    }

    private void onInit() {
        sNames = new ArrayList<String>();
        sWhere = "";

        TextView tv = (TextView) rootView.findViewById(R.id.search_text);
        tv.setOnClickListener(v -> mActivity.go_search(store_id));

        if (titleword != null && !titleword.equals("")) {
            tv.setText(titleword);
        }
        if (keyword != null && !keyword.equals("")) {
            tv.setText(keyword);
        }

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {

                super.handleMessage(msg);

                switch (msg.what) {
                    case 0:
                        int iCount = selectList.size();
                        if (iCount > 0) {
                            textViews = new TextView[iCount];
                            for (int i = 0; i < iCount; i++) {
                                Map<String, Object> map = selectList.get(i);
                                RelativeLayout relativeLayout = getRelativeLayout(i);
                                TextView tvLeft = getTextView();
                                tvLeft.setText(map.get("name").toString());
                                TextView tvRight = getTextViewRight();
                                tvRight.setText(map.get("parameter").toString());
                                textViews[i] = tvRight;
                                relativeLayout.addView(tvLeft);
                                relativeLayout.addView(tvRight);

                                linearSelect.addView(relativeLayout);
                                if (i != iCount - 1) {
                                    View view = getViewBottom();
                                    linearSelect.addView(view);
                                }
                            }
                            scrollView.setVisibility(View.VISIBLE);
                            relativeNoData.setVisibility(View.GONE);
                        } else {
                            scrollView.setVisibility(View.GONE);
                            relativeNoData.setVisibility(View.VISIBLE);
                        }
                        break;
                    case 1:
                        if (goodsSelectAdapter == null) {
                            goodsSelectAdapter = new GoodsSelectAdapter(
                                    mActivity, sNames);
                            listView.setAdapter(goodsSelectAdapter);
                            goodsSelectAdapter.setOnClickListener(iPosition -> {
                                TextView textView = textViews[iFirstPosition];
                                textView.setText(sNames.get(iPosition));
                                if (!sNames.get(iPosition).equals("全部")) {
                                    textView.setTextColor(Color.RED);
                                } else {
                                    textView.setTextColor(Color.BLACK);
                                }
                                drawerSub.closeDrawer(Gravity.RIGHT);
                                sContents = new String[]{
                                        String.valueOf(iPosition),
                                        String.valueOf(selectList.get(
                                                iFirstPosition).get("id")),
                                        sNames.get(iPosition)};
//								mapValueByKey.put(iFirstPosition, sContents);
                                sparseArray.put(iFirstPosition, sContents);
                            });
                        } else {
                            goodsSelectAdapter.setSelectPosition(Integer.parseInt(sparseArray.get(iFirstPosition)[0]));
                            goodsSelectAdapter.notifyDataSetChanged();
                        }
                        break;

                }

            }
        };
        //TODO
        init(orderBy, orderByType, beginCount, selectCount, gc_id, keyword, gb_id, queryType, selfFilter, inventoryFilter, payafterFilter);

        if (gc_id == null) {
            tvSelect.setVisibility(View.GONE);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            drawerSub.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            tvSelect.setVisibility(View.VISIBLE);
            tvSelect.setOnClickListener(v -> drawerLayout.openDrawer(Gravity.RIGHT));
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            drawerSub.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

        drawableRight = getResources().getDrawable(R.mipmap.icon_jt_r);
        drawableRight.setBounds(0, 0, drawableRight.getMinimumWidth(), drawableRight.getMinimumHeight());
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (iFirstSelect == 0 && selectList == null) {
                    getSelect();
                    iFirstSelect = 1;
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                if (request.isCanceled()) {
                    request.cancel();
                }
                iFirstSelect = 0;
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });

        drawerSub.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(View drawerView) {
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                if (!request.isCanceled()) {
                    request.cancel();
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
        imgBack.setOnClickListener(v -> drawerSub.closeDrawer(Gravity.RIGHT));
        tvCancel.setOnClickListener(v -> drawerLayout.closeDrawer(Gravity.RIGHT));
        tvSubmit.setOnClickListener(v -> {
            if (null != sparseArray) {
                StringBuffer stringBuffer = new StringBuffer();
                int iCount = sparseArray.size();
                for (int i = 0; i < iCount; i++) {
                    sContents = sparseArray.get(i);
                    if (!sContents[1].equals("0")
                            && !sContents[2].equals("全部")) {
                        stringBuffer.append(sContents[1]);
                        stringBuffer.append(",");
                        stringBuffer.append(sContents[2]);
                        stringBuffer.append("|");
                    }
                }
                iCount = stringBuffer.length();
                if (iCount > 0) {
                    stringBuffer.delete(iCount - 1, iCount);
                }
                beginCount = 0;
                sWhere = stringBuffer.toString();
                drawerLayout.closeDrawer(Gravity.RIGHT);
                mActivity.showProcessDialog();

                //TODO
                init(orderBy, orderByType, beginCount, selectCount, gc_id, keyword, gb_id, queryType, selfFilter, inventoryFilter, payafterFilter);
            } else {

                drawerLayout.closeDrawer(Gravity.RIGHT);
            }
        });

        btnClear.setOnClickListener(v -> {
            sWhere = "";
            sContents = new String[]{"0", "0", "全部"};
            if (sparseArray != null) {
                int iCount = sparseArray.size();
                sparseArray.clear();
                for (int i = 0; i < iCount; i++) {
                    sparseArray.put(i, sContents);
                    TextView tView = textViews[i];
                    tView.setText("全部");
                    tView.setTextColor(Color.BLACK);
                }
            }
            cbox_goods_self_filter.setChecked(false);
            cbox_goods_inventory_filter.setChecked(false);
            cbox_goods_payafter_filter.setChecked(false);
        });

    }

    public void init(String orderBy, String orderType, final int beginCount, int selectCount, String gc_id, String keyword, String gb_id,
                     String queryType, String selfFilter, String inventoryFilter, String payafterFilter) {
        String url = "/app/goods_list.htm";
        Map paraMap = mActivity.getParaMap();
        if (gc_id != null) {
            paraMap.put("gc_id", gc_id);
        }
        if (keyword != null && !keyword.equals("")) {
            paraMap.put("keyword", keyword);
        }
        if (gb_id != null) {
            paraMap.put("gb_id", gb_id);
        }
        if (selfFilter != null) {
            paraMap.put("selfFilter", selfFilter);
        }
        if (inventoryFilter != null) {
            paraMap.put("inventoryFilter", inventoryFilter);
        }
        if (payafterFilter != null) {
            paraMap.put("payafterFilter", payafterFilter);
        }
        if (queryType != null && !queryType.equals("")) {
            paraMap.put("queryType", queryType);
        }
        if (!sBuildingId.equals("")) {
            paraMap.put("buildingId", sBuildingId);
        }
        paraMap.put("orderBy", orderBy);
        paraMap.put("orderType", orderType);
        paraMap.put("selfFilter", selfFilter);
        paraMap.put("inventoryFilter", inventoryFilter);
        paraMap.put("payafterFilter", payafterFilter);
        paraMap.put("beginCount", "" + beginCount);
        paraMap.put("selectCount", "" + selectCount);
        if (!sWhere.equals("")) {
            paraMap.put("properties", sWhere);
        }
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        request = new NormalPostRequest(mActivity, mActivity.getAddress() + url,
                result -> {
                    try {
                        if (result != null) {
                            if (0 == beginCount) {
                                goods.clear();
                            }
                            JSONArray nameList = result.getJSONArray("goods_list");// 获取JSONArray
                            int length = nameList.length();
                            for (int i = 0; i < length; i++) {// 遍历JSONArray
                                JSONObject oj = nameList.getJSONObject(i);
                                Map goods_map = new HashMap();
                                goods_map.put("id", oj.getInt("id"));
                                goods_map.put("goods_main_photo", oj.getString("goods_main_photo"));
                                goods_map.put("goods_name", oj.getString("goods_name"));
                                goods_map.put("goods_current_price", oj.getString("goods_current_price"));
                                goods_map.put("goods_salenum", oj.getString("goods_salenum"));
                                goods_map.put("status", oj.getString("status"));
                                goods_map.put("evaluate", oj.getString("evaluate"));
                                goods.add(goods_map);
                            }
                            if (beginCount == 0 && length == 0) {
                                // 无数据
                                nodata();
                            } else {
                                mRecyclerView.setVisibility(View.VISIBLE);
                                nodata.setVisibility(View.GONE);
                                mAdapter.notifyDataSetChanged();
                                isLoadingMore = true;
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (beginCount == 0) {
                        mRecyclerView.scrollToPosition(0);
                    }
                    mActivity.hideProcessDialog(0);

                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

        this.beginCount += selectCount;
    }

    void nodata() {
        if (goods.size() == 0) {
            mRecyclerView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
    }

    private void getSelect() {
        mActivity.showProcessDialog();
        StringBuffer stringBuffer = new StringBuffer(mActivity.getAddress());
        stringBuffer.append("/app/getfilter.htm?gc_id=");
        stringBuffer.append(gc_id);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        request = new NormalPostRequest(mActivity, stringBuffer.toString(),
                result -> {
                    try {
                        selectList = new ArrayList<Map<String, Object>>();
                        if (result.has("result")) {
                            JSONArray nameList = result
                                    .getJSONArray("result");// 获取JSONArray
                            int length = nameList.length();
                            sparseArray = new SparseArray<String[]>();
                            sContents = new String[]{"0", "0", "全部"};
                            for (int i = 0; i < length; i++) {
                                result = nameList.getJSONObject(i);
                                Map<String, Object> map = new HashMap<String, Object>();
                                map.put("name", result.get("name"));
                                map.put("id", result.get("id"));
                                map.put("parameter", "全部");
                                map.put("value",
                                        "全部," + result.get("value"));
                                map.put("color", "black");
                                selectList.add(map);
                                sparseArray.put(i, sContents);
                            }
                        } else {
                            selectList.clear();
                        }
                        handler.sendEmptyMessage(0);
                        if (beginCount == 0) {
                            //actualListView.setSelection(1);
                        }
                        mActivity.hideProcessDialog(0);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }, error -> mActivity.hideProcessDialog(1), null);
        mRequestQueue.add(request);
    }


    private TextView getTextView() {
        TextView tView = new TextView(mActivity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        layoutParams.bottomMargin = DensityUtil.dip2px(mActivity, 10);
        tView.setLayoutParams(layoutParams);
        tView.setSingleLine(true);
        tView.setTextColor(Color.BLACK);
        tView.setTextSize(14);
        tView.setGravity(Gravity.CENTER);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        return tView;
    }

    private TextView getTextViewRight() {
        TextView tView = new TextView(mActivity);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(DensityUtil.dip2px(mActivity, 200), Toolbar.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.rightMargin = DensityUtil.dip2px(mActivity, 10);
        tView.setLayoutParams(layoutParams);
        tView.setSingleLine(true);
        tView.setTextColor(getResources().getColor(R.color.text));
        tView.setTextSize(12);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        tView.setGravity(Gravity.RIGHT);
        tView.setCompoundDrawablePadding(DensityUtil.dip2px(mActivity, 10));
        tView.setCompoundDrawables(null, null, drawableRight, null);
        return tView;
    }

    private View getViewBottom() {
        View view = new View(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1);
        layoutParams.leftMargin = DensityUtil.dip2px(mActivity, 20);
        view.setLayoutParams(layoutParams);
        view.setBackgroundColor(getResources().getColor(R.color.border));
        return view;
    }

    private RelativeLayout getRelativeLayout(final int i) {
        RelativeLayout layout = new RelativeLayout(mActivity);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(mActivity, 50));
        layout.setLayoutParams(layoutParams);
        layout.setPadding(DensityUtil.dip2px(mActivity, 20), DensityUtil.dip2px(mActivity, 10), 0, 0);
        layout.setGravity(Gravity.CENTER_VERTICAL);
        layout.setBackgroundResource(R.drawable.goods_select_list_item);
        layout.setClickable(true);
        layout.setOnClickListener(v -> {
            iFirstPosition = i;
            Map<String, Object> map = selectList.get(i);
            sNames.clear();
            sNames.addAll(Arrays.asList(map.get("value").toString().split(",")));
            handler.sendEmptyMessage(1);
            tvBrand.setText(map.get("name").toString());
            drawerSub.openDrawer(Gravity.RIGHT);
        });
        return layout;
    }

}
