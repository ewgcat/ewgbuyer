package com.ewgvip.buyer.android.fragment;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.dialog.MyDialog;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 用户足迹商品显示列表
 */
public class FootprintsFragment extends Fragment {
    Dialog alertDialog;
    private BaseActivity mActivity;
    private View rootView;
    private RequestQueue mRequestQueue;
    //    private AlertDialog alertDialog;
    private com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView lv_foot_point_goods;
    private List userFootPointGoodsList = new ArrayList();
    private List userFootPointGoodsList1 = new ArrayList();
    private UserFootPointGoodAdapter userFootPointGoodAdapter;
    private TextView tv_foot_point_title;
    /**
     * 刷新参数
     */
    //开始的天数
    private long beginCount = 0;
    //结束的天数
    private long selectCount = 20;
    private int clickPosition;

    public FootprintsFragment() {
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.mRequestQueue = null;
        this.alertDialog = null;
        this.lv_foot_point_goods = null;
        this.userFootPointGoodsList = null;
        this.userFootPointGoodAdapter = null;
        this.tv_foot_point_title = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_footprints, container, false);
        mActivity = (BaseActivity) getActivity();
        mActivity.showProcessDialog();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("足迹");
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
        lv_foot_point_goods = (com.ewgvip.buyer.android.pulltorefresh.PullToRefreshListView) rootView.findViewById(R.id.lv_foot_point_goods);
//        tv_foot_point_title = (TextView) rootView.findViewById(R.id.tv_foot_point_title);
        mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        fillCouponGoodsDataView();
        lv_foot_point_goods.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userFootPointGoodsList != null && userFootPointGoodsList.size() > 0) {
                    Map map = (Map) userFootPointGoodsList.get(firstVisibleItem);
                    String type = (String) map.get("type");
                    if ("title".equals(type)) {
                        String titleString = (String) map.get("fp_date");
//                        tv_foot_point_title.setText(titleString);
                    }
                }
            }
        });
        /**
         * 下拉刷新
         */
        lv_foot_point_goods.setOnRefreshListener(refreshView -> {
            beginCount = 0;
            selectCount = 20;
            userFootPointGoodsList.clear();
            new GetDataTask().execute();
        });

        /**
         * 底部更新
         */
        lv_foot_point_goods.setOnLastItemVisibleListener(() -> fillCouponGoodsDataView());
        return rootView;
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
    /**
     * 填充列表的展示数据
     */
    private void fillCouponGoodsDataView() {
        String address = getResources().getString(R.string.http_url);
        String url = address + "/app/buyer/foot_point.htm";
        Map paraMap = mActivity.getParaMap();
        paraMap.put("beginCount", beginCount);
        paraMap.put("endCount", beginCount + selectCount);
        beginCount = beginCount + selectCount;
        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, url,
                result -> {
                    try {
                        parseFillDataJsonResponse(result);
                        if (userFootPointGoodAdapter == null) {
                            userFootPointGoodAdapter = new UserFootPointGoodAdapter(mActivity, userFootPointGoodsList);
                            lv_foot_point_goods.setAdapter(userFootPointGoodAdapter);
                        } else {
                            userFootPointGoodAdapter.notifyDataSetChanged();
                        }
                        if (userFootPointGoodsList.size() == 0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    fillCouponGoodsDataView();
                                }
                            });
                            lv_foot_point_goods.setVisibility(View.GONE);
                        }else{
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            lv_foot_point_goods.setVisibility(View.VISIBLE);
                        }
                        mActivity.hideProcessDialog(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    lv_foot_point_goods.onRefreshComplete();
                }, error -> {
                    Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show();
                    mActivity.hideProcessDialog(1);
                }, paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }

    /**
     * 解析数据
     */
    private void parseFillDataJsonResponse(JSONObject result) throws JSONException {
        JSONArray jsonArray = result.getJSONArray("footpoint_list");
        if (null != jsonArray) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectItem = jsonArray.getJSONObject(i);
                Map map = new HashMap();
                String id = jsonObjectItem.has("id") ? jsonObjectItem.getString("id") : "";
                String fp_date = jsonObjectItem.has("fp_date") ? jsonObjectItem.getString("fp_date") : "";
                String fp_goods_count = jsonObjectItem.has("fp_goods_count") ? jsonObjectItem.getString("fp_goods_count") : "";
                JSONArray fpv_list = jsonObjectItem.has("fpv_list") ? jsonObjectItem.getJSONArray("fpv_list") : null;
                if (fpv_list != null) {
                    for (int j = 0; j < fpv_list.length(); j++) {
                        JSONObject fpv_list_item = fpv_list.getJSONObject(j);
                        if (j == 0) {
                            map.put("type", "title");
                            map.put("id", id);
                            map.put("fp_date", fp_date);
                            map.put("fp_goods_count", fp_goods_count);
                            userFootPointGoodsList.add(map);
                        }
                        Map footPointGoodsMap = new HashMap();
                        String goods_class_id = fpv_list_item.has("goods_class_id") ? fpv_list_item.getString("goods_class_id") : "";
                        String goods_class_name = fpv_list_item.has("goods_class_name") ? fpv_list_item.getString("goods_class_name") : "";
                        String goods_id = fpv_list_item.has("goods_id") ? fpv_list_item.getString("goods_id") : "";
                        String goods_img_path = fpv_list_item.has("goods_img_path") ? fpv_list_item.getString("goods_img_path") : "";
                        String goods_name = fpv_list_item.has("goods_name") ? fpv_list_item.getString("goods_name") : "";
                        String goods_price = fpv_list_item.has("goods_price") ? fpv_list_item.getString("goods_price") : "";
                        String goods_sale = fpv_list_item.has("goods_sale") ? fpv_list_item.getString("goods_sale") : "";
                        String goods_time = fpv_list_item.has("goods_time") ? fpv_list_item.getString("goods_time") : "";
                        footPointGoodsMap.put("goods_class_id", goods_class_id);
                        footPointGoodsMap.put("goods_class_name", goods_class_name);
                        footPointGoodsMap.put("goods_id", goods_id);
                        footPointGoodsMap.put("goods_img_path", goods_img_path);
                        footPointGoodsMap.put("goods_name", goods_name);
                        footPointGoodsMap.put("goods_price", goods_price);
                        footPointGoodsMap.put("goods_sale", goods_sale);
                        footPointGoodsMap.put("goods_time", goods_time);
                        footPointGoodsMap.put("type", "goods");
                        footPointGoodsMap.put("id", id);
                        footPointGoodsMap.put("fp_date", fp_date);
                        footPointGoodsMap.put("fp_goods_count", fp_goods_count);
                        userFootPointGoodsList.add(footPointGoodsMap);
                    }
                }
            }
        }
    }


    /**
     * 删除全天的商品足迹信息
     */
    private void deleteAllDayFootPoint(final Map map, final String date, final int i) {
        String address = getResources().getString(R.string.http_url);
        String url = address + "/app/buyer/foot_point_remove.htm";
        Map paraMap = mActivity.getParaMap();
        paraMap.put("date", date);

        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, url,
                result -> {
                    try {
                        String ret_string = result.has("ret") ? result.getString("ret") : "";
                        int ret = Integer.parseInt(ret_string);
                        if (0 == ret) {
                            Toast.makeText(mActivity, "足迹商品未删除成功", Toast.LENGTH_SHORT).show();
                        } else if (1 == ret) {
                            Iterator it = userFootPointGoodsList.iterator();
                            while (it.hasNext()) {
                                Map map1 = (Map) it.next();
                                String fp_date_string = (String) map1.get("fp_date");
                                if (date.equals(fp_date_string)) {
                                    it.remove();

                                }
                            }
                        } else if (2 == ret) {
                            Iterator it = userFootPointGoodsList.iterator();
                            while (it.hasNext()) {
                                Map map1 = (Map) it.next();
                                String fp_date_string = (String) map1.get("fp_date");
                                if (date.equals(fp_date_string)) {
                                    it.remove();
                                }
                            }
                        }
                        if (userFootPointGoodsList.size() == 0) {
                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view -> fillCouponGoodsDataView());
                            lv_foot_point_goods.setVisibility(View.GONE);
                        }else{
                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                            lv_foot_point_goods.setVisibility(View.VISIBLE);
                        }
                        userFootPointGoodAdapter.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show(), paraMap);
        mRequestQueue.add(couponRequestJSONObject);
    }

    private void showAlertDialog() {
        String[] arr = {"找相似", "删除"};
        alertDialog = MyDialog.showAlert(mActivity, "操作", arr, (adapterView, view, i, l) -> {
            if (i == 0){
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    final Map map = (Map) userFootPointGoodsList.get(clickPosition);
                    String address = getResources().getString(R.string.http_url);
                    String url = address + "/app/like_goods_list.htm";
                    Map paraMap = mActivity.getParaMap();
                    Map mapHobbyLike = new HashMap();
                    String goods_id = (String) map.get("goods_id");
                    paraMap.put("id", goods_id);
                    mapHobbyLike.put("id", goods_id);
                    NormalPostRequest couponRequestJSONObject1 = new NormalPostRequest(mActivity, url,
                            result -> {
                                try {
                                    boolean ret = result.has("ret") ? result.getBoolean("ret") : false;
                                    if (ret) {
                                        Bundle bundle = new Bundle();
                                        JSONArray goodsDataArray = result.getJSONArray("goods_list");
                                        bundle.putString("couponGoodsData", goodsDataArray.toString());
                                        mActivity.go_foot_point_same_like(bundle);
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                alertDialog.dismiss();
                            }, error -> Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show(), mapHobbyLike);
                    mRequestQueue.add(couponRequestJSONObject1);
                }
            }
            if (i == 1)//删除
            {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    final Map map = (Map) userFootPointGoodsList.get(clickPosition);
                    String type = (String) map.get("type");
                    if ("title".equals(type)) {
                        Toast.makeText(mActivity, "不删除信息", Toast.LENGTH_SHORT).show();
                        return;
                    } else if ("goods".equals(type)) {
                        String address1 = getResources().getString(R.string.http_url);
                        String url1 = address1 + "/app/buyer/foot_point_remove.htm";
                        Map paraMap1 = mActivity.getParaMap();
                        String goods_id1 = (String) map.get("goods_id");
                        String date_goods_string = (String) map.get("fp_date");
                        paraMap1.put("goods_id", goods_id1);
                        paraMap1.put("date", date_goods_string);

                        NormalPostRequest couponRequestJSONObject = new NormalPostRequest(mActivity, url1,
                                result -> {
                                    try {
                                        String ret_string = result.has("ret") ? result.getString("ret") : "";
                                        int ret = Integer.parseInt(ret_string);
                                        if (0 == ret) {
                                            Toast.makeText(mActivity, "足迹商品未删除成功", Toast.LENGTH_SHORT).show();
                                        } else if (1 == ret) {
                                            if (userFootPointGoodsList.size() == 2 && clickPosition == 1) {
                                                userFootPointGoodsList.clear();
                                            } else if (userFootPointGoodsList.size() > 2 && clickPosition == (userFootPointGoodsList.size() - 1)) {
                                                Map previousMap = (Map) userFootPointGoodsList.get(clickPosition - 1);
                                                String previousTypeString = (String) previousMap.get("type");
                                                if ("title".equals(previousTypeString) && clickPosition == (userFootPointGoodsList.size() - 1)) {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                    userFootPointGoodsList.remove(clickPosition - 1);
                                                } else {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                }
                                            } else {
                                                Map previousMap = (Map) userFootPointGoodsList.get(clickPosition - 1);
                                                String previousTypeString = (String) previousMap.get("type");
                                                Map nextMap = (Map) userFootPointGoodsList.get(clickPosition + 1);
                                                String nextTypeString = (String) nextMap.get("type");
                                                if ("title".equals(previousTypeString) && "title".equals(nextTypeString)) {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                    userFootPointGoodsList.remove(clickPosition - 1);
                                                } else {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                }
                                            }
                                        } else if (2 == ret) {
                                            if (userFootPointGoodsList.size() == 2 && clickPosition == 1) {
                                                userFootPointGoodsList.clear();
                                            } else if (userFootPointGoodsList.size() > 2 && clickPosition == (userFootPointGoodsList.size() - 1)) {
                                                Map previousMap = (Map) userFootPointGoodsList.get(clickPosition - 1);
                                                String previousTypeString = (String) previousMap.get("type");
                                                if ("title".equals(previousTypeString) && clickPosition == (userFootPointGoodsList.size() - 1)) {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                    userFootPointGoodsList.remove(clickPosition - 1);
                                                } else {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                }
                                            } else {
                                                Map previousMap = (Map) userFootPointGoodsList.get(clickPosition - 1);
                                                String previousTypeString = (String) previousMap.get("type");
                                                Map nextMap = (Map) userFootPointGoodsList.get(clickPosition + 1);
                                                String nextTypeString = (String) nextMap.get("type");
                                                if ("title".equals(previousTypeString) && "title".equals(nextTypeString)) {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                    userFootPointGoodsList.remove(clickPosition - 1);
                                                } else {
                                                    userFootPointGoodsList.remove(clickPosition);
                                                }
                                            }
                                        }
                                        if (userFootPointGoodsList.size() == 0) {
                                            rootView.findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                                            rootView.findViewById(R.id.nodata_refresh).setOnClickListener(view1 -> fillCouponGoodsDataView());
                                            lv_foot_point_goods.setVisibility(View.GONE);
                                        }else{
                                            rootView.findViewById(R.id.nodata).setVisibility(View.GONE);
                                            lv_foot_point_goods.setVisibility(View.VISIBLE);
                                        }
                                        userFootPointGoodAdapter.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> Toast.makeText(mActivity, "网络错误", Toast.LENGTH_SHORT).show(), paraMap1);
                        mRequestQueue.add(couponRequestJSONObject);
                    }
                    alertDialog.dismiss();
                }
            }
        });

    }

    /**
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private SimpleDraweeView iv_goods_icon;
        private TextView tv_goods_name;
        private TextView tv_goods_price;
        private TextView tv_delete;
        private TextView tv_same_like;
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            String[] str = {};
            fillCouponGoodsDataView();
            return str;
        }

        @Override
        protected void onPostExecute(String[] result) {
            // 添加新加载的信息
            super.onPostExecute(result);
        }
    }

    /**
     * 足迹适配器
     */
    class UserFootPointGoodAdapter extends BaseAdapter {

        /**
         * 环境变量
         */
        BaseActivity mActivity;
        /**
         * 保存传入的参数数据
         */
        private List list;
        /**
         * 积分状态
         */
        private Map integrationTypeMap = new HashMap();
        /**
         * 打气筒对象
         */
        private LayoutInflater inflater;

        /**
         * 重写构造器
         */
        public UserFootPointGoodAdapter(BaseActivity mActivity, List list) {
            this.mActivity = mActivity;
            this.list = list;
            inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
            final int i = position;
            final Map map = (Map) list.get(position);
            String type = (String) map.get("type");
            if ("title".equals(type)) {
                View view = null;
                if (0 == position) {
                    view = inflater.inflate(R.layout.item_user_like_hobby_today_title, null);
                } else {
                    view = inflater.inflate(R.layout.item_user_like_hobby_title, null);
                }
                TextView tv_day = (TextView) view.findViewById(R.id.tv_day);
                Button tv_remove_all = (Button) view.findViewById(R.id.tv_remove_all);
                final String footPointDate = (String) map.get("fp_date");
                String[] footDate=footPointDate.split(" ");
                String footTime=footDate[0];
                SimpleDateFormat simpleDateTime = new SimpleDateFormat("yyyy-MM-dd");
                Date nowDate = new Date();
                String nowDateString = simpleDateTime.format(nowDate);
                Date lastGoodsDate;
                Date currentSystemTime;
                try {
                    lastGoodsDate = simpleDateTime.parse(footPointDate);
                    currentSystemTime = simpleDateTime.parse(nowDateString);
                    if (currentSystemTime.getTime() == lastGoodsDate.getTime()) {
                        tv_day.setText("今天");
                    } else {
                        tv_day.setText(footTime);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tv_remove_all.setOnClickListener(view1 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        deleteAllDayFootPoint(map, footPointDate, i);
                    }
                });
                return view;
            }
            View view = null;
            ViewHolder viewHolder = null;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                viewHolder = (ViewHolder) view.getTag();
            } else {
                view = inflater.inflate(R.layout.item_user_like_hobby, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 136));
                view.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.iv_goods_icon = (SimpleDraweeView) view.findViewById(R.id.iv_goods_icon);
                viewHolder.tv_goods_name = (TextView) view.findViewById(R.id.tv_goods_name);
                viewHolder.tv_goods_price = (TextView) view.findViewById(R.id.tv_goods_price);
                viewHolder.tv_delete = (TextView) view.findViewById(R.id.tv_delete);
                viewHolder.tv_same_like = (TextView) view.findViewById(R.id.tv_same_like);
                view.setTag(viewHolder);
            }
            String goods_img_path = (String) map.get("goods_img_path");
            String goods_name = (String) map.get("goods_name");
            String goods_price = (String) map.get("goods_price");
            BaseActivity.displayImage(goods_img_path, viewHolder.iv_goods_icon);
            viewHolder.tv_goods_name.setText(goods_name);
            viewHolder.tv_goods_price.setText("￥" + goods_price);
            view.setOnClickListener(view12 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    String goods_id = (String) map.get("goods_id");
                    mActivity.go_goods(goods_id);
                }
            });
            view.setOnLongClickListener(view13 -> {
                clickPosition = i;
                showAlertDialog();
                return true;
            });
            return view;
        }
    }
}