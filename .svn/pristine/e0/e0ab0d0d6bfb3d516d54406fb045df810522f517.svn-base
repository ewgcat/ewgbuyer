package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品搜索
 */

public class SearchFragment extends Fragment {
    private final List list = new ArrayList();
    private BaseActivity mActivity;
    private EditText editText;
    private Bundle bundle;
    private View rootView;
    private ListView listview;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_search, container, false);
        mActivity = (BaseActivity) getActivity();
        bundle = getArguments();
        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        //设置导航图标
        toolbar.setNavigationIcon(R.mipmap.nav_arrow);
        //设置toolbar
        mActivity.setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                mActivity.hide_keyboard(v);
                mActivity.onBackPressed();
            }
        });
        View search_button = rootView.findViewById(R.id.search_button);
        SharedPreferences preferences = mActivity.getSharedPreferences(
                "user", Context.MODE_PRIVATE);
        // 获取搜索历史，以逗号间隔
        String keywords = preferences.getString("keywords", "");
        // 清除历史按钮
        final View clear_btu = rootView.findViewById(R.id.delete_history);
        final View nohistory = rootView.findViewById(R.id.nohistory);
        boolean vis = false;
        // 查询搜索历史
        if (!keywords.equals("")) {
            String keyword[] = keywords.split(",");
            for (String val : keyword) {
                if (!val.equals("")) {
                    Map map = new HashMap();
                    map.put("key", val);
                    list.add(map);
                    vis = true;
                }
            }
        }
        if (vis) {
            // 历史记录存在
            clear_btu.setVisibility(View.VISIBLE);
            nohistory.setVisibility(View.INVISIBLE);
            Collections.reverse(list);
            // 显示历史记录
            listview = (ListView) rootView.findViewById(R.id.search_history);
            SimpleAdapter sa = new SimpleAdapter(mActivity, list,
                    R.layout.item_search_history, new String[]{"key"},
                    new int[]{R.id.history_item});
            listview.setAdapter(sa);
            int totalHeight = 0;
            for (int i = 0; i < sa.getCount(); i++) {
                View listItem = sa.getView(i, null, listview);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = listview.getLayoutParams();
            params.height = totalHeight
                    + (listview.getDividerHeight() * (sa.getCount() - 1));
            listview.setLayoutParams(params);
        } else {
            clear_btu.setVisibility(View.INVISIBLE);
            nohistory.setVisibility(View.VISIBLE);
        }
        // 清空搜索历史
        clear_btu.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {

                SharedPreferences preferences2 = mActivity.getSharedPreferences("user", Context.MODE_PRIVATE);
                Editor editor = preferences2.edit();
                editor.remove("keywords");
                // 清除关键字
                editor.commit();
                clear_btu.setVisibility(View.INVISIBLE);
                listview.setVisibility(View.INVISIBLE);
                nohistory.setVisibility(View.VISIBLE);
            }
        });
        if (vis) {
            listview.setOnItemClickListener((arg0, arg1, position, id) -> {
                if (FastDoubleClickTools.isFastDoubleClick()) {

                    Map map = (Map) list.get(position);
                    String keyword = (String) map.get("key");
                    if (bundle.containsKey("store_id")) {
                        String store_id = bundle.getString("store_id");
                        mActivity.go_store_goodslist(store_id, keyword);
                        mActivity.hide_keyboard(arg0);
                    } else {
                        mActivity.go_goodslist(keyword);
                        mActivity.hide_keyboard(arg0);
                    }
                }
            });
        }
        editText = (EditText) rootView.findViewById(R.id.search_text);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            search();
            mActivity.hide_keyboard(v);
            return false;
        });

        search_button.setOnClickListener(arg0 -> {
            if (FastDoubleClickTools.isFastDoubleClick()) {
                search();
                mActivity.hide_keyboard(arg0);
            }
        });
        return rootView;
    }

    public void search() {
        InputMethodManager imm = (InputMethodManager) editText.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (editText.isFocused()) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        EditText editText = (EditText) rootView.findViewById(R.id.search_text);
        String keyword = editText.getText().toString().trim();
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(keyword);
        boolean flag = true;
        String msg = "";
        if (keyword.equals("")) {
            flag = false;
            msg = "请输入关键字！";
        }
        if (m.find()) {
            flag = false;
            msg = "不可输入特殊字符！";
        }
        if (flag) {
            // 记录关键字搜索历史，
            SharedPreferences preferences3 = mActivity.getSharedPreferences(
                    "user", Context.MODE_PRIVATE);
            String keywords_temp = preferences3.getString("keywords", "");
            // 获取搜索历史，以逗号间隔
            String keys[] = keywords_temp.split(",");
            for (String key : keys) {
                if (!key.equals("") && key.equals(keyword)) {
                    keywords_temp = keywords_temp.replace(key + ",", "");
                }
            }
            Editor editor = preferences3.edit();
            String val = keywords_temp + "," + keyword;
            editor.putString("keywords", val);
            editor.commit();
            if (bundle.containsKey("store_id")) {
                String store_id = bundle.getString("store_id");
                mActivity.go_store_goodslist(store_id, keyword);
            } else {
                mActivity.go_goodslist(keyword);
            }
        } else {
            Toast.makeText(mActivity, msg, Toast.LENGTH_SHORT).show();
        }
    }
}
