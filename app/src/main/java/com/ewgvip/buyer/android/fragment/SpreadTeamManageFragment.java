package com.ewgvip.buyer.android.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.adapter.SpreadTeamManageInfoadapter;
import com.ewgvip.buyer.android.models.SpreadTeamManageInfo;
import com.ewgvip.buyer.android.net.retrofitclient.RetrofitClient;
import com.ewgvip.buyer.android.net.subsrciber.BaseSubscriber;
import com.ewgvip.buyer.android.utils.FastDoubleClickTools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/12/6.
 */
public class SpreadTeamManageFragment extends Fragment {

    @BindView(R.id.listView)
    ListView listView;
    private BaseActivity mActivity;
    private View rootView;
    ArrayList<SpreadTeamManageInfo> list = new ArrayList<>();
    private SpreadTeamManageInfoadapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mActivity = (BaseActivity) getActivity();
        rootView = inflater.inflate(R.layout.fragment_spread_team_manange, container, false);
        ButterKnife.bind(this, rootView);
        adapter = new SpreadTeamManageInfoadapter(mActivity, list);
        listView.setAdapter(adapter);
        init();
        return rootView;
    }

    private void init() {
        RetrofitClient.getInstance(mActivity, null, mActivity.getParaMap()).createBaseApi().postJSONObject(mActivity.getAddress()
                + "/app/buyer/userpromotionteam.htm", mActivity.getParaMap(), new BaseSubscriber<JSONObject>(mActivity) {
            @Override
            public void onNext(JSONObject jsonObject) {
                try {
                    JSONArray userPromotionTeam = jsonObject.getJSONArray("userPromotionTeam");
                    for (int i = 0; i < userPromotionTeam.length(); i++) {
                        JSONObject jo = (JSONObject) userPromotionTeam.get(i);
                        String team_username = jo.getString("team_username");
                        String grade_name = jo.getString("grade_name");
                        list.add(new SpreadTeamManageInfo(i+1+"", team_username, grade_name));
                    }
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
