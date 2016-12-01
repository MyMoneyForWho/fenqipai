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
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ContainsEmojiEditText;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;

/**
 * @Description:忘记密码页面
 * @author hunaixin
 * @date 2016年11月25日 上午11:33:57
 */
public class ForgotPasswordActivity extends BaseActivity {
	// 接收服务器返回值
	private String registerMessage = "";

	// 手机号
	@ViewInject(R.id.account)
	private ContainsEmojiEditText etPhone;

	// 标题栏
	@ViewInject(R.id.forgot_titleBar)
	private TitleBarView titleView;

	// // 发送验证码按钮
	// @ViewInject(R.id.btnSendCode)
	// private Button btnSendCode;

	// 短信验证码
	@ViewInject(R.id.et_set_sms)
	private ContainsEmojiEditText etCode;

	// 图片验证码
	@ViewInject(R.id.img_password)
	private ContainsEmojiEditText imgPassword;

	// 确认提交
	@ViewInject(R.id.common)
	private Button btnRegister;

	// 图片验证码
	@ViewInject(R.id.ic_invite3)
	private ImageView imgPicCode;

	// 致电我们
	@ViewInject(R.id.call_to_us)
	private LinearLayout callMe;

	// 重设密码
	@ViewInject(R.id.tx_password)
	private ContainsEmojiEditText etPassword;

	// 确定重设密码
	@ViewInject(R.id.confirm_password)
	private ContainsEmojiEditText confirmPassword;

	@ViewInject(R.id.lin_img_pass)
	private RelativeLayout linImagePass;

	// 发送短信验证码
	@ViewInject(R.id.send_sms)
	private Button sendSms;

	private PopupWindow myPopupWindow;

	private LinearLayout background;

	//
	@ViewInject(R.id.activity_forget_password)
	private LinearLayout mLayout;
	
	private String passwordType;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_forgot_password);
		
		passwordType = getIntent().getStringExtra("passwordType");

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
		titleView.setTitleText(R.string.forget_pass_no_line);

		background = (LinearLayout) findViewById(R.id.pop_background);
		
		etPhone.setOnFocusChangeListener(myListener);
		etCode.setOnFocusChangeListener(myListener);
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

		sendSms.setOnClickListener(new OnClickListener() {// 发送验证码

			@Override
			public void onClick(View v) {
				String phone = etPhone.getText().toString();
				hideSoftInputView();
				// 请求标识：regist注册,login登录,forgetpassword忘记密码
				SendSMS.sendSMS(ForgotPasswordActivity.this, application, sendSms, handler, phone,
						imgPassword.getText().toString().trim(), "paypassword");

			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {
			@Override

			public void onClick(View v) {
				hideSoftInputView();
				if (TextUtils.isEmpty(etPhone.getText().toString().trim())) {

					ToastUtils.show(application, "手机号错误", ToastUtils.TOAST_SHORT);
				} else if (TextUtils.isEmpty(etCode.getText().toString().trim())) {
					ToastUtils.show(application, "请输入验证码", ToastUtils.TOAST_SHORT);
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
					forgetPassword(etPhone.getText().toString().trim(), etPassword.getText().toString().trim(),
							etCode.getText().toString().trim(),passwordType);

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
				etCode.setText("");
				ToastUtils.show(application, "短信验证码已发送，请注意查收", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeRequire")) {

				etCode.setText("");
				showVerifyCode();
				ToastUtils.show(application, "请输入验证码", ToastUtils.TOAST_SHORT);
			} else if (jObject.optString("code").equals("captchaCodeError")) {
				etCode.setText("");
				showVerifyCode();
				ToastUtils.show(application, "图片验证码错误", ToastUtils.TOAST_SHORT);
			} else {
				ToastUtils.show(application, "短信验证码发送失败", ToastUtils.TOAST_SHORT);
			}

		}
	};

	/**
	 * @Description: 忘记密码接口
	 * @author hunaixin
	 * @parame mobile, password, messageCode,type
	 * @return registerMessage
	 */
	public void forgetPassword(final String mobile, final String password, final String messageCode,final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				registerMessage = application.forgetPassword(mobile, password, messageCode,type);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				try {
					JSONObject jsonObject = new JSONObject(registerMessage);
					if (jsonObject.optString("code").equals("noRegist")) {
						ToastUtils.show(application, jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
					} else if (jsonObject.optString("code").equals("lock")) {
						ToastUtils.show(application, jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
					} else if (jsonObject.optString("code").equals("resetPasswordSuccess")) {
						JSONObject jObject = jsonObject.optJSONObject("data");
						SPUtils.put(application, Contants.LOGIN_TYPE, 1);
						SPUtils.put(application, Contants.USER_ID, jObject.optString("userId"));
						SPUtils.put(application, Contants.USER_NAME, jObject.optString("nickName"));
						SPUtils.put(application, Contants.USER_PORTRAIT, jObject.optString("portrait"));
						SPUtils.put(application, Contants.USER_TOKEN, jObject.optString("token"));
						SPUtils.put(application, Contants.USER_BALANCE, jObject.optString("balance"));
						SPUtils.put(application, Contants.USER_MOBILE, jObject.optString("mobile"));
						showPopWindow();

						if (background.getVisibility() == View.GONE) {
							background.setVisibility(View.VISIBLE);
						}
						// startActivity(MainActivity.class);
						// LoginActivity.activity.finish();

						new Handler().postDelayed(new Runnable() {

							@Override
							public void run() {
								finish();
							}
						}, 500);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}
		});
	}

	/**
	 * @Description: 修改成功popWindow
	 * @param view
	 */
	public void showPopWindow() {

		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_updated_successfully, null);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});

	}

	/**
	 * @Description: 切换验证码图片显示
	 * @param 
	 * @return 
	 */
	public void showVerifyCode() {
		if (linImagePass.getVisibility() == View.GONE) {
			linImagePass.setVisibility(View.VISIBLE);
		}
		getImage();

	}

	/**
	 * @Description:获取验证码图片接口
	 * @author hunaixin
	 * @parame 
	 * @return registerMessage
	 */
	public void getImage() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				registerMessage = NetClient.http_get_image(application, URL.getURL(URL.CAPTCHA));
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					application.imageLoader.displayImage("file://" + registerMessage, imgPicCode, application.options);
				}
				super.onPostExecute(result);
			}
		});
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
	 * 正则表达式 判断昵称输入密码是否正确
	 * @param password
	 */
	public static boolean isPassword(String password) {
		String str = "^([a-zA-Z]|[0-9]){6,20}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(password);
		return m.matches();
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
