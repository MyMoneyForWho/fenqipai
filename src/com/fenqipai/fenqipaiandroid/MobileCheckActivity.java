package com.fenqipai.fenqipaiandroid;

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
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * @Description:手机号绑定界面
 * @author hunaixin
 * @date 2016年11月25日 下午1:18:14
 */
public class MobileCheckActivity extends BaseActivity {
	// 接收服务器返回值
	private String registerMessage = "";

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 手机号
	@ViewInject(R.id.mobile_account)
	private ContainsEmojiEditText etPhone;

	// 发送验证码按钮
	@ViewInject(R.id.btnSendCode)
	private Button btnSendCode;

	// 输入验证码
	@ViewInject(R.id.et_set_register)
	private ContainsEmojiEditText etCode;

	// 邀请码
	@ViewInject(R.id.img_password)
	private ContainsEmojiEditText imgPassword;

	// 确认提交
	@ViewInject(R.id.common)
	private Button btnRegister;

	// 图片验证码
	@ViewInject(R.id.ic_invite3)
	private ImageView imgPicCode;

	// 图片验证码是否显示
	@ViewInject(R.id.lin_img_password)
	private RelativeLayout llPicCode;

	// 呼叫我们
	@ViewInject(R.id.call_to_us)
	private LinearLayout callMe;

	private String flag;
	// QQ用户名，性别 ，地址，头像
	private String username;
	private String sex;
	private String portrait;
	private String openId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mobile_check);

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
		titleBarView.setTitleText("账号绑定");
		String response = getIntent().getExtras().getString("response");
		flag = getIntent().getExtras().getString("flag");
		JSONObject json;

		try {
			json = new JSONObject(response);
			username = json.optString("nickname");
			sex = json.optString("gender");
			portrait = json.optString("figureurl_qq_2");
			openId = getIntent().getExtras().getString("openId");
		} catch (JSONException e) {
			e.printStackTrace();
		}

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

		btnSendCode.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String phone = etPhone.getText().toString();
				hideSoftInputView();
				if (StringUtils.judgePhoneNums(phone)) {
					// 请求标识：regist注册,login登录,forgetpassword忘记密码
					SendSMS.sendSMS(MobileCheckActivity.this, application, btnSendCode, handler, phone,
							imgPassword.getText().toString().trim(), "login");
				} else {
					ToastUtils.show(application, "手机号错误", ToastUtils.TOAST_SHORT);

				}

			}
		});

		callMe.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4008386777"));// 跳转到拨号界面，同时传递电话号码
				startActivity(dialIntent);

			}
		});

		btnRegister.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (StringUtils.judgePhoneNums(etPhone.getText().toString())) {

					hideSoftInputView();
					qqMobileRegist(flag);
				} else {

					ToastUtils.show(application, "手机号错误", ToastUtils.TOAST_SHORT);

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
			} else if (jObject.optString("code").equals("hasregist")) {
				etCode.setText("");
				ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
			} else  {
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

		if (llPicCode.getVisibility() == View.GONE) {
			llPicCode.setVisibility(View.VISIBLE);
		}
		getImage();

	}

	/**
	 * @Description:获取图片验证码接口
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
	 * @Description:qq号注册绑定手机号
	 * @author hunaixin
	 * @parame type
	 * @return registerMessage
	 */
	public void qqMobileRegist(final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(MobileCheckActivity.this)) {
					registerMessage = application.qqMobileRegist(openId, etPhone.getText().toString(), username,
							portrait, etCode.getText().toString(), sex, type);
					return true;
				}
				return false;

			}

			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(registerMessage);
						if (jsonObject.optString("code").equals("registSuccess")
								|| jsonObject.optString("code").equals("bindSuccess")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);
							JSONObject jObject = jsonObject.optJSONObject("data");
							if (type.equals("qq")) {
								SPUtils.put(application, Contants.LOGIN_TYPE, 1);
							} else if (type.equals("weixin")) {
								SPUtils.put(application, Contants.LOGIN_TYPE, 2);
							}
							SPUtils.put(application, Contants.USER_ID, jObject.optString("userId"));
							SPUtils.put(application, Contants.USER_NAME, jObject.optString("nickName"));
							SPUtils.put(application, Contants.USER_PORTRAIT, jObject.optString("portrait"));
							SPUtils.put(application, Contants.USER_TOKEN, jObject.optString("token"));
							SPUtils.put(application, Contants.USER_BALANCE, jObject.optString("balance"));
							SPUtils.put(application, Contants.USER_MOBILE, jObject.optString("mobile"));
							startActivity(MainActivity.class);
							LoginActivity.activity.finish();
							finish();

						} else if (jsonObject.optString("code").equals("qqHasBind")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);

						} else if (jsonObject.optString("code").equals("mobileHasBind")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);

						}else if (jsonObject.optString("code").equals("messageCodeError")) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);

						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
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

}
