package com.fenqipai.fenqipaiandroid.wxapi;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.SPUtils;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

/**
 * 微信与网络交互
 * 
 * @name WXAsyncHttpClient
 * @author liuchengbao
 * @Description TODO
 * @date 2015年10月19日
 * @modify
 * @modifyDate 2015年10月19日
 * @modifyContent
 */
public class WXAsyncHttpClient {

	/**
	 * 刷新登陆有效期
	 * 
	 * @title getRefreshExpires
	 * @author liuchengbao
	 * @Description TODO
	 * @param mContext
	 * @param refresh_token
	 */

	public static void getRefreshExpires(final Context mContext, final String refresh_token, final Handler mHandler) {
		new Thread() {
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=" + Contants.WEIXIN_APP_ID
						+ "&grant_type=refresh_token&refresh_token=" + refresh_token;
				try {
					JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);
					if (jsonObject != null) {
						String openid = jsonObject.getString("openid").toString().trim();
						String access_token = jsonObject.getString("access_token").toString().trim();
						String expires_in = jsonObject.getString("expires_in").toString().trim();
						String refresh_token = jsonObject.getString("refresh_token").toString().trim();
						/*
						 * "openid":"oAfh3wM1GwMJBP5oqFFHm9yEJe6Y",
						 * "access_token":
						 * "OezXcEiiBSKSxW0eoylIeKTf9g4ZhE5hZGFiRtzTTgcVw8rj9PvwSi9O77A97TrCbRKY-r-esZuab_aj4mwZmakOc8__8RWbt_v5ogEk155g9fSo0DX8BP5TP_9_gg26dxHulFaa4q8VfmmLmbFjZg",
						 * "expires_in":7200, "refresh_token":
						 * "OezXcEiiBSKSxW0eoylIeKTf9g4ZhE5hZGFiRtzTTgcVw8rj9PvwSi9O77A97TrCnSIbb5Bq929mfaTnm-TPc8lLL_WTBfN9T-aGh9cx8fVCaOTScyXkFhPruLfnUzxLZsdgjlu-pyOKkhQgAa-khA",
						 * "scope":"snsapi_base,snsapi_userinfo,"}
						 */

						SPUtils.put(mContext, Contants.WEIXIN_ACCESS_TOKEN, (String) access_token);
						SPUtils.put(mContext, Contants.WEIXIN_PENID, (String) openid);
						SPUtils.put(mContext, Contants.WEIXIN_EXPIRES, (String) expires_in);
						SPUtils.put(mContext, Contants.WEIXIN_REFRESH_TOKEN, (String) refresh_token);

						SPUtils.put(mContext, Contants.LOGIN_STATE, true);
						mHandler.sendEmptyMessageDelayed(0, 2000);
					}
				} catch (Exception e) {
					mHandler.sendEmptyMessageDelayed(0, 2000);
				}
				return;
			};
		}.start();
	}

	/**
	 * 获取用户个人信息
	 * 
	 * @title getUID
	 * @author liuchengbao
	 * @Description TODO
	 * @param openId
	 * @param accessToken
	 * @param mHandler
	 */
	public static void getUID(final String openId, final String accessToken, final Handler mHandler) {
		new Thread() {
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid="
						+ openId;
				try {
					JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);
					if (jsonObject != null) {

						/*
						 * "openid":"oAfh3wM1GwMJBP5oqFFHm9yEJe6Y",
						 * "nickname":"bao", "sex":1, "language":"zh_CN",
						 * "city":"Dalian", "province":"Liaoning",
						 * "country":"CN", "headimgurl":
						 * "http:\/\/wx.qlogo.cn\/mmopen\/OaSj00mzNqhy2Bt2C0icu6o7FfnC098xmEfbicXmJVlrLuufsa42cJicq7ItxH5FdicRFxDSbEttaxm3tkfIpap4XRtSAvh9d0BK\/0",
						 * "privilege":[],
						 * "unionid":"oJ8FQwsVOVSNw0xK7jIp_SlvODFE"}
						 */

						Message msg = mHandler.obtainMessage();
						msg.what = WXEntryActivity.RETURN_NICKNAME_UID;
						Bundle bundle = new Bundle();
						bundle.putString("result", jsonObject.toString());
						msg.obj = bundle;
						mHandler.sendMessage(msg);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return;
			};
		}.start();
	}

	/**
	 * 获取openid accessToken值用于后期操作
	 * 
	 * @title getResult
	 * @author liuchengbao
	 * @Description TODO
	 * @param mContext
	 * @param code
	 *            请求吗
	 * @param mHandler
	 */
	public static void getResult(final Context mContext, final String code, final Handler mHandler) {
		new Thread() {// 开启工作线程进行网络请求
			public void run() {
				String path = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=" + Contants.WEIXIN_APP_ID
						+ "&secret=" + Contants.WEIXIN_SECRET + "&code=" + code + "&grant_type=authorization_code";
				try {
					JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);// 请求https连接并得到json结果
					if (null != jsonObject) {
						// 授权用户唯一标识
						String openid = jsonObject.getString("openid").toString().trim();
						String access_token = jsonObject.getString("access_token").toString().trim();
						String expires_in = jsonObject.getString("expires_in").toString().trim();
						String refresh_token = jsonObject.getString("refresh_token").toString().trim();

						/*
						 * {"access_token":
						 * "OezXcEiiBSKSxW0eoylIeKTf9g4ZhE5hZGFiRtzTTgcVw8rj9PvwSi9O77A97TrCSHSrD1FS-7PhUjy_MeYegDrrluz8-hU_mZmwa7bcX_MYvXlGzr8uPnNPTZhtc-UwMC81WC7whL4BQxaNA8lDrg",
						 * "expires_in":7200, "refresh_token":
						 * "OezXcEiiBSKSxW0eoylIeKTf9g4ZhE5hZGFiRtzTTgcVw8rj9PvwSi9O77A97TrCiarFBnOtYJeDw3hCwKR642jGdgUIcj4h3PuzqNl8iiAJmILylK_6eb1qwG63Ez-WOdshKqKh059JFS72M2opTA",
						 * "openid":"oAfh3wM1GwMJBP5oqFFHm9yEJe6Y",
						 * "scope":"snsapi_userinfo",
						 * "unionid":"oJ8FQwsVOVSNw0xK7jIp_SlvODFE"}
						 */
						SPUtils.put(mContext, Contants.WEIXIN_ACCESS_TOKEN, (String) access_token);
						SPUtils.put(mContext, Contants.WEIXIN_PENID, (String) openid);
						SPUtils.put(mContext, Contants.WEIXIN_EXPIRES, (String) expires_in);
						SPUtils.put(mContext, Contants.WEIXIN_REFRESH_TOKEN, (String) refresh_token);

						Message msg = mHandler.obtainMessage();
						msg.what = WXEntryActivity.RETURN_OPENID_ACCESSTOKEN;
						mHandler.sendMessage(msg);
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return;
			};
		}.start();
	}

	/**
	 * 微信登陆有效期
	 * 
	 * @title getWeiXinExpires
	 * @author liuchengbao
	 * @Description TODO
	 * @param context
	 * @param openId
	 * @param accessToken
	 * @param mHandler
	 */
	public static void getWeiXinExpires(final Context context, final String openId, final String accessToken,
			final Handler mHandler) {
		new Thread() {
			@Override
			public void run() {
				String path = "https://api.weixin.qq.com/sns/auth?access_token=" + accessToken + "&openid=" + openId;
				try {
					JSONObject jsonObject = JsonUtils.initSSLWithHttpClinet(path);
					if (jsonObject != null) {
						String errmsg = jsonObject.getString("errmsg").trim();
						// 有效
						if (errmsg.equals("ok")) {
							SPUtils.put(context, Contants.LOGIN_STATE, true);
							mHandler.sendEmptyMessageDelayed(0, 2000);
						}
						// 无效
						else {
							String refresh_token = (String) SPUtils.get(context, Contants.WEIXIN_REFRESH_TOKEN, "");
							SPUtils.put(context, Contants.LOGIN_STATE, false);
							getRefreshExpires(context, refresh_token, mHandler);
						}
					}
				} catch (Exception e) {
					mHandler.sendEmptyMessageDelayed(0, 2000);
				}
			};
		}.start();
	}

}
