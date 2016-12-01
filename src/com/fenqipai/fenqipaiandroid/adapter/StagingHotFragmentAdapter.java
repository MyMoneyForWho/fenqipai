package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 车辆竞拍
 * 
 * @name
 * @author
 * @date
 * @modify
 * @modifyDate
 * @modifyContent
 */
public class StagingHotFragmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<SaleCar> data;
	private BaseApplication application;

	public StagingHotFragmentAdapter(Context context, List<SaleCar> data) {
		this.mContext = context;
		this.data = data;
		this.application = (BaseApplication) mContext.getApplicationContext();
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_staging_hot, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.name = (TextView) convertView.findViewById(R.id.car_name);
			viewHolder.startPrice = (TextView) convertView.findViewById(R.id.start_price);
			viewHolder.pingguPrice = (TextView) convertView.findViewById(R.id.pinggu_price);
			viewHolder.endTime = (TextView) convertView.findViewById(R.id.end_time);
			viewHolder.endTimeTwo = (TextView) convertView.findViewById(R.id.end_time_two);
			viewHolder.onlookers = (TextView) convertView.findViewById(R.id.bid_onlookers);
			viewHolder.bidNums = (TextView) convertView.findViewById(R.id.bid_num);
			viewHolder.auctionType = (ImageView) convertView.findViewById(R.id.auctionType);
			viewHolder.carstatus = (ImageView) convertView.findViewById(R.id.carstatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		String s = "";  
//		Random ran =new Random(System.currentTimeMillis());  
//		for (int j = 0; j < 10; j++) {
//			s = s + ran.nextInt(100);
//		}
//		 + "?v=" + s
		application.imageLoader.displayImage(URL.getURL(URL.FILE_UPLOAD) + data.get(position).getImagePath(),
				viewHolder.img, application.options);
		viewHolder.name.setText(data.get(position).getCarAllName());
		viewHolder.startPrice.setText("¥" + data.get(position).getNowPrice() + "万");
		viewHolder.pingguPrice.setText("¥" + MoneyUtils.toWan(data.get(position).getFlatlyPrice()) + "万");
		viewHolder.endTime.setText(TimeUtils.convertTimeToDate(data.get(position).getEndTime()));
		viewHolder.endTimeTwo.setText(TimeUtils.convertTimeToMin(data.get(position).getEndTime()));
		viewHolder.onlookers.setText(data.get(position).getOnlookersCount());
		viewHolder.bidNums.setText(data.get(position).getPersonCount());
	    if (data.get(position).getAuctionType().equals("1")) {
        	viewHolder.auctionType.setImageResource(R.drawable.icon_gaopai);
		}else {
			viewHolder.auctionType.setImageResource(R.drawable.icon_kuaipai);
		}
	    if (data.get(position).getCarStatus().equals("1")) {
			viewHolder.carstatus.setImageResource(R.drawable.icon_new_car);
		}else {
			viewHolder.carstatus.setImageResource(R.drawable.icon_used_car);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img,auctionType,carstatus;
		TextView name, startPrice, pingguPrice, endTime,endTimeTwo,onlookers,bidNums;
         
	}

	public List<SaleCar> getList() {
		return data;
	}

	public void setList(List<SaleCar> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
