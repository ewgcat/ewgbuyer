package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ewgvip.buyer.android.R;

/**
 * Created by Administrator on 2015/10/29.
 */
public class ChatFaceAdapter extends BaseAdapter {

    private SparseArray<String> sparseArray;
    private LayoutInflater layoutInflater;


    public ChatFaceAdapter(Context context, SparseArray<String> sparseArray) {
        this.sparseArray = sparseArray;
        layoutInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sparseArray.size();
    }

    @Override
    public Integer getItem(int position) {
        return sparseArray.keyAt(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = layoutInflater.inflate(
                    R.layout.chat_face_item, null);
            viewHolder = new ViewHolder();
            viewHolder.imgFace = (ImageView) convertView
                    .findViewById(R.id.imgFace);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.imgFace.setBackgroundResource(getItem(position));

        return convertView;
    }


    private final class ViewHolder {
        public ImageView imgFace;
    }
}

