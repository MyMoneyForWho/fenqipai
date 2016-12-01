package com.fenqipai.fenqipaiandroid.wxapi;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.MobileCheckActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.RelativeLayout;

/**
 * 微信登陆
 * 
 * @name WXEntryActivity
 * @author liuchengbao
 * @date 2015年10月15日
 * @modify
 * @modifyDate 2015年10月15日
 * @modifyContent
 */
public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {

	// 微信api
	private IWXAPI api;

	// 返回openid，accessToken消息码
	public static final int RETURN_OPENID_ACCESSTOKEN = 0;

	// 返回昵称，uid消息码
	public static final int RETURN_NICKNAME_UID = 1;

	// 提交用户个人信息返回消息
	private String resultMessage = "";

	private Context mContext;

	private boolean isAuth = false;
	
	private String loginMessage;
	
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case RETURN_OPENID_ACCESSTOKEN:
				String openId = (String) SPUtils.get(mContext, Contants.WEIXIN_PENID, "");
				String accessToken = (String) SPUtils.get(mContext, Contants.WEIXIN_ACCESS_TOKEN, "");
				WXAsyncHttpClient.getUID(openId, accessToken, handler);

				break;

			case RETURN_NICKNAME_UID:

				Bundle bundle = (Bundle) msg.obj;
				String result = bundle.getString("result");
				
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					if (jsonObject != null) {
						String nickname = jsonObject.optString("nickname");
						String sex = jsonObject.optString("sex");
						String openid = jsonObject.optString("openid");
						String portrait = jsonObject.optString("headimgurl");
						String city = jsonObject.optString("city");
						
						JSONObject jObjectss=new JSONObject();
						jObjectss.put("nickname", nickname);
						jObjectss.put("gender",sex );
						jObjectss.put("city", city);
						jObjectss.put("figureurl_qq_2", portrait);
						jObjectss.put("openId",openid );
						
						commitUserInfo(openid,jObjectss.toString());
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_elves);
		
		RelativeLayout rel=(RelativeLayout)findViewById(R.id.weixin_login_rel);
		ColorDrawable dw = new ColorDrawable(0x80000000);
		rel.setBackgroundDrawable(dw);
		
		//createLoadingDialog(WXEntryActivity.this);

		mContext = this;

		api = WXAPIFactory.createWXAPI(this, Contants.WEIXIN_APP_ID, false);
		// 注册到微信列表
		api.registerApp(Contants.WEIXIN_APP_ID);
		try {
			api.handleIntent(getIntent(), this);
		} catch (NullPointerException e) {
			e.printStackTrace();
		}

		int type = (Integer) SPUtils.get(this, Contants.LOGIN_TYPE, 0);
		if (0 == type && !isAuth) {
			sendAuth();
		} else {
			finish();
			return;
		}
	}

	/**
	 * 申请授权
	 * 
	 * @title sendAuth
	 * @author liuchengbao
	 * @Description
	 */
	private void sendAuth() {
		SendAuth.Req req = new SendAuth.Req();
		// 用于请求用户信息的作用域
		req.scope = "snsapi_userinfo";
		// 自定义
		req.state = "none";
		api.sendReq(req);
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
	
		
		setIntent(intent);
		
		
		
		api.handleIntent(intent, this);
		
		
	}

	/**
	 * 请求回调接口
	 */
	@Override
	public void onReq(BaseReq req) {
	}

	/**
	 * 请求响应回调接口
	 */
	@Override
	public void onResp(BaseResp resp) {

		switch (resp.errCode) {

		case BaseResp.ErrCode.ERR_AUTH_DENIED:
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			if (ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX == resp.getType())
				ToastUtils.show(mContext, "登录失败", ToastUtils.TOAST_SHORT);
			else {
				ToastUtils.show(mContext, "登录失败", ToastUtils.TOAST_SHORT);
				isAuth = true;
				finish();
			}
			break;
		case BaseResp.ErrCode.ERR_OK:
			switch (resp.getType()) {
			case ConstantsAPI.COMMAND_SENDAUTH:
				isAuth = true;
				// 拿到了微信返回的code,立马再去请求access_token
				String code = ((SendAuth.Resp) resp).code;
				// 就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是get请求
				WXAsyncHttpClient.getResult(mContext, code, handler);
				break;

			case ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX:
				ToastUtils.show(mContext, "微信分享成功", ToastUtils.TOAST_SHORT);

				finish();
				break;
			}
			break;
		}

	}

	protected void commitUserInfo(final String openId,final String response) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();

				
			}

			@Override
			protected Boolean doInBackground(Void... params) {

				if (NetUtils.isConnected(getApplicationContext())) {
					resultMessage = application.qqMobileLogin(openId,"weixin");
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					JSONObject jObject;
					try {
						jObject = new JSONObject(resultMessage);
				
					if (jObject.optString("code").equals("noBind")) {
						Bundle bundle = new Bundle();
						bundle.putString("response", response);
						bundle.putString("openId", openId);
						bundle.putString("flag", "weixin");
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
						e.printStackTrace();
					}
					
					
					
					
					
					
				} else {
					ToastUtils.show(getApplicationContext(), R.string.no_net, ToastUtils.TOAST_SHORT);
				}
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		api.unregisterApp();
	}
	
	/**
	 * @Title:qqMobileLogin
	 * @Description:检查qq登录
	 * @param
	 * @return
	 * @throws
	 */

	protected void weixinLoginCallback(final String response, final String openId) {

		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(mContext)) {
					loginMessage = application.weixinLoginCallback();
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

						} else if (jObject.optString("code").equals(
								"loginSuccess")) {
							JSONObject jsonObject = jObject
									.optJSONObject("data");
							SPUtils.put(mContext, Contants.LOGIN_TYPE, 1);
							SPUtils.put(application, Contants.USER_ID, jsonObject.optString("userId"));
							SPUtils.put(application, Contants.USER_NAME, jsonObject.optString("nickName"));
							SPUtils.put(application, Contants.USER_PORTRAIT, jsonObject.optString("portrait"));
							SPUtils.put(application, Contants.USER_TOKEN, jsonObject.optString("token"));
							SPUtils.put(application, Contants.USER_BALANCE, jsonObject.optString("balance"));
							SPUtils.put(application, Contants.USER_MOBILE, jsonObject.optString("mobile"));
							ToastUtils.show(getApplicationContext(),
									jObject.optString("message"),
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
					ToastUtils.show(getApplicationContext(), R.string.no_net,
							ToastUtils.TOAST_SHORT);
				}

			}
		});

	}
}
