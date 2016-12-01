package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * 分期期限适配器
 * @author hunaixin
 *
 */
public class TermGridAdapter extends BaseAdapter {

	private Context mContext;
	private List<LoanAlgorithmList> data;
	private BaseApplication application;
	private Handler mHandler;

	public TermGridAdapter(Context mContext, List<LoanAlgorithmList> data) {
		this.mContext = mContext;
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

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_program_gridview, null);
			viewHolder = new ViewHolder();
			viewHolder.program = (TextView) convertView.findViewById(R.id.program);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.program.setText(data.get(position).getLoanTerm());
		return convertView;
	}

	class ViewHolder {
		TextView program;
	}

	public void setData(List<LoanAlgorithmList> data) {
		this.data = data;
		notifyDataSetChanged();
	}
	 
	
}
