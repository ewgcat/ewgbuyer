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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.CouponGoodsData;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/10/13.
 * 优惠券领取成功页面推荐优惠券可以购买的商品
 */
public class CouponsGetSuccessFragment extends Fragment {

    private BaseActivity mActivity;
    private View rootView;
    private String goodsData;
    private List<CouponGoodsData> couponGoodsDataList = new ArrayList<CouponGoodsData>();
    /**
     * 页面视图
     */
    private LayoutInflater inflater;
    private LinearLayout ll_coupon_goods;
    private LinearLayout iv_index;

    public CouponsGetSuccessFragment() {
    }

    @Override
    public void onDetach() {
        super.onDetach();

        this.rootView = null;
        this.goodsData = null;
        this.couponGoodsDataList = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_coupon_get_success, container, false);
        initView();
        initData();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置标题
        toolbar.setTitle("领取优惠券");
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if(!getTargetFragment().equals(null)) {
                    getTargetFragment().onActivityResult(getTargetRequestCode(), 0, null);
                    getFragmentManager().popBackStack();
                }
//                    mActivity.onBackPressed();
            }
        });
        //设置菜单可用
        setHasOptionsMenu(true);
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
     * 初始化试图
     */
    private void initView() {
        ll_coupon_goods = (LinearLayout) rootView.findViewById(R.id.ll_coupon_goods);
    }

    /**
     * 初始化数据
     */
    private void initData() {
        mActivity = (BaseActivity) getActivity();
        /**
         * 优惠券领取成功推荐优惠券可以购买的商品详细信息队列
         */
        inflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        goodsData = getArguments().getString("couponGoodsData");
        /**
         * 解析数据
         */
        try {
            JSONArray jsonArray = new JSONArray(goodsData);
            parseCouponGoodsDataJSONArray(jsonArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (couponGoodsDataList != null && couponGoodsDataList.size() > 0) {
            if (couponGoodsDataList.size() % 2 == 1) {
                for (int i = 0; i < couponGoodsDataList.size(); i += 2) {
                    if (couponGoodsDataList.size() - 1 == i) {
                        View view = inflater.inflate(R.layout.item_coupon_goods, null);
                        SimpleDraweeView iv_coupon_goods_pic0 = (SimpleDraweeView) view.findViewById(R.id.iv_coupon_goods_pic0);
                        TextView tv_coupon_name0 = (TextView) view.findViewById(R.id.tv_coupon_name0);
                        TextView tv_coupon_price0 = (TextView) view.findViewById(R.id.tv_coupon_price0);
                        BaseActivity.displayImage(couponGoodsDataList.get(i).getGoods_pic(), iv_coupon_goods_pic0);
                        tv_coupon_name0.setText(couponGoodsDataList.get(i).getGoods_name());
                        tv_coupon_price0.setText("￥" + couponGoodsDataList.get(i).getGoods_price());
                        SimpleDraweeView iv_coupon_goods_pic1 = (SimpleDraweeView) view.findViewById(R.id.iv_coupon_goods_pic1);
                        TextView tv_coupon_name1 = (TextView) view.findViewById(R.id.tv_coupon_name1);
                        TextView tv_coupon_price1 = (TextView) view.findViewById(R.id.tv_coupon_price1);
                        BaseActivity.displayImage(couponGoodsDataList.get(i).getGoods_pic(), iv_coupon_goods_pic1);
                        tv_coupon_name1.setText(couponGoodsDataList.get(i).getGoods_name());
                        tv_coupon_price1.setText("￥" + couponGoodsDataList.get(i).getGoods_price());
                        LinearLayout ll_left_goods = (LinearLayout) view.findViewById(R.id.ll_left_goods);
                        ll_left_goods.setOnClickListener(view1 -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                String goods_id = couponGoodsDataList.get(couponGoodsDataList.size() - 1).getGoods_id();
                                mActivity.go_goods(goods_id);
                            }
                        });
                        LinearLayout ll_right_goods = (LinearLayout) view.findViewById(R.id.ll_right_goods);
                        ll_right_goods.setOnClickListener(view12 -> {
                            if (FastDoubleClickTools.isFastDoubleClick()) {
                                String goods_id = couponGoodsDataList.get(couponGoodsDataList.size() - 1).getGoods_id();
                                mActivity.go_goods(goods_id);
                            }
                        });
                        ll_right_goods.setVisibility(View.INVISIBLE);
                        iv_coupon_goods_pic1.setVisibility(View.INVISIBLE);
                        tv_coupon_name1.setVisibility(View.INVISIBLE);
                        tv_coupon_price1.setVisibility(View.INVISIBLE);
                        ll_coupon_goods.addView(view);
                    } else {
                        fillCouponGoodsDataView(i);
                    }
                }
            } else if (couponGoodsDataList.size() % 2 == 0) {
                for (int i = 0; i < couponGoodsDataList.size(); i += 2) {
                    fillCouponGoodsDataView(i);
                }
            }
        }
    }

    /**
     * 解析数据
     */
    private void parseCouponGoodsDataJSONArray(JSONArray jsonArray) throws JSONException {
        if (jsonArray != null) {
            for (int i = 0; i <= jsonArray.length(); i++) {
                JSONObject jsonArrayItem = jsonArray.getJSONObject(i);
                CouponGoodsData couponGoodsData = new CouponGoodsData();
                String goods_pic_string = jsonArrayItem.has("goods_pic") ? jsonArrayItem.getString("goods_pic") : "";
                String goods_name_string = jsonArrayItem.has("goods_name") ? jsonArrayItem.getString("goods_name") : "";
                String goods_price_string = jsonArrayItem.has("goods_price") ? jsonArrayItem.getString("goods_price") : "";
                String goods_id_string = jsonArrayItem.has("goods_id") ? jsonArrayItem.getString("goods_id") : "";
                couponGoodsData.setGoods_pic(goods_pic_string);
                couponGoodsData.setGoods_name(goods_name_string);
                couponGoodsData.setGoods_price(goods_price_string);
                couponGoodsData.setGoods_id(goods_id_string);
                couponGoodsDataList.add(couponGoodsData);
            }
        }
    }

    /**
     * 填充优惠券推荐商品的列表展示
     */
    private void fillCouponGoodsDataView(final int i) {
        View view = inflater.inflate(R.layout.item_coupon_goods, null);
        SimpleDraweeView iv_coupon_goods_pic0 = (SimpleDraweeView) view.findViewById(R.id.iv_coupon_goods_pic0);
        TextView tv_coupon_name0 = (TextView) view.findViewById(R.id.tv_coupon_name0);
        TextView tv_coupon_price0 = (TextView) view.findViewById(R.id.tv_coupon_price0);
        BaseActivity.displayImage(couponGoodsDataList.get(i).getGoods_pic(), iv_coupon_goods_pic0);
        tv_coupon_name0.setText(couponGoodsDataList.get(i).getGoods_name());
        tv_coupon_price0.setText("￥" + couponGoodsDataList.get(i).getGoods_price());
        SimpleDraweeView iv_coupon_goods_pic1 = (SimpleDraweeView) view.findViewById(R.id.iv_coupon_goods_pic1);
        TextView tv_coupon_name1 = (TextView) view.findViewById(R.id.tv_coupon_name1);
        TextView tv_coupon_price1 = (TextView) view.findViewById(R.id.tv_coupon_price1);
        BaseActivity.displayImage(couponGoodsDataList.get(i + 1).getGoods_pic(), iv_coupon_goods_pic1);
        tv_coupon_name1.setText(couponGoodsDataList.get(i + 1).getGoods_name());
        tv_coupon_price1.setText("￥" + couponGoodsDataList.get(i + 1).getGoods_price());
        LinearLayout ll_left_goods = (LinearLayout) view.findViewById(R.id.ll_left_goods);
        LinearLayout ll_right_goods = (LinearLayout) view.findViewById(R.id.ll_right_goods);
        ll_left_goods.setOnClickListener(view1 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                String goods_id = couponGoodsDataList.get(i).getGoods_id();
                mActivity.go_goods(goods_id);
            }
        });
        ll_right_goods.setOnClickListener(view12 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                String goods_id = couponGoodsDataList.get(i + 1).getGoods_id();
                mActivity.go_goods(goods_id);
            }
        });
        ll_coupon_goods.addView(view);
    }


}