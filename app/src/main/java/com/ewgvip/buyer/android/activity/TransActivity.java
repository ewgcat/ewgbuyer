package com.ewgvip.buyer.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.adapter.TransListAdapter;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:37
 * Description:  物流信息查询
 */
public class TransActivity extends BaseActivity {

    BaseActivity mActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("物流详情");//设置标题
        mActivity = (BaseActivity) TransActivity.this;
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> mActivity.onBackPressed());

        Intent intent = getIntent();
        SharedPreferences preferences = getSharedPreferences("user",
                Context.MODE_PRIVATE);
        String user_id = preferences.getString("user_id", "");
        String token = preferences.getString("token", "");
        Map paramap = new HashMap<String, String>();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("order_id", intent.getStringExtra("order_id"));

        RequestQueue mRequestQueue = Volley.newRequestQueue(TransActivity.this);
        Request<JSONObject> request = new NormalPostRequest(TransActivity.this, getAddress() + "/app/buyer/goods_order_ship.htm",
                result -> {
                    try {
                        List<Map> list = new ArrayList<Map>();
                        JSONArray jsonarr1 = result.getJSONArray("json_list");
                        JSONObject jo = jsonarr1.getJSONObject(0);
                        JSONArray jsonarr2 = jo.getJSONArray("content");
                        String status = jo.getString("status");
                        String message = jo.has("message")?jo.getString("message"):"没有物流信息！";
                        int length = jsonarr2.length();

                        for (int i = 0; i < length; i++) {
                            JSONObject oj = jsonarr2.getJSONObject(i);
                            Map map = new HashMap();
                            map.put("content", oj.getString("content"));
                            map.put("time", oj.getString("time"));
                            if (i == 0)
                                map.put("img", R.mipmap.logistics_over);
                            else
                                map.put("img", R.mipmap.logistics_round);
                            list.add(map);
                        }

                        if (length == 0) {
                            if (status != null && status.equals("0")) {
                                TextView tv = (TextView) findViewById(R.id.nodata);
                                tv.setText(message);
                            }
                            findViewById(R.id.nodata).setVisibility(View.VISIBLE);
                            findViewById(R.id.translist).setVisibility(View.GONE);

                        }else {
                            findViewById(R.id.nodata).setVisibility(View.GONE);
                            findViewById(R.id.translist).setVisibility(View.VISIBLE);
                        }
                        ListView translist = (ListView) findViewById(R.id.translist);
                        translist.setAdapter(new TransListAdapter(TransActivity.this, list));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    Log.i("test",error.toString());
                    TextView textView=(TextView)findViewById(R.id.nodata);
                    textView.setText("网络异常");
                    textView.setVisibility(View.VISIBLE);

                }, paramap);
        mRequestQueue.add(request);

    }

}
