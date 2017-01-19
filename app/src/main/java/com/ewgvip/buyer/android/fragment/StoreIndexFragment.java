package com.ewgvip.buyer.android.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.StoreIndexAdapter;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.utils.ScrollListener;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 店铺详情的主页面显示功能
 * 作者：张曼袁
 * 修改：张牧之
 */
public class StoreIndexFragment extends android.app.Fragment {
    //店铺首页头部完全隐藏
    private final int HEADER_VIEW_HIDE = 0;
    //店铺首页头部完全展示
    private final int HEADER_VIEW_ALL_SHOW = 1;
    //店铺首页头部完全隐藏部分
    private final int HEADER_VIEW_SHOW = 2;
    //店铺首页tablayout完全隐藏
    private final int HEADER_VIEW_TITLE_HIDE = 3;
    //店铺首页tablayout完全展示
    private final int HEADER_VIEW_TITLE_ALL_SHOW = 4;
    //店铺首页tablayout隐藏部分
    private final int HEADER_VIEW_TITLE_SHOW = 5;
    private View rootView;
    private BaseActivity mActivity;
    private String store_id = "";
    //TabHost数据
    private List<String> title_list = new ArrayList();
    //TabHost数据
    private List<Integer> title_list_img = new ArrayList();
    //店铺首页数据
    private TabLayout tabLayout;
    private ViewPager vp_list;
    private ImageView iv_select_store;
    private Boolean b;
    /**
     * 首页滑动添加变量
     */
    //按下时y坐标
    private int downY;
    //    private int moveY;
    //首页头部布局
    private RelativeLayout rl_headerView;
    private int rl_headerViewHeight;
    private int beforeDy = 1;
    //当前店铺首页头部展示状态
    private int current_headerView_status = HEADER_VIEW_ALL_SHOW;
    //店铺首页tablayout展示状态
    private int current_headerView_title_status = HEADER_VIEW_TITLE_ALL_SHOW;
    //店铺首页tablayout第一个试图
    private View view1;
    //店铺首页tablayout第二个试图
    private View view2;
    //店铺首页tablayout第三个试图
    private View view3;
    //店铺首页tablayout第四个试图
    private View view4;
    private int headerViewHeight;
    //显示页面
    private RelativeLayout rl_mainViewList;
    private boolean canRunnable = false;

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = android.app.Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        this.rootView = null;
        this.store_id = null;
        this.title_list = null;
        this.title_list_img = null;
        this.tabLayout = null;
        this.vp_list = null;
    }


    /**
     * 定义头部试图数据信息
     */
    private void initHeaderView() {
        rl_headerView = (RelativeLayout) rootView.findViewById(R.id.rl_headerView);
        rl_mainViewList = (RelativeLayout) rootView.findViewById(R.id.rl_mainViewList);
        //主动通知系统去测量该view;
        rl_headerView.measure(0, 0);
        rl_headerViewHeight = rl_headerView.getMeasuredHeight();
    }

    /**
     * 头部标题向上滑动
     */
    private void hideHeadertitleUp(int dy) {
        if (view1!=null){
            ImageView img1 = (ImageView) view1.findViewById(R.id.img_tab);
            ViewGroup.LayoutParams img1_lp1 = img1.getLayoutParams();
            int height = img1_lp1.height;
            if ((height - dy) <= 0) {
                img1_lp1.height = 0;
                current_headerView_title_status = HEADER_VIEW_TITLE_HIDE;
            } else if ((height - dy) > 0) {
                img1_lp1.height = height - dy;
                current_headerView_title_status = HEADER_VIEW_TITLE_SHOW;
            }
            img1.setLayoutParams(img1_lp1);

            if (view2!=null){
                ImageView img2 = (ImageView) view2.findViewById(R.id.img_tab);
                img2.setLayoutParams(img1_lp1);
            }
            if (view3!=null){
                ImageView img3 = (ImageView) view3.findViewById(R.id.img_tab);
                img3.setLayoutParams(img1_lp1);
            }
        }

    }

    /**
     * 头部标题向下滑动
     */
    private void hideHeadertitleDown(int dy) {
        ImageView img1 = (ImageView) view1.findViewById(R.id.img_tab);
        ImageView img2 = (ImageView) view2.findViewById(R.id.img_tab);
        ImageView img3 = (ImageView) view3.findViewById(R.id.img_tab);
//        ImageView img4 = (ImageView) view4.findViewById(R.id.img_tab);
        ViewGroup.LayoutParams img1_lp1 = img1.getLayoutParams();
        int height = img1_lp1.height;
        if (headerViewHeight > (height + dy)) {
            img1_lp1.height = height + dy;
            current_headerView_title_status = HEADER_VIEW_TITLE_SHOW;
        } else if (headerViewHeight <= (height + dy)) {
            img1_lp1.height = headerViewHeight;
            current_headerView_title_status = HEADER_VIEW_TITLE_ALL_SHOW;
        }
        img1.setLayoutParams(img1_lp1);
        img2.setLayoutParams(img1_lp1);
        img3.setLayoutParams(img1_lp1);
//        img4.setLayoutParams(img1_lp1);
    }

    /**
     * 头部背景信息向下滑动
     */
    private void hideHeaderUp(int dy) {
        ViewGroup.LayoutParams layoutParams = rl_headerView.getLayoutParams();
        int height = layoutParams.height;
        if ((height - dy) <= 0) {
            layoutParams.height = 0;
            current_headerView_status = HEADER_VIEW_HIDE;
        } else if ((height - dy) > 0) {
            layoutParams.height = height - dy;
            current_headerView_status = HEADER_VIEW_SHOW;
        }
        rl_headerView.setLayoutParams(layoutParams);
    }

    /**
     * 头部背景信息向下滑动
     */
    private void hideHeaderDown(int dy) {
        ViewGroup.LayoutParams layoutParams = rl_headerView.getLayoutParams();
        int height = layoutParams.height;
        if ((height + dy) >= rl_headerViewHeight) {
            layoutParams.height = rl_headerViewHeight;
            current_headerView_status = HEADER_VIEW_ALL_SHOW;
        } else if ((height + dy) < rl_headerViewHeight) {
            layoutParams.height = height + dy;
            current_headerView_status = HEADER_VIEW_SHOW;
        }
        rl_headerView.setLayoutParams(layoutParams);
    }

    /**
     * 获取标题头的试图信息
     *
     * @param position 每一个标题头的实际位置
     * @return
     */
    public View getTabView(int position) {
        View v = LayoutInflater.from(mActivity).inflate(R.layout.store_index_layout_tab, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_tab);
        tv.setText(title_list.get(position));
        ImageView img = (ImageView) v.findViewById(R.id.img_tab);
        img.setImageResource(title_list_img.get(position));
        /**
         * 获得标题栏试图对象
         */
        if (position == 0) {
            //主动通知系统去测量该view;
            img.measure(0, 0);
            headerViewHeight = img.getMeasuredHeight();
            view1 = v;
        } else if (position == 1) {
            view2 = v;
        } else if (position == 2) {
            view3 = v;
        } else if (position == 3) {
            v = rootView.findViewById(R.id.layout_store);
            view4 = v;
        }
        return v;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_store_index, container, false);
        mActivity = (BaseActivity) getActivity();
        initHeaderView();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("店铺首页");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);



        rootView.findViewById(R.id.layout_go_chat).setOnClickListener(view -> mActivity.go_chat(new Bundle()));
        ScrollListener scrollListener = new ScrollListener() {
            @Override
            public boolean setOnTouchListener(View view, MotionEvent motionEvent, LinearLayoutManager mLayoutManager) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        downY = (int) motionEvent.getY();
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int moveY = (int) motionEvent.getY();
                        int deltaY = (downY - moveY);
                        downY = moveY;
                        int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();
                        if (current_headerView_status == HEADER_VIEW_ALL_SHOW && current_headerView_title_status != HEADER_VIEW_TITLE_ALL_SHOW) {
                            if (deltaY > 0) {
                                return true;
                            }
                        }
                        if (deltaY > 0) {
                            if (firstVisibleItem < 2 && current_headerView_status != HEADER_VIEW_HIDE) {
                                hideHeaderUp(deltaY / 2);
                                canRunnable = true;
                                return true;
                            } else if (firstVisibleItem < 2 && current_headerView_status != HEADER_VIEW_HIDE) {
                                hideHeadertitleUp(deltaY / 3);
                                canRunnable = true;
                                return true;
                            } else if (firstVisibleItem >= 2 && current_headerView_status != HEADER_VIEW_HIDE) {
                                hideHeaderUp(deltaY / 2);
                                canRunnable = true;
                            } else if (firstVisibleItem >= 2 && current_headerView_title_status != HEADER_VIEW_TITLE_HIDE) {
                                hideHeadertitleUp(deltaY / 3);
                                canRunnable = true;
                            }
                            canRunnable = false;
                        } else if (deltaY < 0) {
                            deltaY = Math.abs(deltaY);
                            if (firstVisibleItem < 2 && current_headerView_title_status != HEADER_VIEW_TITLE_ALL_SHOW) {
                                hideHeadertitleDown(deltaY / 3);
                                canRunnable = true;
                                return true;
                            } else if (firstVisibleItem < 2 && current_headerView_status != HEADER_VIEW_ALL_SHOW) {
                                hideHeaderDown(deltaY / 2);
                                canRunnable = true;
                                return true;
                            }
                            canRunnable = false;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (canRunnable) {
                            return true;
                        } else {
                            return false;
                        }
                }
                return false;
            }

            @Override
            public void setScrollListener(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager mLayoutManager) {
            }

            @Override
            public void setScrollListener(RecyclerView recyclerView, int dx, int dy, Fragment fragment, LinearLayoutManager mLayoutManager, boolean isLoadingMore, String orderBy, String orderType) {
            }
        };

        if (scrollListener != null) {
            rl_mainViewList.setOnTouchListener((view, motionEvent) -> false);
        }
        /**
         * 店铺收藏按钮，收藏店铺
         */
        iv_select_store = (ImageView) rootView.findViewById(R.id.iv_select_store);
        iv_select_store.setOnClickListener(view -> {
            Map paramap = mActivity.getParaMap();
            paramap.put("store_id", store_id);
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(getActivity(), mActivity.getAddress() + "/app/add_store_favorite.htm",
                    result -> {
                        try {
                            String code = result.has("code") ? result.getString("code") : "";
                            if ("0".equals(code)) {
                                if (b != null) {
                                    if (b == true) {
                                        iv_select_store.setImageResource(R.mipmap.sellect);
                                        Toast.makeText(mActivity, "取消收藏！", Toast.LENGTH_SHORT).show();
                                        b = false;
                                    } else {
                                        iv_select_store.setImageResource(R.mipmap.cellect);
                                        Toast.makeText(mActivity, "收藏成功！", Toast.LENGTH_SHORT).show();
                                        b = true;
                                    }
                                }
                            } else {
                                Toast.makeText(mActivity, "", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.i("test",error.toString()), paramap);
            mRequestQueue.add(request);
        });

        Bundle bundle = getArguments();
        store_id = bundle.getString("store_id");
        tabLayout = (TabLayout) rootView.findViewById(R.id.tl_store);
        vp_list = (ViewPager) rootView.findViewById(R.id.vp_list);
        title_list_img.add(R.mipmap.store);
        title_list_img.add(R.mipmap.all);
        title_list_img.add(R.mipmap.new_good);
        title_list_img.add(R.mipmap.contect);
        title_list.add("店铺首页");
        title_list.add("全部宝贝");
        title_list.add("新品上架");
        title_list.add("店铺活动");
        List<android.support.v4.app.Fragment> fragment_list = new ArrayList();
        StorePager1Fragment f1 = StorePager1Fragment.getInstance(store_id);
        f1.setScrollListener(scrollListener);
        fragment_list.add(f1);
        StorePager2Fragment f2 = StorePager2Fragment.getInstance(store_id);
        f2.setScrollListener(scrollListener);
        fragment_list.add(f2);
        StorePager3Fragment f3 = StorePager3Fragment.getInstance(store_id);
        f3.setScrollListener(scrollListener);
        fragment_list.add(f3);
        //StorePager4Fragment f4 = StorePager4Fragment.getInstance(store_id);
//        f4.setScrollListener(scrollListener);
//        fragment_list.add(f4);
        StoreIndexAdapter storeFirstAdapter = new StoreIndexAdapter(((BaseActivity) getActivity()).getSupportFragmentManager(), mActivity, title_list, fragment_list);

        vp_list.setAdapter(storeFirstAdapter);
        //解决tablayout的延迟执行的bug
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                if (vp_list.getAdapter()!=null&&vp_list.getAdapter().getCount()>0){
                    tabLayout.setupWithViewPager(vp_list);
                }
            }
        });
        //重绘视图
        vp_list.setOffscreenPageLimit(3);


        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        // 加载用户信息
        init();
        return rootView;
    }

    public void init() {

        Map paramap = mActivity.getParaMap();
        paramap.put("store_id", store_id);
        RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress() + "/app/store_info.htm", paramap, new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    SimpleDraweeView store_logo = (SimpleDraweeView) rootView.findViewById(R.id.store_logo);
                    BaseActivity.displayImage(jsonObject.getString("store_logo"), store_logo);
                    TextView textView = (TextView) rootView.findViewById(R.id.tv_storename);
                    textView.setText(jsonObject.getString("store_name"));
                    textView = (TextView) rootView.findViewById(R.id.tv_storeinfo);
                    textView.setText(jsonObject.getString("fans_count") + "人关注");
                    if (jsonObject.get("favourited").toString().equals("1")) {
                        iv_select_store.setImageResource(R.mipmap.cellect);
                        b = true;
                    } else {
                        iv_select_store.setImageResource(R.mipmap.sellect);
                        b = false;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

}