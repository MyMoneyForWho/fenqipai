package com.fenqipai.fenqipaiandroid.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.jar.Attributes.Name;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.adapter.StagingHotFragmentAdapter.ViewHolder;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.IndexNewsList;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.view.RoundedImageView;
import com.hp.hpl.sparta.Document.Index;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 首页新闻适配器
 * 
 * @name HomeFragmentAdapter
 * @author hunaixin
 */
public class HomeFragmentNewsAdapter extends BaseAdapter {
	private Context mContext;
	private List<IndexNewsList> data;
	private BaseApplication application;

	public HomeFragmentNewsAdapter(Context context, List<IndexNewsList> data) {
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
			viewHolder.img = (ImageView) convertView.findViewById(R.id.img);
			viewHolder.title = (TextView) convertView.findViewById(R.id.title);
			viewHolder.content = (TextView) convertView.findViewById(R.id.content);
			viewHolder.time = (TextView) convertView.findViewById(R.id.time);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

//		String s = "";  
//		Random ran =new Random(System.currentTimeMillis());  
//		for (int j = 0; j < 10; j++) {
//			s = s + ran.nextInt(100);
//		}
//		 + "?v=" + s
		application.imageLoader.displayImage(
				URL.getURL( URL.FILE_UPLOAD) + data.get(position).getImagePath(), viewHolder.img,
				application.options);
		viewHolder.title.setText(data.get(position).getTitle());
		viewHolder.content.setText(data.get(position).getBody());
		viewHolder.time.setText(data.get(position).getTime());
		return convertView;
	}

	class ViewHolder {
		ImageView img;
		TextView title;
		TextView content;
		TextView time;
	}

	public List<IndexNewsList> getList() {
		return data;
	}

	public void setList(List<IndexNewsList> list) {
		this.data = list;
		notifyDataSetChanged();
	}

}
