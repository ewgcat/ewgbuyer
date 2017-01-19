package com.ewgvip.buyer.android.fragment;


import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.MyRedPackageListAdapter;
import com.ewgvip.buyer.android.models.RedPackageItem;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class MyRedPackageFragment extends Fragment {
    private int mfirstVisibleItem = 0;
    private BaseActivity mActivity;
    private View rootView;
    private int page = 1;
    ArrayList<RedPackageItem> redPackageList = new ArrayList<>();
    ArrayList<RedPackageItem> newredPackageList = new ArrayList<>();
    private MyRedPackageListAdapter adapter;
    private int clickPosition;
    private String user_id;
    private String token;
    private PopupWindow popupWindow;
    private ObjectAnimator ra;
    private ImageView iv_right;
    private EditText et_redpackage_input;
    private View send;
    private String wx_head_img;
    private ListView mListView;

    public MyRedPackageFragment() {

    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.rootView = null;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        SharedPreferences preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        user_id = preferences.getString("user_id", "");
        token = preferences.getString("token", "");

        rootView = inflater.inflate(R.layout.fragment_my_red_package, container, false);
        rootView.findViewById(R.id.iv_back).setOnClickListener(v -> {
            mActivity.onBackPressed();
            mActivity.hide_keyboard(v);
        });


        mActivity.showProcessDialog();

        mListView = (ListView) rootView.findViewById(R.id.redpackage_listview);
        mListView.setDivider(null);
        mListView.setFocusable(false);

        adapter = new MyRedPackageListAdapter(mActivity, redPackageList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener((parent, view, position, id) -> {

            RedPackageItem redPackageItem = redPackageList.get(position);
            if (!TextUtils.isEmpty(redPackageList.get(position).receive_state)&&!redPackageList.get(position).receive_state.equals("RECEIVED")) {
                clickPosition = position;
                showPopupWindow();
            } else if (TextUtils.isEmpty(redPackageList.get(position).receive_state)){

            } else {
                Bundle bundle = new Bundle();
                bundle.putString("iv_user_photo", redPackageItem.wx_head_img);
                bundle.putString("tv_red_package_message", redPackageItem.wishing);
                bundle.putString("tv_money_count", redPackageItem.amount);
                bundle.putString("tv_date", redPackageItem.addTime);
                bundle.putString("tv_name", redPackageItem.trueName);
                mActivity.go_my_red_package_balance(bundle);
            }
        });


        //listview滑动监听
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

                //数据集第一项的索引
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mfirstVisibleItem == 0) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    loadData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                mfirstVisibleItem = firstVisibleItem;
            }
        });


        iv_right = (ImageView) rootView.findViewById(R.id.iv_right);
        et_redpackage_input = (EditText) rootView.findViewById(R.id.et_redpackage_input);
        send = rootView.findViewById(R.id.send);
        et_redpackage_input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                iv_right.setVisibility(View.GONE);
                send.setVisibility(View.VISIBLE);
                send.setOnClickListener(v -> {
                    RedPackageItem redPackageItem = new RedPackageItem("", "", "", "", "", wx_head_img, "", "message", et_redpackage_input.getText().toString().trim());
                    et_redpackage_input.setText("");
                    mActivity.hide_keyboard(v);
                    redPackageList.add(redPackageItem);
                    mHandler.sendEmptyMessage(0);
                });
            }
        });

        return rootView;
    }

    Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    iv_right.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    if (redPackageList.size()>0){
                        mListView.setSelection(adapter.getCount()-1);
                    }
                    CommonUtil.getHandler().postDelayed(() -> new AlertDialog.Builder(mActivity).setMessage("e网购消息为内部体验功能，暂不可实际对话，关闭该页面后消息自动清除。").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).create().show(),1000);
                    break;
                case  1:
                    adapter.notifyDataSetChanged();

                    break;
                case  2:
                    adapter.notifyDataSetChanged();
                    if (redPackageList.size()>0){
                        mListView.setSelection(adapter.getCount()-1);
                    }
                    break;
                default:
                    break;
            }
            return false;
        }
    });


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            redPackageList.clear();
            page = 1;
            loadData();
            CommonUtil.getHandler().post(() -> {
                if (redPackageList.size()>0){
                    mListView.setSelection(adapter.getCount()-1);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        page = 1;
        loadData();
    }

    private void loadData() {
        mActivity.showProcessDialog();
        Map paraMap = new HashMap();
        paraMap.put("user_id", user_id);
        paraMap.put("token", token);
        paraMap.put("page", page + "");

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, getResources().getString(R.string.http_url) + "/app/buyer/my-redpack-data.htm"
                , response -> {
            try {
                page++;
                String ret = response.get("ret").toString();
                if ("1".equals(ret)) {
                    String list = response.get("list").toString();
                    JSONArray jsonArray = new JSONArray(list);
                    int length = jsonArray.length();
                    if (length > 0) {

                        for (int i = length-1; i >=0; i--) {
                            JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                            String addTime = jsonObj.get("addTime").toString();
                            String amount = jsonObj.get("amount").toString();
                            String id = jsonObj.get("id").toString();
                            String receive_state = jsonObj.get("receive_state").toString();
                            String wishing = jsonObj.get("wishing").toString();
                            String redpack_user = jsonObj.get("redpack_user").toString();
                            JSONObject redpack_userjsonobj = new JSONObject(redpack_user);
                            wx_head_img = "";
                            if (redpack_user.contains("wx_head_img")) {
                                wx_head_img = redpack_userjsonobj.get("wx_head_img").toString();
                            }

                            if (TextUtils.isEmpty(wx_head_img)) {
                                wx_head_img = mActivity.getCache("user_image_photo_url");
                            }
                            String trueName = redpack_userjsonobj.get("trueName").toString();
                            RedPackageItem redPackageItem = new RedPackageItem(addTime, amount, id, receive_state, wishing, wx_head_img, trueName, "redpackage", "");
                            newredPackageList.add(redPackageItem);
                        }
                       //保存旧集合到临时变量
                        ArrayList<RedPackageItem> temp=new  ArrayList<RedPackageItem>();
                        temp.addAll(redPackageList);
                        //清空旧集合
                        redPackageList.clear();
                        //添加新集合
                        redPackageList.addAll(newredPackageList);
                        //添加旧集合
                        redPackageList.addAll(temp);
                        //清空集合，释放内存
                        newredPackageList.clear();
                        temp.clear();

                        if (page>2){
                            mHandler.sendEmptyMessage(1);
                        }else if (page==2){
                            mHandler.sendEmptyMessage(2);
                        }

                    }
                }
                mActivity.hideProcessDialog(0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);
    }

    //领取红包到账户余额
    private void receiveRedpackageToBalance(final RedPackageItem redPackageItem) {
        Map paraMap = new HashMap();
        paraMap.put("user_id", user_id);
        paraMap.put("token", token);
        paraMap.put("redpackId", redPackageItem.id + "");

        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(mActivity, getResources().getString(R.string.http_url) + "/app/buyer/receive-redpack.htm"
                , response -> {
                    try {
                        String result = response.get("result").toString();
                        if ("1".equals(result)) {
                            //跳转到红包详情
                            CommonUtil.showSafeToast(mActivity, "领取成功");
                            Bundle bundle = new Bundle();
                            bundle.putString("iv_user_photo", redPackageItem.wx_head_img);
                            bundle.putString("tv_red_package_message", redPackageItem.wishing);
                            bundle.putString("tv_money_count", redPackageItem.amount);
                            bundle.putString("tv_date", redPackageItem.addTime);
                            bundle.putString("tv_name", redPackageItem.trueName);
                            mActivity.go_my_red_package_balance(bundle);
                            popupWindow.dismiss();

                        } else if ("2".equals(result)) {
                            CommonUtil.showSafeToast(mActivity, "领取失败");
                            popupWindow.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mActivity.hideProcessDialog(0);

                }, error -> mActivity.hideProcessDialog(1), paraMap);
        mRequestQueue.add(request);

    }


    private void showPopupWindow() {
        backgroundAlpha(0.3f);
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mActivity).inflate(
                R.layout.pop_window, null);
        final View open = contentView.findViewById(R.id.iv_open_red_package);
        final View close = contentView.findViewById(R.id.iv_close_popwinddow);
        int width = WindowManager.LayoutParams.WRAP_CONTENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(contentView, width, height, true);
        popupWindow.setAnimationStyle(R.style.popwin_anim_style);
        popupWindow.setTouchable(true);
        //添加popupWindow窗口关闭监听
        popupWindow.setOnDismissListener(() -> backgroundAlpha(1f));
        popupWindow.setTouchInterceptor((v, event) -> {
            float x = event.getRawX(); // 获取相对于屏幕左上角的 x 坐标值
            float y = event.getRawY(); // 获取相对于屏幕左上角的 y 坐标值
            // View view;
            RectF closeRectF = calcViewScreenLocation(close);
            RectF openRectF = calcViewScreenLocation(open);
            if (closeRectF.contains(x, y)) {
                popupWindow.dismiss();

            }
            if (openRectF.contains(x, y)) {
                RedPackageItem redPackageItem = redPackageList.get(clickPosition);
                receiveRedpackageToBalance(redPackageItem);
            }
            return false;
        });

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // 设置好参数之后再show
        popupWindow.showAtLocation(rootView, Gravity.CENTER, 0, 0);
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        mActivity.getWindow().setAttributes(lp);
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }


}
