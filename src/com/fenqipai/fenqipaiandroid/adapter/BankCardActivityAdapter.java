package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * 银行卡listview适配器
 * 
 * @name BankCardActivityAdapter
 * @author hunaixin
 */
public class BankCardActivityAdapter extends BaseAdapter {
	private Context mContext;
	private List<BankCardList> list;
	private BaseApplication application;
	private Handler mHandler;

	public BankCardActivityAdapter(Context context,List<BankCardList> list,Handler mHandler) {
		this.mContext = context;
		this.list = list;
		this.mHandler = mHandler;
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_add_bank_card, null);
			viewHolder = new ViewHolder();
			viewHolder.bankName = (TextView) convertView.findViewById(R.id.item_bank_name);
			viewHolder.bankCardType = (TextView) convertView.findViewById(R.id.item_bank_card_type);
			viewHolder.bankCardNum = (TextView) convertView.findViewById(R.id.item_bank_card_num);
			viewHolder.delImg  = (ImageView) convertView.findViewById(R.id.item_bank_card_del);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		viewHolder.bankName.setText(list.get(position).getBankName());
		viewHolder.bankCardNum.setText(list.get(position).getNo());
		viewHolder.delImg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = 1;
				message.arg1 = position;
				message.obj = v;
				
				mHandler.sendMessage(message);
			}
		});
		return convertView;
	}

	class ViewHolder {
		TextView bankName, bankCardType, bankCardNum;
		ImageView delImg;
	}
	
	/**
	 * 卡号以**** **** **** 1111形式显示
	 * 
	 * @author hunaixin
	 */
	private String showHintCardNum(String num) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(num);
		if (isNum.matches()) {
			num = num.substring(num.length() - 4, num.length());
		} else {
			
		}

		return num;
	}
	

	public List<BankCardList> getList() {
		return list;
	}

	public void setList(List<BankCardList> list) {
		this.list = list;
		notifyDataSetChanged();
	}

}
