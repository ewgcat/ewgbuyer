package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.adapter.IndexListAdapter;
import com.ewgvip.buyer.android.layout.BadgeView;
import com.ewgvip.buyer.android.models.AcListBean;
import com.ewgvip.buyer.android.models.FlipperData;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页，加载首页楼层，启动后显示
 */
public class IndexFragment extends Fragment {

    public static IndexFragment fragment = null;
    private MainActivity mActivity;
    private View rootView;
    private RelativeLayout main_top_layout;
    private IndexListAdapter indexListAdapter;
    private List indexList;
    private List ad_list;
    private List nav_list;
    private PullToRefreshListView indexPullToRefreshListView;
    private ImageView go_top;//回到顶部按钮
    private boolean ishowgroup = false;   //判断是否显示团购
    private ListView listview;
    private View barge;
    public static BadgeView badge;

    private FlipperData mFlipperData;

    //静态工厂方法
    public static IndexFragment getInstance() {
        if (fragment == null) {
            fragment = new IndexFragment();
        }
        return fragment;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.fragment = null;
        this.mActivity = null;
        this.rootView = null;
        this.main_top_layout = null;
        this.indexListAdapter = null;
        this.indexList = null;
        this.ad_list = null;
        this.nav_list = null;
        this.indexPullToRefreshListView = null;
        this.go_top = null;
        this.listview = null;
        this.barge = null;
        this.badge = null;
        this.mFlipperData = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_index, container, false);
        // 未读消息数量提示
        barge = rootView.findViewById(R.id.barge);
        badge = new BadgeView(getActivity(), barge);

        ad_list = new ArrayList();
        nav_list = new ArrayList();
        indexList = new ArrayList();

        rootView.findViewById(R.id.scan_qr).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_scan_code();

            }
        });

        rootView.findViewById(R.id.message).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_message_list();
            }
        });
        rootView.findViewById(R.id.main_top_search).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_search();
            }
        });

        go_top = (ImageView) rootView.findViewById(R.id.fab);
        main_top_layout = (RelativeLayout) rootView.findViewById(R.id.main_top_layout);
        //首页信息界面
        indexPullToRefreshListView = (PullToRefreshListView) rootView.findViewById(R.id.index_list);
        indexPullToRefreshListView.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        indexPullToRefreshListView.setOnRefreshListener(refreshView -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                new GetDataTask().execute();
            }
        });

        indexPullToRefreshListView.setOnPullEventListener((refreshView, state, direction) -> {
            if (state == PullToRefreshBase.State.PULL_TO_REFRESH && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                rootView.findViewById(R.id.main_top_frame).setVisibility(View.GONE);
            } else if (state == PullToRefreshBase.State.RESET && direction == PullToRefreshBase.Mode.PULL_FROM_START) {
                rootView.findViewById(R.id.main_top_frame).setVisibility(View.VISIBLE);
            }

        });

        listview = indexPullToRefreshListView.getRefreshableView();
        initList();
        indexListAdapter = new IndexListAdapter(mActivity, indexList, ishowgroup, mFlipperData);
        listview.setAdapter(indexListAdapter);


        listview.setDivider(null);
        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 判断当前最上面显示的是不是头布局，因为Xlistview有刷新控件，所以头布局的位置是1，即第二个
                if (firstVisibleItem == 1) {
                    // 获取头布局
                    View top_view = listview.getChildAt(0);
                    if (top_view != null) {
                        // 获取头布局现在的最上部的位置的相反数
                        int top = -top_view.getTop();
                        // 获取头布局的高度
                        int headerHeight = top_view.getHeight();
                        // 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
                        if (top <= headerHeight && top >= 0) {
                            // 获取当前位置占头布局高度的百分比
                            float f = (float) top / (float) headerHeight;
                            int alhpa = (int) (f * 255);
                            if (alhpa >= 0 && alhpa <= 255) {
                                String str = Integer.toHexString(alhpa);
                                if (str.length() == 1)
                                    str = "0" + str;
                                main_top_layout.setBackgroundColor(Color.parseColor("#" + str + "db4437"));
                            } else
                                main_top_layout.setBackgroundColor(Color.parseColor("#ffdb4437"));
                            // 通知标题栏刷新显示
                            main_top_layout.invalidate();
                        }
                    }
                    go_top.setVisibility(View.GONE);
                } else if (firstVisibleItem > 1) {
                    main_top_layout.setBackgroundColor(Color.parseColor("#ffdb4437"));
                    go_top.setVisibility(View.VISIBLE);
                } else {
                    main_top_layout.setBackgroundColor(Color.parseColor("#00db4437"));
                    go_top.setVisibility(View.GONE);
                }
            }
        });

        //回到顶部按钮
        go_top.setVisibility(View.GONE);
        go_top.setOnClickListener(v -> listview.setSelection(1));

        return rootView;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        main_top_layout.setBackgroundResource(R.color.toolbar_color);
        main_top_layout.invalidate();
        initUnReadMessageNum();
    }

    @Override
    public void onPause() {
        super.onPause();
        main_top_layout.setBackgroundResource(R.color.toolbar_color);
        main_top_layout.invalidate();
    }

    @Override
    public void onStop() {
        super.onStop();
        main_top_layout.setBackgroundResource(R.color.toolbar_color);
        main_top_layout.invalidate();
    }

    @Override
    public void onResume() {
        super.onResume();
        getIndexNav();
    }

    // 未读消息数量提示提醒
    public static void setbadge(int num) {
        if (badge!=null&&badge.isShown()){
            badge.hide();
            if (num > 0) {
                badge.setText("" + num);
                badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                badge.toggle();
            }
        }
    }

    //未读消息请求
    private void initUnReadMessageNum() {
        if (mActivity.islogin()) {
            Map paramap = mActivity.getParaMap();
            paramap.put("beginCount", "0");
            paramap.put("selectCount", "100");
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(
                    getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/message.htm",
                    result -> {
                        try {
                            if (result != null && result.getString("ret").equals("true")) {
                                JSONArray msg_list = result.getJSONArray("msg_list");
                                int lenght = msg_list.length();
                                int unreadmessageNum = 0;
                                if (lenght > 0) {
                                    for (int i = 0; i < lenght; i++) {
                                        JSONObject obj = msg_list.getJSONObject(i);
                                        String status = obj.getString("status");
                                        if (status.equals("0")) {
                                            unreadmessageNum += 1;
                                        }
                                    }
                                }
                                if (unreadmessageNum > 0) {
                                    setbadge(unreadmessageNum);
                                } else {
                                    setbadge(0);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {Log.i("test", error.toString());}, paramap);
            mRequestQueue.add(request);
        }

    }

    //把缓存的数据先加载
    private void initList() {
        ad_list.clear();
        nav_list.clear();
        indexList.clear();
        //广告
        String str = mActivity.getCache("indexAds");
        if (!str.equals("")) {
            try {
                JSONObject result = new JSONObject(str);
                ad_list.clear();
                if (result.getInt("code") == 100) {//有广告加载广告
                    JSONArray jsonArray = result.getJSONArray("ad_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject obj = jsonArray.getJSONObject(i);
                        Map map = new HashMap();
                        map.put("id", obj.getString("id"));
                        map.put("img_url", obj.getString("img_url"));
                        map.put("click_url", obj.getString("click_url"));
                        ad_list.add(map);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //公告
        str = mActivity.getCache("mFlipperData");
        if (!str.equals("")) {
            try {
                JSONObject result = new JSONObject(str);
                JSONArray acList = result.getJSONArray("ac_list");
                List<AcListBean> ac_list = new ArrayList<AcListBean>();
                for (int i = 0; i < acList.length(); i++) {
                    JSONObject acListJSONObject = acList.getJSONObject(i);
                    ac_list.add(new AcListBean(acListJSONObject.getString("ac_url"), acListJSONObject.getString("title")));
                }
                mFlipperData = new FlipperData(result.getString("url"), ac_list);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        Map map1 = new HashMap();
        map1.put("type", "ad");
        map1.put("list", ad_list);
        if (indexList.size() >= 1) {
            indexList.remove(0);
            indexList.add(0, map1);
        } else {
            indexList.add(map1);
        }

        Map map2 = new HashMap();
        map2.put("type", "nav");
        map2.put("list", nav_list);
        if (indexList.size() >= 2) {
            indexList.remove(1);
            indexList.add(1, map2);
        } else {
            indexList.add(map2);
        }

        //楼层
        str = mActivity.getCache("indexFloors");
        if (!str.equals("")) {
            try {
                JSONObject result = new JSONObject(str);
                indexList = indexList.subList(0, 2);
                if (result.getInt("code") == 100) {//有数据就展示商品
                    JSONArray jsonArray = result.getJSONArray("floor_list");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject objOuter = jsonArray.getJSONObject(i);
                        if (objOuter.has("title")) {
                            Map map = new HashMap();
                            map.put("type", "title");
                            map.put("title", objOuter.get("title"));
                            indexList.add(map);
                        }
                        JSONArray innerArray = objOuter.getJSONArray("lines_info");
                        for (int j = 0; j < innerArray.length(); j++) {
                            JSONObject objInner = innerArray.getJSONObject(j);
                            JSONArray detailArray = objInner.getJSONArray("line_info");
                            Map line_map = new HashMap();
                            int line_type = objInner.getInt("line_type");
                            line_map.put("type", line_type);
                            List line_list = new ArrayList();
                            if (line_type == 2) {
                                for (int n = 0; n < detailArray.length(); n++) {
                                    JSONObject objDetail = detailArray.getJSONObject(n);
                                    Map map = new HashMap();
                                    map.put("url", objDetail.get("img_url"));
                                    map.put("click_type", objDetail.get("click_type"));
                                    map.put("click_info", objDetail.get("click_info"));
                                    map.put("height", objDetail.get("height"));
                                    map.put("width", objDetail.get("width"));
                                    if (objDetail.has("goods_name")){
                                        map.put("goods_name", objDetail.get("goods_name"));
                                    }
                                    if (objDetail.has("goods_sales_price")){
                                        map.put("goods_sales_price", objDetail.get("goods_sales_price"));
                                    }

                                    line_list.add(map);
                                }
                            } else {
                                for (int n = 0; n < detailArray.length(); n++) {
                                    JSONObject objDetail = detailArray.getJSONObject(n);
                                    Map map = new HashMap();
                                    map.put("url", objDetail.get("img_url"));
                                    map.put("click_type", objDetail.get("click_type"));
                                    map.put("click_info", objDetail.get("click_info"));
                                    map.put("height", objDetail.get("height"));
                                    map.put("width", objDetail.get("width"));
                                    line_list.add(map);
                                }
                            }
                            line_map.put("list", line_list);
                            indexList.add(line_map);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    //app首页幻灯广告
    private void getIndexAds() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/index_ad.htm",
                result -> {
                    String str = mActivity.getCache("indexAds");
                    String str2 = result.toString();
                    if (str.equals("") || !str.equals(str2)) {
                        mActivity.setCache("indexAds", str2);
                        initList();
                        indexListAdapter.notifyDataSetChanged();
                    }
                }, error -> indexPullToRefreshListView.onRefreshComplete(), null);
        mRequestQueue.add(request);
    }

    //公告
    private void getViewFlipperData() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/information.htm",
                result -> {
                    String str = mActivity.getCache("mFlipperData");
                    String str2 = result.toString();
                    if (str.equals("") || !str.equals(str2)) {
                        mActivity.setCache("mFlipperData", str2);
                        initList();
                        indexListAdapter.notifyDataSetChanged();
                    }
                }, error -> Log.i("test", error.toString()), null);
        mRequestQueue.add(request);
    }

    private void getIndexNav() {
        nav_list.clear();
        for (int i = 1; i <= 8; i++) {
            nav_list.add(mActivity.getCache("index_" + i));
        }
    }

    //楼层
    private void getIndexFloors() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity,
                CommonUtil.getAddress(mActivity) + "/app/index_floor.htm",
                result -> {
                    String str = mActivity.getCache("indexFloors");
                    String str2 = result.toString();
                    if (str.equals("") || !str.equals(str2)) {
                        mActivity.setCache("indexFloors", str2);
                        initList();
                        indexListAdapter.notifyDataSetChanged();
                    }
                    indexPullToRefreshListView.onRefreshComplete();

                }, error -> indexPullToRefreshListView.onRefreshComplete(), null);
        mRequestQueue.add(request);
    }


    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            // 这里可以写查询事件
            getIndexAds();
            getViewFlipperData();
            getIndexFloors();

            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息
            super.onPostExecute(result);
            initList();
            indexListAdapter.notifyDataSetChanged();

        }
    }

}
