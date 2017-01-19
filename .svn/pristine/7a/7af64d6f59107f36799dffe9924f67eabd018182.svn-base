package com.ewgvip.buyer.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ewgvip.buyer.android.R;

import java.util.List;
import java.util.Map;
/**
 * 
* <p>Title: ConsultListAdapter.java</p>

* <p>Description: </p>

* <p>Copyright: Copyright (c) 2014</p>

* <p>Company: 沈阳网之商科技有限公司 www.iskyshop.com</p>

* @author lixiaoyang

* @date 2014-8-18

* @version 1.0
 */
public class ConsultListAdapter extends BaseAdapter {
	Context context;
	LayoutInflater inflater;
	List<Map<String, String>> myList;

	public ConsultListAdapter(Context context, List<Map<String, String>> myList) {
		this.myList = myList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {

		return myList.size();
	}

	@Override
	public Object getItem(int position) {

		return myList.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.item_consult, null);
			holder = new ViewHolder();
			holder.user = (TextView) convertView.findViewById(R.id.user);
			holder.addTime = (TextView) convertView.findViewById(R.id.addtime);
			holder.question = (TextView) convertView
					.findViewById(R.id.question);
			holder.answer = (TextView) convertView.findViewById(R.id.answer);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		Map<String, String> mymap = myList.get(position);
		holder.user.setText(mymap.get("consult_user"));
		holder.addTime.setText(mymap.get("addTime"));
		holder.question.setText(mymap.get("content"));

		if (!mymap.containsKey("reply_content"))
			holder.answer.setText("暂无回答");
		else
			holder.answer.setText(mymap.get("reply_content"));
		return convertView;
	}

	public static class ViewHolder {
		public TextView user;
		public TextView addTime;
		public TextView question;
		public TextView answer;
	}

}
