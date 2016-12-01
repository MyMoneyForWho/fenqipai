package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.weibo.WBOpenAPI;
import com.fenqipai.fenqipaiandroid.wxapi.WXAsyncHttpClient;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

/**
 * @Description:引导页
 * @author hunaixin
 * @date 2016年11月25日 上午11:38:23
 */
public class GuideActivity extends BaseActivity {
	// 记录是否第一次安装
	private SharedPreferences sharedPreferences;
	@SuppressWarnings("unused")
	private boolean isFirst = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_guide);

		// 判断是否连接网络
		if (NetUtils.isConnected(getApplicationContext())) {
			// setLoginState();
			isFirst();
		} else {
			// isLogin();
			Toast.makeText(this, "请检查网络连接", Toast.LENGTH_LONG).show();
			isFirst();
		}

	}

	/**
	 * 在未连网的状态下,判断是否登陆过
	 * @title isLogin
	 * @author liuchengbao
	 * @Description 
	 */
	@SuppressWarnings("unused")
	private void isLogin() {
		int type = (Integer) SPUtils.get(this, Contants.LOGIN_TYPE, 0);
		if (type == 1 || type == 2 || type == 3 || type == 4) {
			SPUtils.put(this, Contants.LOGIN_STATE, true);
		} else {
			SPUtils.put(this, Contants.LOGIN_STATE, false);
		}
		handler.sendEmptyMessageDelayed(0, 2000);
	}

	/**
	 * @Description:设置登录状态
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	@SuppressWarnings("unused")
	private void setLoginState() {

		int type = (Integer) SPUtils.get(this, Contants.LOGIN_TYPE, 0);

		switch (type) {
		case 0:
			handler.sendEmptyMessageDelayed(0, 2000);
			break;
		case 1:

			String qq_token = (String) SPUtils.get(this, Contants.QQ_TOKEN, "");
			String qq_expires = (String) SPUtils.get(this, Contants.QQ_EXPIRES, "");
			if (StringUtils.isEmpty(qq_token)) {
				return;
			} else {
				long many_time = (Long.parseLong(qq_expires) - (long) System.currentTimeMillis());
				if (many_time > 0) {
					SPUtils.put(this, Contants.LOGIN_STATE, true);
				} else {
					SPUtils.put(this, Contants.LOGIN_STATE, false);
				}
			}
			handler.sendEmptyMessageDelayed(0, 2000);
			break;
		case 2:

			String weibo_token = (String) SPUtils.get(this, Contants.WEIBO_TOKEN, "");
			String weibo_expires = (String) SPUtils.get(this, Contants.WEIBO_EXPIRES, "");
			String weibo_refresh_token = (String) SPUtils.get(this, Contants.WEIBO_REFRESH_TOKEN, "");
			if (StringUtils.isEmpty(weibo_token)) {
				return;
			} else {
				long many_time = (Long.parseLong(weibo_expires) - (long) System.currentTimeMillis());
				if (many_time > 0) {
					SPUtils.put(this, Contants.LOGIN_STATE, true);
					handler.sendEmptyMessageDelayed(0, 2000);
				} else {
					WBOpenAPI.refreshTokenRequest(getApplicationContext(), handler, weibo_refresh_token);
				}
			}
			break;
		case 3:

			String openId = (String) SPUtils.get(this, Contants.WEIXIN_PENID, "");
			String accessToken = (String) SPUtils.get(this, Contants.WEIXIN_ACCESS_TOKEN, "");

			WXAsyncHttpClient.getWeiXinExpires(getApplicationContext(), openId, accessToken, handler);
			break;
		case 4:
			String userId = (String) SPUtils.get(this, Contants.USER_ID, "");
			if (StringUtils.isEmpty(userId)) {
				return;
			} else {
				SPUtils.put(this, Contants.LOGIN_STATE, true);
				handler.sendEmptyMessageDelayed(0, 2000);
			}
			break;

		default:
			break;
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {

		public void handleMessage(Message msg) {
			// 获取登录状态
			// boolean loginState = (Boolean) SPUtils.get(GuideActivity.this,
			// Contants.LOGIN_STATE, false);

			// 如果已经登录 跳转到主页面 否则跳转到登录页面
			// if (loginState) {
			// startActivity(MainActivity.class);
			Intent intent = new Intent(getApplicationContext(), MainActivity.class);
			startActivity(intent);
			// } else {
			// startActivity(LoginActivity.class);
			// }

			finish();
		};
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	/**
	 * @Description:记录是否第一次安装app
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void isFirst() {
		sharedPreferences = getSharedPreferences("isFirst", MODE_PRIVATE);
		// 	暂时不需要这个功能，修改为false，需要时改回true
		if (sharedPreferences.getBoolean("isFirst", false)) {
			// 第一次启动
			SharedPreferences.Editor editor = sharedPreferences.edit();
			editor.putBoolean("isFirst", false);
			editor.commit();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(GuideActivity.this, FirstGuideActivity.class);
					startActivity(intent);
					finish();

				}
			}, 2000);
		} else {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					Intent intent = new Intent(GuideActivity.this, MainActivity.class);
					startActivity(intent);
					finish();

				}
			}, 2000);
		}

	}

}
