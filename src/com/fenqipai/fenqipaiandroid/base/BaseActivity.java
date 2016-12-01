package com.fenqipai.fenqipaiandroid.base;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.view.RotateLoading;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 所有Activity的基类
 * 
 * @name BaseActivity
 * @author liuchengbao
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
@SuppressLint("NewApi")
public class BaseActivity extends FragmentActivity {

	// activity共用application
	protected BaseApplication application;
	private Dialog loadingDialog = null;
	private TextView tipTextView;
	private RotateLoading newtonCradleLoading = null;

	public TextView emptyView;

	// 所有的异步任务
	protected List<AsyncTask<Void, Void, Boolean>> myAsyncTasks = new ArrayList<AsyncTask<Void, Void, Boolean>>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		application = (BaseApplication) getApplicationContext();

		createEmptyView();
		
		AppManager.getAppManager().addActivity(this);
		
	}

	private void createEmptyView() {
		emptyView = new TextView(this);

		emptyView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));

		emptyView.setText("暂无数据");

		emptyView.setGravity(Gravity.CENTER_HORIZONTAL);

		emptyView.setTextColor(getResources().getColor(R.color.hint_color));
	}
	
	/**
	 * 添加异步任务到数组中
	 * 
	 * @param asyncTask
	 */
	public void putAsyncTask(AsyncTask<Void, Void, Boolean> asyncTask) {
		myAsyncTasks.add(asyncTask.execute());
	}

	/**
	 * 通过Class跳转界面
	 */
	protected void startActivity(Class<?> cls) {

		startActivity(cls, null);
	}

	/**
	 * 含有Bundle通过Class跳转界面
	 */
	protected void startActivity(Class<?> cls, Bundle bundle) {
		Intent intent = new Intent();
																																																																															
		intent.setClass(this, cls);

		if (bundle != null) {
			intent.putExtras(bundle);
		}

		startActivity(intent);
	}

	/**
	 * 清除所有的异步任务
	 */
	protected void clearAsyncTask() {
		Iterator<AsyncTask<Void, Void, Boolean>> iterator = myAsyncTasks.iterator();

		while (iterator.hasNext()) {
			AsyncTask<Void, Void, Boolean> asyncTask = iterator.next();

			if (asyncTask != null && !asyncTask.isCancelled()) {
				asyncTask.cancel(true);
			}
		}

		myAsyncTasks.clear();
	}
	

	/**
	 * 创建等待dialog
	 * 
	 * @title createLoadingDialog
	 * @author zhaoqingyang
	 * @Description TODO
	 */
	public void createLoadingDialog(Activity activity) {

		while (activity.getParent() != null) {
			activity = activity.getParent();
		}

		LayoutInflater inflater = LayoutInflater.from(activity);
		View v = inflater.inflate(R.layout.loading_dialog, null);// 得到加载view
		RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view);// 加载布局
		newtonCradleLoading = (RotateLoading) v.findViewById(R.id.newton_cradle_loading);
		newtonCradleLoading.start();
		tipTextView = (TextView) v.findViewById(R.id.tipTextView);// 提示文字

		loadingDialog = new Dialog(activity, R.style.loading_dialog);// 创建自定义样式dialog

		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT,
				LinearLayout.LayoutParams.FILL_PARENT));// 设置布局

	}
	

	/**
	 * 显示等待dialog
	 * 
	 * @title loadingDialogShow
	 * @author zhaoqingyang
	 * @Description TODO
	 */
	public void loadingDialogShow(String msg) {
		if (loadingDialog != null && newtonCradleLoading != null) {
			tipTextView.setText(msg);
			newtonCradleLoading.start();
			loadingDialog.show();
		}
	}

	/**
	 * 显示等待dialog
	 * 
	 * @title loadingDialogShow
	 * @author zhaoqingyang
	 * @Description TODO
	 */
	public void loadingDialogShow(int res) {
		if (loadingDialog != null && newtonCradleLoading != null) {
			tipTextView.setText(res);
			newtonCradleLoading.start();
			loadingDialog.show();
		}
	}

	/**
	 * 摧毁等待dialog
	 * 
	 * @title loadingDialogDismiss
	 * @author zhaoqingyang
	 */
	public void loadingDialogDismiss() {
		if (loadingDialog != null && newtonCradleLoading != null) {
			newtonCradleLoading.stop();
			loadingDialog.dismiss();
		}
	}

	@Override
	protected void onDestroy() {

		super.onDestroy();

		clearAsyncTask();

	}

}
