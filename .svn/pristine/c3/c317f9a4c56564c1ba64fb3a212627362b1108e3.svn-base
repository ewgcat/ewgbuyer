package com.ewgvip.buyer.android.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.models.BankCardItem;
import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/17.
 */
public class MyBankCardListAdapter extends BaseAdapter {

    BaseActivity mActivity;
    private ArrayList<BankCardItem> list;

    public MyBankCardListAdapter(BaseActivity mActivity, ArrayList<BankCardItem> list) {
        this.mActivity = mActivity;
        this.list = list;
    }

   public void update(ArrayList<BankCardItem> list){
       this.list = list;
       notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){
            convertView=View.inflate(mActivity, R.layout.item_bank_card,null);
        }
        ImageView iv_bank_logo= (ImageView) convertView.findViewById(R.id.iv_bank_logo);
        TextView tv_bank_name= (TextView) convertView.findViewById(R.id.tv_bank_name);
        TextView tv_bankcard_number= (TextView) convertView.findViewById(R.id.tv_bankcard_number);
        BankCardItem bankCardItem = list.get(position);
        Glide.with(mActivity).load(bankCardItem.bank_img).into(iv_bank_logo);
        tv_bank_name.setText(bankCardItem.bank_name);
        String cardNumber = bankCardItem.card_number;
        String lastFourCardNumber = cardNumber.substring(15);
        tv_bankcard_number.setText("**** **** **** ***"+lastFourCardNumber);

        return convertView;
    }

}
