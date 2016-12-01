package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.BankCardList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 银行卡listview适配器
 * 
 * @name BankCardAdapter
 * @author hunaixin
 */
public class BankCardAdapter extends BaseAdapter {
	private Context mContext;
	private List<BankCardList> list;
	private BaseApplication application;

	public BankCardAdapter(Context context, List<BankCardList> list) {
		this.mContext = context;
		this.list = list;
		this.application = (BaseApplication) mContext.getApplicationContext();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		 ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_withdraw_bank_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.bankName = (TextView) convertView.findViewById(R.id.item_bank_name);
			viewHolder.num = (TextView) convertView.findViewById(R.id.item_bank_num);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bankName.setText(list.get(position).getBankName());
		viewHolder.num.setText("尾号"+list.get(position).getNo());
		return convertView;
	}

	public class ViewHolder {
		TextView bankName,num;
	}

	public List<BankCardList> getList() {
		return list;
	}

	public void setList(List<BankCardList> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}
