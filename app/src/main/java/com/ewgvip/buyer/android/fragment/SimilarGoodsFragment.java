package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/13.
 * 足迹商品的相识商品列表页面
 */
public class SimilarGoodsFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private String goodsData;
    private List sameLikeGoodsDataList = new ArrayList();
    private ListView lv_foot_point_goods;


    public SimilarGoodsFragment() {
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
        this.goodsData = null;
        this.sameLikeGoodsDataList = null;
        this.lv_foot_point_goods = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_goods_similar, container, false);
        lv_foot_point_goods = (ListView) rootView.findViewById(R.id.lv_foot_point_goods);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("相似商品");
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

        goodsData = getArguments().getString("couponGoodsData");
        try {
            JSONArray jsonArray = new JSONArray(goodsData);
            if (jsonArray != null) {
                for (int i = 0; i <= jsonArray.length(); i++) {
                    JSONObject jsonArrayItem = jsonArray.getJSONObject(i);
                    Map map = new HashMap();
                    String goods_main_photo = jsonArrayItem.has("goods_main_photo") ? jsonArrayItem.getString("goods_main_photo") : "";
                    String id = jsonArrayItem.has("id") ? jsonArrayItem.getString("id") : "";
                    String name = jsonArrayItem.has("name") ? jsonArrayItem.getString("name") : "";
                    map.put("goods_main_photo", goods_main_photo);
                    map.put("id", id);
                    map.put("name", name);
                    sameLikeGoodsDataList.add(map);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        lv_foot_point_goods.setAdapter(new UserSameLikeGoodsAdapter(mActivity, sameLikeGoodsDataList));

        /**
         *详细商品
         */
        lv_foot_point_goods.setOnItemClickListener((adapterView, view, i, l) -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                Map map = (Map) sameLikeGoodsDataList.get(i);
                String goods_id = (String) map.get("id");
                mActivity.go_goods(goods_id);
            }
        });
        mActivity.hideProcessDialog(0);
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
     * 定义一个适配器的内部ViewHolder类
     */
    private static class ViewHolder {
        private SimpleDraweeView iv_goods_icon;
        private TextView tv_goods_name;
    }

    class UserSameLikeGoodsAdapter extends BaseAdapter {
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
        private LayoutInflater inflater;

        /**
         * 重写构造器
         */
        public UserSameLikeGoodsAdapter(BaseActivity mActivity, List list) {
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
            Map map = (Map) list.get(position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_user_like_hobby, null);
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT, MainActivity.dp2px(mActivity, 136));
                convertView.setLayoutParams(lp);
                viewHolder = new ViewHolder();
                viewHolder.iv_goods_icon = (SimpleDraweeView) convertView.findViewById(R.id.iv_goods_icon);
                viewHolder.tv_goods_name = (TextView) convertView.findViewById(R.id.tv_goods_name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String goods_img_path = (String) map.get("goods_main_photo");
            String goods_name = (String) map.get("name");
            BaseActivity.displayImage(goods_img_path, viewHolder.iv_goods_icon);
            viewHolder.tv_goods_name.setText(goods_name);
            return convertView;
        }
    }
}