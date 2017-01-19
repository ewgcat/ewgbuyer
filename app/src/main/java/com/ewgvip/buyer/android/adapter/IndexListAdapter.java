package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.activity.MainActivity;
import com.ewgvip.buyer.android.fragment.WebFragment;
import com.ewgvip.buyer.android.models.AcListBean;
import com.ewgvip.buyer.android.models.FlipperData;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/10/14.
 */
public class IndexListAdapter extends BaseAdapter {

    MainActivity mActivity;
    List list;
    LayoutInflater inflater;
    boolean ishowgroup;
    FlipperData flipperData;
    private List<Map> adslist;//广告集合
    public IndexListAdapter(MainActivity mActivity, List first_list, boolean ishowgroup, FlipperData flipperData) {
        this.mActivity = mActivity;
        this.list = first_list;
        this.ishowgroup = ishowgroup;
        this.flipperData = flipperData;
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
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.index_template, null);
            holder = new ViewHolder();
            //轮播图容器
            holder.index_ad_container = (RelativeLayout) convertView.findViewById(R.id.index_ad_container);
            holder.viewflipper = (ViewFlipper) convertView.findViewById(R.id.viewflipper);
            holder.tv_more = (TextView) convertView.findViewById(R.id.tv_more);
            holder.ll_ads = (LinearLayout) convertView.findViewById(R.id.ll_ads);
            //八大导航索引容器
            holder.index_nav_container = (LinearLayout) convertView.findViewById(R.id.index_nav_container);

            holder.index_line_type1 = (LinearLayout) convertView.findViewById(R.id.index_line_type1);
            holder.index_line_type2 = (LinearLayout) convertView.findViewById(R.id.index_line_type2);
            holder.index_line_type2_1 = (LinearLayout) convertView.findViewById(R.id.index_line_type2_1);
            holder.index_line_type3 = (LinearLayout) convertView.findViewById(R.id.index_line_type3);
            holder.index_line_type4 = (LinearLayout) convertView.findViewById(R.id.index_line_type4);
            holder.index_line_type6 = (LinearLayout) convertView.findViewById(R.id.index_line_type6);
            holder.index_line_type7 = (LinearLayout) convertView.findViewById(R.id.index_line_type7);
            //索引标题容器
            holder.index_title_container = (LinearLayout) convertView.findViewById(R.id.index_title_container);
            //轮播图
            holder.index_ad = (ConvenientBanner) convertView.findViewById(R.id.index_ad);
            // 1 品牌街
            holder.recharge = (LinearLayout) convertView.findViewById(R.id.recharge);
            // 2 积分商城
            holder.points = (LinearLayout) convertView.findViewById(R.id.points);
            // 3 促销
            holder.sale = (LinearLayout) convertView.findViewById(R.id.sale);
            // 4 全球馆

            holder.ll_globe_store = (LinearLayout) convertView.findViewById(R.id.ll_globe_store);
            //1.品牌图片
            holder.recharge_img = (SimpleDraweeView) convertView.findViewById(R.id.recharge_img);
            //2.积分商城图片
            holder.points_img = (SimpleDraweeView) convertView.findViewById(R.id.points_img);
            //3.促销图片
            holder.sale_img = (SimpleDraweeView) convertView.findViewById(R.id.sale_img);
            //4.全球馆图片
            holder.globe_img = (SimpleDraweeView) convertView.findViewById(R.id.globe_img);


            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.index_ad_container.setVisibility(View.GONE);
        holder.index_nav_container.setVisibility(View.GONE);
        holder.ll_ads.setVisibility(View.GONE);
        holder.ll_ads.setVisibility(View.GONE);
        holder.index_line_type1.setVisibility(View.GONE);
        holder.index_line_type2.setVisibility(View.GONE);
        holder.index_line_type2_1.setVisibility(View.GONE);
        holder.index_line_type3.setVisibility(View.GONE);
        holder.index_line_type4.setVisibility(View.GONE);
        holder.index_line_type6.setVisibility(View.GONE);
        holder.index_line_type7.setVisibility(View.GONE);
        holder.index_title_container.setVisibility(View.GONE);


        //3种数据类型 type：ad nav
        Map map = (Map) list.get(position);
        String type = map.get("type") + "";
        if (type.equals("ad")) {
            holder.index_ad_container.setVisibility(View.VISIBLE);
            int screenWidth = mActivity.getScreenWidth();
            int w = screenWidth * 150 / 320;
            holder.index_ad_container.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, w));
            if(map.containsKey("list")){
                adslist = (List) map.get("list");
            }
            if(adslist != null){
                holder.index_ad.setPages(
                        new CBViewHolderCreator<LocalImageHolderView>() {
                            @Override
                            public LocalImageHolderView createHolder() {
                                return new LocalImageHolderView();
                            }
                        }, adslist)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        .setPageIndicator(new int[]{R.mipmap.viewpager_unselected, R.mipmap.viewpager_selected})
                        //设置指示器的方向
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
                        //设置翻页的效果，不需要翻页效果可用不设
                        .startTurning(2000);


                //轮播图点击事件
                holder.index_ad.setOnItemClickListener(position1 -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {

                  //      Log.i("IndexListAdpater", " 点击广告图片 click_url  " + adslist.get(position1).get("click_url"));

                        //获取点击的click_url,根据参数跳转到不同的界面
//                         1.促销-->直接跳转到促销活动界面
//                          有参数 app://activity?id=3&name=hh
//                          无参数：app://activity
//                        2.商品 app://goods?id=1524&name=老北京布鞋
//                        3：http->跳转到网页界面
                        if (position1 < adslist.size()) {
                            String click_url = adslist.get(position1).get("click_url").toString();

                            //app://跳转原生界面
                            if (click_url.contains("app://")) {
                                //促销

                                if (click_url.contains("activity")) {
                                    if (!click_url.contains("id")) {
                                        mActivity.go_activity_list();
                                    } else {
                                        String s1 = click_url.split("&")[0];
                                        String id = s1.substring(18);
                                        Bundle bundle=new Bundle();
                                        bundle.putString("id",id);
                                        mActivity.go_salespm(bundle);
                                    }
                                }
                                if (click_url.contains("goods")) {
                                    if (!click_url.contains("id")) {
                                        mActivity.go_goods("");
                                    } else {
                                        String s1 = click_url.split("&")[0];
                                        String id = s1.substring(15);
                                        mActivity.go_goods(id);
                                    }
                                }

                            }

                            //http->跳转到web界面
                            if (click_url.contains("http")) {
                                Bundle bundle = new Bundle();
                                bundle.putString("url", adslist.get(position1).get("click_url") + "");
                                mActivity.go_web(bundle);
                            }
                        }

                    }
                });

            }

        } else {
            if (type.equals("nav")) {
                holder.index_nav_container.setVisibility(View.VISIBLE);

                if (!mActivity.getCache("index_1").equals("")) {
                    BaseActivity.displayImage(mActivity.getCache("index_1"), holder.recharge_img);//1品牌
                    BaseActivity.displayImage(mActivity.getCache("index_2"), holder.points_img);//2积分
                    BaseActivity.displayImage(mActivity.getCache("index_6"), holder.sale_img);//3促销
                    BaseActivity.displayImage(mActivity.getCache("index_8"), holder.globe_img);//全球馆
                } else {
                    BaseActivity.displayImage("res://com.ewgvip.buyer.android/" + R.mipmap.brand_img, holder.recharge_img);//1品牌
                    BaseActivity.displayImage("res://com.ewgvip.buyer.android/" + R.mipmap.points, holder.points_img);//2积分
                    BaseActivity.displayImage("res://com.ewgvip.buyer.android/" + R.mipmap.sale, holder.sale_img);//3促销
                    BaseActivity.displayImage("res://com.ewgvip.buyer.android/" + R.mipmap.globe_store, holder.globe_img);//4全球馆
                }


                //1.点击品牌街跳转到品牌
                holder.recharge.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        //跳转品牌
                        mActivity.go_gb();
                    }
                });

                //2积分商城
                holder.points.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_integral_index();
                    }
                });

                //3促销
                holder.sale.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        mActivity.go_activity_list();
                    }
                });

                //4全球馆
                holder.ll_globe_store.setOnClickListener(v -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        Bundle bundle = new Bundle();
                        bundle.putString("type", WebFragment.GLOBLE_STORE);
                        mActivity.go_globe_store(bundle);
                    }
                });

                //公告
                if (flipperData != null) {
                    holder.ll_ads.setVisibility(View.VISIBLE);
                    final List<AcListBean> ac_list = flipperData.ac_list;
                    for (int i = 0; i < ac_list.size(); i++) {
                        TextView textView = new TextView(mActivity);
                        textView.setText(ac_list.get(i).title);
                        textView.setMaxLines(1);
                        textView.setEllipsize(TextUtils.TruncateAt.END);
                        textView.setGravity(Gravity.CENTER);
                        textView.setGravity(Gravity.CENTER_VERTICAL);
                        holder.viewflipper.addView(textView);
                    }
                    for (int i = 0; i < ac_list.size(); i++) {
                        holder.viewflipper.getChildAt(i).setOnClickListener(arg0 -> {
                            TextView tv = (TextView) arg0;
                            for (int i1 = 0; i1 < ac_list.size(); i1++) {
                                if (tv.getText().equals(ac_list.get(i1).title)) {
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", mActivity.getAddress() + ac_list.get(i1).ac_url);
                                    mActivity.go_web(bundle);
                                }
                            }
                        });
                    }
                    holder.tv_more.setOnClickListener(v -> {
                        Bundle bundle = new Bundle();
                        bundle.putString("url", mActivity.getAddress() + flipperData.url);
                        mActivity.go_web(bundle);
                    });
                    holder.viewflipper.setInAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.anim_from_down_in));
                    holder.viewflipper.setOutAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.anim_from_up_out));
                    holder.viewflipper.startFlipping();
                }

            } else if (type.equals("title")) {
                holder.index_title_container.setVisibility(View.VISIBLE);
                TextView title = (TextView) holder.index_title_container.findViewById(R.id.title);
                title.setText(map.get("title") + "");
            } else {
                List img_list = (List) map.get("list");
                //Log.i("test","img_list="+img_list);
                int count = 0;
                Map single_map = (Map) img_list.get(count);
                int screenWidth = mActivity.getScreenWidth();
                if (type.equals("1")) {
                    holder.index_line_type1.setVisibility(View.VISIBLE);
                    int h = screenWidth * Integer.valueOf(single_map.get("height") + "") / Integer.valueOf(single_map.get("width") + "");
                    holder.index_line_type1.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
                    show_img(holder.index_line_type1, img_list, count);
                } else if (type.equals("2")) {

                    int h = screenWidth * Integer.valueOf(single_map.get("height") + "") / Integer.valueOf(single_map.get("width") + "") / 2;
                    holder.index_line_type2.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h + 160));
                    Map map2 = (Map) img_list.get(count);
                  if (map2.containsKey("goods_name")) {
                      holder.index_line_type2.setVisibility(View.VISIBLE);
                      show_goods_line(holder.index_line_type2, img_list, count);
                  }else {
                      holder.index_line_type2_1.setVisibility(View.VISIBLE);
                      show_img(holder.index_line_type2_1, img_list, count);
                  }


                } else if (type.equals("3")) {
                    holder.index_line_type3.setVisibility(View.VISIBLE);
                    int h = screenWidth * Integer.valueOf(single_map.get("height") + "") / Integer.valueOf(single_map.get("width") + "") / 3;
                    holder.index_line_type3.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
                    show_img(holder.index_line_type3, img_list, count);
                } else if (type.equals("4")) {
                    holder.index_line_type4.setVisibility(View.VISIBLE);
                    int h = screenWidth * Integer.valueOf(single_map.get("height") + "") / Integer.valueOf(single_map.get("width") + "") / 4;
                    holder.index_line_type4.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
                    show_img(holder.index_line_type4, img_list, count);
                } else if (type.equals("6")) {
                    holder.index_line_type6.setVisibility(View.VISIBLE);
                    int h = screenWidth * 314 / 700;
                    holder.index_line_type6.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
                    show_img(holder.index_line_type6, img_list, count);
                } else if (type.equals("7")) {
                    holder.index_line_type7.setVisibility(View.VISIBLE);
                    int h = screenWidth * 340 / 650;
                    holder.index_line_type7.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, h));
                    show_img(holder.index_line_type7, img_list, count);
                }
            }
        }
        return convertView;
    }

    private void show_goods_line(LinearLayout parent_lin, List img_list, int count) {
        for (int i = 0; i < parent_lin.getChildCount(); i++) {
            LinearLayout v = (LinearLayout) parent_lin.getChildAt(i);
            final Map single_map = (Map) img_list.get(count);
            final String click_info = single_map.get("click_info") + "";

            v.setOnClickListener(v1 -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {
                    mActivity.go_goods(click_info);
                }
            });
            SimpleDraweeView simpleDraweeView = (SimpleDraweeView) v.getChildAt(0);
            Uri uri = Uri.parse(single_map.get("url") + "");
            simpleDraweeView.setImageURI(uri);
            count++;
            TextView goodsname = (TextView) v.getChildAt(1);
            goodsname.setText(single_map.get("goods_name") + "");
            TextView goodsprice = (TextView) v.getChildAt(2);
            goodsprice.setText("￥" + single_map.get("goods_sales_price"));
        }

    }


    void show_img(LinearLayout parent_lin, List img_list, int count) {
        for (int i = 0; i < parent_lin.getChildCount(); i++) {
            View v = parent_lin.getChildAt(i);
            if (v instanceof SimpleDraweeView) {

                final Map single_map = (Map) img_list.get(count);
                final String click_info = single_map.get("click_info") + "";

                Uri uri = Uri.parse(single_map.get("url") + "");
                Log.i("test","uri="+uri);
                SimpleDraweeView simpleDraweeView = (SimpleDraweeView) v;
                simpleDraweeView.setImageURI(uri);
                count++;

                if (single_map.get("click_type").equals("goods")) {
                    v.setOnClickListener(v1 -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {

//                                Log.i("IndexListAdapter"," 点击商品图片 click_url  " +click_info );
                            mActivity.go_goods(click_info);
                        }
                    });
                } else if (single_map.get("click_type").equals("url")) {
                    v.setOnClickListener(v12 -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            Bundle bundle = new Bundle();
//                                Log.i("IndexListAdapter"," 11点击网页 click_url  " +click_info );
                            bundle.putString("url", click_info);
                            mActivity.go_web(bundle);
                        }
                    });
                } else if ("goods_brand".equals(single_map.get("click_type"))) {
                    v.setOnClickListener(v13 -> {
//                           Log.i("IndexListAdapter"," 点击品牌 click_url  " +click_info );
                        mActivity.go_brand_goods(click_info);
                    });
                } else if ("activity".equals(single_map.get("click_type"))) {

                    v.setOnClickListener(v14 -> {
//                           Log.i("IndexListAdapter"," 点击促销 click_url  " +click_info );
                        String id = click_info.split("[|]")[0];
                        Bundle bundle=new Bundle();
                        bundle.putString("id",id);
                        mActivity.go_salespm(bundle);

                    });
                } else if ("integral".equals(single_map.get("click_type"))) {

                    v.setOnClickListener(v15 -> {
//                            Log.i("IndexListAdapter"," 点击积分 click_url  " +click_info );
                        String goods_id = click_info.split("[|]")[0];

                        mActivity.go_integral_goods(goods_id);
                    });
                } else if ("goods_class".equals(single_map.get("click_type"))) {

                    v.setOnClickListener(v16 -> {
//                           Log.i("IndexListAdapter"," 点击分类 click_url  " +click_info );
                        String gc_id = click_info.split("[|]")[0];
                        String brandName = click_info.split("[|]")[1];
                        mActivity.go_goodslist("gc_id", gc_id + "", brandName);

                    });
                }
            }
            if (v instanceof LinearLayout) {
                show_img((LinearLayout) v, img_list, count);
            }
        }
    }

    public static class ViewHolder {
        public RelativeLayout index_ad_container;
        public LinearLayout index_nav_container;
        public LinearLayout index_line_type1;
        public LinearLayout index_line_type2;
        public LinearLayout index_line_type3;
        public LinearLayout index_line_type4;
        public LinearLayout index_line_type6;
        public LinearLayout index_line_type7;
        public LinearLayout index_title_container;
        public ConvenientBanner index_ad;
        public LinearLayout recharge;
        public LinearLayout sale;
        public LinearLayout ll_globe_store;
        public LinearLayout points;
        public SimpleDraweeView recharge_img;
        public SimpleDraweeView sale_img;
        public SimpleDraweeView points_img;
        public SimpleDraweeView globe_img;
        public ViewFlipper viewflipper;
        public LinearLayout ll_ads;
        public TextView tv_more;
        public LinearLayout index_line_type2_1;
    }


    public class LocalImageHolderView implements Holder<Map> {
        private SimpleDraweeView view;

        @Override
        public View createView(Context context) {
            view = new SimpleDraweeView(context);
            return view;
        }

        @Override
        public void UpdateUI(Context context, final int position, Map data) {
            Uri uri = Uri.parse(data.get("img_url") + "");
            view.setImageURI(uri);
        }
    }
}
