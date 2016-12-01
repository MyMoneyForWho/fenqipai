package com.fenqipai.fenqipaiandroid.service;

import java.io.File;
import java.net.URLEncoder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.common.DownloadUtils;
import com.fenqipai.fenqipaiandroid.net.URL;

/**
 * 系统更新Service
 * 
 * @name UpdateService
 * @author liuchengbao
 * @Description TODO
 * @date 2015年12月17日
 * @modify
 * @modifyDate 2015年12月17日
 * @modifyContent
 */
public class UpdateService extends Service {

	public static final int APP_VERSION_LATEST = 0;
	public static final int APP_VERSION_OLDER = 1;

	public static final int mNotificationId = 100;
	private String mDownloadUrl = "http://" + URL.URL_HOST + ":9000/public/apk/MeiTanAndroid.apk";
	private NotificationManager mNotificationManager = null;
	private Notification mNotification = null;
	private PendingIntent mPendingIntent = null;

	private File destDir = null;
	private File destFile = null;

	private static final int DOWNLOAD_FAIL = -1;
	private static final int DOWNLOAD_SUCCESS = 0;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_SUCCESS:
				Toast.makeText(getApplicationContext(), "新版本下载成功,开始安装.", Toast.LENGTH_LONG).show();
				install(destFile);
				break;
			case DOWNLOAD_FAIL:
				Toast.makeText(getApplicationContext(), "新版本下载失败，请稍后再试.", Toast.LENGTH_LONG).show();
				mNotificationManager.cancel(mNotificationId);
				break;
			default:
				break;
			}
		}

	};
	private DownloadUtils.DownloadListener downloadListener = new DownloadUtils.DownloadListener() {
		@Override
		public void downloading(int progress) {
			mNotification.contentView.setProgressBar(R.id.app_upgrade_progressbar, 100, progress, false);
			mNotification.contentView.setTextViewText(R.id.app_upgrade_progresstext, progress + "%");
			mNotificationManager.notify(mNotificationId, mNotification);
		}

		@Override
		public void downloaded() {
			mNotification.contentView.setViewVisibility(R.id.app_upgrade_progressblock, View.GONE);
			mNotification.defaults = Notification.DEFAULT_SOUND;
			mNotification.contentIntent = mPendingIntent;
			mNotification.contentView.setTextViewText(R.id.app_upgrade_progresstext, "下载完成。");
			mNotificationManager.notify(mNotificationId, mNotification);
			if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
				Message msg = mHandler.obtainMessage();
				msg.what = DOWNLOAD_SUCCESS;
				mHandler.sendMessage(msg);
			}
			mNotificationManager.cancel(mNotificationId);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			destDir = new File(Environment.getExternalStorageDirectory().getPath() + Contants.DOWN_APP);
			if (destDir.exists()) {
				File destFile = new File(destDir.getPath() + "/" + URLEncoder.encode(mDownloadUrl));
				if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
					destFile.delete();
					destDir.mkdirs();
				}
			}
		}

		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		mNotification = new Notification();

		mNotification.contentView = new RemoteViews(getApplication().getPackageName(),
				R.layout.app_upgrade_notification);

		Intent completingIntent = new Intent();
		completingIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		completingIntent.setClass(getApplication().getApplicationContext(), UpdateService.class);

		mPendingIntent = PendingIntent.getActivity(UpdateService.this, R.string.app_name, completingIntent,
				PendingIntent.FLAG_UPDATE_CURRENT);

		mNotification.icon = R.drawable.logo;
		mNotification.tickerText = "开始下载";
		mNotification.contentIntent = mPendingIntent;
		mNotification.contentView.setProgressBar(R.id.app_upgrade_progressbar, 100, 0, false);
		mNotification.contentView.setTextViewText(R.id.app_upgrade_progresstext, "0%");
		mNotificationManager.cancel(mNotificationId);
		mNotificationManager.notify(mNotificationId, mNotification);
		new AppUpgradeThread().start();
		return super.onStartCommand(intent, flags, startId);
	}

	class AppUpgradeThread extends Thread {

		@Override
		public void run() {
			if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
				if (destDir == null) {
					destDir = new File(Environment.getExternalStorageDirectory().getPath() + Contants.DOWN_APP);
				}
				if (destDir.exists() || destDir.mkdirs()) {
					destFile = new File(destDir.getPath() + "/" + URLEncoder.encode(mDownloadUrl));
					if (destFile.exists() && destFile.isFile() && checkApkFile(destFile.getPath())) {
						install(destFile);
					} else {
						try {
							DownloadUtils.download(mDownloadUrl, destFile, false, downloadListener);
						} catch (Exception e) {
							Message msg = mHandler.obtainMessage();
							msg.what = DOWNLOAD_FAIL;
							mHandler.sendMessage(msg);
							e.printStackTrace();
						}
					}
				}
			}
			stopSelf();
		}
	}

	public boolean checkApkFile(String apkFilePath) {
		boolean result = false;
		try {
			PackageManager pManager = getPackageManager();
			PackageInfo pInfo = pManager.getPackageArchiveInfo(apkFilePath, PackageManager.GET_ACTIVITIES);
			if (pInfo == null) {
				result = false;
			} else {
				result = true;
			}
		} catch (Exception e) {
			result = false;
			e.printStackTrace();
		}
		return result;
	}

	public void install(File apkFile) {
		Uri uri = Uri.fromFile(apkFile);
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setDataAndType(uri, "application/vnd.android.package-archive");
		startActivity(intent);
	}
}
