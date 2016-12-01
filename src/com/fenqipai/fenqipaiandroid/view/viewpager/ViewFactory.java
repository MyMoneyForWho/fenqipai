package com.fenqipai.fenqipaiandroid.view.viewpager;


import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.model.Notice;
import com.fenqipai.fenqipaiandroid.net.URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * ImageView创建工厂
 */
public class ViewFactory {

	/**
	 * 获取ImageView视图的同时加载显示url
	 * 
	 * @param application
	 * 
	 * @param text
	 * @return
	 */
	public static View getImageView(Context context, BaseApplication application, String str) {
		View view = LayoutInflater.from(context).inflate(R.layout.view_banner, null);
		String url = URL.getURL( URL.FILE_UPLOAD) + str;
		ImageView imageView = (ImageView) view.findViewById(R.id.banner_img);
		TextView textView = (TextView) view.findViewById(R.id.banner_txt);
		application.imageLoader.displayImage(url, imageView,
				application.options);
		textView.setText("新车集结 8月买车送豪礼");
		return view;
	}
}
