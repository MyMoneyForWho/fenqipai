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
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
/**
 * @Description:修改邮箱界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:08:25
 */
public class ChangeEmailActivity extends BaseActivity {

	// 接收服务器返回值

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 输入邮箱
	@ViewInject(R.id.e_mail)
	private EditText llEmail;
	
	// 确认btn
	@ViewInject(R.id.confirm)
	private Button confirm;
	
	// 返回值
	private String message;
	@SuppressWarnings("unused")
	private UserInfo userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_email);
		Bundle bundle = getIntent().getExtras();
		userInfo = (UserInfo) bundle.getSerializable("userInfo");
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
		titleBarView.setTitleText("修改邮箱");
		
//		Intent intent = getIntent();
//		String email = intent.getStringExtra("email");
		llEmail.setText(getIntent().getStringExtra("email"));
		
		llEmail.setOnFocusChangeListener(myListener);

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
				if (TextUtils.isEmpty(llEmail.getText().toString().trim())) {
					ToastUtils.show(application, "请输入邮箱", ToastUtils.TOAST_SHORT);
				} else {
					if (isEmail(llEmail.getText().toString().trim())) {
						updateNbuser(llEmail.getText().toString().trim());
					}else {
						ToastUtils.show(application, "邮箱格式错误", 1000);
					}

				}
			}
		});
	}

	/**
	 * @Description:判断邮箱格式是否正确
	 * @author hunaixin
	 * @parame email
	 * @return true/false
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * @Description:修改个人信息接口
	 * @author hunaixin
	 * @param nickName-昵称  email-邮箱  account-帐号 sex-性别：男 | 女 birthday-生日 card-身份证号码 email-邮箱 
	 * @return message
	 */
	protected void updateNbuser(final String email) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, null, null, null, null, email, null,null,null);

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
							ToastUtils.show(ChangeEmailActivity.this, jObject.optString("message"), 1000);
							Intent intent=new Intent ();
							intent.putExtra("email", email);
							setResult(Contants.CHANGE_EMAIL_RETURN, intent);
							finish();
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(ChangeEmailActivity.this, jObject.optString("message"), 1000);
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
