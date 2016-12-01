package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页横滑gridview适配器
 * 
 * @name HomeFragmentAdapter
 * @author hunaixin
 */
public class HomeFragmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<SaleCar> data;
	private BaseApplication application;

	public HomeFragmentAdapter(Context context, List<SaleCar> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.price = (TextView) convertView.findViewById(R.id.price);
			viewHolder.lookers = (TextView) convertView.findViewById(R.id.lookers);
			viewHolder.auctionType = (ImageView) convertView.findViewById(R.id.auctionType);
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
		application.imageLoader.displayImage(
				URL.getURL( URL.FILE_UPLOAD) + data.get(position).getImagePath(), viewHolder.img,
				application.options);
        viewHolder.name.setText(data.get(position).getCarAllName());
        viewHolder.price.setText("¥" + data.get(position).getNowPrice() + "万");
        viewHolder.lookers.setText("¥" + MoneyUtils.toWan(data.get(position).getFlatlyPrice()) + "万");
        if (data.get(position).getAuctionType().equals("1")) {
        	viewHolder.auctionType.setImageResource(R.drawable.icon_gaopai);
		}else {
			viewHolder.auctionType.setImageResource(R.drawable.icon_kuaipai);
		}
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView name;
		TextView price;
		TextView lookers;
		ImageView auctionType;
	}

	public List<SaleCar> getList() {
		return data;
	}

	public void setList(List<SaleCar> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
