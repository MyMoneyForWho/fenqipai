package com.fenqipai.fenqipaiandroid.wxapi;

import java.io.StringReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import com.fenqipai.fenqipaiandroid.common.Contants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;

public class WeiXinPayOperator {

	Context context;

	PayReq req;

	final IWXAPI msgApi;

	// 订单号
	public static String mOrderId;

	// 需要支付的钱数
	public static String mTotalFee;

	Map<String, String> resultunifiedorder;

	public WeiXinPayOperator(Context context, String orderId, String totalFee) {
		super();
		this.context = context;
		mOrderId = orderId;
		this.mTotalFee = ((int) (Double.parseDouble(totalFee) * 100)) + "";
		req = new PayReq();
		msgApi = WXAPIFactory.createWXAPI(context, null);
		msgApi.registerApp(Contants.WEIXIN_PAY_PENID);
	}

	public void pay() {
		GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
		getPrepayId.execute();
	}

	private class GetPrepayIdTask extends
			AsyncTask<Void, Void, Map<String, String>> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, null, "正在获取预支付订单...");
			dialog.setCanceledOnTouchOutside(false);
		}

		@Override
		protected void onPostExecute(Map<String, String> result) {
			if (dialog != null) {
				dialog.dismiss();
			}

			resultunifiedorder = result;

			genPayReq();

			sendPayReq();

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected Map<String, String> doInBackground(Void... params) {

			String url = String
					.format("https://api.mch.weixin.qq.com/pay/unifiedorder");

			String entity = genProductArgs();


			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);

			Map<String, String> xml = decodeXml(content);

			return xml;
		}
	}

	private String genProductArgs() {
		StringBuffer xml = new StringBuffer();

		try {
			String nonceStr = genNonceStr();

			xml.append("</xml>");
			List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
			packageParams
					.add(new BasicNameValuePair("appid", Contants.WEIXIN_APP_ID));
			packageParams.add(new BasicNameValuePair("body", "分期拍支付"));
			packageParams
					.add(new BasicNameValuePair("mch_id", Contants.WEIXIN_APP_ID));
			packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));
			packageParams.add(new BasicNameValuePair("notify_url",
					"http://www.yinle.cc/wxpay/ResultNotifyPage.aspx"));
			packageParams.add(new BasicNameValuePair("out_trade_no",
					genOutTradNo()));
			packageParams.add(new BasicNameValuePair("spbill_create_ip",
					"127.0.0.1"));
			packageParams.add(new BasicNameValuePair("total_fee", mTotalFee));
			packageParams.add(new BasicNameValuePair("trade_type", "APP"));

			String sign = genPackageSign(packageParams);

			packageParams.add(new BasicNameValuePair("sign", sign));

			String xmlstring = toXml(packageParams);

			// return xmlstring;
			return new String(xmlstring.toString().getBytes(), "ISO8859-1");

		} catch (Exception e) {
			return null;
		}

	}

	public Map<String, String> decodeXml(String content) {

		try {
			Map<String, String> xml = new HashMap<String, String>();
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(new StringReader(content));
			int event = parser.getEventType();
			while (event != XmlPullParser.END_DOCUMENT) {

				String nodeName = parser.getName();

				switch (event) {

				case XmlPullParser.START_DOCUMENT:

					break;

				case XmlPullParser.START_TAG:

					if ("xml".equals(nodeName) == false) {
						//实例化student对象
						xml.put(nodeName, parser.nextText());
					}
					break;

				case XmlPullParser.END_TAG:
					break;
				}
				event = parser.next();
			}

			return xml;
		} catch (Exception e) {
		}
		return null;

	}

	private String genNonceStr() {
		Random random = new Random();
		return MD5.getMessageDigest(String.valueOf(random.nextInt(10000))
				.getBytes());
	}

	private String genOutTradNo() {
		return mOrderId;
	}

	/**
	 * 生成签名
	 */

	private String genPackageSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Contants.API_KEY);

		String packageSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return packageSign;
	}

	private String toXml(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();
		sb.append("<xml>");
		for (int i = 0; i < params.size(); i++) {
			sb.append("<" + params.get(i).getName() + ">");

			sb.append(params.get(i).getValue());
			sb.append("</" + params.get(i).getName() + ">");
		}
		sb.append("</xml>");

		return sb.toString();
	}

	private void sendPayReq() {

		msgApi.registerApp(Contants.WEIXIN_APP_ID);
		msgApi.sendReq(req);
	}

	private void genPayReq() {

		req.appId = Contants.WEIXIN_APP_ID;
		req.partnerId = Contants.MCH_ID;
		req.prepayId = resultunifiedorder.get("prepay_id");
		req.packageValue = "Sign=WXPay";
		req.nonceStr = genNonceStr();
		req.timeStamp = String.valueOf(genTimeStamp());

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

		req.sign = genAppSign(signParams);

	}

	private long genTimeStamp() {
		return System.currentTimeMillis() / 1000;
	}

	private String genAppSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < params.size(); i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append("key=");
		sb.append(Contants.API_KEY);

		String appSign = MD5.getMessageDigest(sb.toString().getBytes())
				.toUpperCase();
		return appSign;
	}

}
