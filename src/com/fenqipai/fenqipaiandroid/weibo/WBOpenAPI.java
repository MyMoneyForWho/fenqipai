package com.fenqipai.fenqipaiandroid.weibo;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.openapi.RefreshTokenApi;

import android.content.Context;
import android.os.Handler;

/**
 * 微博刷新access_token有效期
 * 
 * @name WBOpenAPI
 * @author liuchengbao
 * @date 2015年10月27日
 * @modify
 * @modifyDate 2015年10月27日
 * @modifyContent
 */
public class WBOpenAPI {

	/**
	 * 刷新access_token
	 * 
	 * @title refreshTokenRequest
	 * @author liuchengbao
	 * @Description TODO
	 * @param context
	 * @param refresh_token
	 */
	public static void refreshTokenRequest(final Context context, final Handler mHandler, final String refresh_token) {
		RefreshTokenApi.create(context).refreshToken(Contants.WEIBO_APP_KEY, refresh_token, new RequestListener() {

			@Override
			public void onWeiboException(WeiboException arg0) {
				ToastUtils.show(context, "RefreshToken Result : " + arg0.getMessage(), ToastUtils.TOAST_LONG);
				SPUtils.put(context, Contants.LOGIN_STATE, false);
				mHandler.sendEmptyMessageDelayed(0, 2000);
			}

			@Override
			public void onComplete(String arg0) {
				try {
					JSONObject jsonObject = new JSONObject(arg0);
					if (jsonObject != null) {
						String access_token = jsonObject.getString("access_token").toString().trim();
						String refresh_token = jsonObject.getString("refresh_token").toString().trim();
						String uid = jsonObject.getString("uid").toString().trim();
						String expires_in = jsonObject.getString("expires_in").toString().trim();

						// 保存 Token 到 SharedPreferences
						SPUtils.put(context, Contants.WEIBO_TOKEN, (String) access_token);
						SPUtils.put(context, Contants.WEIBO_REFRESH_TOKEN, (String) refresh_token);
						SPUtils.put(context, Contants.WEIBO_UID, (String) uid);
						SPUtils.put(context, Contants.WEIBO_EXPIRES, String.valueOf(expires_in));

						SPUtils.put(context, Contants.LOGIN_STATE, true);
					}
				} catch (JSONException e) {
					SPUtils.put(context, Contants.LOGIN_STATE, false);
				}
				mHandler.sendEmptyMessageDelayed(0, 2000);

				ToastUtils.show(context, "RefreshToken Result : " + arg0, ToastUtils.TOAST_LONG);

			}
		});
	}

}
