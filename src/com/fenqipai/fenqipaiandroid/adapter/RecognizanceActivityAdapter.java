package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.net.URL;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 保证金listview适配器
 * 
 * @name RecognizanceActivityAdapter
 * @author hunaixin
 */
public class RecognizanceActivityAdapter extends BaseAdapter {
	private Context mContext;
	private List<SaleCar> data;
	private BaseApplication application;
	private Handler mHandler;

	public RecognizanceActivityAdapter(Context context, List<SaleCar> data) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_recognizance_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.lotNum = (TextView) convertView.findViewById(R.id.lot_number);
			viewHolder.depositType = (TextView) convertView.findViewById(R.id.deposit_type);
			viewHolder.transactionTime = (TextView) convertView.findViewById(R.id.auction_time);
		    viewHolder.deposit = (TextView) convertView.findViewById(R.id.deposit_text);
		    viewHolder.depositBtn = (Button) convertView.findViewById(R.id.btn_deposit_back);
		    viewHolder.carImg = (ImageView) convertView.findViewById(R.id.car_img);
		    viewHolder.carName = (TextView) convertView.findViewById(R.id.car_name);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		application.imageLoader.displayImage(
				URL.getURL( URL.FILE_UPLOAD) + data.get(position).getImagePath(), viewHolder.carImg,
				application.options);
		viewHolder.lotNum.setText("Pm62854");
		viewHolder.depositType.setText("拍卖保证金");
		viewHolder.transactionTime.setText("2016/06/06");
        viewHolder.deposit.setText("10000.00");

		return convertView;
	}

	class ViewHolder {
		ImageView carImg;
		TextView lotNum, depositType, transactionTime,deposit,carName;
		Button depositBtn;
	}

	public List<SaleCar> getList() {
		return data;
	}

	public void setList(List<SaleCar> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
