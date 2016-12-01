package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fenqipai.fenqipaiandroid.R;

public class HorImgListAdapter extends BaseAdapter{

	private Context mContext;
	private List<Map<String, Object>> data;
	
	
	public HorImgListAdapter(Context mContext,List<Map<String, Object>> data){
		this.mContext=mContext;
		this.data=data;
		
	}


	@Override
	public int getCount() {
		return data.size();
	}


	@Override
	public Object getItem(int arg0) {
		return arg0;
	}


	@Override
	public long getItemId(int arg0) {
		return arg0;
	}


	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_img_show, null);
			viewHolder = new ViewHolder();
			viewHolder.tv = (TextView) convertView.findViewById(R.id.imgView_title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
			viewHolder.tv.setText(data.get(position).get("subjectName").toString());
		
			if (data.get(position).get("flag").equals("1")) {
				viewHolder.tv.setTextColor(mContext.getResources().getColor(
						R.color.white));
				viewHolder.tv.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.color.car_text_orange));
			}else {
				viewHolder.tv.setTextColor(mContext.getResources().getColor(
						R.color.car_detials_name_text));
				viewHolder.tv.setBackgroundDrawable(mContext.getResources()
						.getDrawable(R.color.onlookers_background_color));
			}
			
			
			
			
		return convertView;
	}
	
	
	class ViewHolder {
		TextView tv;
	}
	
	public void setData(List<Map<String, Object>> data){
		this.data=data;
		notifyDataSetChanged();
	}

}
