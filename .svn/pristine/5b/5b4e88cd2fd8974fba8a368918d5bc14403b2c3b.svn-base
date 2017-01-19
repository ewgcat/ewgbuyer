package com.ewgvip.buyer.android.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * Title: AddConsultingActivity.java
 * </p>
 * <p/>
 * <p>
 * Description: 添加购买咨询
 * </p>
 * <p/>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 * <p/>
 * <p>
 * Company: 沈阳网之商科技有限公司 www.iskyshop.com
 * </p>
 *
 * @author lixiaoyang
 * @version 1.0
 * @date 2014-8-27
 */
public class ConsultAddActivity extends BaseActivity {

    private Spinner spinner;
    private EditText content;
    private String goods_id;
    private List<String> consultType = new ArrayList<String>();
    private TextView tv_select_type;
    private PopupWindow popupWindow;
    private ListView popupListView;
    private ImageView iv_select_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//设置toolbar
        toolbar.setTitle("发表咨询");//设置标题
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        consultType.add("产品咨询");
        consultType.add("库存及配送");
        consultType.add("支付及发票");
        consultType.add("售后咨询");
        consultType.add("促销活动");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                ConsultAddActivity.this, R.layout.spinner_item, consultType);
        spinner = (Spinner) findViewById(R.id.spinner1);
        tv_select_type = (TextView) findViewById(R.id.tv_select_type);
        iv_select_type = (ImageView) findViewById(R.id.iv_select_type);
        popupListView = new ListView(ConsultAddActivity.this);
        popupListView.setVerticalScrollBarEnabled(false);
        popupListView.setBackgroundResource(R.drawable.button_round_white_frame);
        popupListView.setDividerHeight(1);
        popupListView.setAdapter(adapter);
        tv_select_type.setOnClickListener(view -> {
            iv_select_type.setImageResource(R.mipmap.pull_up);
            if (popupWindow == null) {
                popupWindow = new PopupWindow(popupListView, tv_select_type.getWidth(), tv_select_type.getHeight() * 5);
                popupWindow.setOnDismissListener(() -> iv_select_type.setImageResource(R.mipmap.pull_down));
            }
            popupWindow.setFocusable(true);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_round_white_frame));
            popupWindow.showAsDropDown(tv_select_type, 0, 0);
        });
        popupListView.setOnItemClickListener((adapterView, view, i, l) -> {
            popupWindow.dismiss();
            String type = consultType.get(i);
            tv_select_type.setText(type);
        });
        spinner.setAdapter(adapter);
        spinner.setPrompt("请选择咨询类型");
        content = (EditText) findViewById(R.id.editText1);
        Intent intent = getIntent();
        goods_id = intent.getStringExtra("id");
        findViewById(R.id.consulting_add).setOnClickListener(
                v -> {

                    String str = content.getText().toString();
                    String type = tv_select_type.getText().toString();
                    if (str.length() > 0 && (!type.equals(getResources().getString(R.string.goods_consult)))) {
                        Map paramap = new HashMap();
                        paramap.put("goods_id", goods_id);
                        paramap.put("content", str);
                        paramap.put("consult_type", type);
                        SharedPreferences preferences1 = getSharedPreferences("user", Context.MODE_PRIVATE);
                        String user_id = preferences1.getString("user_id", "");
                        String token = preferences1.getString("token", "");
                        paramap.put("user_id", user_id);
                        paramap.put("token", token);
                        RequestQueue mRequestQueue = Volley.newRequestQueue(ConsultAddActivity.this);
                        Request<JSONObject> request = new NormalPostRequest(ConsultAddActivity.this, getAddress() + "/app/goods_consult_save.htm",
                                result -> {
                                    try {
                                        if (result.getBoolean("ret")) {
                                            finish();
                                        } else {
                                            Toast.makeText(ConsultAddActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }, error -> {
                                }, paramap);
                        mRequestQueue.add(request);

                    } else {
                        Toast.makeText(ConsultAddActivity.this, "请填写咨询内容",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //菜单选点点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_index) {
            go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
