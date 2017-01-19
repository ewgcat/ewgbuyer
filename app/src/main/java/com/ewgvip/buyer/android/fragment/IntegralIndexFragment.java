package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.pulltorefresh.PullToRefreshBase;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    积分商城页面
 */
public class IntegralIndexFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;
    private List list = new ArrayList();
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_integration;
    private IntegralGoodsListViewAdapter mIntegralGoodsListViewAdapter;
    private int selectcount = 20;
    private String signStatus = "";
    private TextView tv_integral_count;
    private RelativeLayout rl_integral_screen;
    private List stringList = new ArrayList();
    private ListView popupListView;
    private PopupWindowListViewAdapter popupWindowListViewAdapter;
    private PopupWindow popupWindow;
    private TextView tv_integral_screen;
    private String rang_begin = "";
    private String rang_end = "";
    private ImageView iv_pull_status;
    private TextView tv_sign_integral;

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.list = null;
        this.lv_integration = null;
        this.mIntegralGoodsListViewAdapter = null;
        this.signStatus = null;
        this.tv_integral_count = null;
        this.rl_integral_screen = null;
        this.stringList = null;
        this.popupListView = null;
        this.popupWindowListViewAdapter = null;
        this.popupWindow = null;
        this.tv_integral_screen = null;
        this.rang_begin = null;
        this.rang_end = null;
        this.iv_pull_status = null;
        this.tv_sign_integral = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_integral_index, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("登录");//设置标题

        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        stringList.add("0-50");
        stringList.add("50~100");
        stringList.add("100~200");
        stringList.add("200~500");
        stringList.add("500以上");
        tv_integral_screen = (TextView) rootView.findViewById(R.id.tv_integral_screen);
        tv_integral_count = (TextView) rootView.findViewById(R.id.tv_integral_count);
        rootView.findViewById(R.id.rl_integral_count).setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_integral_detail();
            }
        });
        rootView.findViewById(R.id.tv_integral_count).setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_integral_detail();
            }
        });
        rootView.findViewById(R.id.rl_integral_exchange).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_order_integral_list();
            }
        });
        rootView.findViewById(R.id.tv_integral_exchange).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_order_integral_list();
            }
        });


        rl_integral_screen = (RelativeLayout) rootView.findViewById(R.id.rl_integral_screen);
        iv_pull_status = (ImageView) rootView.findViewById(R.id.iv_pull_status);
        tv_sign_integral = (TextView) rootView.findViewById(R.id.tv_sign_integral);
        popupListView = new ListView(mActivity);
        popupListView.setVerticalScrollBarEnabled(false);
        popupListView.setBackgroundResource(R.drawable.background_popupwindow);
        popupListView.setDividerHeight(1);
        popupWindowListViewAdapter = new PopupWindowListViewAdapter(mActivity, stringList);
        popupListView.setAdapter(popupWindowListViewAdapter);
        popupListView.setOnItemClickListener((adapterView, view, i, l) -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                popupWindow.dismiss();
                String price = (String) stringList.get(i);
                tv_integral_screen.setText(price);
                list.clear();
                if ("0-50".equals(price)) {
                    rang_begin = "0";
                    rang_end = "50";
                } else if ("50-100".equals(price)) {
                    rang_begin = "50";
                    rang_end = "100";
                } else if ("100-200".equals(price)) {
                    rang_begin = "100";
                    rang_end = "200";
                }else if ("200-500".equals(price)) {
                    rang_begin = "200";
                    rang_end = "500";
                } else if ("500以上".equals(price)) {
                    rang_begin = "500";
                    rang_end = "99999";
                }
                getData(rang_begin,rang_end);
            }
        });
        rl_integral_screen.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                iv_pull_status.setImageResource(R.mipmap.pull_up);
                if (popupWindow == null) {
                    popupWindow = new PopupWindow(popupListView, rl_integral_screen.getWidth(), rl_integral_screen.getHeight() * 5);
                    popupWindow.setOnDismissListener(() -> iv_pull_status.setImageResource(R.mipmap.pull_down));
                }
                popupWindow.setFocusable(true);
                popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.background_popupwindow));
                popupWindow.showAsDropDown(rl_integral_screen, 0, 0);
                popupWindow.setOutsideTouchable(true);
            }
        });

//        String integralString = "积分 " + mActivity.getCache("integral");
//        SpannableStringBuilder builder = new SpannableStringBuilder(integralString.toString());
//        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.RED);
//        builder.setSpan(redSpan, 2, integralString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        tv_integral_count.setText(builder);
        lv_integration = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) rootView.findViewById(R.id.listview);
//        lv_integration.setDividerDrawable(null);
        tv_sign_integral.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.go_sign_integral_goods();
            }
        });
        /**
         * 下拉刷新
         */
        lv_integration.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                if (FastDoubleClickTools.isFastDoubleClick()) {

                    new AsyncTask<Void, Void, String[]>() {
                        @Override
                        protected String[] doInBackground(Void... voids) {
                            list.clear();
                            getData(rang_begin,rang_end);
                            String[] str = {};
                            return str;
                        }

                        @Override
                        protected void onPostExecute(String[] result) {
                            super.onPostExecute(result);
                            lv_integration.onRefreshComplete();
                        }
                    }.execute();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

            }
        });

        /**
         * 底部更新
         */
        lv_integration.setOnLastItemVisibleListener(() -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                getData(rang_begin,rang_end);
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        getSignStatus();
        getData(rang_begin,rang_end);
        super.onResume();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getSignStatus();
        }
    }

    /**
     * 获取数据
     */
    private void getData(String rang_begin,String rang_end) {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        paraMap.put("begincount", list.size());
        paraMap.put("selectcount", selectcount);
        paraMap.put("rang_begin", rang_begin);
        paraMap.put("rang_end", rang_end);
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/integral_list.htm",
                result -> {
                    try {
                        if(result.has("integral")){
                            String integral=result.get("integral")+"";

                            tv_integral_count.setText("积分"+integral);
                        }
                        else {
                            String integral="0";
                        }
                        parseJSONObject(result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        mActivity.hideProcessDialog(0);
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
    }

    /**
     * 签到状态
     */
    private void getSignStatus() {
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Map paraMap = mActivity.getParaMap();
        Map map = new HashMap();
        map.put("userId", paraMap.get("user_id"));
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/get_sign_status.htm",
                result -> {
                    try {
                        String code = result.getString("code");
                        if ("200".equals(code)) {
                            String integralaviliable = result.getString("integralaviliable");
                            String desc = result.getString("desc");
                            signStatus = desc;
                        } else if ("500".equals(code)) {
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), map);
        mRequestQueue.add(request);
    }

    /**
     * 解析数据
     */
    private void parseJSONObject(JSONObject result) throws Exception {
        JSONArray arr = result.getJSONArray("recommend_igs");
        for (int i = 0; i < arr.length(); i++) {
            JSONObject obj = arr.getJSONObject(i);
            Map goodsMap = new HashMap();
            goodsMap.put("ig_id", obj.get("ig_id"));
            goodsMap.put("ig_goods_name", obj.get("ig_goods_name"));
            goodsMap.put("ig_goods_integral", obj.get("ig_goods_integral").toString());
            goodsMap.put("ig_user_level", obj.get("ig_user_level"));
            goodsMap.put("ig_goods_img", obj.get("ig_goods_img"));
            goodsMap.put("ig_goods_count", obj.get("ig_goods_count"));
            if (obj.has("ig_goods_pay_price")&& !TextUtils.isEmpty(obj.get("ig_goods_pay_price").toString().trim())){
                goodsMap.put("ig_goods_pay_price", obj.get("ig_goods_pay_price"));
            }
            goodsMap.put("type", "goodsData");
            list.add(goodsMap);
        }
        if (list.size() == 0) {
            lv_integration.setVisibility(View.GONE);
            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
        } else {
            lv_integration.setVisibility(View.VISIBLE);
            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
        }
        if (mIntegralGoodsListViewAdapter == null) {
            mIntegralGoodsListViewAdapter = new IntegralGoodsListViewAdapter(mActivity, list);
            lv_integration.setAdapter(mIntegralGoodsListViewAdapter);
        } else {
            mIntegralGoodsListViewAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private SimpleDraweeView iv_goods_icon;
        private TextView tv_goods_name;
        private TextView tv_goods_price;
        private TextView tv_goods_last;
        private TextView tv_goods_vip;
        private TextView tv_goods_price_money;
    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class PopupViewHolder {
        private TextView tv_integral_screen_item;
    }

    private class IntegralGoodsListViewAdapter extends BaseAdapter {

        /**
         * 环境变量
         */
        BaseActivity mActivity;
        /**
         * 保存传入的参数数据
         */
        private List list;
        /**
         * 打气筒对象
         */
        private LayoutInflater inflaterAdapter;

        /**
         * 积分等级
         */
        private Map userVIPMap = new HashMap();


        /**
         * 重写构造器
         */
        public IntegralGoodsListViewAdapter(BaseActivity mActivity, List list) {
            this.mActivity = mActivity;
            this.list = list;
            inflaterAdapter = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            userVIPMap.put(1, mActivity.getResources().getString(R.string.uservip0));
//            userVIPMap.put(2, mActivity.getResources().getString(R.string.uservip1));
//            userVIPMap.put(3, mActivity.getResources().getString(R.string.uservip2));
//            userVIPMap.put(4, mActivity.getResources().getString(R.string.uservip3));
        }


        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {

            return null;
        }

        @Override
        public long getItemId(int position) {

            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final Map map = (Map) list.get(position);
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflaterAdapter.inflate(R.layout.integralindexlistviewitem, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 160));
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.iv_goods_icon = (SimpleDraweeView) view.findViewById(R.id.iv_goods_icon);
                viewHolder.tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
                viewHolder.tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
                viewHolder.tv_goods_price_money = (TextView) view.findViewById(R.id.tv_goods_price_money);
                viewHolder.tv_goods_last = (TextView) view.findViewById(R.id.tv_goods_last);
                viewHolder.tv_goods_vip = (TextView) view.findViewById(R.id.tv_goods_vip);
                view.setTag(viewHolder);
            }
            String goods_img = (String) map.get("ig_goods_img");
            BaseActivity.displayImage(goods_img, viewHolder.iv_goods_icon);
            viewHolder.tv_goods_name.setText((String) map.get("ig_goods_name"));
             String ig_goods_integral = (String) map.get("ig_goods_integral");
            viewHolder.tv_goods_price.setText(ig_goods_integral +"积分");
            if (map.containsKey("ig_goods_pay_price")&&!TextUtils.isEmpty(map.get("ig_goods_pay_price").toString().trim())){
                viewHolder.tv_goods_price_money.setText("+"+ map.get("ig_goods_pay_price")+"元"      );
            }


 //           String userVIP = map.get("ig_user_level") + "";
//            Iterator iterator = userVIPMap.entrySet().iterator();
//            String VIPString = "";
//            Drawable vip_imge = null;
//            while (iterator.hasNext()) {
//                Map.Entry entry = (Map.Entry) iterator.next();
//                int string =  entry.getKey();
//                if (userVIP == string) {
//                    VIPString = (String) (userVIPMap.get(string));
//                    if (1 == string) {
//                        vip_imge = mActivity.getResources().getDrawable(R.mipmap.userlevel_0);
//                    } else if (2 == string) {
//                        vip_imge = mActivity.getResources().getDrawable(R.mipmap.userlevel_1);
//                    } else if (3 == string) {
//                        vip_imge = mActivity.getResources().getDrawable(R.mipmap.userlevel_2);
//                    } else if (4 == string) {
//                        vip_imge = mActivity.getResources().getDrawable(R.mipmap.userlevel_3);
//                    }
//                }
//            }
//            if (vip_imge != null) {
//                vip_imge.setBounds(0, 0, vip_imge.getMinimumWidth(), vip_imge.getMinimumHeight());
//                viewHolder.tv_goods_last.setCompoundDrawables(vip_imge, null, null, null);
//            }
//            viewHolder.tv_goods_last.setText(userVIP);
            view.setOnClickListener(view1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    int goods_id = (Integer) map.get("ig_id");
                    mActivity.go_integral_goods(String.valueOf(goods_id));
                }
            });
            return view;
        }

    }

    /**
     * 弹出框下拉列表的适配器
     */
    private class PopupWindowListViewAdapter extends BaseAdapter {

        /**
         * 环境变量
         */
        BaseActivity mActivity;
        /**
         * 保存传入的参数数据
         */
        private List list;
        /**
         * 打气筒对象
         */
        private LayoutInflater inflaterAdapter;

        /**
         * 重写构造器
         */
        public PopupWindowListViewAdapter(BaseActivity mActivity, List list) {
            this.mActivity = mActivity;
            this.list = list;
            inflaterAdapter = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return stringList.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            PopupViewHolder viewHolder = null;
            if (convertView != null) {
                view = convertView;
                viewHolder = (PopupViewHolder) view.getTag();
            } else {
                view = inflaterAdapter.inflate(R.layout.integralpopupwindowlistviewitem, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 48));
                view.setLayoutParams(lp);
                viewHolder = new PopupViewHolder();
                viewHolder.tv_integral_screen_item = (TextView) view.findViewById(R.id.tv_integral_screen_item);
                view.setTag(viewHolder);
            }

            viewHolder.tv_integral_screen_item.setText((String) stringList.get(position));
            return view;
        }
    }
}
