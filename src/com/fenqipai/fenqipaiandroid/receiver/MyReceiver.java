package com.fenqipai.fenqipaiandroid.receiver;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * 自定义接收器
 * 
 * 如果不定义这个 Receiver，则： 1) 默认用户会打开主界面 2) 接收不到自定义消息
 */
public class MyReceiver extends BroadcastReceiver {

	private BaseApplication application;

	private static final String TAG = "JPush";

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		application = (BaseApplication) context.getApplicationContext();
	}

	// 打印所有的 intent extra 数据

}
