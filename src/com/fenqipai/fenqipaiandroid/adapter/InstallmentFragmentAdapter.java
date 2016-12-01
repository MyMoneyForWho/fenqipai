package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.InstallmentCar;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.ta.utdid2.core.persistent.TransactionXMLFile;

/**
 * 车辆分期
 * 
 * @name
 * @author
 * @date
 * @modify
 * @modifyDate
 * @modifyContent
 */
public class InstallmentFragmentAdapter extends BaseAdapter {
	private Context mContext;
	private List<InstallmentCar> data;
	private BaseApplication application;

	public InstallmentFragmentAdapter(Context context, List<InstallmentCar> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_installment, null);
			viewHolder = new ViewHolder();
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.name = (TextView) convertView.findViewById(R.id.car_name);
			viewHolder.startPrice = (TextView) convertView.findViewById(R.id.start_price);
			viewHolder.insurance = (TextView) convertView.findViewById(R.id.insurance_price);
			viewHolder.tax = (TextView) convertView.findViewById(R.id.tax);
			viewHolder.extractPrice = (TextView) convertView.findViewById(R.id.extract_car_price);
			viewHolder.minPayRatioPrice = (TextView) convertView.findViewById(R.id.shoufu_price);
			viewHolder.amr = (TextView) convertView.findViewById(R.id.rent_price);
			viewHolder.gpsPrice = (TextView) convertView.findViewById(R.id.gps_price);
			viewHolder.term = (TextView) convertView.findViewById(R.id.term);
			viewHolder.carstatus = (ImageView) convertView.findViewById(R.id.carstatus);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		String s = "";
//		Random ran = new Random(System.currentTimeMillis());
//		for (int j = 0; j < 10; j++) {
//			s = s + ran.nextInt(100);
//		}
//		 + "?v=" + s
		application.imageLoader.displayImage(
				URL.getURL(URL.FILE_UPLOAD) + data.get(position).getImagePath(), viewHolder.img,
				application.options);
		viewHolder.name.setText(data.get(position).getCarAllName());
		viewHolder.startPrice.setText("¥" + MoneyUtils.toWan(data.get(position).getCarPrice()) + "万");
		viewHolder.minPayRatioPrice.setText("¥" + data.get(position).getMinPayRatio());
		viewHolder.amr.setText("¥" + data.get(position).getAMR());
		viewHolder.gpsPrice.setText("¥" + data.get(position).getGpsExpanse());
		viewHolder.term.setText(data.get(position).getLoanTerm());
		viewHolder.extractPrice.setText("¥" + data.get(position).getDownPaymentPrice() + "万");
		if (data.get(position).getInsurance().equals("0")) {
			viewHolder.insurance.setText("自付");
		} else {
			viewHolder.insurance.setText("赠送");
		}
		if (data.get(position).getTax().equals("0")) {
			viewHolder.tax.setText("自付");
		} else {
			viewHolder.tax.setText("赠送");
		}
		if (data.get(position).getCarStatus().equals("1")) {
			viewHolder.carstatus.setImageResource(R.drawable.icon_new_car);
		} else {
			viewHolder.carstatus.setImageResource(R.drawable.icon_used_car);
		}
		return convertView;
	}

	class ViewHolder {

		ImageView img, carstatus;
		TextView name, startPrice, insurance, tax, extractPrice, minPayRatioPrice, amr, gpsPrice, term;

	}

	public List<InstallmentCar> getList() {
		return data;
	}

	public void setList(List<InstallmentCar> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
