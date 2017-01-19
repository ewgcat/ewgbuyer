package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.layout.BadgeView;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/*
    程序底部导航栏
 */
public class IndexNavigatiorFragment extends Fragment {

    public static BadgeView badge;
    public static IndexNavigatiorFragment fragment;
    // 购物车数量标识
    private View barge;
    private View rootView;
    private SimpleDraweeView currentImg;

    private Map nav_icon;
    private Uri uri;
    private SimpleDraweeView index_img;
    private SimpleDraweeView sort_img;
    private SimpleDraweeView cart_img;
    private SimpleDraweeView order_img;
    private SimpleDraweeView usercenter_img;
    private TextView index_text;
    private TextView sort_text;
    private TextView cart_text;
    private TextView order_text;
    private TextView usercenter_text;
    private BaseActivity mActivity;

    public IndexNavigatiorFragment() {
    }

    //静态工厂方法
    public static IndexNavigatiorFragment getInstance() {
        if (fragment == null) {
            fragment = new IndexNavigatiorFragment();
        }
        return fragment;
    }

    // 购物车图标右上角提醒
    public static void setbadge(int num) {
        if (badge!=null){
            if (badge.isShown())
                badge.hide();
            if (num > 0) {
                badge.setText("" + num);
                badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
                badge.toggle();
            }
        }
       
    }

    @Override
    public void onDetach() {
        super.onDetach();


        this.barge = null;
        this.rootView = null;
        this.currentImg = null;
        this.nav_icon = null;
        this.uri = null;
        this.index_img = null;
        this.sort_img = null;
        this.cart_img = null;
        this.order_img = null;
        this.usercenter_img = null;
        this.index_text = null;
        this.sort_text = null;
        this.cart_text = null;
        this.order_text = null;
        this.usercenter_text = null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_navigator, container,
                false);
        mActivity = (BaseActivity) getActivity();
        // 购物车数量提示
        barge = rootView.findViewById(R.id.barge);
        badge = new BadgeView(getActivity(), barge);
        nav_icon = new HashMap();
        String icon = mActivity.getCache("nav_1a");
        nav_icon.put("main_navigation_home", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_home : icon);
        icon = mActivity.getCache("nav_1b");
        nav_icon.put("main_navigation_home_a", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_home_a : icon);
        icon = mActivity.getCache("nav_2a");
        nav_icon.put("main_navigation_sort", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_sort : icon);
        icon = mActivity.getCache("nav_2b");
        nav_icon.put("main_navigation_sort_a", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_sort_a : icon);
        icon = mActivity.getCache("nav_3a");
        nav_icon.put("main_navigation_car", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_car : icon);
        icon = mActivity.getCache("nav_3b");
        nav_icon.put("main_navigation_car_a", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_car_a : icon);

        icon = mActivity.getCache("nav_4a");
        nav_icon.put("main_navigation_order", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_order : icon);
        icon = mActivity.getCache("nav_4b");
        nav_icon.put("main_navigation_order_a", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_order_a : icon);

        icon = mActivity.getCache("nav_5a");
        nav_icon.put("main_navigation_usercenter", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_usercenter : icon);
        icon = mActivity.getCache("nav_5b");
        nav_icon.put("main_navigation_usercenter_a", icon.equals("") ? "res://com.ewgvip.buyer.android/" + R.mipmap.main_navigation_usercenter_a : icon);
        index_img = (SimpleDraweeView) rootView.findViewById(R.id.index_img);
        sort_img = (SimpleDraweeView) rootView.findViewById(R.id.sort_img);
        cart_img = (SimpleDraweeView) rootView.findViewById(R.id.cart_img);
        order_img = (SimpleDraweeView) rootView.findViewById(R.id.order_img);
        usercenter_img = (SimpleDraweeView) rootView.findViewById(R.id.usercenter_img);

        ImagePipeline imagePipeline = Fresco.getImagePipeline();
        Uri uri = Uri.parse(nav_icon.get("main_navigation_home").toString());
        ImageRequest imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, index_img);

        uri = Uri.parse(nav_icon.get("main_navigation_home_a").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, sort_img);

        uri = Uri.parse(nav_icon.get("main_navigation_sort").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, sort_img);

        uri = Uri.parse(nav_icon.get("main_navigation_sort_a").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, sort_img);

        uri = Uri.parse(nav_icon.get("main_navigation_car").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, cart_img);

        uri = Uri.parse(nav_icon.get("main_navigation_car_a").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, cart_img);

        uri = Uri.parse(nav_icon.get("main_navigation_order").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, order_img);

        uri = Uri.parse(nav_icon.get("main_navigation_order_a").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, order_img);


        uri = Uri.parse(nav_icon.get("main_navigation_usercenter").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, usercenter_img);

        uri = Uri.parse(nav_icon.get("main_navigation_usercenter_a").toString());
        imageRequest = ImageRequestBuilder.newBuilderWithSource(uri)
                .setLocalThumbnailPreviewsEnabled(true)
                .build();
        imagePipeline.prefetchToBitmapCache(imageRequest, usercenter_img);

        BaseActivity.displayImage(nav_icon.get("main_navigation_home").toString(), index_img);
        BaseActivity.displayImage(nav_icon.get("main_navigation_sort").toString(), sort_img);
        BaseActivity.displayImage(nav_icon.get("main_navigation_car").toString(), cart_img);

        BaseActivity.displayImage(nav_icon.get("main_navigation_order").toString(), order_img);

        BaseActivity.displayImage(nav_icon.get("main_navigation_usercenter").toString(), usercenter_img);

        index_text = (TextView) rootView.findViewById(R.id.index_text);
        sort_text = (TextView) rootView.findViewById(R.id.sort_text);
        cart_text = (TextView) rootView.findViewById(R.id.cart_text);

        order_text = (TextView) rootView.findViewById(R.id.order_text);

        usercenter_text = (TextView) rootView.findViewById(R.id.usercenter_text);

        currentImg = index_img;
        change_icon(index_img);

        // 导航按钮绑定点击事件
        rootView.findViewById(R.id.navigation_index).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (index_img != currentImg) {
                    change_icon(index_img);
                    mActivity.go_index();
                }
            }
        });
        rootView.findViewById(R.id.navigation_sort).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                if (sort_img != currentImg) {
                    change_icon(sort_img);
                    mActivity.go_gc();
                }
            }
        });
        rootView.findViewById(R.id.navigation_cart).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (cart_img != currentImg) {
                    change_icon(cart_img);
                    mActivity.go_cart1();
                }
            }
        });
        rootView.findViewById(R.id.navigation_brand).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (mActivity.islogin()) {
                    if (order_img != currentImg) {
                        change_icon(order_img);
                        mActivity.go_orders_all();
                    }
                }else {
                    mActivity.go_login();
                }

            }
        });
        rootView.findViewById(R.id.navigation_usercenter).setOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if (usercenter_img != currentImg) {
                    change_icon(usercenter_img);
                    mActivity.go_usercenter();
                }
            }
        });
        return rootView;
    }

    @Override
    public void onResume() {
        cartSum();
        super.onResume();
    }





    public void set_index() {
        change_icon(index_img);
    }

    public void set_cart() {
        change_icon(cart_img);
    }

    public void set_user_center() {
        change_icon(usercenter_img);
    }

    public void set_order() {
        change_icon(order_img);
    }

    public void set_index_top() {
        if (currentImg == index_img) {
            mActivity.go_index();
        }
        if (currentImg == sort_img) {
            mActivity.go_gc();
        }
        if (currentImg == cart_img) {
            mActivity.go_cart1();
        }
        if (currentImg == order_img) {
            mActivity.go_orders_all();
        }
        if (currentImg == usercenter_img) {
            mActivity.go_usercenter();
        }
    }

    void change_icon(SimpleDraweeView view) {
        if (nav_icon != null) {
            if (currentImg == index_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_home"));
                index_img.setImageURI(uri);
                index_text.setTextColor(getResources().getColor(R.color.textdark));
            }
            if (currentImg == sort_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_sort"));
                sort_img.setImageURI(uri);
                sort_text.setTextColor(getResources().getColor(R.color.textdark));
            }
            if (currentImg == cart_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_car"));
                cart_img.setImageURI(uri);
                cart_text.setTextColor(getResources().getColor(R.color.textdark));
            }
            if (currentImg == order_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_order"));
                order_img.setImageURI(uri);
                order_text.setTextColor(getResources().getColor(R.color.textdark));
            }
            if (currentImg == usercenter_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_usercenter"));
                usercenter_img.setImageURI(uri);
                usercenter_text.setTextColor(getResources().getColor(R.color.textdark));
            }
            currentImg = view;
            if (currentImg == index_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_home_a"));
                index_img.setImageURI(uri);
                index_text.setTextColor(getResources().getColor(R.color.nav_text));
            }
            if (currentImg == sort_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_sort_a"));
                sort_img.setImageURI(uri);
                sort_text.setTextColor(getResources().getColor(R.color.nav_text));
            }
            if (currentImg == cart_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_car_a"));
                cart_img.setImageURI(uri);
                cart_text.setTextColor(getResources().getColor(R.color.nav_text));
            }
            if (currentImg == order_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_order_a"));
                order_img.setImageURI(uri);
                order_text.setTextColor(getResources().getColor(R.color.nav_text));
            }
            if (currentImg == usercenter_img) {
                uri = Uri.parse("" + nav_icon.get("main_navigation_usercenter_a"));
                usercenter_img.setImageURI(uri);
                usercenter_text.setTextColor(getResources().getColor(R.color.nav_text));
            }
        }
    }

    void cartSum() {
        SharedPreferences preferences = getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        String cart_mobile_ids = preferences.getString("cart_mobile_ids", "");
        Map paramap = new HashMap();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(getActivity()) + "/app/goods_cart_count.htm",
                result -> {
                    try {
                        int count = result.getInt("count");
                        if (count > 0) {
                            setbadge(count);
                        } else {
                            setbadge(0);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {Log.i("test", error.toString());}, paramap);
        mRequestQueue.add(request);
    }
}
