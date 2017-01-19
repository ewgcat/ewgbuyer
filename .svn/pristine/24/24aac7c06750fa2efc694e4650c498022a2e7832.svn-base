package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.glideutils.GlideCircleTransform;
import com.ewgvip.buyer.android.utils.DensityUtil;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;
import com.ewgvip.buyer.android.utils.ImageItem;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.websocket.android_websockets.WebSocketClient;
import com.facebook.drawee.view.SimpleDraweeView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ewgvip.buyer.android.R.id.iv_goods_icon;

/**
 * 客服聊天
 */
public class ChatFragment extends Fragment {
    public static final int CHAT_PHOTO = 0;
    private BaseActivity mActivity;
    private View rootView;
    private Bundle bundle;
    private EditText et;
    private List data_list;
    private ChatAdapter myadapter;
    private ListView chat_list;
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            myadapter.notifyDataSetChanged();
            chat_list.setSelection(chat_list.getBottom());
        }
    };
    private Map map;
    private int flag = 1;
    private String startURLString;
    private String endURLString;
    private String imageSign = "<img id='waiting_img' src='";
    private String imageEnd = "' onclick='show_image(this)' style='max-height:50px;cursor:pointer'/>";
    private String service_id;//聊天客服ID
    private WebSocketClient client;
    private ChatFragment chatFragment;



    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        mActivity = (BaseActivity) getActivity();
        startURLString = "<img src=\"" + mActivity.getAddress() + "/resources/style/im/images/emo/";
        endURLString = ".gif\">";
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setTitle("联系客服");//设置标题
        mActivity.setSupportActionBar(toolbar);//设置toolbar
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);//设置导航图标
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.onBackPressed();
            }
        });
        setHasOptionsMenu(true);//设置菜单可用


        List<BasicNameValuePair> extraHeaders = Arrays.asList(
                new BasicNameValuePair("Cookie", "session=abcd")
        );


        client = new WebSocketClient(URI.create("ws://" + getString(R.string.url) + "/websocket"), new WebSocketClient.Listener() {
            @Override
            public void onConnect() {//建立连接时发送验证信息

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("type", "user");
                    jsonObject.put("service_id", service_id);
                    Map para = mActivity.getParaMap();
                    jsonObject.put("user_id", para.get("user_id"));
                    jsonObject.put("token", para.get("token").toString().toLowerCase());
                    client.send(jsonObject.toString());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onMessage(String message) {//接收消息

                JSONObject data = null;
                try {
                    data = new JSONObject(message);


                    String chatString = data.getString("content");
                    Map map = new HashMap();
                    if (data.getString("send_from").equals("service")) {
                        if (chatString.contains(imageSign)) {
                            chatString = chatString.replace(imageSign, "");
                            chatString = chatString.replace(imageEnd, "");
                            map.put("type", "image");
                            map.put("imageURL", chatString);
                        } else {
                            map.put("type", "1");
                            map.put("data", data.getString("content"));
                        }
                    } else {
                        map.put("type", "0");
                        map.put("data", data.getString("content"));
                    }


                    map.put("time", data.getString("addTime"));
                    map.put("chatlog_id", data.get("id") + "");
                    data_list.add(map);


                    data = new JSONObject();
                    data.put("type", "user");
                    data.put("chatlog_id", map.get("chatlog_id"));
                    client.send(data.toString());

                    refresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onMessage(byte[] message) {
                JSONObject data = null;
                try {
                    data = new JSONObject(message.toString());
                    Map map = new HashMap();

                    map.put("type", "1");
                    map.put("data", data.getString("content"));

                    map.put("time", data.getString("addTime"));
                    map.put("chatlog_id", data.get("id") + "");
                    data_list.add(map);


                    data = new JSONObject();
                    data.put("type", "user");
                    data.put("chatlog_id", map.get("chatlog_id"));
                    client.send(data.toString());

                    refresh();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onDisconnect(int code, String reason) {

            }

            @Override
            public void onError(Exception error) {

            }
        }, extraHeaders);


        bundle = getArguments();
        if (bundle != null) {
            final String goods_id = bundle.containsKey("goods_id") ? bundle.getString("goods_id") : "";
            String goods_name = bundle.getString("goods_name");
            String photo = bundle.getString("photo");

            Map paramap = mActivity.getParaMap();
            paramap.put("goods_id", goods_id);

            //分配客服
            RequestQueue mRequestQueue = MySingleRequestQueue.getInstance(mActivity).getRequestQueue();
            Request<JSONObject> request = new NormalPostRequest(mActivity,
                    mActivity.getAddress() + "/app/buyer/chatting_distribute.htm",
                    result -> {
                        try {
                            if (mActivity.islogin()) {
                                if (result.has("result") && result.has("code")) {
                                    if (result.getString("result").equals("SUCCESS") && result.getInt("code") == 60001) {
                                        JSONObject obj = result.getJSONObject("data");
                                        service_id = obj.get("service_id") + "";
                                        client.connect();
                                    }
                                }
                            } else {
                                if (chatFragment != null) {
                                    Fragment current_fragment = getFragmentManager().findFragmentById(R.id.index_top);
                                    current_fragment = chatFragment;
                                    if (current_fragment instanceof GoodsFragment) {
                                        FragmentManager fragmentManager = getFragmentManager();
                                        fragmentManager.popBackStack();
                                        fragmentManager.beginTransaction().remove(current_fragment).commit();
                                    }
                                }

//                                android.app.Fragment fragment = getCurrentfragment();
//                                if (fragment != null && fragment instanceof GoodsContainerFragment) {
//                                    GoodsContainerFragment f = (GoodsContainerFragment) fragment;
//                                    int i = f.get_index();
//                                    if (i > 0)
//                                        f.go_index();
//                                    else
//                                        my_back_pressed();
//                                } else if (SuccessFragment.flag) {
//                                    if (this instanceof BaseActivity) {
//                                        FragmentManager fragmentManager = getFragmentManager();
//                                        int count = fragmentManager.getBackStackEntryCount();
//                                        if (count > 0) {
//                                            go_index();
//                                        }
//                                    }
//                                    SuccessFragment.flag = false;
//                                } else {
//                                    my_back_pressed();
//                                }
                            }
                        } catch (JSONException e) {
                        }
                        mActivity.hideProcessDialog(0);
                    }, error -> mActivity.hideProcessDialog(1), paramap);
            mRequestQueue.add(request);


            map = mActivity.getParaMap();
            map.put("goods_id", goods_id);
            et = (EditText) rootView.findViewById(R.id.etText);
            data_list = new ArrayList();
            chat_list = (ListView) rootView.findViewById(R.id.listViewMain);
            chat_list.setDivider(null);
            myadapter = new ChatAdapter(mActivity, data_list);
            chat_list.setAdapter(myadapter);
            //发送聊天信息
            rootView.findViewById(R.id.tvSubmit)
                    .setOnClickListener(v -> {
                        if (FastDoubleClickTools.isFastDoubleClick()) {
                            final String chatting_content = et.getText().toString();
                            if (chatting_content != null && !chatting_content.equals("")) {
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("type", "user");
                                    jsonObject.put("service_id", service_id);
                                    Map para = mActivity.getParaMap();
                                    jsonObject.put("user_id", para.get("user_id"));
                                    jsonObject.put("content", chatting_content);

                                    client.send(jsonObject.toString());

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                et.setText("");
                                Map map1 = new HashMap();
                                map1.put("type", "0");
                                map1.put("data", chatting_content);
                                data_list.add(map1);
                                refresh();
                            }
                        }
                    }
                    );
        }


        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        mActivity.setIconEnable(menu, true);
        super.onCreateOptionsMenu(menu, inflater);

    }

    /**
     * 菜单图文混合
     *
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_index) {
            mActivity.go_index();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 替换图片
     */
    private SpannableString addImageSpan(String sName) {
        String sendNameString = startURLString + sName + endURLString;
        String sNewName = "#[" + sName + "]";
        SpannableString spanString = new SpannableString(sendNameString);
        sName = "emoji_" + sName;
        int id = getResources().getIdentifier(sName, "mipmap", "com.ewgvip.buyer.android");
        Drawable drawable = getResources().getDrawable(id);
        drawable.setBounds(0, 0, DensityUtil.dip2px(mActivity, 25), DensityUtil.dip2px(mActivity, 25));
        ImageSpan span = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spanString.setSpan(span, 0, sendNameString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanString;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        client.disconnect();
    }


    private void refresh() {

        mHandler.sendEmptyMessage(0);
    }


    class ChatAdapter extends BaseAdapter {

        Context context;
        LayoutInflater inflater;
        List<Map> mylist;

        public ChatAdapter(Context context, List<Map> list) {
            this.context = context;
            mylist = list;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {

            return mylist.size();
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

            Map map = mylist.get(position);
            if (map.get("type").equals("2")) {
                ImageItem imageItem = (ImageItem) map.get("data");
                View view = View.inflate(mActivity, R.layout.item_chat_photo, null);
                ImageView user_content = (ImageView) view.findViewById(R.id.user_content);
                View user = view.findViewById(R.id.user);
                View store = view.findViewById(R.id.store);
                ImageView iv_goods_icon = (ImageView) view.findViewById(R.id.iv_goods_icon);
                store.setVisibility(View.GONE);
                user.setVisibility(View.VISIBLE);

                iv_goods_icon.setImageURI(Uri.parse( mActivity.getCache("user_image_photo_url")));

                return view;
            } else if (map.get("type").equals("image")) {
                View view = View.inflate(mActivity, R.layout.item_chat_photo, null);
                SimpleDraweeView store_content = (SimpleDraweeView) view.findViewById(R.id.store_content);
                View user = view.findViewById(R.id.user);
                View store = view.findViewById(R.id.store);
                store.setVisibility(View.VISIBLE);
                user.setVisibility(View.GONE);
                store_content.setImageURI(Uri.parse( (String) map.get("imageURL")));
                return view;
            }
            ViewHolder holder = null;
            View view = null;
            if (convertView != null && convertView instanceof RelativeLayout) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(mActivity, R.layout.item_chat, null);
                holder = new ViewHolder();
                holder.store_content = (TextView) view.findViewById(R.id.store_content);
                holder.user_content = (TextView) view.findViewById(R.id.user_content);
                holder.user = view.findViewById(R.id.user);
                holder.store = view.findViewById(R.id.store);
                holder.iv_goods = (ImageView) view.findViewById(R.id.iv_goods);
                holder.iv_goods_icon = (ImageView) view.findViewById(iv_goods_icon);
                view.setTag(holder);
            }
            String chatString = (String) map.get("data");
            if (chatString.contains("<br>")) {
                chatString = chatString.replace("<br>", "");
            }
            if (map.get("type").equals("0")) {
                holder.store.setVisibility(View.GONE);
                holder.user.setVisibility(View.VISIBLE);
                holder.user_content.setText("");
                String urlString = mActivity.getCache("user_image_photo_url");
                Glide.with(mActivity).load(urlString).transform(new GlideCircleTransform(mActivity)).into( holder.iv_goods_icon);
                holder.iv_goods_icon.setVisibility(View.VISIBLE);
                if (chatString.contains(startURLString)) {
                    String[] chatArrayString = chatString.split(startURLString);
                    for (int i = 0; i < chatArrayString.length; i++) {

                        String chatArrayStringItem = chatArrayString[i];
                        if (chatArrayStringItem.contains(endURLString)) {
                            String[] lastArrayString = chatArrayStringItem.split(endURLString);
                            for (int j = 0; j < lastArrayString.length; j++) {

                                String lastArrayStringItem = lastArrayString[j];
                                if (j == 0) {
                                    SpannableString spannableString = addImageSpan(lastArrayStringItem);
                                    holder.user_content.append(spannableString);
                                } else {
                                    holder.user_content.append(lastArrayStringItem);
                                }
                            }
                        } else {
                            holder.user_content.append(chatArrayStringItem);
                        }
                    }
                } else {
                    holder.user_content.setText(chatString);
                }
            } else {
                holder.store.setVisibility(View.VISIBLE);
                holder.user.setVisibility(View.GONE);
                holder.store_content.setText("");
                if (chatString.contains(startURLString)) {
                    String[] chatArrayString = chatString.split(startURLString);
                    for (int i = 0; i < chatArrayString.length; i++) {

                        String chatArrayStringItem = chatArrayString[i];
                        if (chatArrayStringItem.contains(endURLString)) {
                            String[] lastArrayString = chatArrayStringItem.split(endURLString);
                            for (int j = 0; j < lastArrayString.length; j++) {

                                String lastArrayStringItem = lastArrayString[j];
                                if (j == 0) {
                                    SpannableString spannableString = addImageSpan(lastArrayStringItem);
                                    holder.store_content.append(spannableString);
                                } else {
                                    holder.store_content.append(lastArrayStringItem);
                                }
                            }
                        } else {
                            holder.store_content.append(chatArrayStringItem);
                        }
                    }
                } else {
                    holder.store_content.setText(chatString);
                }
            }
            return view;
        }

        public class ViewHolder {
            public View user;
            public View store;
            public TextView store_content;
            public TextView user_content;
            private ImageView iv_goods;
            private ImageView iv_goods_icon;
        }

    }

    /**
     * 获取当前显示的fragment
     *
     * @return
     */
    public Fragment getCurrentfragment() {
        Fragment current_fragment = getFragmentManager().findFragmentById(R.id.index_top);
        if (current_fragment != null && current_fragment.isHidden()) {
//            Fragment fragment = ChatFragment.getInstance();
//            if (!fragment.isHidden()) {
//                current_fragment = fragment;
//            }

        }
        return current_fragment;
    }

}
