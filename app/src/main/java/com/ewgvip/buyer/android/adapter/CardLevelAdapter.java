package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.CardInfo;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/11/29.
 */

public class CardLevelAdapter extends BaseAdapter {
    private ArrayList<CardInfo> list;
    private BaseActivity mActivity;

    public CardLevelAdapter(BaseActivity mActivity, ArrayList<CardInfo> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        CardLevelAdapter.ViewHodler viewHodler = null;
        if (convertView == null) {
            convertView = View.inflate(mActivity, R.layout.item_card_level_manange, null);
            viewHodler = new CardLevelAdapter.ViewHodler();
            viewHodler.tv_card_code = (TextView) convertView.findViewById(R.id.tv_card_code);
            viewHodler.tv_grade_name = (TextView) convertView.findViewById(R.id.tv_grade_name);
            viewHodler.tv_cardnumber = (TextView) convertView.findViewById(R.id.tvcardnumber);
            viewHodler.tv_password = (TextView) convertView.findViewById(R.id.tvpassword);

            convertView.setTag(viewHodler);

        } else {
            viewHodler = (CardLevelAdapter.ViewHodler) convertView.getTag();
        }
        CardInfo cardInfo = list.get(position);

        viewHodler.tv_card_code.setText(cardInfo.card_code);
        viewHodler.tv_grade_name.setText(cardInfo.grade_name);
        viewHodler.tv_cardnumber.setText(cardInfo.card_no);
        viewHodler.tv_password.setText("密码:******点击查看密码");
        ViewHodler finalViewHodler = viewHodler;
        viewHodler.tv_password.setOnClickListener(view -> finalViewHodler.tv_password.setText("密码:"+cardInfo.expressly_password));
        return convertView;
    }

    private static class ViewHodler {

        private TextView tv_card_code;
        private TextView tv_grade_name;
        private TextView tv_cardnumber;
        private TextView tv_password;
    }
}
