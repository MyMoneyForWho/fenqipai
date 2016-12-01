package com.fenqipai.fenqipaiandroid.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @ClassName: CommonAdapter<T>
 * @Description: 通用适配器
 * @author qianyuhang
 * @date 2016-6-24 下午1:33:28
 */
public abstract class CommonAdapter<T> extends BaseAdapter {

	/**
	 * @Fields 
	 */ 
	protected Context mContext;

	/* item����id */
	protected int mItemId;

	/* �洢ListView���Bean��List. */
	protected List<T> mData;

	LayoutInflater mLayoutInflater;

	public CommonAdapter(Context context, int itemId, List<T> mData) {
		this.mContext = context;
		this.mData = mData;
		this.mItemId = itemId;
		mLayoutInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mData.size();
	}

	@Override
	public T getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = ViewHolder.getInstance(mContext, convertView,
				parent, mItemId, position);
		convert(holder, getItem(position));
		return holder.getConvertView();
	}

	public abstract void convert(ViewHolder holder, T t);
}
