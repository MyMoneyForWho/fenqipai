package com.fenqipai.fenqipaiandroid;

import java.io.File;

import com.fenqipai.fenqipaiandroid.base.AppManager;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.FileUtil;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.util.WebViewUtils;
import com.fenqipai.fenqipaiandroid.view.Effectstype;
import com.fenqipai.fenqipaiandroid.view.ExitDialogBuilder;
import com.fenqipai.fenqipaiandroid.view.SettingsItemView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SettingsActivity extends BaseActivity {
	// 接收服务器返回值
	private String registerMessage = "";

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 推送信息
	@ViewInject(R.id.Information_push)
	private SettingsItemView llInformation;

	// 清理缓存
	@ViewInject(R.id.clean_cache)
	private SettingsItemView llClean;

	// 常见问题
	@ViewInject(R.id.common_problem)
	private SettingsItemView llProblem;

	// 联系客服
	@ViewInject(R.id.contact_service)
	private SettingsItemView llContact;

	// 意见反馈
	@ViewInject(R.id.opinion_feedback)
	private SettingsItemView llOpinion;

	// 在线升级
	@ViewInject(R.id.update_online)
	private SettingsItemView llUpdate;

	// 关于分期拍
	@ViewInject(R.id.about_us)
	private SettingsItemView llAboutUs;

	// 退出登录
	@ViewInject(R.id.logout)
	private Button btnLogout;
	
	public Context mContext;
	
	// 计算缓存大小
	private long fileSize = 0;

	private String cacheSize = "0KB";

	private boolean isClear = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		// 注入view和事件
		ViewUtils.inject(this);
		mContext=SettingsActivity.this ;
		
		
		initData();
		
		initView();

		initEvent();
	}

	public void initView() {
		application = (BaseApplication) getApplication();

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText("设置");

		llInformation.setCommonsettings(View.VISIBLE, View.GONE, View.GONE, View.GONE);
		llInformation.setTextLeft(R.string.settings_Information);

		llClean.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llClean.setTextLeft(R.string.settings_Clean);
		llClean.setTextRight(cacheSize);

		llProblem.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llProblem.setTextLeft(R.string.settings_Problem);
		llProblem.setBtnRight(R.drawable.details);

		llContact.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llContact.setTextLeft(R.string.settings_Contact);
		llContact.setTextRight("400-838-6777");
		llContact.setBtnRight(R.drawable.details);

		llOpinion.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llOpinion.setTextLeft(R.string.settings_Opinion);
		llOpinion.setBtnRight(R.drawable.details);

		llUpdate.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llUpdate.setTextLeft(R.string.settings_Update);
		llUpdate.setBtnRight(R.drawable.details);

		llAboutUs.setCommonsettings(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE);
		llAboutUs.setTextLeft(R.string.settings_about);
		llAboutUs.setBtnRight(R.drawable.details);
	}

	private void initData() {
		File filesDir = getFilesDir();
		File cacheDir = getCacheDir();

		fileSize += FileUtil.getDirSize(filesDir);
		fileSize += FileUtil.getDirSize(cacheDir);

		if (fileSize > 0) {
			cacheSize = FileUtil.formatFileSize(fileSize);
		}
		
	}
	
	public void initEvent() {

		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//消息推送
		llInformation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


			}
		});
		//清除缓存
		llClean.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AlertDialog();

			}
		});
		//常见问题
		llProblem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {


			}
		});
		//联系客服
		llContact.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008386777"));// 跳转到拨号界面，同时传递电话号码
				startActivity(dialIntent);
			}
		});
		//意見反饋
		llOpinion.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(FeedBackActivity.class);
			}
		});
		//現在升級
		llUpdate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			ToastUtils.show(SettingsActivity.this, "当前已是最新版本", ToastUtils.TOAST_SHORT);

			}
		});
		//关于分期拍
		llAboutUs.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(AboutUsActivity.class);

//				ToastUtils.show(application, "消息通知", ToastUtils.TOAST_SHORT);

			}
		});
		//退出登录
		btnLogout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SPUtils.put(mContext, Contants.LOGIN_TYPE,0);
				SPUtils.remove(application, Contants.USER_ID);
				SPUtils.remove(application, Contants.USER_NAME);
				SPUtils.remove(application, Contants.USER_PORTRAIT);
				SPUtils.remove(application, Contants.USER_TOKEN);
				SPUtils.remove(application, Contants.USER_BALANCE);
				SPUtils.remove(application, Contants.USER_MOBILE);
				Intent intent=new Intent ();
				setResult(Contants.RETURN_MESSAGE, intent);
				finish();


				
			}
		});

	}
	
	/**
	 * 清除缓存dialog
	 * @author hunaixin
	 */
	private void AlertDialog() {

		final ExitDialogBuilder dialogBuilder = ExitDialogBuilder.getInstance(SettingsActivity.this);

		dialogBuilder.withTitle("确定清除缓存吗？").isCancelableOnTouchOutside(false).withDuration(ExitDialogBuilder.DEFAULT_DURATION)
				.withEffect(Effectstype.Fadein).withButton1Text("确定").withButton2Text("取消")
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (!isClear) {

							FileUtil.deleteFile(getFilesDir());
							FileUtil.deleteFile(getCacheDir());

							llClean.setTextRight("0KB");
							WebViewUtils.clearWebViewCache(getApplicationContext());
							application.imageLoader.clearMemoryCache();
							application.imageLoader.clearDiscCache();

							isClear = true;

						}
		                ToastUtils.show(SettingsActivity.this, "缓存清除完成！", ToastUtils.TOAST_SHORT);
		                dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();

	}
}