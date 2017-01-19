package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.ewgvip.buyer.android.R;
import com.ewgvip.buyer.android.activity.BaseActivity;
import com.ewgvip.buyer.android.utils.CommonUtil;
import com.ewgvip.buyer.android.volley.NormalPostRequest;
import com.ewgvip.buyer.android.volley.Request;
import com.ewgvip.buyer.android.volley.RequestQueue;
import com.ewgvip.buyer.android.volley.Response;
import com.ewgvip.buyer.android.volley.VolleyError;
import com.ewgvip.buyer.android.utils.MySingleRequestQueue;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/11/25.
 */
public class DialogAllAdapter extends BaseAdapter {

    BaseActivity mActivity;
    List list;
    String contern_id;
    LayoutInflater layoytInflater;
    public   DialogAllAdapter(BaseActivity mActivity,List list,String contern_id)
    {
        this.mActivity=mActivity;
        this.list=list;
        this.contern_id=contern_id;

        layoytInflater=(LayoutInflater)mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return list.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHodler holder = null;
        if (view == null) {
            holder = new ViewHodler();
            view = layoytInflater.inflate(R.layout.item_dialogall, null);
            holder.bt_resetguan_dialogall = (TextView) view.findViewById(R.id.bt_resetguan_dialogall);
            holder.checkBox_list = (CheckBox) view.findViewById(R.id.checkBox_list);
            holder.linearLayout_dialog_item = (LinearLayout) view.findViewById(R.id.linearLayout_dialog_item);
            view.setTag(holder);
        } else {
            holder = (ViewHodler) view.getTag();
        }
        holder.bt_resetguan_dialogall.setText(list.get(i).toString());

        return view;
    }
    public static class ViewHodler{

        private TextView bt_resetguan_dialogall;
        private CheckBox checkBox_list;
        private  LinearLayout linearLayout_dialog_item;

    }
}
