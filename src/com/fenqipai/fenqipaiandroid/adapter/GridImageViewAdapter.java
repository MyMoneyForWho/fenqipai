package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.net.URL;

public class GridImageViewAdapter extends BaseAdapter{

	private Context mContext;
	private List<Map<String, String>>  data;
	private BaseApplication application;
	
	
	public GridImageViewAdapter(Context mContext, List<Map<String, String>> data){
		this.mContext=mContext;
		this.data=data;
		this.application=(BaseApplication)mContext.getApplicationContext();
		
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


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder= new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_img_grid_show, null);
			viewHolder.img = (ImageView) convertView.findViewById(R.id.imgView);
			viewHolder.bg = (ImageView) convertView.findViewById(R.id.img_bg);
			
		if (data.get(position).get("flag").equals("0")) {
			viewHolder.bg.setVisibility(View.GONE);
		}else {
			viewHolder.bg.setVisibility(View.VISIBLE);
		}
		
		
		application.imageLoader.displayImage(
				URL.getURL( URL.FILE_UPLOAD)+data.get(position).get("imagePath")
				, viewHolder.img, application.options);
		
		
		
		return convertView;
	}
	
	public void setItemShow(int position){
		
	}
	
	
	class ViewHolder {
		ImageView img;
		ImageView bg;
	}
	
	public void setData( List<Map<String, String>>  data){
		this.data=data;
		notifyDataSetChanged();
	}
}
