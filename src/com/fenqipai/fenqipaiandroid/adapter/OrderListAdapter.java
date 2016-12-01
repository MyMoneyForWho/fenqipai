package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 车辆订单列表适配器
 * 
 * @author hunaixin
 */
public class OrderListAdapter extends BaseAdapter {
	private Context mContext;
	private List<OrderList> data;
	private BaseApplication application;

	public OrderListAdapter(Context context, List<OrderList> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_order_list, null);
			viewHolder = new ViewHolder();
			viewHolder.name = (TextView) convertView.findViewById(R.id.car_name);
			viewHolder.orderday = (TextView) convertView.findViewById(R.id.on_create_day);
			viewHolder.ordertype = (TextView) convertView.findViewById(R.id.order_type);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.orderMin = (TextView) convertView.findViewById(R.id.on_create_min);
			viewHolder.orderNum = (TextView) convertView.findViewById(R.id.order_num);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.name.setText(data.get(position).getCarAllName());
		viewHolder.orderday.setText(TimeUtils.convertTimeToDate(data.get(position).getOrderTime()));
		viewHolder.orderMin.setText(TimeUtils.convertTimeToMin(data.get(position).getOrderTime()));
		viewHolder.ordertype.setText(data.get(position).getOrderType());
		viewHolder.money.setText(data.get(position).getOrderMoney());
		viewHolder.orderNum.setText(data.get(position).getOrderNumber());
		String status = data.get(position).getOrderStatus();
		if (status.equals("unconfirmed")) {
			viewHolder.state.setText("待确认");
			viewHolder.money.setText(data.get(position).getCarPrice());
		} else if (status.equals("pendingpay")) {
			viewHolder.state.setText("待付款");
		} else if (status.equals("paying")) {
			viewHolder.state.setText("交易中");
		} else if (status.equals("completed")) {
			viewHolder.state.setText("交易完成");
		} else if (status.equals("cancelled")) {
			viewHolder.money.setText("0");
			viewHolder.state.setText("交易失败");
		} else {
			viewHolder.state.setText(status);
		}

		return convertView;
	}

	class ViewHolder {

		ImageView carImg;
		TextView name, orderday, ordertype, money, state,orderMin,orderNum;

	}

	public List<OrderList> getList() {
		return data;
	}

	public void setList(List<OrderList> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
