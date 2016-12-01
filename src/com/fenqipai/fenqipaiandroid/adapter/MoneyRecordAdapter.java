package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.Moneyrecord;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

/**
 * 车辆订单列表适配器
 * 
 * @author hunaixin
 */
public class MoneyRecordAdapter extends BaseAdapter {
	private Context mContext;
	private List<Moneyrecord> data;
	private BaseApplication application;

	public MoneyRecordAdapter(Context context, List<Moneyrecord> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_money_record_list, null);
			viewHolder = new ViewHolder();
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			viewHolder.instructions = (TextView) convertView.findViewById(R.id.instructions);
			viewHolder.money = (TextView) convertView.findViewById(R.id.money);
			viewHolder.timeMin = (TextView) convertView.findViewById(R.id.time_min);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.time.setText(TimeUtils.convertTimeToDate(data.get(position).getDateTimestamp()));
		viewHolder.timeMin.setText(TimeUtils.convertTimeToMin(data.get(position).getDateTimestamp()));
		viewHolder.instructions.setText(data.get(position).getTypeString());
		viewHolder.money.setText(data.get(position).getPrice());
		return convertView;
	}

	class ViewHolder {

		TextView time, instructions, money,timeMin;

	}

	public List<Moneyrecord> getList() {
		return data;
	}

	public void setList(List<Moneyrecord> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
