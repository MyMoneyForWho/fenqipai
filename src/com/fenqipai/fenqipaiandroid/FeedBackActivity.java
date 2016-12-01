package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.sms.SendSMS;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;
import com.fenqipai.fenqipaiandroid.view.SettingsItemView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * @Description:意见反馈界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:31:28
 */
public class FeedBackActivity extends BaseActivity {
	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 确认提交
	@ViewInject(R.id.btnRegister)
	private Button btnuptrue;
	
	// 反馈内容
	@ViewInject(R.id.feedback_text)
	private EditText feedbackText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_feed_back);
		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();
	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initView() {
		application = (BaseApplication) getApplication();

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText("意见反馈");

		
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initEvent() {

		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnuptrue.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.isEmpty(feedbackText.getText().toString())) {
					ToastUtils.show(FeedBackActivity.this, "反馈内容不得为空", ToastUtils.TOAST_SHORT);
				}else {
					ToastUtils.show(FeedBackActivity.this, "提交成功", ToastUtils.TOAST_SHORT);
					finish();
				}
			}
		});
		
	}
}