package com.ewgvip.buyer.android.dialog;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.CartGoodsItem;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Author: lixiaoyang
 * Date: 12/31/15 09:40
 * Description:  商品或者购物车选择规格对话框
 */
public class GoodsEditDialog extends Dialog {
    Button btn_dialog_goods_edit_add_car;
    Button btn_dialog_goods_edit_buy;
    ImageButton btn_dialog_goods_edit_plus;
    ImageButton btn_dialog_goods_edit_delease;
    int dialogcount = 0;
    private BaseActivity context;
    private JSONArray jsonArray;
    private ListView lview_dialog_goods_edit;
    private View view;
    private MyGridAdapter myGridAdapter;
    private String goods_id;//商品id
    private String cartgsp = "";//商品规格
    private String count;//商品数量
    private String select = "";//已选商品信息
    private String type;//判断进入的页面0商品详情页进入1购物车页
    private String refresh = "0";//返回购物车页0不刷新1刷新
    private String area_id = "";//送货地区
    private Map map = new HashMap();//存放选中商品规格
    private Map map2 = new HashMap();//存放选中商品信息
    private String price = "0";//商品价格
    private int totalCount = 0;//库存
    private TextView tv_dialog_goods_edit_count;
    private TextView tv_dialog_goods_edit_price;
    private TextView tv_dialog_goods_edit_select;
    private EditText et_dialog_goods_num;
    private SimpleDraweeView iv_dialog_goods_edit_img;
    private ArrayList<String> imglist;//商品图片集合
    private boolean isPromotions = false;
    private boolean isFcode = false;
    private boolean isLimit = false;
    private int limit = 0;
    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btn_dialog_goods_edit_plus:
                    int num = Integer.valueOf(et_dialog_goods_num.getText().toString());
                    if (num == totalCount) {

                    } else {
                        num++;
                    }
                    et_dialog_goods_num.setText(num + "");
                    break;
                case R.id.btn_dialog_goods_edit_delease:
                    int num1 = Integer.valueOf(et_dialog_goods_num.getText().toString());
                    if (num1 < 2) {

                    } else {
                        num1--;
                        et_dialog_goods_num.setText(num1 + "");
                    }
                    break;
                case R.id.btn_dialog_goods_edit_add_car:
                    if (isFcode) {
                        F_addToCart();
                    } else {
                        context.hide_keyboard(view);
                        addToCart();
                    }
                    break;
                case R.id.btn_dialog_goods_edit_buy:
                    if (advance_ding_pay.equals("true")) {
                        easybuy();
                    } else {
                        CommonUtil.showSafeToast(context, "抱歉,该商品已超出预付定金的期限");
                    }

                    GoodsEditDialog.this.dismiss();
                    break;
                case R.id.btn_goods_edit_bottom:
                    refresh = "1";
                    cancel();
                    break;
            }
        }
    };
    private final RequestQueue mRequestQueue;
    private final SharedPreferences preferences;
    private final String user_id;
    private final String token;
    private final String cart_mobile_ids;
    private String advance_ding_pay;

    /*
        构造方法参数
        context 上下文
        theme   dialog样式
        jsonArray 商品规格json
        good_id 商品id
        cartgsp 当前商品规格
        count 购买数量
        type 类别0商品详情页1购物车页
     */
    public GoodsEditDialog(BaseActivity context, int theme, JSONArray jsonArray, String goods_id, String cartgsp, String count, String type, String area_id, boolean isPromotions, boolean isFcode, boolean isLimit, int limit, String advance_ding_pay) {
        super(context, theme);
        this.context = context;
        this.jsonArray = jsonArray;
        this.goods_id = goods_id;
        this.cartgsp = cartgsp;
        this.count = count;
        this.type = type;
        this.area_id = area_id;
        this.isPromotions = isPromotions;
        this.isFcode = isFcode;
        this.isLimit = isLimit;
        this.isFcode = isFcode;
        this.limit = limit;
        this.advance_ding_pay = advance_ding_pay;
        mRequestQueue = Volley.newRequestQueue(context);
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = preferences.getString("user_id", "");
        token = preferences.getString("token", "");
        cart_mobile_ids = preferences.getString("cart_mobile_ids", "");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(context).inflate(R.layout.dialog_goods_edit, null);
        tv_dialog_goods_edit_price = (TextView) view.findViewById(R.id.tv_dialog_goods_edit_price);
        tv_dialog_goods_edit_count = (TextView) view.findViewById(R.id.tv_dialog_goods_edit_count);
        tv_dialog_goods_edit_select = (TextView) view.findViewById(R.id.tv_dialog_goods_edit_select);
        et_dialog_goods_num = (EditText) view.findViewById(R.id.et_dialog_goods_num);
        btn_dialog_goods_edit_plus = (ImageButton) view.findViewById(R.id.btn_dialog_goods_edit_plus);
        btn_dialog_goods_edit_delease = (ImageButton) view.findViewById(R.id.btn_dialog_goods_edit_delease);
        btn_dialog_goods_edit_add_car = (Button) view.findViewById(R.id.btn_dialog_goods_edit_add_car);
        btn_dialog_goods_edit_buy = (Button) view.findViewById(R.id.btn_dialog_goods_edit_buy);
        Button btn_goods_edit_bottom = (Button) view.findViewById(R.id.btn_goods_edit_bottom);
        LinearLayout layout_goods_edit_bottom = (LinearLayout) view.findViewById(R.id.layout_goods_edit_bottom);
        RelativeLayout layout_dialog_goods_edit_num = (RelativeLayout) view.findViewById(R.id.layout_dialog_goods_edit_num);
        iv_dialog_goods_edit_img = (SimpleDraweeView) view.findViewById(R.id.iv_dialog_goods_edit_img);
        btn_dialog_goods_edit_plus.setClickable(false);
        btn_dialog_goods_edit_delease.setClickable(false);
        btn_dialog_goods_edit_add_car.setClickable(false);
        btn_dialog_goods_edit_buy.setClickable(false);
        btn_dialog_goods_edit_add_car.setBackgroundColor(context.getResources().getColor(R.color.gray));
        btn_dialog_goods_edit_buy.setBackgroundColor(context.getResources().getColor(R.color.gray));
        btn_dialog_goods_edit_plus.setOnClickListener(listener);
        btn_dialog_goods_edit_delease.setOnClickListener(listener);
        btn_dialog_goods_edit_add_car.setOnClickListener(listener);
        btn_dialog_goods_edit_buy.setOnClickListener(listener);
        btn_goods_edit_bottom.setOnClickListener(listener);
        et_dialog_goods_num.setText(count);
        tv_dialog_goods_edit_select.setText("已选： " + select);
        if (isPromotions) {
            btn_dialog_goods_edit_add_car.setVisibility(View.GONE);
            btn_dialog_goods_edit_buy.setText("立即付定金");
            btn_dialog_goods_edit_buy.setClickable(true);
        } else {
            if (isFcode) {
                btn_dialog_goods_edit_buy.setVisibility(View.GONE);
                btn_dialog_goods_edit_add_car.setText("F码购买");
            } else {
                btn_dialog_goods_edit_buy.setVisibility(View.VISIBLE);
            }
        }
        //0商品详情页进入1购物车页
        if (type.equals("0")) {
            layout_goods_edit_bottom.setVisibility(View.VISIBLE);
            layout_dialog_goods_edit_num.setVisibility(View.VISIBLE);
            btn_goods_edit_bottom.setVisibility(View.GONE);
        } else {
            layout_goods_edit_bottom.setVisibility(View.GONE);
            layout_dialog_goods_edit_num.setVisibility(View.GONE);
            btn_goods_edit_bottom.setVisibility(View.VISIBLE);
        }
        //初始数据
        getGoodsInfo(cartgsp);
        String[] strings = cartgsp.split(",");
        //遍历初始选中规格
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONArray jsonArray2 = jsonArray.getJSONObject(i).getJSONArray("spec_values");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    for (int k = 0; k < strings.length; k++) {
                        if (String.valueOf(jsonArray2.getJSONObject(j).get("id")).equals(strings[k])) {
                            map.put(i, j);//每个item选中的position
                            map2.put(i, jsonArray2.getJSONObject(j).get("val") + "");//每个item选中的值
                        }
                    }
                }
            } catch (Exception e) {
            }
        }
        lview_dialog_goods_edit = (ListView) view.findViewById(R.id.lview_dialog_goods_edit);
        lview_dialog_goods_edit.setAdapter(new MyListAdapter());
        setListViewHeightBasedOnChildren(lview_dialog_goods_edit);
        setContentView(view);
        windowDeploy(0, 0);
        setCanceledOnTouchOutside(true);

    }

    //dialog宽度
    public void windowDeploy(int x, int y) {
        Window window = getWindow();
        window.setWindowAnimations(R.style.AnimBottom); // 设置窗口弹出动画
        window.setBackgroundDrawableResource(R.color.transparent); // 设置对话框背景为透明
        WindowManager.LayoutParams wl = window.getAttributes();
        // window.setGravity(Gravity.TOP);
        // 根据x，y坐标设置窗口需要显示的位置
        wl.gravity = Gravity.BOTTOM; // 设置重力
        wl.x = x; // x小于0左移，大于0右移
        wl.y = y; // y小于0上移，大于0下移
        // wl.alpha = 0.6f; //设置透明度
        window.setAttributes(wl);
        WindowManager m = context.getWindowManager();//
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用//
        WindowManager.LayoutParams p = window.getAttributes(); // 获取对话框当前的参数值//
        p.width = d.getWidth(); // 宽度设置
        window.setAttributes(p);
    }

    //测量gridview高度
    public void setGridViewHeightBasedOnChildren(GridView gridView) {
        // 获取GridView对应的Adapter
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int rows;
        int columns = 0;
        int horizontalBorderHeight = 0;
        Class<?> clazz = gridView.getClass();
        try {
            //利用反射，取得每行显示的个数
            Field column = clazz.getDeclaredField("mRequestedNumColumns");
            column.setAccessible(true);
            columns = (Integer) column.get(gridView);
            //利用反射，取得横向分割线高度
            Field horizontalSpacing = clazz.getDeclaredField("mRequestedHorizontalSpacing");
            horizontalSpacing.setAccessible(true);
            horizontalBorderHeight = (Integer) horizontalSpacing.get(gridView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断数据总数除以每行个数是否整除。不能整除代表有多余，需要加一行
        if (listAdapter.getCount() % columns > 0) {
            rows = listAdapter.getCount() / columns + 1;
        } else {
            rows = listAdapter.getCount() / columns;
        }
        int totalHeight = 0;
        for (int i = 0; i < rows; i++) { //只计算每项高度*行数
            View listItem = listAdapter.getView(i, null, gridView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }
        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight + horizontalBorderHeight * (rows - 1) + 100;//最后加上分割线总高度
        gridView.setLayoutParams(params);
    }

    //测量listview高度
    public void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        if (listAdapter.getCount() < 2) {
            View listItem = listAdapter.getView(0, null, listView);
            listItem.measure(0, 0);
            params.height = listItem.getMeasuredHeight();
        } else {
            params.height = BaseActivity.dp2px(context, 200);
        }
        listView.setLayoutParams(params);
    }

    //获取商品信息
    public void getGoodsInfo(final String gsp) {
        Map paramap = new HashMap();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("id", goods_id);
        paramap.put("gsp", gsp);
        area_id = context.getCache("area_id");
        paramap.put("area_id", area_id);
        imglist = new ArrayList<>();

        Request<JSONObject> request = new NormalPostRequest(context, context.getAddress() + "/app/load_goods_gsp.htm",
                result -> {
                    try {

                        String ret = result.getString("ret") + "";
                        if (ret.equals("true")) {
                            btn_dialog_goods_edit_plus.setClickable(true);
                            btn_dialog_goods_edit_delease.setClickable(true);
                            if (result.has("img_list")) {
                                JSONArray jsonArray1 = result.getJSONArray("img_list");
                                for (int i = 0; i < jsonArray1.length(); i++) {
                                    imglist.add(jsonArray1.getJSONObject(i).getString("bigImg").toString());
                                }
                                BaseActivity.displayImage(jsonArray1.getJSONObject(0).getString("smallImg").toString(), iv_dialog_goods_edit_img);
                            }
                            totalCount = (int) result.get("count");

                            if (totalCount == 0) {
                                btn_dialog_goods_edit_add_car.setBackgroundColor(context.getResources().getColor(R.color.gray));
                                btn_dialog_goods_edit_buy.setBackgroundColor(context.getResources().getColor(R.color.gray));
                                btn_dialog_goods_edit_add_car.setClickable(false);
                                btn_dialog_goods_edit_buy.setClickable(false);
                            } else {
                                if (isFcode) {
                                    totalCount = 1;
                                }
                                if (isLimit) {
                                    totalCount = limit;
                                }
                                btn_dialog_goods_edit_add_car.setBackgroundColor(context.getResources().getColor(R.color.light_yellow));
                                btn_dialog_goods_edit_buy.setBackgroundColor(context.getResources().getColor(R.color.red));
                                btn_dialog_goods_edit_add_car.setClickable(true);
                                btn_dialog_goods_edit_buy.setClickable(true);
                            }
                            if (et_dialog_goods_num.getText().toString().trim().equals("")) {
                                et_dialog_goods_num.setText("1");
                            } else {
                                if (totalCount < Integer.parseInt(et_dialog_goods_num.getText().toString())) {
                                    if (totalCount == 0) {
                                        et_dialog_goods_num.setText("");
                                    } else {
                                        et_dialog_goods_num.setText(totalCount + "");
                                    }
                                }
                                et_dialog_goods_num.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                        if (charSequence.toString().length() > 0) {
                                            dialogcount = Integer.parseInt(charSequence.toString());
                                        } else {
                                            dialogcount = 0;
                                        }
                                        if (dialogcount < 1) {
                                            dialogcount = 1;
                                            et_dialog_goods_num.setText(dialogcount + "");
                                        }
                                        if (dialogcount == 1) {
                                        } else {
                                            if (dialogcount > totalCount) {
                                                et_dialog_goods_num.setText(totalCount + "");
                                            }
                                            et_dialog_goods_num.setSelection(et_dialog_goods_num.getText().toString().length());
                                        }
                                    }

                                    @Override
                                    public void afterTextChanged(Editable editable) {


                                    }
                                });
                            }
                            tv_dialog_goods_edit_count.setText("库存：" + result.get("count") + "件");
                            if (Double.parseDouble(context.moneytodouble(result.getString("act_price"))) > 0) {
                                tv_dialog_goods_edit_price.setText("¥ " + context.moneytodouble(result.getString("act_price") + ""));
                                price = result.getString("act_price") + "";
                            } else {
                                tv_dialog_goods_edit_price.setText("¥ " + context.moneytodouble(result.get("price") + ""));
                                price = result.get("price") + "";
                            }
                            select = "";
                            String[] strings = gsp.split(",");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                for (int j = 0; j < jsonArray.getJSONObject(i).getJSONArray("spec_values").length(); j++) {
                                    if (strings[i].equals(jsonArray.getJSONObject(i).getJSONArray("spec_values").getJSONObject(j).get("id") + "")) {
                                        select += "''" + jsonArray.getJSONObject(i).getJSONArray("spec_values").getJSONObject(j).get("val") + "'' ";
                                    }
                                }
                            }
                            tv_dialog_goods_edit_select.setText("已选： " + select);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> context.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }

    //加入购物车
    @SuppressLint("CommitPrefEdits")
    protected void addToCart() {
        Map paramap = new HashMap();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", cart_mobile_ids);
        paramap.put("goods_id", goods_id);
        if (et_dialog_goods_num.getText().equals("")) {
            Toast.makeText(context, "购买数量不能为空", Toast.LENGTH_SHORT).show();
        } else {
            paramap.put("count", et_dialog_goods_num.getText().toString() + "");
        }

        paramap.put("gsp", cartgsp);
        paramap.put("area_id", area_id);

        RequestQueue mRequestQueue = Volley.newRequestQueue(context);

        Request<JSONObject> request = new NormalPostRequest(context, context.getAddress() + "/app/add_goods_cart.htm",
                result -> {
                    try {

                        int code = result.getInt("code");

                        if (code == 0 || code == 1 || code == 2) {
                            if (TextUtils.isEmpty(user_id)) {
                                if (result.toString().contains("cart_mobile_id")) {
                                    String cart_mobile_id = result.getString("cart_mobile_id");
                                    if (!TextUtils.isEmpty(cart_mobile_id)) {
                                        preferences.edit().putString("cart_mobile_ids", cart_mobile_ids + cart_mobile_id + ",").commit();
                                    }
                                }
                            }
                            new AlertDialog.Builder(context).setTitle("添加成功！").setMessage("商品已成功加入购物车")
                                    .setPositiveButton("去购物车", (dialog, which) -> {
                                        dismiss();// 关闭对话框
                                        context.go_cart();
                                    }).setNegativeButton("再逛逛", (dialog, which) -> {
                            }).create().show();// 创建对话框并且显示该对话框
                        }
                        if (code == -3) {
                            Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -4) {
                            Toast.makeText(context, "超过限购数量", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -5) {
                            Toast.makeText(context, "限购商品,请登录", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -1) {
                            Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -2) {
                            Toast.makeText(context, "商品已下架", Toast.LENGTH_SHORT).show();
                        }

                        if (code == -7) {
                            Toast.makeText(context, "购买数量必须大于商品的最低购买数量", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -8) {
                            Toast.makeText(context, "购买数量必须小于商品的最高购买数量\n", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -9) {
                            Toast.makeText(context, "当前用户等级低于该商品购买等级", Toast.LENGTH_SHORT).show();
                        }
                        if (code == -10) {
                            Toast.makeText(context, "商品店铺已停业", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> context.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);

    }

    ArrayList<CartGoodsItem> cartGoodsItemList = new ArrayList<>();


    //查询
    public void getAllCartGoodsList() {
        Map paramap = new HashMap();
        paramap.put("user_id", user_id);
        paramap.put("token", token);
        paramap.put("cart_mobile_ids", "");

        Request<JSONObject> requestgetall = new NormalPostRequest(context, context.getAddress() + "/app/goods_cart1.htm", response -> {
            try {
                String cart_list = response.get("cart_list").toString();
                JSONArray jsonarr = new JSONArray(cart_list);
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject jsonobj = (JSONObject) jsonarr.get(i);
                    String cartId = jsonobj.get("cart_id") + "";
                    String goodsid = jsonobj.get("goods_id") + "";
                    CartGoodsItem cartGoodsItem = new CartGoodsItem(cartId, goodsid);
                    cartGoodsItemList.add(cartGoodsItem);
                }
                deleteSameGoods();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Log.i("test",error.toString()), paramap);
        mRequestQueue.add(requestgetall);

    }

    //删除指定
    public void deleteSameGoods() {
        CartGoodsItem sameGoods = checkSameGoods(cartGoodsItemList, goods_id);
        if (sameGoods != null) {
            Map paramap = new HashMap();
            paramap.put("user_id", user_id);
            paramap.put("token", token);
            paramap.put("cart_mobile_ids", cart_mobile_ids);
            paramap.put("cart_ids", sameGoods.cart_id);
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(context).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(context, context.getAddress() + "/app/remove_goods_cart.htm",
                    result -> {
                        try {
                            int code = result.getInt("code");
                            if (code == 100) {
                                easybuyaddToCart();
                            } else {
                                Toast.makeText(context, "删除失败，请稍候重试", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> Log.i("test",error.toString()), paramap);
            mRequestQueue.add(request);

        } else {
            easybuyaddToCart();
        }


    }

    public CartGoodsItem checkSameGoods(ArrayList<CartGoodsItem> list, String goods_id) {

        for (int i = 0; i < list.size(); i++) {
            if (goods_id.equals(list.get(i).goodsid)) {
                //相同的商品cartId
                return list.get(i);
            }
        }
        return null;
    }

    //立即购买
    public void easybuyaddToCart() {
        if (context.islogin()) {
            Map paramap = new HashMap();
            final SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            final String user_id = preferences.getString("user_id", "");
            String token = preferences.getString("token", "");
            paramap.put("user_id", user_id);
            paramap.put("token", token);
            paramap.put("cart_mobile_ids", "");
            paramap.put("goods_id", goods_id);
            if (et_dialog_goods_num.getText().equals("")) {
                Toast.makeText(context, "购买数量不能为空", Toast.LENGTH_SHORT).show();
            } else {
                paramap.put("count", et_dialog_goods_num.getText().toString() + "");
            }
            paramap.put("gsp", cartgsp);
            paramap.put("area_id", area_id);
            RequestQueue mRequestQueue = Volley.newRequestQueue(context);
            Request<JSONObject> request = new NormalPostRequest(context, context.getAddress() + "/app/add_goods_cart.htm",
                    result -> {
                        try {

                            int code = result.getInt("code");
                            if (code == 0 || code == 1 || code == 2) {
                                String cart_mobile_id = result.getString("cart_id");
                                context.go_cart2(cart_mobile_id);
                            }
                            if (code == -3) {
                                Toast.makeText(context, "库存不足", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -4) {
                                Toast.makeText(context, "超过限购数量", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -5) {
                                Toast.makeText(context, "限购商品,请登录", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -1) {
                                Toast.makeText(context, "添加失败", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -2) {
                                Toast.makeText(context, "商品已下架", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -7) {
                                Toast.makeText(context, "购买数量必须大于商品的最低购买数量", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -8) {
                                Toast.makeText(context, "购买数量必须小于商品的最高购买数量\n", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -9) {
                                Toast.makeText(context, "当前用户等级低于该商品购买等级", Toast.LENGTH_SHORT).show();
                            }
                            if (code == -10) {
                                Toast.makeText(context, "商品店铺已停业", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }, error -> context.hideProcessDialog(1), paramap);
            mRequestQueue.add(request);
        } else {
            context.go_login();
        }
    }


    //立即购买
    public void easybuy() {
        getAllCartGoodsList();

    }

    protected void F_addToCart() {
        final EditText f_et = new EditText(context);
        new android.support.v7.app.AlertDialog.Builder(context).setTitle("请输入F码").setView(f_et)
                .setPositiveButton("确定", (dialog, which) -> {
                    if (FastDoubleClickTools.isFastDoubleClick()) {
                        String f_string = f_et.getText().toString();
                        Map paramap = new HashMap();
                        final SharedPreferences preferences1 = context.getSharedPreferences("user", Context.MODE_PRIVATE);
                        final String user_id1 = preferences1.getString("user_id", "");
                        String token1 = preferences1.getString("token", "");
                        final String cart_mobile_ids1 = preferences1.getString("cart_mobile_ids", "");
                        paramap.put("user_id", user_id1);
                        paramap.put("token", token1);
                        paramap.put("cart_mobile_ids", cart_mobile_ids1);
                        paramap.put("goods_id", goods_id);
                        paramap.put("count", et_dialog_goods_num.getText() + "");
                        paramap.put("price", price + "");
                        paramap.put("gsp", cartgsp);
                        paramap.put("f_code", f_string);
                        RequestQueue mRequestQueue1 = MySingleRequestQueue.getInstance(context).getRequestQueue();
                        Request<JSONObject> request = new NormalPostRequest(context, context.getAddress() + "/app/add_f_code_goods_cart.htm",
                                result -> {
                                    try {
                                        if (result.getBoolean("ret")) {
                                            if ("".equals(user_id1)) {
                                                String cart_mobile_id = result.getString("cart_mobile_id");
                                                if (!cart_mobile_id.equals("")) {
                                                    SharedPreferences.Editor editor = preferences1.edit();
                                                    editor.putString("cart_mobile_ids", cart_mobile_ids1 + cart_mobile_id + ",");
                                                    editor.commit();
                                                }
                                            }
                                            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context)
                                                    .setTitle("添加成功！")
                                                    .setMessage("商品已成功加入购物车")
                                                    .setPositiveButton("去购物车", (dialog1, which1) -> {
                                                        if (FastDoubleClickTools.isFastDoubleClick()) {
                                                            dismiss();// 关闭对话框
                                                            context.go_cart();
                                                        }
                                                    })
                                                    .setNegativeButton("再逛逛", (dialog1, which1) -> {
                                                        dismiss();// 关闭对话框
                                                    });
                                            builder.create().show();// 创建对话框并且显示该对话框
                                        } else {
                                            Toast.makeText(context, "F码错误或已被使用", Toast.LENGTH_SHORT).show();
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }, error -> context.hideProcessDialog(1), paramap);
                        mRequestQueue1.add(request);

                    }
                }).setNegativeButton("取消", null).show();
    }

    //返回购买数量
    public String getCount() {
        return et_dialog_goods_num.getText().toString();
    }

    //返回购买价格
    public String getPrice() {
        return price;
    }

    //返回规格（请求参数）
    public String getGsp() {
        return cartgsp;
    }

    //返回商品规格信息（显示在页面）
    public String getInfo() {
        String info = "";
        for (int j = 0; j < map2.size(); j++) {
            info += map2.get(j) + " ";
        }
        info += Integer.parseInt(et_dialog_goods_num.getText().toString()) + "件";
        return info;
    }

    //返回选中商品图片
    public ArrayList<String> getImgList() {
        return imglist;
    }

    //判断是否刷新0不刷新1刷新
    public String getRefresh() {
        return refresh;
    }

    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            final ViewHolder viewHolder;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_dialog_goods_edit_list, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_item_dialog_goods_edit_type = (TextView) view.findViewById(R.id.tv_item_dialog_goods_edit_type);
                viewHolder.gview_item_dialog_goods_edit = (GridView) view.findViewById(R.id.gview_item_dialog_goods_edit);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            try {
                viewHolder.tv_item_dialog_goods_edit_type.setText(jsonArray.getJSONObject(position).getString("spec_key"));
                final JSONArray jsonArray2 = jsonArray.getJSONObject(position).getJSONArray("spec_values");
                myGridAdapter = new MyGridAdapter(jsonArray2, position);
                viewHolder.gview_item_dialog_goods_edit.setAdapter(myGridAdapter);
                setGridViewHeightBasedOnChildren(viewHolder.gview_item_dialog_goods_edit);
                viewHolder.gview_item_dialog_goods_edit.setOnItemClickListener((adapterView, view1, i, l) -> {
                    try {
                        map.put(position, i);
                        map2.put(position, jsonArray2.getJSONObject(i).get("val") + "");
                        myGridAdapter = new MyGridAdapter(jsonArray2, position);
                        viewHolder.gview_item_dialog_goods_edit.setAdapter(myGridAdapter);
                        cartgsp = "";
                        for (int j = 0; j < jsonArray.length(); j++) {
                            JSONArray jsonArray3 = jsonArray.getJSONObject(j).getJSONArray("spec_values");
                            if (j == 0) {
                                cartgsp += jsonArray3.getJSONObject((int) map.get(j)).getInt("id") + "";
                            } else {
                                cartgsp += "," + jsonArray3.getJSONObject((int) map.get(j)).getInt("id");
                            }
                        }
                        getGoodsInfo(cartgsp);

                    } catch (Exception e) {
                    }
                });
            } catch (Exception e) {
            }
            return view;
        }

        class ViewHolder {
            TextView tv_item_dialog_goods_edit_type;
            GridView gview_item_dialog_goods_edit;
        }
    }

    class MyGridAdapter extends BaseAdapter {
        JSONArray jsonArray;
        int current;

        MyGridAdapter(JSONArray jsonArray, int current) {
            this.jsonArray = jsonArray;
            this.current = current;
        }

        @Override
        public int getCount() {
            return jsonArray.length();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {

            Holder holder = null;
            if (view == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_dialog_goods_edit_grid, null);
                holder = new Holder();
                holder.rbtn_item_dialog_goods_edit = (TextView) view.findViewById(R.id.rbtn_item_dialog_goods_edit);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            try {
                holder.rbtn_item_dialog_goods_edit.setText(jsonArray.getJSONObject(i).getString("val"));
                if ((int) map.get(current) == i) {
                    holder.rbtn_item_dialog_goods_edit.setBackgroundResource(R.drawable.goods_edit_tv_select_bg);
                    holder.rbtn_item_dialog_goods_edit.setTextColor(context.getResources().getColor(R.color.white));
                }
            } catch (Exception e) {
            }
            return view;
        }

        class Holder {
            TextView rbtn_item_dialog_goods_edit;
        }
    }
}
