package com.fenqipai.fenqipaiandroid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.net.NetClient;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.sms.SendSMS;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


/**
 * @Description:注册页面
 * @author qianyuhang
 * @date 2016-11-14 下午1:44:04
 */
public class RegistActivity extends BaseActivity {

	private BaseApplication application;

	// 接收服务器返回值
	private String registerMessage = "";

	// 公共title
	@ViewInject(R.id.login_titleBar)
	private TitleBarView titleBarView;
	//
	// 手机号
	@ViewInject(R.id.et_set_phone)
	private ContainsEmojiEditText et_phone;

	// 发送验证码按钮
	@ViewInject(R.id.btnSendCode)
	private Button btnSendCode;

	// 输入验证码
	@ViewInject(R.id.et_set_register)
	private ContainsEmojiEditText et_Code;

	// 输入密码
	@ViewInject(R.id.et_set_password)
	private ContainsEmojiEditText etPassword;

	// 确定密码
	@ViewInject(R.id.confirm_password)
	private ContainsEmojiEditText confirmPassword;

	// 图片验证码
	@ViewInject(R.id.image_pass_code)
	private ContainsEmojiEditText invitePassword;

	// 注册按钮
	@ViewInject(R.id.btnRegister)
	private Button btnRegister;
	//
	// // 注册协议
	// @ViewInject(R.id.register_agreement)
	// private TextView registerAgreement;
	//
	// // 验证邀请码
	// @ViewInject(R.id.btnVerify)
	// private Button btnVerify;

	// 图片验证码
	@ViewInject(R.id.image_code)
	private ImageView imgPicCode;

	// 致电我们
	@ViewInject(R.id.call_to_us)
	private LinearLayout callMe;

	// 致电我们
	@ViewInject(R.id.lin_img_pass)
	private RelativeLayout imgPass;

	private String imageUrl = "";

	// 记录选择用户协议状态
	private Boolean flag = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_regist);

		createLoadingDialog(RegistActivity.this);

		// 注入view和事件
		ViewUtils.inject(this);

		application = (BaseApplication) getApplication();

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
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.register);
		flag = true;
		
		et_phone.setOnFocusChangeListener(myListener);
		et_Code.setOnFocusChangeListener(myListener);
		etPassword.setOnFocusChangeListener(myListener);
		confirmPassword.setOnFocusChangeListener(myListener);
		invitePassword.setOnFocusChangeListener(myListener);
		
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initEvent() {
		// 发送验证码
		btnSendCode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String phone = et_phone.getText().toString();
				// 请求标识：regist注册,login登录,forgetpassword忘记密码
				SendSMS.sendSMS(RegistActivity.this, application, btnSendCode, handler, phone,
						invitePassword.getText().toString().trim(), "regist");

			}
		});
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		callMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// 跳转到拨号界面，同时传递电话号码
				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));
				startActivity(dialIntent);

			}
		});
		// 注册
		btnRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (flag) {
					collapseSoftInputMethod();
					String phoneNums = et_phone.getText().toString();
					String etCode = et_Code.getText().toString();
					String password = etPassword.getText().toString();
					String confirm = confirmPassword.getText().toString();
					if (StringUtils.isEmpty(phoneNums)) {
						ToastUtils.show(getApplicationContext(), "请输入手机号", ToastUtils.TOAST_SHORT);
						return;
					} else if (StringUtils.isEmpty(etCode)) {
						ToastUtils.show(getApplicationContext(), "请输入验证码", ToastUtils.TOAST_SHORT);
						return;
					} else if (StringUtils.isEmpty(password)) {
						ToastUtils.show(getApplicationContext(), "请设置密码", ToastUtils.TOAST_SHORT);
						return;
					} else if (!StringUtils.judgePhoneNums(phoneNums)) {
						ToastUtils.show(getApplicationContext(), "手机号格式错误", ToastUtils.TOAST_SHORT);
						return;

					} else if (StringUtils.isEmpty(confirm)) {
						ToastUtils.show(getApplicationContext(), "请输入确认密码", ToastUtils.TOAST_SHORT);
						return;
					} else if (!confirm.equals(password)) {
						ToastUtils.show(getApplicationContext(), "请确认两次输入一致", ToastUtils.TOAST_SHORT);
						return;
					} else if (!StringUtils.isPassWord(etPassword.getText().toString())) {
						ToastUtils.show(getApplicationContext(), "密码必须由6-18数字加英文组成", ToastUtils.TOAST_SHORT);
						return;
					}
					commitPhoneInfo(phoneNums, phoneNums, password, etCode);
				} else {
					ToastUtils.show(RegistActivity.this, "请阅读并同意用户协议", ToastUtils.TOAST_SHORT);
				}

			}
		});

	}

	/**
	 * 正则表达式 判断昵称输入密码是否正确
	 */
	public static boolean isPassword(String password) {
		String str = "^([a-zA-Z]|[0-9]){6,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(password);
		return m.matches();
	}

	/**
	 * 收起软键盘并设置提示文字
	 * 
	 * @title collapseSoftInputMethod
	 * @author liuchengbao
	 */
	public void collapseSoftInputMethod() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(btnRegister.getWindowToken(), 0);
	}

	/**
	 * 提交注册账户信息
	 * @title commitPhoneInfo
	 * @author hunaixin
	 * @param String account, String phone, String password, String messageCode
	 * @return registerMessage
	 */
	protected void commitPhoneInfo(final String account, final String phone, final String password,
			final String messageCode) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				loadingDialogShow(R.string.commit_msg);
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				if (NetUtils.isConnected(getApplicationContext())) {
					registerMessage = application.register(account, phone, password, messageCode);
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				loadingDialogDismiss();
				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(registerMessage);
						if (jsonObject.optString("code").equals("loginSuccess")) {
							JSONObject jObject = jsonObject.optJSONObject("data");
							SPUtils.put(getApplicationContext(), Contants.LOGIN_TYPE, 4);
							SPUtils.put(application, Contants.USER_ID, jObject.optString("userId"));
							SPUtils.put(application, Contants.USER_NAME, jObject.optString("nickName"));
							SPUtils.put(application, Contants.USER_PORTRAIT, jObject.optString("portrait"));
							SPUtils.put(application, Contants.USER_TOKEN, jObject.optString("token"));
							SPUtils.put(application, Contants.USER_BALANCE, jObject.optString("balance"));
							SPUtils.put(application, Contants.USER_MOBILE, jObject.optString("mobile"));
							Intent intent = new Intent(RegistActivity.this,AccountImprovementActivity.class);
							intent.putExtra("phone", phone);
							startActivity(intent);
							LoginActivity.activity.finish();
							finish();
						} else if (jsonObject.optString("code").equals("messageCodeError")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);
						} else if (jsonObject.optString("code").equals("hasRegist")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
				}
			}
		});
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
				et_Code.setText("");
				ToastUtils.show(application, "短信验证码已发送，请注意查收", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeRequire")) {

				et_Code.setText("");
				showVerifyCode();
				ToastUtils.show(application, "请输入验证码", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeError")) {
				et_Code.setText("");
				showVerifyCode();
				ToastUtils.show(application, "图片验证码错误", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("hasregist")) {
				et_Code.setText("");
				showVerifyCode();
				ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
			}  else {
				ToastUtils.show(application, "短信验证码发送失败", ToastUtils.TOAST_SHORT);
			}

		}
	};

	/**
	 * @Description:切换验证码图片显示
	 * @param 
	 * @return 
	 */
	public void showVerifyCode() {

		if (imgPass.getVisibility() == View.GONE) {
			imgPass.setVisibility(View.VISIBLE);
		}
		http_get_image();
		// String imgUrl=NetClient.http_get_image

		// application.imageLoader.displayImage(
		// URL.getOffLineUrl(application, URL.CAPTCHA) + "?round="
		// + new Random().nextInt(), imgPicCode,
		// application.options);
		// application.imageLoader.d

	}

	/**
	 * @Description:获取图片验证码接口
	 * @author hunaixin
	 * @parame 
	 * @return imageUrl
	 */
	public void http_get_image() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				imageUrl = NetClient.http_get_image(application, URL.getURL(URL.CAPTCHA));

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					application.imageLoader.displayImage("file://" + imageUrl, imgPicCode, application.options);
				}
				super.onPostExecute(result);
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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

}
