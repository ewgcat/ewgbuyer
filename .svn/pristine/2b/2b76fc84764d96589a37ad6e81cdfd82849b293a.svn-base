package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ComplainDetailFragment extends Fragment {
    private View rootView;
    private BaseActivity mActivity;
    private SharedPreferences preferences;
    private LayoutInflater layoutInflater;
    //status view
    private RelativeLayout rl_inmates_detail;
    private LinearLayout ll_inmates_picture;
    private LinearLayout ll_complain_conversation;
    private LinearLayout ll_complain_suggestion;
    private LinearLayout ll_complain_conversation_chat;
    private LinearLayout ll_complain_picture;
    private LinearLayout ll_picture;
    private Button b_send_conversation;
    private Button button_refresh;
    private Button b_commit_complain;
    private EditText ed_conversation_content;
    private String status;

    @Override
    public void onDetach() {
        super.onDetach();
        this.mActivity = null;
        this.rootView = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_complain_detail_infrmation, container, false);
        mActivity = (BaseActivity) getActivity();
        preferences = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
        layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //status view
        rl_inmates_detail = (RelativeLayout) rootView.findViewById(R.id.rl_inmates_detail);
        ll_inmates_picture = (LinearLayout) rootView.findViewById(R.id.ll_inmates_picture);
        ll_complain_conversation = (LinearLayout) rootView.findViewById(R.id.ll_complain_conversation);
        ll_complain_suggestion = (LinearLayout) rootView.findViewById(R.id.ll_complain_suggestion);
        ll_complain_conversation_chat = (LinearLayout) rootView.findViewById(R.id.ll_complain_conversation_chat);
        ll_complain_picture = (LinearLayout) rootView.findViewById(R.id.ll_complain_picture);
        ll_picture = (LinearLayout) rootView.findViewById(R.id.ll_picture);
        b_send_conversation = (Button) rootView.findViewById(R.id.b_send_conversation);
        button_refresh = (Button) rootView.findViewById(R.id.button_refresh);
        button_refresh.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                getData();
            }
        });
        b_send_conversation.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                ed_conversation_content = (EditText) rootView.findViewById(R.id.ed_conversation_content);
                String content = ed_conversation_content.getText().toString();
                if (!"".equals(content)) {
                    sendConversationContent(content);
                } else {
                    Toast.makeText(mActivity, "不能为空", Toast.LENGTH_SHORT).show();
                }
            }
        });
        b_commit_complain = (Button) rootView.findViewById(R.id.b_commit_complain);
        b_commit_complain.setOnClickListener(view -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                if(b_commit_complain.getText().toString().equals("提交仲裁")) {
                    commitComplain();
                }
            }
        });
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.complain_desc));//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单不可用
        getData();
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

    public void getData() {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("id", getArguments().getString("id"));
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_view.htm",
                result -> {
                    try {
                        String id = result.has("id") ? result.getString("id") : "";
                        String store_name = result.has("store_name") ? result.getString("store_name") : "";
                        String addTime = result.has("addTime") ? result.getString("addTime") : "";
                        status = result.has("status") ? result.getString("status") : "";
                        String img = result.has("img") ? result.getString("img") : "";
                        String from_user_content = result.has("from_user_content") ? result.getString("from_user_content") : "";
                        String to_user_content = result.has("to_user_content") ? result.getString("to_user_content") : "";
                        String from_acc1 = result.has("from_acc1") ? result.getString("from_acc1") : "";
                        String from_acc2 = result.has("from_acc2") ? result.getString("from_acc2") : "";
                        String from_acc3 = result.has("from_acc3") ? result.getString("from_acc3") : "";
                        String to_acc1 = result.has("to_acc1") ? result.getString("to_acc1") : "";
                        String to_acc2 = result.has("to_acc2") ? result.getString("to_acc2") : "";
                        String to_acc3 = result.has("to_acc3") ? result.getString("to_acc3") : "";
                        //init data
                        com.facebook.drawee.view.SimpleDraweeView image_goods = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.image_goods);
                        image_goods.setImageURI(Uri.parse(img));
                        TextView tv_goods_name = (TextView) rootView.findViewById(R.id.tv_goods_name);
                        tv_goods_name.setText(store_name);
                        TextView tv_goods_time = (TextView) rootView.findViewById(R.id.tv_goods_time);
                        tv_goods_time.setText(addTime);
                        //complain detail
                        EditText ed_complain_detail = (EditText) rootView.findViewById(R.id.ed_complain_detail);
                        ed_complain_detail.setText(from_user_content);
                        EditText ed_inmates_detail = (EditText) rootView.findViewById(R.id.ed_inmates_detail);
                        ed_inmates_detail.setText(to_user_content);
                        //投诉图片
                        int complainPictureFlag = 0;
                        int inmatesPictureFlag = 0;
                        com.facebook.drawee.view.SimpleDraweeView iv_complain_picture_1 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_complain_picture_1);
                        if (!"".equals(from_acc1)) {
                            complainPictureFlag++;
                            iv_complain_picture_1.setImageURI(Uri.parse(from_acc1));
                        } else {
                            iv_complain_picture_1.setVisibility(View.GONE);
                        }
                        com.facebook.drawee.view.SimpleDraweeView iv_complain_picture_2 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_complain_picture_2);
                        if (!"".equals(from_acc2)) {
                            complainPictureFlag++;
                            iv_complain_picture_2.setImageURI(Uri.parse(from_acc2));
                        } else {
                            iv_complain_picture_2.setVisibility(View.GONE);
                        }
                        com.facebook.drawee.view.SimpleDraweeView iv_complain_picture_3 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_complain_picture_3);
                        if (!"".equals(from_acc3)) {
                            complainPictureFlag++;
                            iv_complain_picture_3.setImageURI(Uri.parse(from_acc3));
                        } else {
                            iv_complain_picture_3.setVisibility(View.GONE);
                        }
                        //申述图片
                        com.facebook.drawee.view.SimpleDraweeView iv_inmates_picture_1 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_inmates_picture_1);
                        if (!"".equals(to_acc1)) {
                            inmatesPictureFlag++;
                            iv_inmates_picture_1.setImageURI(Uri.parse(to_acc1));
                        } else {
                            iv_inmates_picture_1.setVisibility(View.GONE);
                        }
                        com.facebook.drawee.view.SimpleDraweeView iv_inmates_picture_2 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_inmates_picture_2);
                        if (!"".equals(to_acc2)) {
                            inmatesPictureFlag++;
                            iv_inmates_picture_2.setImageURI(Uri.parse(to_acc2));
                        } else {
                            iv_inmates_picture_2.setVisibility(View.GONE);
                        }
                        com.facebook.drawee.view.SimpleDraweeView iv_inmates_picture_3 = (com.facebook.drawee.view.SimpleDraweeView) rootView.findViewById(R.id.iv_inmates_picture_3);
                        if (!"".equals(to_acc3)) {
                            inmatesPictureFlag++;
                            iv_inmates_picture_3.setImageURI(Uri.parse(to_acc3));
                        } else {
                            iv_inmates_picture_3.setVisibility(View.GONE);
                        }
                        String statusString = "";
                        if (0 == complainPictureFlag) {
                            ll_complain_picture.setVisibility(View.GONE);
                        }
                        if (0 == inmatesPictureFlag) {
                            ll_inmates_picture.setVisibility(View.GONE);
                        }
                        if (0 == complainPictureFlag && 0 == inmatesPictureFlag) {
                            ll_picture.setVisibility(View.GONE);
                        }
                        if ("0".equals(status)) {
                            statusString = "新投诉";
                            rl_inmates_detail.setVisibility(View.GONE);
                            ll_inmates_picture.setVisibility(View.GONE);
                        } else if ("1".equals(status)) {
                            statusString = "待申诉";
                            rl_inmates_detail.setVisibility(View.GONE);
                            ll_inmates_picture.setVisibility(View.GONE);
                        } else if ("2".equals(status)) {
                            ll_complain_conversation.setVisibility(View.VISIBLE);
                            statusString = "沟通中";
                        } else if ("3".equals(status)) {
                            ll_complain_conversation.setVisibility(View.VISIBLE);
                            statusString = "待仲裁";
//                                b_commit_complain.setText("待仲裁");
                            b_commit_complain.setVisibility(View.GONE);
                        } else if ("4".equals(status)) {
                            ll_complain_conversation.setVisibility(View.VISIBLE);
                            ll_complain_suggestion.setVisibility(View.VISIBLE);
                            LinearLayout ll_send_complain_conversation_chat = (LinearLayout) rootView.findViewById(R.id.ll_send_complain_conversation_chat);
                            ll_send_complain_conversation_chat.setVisibility(View.GONE);
                            LinearLayout ll_complain_conversation_chat_done = (LinearLayout) rootView.findViewById(R.id.ll_complain_conversation_chat_done);
                            ll_complain_conversation_chat_done.setVisibility(View.GONE);
                            ll_complain_suggestion.setVisibility(View.VISIBLE);
                            String handle_content = result.has("handle_content") ? result.getString("handle_content") : "";
                            TextView tv_complain_suggestion = (TextView) rootView.findViewById(R.id.tv_complain_suggestion);
                            tv_complain_suggestion.setText(handle_content);
                            statusString = "投诉结束";
                        }
                        TextView tv_complain_status = (TextView) rootView.findViewById(R.id.tv_complain_status);
                        tv_complain_status.setText(statusString);
                        JSONArray datasList = result.getJSONArray("datas");
                        showConversationContent(datasList);
                        mActivity.hideProcessDialog(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> mActivity.hideProcessDialog(1), paramap);
        mRequestQueue.add(request);
    }



    private void showConversationContent(JSONArray datasList) throws Exception {
        ll_complain_conversation_chat.removeAllViews();
        if (datasList.length() > 0) {
            for (int i = 0; i < datasList.length(); i++) {
                JSONObject data = datasList.getJSONObject(i);
                String content = data.has("content") ? data.getString("content") : "";
                String role = data.has("role") ? data.getString("role") : "";
                if (!"".equals(content) && !"".equals(role)) {
                    int startIndex = content.indexOf("]");
                    int endIndex = content.indexOf("说");
                    String time = content.substring(startIndex + 1, endIndex);
                    String name = content.substring(0, startIndex + 1);
                    String detail = content.substring((endIndex + 3), content.length());
                    View view = null;
                    if ("from_user".equals(role)) {
                        String userName = preferences.getString("userName", "");
                        view = layoutInflater.inflate(R.layout.item_complain_conversation, null);
                        TextView tv_username = (TextView) view.findViewById(R.id.tv_username);
                        tv_username.setText(name);
                        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
                        tv_time.setText(time);
                        TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
                        tv_detail.setText(detail);
                    } else {
                        view = layoutInflater.inflate(R.layout.item_complain_conversation_result, null);
                        TextView tv_seller_name = (TextView) view.findViewById(R.id.tv_seller_name);
                        tv_seller_name.setText(name);
                        TextView tv_time = (TextView) view.findViewById(R.id.tv_time);
                        tv_time.setText(time);
                        TextView tv_detail = (TextView) view.findViewById(R.id.tv_detail);
                        tv_detail.setText(detail);
                    }
                    ll_complain_conversation_chat.addView(view);
                }
            }
        }
    }

    private void sendConversationContent(String content) {
        mActivity.showProcessDialog();
        Map paramap = mActivity.getParaMap();
        paramap.put("talk_content", content);
        paramap.put("id", getArguments().getString("id"));
        RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
        Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_talk.htm",
                result -> {
                    try {
                        JSONArray datasList = result.getJSONArray("datas");
                        showConversationContent(datasList);
                        mActivity.hideProcessDialog(0);
                        ed_conversation_content.setText("");
                    } catch (/*JSON*/Exception e) {
                        e.printStackTrace();
                    }
                }, error -> {
                    mActivity.hideProcessDialog(1);
                    Toast.makeText(mActivity, "发布失败！", Toast.LENGTH_SHORT).show();
                }, paramap);
        mRequestQueue.add(request);
    }

    private void commitComplain() {
        if ("3".equals(status)) {
            Toast.makeText(mActivity, "已提交仲裁，不能重复提交此请求！", Toast.LENGTH_SHORT).show();
        } else if ("2".equals(status)) {
            mActivity.showProcessDialog();
            Map paramap = mActivity.getParaMap();
            paramap.put("id", getArguments().getString("id"));
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(getActivity()).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(getActivity(), CommonUtil.getAddress(mActivity) + "/app/buyer/complaint_arbitrate.htm",
                    result -> {
                        try {
                            String code = result.has("code") ? result.getString("code") : "";
                            if ("100".equals(code)) {
                                Toast.makeText(mActivity, "提交仲裁已成功！", Toast.LENGTH_SHORT).show();
                            }
                            mActivity.hideProcessDialog(0);
                        } catch (/*JSON*/Exception e) {
                            Toast.makeText(mActivity, "提交仲裁未成功！", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }, error -> {
                        mActivity.hideProcessDialog(1);
                        Toast.makeText(mActivity, "提交仲裁未成功！", Toast.LENGTH_SHORT).show();
                    }, paramap);
            mRequestQueue.add(request);
        }
    }

}
