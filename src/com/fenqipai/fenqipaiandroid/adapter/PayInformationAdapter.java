package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.PayFullActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.PayInfo;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 支付信息列表适配器
 * 
 * @author wangZhonghan
 */
public class PayInformationAdapter extends BaseAdapter {
	private Context mContext;
	private List<PayInfo> data;
	private BaseApplication application;
	private Handler mHandler;

	public PayInformationAdapter(Context context, List<PayInfo> data,Handler mHandler) {
		this.mContext = context;
		this.data = data;
		this.mHandler = mHandler;
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

	private String resultMessage;

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_pay_information, null);
			viewHolder = new ViewHolder();
			viewHolder.number = (TextView) convertView.findViewById(R.id.pay_serial_number);
			viewHolder.orderday = (TextView) convertView.findViewById(R.id.oncreate_day);
			viewHolder.orderMin = (TextView) convertView.findViewById(R.id.oncreate_min);
			viewHolder.ordertype = (TextView) convertView.findViewById(R.id.buy_type);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.confirm = (LinearLayout) convertView.findViewById(R.id.confirm);
			viewHolder.cancel = (ImageView) convertView.findViewById(R.id.cancel);
			viewHolder.buyType = (TextView) convertView.findViewById(R.id.buy_type_string);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		/*
		 * application.imageLoader.displayImage( URL.getURL( URL.FILE_UPLOAD) +
		 * data.get(position).getCarImgPath(), viewHolder.carImg,
		 * application.options);
		 */
		viewHolder.number.setText(data.get(position).getPayInfoNumber());
		viewHolder.orderday.setText(TimeUtils.convertTimeToDate(data.get(position).getOrderTime()));
		viewHolder.orderMin.setText(TimeUtils.convertTimeToMin(data.get(position).getOrderTime()));
		viewHolder.ordertype.setText(data.get(position).getPayType());
		viewHolder.money.setText(data.get(position).getMoney());
		viewHolder.state.setText(data.get(position).getState());
		viewHolder.buyType.setText(data.get(position).getPayType() + "流水信息");

		viewHolder.cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Message message = new Message();
				message.what = 1;
				message.arg1 = position;
				message.obj = v;
				mHandler.sendMessage(message);
			}
		});

		viewHolder.confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, PayFullActivity.class);
				intent.putExtra("price", viewHolder.money.getText().toString());
				intent.putExtra("ordertype", viewHolder.buyType.getText().toString());
				intent.putExtra("payType", "payId");
				intent.putExtra("activityType", "1");
				intent.putExtra("payId", data.get(position).getSysId());
				mContext.startActivity(intent);
			}
		});

		return convertView;
	}


	class ViewHolder {

		ImageView carImg, cancel;
		TextView number, orderday, orderMin, ordertype, money, state, buyType;
		LinearLayout confirm;

	}

	
	public List<PayInfo> getList() {
		return data;
	}

	public void setList(List<PayInfo> list) {
		this.data = list;
		notifyDataSetChanged();
	}

	public void finish() {
		notifyDataSetChanged();
	}
}
