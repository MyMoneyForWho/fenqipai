package com.fenqipai.fenqipaiandroid.sms;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;

import android.app.Activity;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;

/**
 * 发送验证码类
 * 
 * @name SendSMS
 * @author liuchengbao
 * @date 2015年11月10日
 * @modify
 * @modifyDate 2015年11月10日
 * @modifyContent
 */
public class SendSMS {

	private static final int RETRY_INTERVAL = 60;

	// 鍊掕鏃�
	private static int time = RETRY_INTERVAL;

	private static Button button;

	private static String Message;
	
	private static Handler handlers;
	/**
	 * 发送验证码
	 * 
	 * @title sendSMS
	 * @author liuchengbao
	 * @param activity
	 * @param phone
	 * @param application
	 */
	public static void sendSMS(Activity activity, BaseApplication application, 
			Button btn,Handler handles, String phone,String captchaCode ,String actionType) {
		if (NetUtils.isConnected(activity)) {
			if (StringUtils.isEmpty(phone)) {
				ToastUtils.show(activity, "请输入手机号", ToastUtils.TOAST_SHORT);
				return;
			}
			handlers=handles;
			// 1. 通过规则判断手机号
			if (!StringUtils.judgePhoneNums(phone)) {
				ToastUtils.show(activity, "请输入正确手机号码", ToastUtils.TOAST_SHORT);
				return;
			}

			// 2.发送验证码
			sendToSMS(activity, application, phone,captchaCode,actionType);

			// 3. 把按钮变成不可点击，并且显示倒计时（正在获取
			button = btn;
			button.setClickable(false);
			button.setTextColor(Color.parseColor("#999999"));
			button.setText("重新发送(" + time-- + ")");
			new Thread(new Runnable() {
				@Override
				public void run() {
					for (int i = 60; i > 0; i--) {
						handler.sendEmptyMessage(-9);
						if (i <= 0) {
							break;
						}
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					handler.sendEmptyMessage(-8);
				}
			}).start();
		} else {
			ToastUtils.show(activity, R.string.no_internet, ToastUtils.TOAST_LONG);
		}
	}

	static Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what == -9) {
				button.setText("重新发送(" + time-- + ")");
			}
			if (msg.what == -8) {
				time = RETRY_INTERVAL;
				button.setClickable(true);
				button.setText("重新发送");
				button.setTextColor(Color.parseColor("#e45614"));
			}
		}
	};

	/**
	 * 发送验证码
	 * 
	 * @title sendSMS
	 * @author liuchengbao
	 * @param activity
	 * @param application
	 * @param phone
	 */
	protected static void sendToSMS(final Activity activity, final BaseApplication application, 
			final String phone,final String captchaCode ,final String actionType) {
		((BaseActivity) activity).putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				Message = application.sendSMS(phone,captchaCode,actionType);
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				Message msg=new Message();
				msg.obj=Message;
				handlers.sendMessage(msg);
			}
		});
	}

}
