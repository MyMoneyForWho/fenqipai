package com.fenqipai.fenqipaiandroid.util;

import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * 消息处理公共类
 * 
 * @name MsgUtils
 * @author zhaoqingyang
 * @date 2015年11月10日
 * @modify
 * @modifyDate 2015年11月10日
 * @modifyContent
 */
public class MsgUtils {

	private static String Type="chatMsg";
	private MsgUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("MsgUtils cannot be instantiated");
	}

	/**
	 * 
	 * @title handleMsg
	 * @author zhaoqingyang
	 * @param application
	 * @Description TODO
	 * @param msg
	 */
	public static void handleMsg(Context context, BaseApplication application, String msg) {
		NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		Intent intent=new Intent(context,MainActivity.class);
		
		
//		ComponentName comp = new ComponentName(context.getPackageName(),
//				"MessageActivity");
//		intent.setComponent(comp);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

		PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		// 需要注意build()是在API level 16及之后增加的，在API11中可以使用getNotificatin()来代替
		Notification notify = new Notification.Builder(context).setSmallIcon(R.drawable.logo).setTicker("推送来了")
				.setContentTitle("极光推送").setContentText(msg).setContentIntent(pendingIntent).getNotification();
		notify.flags |= Notification.FLAG_AUTO_CANCEL;
		notify.defaults = Notification.DEFAULT_ALL;

		// 执行通知
		manager.notify(1, notify);
	

	}

	
	

}
