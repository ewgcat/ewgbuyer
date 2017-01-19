package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.SpreadDrawoutHistoryadapter;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 *
 */
public class SpreadDrawoutHistoryFragment extends Fragment {

    @BindView(R.id.listView)
    ListView listView;
    private BaseActivity mActivity;
    private View rootView;
    private int visibleLastIndex;
        int begincount=0;
        int selectcount=10;
        ArrayList<String> list=new ArrayList<>();
    private SpreadDrawoutHistoryadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_spread_drawout_history, container, false);
        ButterKnife.bind(this, rootView);


        adapter = new SpreadDrawoutHistoryadapter(mActivity, list);
        listView.setAdapter(adapter);
        getData();
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int itemsLastIndex = adapter.getCount() - 1;    //数据集最后一项的索引
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && visibleLastIndex == itemsLastIndex) {
                    //如果是自动加载,可以在这里放置异步加载数据的代码
                    getData();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                visibleLastIndex = firstVisibleItem + visibleItemCount - 1;
            }
        });

        return rootView;
    }

private  void  getData(){
    Map paraMap = mActivity.getParaMap();
    paraMap.put("begin_count",begincount+"");
    paraMap.put("select_count",selectcount+"");
    RetrofitClient.getInstance(mActivity,null,mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress()
            + "/app/buyer/promotion_income_withdrawal_list.htm", paraMap, new BaseSubscriber<JSONObject>(mActivity) {
        @Override
        public void onNext(JSONObject jsonObject) {
            try {
                JSONArray withdrawal_list = jsonObject.getJSONArray("withdrawal_list");
                for (int i = 0; i < withdrawal_list.length(); i++) {
                    JSONObject  jso= (JSONObject) withdrawal_list.get(i);
                    String time = jso.getString("time");
                    String withdrawal_amount = jso.getString("withdrawal_amount");
                    String withdrawalState = jso.getString("withdrawalState");
                    list.add(i+1+"  "+ time+"   ￥"+withdrawal_amount+"  "+withdrawalState);
                }
                begincount+=withdrawal_list.length();
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    });

}
    @OnClick(R.id.iv_back)
    public void onClick() {
        if (FastDoubleClickTools.isFastDoubleClick()) {
            mActivity.onBackPressed();
        }
    }


}
