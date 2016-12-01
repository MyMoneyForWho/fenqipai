package com.fenqipai.fenqipaiandroid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.UserInfo;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description:修改密码界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:11:58
 */
public class ChangeLoginPasswordActivity extends BaseActivity {
	// 接收服务器返回值
	// private String registerMessage = "";

	public Context mContext;

	// 原始密码
	@ViewInject(R.id.primary_password)
	private ContainsEmojiEditText etPrimaryPassword;

	// 标题栏
	@ViewInject(R.id.forgot_titleBar)
	private TitleBarView titleView;

	// 新密码
	@ViewInject(R.id.new_password)
	private ContainsEmojiEditText etPassword;

	// 确认新密码
	@ViewInject(R.id.conf_password)
	private ContainsEmojiEditText confirmPassword;
	// 确认提交
	@ViewInject(R.id.common)
	private Button btnRegister;

	// 忘记密码
	@ViewInject(R.id.forgot_password)
	private Button tvForgotPassword;

	// 致电我们
	@ViewInject(R.id.call_to_us)
	private LinearLayout callMe;

	// 标题
	@ViewInject(R.id.title)
	private TextView title;

	// 原始密码linearlayout
	@ViewInject(R.id.primary_password_linearlayout)
	private LinearLayout primaryLinearlayout;

	// 原密码line
	@ViewInject(R.id.primary_password_view)
	private View primaryView;

	// 密码类型
	private String passwordType;

	// // 原密码
	// private String oldPassword;
	//
	// // 新密码
	// private String newPassword;

	// 返回值
	private String message;

	// 是否有提现密码
	private int isPayPassword = 0;
	
	// 是否有登录密码
	private int isPassword = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_login_password);

		Intent intent = getIntent();

		passwordType = intent.getStringExtra("passwordType");
		
		isPassword = (Integer) SPUtils.get(ChangeLoginPasswordActivity.this, Contants.USER_PASSWORD, 0);
		
		isPayPassword = (Integer) SPUtils.get(ChangeLoginPasswordActivity.this, Contants.USER_PAYPASSWORD, 0);

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
		titleView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleView.setBtnLeft(R.drawable.btn_back);
		titleView.setTitleText(R.string.change_the_password);

		if (passwordType.equals("payPassword")) {
			title.setText(R.string.pay_password);

			if (isPayPassword == 0) {
				primaryLinearlayout.setVisibility(View.GONE);
				primaryView.setVisibility(View.GONE);
			} else if (isPayPassword == 1) {
				primaryLinearlayout.setVisibility(View.VISIBLE);
				primaryView.setVisibility(View.VISIBLE);
			}
		} else if (passwordType.equals("password")) {
			title.setText(R.string.login_password);
			
			if (isPassword == 0) {
				primaryLinearlayout.setVisibility(View.GONE);
				primaryView.setVisibility(View.GONE);
			} else if (isPassword == 1) {
				primaryLinearlayout.setVisibility(View.VISIBLE);
				primaryView.setVisibility(View.VISIBLE);
			}
		}


		etPrimaryPassword.setOnFocusChangeListener(myListener);
		etPassword.setOnFocusChangeListener(myListener);
		confirmPassword.setOnFocusChangeListener(myListener);

	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initEvent() {
		titleView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideSoftInputView();
				
				if (passwordType.equals("payPassword")) {
					if (isPayPassword == 0) {
						if (TextUtils.isEmpty(etPassword.getText().toString().trim())
								&& TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码不能为空", ToastUtils.TOAST_SHORT);
						} else if (!isPassword(etPassword.getText().toString())) {
							ToastUtils.show(application, "密码由6到20位数字,字母组成", ToastUtils.TOAST_SHORT);
						} else if (!isPassword(confirmPassword.getText().toString())) {
							ToastUtils.show(application, "密码由6到20位数字,字母组成", ToastUtils.TOAST_SHORT);
						} else if (!etPassword.getText().toString().trim()
								.equals(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码输入不相同", ToastUtils.TOAST_SHORT);
						} else {
							updateNbuserPassword("",etPassword.getText().toString().trim(), passwordType);
						}
					} else if (isPayPassword == 1) {
						if (TextUtils.isEmpty(etPrimaryPassword.getText().toString().trim())) {
							ToastUtils.show(application, "请输入密码", ToastUtils.TOAST_SHORT);
						} else if (TextUtils.isEmpty(etPassword.getText().toString().trim())
								&& TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码不能为空", ToastUtils.TOAST_SHORT);
						} else if (!isPassword(etPassword.getText().toString())) {
							ToastUtils.show(application, "密码由6到20位数字,字母组成", ToastUtils.TOAST_SHORT);
						} else if (!isPassword(confirmPassword.getText().toString())) {
							ToastUtils.show(application, "密码由6到20位数字,字母组成", ToastUtils.TOAST_SHORT);
						} else if (!etPassword.getText().toString().trim()
								.equals(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码输入不相同", ToastUtils.TOAST_SHORT);
						} else {
							updateNbuserPassword(etPrimaryPassword.getText().toString().trim(),
									etPassword.getText().toString().trim(), passwordType);
						}
					}
				} else if (passwordType.equals("password")) {
					if (isPassword == 0) {
						if (TextUtils.isEmpty(etPassword.getText().toString().trim())
								&& TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码不能为空", ToastUtils.TOAST_SHORT);
						} else if (!StringUtils.isPassWord(etPassword.getText().toString())) {
							ToastUtils.show(application, "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						} else if (!StringUtils.isPassWord(confirmPassword.getText().toString())) {
							ToastUtils.show(application, "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						} else if (!etPassword.getText().toString().trim()
								.equals(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码输入不相同", ToastUtils.TOAST_SHORT);
						} else {
							updateNbuserPassword("",etPassword.getText().toString().trim(), passwordType);
						}
					} else if (isPassword == 1) {
						if (TextUtils.isEmpty(etPrimaryPassword.getText().toString().trim())) {
							ToastUtils.show(application, "请输入密码", ToastUtils.TOAST_SHORT);
						} else if (TextUtils.isEmpty(etPassword.getText().toString().trim())
								&& TextUtils.isEmpty(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码不能为空", ToastUtils.TOAST_SHORT);
						} else if (!StringUtils.isPassWord(etPassword.getText().toString())) {
							ToastUtils.show(application, "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						} else if (!StringUtils.isPassWord(confirmPassword.getText().toString())) {
							ToastUtils.show(application, "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						} else if (!etPassword.getText().toString().trim()
								.equals(confirmPassword.getText().toString().trim())) {
							ToastUtils.show(application, "密码输入不相同", ToastUtils.TOAST_SHORT);
						} else {
							updateNbuserPassword(etPrimaryPassword.getText().toString().trim(),
									etPassword.getText().toString().trim(), passwordType);
						}
					}
				}
	

			}
		});

		// 客服电话
		callMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));// 跳转到拨号界面，同时传递电话号码
				startActivity(dialIntent);

			}
		});

		// 忘记密码
		tvForgotPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (passwordType.equals("payPassword")) {
					Intent intent = new Intent(ChangeLoginPasswordActivity.this,ForgotPasswordActivity.class);
					intent.putExtra("passwordType", "payPassword");
					startActivity(intent);
				}else if(passwordType.equals("password")){
					Intent intent = new Intent(ChangeLoginPasswordActivity.this,ForgotPasswordActivity.class);
					intent.putExtra("passwordType", "password");
					startActivity(intent);
				}
				finish();
			}
		});

	}

    /**
	 * @Description:判断昵称输入密码是否正确
	 * @author hunaixin
	 * @parame password
	 * @return true/false
	 */
	public static boolean isPassword(String password) {
		String str = "^([a-zA-Z]|[0-9]){6,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 隐藏键盘
	 * 
	 * @title hideSoftInputView
	 * @author zhaoqingyang
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	/**
	 *@Description:修改密码 
	 *@param passwordType - 修改密码类型  password登录密码 | payPassword支付密码
	 *@return message
	 */
	protected void updateNbuserPassword(final String oldPassword, final String newPassword, final String passwordType) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuserPassword(passwordType, oldPassword, newPassword);

					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {
					try {
						JSONObject jObject = new JSONObject(message);
						if (application.getLoginTimeOut(application, jObject.optString("code"))) {
							ToastUtils.show(ChangeLoginPasswordActivity.this, jObject.optString("message"), 1000);
							getNbuserInfoResult();
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeLoginPasswordActivity.this, jObject.optString("message"), 1000);
						}
					} catch (JSONException e) {

						e.printStackTrace();
					}

				} else {
					ToastUtils.show(application, R.string.no_net, ToastUtils.TOAST_SHORT);
				}
			}

		});
	}

	OnFocusChangeListener myListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et_no);
			} else {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et);
			}
			v.setPadding(60, 0, 0, 0);
		}
	};

	
	
	public UserInfo usrInfo;
	/**
	 * @Description:用户信息接口
	 * @author hunaixin
	 * @parame 
	 * @return usrInfo
	 */
	public void getNbuserInfoResult() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				usrInfo = application.getNbuserInfoResult();

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				SPUtils.put(application, Contants.USER_PASSWORD, usrInfo.getIsPassword());
				
				SPUtils.put(application, Contants.USER_PAYPASSWORD, usrInfo.getIsPayPassword());

				super.onPostExecute(result);
			}
		});
	}
}