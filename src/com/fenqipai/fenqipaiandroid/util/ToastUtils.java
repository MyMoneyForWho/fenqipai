package com.fenqipai.fenqipaiandroid.util;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.common.DrawerToast;

import android.content.Context;
import android.graphics.Color;

/**
 * toast工具类
 * 
 * @name ToastUtils
 * @author zhaoqingyang
 * @date 2015年11月3日
 * @modify
 * @modifyDate 2015年11月3日
 * @modifyContent
 */
public class ToastUtils {

	public static final int TOAST_SHORT = 2000;

	public static final int TOAST_LONG = 3000;

	private ToastUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("ToastUtils cannot be instantiated");
	}

	/**
	 * 显示
	 * 
	 * @title show
	 * @author zhaoqingyang
	 * @param context
	 * @param duration
	 */
	public static void show(Context context, String msg, int duration) {
		DrawerToast toast = DrawerToast.getInstance(context);
		toast.setDefaultBackgroundResource(R.drawable.cave);
		toast.setDefaultTextColor(Color.WHITE);
		toast.show(msg, duration);
	}

	/**
	 * 显示
	 * 
	 * @title show
	 * @author zhaoqingyang
	 * @param context
	 * @param duration
	 */
	public static void show(Context context, int res, int duration) {
		DrawerToast toast = DrawerToast.getInstance(context);
		toast.setDefaultBackgroundResource(R.drawable.cave);
		toast.setDefaultTextColor(Color.WHITE);
		toast.show(context.getResources().getString(res), duration);
	}

	/**
	 * 显示
	 * 
	 * @title show
	 * @author zhaoqingyang
	 * @param context
	 * @param duration
	 */
	public static void show(Context context, String msg, int color, int duration) {
		DrawerToast toast = DrawerToast.getInstance(context);
		toast.setDefaultBackgroundResource(R.drawable.cave);
		toast.setDefaultTextColor(color);
		toast.show(msg, duration);
	}

}
