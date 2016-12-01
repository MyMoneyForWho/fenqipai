package com.fenqipai.fenqipaiandroid;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.alipay.AlipayOperator;
import com.fenqipai.fenqipaiandroid.alipay.PayResult;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.unionpay.UPPayAssistEx;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @Description:充值页面
 * @author qianyuhang
 * @date 2016-8-2 下午3:12:38
 */
public class RechargeActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.recharge_titleBar)
	private TitleBarView titleBarView;

	// 余额
	@ViewInject(R.id.up_to_money_num)
	private EditText moneyBtn;

	// 确定充值
	@ViewInject(R.id.top_up)
	private Button topUp;

	// 微信充值
	@ViewInject(R.id.pay_wechat)
	private LinearLayout payWxapi;

	// 支付宝充值
	@ViewInject(R.id.pay_zfb)
	private LinearLayout payZfb;

	// 银联充值
	@ViewInject(R.id.unionpay)
	private LinearLayout payYl;

	// 微信充值圆点
	@ViewInject(R.id.pay_wechat_choose)
	private ImageView wxCheck;

	// 支付宝充值圆点
	@ViewInject(R.id.pay_zfb_choose)
	private ImageView zfCheck;

	// 银联充值圆点
	@ViewInject(R.id.pay_unionpay_choose)
	private ImageView ylCheck;

	// 充值类型
	@ViewInject(R.id.input_type)
	private TextView inputType;

	// 1 银联 2 支付宝 3微信
	private int flag = 2;

	public String resultMessage;

	private static final int SDK_PAY_FLAG = 1;

	public static final String LOG_TAG = "PayDemo";

	// 银联支付获取tn时，获取号
	public String payId = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recharge);

		// 注入view和事件
		ViewUtils.inject(this);

		initViews();

		initEvents();

	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initViews() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.recharge_activity_title);


		moneyBtn.setOnFocusChangeListener(myListener);

		if (getIntent().getStringExtra("inputType").equals("RecognizanceActivity")) {
			inputType.setText(R.string.get_in_bail);
		}

	};

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initEvents() {

		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		payYl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				ylCheck.setImageResource(R.drawable.btn_choose_on);
				wxCheck.setImageResource(R.drawable.btn_choose_off);
				zfCheck.setImageResource(R.drawable.btn_choose_off);
				flag = 1;

			}
		});
		payZfb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wxCheck.setImageResource(R.drawable.btn_choose_off);
				zfCheck.setImageResource(R.drawable.btn_choose_on);
				ylCheck.setImageResource(R.drawable.btn_choose_off);
				flag = 2;
			}
		});

		payWxapi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wxCheck.setImageResource(R.drawable.btn_choose_on);
				zfCheck.setImageResource(R.drawable.btn_choose_off);
				ylCheck.setImageResource(R.drawable.btn_choose_off);
				flag = 3;
			}
		});
		topUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String money = moneyBtn.getText().toString();
				try {
				if (TextUtils.isEmpty(money)) {
					ToastUtils.show(getApplicationContext(), "充值金额不能为空", ToastUtils.TOAST_SHORT);
					return;
				}else if (!isMoney(money)) {
					ToastUtils.show(getApplicationContext(), "充值金额整百起存", ToastUtils.TOAST_SHORT);
					return;
				} else {
						// 将订单号加上时间戳，保持唯一性，也是out_trade_no
						if (flag == 1) {// 1 银联支付.

//							ToastUtils.show(getApplicationContext(), "无法支付", ToastUtils.TOAST_SHORT);
//							 topUp.setClickable(false);
//							 getYlpayTn(money,"UnionPay");
						} else if (flag == 2) {// 2 支付宝
							 topUp.setClickable(false);
							getYlpayTn(money, "AliPay");

						} else if (flag == 3) {// 3微信

//							getYlpayTn(money, "AliPay");

							// //支付方式:微信
							// WXPayEntryActivity.isFromReCharge =
							// false;//添加标志位，标志当前是付款操作
							// WeiXinPayOperator operator = new
							// WeiXinPayOperator(PaymentActivity.this, orderId,
							// mAccountDiff);
							// operator.pay();
						}
					}
				} catch (Exception e) {
					ToastUtils.show(getApplicationContext(), "充值金额不正确", ToastUtils.TOAST_SHORT);
					return;
				}
				}

		});

	};

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultStatus = payResult.getResultStatus();
				String resultInfo = payResult.getResult();
//				String orderId = payResult.getOutTradeNumber();

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(RechargeActivity.this, R.string.pay_success, Toast.LENGTH_SHORT).show();

					// 支付成功后，修改订单
					unionpayMobileFront(payId, resultInfo, "AliPay");

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					if (TextUtils.equals(resultStatus, "8000")) {
						// “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
						Toast.makeText(application, R.string.confirming_pay_result, Toast.LENGTH_SHORT).show();

					} else {
						// 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
						Toast.makeText(application, R.string.pay_failed, Toast.LENGTH_SHORT).show();
					}
				}
				break;
			}
			}
		};
	};

	/**
	 * @Description:获取银联支付tn 
	 * @author hunaixin
	 * @parame money, type（AliPay,UnionPay,WePay）
	 * @return resultMessage
	 */
	public void getYlpayTn(final String money, final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				if (NetUtils.isConnected(getApplicationContext())) {
					resultMessage = application.getYLpatTn(money, type);
					return true;
				}
				return false;

			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				topUp.setClickable(true);
				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(resultMessage);
						JSONObject jObject = jsonObject.optJSONObject("data");
						
						if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
							payId = jObject.optString("payId");
							if (type.equals("UnionPay")) {// 银联
								doStartUnionPayPlugin(RechargeActivity.this, jObject.optString("tn"),
										Contants.YINLIAN_MODE);
							} else if (type.equals("AliPay")) {// 支付宝

									AlipayOperator alipayOperator = new AlipayOperator(RechargeActivity.this, mHandler,
											money, payId);
									alipayOperator.pay();


							} else if (type.equals("WePay")) {// 微信

							}
						} else if (jsonObject.optString("code").equals("fail")) {
							ToastUtils.show(RechargeActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
						}else if (jsonObject.optString("code").equals("priceError")) {
							ToastUtils.show(RechargeActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}

				}

			}

		});

	}

	/**
	 * @Description: 启动支付界面
	 * @author hunaixin
	 * @parame activity,tn,mode
	 * @return
	 */
	public void doStartUnionPayPlugin(Activity activity, String tn, String mode) {
		// mMode参数解释：
		// 0 - 启动银联正式环境
		// 1 - 连接银联测试环境
		int ret = UPPayAssistEx.startPay(this, null, null, tn, mode);
		if (ret == Contants.PLUGIN_NEED_UPGRADE || ret == Contants.PLUGIN_NOT_INSTALLED) {
			// 需要重新安装控件

			// AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("提示");
			// builder.setMessage("完成购买需要安装银联支付控件，是否安装？");
			//
			// builder.setNegativeButton("确定",
			// new DialogInterface.OnClickListener() {
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// UPPayAssistEx.installUPPayPlugin(RechargeActivity.this);
			// dialog.dismiss();
			// }
			// });
			//
			// builder.setPositiveButton("取消",
			// new DialogInterface.OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// dialog.dismiss();
			// }
			// });
			// builder.create().show();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data == null) {
			return;
		}
		String msg = "";
		/*
		 * 支付控件返回字符串:success、fail、cancel 分别代表支付成功，支付失败，支付取消
		 */
		topUp.setClickable(true);
		Bundle bundle = data.getExtras();

		String str = bundle.getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
			String sign = bundle.getString("result_data");

			unionpayMobileFront(payId, sign, "UnionPay");

		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";

		} else if (str.equalsIgnoreCase("cancel")) {

			msg = "用户取消了支付";
		}
		// 支付完成,处理自己的业务逻辑!
		ToastUtils.show(RechargeActivity.this, msg, Toast.LENGTH_SHORT);

	}

	/**
	 * @Description: UnionPay回调接口 银联 AliPay支付宝 WePay微信 
	 * @param payId, jsonData, type
	 * @return 
	 */
	public void unionpayMobileFront(final String payId, final String jsonData, final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				resultMessage = application.unionpayMobileFront(payId, jsonData, type);

				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				topUp.setClickable(true);
				try {
					JSONObject jsonObject = new JSONObject(resultMessage);
					if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
						Intent intent = new Intent();
						intent.setClass(RechargeActivity.this, PaySuccessActivity.class);
						intent.putExtra("activityType", "3");
						if (type.equals("UnionPay")) {
							intent.putExtra("fome", "银联支付");
						} else if (type.equals("AliPay")) {
							intent.putExtra("fome", "支付宝支付");
						} else if (type.equals("WePay")) {
							intent.putExtra("fome", "微信支付");
						}

						intent.putExtra("money", moneyBtn.getText().toString());

						startActivity(intent);

						finish();

					} else {
						ToastUtils.show(RechargeActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}

		});

	}

	// edittext选中效果切换
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

	/**
	 * @Description:判断充值金额输入是否正确
	 * @author hunaixin
	 * @parame money
	 * @return true/false
	 */
	public static boolean isMoney(String money) {
		String str = "^(?!000$)[1-9]?[0-9]+00$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(money);
		return m.matches();
	}
	
}
