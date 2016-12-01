package com.fenqipai.fenqipaiandroid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * @Description:修改昵称等信息界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:20:37
 */
public class ChangeNameActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 输入昵称
	@ViewInject(R.id.nick_name)
	private EditText etNickName;

	// 确认btn
	@ViewInject(R.id.confirm)
	private Button confirm;

	// 输入条件
	@ViewInject(R.id.message)
	private TextView msg;

	// 输入标题
	@ViewInject(R.id.title)
	private TextView title;

	// 返回值
	private String message;

	// type
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_name);

		type = getIntent().getStringExtra("type");

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

		if (type.equals("truename")) {
			titleBarView.setTitleText("修改真实姓名");
			title.setText("真实姓名");
			msg.setText("输入您的真实姓名");
			etNickName.setHint("输入您的真实姓名");
			if (getIntent().getStringExtra("truename") != null) {
				etNickName.setText(getIntent().getStringExtra("truename"));
			}
		} else if (type.equals("nickname")) {
			titleBarView.setTitleText("修改昵称");
			etNickName.setText(getIntent().getStringExtra("name"));
		} else if (type.equals("idCard")) {
			titleBarView.setTitleText("修改身份证号");
			title.setText("身份证号");
			msg.setText("输入您的身份证号");
			etNickName.setHint("输入您的身份证号");
			etNickName.setKeyListener(new NumberKeyListener() {
				protected char[] getAcceptedChars() {
					return new char[] { '1', '2', '3', '4', '5', '6', '7', '8', '9', '0','x'};
				}

				public int getInputType() {
					return android.text.InputType.TYPE_CLASS_PHONE;
				}
			});
			if (getIntent().getStringExtra("card") != null) {
				etNickName.setText(getIntent().getStringExtra("card"));
			}
		} else if (type.equals("address")) {
			titleBarView.setTitleText("修改地址");
			title.setText("地址");
			msg.setText("输入您的地址");
			etNickName.setHint("输入您的常用地址");
			if (getIntent().getStringExtra("address") != null) {
				etNickName.setText(getIntent().getStringExtra("address"));
			}
		} else if (type.equals("job")) {
			titleBarView.setTitleText("修改职业");
			title.setText("职业");
			msg.setText("输入您的职业");
			etNickName.setHint("输入您的职业");
			if (getIntent().getStringExtra("job") != null) {
				etNickName.setText(getIntent().getStringExtra("job"));
			}
		}

		etNickName.setOnFocusChangeListener(myListener);

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
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideSoftInputView();
				if (type.equals("truename")) {
					if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
						ToastUtils.show(application, "请输入真实姓名", ToastUtils.TOAST_SHORT);
					} else {
						if (isTrueName(etNickName.getText().toString().trim())) {
							updateNbuserTrueName(etNickName.getText().toString().trim());
						} else {
							ToastUtils.show(application, "姓名格式错误", ToastUtils.TOAST_SHORT);
						}
					}
				} else if (type.equals("idCard")) {
					if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
						ToastUtils.show(application, "请输入身份证号", ToastUtils.TOAST_SHORT);
					} else {
						if (isIdCard(etNickName.getText().toString().trim())) {
							updateNbuserIdCard(etNickName.getText().toString().trim());
						} else {
							ToastUtils.show(application,
									StringUtils.IDCardValidate(etNickName.getText().toString().trim()),
									ToastUtils.TOAST_SHORT);
						}
					}
				} else if (type.equals("nickname")) {
					if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
						ToastUtils.show(application, "请输入昵称", ToastUtils.TOAST_SHORT);
					} else {
						if (isName(etNickName.getText().toString().trim())) {
							updateNbuser(etNickName.getText().toString().trim());
						} else {
							ToastUtils.show(application, "昵称格式错误", ToastUtils.TOAST_SHORT);
						}
					}
				} else if (type.equals("address")) {
					if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
						ToastUtils.show(application, "请输入地址", ToastUtils.TOAST_SHORT);
					} else {
						updateNbuserAddress(etNickName.getText().toString().trim());
					}
				} else if (type.equals("job")) {
					if (TextUtils.isEmpty(etNickName.getText().toString().trim())) {
						ToastUtils.show(application, "请输入职业", ToastUtils.TOAST_SHORT);
					} else {
						updateNbuserJob(etNickName.getText().toString().trim());
					}
				}
			}
		});
	}

	/**
	 * 正则表达式 判断昵称输入是否正确
	 */
	public static boolean isName(String name) {
		String str = "^[\\u4e00-\\u9fa50-9a-zAZ_\\-]{2,10}$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(name);
		return m.matches();
	}

	/**
	 * 正则表达式 判断真实姓名输入是否正确
	 */
	public static boolean isTrueName(String name) {
		String str = "[\u4e00-\u9fa5]{2,}";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(name);
		return m.matches();
	}

	/**
	 * 正则表达式 判断身份证号码输入是否正确
	 */
	public static boolean isIdCard(String id) {
		String str = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(id);
		return m.matches();
	}

	/**
	 * @Description:修改个人信息接口（nickName-昵称）
	 * @param nickName
	 * @return message
	 */
	protected void updateNbuser(final String nickName) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, nickName, null, null, null, null, null, null, null);
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
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
							Intent intent = new Intent();
							intent.putExtra("nickname", nickName);
							setResult(Contants.CHANGE_NAME_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
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

	/**
	 * @Description:修改个人信息接口（trueName）
	 * @param trueName
	 * @return message
	 */
	protected void updateNbuserTrueName(final String trueName) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, null, trueName, null, null, null, null, null, null);
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
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
							Intent intent = new Intent();
							intent.putExtra("trueName", trueName);
							setResult(Contants.CHANGE_TRUENAME_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
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

	/**
	 * @Description:修改个人信息接口（idCard）
	 * @param idCard
	 * @return message
	 */
	protected void updateNbuserIdCard(final String idCard) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, null, null, null, null, null, idCard, null, null);
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
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
							Intent intent = new Intent();
							intent.putExtra("idCard", idCard);
							setResult(Contants.CHANGE_CARD_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
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

	/**
	 * @Description:修改个人信息接口（address）
	 * @param address
	 * @return message
	 */
	protected void updateNbuserAddress(final String address) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, null, null, null, null, null, null, address, null);
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
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
							Intent intent = new Intent();
							intent.putExtra("address", address);
							setResult(Contants.CHANGE_ADDRESS_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
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

	/**
	 * @Description:修改个人信息接口（job）
	 * @param job
	 * @return message
	 */
	protected void updateNbuserJob(final String job) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, null, null, null, null, null, null, null, job);
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
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
							Intent intent = new Intent();
							intent.putExtra("job", job);
							setResult(Contants.CHANGE_JOB_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeNameActivity.this, jObject.optString("message"), 1000);
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
