package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.BankList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 添加银行卡listview适配器
 * 
 * @name BankCardActivityAdapter
 * @author hunaixin
 */
public class AddBankCardAdapter extends BaseAdapter {
	private Context mContext;
	private List<BankList> list;
	private BaseApplication application;
	private Boolean flag = false;

	public AddBankCardAdapter(Context context, List<BankList> list) {
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

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_bank_listview, null);
			viewHolder = new ViewHolder();
			viewHolder.bankName = (TextView) convertView.findViewById(R.id.item_bank_name);
			viewHolder.bankImg = (ImageView) convertView.findViewById(R.id.item_bank_img);
			viewHolder.chooseImg = (ImageView) convertView.findViewById(R.id.item_bank_choose);
			viewHolder.bankLayout = (LinearLayout) convertView.findViewById(R.id.bank_LinearLayout);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bankName.setText(list.get(position).getName());
		// 判读是否选择
		if (list.get(position).getIsChoose()) {
			viewHolder.chooseImg.setImageResource(R.drawable.btn_choose_on);
		} else {
			viewHolder.chooseImg.setImageResource(R.drawable.btn_choose_off);
		}

		return convertView;
	}

	public class ViewHolder {
		TextView bankName;
		ImageView bankImg, chooseImg;
		LinearLayout bankLayout;
	}

	public List<BankList> getList() {
		return list;
	}

	public void setList(List<BankList> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}
