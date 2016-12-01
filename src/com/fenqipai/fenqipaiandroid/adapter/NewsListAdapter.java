package com.fenqipai.fenqipaiandroid.adapter;

import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.NewsList;
import com.fenqipai.fenqipaiandroid.net.URL;

import android.content.Context;
import android.text.Html;
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
public class NewsListAdapter extends BaseAdapter {
	private Context mContext;
	private List<NewsList> data;
	private BaseApplication application;

	public NewsListAdapter(Context context, List<NewsList> data) {
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_homefragment_news, null);
			viewHolder = new ViewHolder();
			viewHolder.carImg = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.body = (TextView) convertView.findViewById(R.id.content);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		application.imageLoader.displayImage(URL.getURL( URL.FILE_UPLOAD) + data.get(position).getImagePath(), viewHolder.carImg,application.options);
		viewHolder.title.setText(data.get(position).getTitle());
		viewHolder.body.setText(Html.fromHtml(data.get(position).getBody()));
		return convertView;
	}

	class ViewHolder {
		ImageView carImg;
		TextView title,body;
	}

	public List<NewsList> getList() {
		return data;
	}

	public void setList(List<NewsList> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
