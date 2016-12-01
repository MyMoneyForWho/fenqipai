package com.fenqipai.fenqipaiandroid.net;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.httpclient.params.HttpMethodParams;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.model.BankList;
import com.fenqipai.fenqipaiandroid.model.Commodity;
import com.fenqipai.fenqipaiandroid.model.IndexNewsList;
import com.fenqipai.fenqipaiandroid.model.InstallmentCar;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;
import com.fenqipai.fenqipaiandroid.model.MoneyChangeRecord;
import com.fenqipai.fenqipaiandroid.model.Moneyrecord;
import com.fenqipai.fenqipaiandroid.model.MyLocation;
import com.fenqipai.fenqipaiandroid.model.NewsList;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.model.PayInfo;
import com.fenqipai.fenqipaiandroid.model.PaymentSaleLoan;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;
import com.fenqipai.fenqipaiandroid.model.SingleSaleCarDetails;
import com.fenqipai.fenqipaiandroid.model.StagingBill;
import com.fenqipai.fenqipaiandroid.model.StagingBillDetails;
import com.fenqipai.fenqipaiandroid.model.UserInfo;
import com.fenqipai.fenqipaiandroid.util.AppUtils;

import android.content.pm.PackageInfo;

/**
 * 访问网络公共类
 * 
 * @name NetClient
 * @author liuchengbao
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class NetClient {

	// utf-8编码
	public static final String UTF_8 = "UTF-8";

	// 连接超时时间
	private static final int TIMEOUT_CONNECTION = 2000;

	// 重新操作次数
	private static final int RETRY_TIME = 3;

	// 网络错误提示
	private static final String CONNECT_ERROR = "网络链接错误:";

	private static String userAgent;

	private static HttpState state = null;

	private static String getUserAgent(BaseApplication application) {
		if (userAgent == null || userAgent.equals("")) {
			StringBuffer sb = new StringBuffer("qingyang");

			PackageInfo packageInfo = AppUtils.getVersion(application);

			// app版本
			sb.append('/' + packageInfo.versionName + '_' + packageInfo.versionCode);
			// andorid 平台
			sb.append("/Android");
			// andorid 平台
			sb.append("/FenQiPai");
			// 手机系统版本
			sb.append("/" + android.os.Build.VERSION.RELEASE);
			// 手机型号
			sb.append("/" + android.os.Build.MODEL);

			userAgent = sb.toString();
		}
		return userAgent;
	}

	/**
	 * 获取HttpClient
	 * 
	 * @title getHttpClient
	 * @author liuchengbao
	 * @return
	 */
	private static HttpClient getHttpClient() {
		HttpClient httpClient = new HttpClient();

		// 设置默认的超时重试处理策略
		httpClient.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
		// 设置连接超时时间
		httpClient.getHttpConnectionManager().getParams().setConnectionTimeout(TIMEOUT_CONNECTION);
		httpClient.getParams().setContentCharset(UTF_8);
		return httpClient;
	}

	/**
	 * 获取get方法
	 * 
	 * @title getHttpGet
	 * @author liuchengbao
	 * @param url
	 * @param userAgent
	 * @return
	 */
	private static GetMethod getHttpGet(String url, String userAgent) {
		GetMethod httpGet = new GetMethod(url);
		// 设置请求超时时间
		httpGet.getParams().setSoTimeout(TIMEOUT_CONNECTION);
		httpGet.setRequestHeader("Host", URL.URL_HOST);
		httpGet.setRequestHeader("Connection", "Keep_Alive");
		httpGet.setRequestHeader("User-Agent", userAgent);
		return httpGet;
	}

	/**
	 * 获取post方法
	 * 
	 * @title getHttpPost
	 * @author liuchengbao
	 * @param url
	 * @param userAgent
	 * @return
	 */
	private static PostMethod getHttpPost(String url, String userAgent) {
		PostMethod httpPost = new PostMethod(url);
		// 设置请求超时时间
		httpPost.getParams().setSoTimeout(TIMEOUT_CONNECTION);
		httpPost.setRequestHeader("Host", URL.URL_HOST);
		httpPost.setRequestHeader("Connection", "Keep_Alive");
		httpPost.setRequestHeader("User-Agent", userAgent);
		return httpPost;
	}

	/**
	 * 公共用的get方法
	 * 
	 * @param application
	 * @param url
	 * @return
	 * @throws AppException
	 */
	private static String http_get(BaseApplication application, String url) {

		String userAgent = getUserAgent(application);

		HttpClient httpClient = null;

		GetMethod httpGet = null;

		String responseBody = "";

		int time = 0;

		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, userAgent);

				if (state != null)
					httpClient.setState(state);

				int statusCode = httpClient.executeMethod(httpGet);

				if (state == null)
					state = httpClient.getState();

				if (statusCode != HttpStatus.SC_OK) {
					return CONNECT_ERROR + statusCode;
				}

				responseBody = httpGet.getResponseBodyAsString();

				break;

			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		return responseBody;
	}

	/**
	 * 公用的post方法
	 * 
	 * @param application
	 * @param url
	 * @param params
	 * @param files
	 * @return
	 * @throws AppException
	 */
	private static String http_post(BaseApplication application, String url, Map<String, Object> params,
			Map<String, File> files) {


		String userAgent = getUserAgent(application);

		HttpClient httpClient = null;

		PostMethod httpPost = null;

		// post表单参数处理
		int length = (params == null ? 0 : params.size()) + (files == null ? 0 : files.size());

		Part[] parts = new Part[length];

		int i = 0;

		if (params != null) {
			for (String name : params.keySet()) {
				parts[i++] = new StringPart(name, String.valueOf(params.get(name)), UTF_8);
			}
		}

		if (files != null) {
			for (String fileName : files.keySet()) {
				try {
					parts[i++] = new FilePart(fileName, files.get(fileName));
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			}
		}

		String responseBody = "";

		int time = 0;

		do {
			try {
				httpClient = getHttpClient();

				httpPost = getHttpPost(url, userAgent);

				httpPost.setRequestEntity(new MultipartRequestEntity(parts, httpPost.getParams()));

				if (state != null)
					httpClient.setState(state);

				int statusCode = httpClient.executeMethod(httpPost);

				if (state == null)
					state = httpClient.getState();

				if (statusCode != HttpStatus.SC_OK) {
					return "网络连接超时";
				}
				responseBody = httpPost.getResponseBodyAsString();

				break;
			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生致命的异常，可能是协议不对或者返回的内容有问题
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				// 发生网络异常
				e.printStackTrace();
			} finally {
				// 释放连接
				httpPost.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);

		return responseBody;
	}

	public static String http_get_image(BaseApplication application, String url) {
		String userAgent = getUserAgent(application);

		HttpClient httpClient = null;

		GetMethod httpGet = null;

		int time = 0;
		String path = "";
		File storeFile = null;

		do {
			try {
				httpClient = getHttpClient();
				httpGet = getHttpGet(url, userAgent);

				if (state != null)
					httpClient.setState(state);

				httpClient.executeMethod(httpGet);

				if (state == null)
					state = httpClient.getState();

				InputStream responseBody = httpGet.getResponseBodyAsStream();

				File dirFile = new File(Contants.DOWN_PATH);
				if (!dirFile.exists()) {
					dirFile.mkdirs();
				}
				path = Contants.DOWN_PATH + System.currentTimeMillis() + ".jpg";
				storeFile = new File(path);

				// 得到网络资源的字节数组,并写入文件
				FileOutputStream output = new FileOutputStream(storeFile);
				int bufferSize = 1024;
				byte[] buffer = new byte[bufferSize];
				int len = 0;
				while ((len = responseBody.read(buffer)) != -1) {
					output.write(buffer, 0, len);
				}
				output.close();
				break;

			} catch (HttpException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} catch (IOException e) {
				time++;
				if (time < RETRY_TIME) {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
					}
					continue;
				}
				e.printStackTrace();
			} finally {
				// 释放连接
				httpGet.releaseConnection();
				httpClient = null;
			}
		} while (time < RETRY_TIME);
		return storeFile.getAbsolutePath();
	}

	/**
	 * 手机号快捷登录
	 * 
	 * @title phoneLoginCheck
	 * @author liuchengbao
	 * @param application
	 * @param phone
	 * @param password
	 * @return
	 */
	public static String loginMobile(BaseApplication application, String mobile, String messageCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", mobile);
		params.put("messageCode", messageCode);
		return http_post(application, URL.getURL(URL.LOGIN_MOBILE), params, null);
	}

	/**
	 * 手机号登陆验证
	 * 
	 * @title phoneLoginCheck
	 * @author liuchengbao
	 * @param application
	 * @param phone
	 * @param password
	 * @return
	 */
	public static String phoneLoginCheck(BaseApplication application, String phone, String password,
			String captchaCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", phone);
		params.put("password", password);
		params.put("captchaCode", captchaCode);// captchaCode
		return http_post(application, URL.getURL(URL.LOGIN_IN), params, null);
	}

	/**
	 * 保存bug信息
	 * 
	 * @title saveBugInfo
	 * @author zhaoqingyang
	 * @param baseApplication
	 * @param result
	 * @return
	 */
	public static String saveBugInfo(BaseApplication application, String result) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", application.getUserId());
		params.put("bugDescription", result);
		params.put("versionCode", AppUtils.getVersion(application).versionCode);
		params.put("versionName", AppUtils.getVersion(application).versionName);
		params.put("phoneModel", android.os.Build.MODEL);
		return http_post(application, URL.getURL(URL.SAVE_BUG_URL), params, null);
	}

	/**
	 * 获取今日江湖求助
	 * 
	 * @title getJRJHHelp
	 * @author liuchengbao
	 * @param application
	 */
	public static void getCommodityList(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("userId", application.getUserId() + "");
		Commodity.parse(application, http_post(application, URL.getURL(URL.GET_COMMODITY_LIST), params, null), true);
	}

	/**
	 * 获取今日江湖首页轮播图
	 * 
	 * @title getJRJHNotice
	 * @author liuchengbao
	 * @param application
	 */
	public static String getJRJHNotice(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();
		return http_post(application, URL.getURL(URL.GET_JRJH_NOTICE), params, null);
	}

	/**
	 * 发送短信
	 * 
	 * @title sendSMS
	 * @author liuchengbao
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String sendSMS(BaseApplication application, String phone, String captchaCode, String actionType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile", phone);
		params.put("captchaCode", captchaCode);
		params.put("actionType", actionType);

		return http_post(application, URL.getURL(URL.SEND_SMS), params, null);
	}

	/**
	 * 注册
	 * 
	 * @title sendSMS
	 * @author liuchengbao
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String register(BaseApplication application, String account, String phone, String password,
			String messageCode) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("mobile", phone);
		params.put("password", password);
		params.put("messageCode", messageCode);

		return http_post(application, URL.getURL(URL.REGIST), params, null);
	}

	/**
	 * qq验证是否注册
	 * 
	 * @title sendSMS
	 * @author qqMobileLogin
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String qqMobileLogin(BaseApplication application, String openId, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId ", openId);
		params.put("type ", type);
		return http_post(application, URL.getURL(URL.QQ_MOBILE_LOGIN), params, null);
	}

	/**
	 * 微信验证是否注册
	 * 
	 * @title sendSMS
	 * @author qqMobileLogin
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String weixinLoginCallback(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();

		return http_post(application, URL.getURL(URL.WEIXIN_MOBILE_LOGIN), params, null);
	}

	/**
	 * 忘記密碼
	 * 
	 * @title sendSMS
	 * @author qqMobileLogin
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String forgetPassword(BaseApplication application, String mobile, String password,
			String messageCode,String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("mobile ", mobile);
		params.put("password ", password);
		params.put("messageCode ", messageCode);
		params.put("type", type);
		
		return http_post(application, URL.getURL(URL.FORGET_PASSWORD), params, null);
	}

	/**
	 * 注册
	 * 
	 * @title sendSMS
	 * @author liuchengbao
	 * @param application
	 * @param phone
	 * @return
	 */
	public static String qqMobileRegist(BaseApplication application, String openId, String mobile, String nickName,
			String portrait, String messageCode, String sex, String type) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openId", openId);
		params.put("mobile", mobile);
		params.put("nickName ", nickName);
		params.put("portrait", portrait);
		params.put("messageCode", messageCode);
		params.put("sex", messageCode);
		params.put("type", type);
		return http_post(application, URL.getURL(URL.QQ_MOBILE_REGIST), params, null);
	}

	/**
	 * 获取拍卖列表
	 * 
	 * @title auctionList
	 * @author liuchengbao
	 * @return
	 */
	public static void auctionList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize, String auctionType) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("auctionType", auctionType);

		SaleCar.parse(application, http_post(application, URL.getURL(URL.AUCTION_LIST), params, null), isLoad);
	}

	/**
	 * 获取某个拍卖的车辆详细信息
	 * 
	 * @param auctionId
	 *            - 拍卖id
	 */
	public static void auctionInfo(BaseApplication application, String auctionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auctionId", auctionId);

		SingleCarDetails.parse(application, http_post(application, URL.getURL(URL.AUCTION_INFO), params, null));

	}

	/**
	 * 获取某个分期的车辆详细信息
	 * 
	 * @param saleId
	 *            - 分期id
	 */
	public static void saleInfo(BaseApplication application, String saleId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("saleId", saleId);

		SingleSaleCarDetails.parse(application, http_post(application, URL.getURL(URL.SALE_INFO), params, null));

	}

	/**
	 * 获取某个拍卖的车辆图片
	 * 
	 * @param auctionId
	 *            - 拍卖id
	 */
	public static String getAuctionImgList(BaseApplication application, String auctionId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auctionId", auctionId);

		return http_post(application, URL.getURL(URL.AUCTION_IMG_LIST), params, null);

	}

	/**
	 * 获取分期车辆列表
	 * 
	 * @title installmentList
	 * @author hunaixin
	 * @param application
	 * @param phone
	 * @return
	 */
	public static void installmentList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);

		InstallmentCar.parse(application, http_post(application, URL.getURL(URL.SALE_LIST), params, null), isLoad);
	}

	/**
	 * 获取银联支付tn
	 */
	public static String getYLpayTn(BaseApplication application, String money,String type, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("price", money);
		params.put("token", token);
		params.put("type", type);
		return http_post(application, URL.getURL(URL.YIN_LIAN_TN), params, null);

	}

	/**
	 * 拍卖出价
	 */
	public static String auctionRaisePrice(BaseApplication application, String auctionId, String money, String token,
			String userId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("auctionId", auctionId);
		params.put("money", money);
		params.put("token", token);
		return http_post(application, URL.getURL(URL.AUCTION_RAISE_PRICE), params, null);

	}

	/**
	 * 获取订单列表
	 * 
	 * @author hunaixin
	 */
	public static void getOrderList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		OrderList.parse(application, http_post(application, URL.getURL(URL.ORDER_LIST), params, null), isLoad);
	}

	/**
	 * 获取个人信息
	 * 
	 * @title getNbuserInfoResult
	 * @return
	 */
	public static void getNbuserInfoResult(BaseApplication application, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		UserInfo.parse(application, http_post(application, URL.getURL(URL.GET_USERINFO), params, null));
	}

	/**
	 * 获取订单详细
	 * 
	 * @author hunaixin
	 */
	public static void getOrderInfo(BaseApplication application, String orderId, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("token", token);

		OrderInfo.parse(application, http_post(application, URL.getURL(URL.OREDER_INFO), params, null));
	}

	/**
	 * 获取支持银行列表
	 * 
	 * @author hunaixin
	 */
	public static void getBankList(BaseApplication application, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);

		BankList.parse(application, http_post(application, URL.getURL(URL.BANK_LIST), params, null));
	}

	/**
	 * 会员修改密码
	 * 
	 * @author WangZhonghan
	 */

	public static String updateNbuserPassword(BaseApplication application, String passwordType, String oldPassword,
			String newPassword, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("passwordType", passwordType);
		params.put("oldPassword", oldPassword);
		params.put("newPassword", newPassword);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.UPDATE_PASSWORD), params, null);

	}

	/**
	 * 修改个人信息
	 * 
	 * @author WangZhonghan
	 */

	public static String updateNbuser(BaseApplication application, String account, String nickName, String trueName,
			String sex, String birthday, String email, String card, String address, String job,String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("nickName", nickName);
		params.put("trueName", trueName);
		params.put("sex", sex);
		params.put("birthday", birthday);
		params.put("email", email);
		params.put("card", card);
		params.put("address", address);
		params.put("job", job);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.UPDATE_INFORMATION), params, null);

	}

	/**
	 * 验证支付密码
	 */
	public static String payPasswordCheck(BaseApplication application, String payPassword, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payPassword", payPassword);
		params.put("token", token);
		return http_post(application, URL.getURL(URL.PASSWORD_CHECK), params, null);

	}

	/**
	 * 添加银行卡
	 */
	public static String insertCarNo(BaseApplication application, String no, String bankId, String cardUserName,
			String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("NO", no);
		params.put("bankId", bankId);
		params.put("cardUserName", cardUserName);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.INSERT_CARD), params, null);
	}

	/**
	 * 修改用户头像
	 */
	public static String updateUserHeadImg(BaseApplication application,String account, String nickName, String trueName,
			String sex, String birthday, String email, String card, String address, String job, Map<String, File> files, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("account", account);
		params.put("nickName", nickName);
		params.put("trueName", trueName);
		params.put("sex", sex);
		params.put("birthday", birthday);
		params.put("email", email);
		params.put("card", card);
		params.put("address", address);
		params.put("job", job);
		params.put("token", token);
		return http_post(application, URL.getURL(URL.UPDATE_USER_HEADIMG), params, files);
	}

	/**
	 * 绑定银行卡列表
	 */
	public static void bankCardList(BaseApplication application, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);

		BankCardList.parse(application, http_post(application, URL.getURL(URL.CARDNO_LIST), params, null));

	}

	/**
	 * 解除绑定银行卡
	 */
	public static String deleteCarNo(BaseApplication application, String cardNoId, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cardNoId", cardNoId);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.DELETE_CARD), params, null);

	}

	/**
	 * 销售方案确认查询列表
	 */
	public static void paymentSaleLoan(BaseApplication application, String orderId, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("token", token);

		PaymentSaleLoan.parse(application, http_post(application, URL.getURL(URL.SALE_LOANLIST), params, null));

	}

	/**
	 * 分期业务获取对应信息列表
	 */
	public static void loanAlgorithmList(BaseApplication application, String cid, String type, String loanId,
			String orderId, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cid", cid);
		params.put("type", type);
		params.put("loanId", loanId);
		params.put("orderId", orderId);
		params.put("token", token);

		LoanAlgorithmList.parse(application, http_post(application, URL.getURL(URL.LOAN_LIST), params, null));

	}

	/**
	 * 预约购车
	 */

	public static String buyApply(BaseApplication application, String saleId, String mobile, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("saleId ", saleId);
		params.put("mobile", mobile);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.BUY_APPLY), params, null);

	}
	
	/**
	 * 预约购车
	 */
	public static String inserServicePhone(BaseApplication application, String serviceType, String mobile, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("serviceType ", serviceType);
		params.put("mobile", mobile);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.INSERT_SERVICE_PHONE), params, null);

	}


	/**
	 * 银联支付回调
	 */

	public static String unionpayMobileFront(BaseApplication application, String payId, String jsonData,String type,
			String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payId ", payId);
		params.put("jsonData", jsonData);
		params.put("token", token);
		params.put("type ", type);
		return http_post(application, URL.getURL(URL.UNIONPAY_MOBILE_FRONT), params, null);
	}

	/**
	 * 根据支付单获取银联订单号
	 */
	public static String unionpayMobileCreate(BaseApplication application, String payId,String type, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payId ", payId);
		params.put("type ", type);
		params.put("token", token);
		return http_post(application, URL.getURL(URL.UNIONPAY_MOBILE_CREATE), params, null);
	}

	/**
	 * 获取保证金记录
	 * 
	 * @author hunaixin
	 */
	public static void getBailList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		Moneyrecord.parse(application, http_post(application, URL.getURL(URL.BAIL_LIST), params, null), isLoad);
	}

	/**
	 * 获取服务器时间
	 */
	public static String getClientTime(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();

		return http_post(application, URL.getURL(URL.CLIENT_TIME), params, null);

	}

	/**
	 * 获取支付信息列表
	 * 
	 * @author hunaixin
	 */
	public static void getPayInfo(BaseApplication application, String payStatus, Long updateTime, Boolean isLoad,
			int curRows, int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payStatus", payStatus);
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		PayInfo.parse(application, http_post(application, URL.getURL(URL.PAY_LIST), params, null), isLoad);
	}

	/**
	 * 查询余额
	 */
	public static String getMyBalance(BaseApplication application, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		return http_post(application, URL.getURL(URL.GET_MY_BALANCE), params, null);

	}

	/**
	 * 获取分期账单列表
	 * 
	 * @author hunaixin
	 */
	public static void getRepaymentOrderList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		StagingBill.parse(application, http_post(application, URL.getURL(URL.GET_REPAYMENT_ORDERLIST), params, null),
				isLoad);
	}

	/**
	 * 新闻与轮播图列表
	 */
	public static void newsList(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();

		IndexNewsList.parse(application, http_post(application, URL.getURL(URL.NEWS_LIST), params, null));

	}

	/**
	 * 银联支付tn码 orderId - 订单Id loanId - 分期业务Id loanAlgorithmId - 分期期限Id
	 * actionType - 支付单类型:fullAmount全款, downPayment 首付, staging 分期
	 * repaymentIdArray[] - 分期期限Id数组
	 */
	public static String createPay(BaseApplication application, String orderId, String actionType,
			String repaymentIdArray, String loanId, String loanAlgorithmId,String type, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("loanId ", loanId);
		params.put("loanAlgorithmId ", loanAlgorithmId);
		params.put("actionType", actionType);
		params.put("repaymentIdArray", repaymentIdArray);
		params.put("type", type);
		params.put("token", token);
		return http_post(application, URL.getURL(URL.CREATE_PAY), params, null);

	}

	/**
	 * 更多新闻列表
	 */
	public static void getNewsList(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);

		NewsList.parse(application, http_post(application, URL.getURL(URL.GET_NEWS_LIST), params, null), isLoad);
	}

	/**
	 * 取消支付信息
	 */
	public static String closePay(BaseApplication application, String payId, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("payId", payId);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.DELETE_PAY), params, null);

	}

	/**
	 * 获取分期账单列表
	 * 
	 * @author hunaixin
	 */
	public static void getOrderRepaymentList(BaseApplication application, String orderId, String orderRepaymentStatus,
			Long updateTime, Boolean isLoad, int curRows, int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("orderId", orderId);
		params.put("orderRepaymentStatus", orderRepaymentStatus);
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		StagingBillDetails.parse(application,
				http_post(application, URL.getURL(URL.GET_ORDER_REPAYMENT_LIST), params, null), isLoad);
	}

	/**
	 * 获取活动位置列表
	 * 
	 * @author hunaixin
	 */
	public static void seeCarPoint(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();
		MyLocation.parse(application, http_post(application, URL.getURL(URL.SEE_CAR_POINT), params, null));
	}

	/**
	 * 保证金提现
	 */
	public static String withdraw(BaseApplication application, String name, String cardNoId, String cashPrice,
			String password, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("cardNoId", cardNoId);
		params.put("cashPrice", cashPrice);
		params.put("password", password);
		params.put("token", token);

		return http_post(application, URL.getURL(URL.WITH_DRAW), params, null);

	}

	/**
	 * 获取维修与保养列表webviewUrl
	 */
	public static String getMRepairListUrl(BaseApplication application) {
		Map<String, Object> params = new HashMap<String, Object>();

		return http_post(application, URL.getURL(URL.GET_MREPAIR_LIST_URL), params, null);

	}

	/**
	 * 获取资金记录记录
	 * 
	 * @author hunaixin
	 */
	public static void getMoneyChangeRecord(BaseApplication application, Long updateTime, Boolean isLoad, int curRows,
			int pageSize, String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("updateTime", updateTime);
		params.put("isLoad", isLoad);
		params.put("curRows", curRows);
		params.put("pageSize", pageSize);
		params.put("token", token);
		MoneyChangeRecord.parse(application, http_post(application, URL.getURL(URL.BOOK_LIST), params, null), isLoad);
	}
}
