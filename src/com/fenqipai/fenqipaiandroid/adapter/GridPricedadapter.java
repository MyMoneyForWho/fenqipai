package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GridPricedadapter extends BaseAdapter{

	private Context mContext;
	private List<String>  data;
	private BaseApplication application;
	
	
	public GridPricedadapter(Context mContext, List<String> data){
		this.mContext=mContext;
		this.data=data;
		this.application=(BaseApplication)mContext.getApplicationContext();
		
	}


	@Override
	public int getCount() {
		return data.size();
	}


	@Override
	public Object getItem(int position) {
		return data.get(position);
	}


	@Override
	public long getItemId(int position) {
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bid_gridview, null);
			viewHolder = new ViewHolder();
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.price.setText(data.get(position));
		return convertView;
	}
	
	
	class ViewHolder {
		TextView price;
	}
	
	public void setData( List<String>  data){
		this.data=data;
		notifyDataSetChanged();
	}
}
