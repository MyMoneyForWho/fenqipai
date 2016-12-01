package com.fenqipai.fenqipaiandroid;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.common.ProgressGenerator;
import com.fenqipai.fenqipaiandroid.net.NetClient;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.sms.SendSMS;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.wxapi.WXEntryActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Description:登录界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:50:32
 */
public class LoginActivity extends BaseActivity implements ProgressGenerator.OnCompleteListener {

	// 标题栏
	@ViewInject(R.id.login_titleBar)
	private TitleBarView titleBarView;

	// 帐号
	@ViewInject(R.id.accounts)
	private ContainsEmojiEditText account;

	// 帐号
	@ViewInject(R.id.smspassword)
	private ContainsEmojiEditText smsPassword;

	// 密码
	@ViewInject(R.id.password)
	private ContainsEmojiEditText password;

	// 图片验证码
	@ViewInject(R.id.image_code)
	private ImageView ImageCode;

	// 图片验证码（编辑区）
	@ViewInject(R.id.image_pass_code)
	private ContainsEmojiEditText ImagePassword;

	// 登录按钮
	@ViewInject(R.id.btnSignIn)
	private Button btnSignIn;

	// 没有帐号
	@ViewInject(R.id.tv_no_account)
	private Button tvNoAccount;

	// 忘记密码
	@ViewInject(R.id.tv_forget_password)
	private Button tvForgetPassword;

	// qq登录
	@ViewInject(R.id.qq_login)
	private ImageView qqLogin;

	// 微信登录
	@ViewInject(R.id.weixin_login)
	private ImageView weixinLogin;

	// 手机登录
	@ViewInject(R.id.lin_password)
	private LinearLayout passLogin;

	// 快捷登录
	@ViewInject(R.id.lin_sms)
	private LinearLayout phoneLogin;

	// 快捷登录
	@ViewInject(R.id.lin_img_pass)
	private RelativeLayout imgPassword;

	// 发送短信验证码
	@ViewInject(R.id.send_sms)
	private Button sendSms;

	// 登录方式切换
	@ViewInject(R.id.radio_group)
	private RadioGroup rGroup;

	// 登录方式text
	@ViewInject(R.id.login_type)
	private TextView typeText;

	public Context mContext;

	private String loginMessage;

	@SuppressWarnings("unused")
	private String qqMessage;

	public static LoginActivity activity;

	// QQ用户
	private UserInfo mInfo;

	private Boolean isPassword = true;

	// QQ登陆
	private Tencent mTencent = null;

	// 记录各个编辑框状态
	private Map<String, String> map = new HashMap<String, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		createLoadingDialog(LoginActivity.this);
		// 注入view和事件
		ViewUtils.inject(this);

		activity = LoginActivity.this;
		mContext = LoginActivity.this;
		createLoadingDialog(activity);
		// 初始化QQ
		if (mTencent == null) {
			mTencent = Tencent.createInstance(Contants.QQ_APP_ID, this);
		}
		initView();

		initEvent();

	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	protected void initView() {
		createLoadingDialog(LoginActivity.activity);
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);

		titleBarView.setTitleText(R.string.login);

		map.put("pass", "0");
		map.put("phone", "0");
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	protected void initEvent() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		btnSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String phoneAccount = account.getText().toString().trim();
				String passWord = password.getText().toString().trim();
				if (StringUtils.isEmpty(phoneAccount)) {
					ToastUtils.show(getApplicationContext(), "账号不能为空", ToastUtils.TOAST_SHORT);
				} else if (!StringUtils.judgePhoneNums(phoneAccount)) {
					ToastUtils.show(getApplicationContext(), "请检查手机号", ToastUtils.TOAST_SHORT);
				} else {
					hideSoftInputView();
					// btnSignIn.setEnabled(false);
					if (isPassword) {
						if (StringUtils.isEmpty(passWord)) {
							ToastUtils.show(LoginActivity.this, "密码不能为空", ToastUtils.TOAST_SHORT);
						} else if (!StringUtils.isPassWord(passWord)) {
							ToastUtils.show(LoginActivity.this, "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						} else {
							// 账号登录
							loginByAccount();
						}
					} else {
						// 手机号快捷登录
						loginByPhone();
					}
				}
			}
		});
		sendSms.setOnClickListener(new OnClickListener() {// 发送验证码

			@Override
			public void onClick(View v) {
				String phone = account.getText().toString();
				hideSoftInputView();
				// 请求标识：regist注册,login登录,forgetpassword忘记密码
				SendSMS.sendSMS(LoginActivity.this, application, sendSms, handler, phone,
						ImagePassword.getText().toString().trim(), "login");

			}
		});

		qqLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetUtils.isConnected(mContext)) {
					qqLogin.setClickable(false);
					QQ_Login();
				} else {
					ToastUtils.show(getApplicationContext(), "未连接网络", ToastUtils.TOAST_SHORT);
				}

			}
		});

		weixinLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (NetUtils.isConnected(mContext)) {
					startActivity(WXEntryActivity.class);
				} else {
					ToastUtils.show(getApplicationContext(), "未连接网络", ToastUtils.TOAST_SHORT);
				}

			}
		});

		tvNoAccount.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(RegistActivity.class);
			}
		});

		rGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.ragido_pass:
					typeText.setText(R.string.account_on);
					if (map.get("pass").equals("1")) {
						imgPassword.setVisibility(View.VISIBLE);
					} else {
						imgPassword.setVisibility(View.GONE);
					}
					passLogin.setVisibility(View.VISIBLE);
					phoneLogin.setVisibility(View.GONE);
					isPassword = true;
					break;
				case R.id.ragido_phone:
					typeText.setText(R.string.phone_on);
					if (map.get("phone").equals("1")) {
						imgPassword.setVisibility(View.VISIBLE);
					} else {
						imgPassword.setVisibility(View.GONE);
					}
					passLogin.setVisibility(View.GONE);
					phoneLogin.setVisibility(View.VISIBLE);
					isPassword = false;

					break;
				default:
					break;
				}

			}
		});
		tvForgetPassword.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
				intent.putExtra("passwordType", "password");
				startActivity(intent);
				finish();
			}
		});

	};

	/**
	 * QQ登陆
	 * 
	 * @title QQ_Login
	 * @author liuchengbao
	 * @Description
	 */
	public void QQ_Login() {
		// if (!mTencent.isSessionValid()) {
		mTencent.login(this, "all", loginListener);
		// }
	}

	/**
	 * @Title:切换验证码图片显示 @param @return @throws
	 */
	public void showVerifyCode() {
		if (imgPassword != null && imgPassword.getVisibility() == View.GONE) {
			imgPassword.setVisibility(View.VISIBLE);
		}
		getImage();

		if (isPassword) {
			map.put("pass", "1");
		} else {
			map.put("phone", "1");
		}

	}

	/**
	 * @Description:获取验证码图片接口
	 * @author hunaixin
	 * @parame 
	 * @return loginMessage
	 */
	public void getImage() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				loginMessage = NetClient.http_get_image(application, URL.getURL(URL.CAPTCHA));
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					application.imageLoader.displayImage("file://" + loginMessage, ImageCode, application.options);
				}
				super.onPostExecute(result);
			}
		});
	}

	/**
	 * QQ登陆回调接口
	 */
	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			qqLogin.setClickable(true);
			loadingDialogShow("账号验证中....");
			initOpenidAndToken(values);
			// String openid = (String) SPUtils.get(mContext,
			// Contants.QQ_OPENID, "");
			// qqMobileLogin(values.toString(),openid);
			updateUserInfo();
		}
	};

	/**
	 * 初始化Openid和Token
	 * 
	 * @title initOpenidAndToken
	 * @author liuchengbao
	 * @Description
	 * @param jsonObject
	 */
	private void initOpenidAndToken(JSONObject jsonObject) {

		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires) && !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
				SPUtils.put(this, Contants.QQ_TOKEN, (String) token);
				SPUtils.put(this, Contants.QQ_OPENID, (String) openId);
				SPUtils.put(this, Contants.QQ_EXPIRES,
						String.valueOf((System.currentTimeMillis() + (Long.parseLong(expires) * 1000))));
			}
		} catch (Exception e) {

		}
	}

	/**
	 * @Title:loginByAccount
	 * @Description:账号登录
	 * @param phone, password, captchaCode
	 * @return loginMessage
	 */
	protected void loginByAccount() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(mContext)) {
					loginMessage = application.phoneLoginCheck(account.getText().toString().trim(),
							password.getText().toString().trim(), ImagePassword.getText().toString().trim());

					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					try {
						parsing();
					} catch (JSONException e) {
						btnSignIn.setEnabled(true);
						e.printStackTrace();
					}
				} else {
					btnSignIn.setEnabled(true);
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
				}
			}
		});
	}

	/**
	 * @Title:loginByPhone 
	 * @Description:手机号快捷登录
	 * @param mobile, messageCode
	 * @return 
	 */
	protected void loginByPhone() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(mContext)) {
					loginMessage = application.loginMobile(account.getText().toString().trim(),
							smsPassword.getText().toString().trim());
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					try {
						loadingDialogDismiss();
						parsing();
					} catch (JSONException e) {
						btnSignIn.setEnabled(true);
						e.printStackTrace();
					}
				} else {
					btnSignIn.setEnabled(true);
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
				}
			}
		});
	}

	/**
	 * @Title:parsing 
	 * @Description:解析
	 * @param 
	 * @return 
	 */

	private void parsing() throws JSONException {
		JSONObject jsonObject = new JSONObject(loginMessage);
		if (jsonObject.optString("code").equals("loginSuccess")) {// 登录成功
			JSONObject jObject = jsonObject.optJSONObject("data");

			btnSignIn.setEnabled(false);
			account.setEnabled(false);
			password.setEnabled(false);
			SPUtils.put(mContext, Contants.LOGIN_TYPE, 4);
			SPUtils.put(application, Contants.USER_ID, jObject.optString("userId"));
			SPUtils.put(application, Contants.USER_NAME, jObject.optString("nickName"));
			SPUtils.putPortrait(application, Contants.USER_PORTRAIT, jObject.optString("portrait"));
			SPUtils.put(application, Contants.USER_TOKEN, jObject.optString("token"));
			SPUtils.put(application, Contants.USER_BALANCE, jObject.optString("balance"));
			SPUtils.put(application, Contants.USER_MOBILE, jObject.optString("mobile"));
			startActivity(MainActivity.class);
			finish();
		} else if (jsonObject.optString("code").equals("passwordError")) {// 密码错误
			ToastUtils.show(getApplicationContext(), jsonObject.optString("message"), ToastUtils.TOAST_SHORT);

		} else if (jsonObject.optString("code").equals("noRegist")) {// 该手机号或帐号未注册
			ToastUtils.show(getApplicationContext(), jsonObject.optString("message"), ToastUtils.TOAST_SHORT);

		} else if (jsonObject.optString("code").equals("captchaCodeRequire")) {// 请输入验证码
			showVerifyCode();
			ToastUtils.show(getApplicationContext(), jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
		} else if (jsonObject.optString("code").equals("lock")) {// 账号锁死
			ToastUtils.show(getApplicationContext(), jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
		} else if (jsonObject.optString("code").equals("captchaCodeError")) {// 验证码错误
			showVerifyCode();
			ToastUtils.show(getApplicationContext(), jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
		}

	}

	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				return;
			}
			doComplete((JSONObject) response);
		}

		protected void doComplete(JSONObject values) {

		}

		@Override
		public void onError(UiError e) {
		}

		@Override
		public void onCancel() {
		}
	}

	@Override
	public void onComplete() {
		// finish();
	}

	/**
	 * @Description:更新用户信息
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {

				@Override
				public void onError(UiError e) {

				}

				@Override
				public void onComplete(final Object response) {
					String openid = (String) SPUtils.get(mContext, Contants.QQ_OPENID, "");

					qqMobileLogin(response.toString(), openid);
				}

				@Override
				public void onCancel() {

				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);

		}
	}

	/**
	 * @Title:qqMobileLogin 
	 * @Description:检查qq登录 
	 * @param response, openId
	 * @return loginMessage
	 */
	protected void qqMobileLogin(final String response, final String openId) {

		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(mContext)) {
					loginMessage = application.qqMobileLogin(openId, "qq");
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					try {
						loadingDialogDismiss();
						JSONObject jObject = new JSONObject(loginMessage);
						if (jObject.optString("code").equals("noBind")) {
							Bundle bundle = new Bundle();
							bundle.putString("response", response);
							bundle.putString("openId", openId);
							bundle.putString("flag", "qq");
							startActivity(MobileCheckActivity.class, bundle);

						} else if (jObject.optString("code").equals("loginSuccess")) {
							JSONObject jsonObject = jObject.optJSONObject("data");
							SPUtils.put(mContext, Contants.LOGIN_TYPE, 1);
							SPUtils.put(application, Contants.USER_ID, jsonObject.optString("userId"));
							SPUtils.put(application, Contants.USER_NAME, jsonObject.optString("nickName"));
							SPUtils.put(application, Contants.USER_PORTRAIT, jsonObject.optString("portrait"));
							if (TextUtils.isEmpty(jsonObject.optString("portrait"))) {
							} else if (jsonObject.optString("portrait").startsWith("http")) {
								SPUtils.put(application, Contants.USER_PORTRAIT, jsonObject.optString("portrait"));
							} else {
								SPUtils.put(application, Contants.USER_PORTRAIT,
										URL.getURL(URL.FILE_UPLOAD) + jsonObject.optString("portrait"));
							}

							SPUtils.put(application, Contants.USER_TOKEN, jsonObject.optString("token"));
							SPUtils.put(application, Contants.USER_BALANCE, jsonObject.optString("balance"));
							SPUtils.put(application, Contants.USER_MOBILE, jsonObject.optString("mobile"));
							ToastUtils.show(getApplicationContext(), jObject.optString("message"),
									ToastUtils.TOAST_SHORT);
							startActivity(MainActivity.class);
							finish();
						}

					} catch (JSONException e) {
						loadingDialogDismiss();
						e.printStackTrace();
					}

				} else {
					loadingDialogDismiss();
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
				}

			}
		});

	}

	@SuppressWarnings("static-access")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// QQ授权回调
		mTencent.onActivityResultData(requestCode, resultCode, data, loginListener);
		if (requestCode == Constants.REQUEST_API) {
			if (resultCode == Constants.RESULT_LOGIN) {
				Tencent.handleResultData(data, loginListener);
			}
		} else if (requestCode == Constants.REQUEST_APPBAR) { // app内应用吧登录
			if (resultCode == Constants.RESULT_LOGIN) {
				// updateUserInfo();
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			JSONObject jObject = null;
			String message = (String) msg.obj;
			try {
				jObject = new JSONObject(message);
			} catch (JSONException e) {
				jObject = new JSONObject();
				e.printStackTrace();
			}
			if (jObject.optString("code").equals("success")) {
				smsPassword.setText("");
				ToastUtils.show(application, "短信验证码已发送，请注意查收", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeRequire")) {

				smsPassword.setText("");
				showVerifyCode();
				ToastUtils.show(application, "请输入验证码", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeError")) {
				smsPassword.setText("");
				showVerifyCode();
				ToastUtils.show(application, "图片验证码错误", ToastUtils.TOAST_SHORT);
			} else {
				ToastUtils.show(application, "短信验证码发送失败", ToastUtils.TOAST_SHORT);
			}

		}
	};

	/**
	 * 隐藏键盘
	 * 
	 * @title hideSoftInputView
	 * @author zhaoQingyang
	 */
	public void hideSoftInputView() {
		InputMethodManager manager = ((InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE));
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}