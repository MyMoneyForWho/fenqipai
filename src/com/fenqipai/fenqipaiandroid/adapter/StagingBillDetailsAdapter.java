package com.fenqipai.fenqipaiandroid.adapter;

import java.util.HashMap;
import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.StagingBillDetails;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * 车辆订单列表适配器
 * 
 * @author hunaixin
 */
public class StagingBillDetailsAdapter extends BaseAdapter {
	private Context mContext;
	private List<StagingBillDetails> data;
	private BaseApplication application;
	// 用来控制CheckBox的选中状况  
    private static HashMap<Integer,Boolean> isSelected;  

	public StagingBillDetailsAdapter(Context context, List<StagingBillDetails> data) {
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

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stagingbill_details_list, null);
			viewHolder = new ViewHolder();
            viewHolder.date = (TextView) convertView.findViewById(R.id.repaymentDate);
            viewHolder.periods = (TextView) convertView.findViewById(R.id.periods);
            viewHolder.price = (TextView) convertView.findViewById(R.id.perios_price);
            viewHolder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
        viewHolder.date.setText(TimeUtils.convertDateTime(data.get(position).getLastDate()));
        viewHolder.periods.setText("第" + data.get(position).getPeriods() + "/" + data.get(position).getOrderRepSize() + "期");
        viewHolder.price.setText(data.get(position).getRepaymentPrice());
        // 根据isSelected来设置checkbox的选中状况  
        viewHolder.checkBox.setChecked(data.get(position).getIsChecked());
        
		return convertView;
	}

	public class ViewHolder {

		TextView date,periods,price;
		public CheckBox checkBox;

	}

	public List<StagingBillDetails> getList() {
		return data;
	}

	public void setList(List<StagingBillDetails> list) {
		this.data = list;
		notifyDataSetChanged();
	}
	
  
}
