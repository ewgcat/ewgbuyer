package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.OrderGoodsListAdapter;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 购物车商品列表
 */
public class Cart2GoodsListFragment extends Fragment {
    private BaseActivity mActivity;
    private View rootView;

    @Override
    public void onDetach() {
        super.onDetach();
        
        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_cart2_goodlist,
                container, false);
        mActivity = (BaseActivity) getActivity();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("商品清单");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(false);//设置菜单可用
        final Bundle bundle = getArguments();
        Map paramap = mActivity.getParaMap();
        paramap.put("cart_ids", bundle.get("cart_ids_temp"));

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, mActivity.getAddress() + "/app/goods_cart2_goodsInfo.htm",
                result -> {
                    List goodlist = new ArrayList();
                    try {
                        JSONArray arr = result.getJSONArray("goods_list");
                        int length = arr.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject jo = arr.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("goods_main_photo", jo.getString("goods_main_photo"));
                            map.put("goods_price", "￥" + jo.getString("goods_price"));
                            map.put("goods_name", jo.getString("goods_name"));
                            map.put("goods_count", jo.getString("goods_count"));
                            if (jo.has("cart_status") && jo.getString("cart_status").equals("赠品")) {
                                map.put("goods_price", "赠品");
                            }
                            goodlist.add(map);
                            if (jo.has("cart_status") && jo.getString("cart_status").equals("组合销售")) {
                                JSONArray suit_arr = jo.getJSONArray("suit_list");
                                for (int j = 0; j < suit_arr.length(); j++) {
                                    JSONObject suit = suit_arr.getJSONObject(j);
                                    Map suitmap = new HashMap();
                                    suitmap.put("goods_name", suit.getString("name"));
                                    suitmap.put("goods_main_photo", suit.getString("img"));
                                    suitmap.put("goods_price", "组合配件");
                                    goodlist.add(suitmap);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ListView lv = (ListView) rootView.findViewById(R.id.goods_list);
                    lv.setAdapter(new OrderGoodsListAdapter(mActivity, goodlist));
                    mActivity.hideProcessDialog(0);
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
        return rootView;
    }
}
