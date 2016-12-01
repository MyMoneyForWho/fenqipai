package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.StagingBill;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 分期账单列表适配器
 * 
 * @author hunaixin
 */
public class StagingBillListAdapter extends BaseAdapter {
	private Context mContext;
	private List<StagingBill> data;
	private BaseApplication application;

	public StagingBillListAdapter(Context context, List<StagingBill> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stagingbill_list, null);
			viewHolder = new ViewHolder();
			viewHolder.orderNum = (TextView) convertView.findViewById(R.id.order_num);
			viewHolder.repaymentDate = (TextView) convertView.findViewById(R.id.repaymentDate);
			viewHolder.repaymentType = (TextView) convertView.findViewById(R.id.repaymentType);
			viewHolder.currentPrice = (TextView) convertView.findViewById(R.id.currentPrice);
			viewHolder.hasPayPrice = (TextView) convertView.findViewById(R.id.hasPayPrice);
			viewHolder.state = (TextView) convertView.findViewById(R.id.state);
			viewHolder.carName = (TextView) convertView.findViewById(R.id.car_name);
			viewHolder.totalPeriods = (TextView) convertView.findViewById(R.id.total_periods);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.orderNum.setText(data.get(position).getOrderNumber());
		viewHolder.repaymentDate.setText(data.get(position).getRepaymentDate());
		viewHolder.repaymentType.setText("已还至" + data.get(position).getCurrentPeriods() + "期");
		viewHolder.totalPeriods.setText("共" + data.get(position).getTotalPeriods() + "期");
		viewHolder.currentPrice.setText(data.get(position).getCurrentPrice());
		viewHolder.hasPayPrice.setText(data.get(position).getHasPayPrice());
		viewHolder.state.setText(data.get(position).getOrderRepaymentStatusString());
		viewHolder.carName.setText(data.get(position).getCarAllName());
		return convertView;
	}

	class ViewHolder {

		TextView orderNum, repaymentDate, repaymentType, currentPrice,totalPeriods, hasPayPrice, state,carName;

	}

	public List<StagingBill> getList() {
		return data;
	}

	public void setList(List<StagingBill> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
