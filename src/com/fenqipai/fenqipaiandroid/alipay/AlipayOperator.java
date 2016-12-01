package com.fenqipai.fenqipaiandroid.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.alipay.sdk.app.PayTask;

public class AlipayOperator {

	private Context mContext;

	private Handler mHandler;

	private String mPrice;

	//商户自定义订单号
	private String mOrderId;

	// 商户PID                            
	public static final String PARTNER = "2088421214028459";
	// 商户收款账号
	public static final String SELLER = "2585692101@qq.com";
	// 商户私钥，pkcs8格式
	public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAMsPlxO0lmXfGks0DL4d+jJI4vUcYexQYNGweE3Qawe49+ox/M5W0zXvAkcg0wsOCVAAuMUoivFsT2JSJDdl4d0aNzPLDR+cjios9N4oZY+/ldlACMGz/GvWq2sm//8GnXm54SIDyM3YTk7JfYGRPVk3usDTCFis7P/Whioz1orXAgMBAAECgYAybT20XA1ZYQ0zjcp4UBN491a9Tf4XZGKW0D8f+9kIguu2EhHY4AD1jNRA+6rNPQAdCQDZnVbb22zfWsy5oSh0gaxh31DtNtMFneE83sAjSvnDnaqz7eYZKjxbuldaUnDnC74sXhiBCRAIAWlQKlFAm8dZ51Govfe721gK6v4o8QJBAOpffnbtfho3SJYm0lqrAT25M4ePGb3czGg+FMKun/HTnv4xFmfhqLnfbcL/rgKIbSO+a8L7UfZpwCoIHbzgSpkCQQDdzGvsT6EiswzMypLj2DDN0cjalnqqktdobQYAmzo20uw659Yv154mTAFbydKusbrwK36oNewettAyUmEK3dbvAkBGFD+wN9sY46bknr8PNUqttg7eXb+Isdi84rM6cxSILoq/3tWgQi09Rr1/LQxO3ZQkt+9o4dTkJUK8Tqj1rOshAkAA2JjIy/z3Xut7lII+8EIvZs93Vf+dyvWbZl8RfYYZU3CnQN8mS5JE3yFaMkjldbQa2m+fQKVn8JaWuo24NuRtAkBpO2xmHlWQOsQULCbdY4QEGh7Zm6AVbXQSFlWZ4u3+zUvfdLnGuuz7mj5ERKjvMk2pxA11+gembT1XqcrVrdb4";
	// 支付宝公钥
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDLD5cTtJZl3xpLNAy+HfoySOL1HGHsUGDRsHhN0GsHuPfqMfzOVtM17wJHINMLDglQALjFKIrxbE9iUiQ3ZeHdGjczyw0fnI4qLPTeKGWPv5XZQAjBs/xr1qtrJv//Bp15ueEiA8jN2E5OyX2BkT1ZN7rA0whYrOz/1oYqM9aK1wIDAQAB";
	//MD5-KEY
	public static final String MD5_KEY = "8bfxndfggqgk97aj7cskul3250gfbdyq";

	private static final int SDK_PAY_FLAG = 1;

//	public AlipayOperator(Context mContext, Handler mHandler, String mPrice) {
//		super();
//		this.mContext = mContext;
//		this.mHandler = mHandler;
//		this.mPrice = mPrice;
//	}

	public AlipayOperator(Context mContext, Handler mHandler, String mPrice,
			String mOrderId) {
		super();
		this.mContext = mContext;
		this.mHandler = mHandler;
		this.mPrice = mPrice;
		this.mOrderId = mOrderId;
	}

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay( ) {
		// 订单
		String orderInfo = getOrderInfo("分期拍订单", "分期拍订单", mPrice);
		// 对订单做RSA 签名
		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 完整的符合支付宝参数规范的订单信息
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
				+ getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask((Activity)mContext);
				Log.e("qf", "------"+payInfo);
				// 调用支付接口，获取支付结果
				String result = alipay.pay(payInfo,true);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		// 必须异步调用
		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}
	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String price) {

		// 签约合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 签约卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		orderInfo += "&notify_url=" + "\"" + "http://www.fenqipai.net.cn/ports/aliPayBack"
				+ "\"";

		// 服务接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
		// orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空	
		orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
		// orderInfo += "&paymethod=\"expressGateway\"";

		return orderInfo;
	}

	/**
	 * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
	 * 
	 */
	public String getOutTradeNo() {
		return mOrderId;
	}

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content 待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}
}
