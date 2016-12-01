package com.fenqipai.fenqipaiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fenqipai.fenqipaiandroid.alipay.AlipayOperator;
import com.fenqipai.fenqipaiandroid.alipay.PayResult;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.PaymentSaleLoan;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.unionpay.UPPayAssistEx;

/**
 * @Description:支付方式界面
 * @author hunaixin
 * @date 2016年11月25日 下午1:39:33
 */
public class PayFullActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.pay_full_titleBar)
	private TitleBarView titleBarView;

	// 银联支付
	@ViewInject(R.id.unionpay)
	private LinearLayout unionpay;

	// 银联支付圆圈
	@ViewInject(R.id.pay_unionpay_choose)
	private ImageView unionpayChoose;

	// 支付宝支付
	@ViewInject(R.id.pay_zfb)
	private LinearLayout zfb;

	// 支付宝支付圆圈
	@ViewInject(R.id.pay_zfb_choose)
	private ImageView zfbChoose;

	// 微信支付
	@ViewInject(R.id.pay_wechat)
	private LinearLayout wechat;

	// 微信支付圆圈
	@ViewInject(R.id.pay_wechat_choose)
	private ImageView wechatChoose;

	// 确认按钮
	@ViewInject(R.id.confirm_pay_btn)
	private Button confirmBtn;

	// 售价：
	@ViewInject(R.id.price)
	private TextView priceText;

	// 车名：
	@ViewInject(R.id.car_name)
	private TextView nameText;

	// 应付金额
	@ViewInject(R.id.car_price)
	private TextView payMoney;

	// 余额支付密码popWindow
//	private PopupWindow myPopupWindow;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	// 支付方式 1表示银联支付 2表示支付宝 3表示微信 0表示初始状态
	private int type = 2;

	private String payType;

	private String price;

	public String resultMessage;

	private OrderInfo orderInfo;

	private String repaymentIdArray;

	private LoanAlgorithmList loanAlgorithm;

	private PaymentSaleLoan paymentSaleLoan;

	// 支付类型
	private String ordertype;

	private String payId;

	// 页面来源，及出口 1,支付列表页 payInformationActivity 2,分期列表页 payStagingAcitvity 3.订单页
	// order 4.分期账单页 stagingBillDetails
	private String activityType;

	private static final int SDK_PAY_FLAG = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_full);

		Bundle bundle = getIntent().getExtras();
		// 支付单类型:fullAmount全款, downPayment 首付, staging 分期
		payType = bundle.getString("payType");
		price = bundle.getString("price");
		payId = bundle.getString("payId");
		activityType = bundle.getString("activityType");
		orderInfo = (OrderInfo) bundle.getSerializable("orderInfo");
		repaymentIdArray = bundle.getString("repaymentIdArray");
		loanAlgorithm = (LoanAlgorithmList) bundle.getSerializable("LoanAlgorithmList");
		paymentSaleLoan = (PaymentSaleLoan) bundle.getSerializable("PaymentSaleLoan");
		ordertype = bundle.getString("ordertype");

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.pay_way);

		if (payType.equals("payId")) {
			nameText.setText(ordertype);
		} else {
			nameText.setText(orderInfo.getCarAllName());
		}
		priceText.setText(price + "元");

		// 默认选中第一种支付方式
		unionpayChoose.setBackgroundResource(R.drawable.btn_choose_on);
	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	private void initEvent() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		titleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到拨号界面，同时传递电话号码
				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));
				startActivity(dialIntent);
			}
		});

		unionpay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				unionpayChoose.setBackgroundResource(R.drawable.btn_choose_on);
				type = 1;
				zfbChoose.setBackgroundResource(R.drawable.btn_choose_off);
				wechatChoose.setBackgroundResource(R.drawable.btn_choose_off);

			}
		});

		zfb.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				zfbChoose.setBackgroundResource(R.drawable.btn_choose_on);
				unionpayChoose.setBackgroundResource(R.drawable.btn_choose_off);
				wechatChoose.setBackgroundResource(R.drawable.btn_choose_off);
				type = 2;
			}
		});

		wechat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				wechatChoose.setBackgroundResource(R.drawable.btn_choose_on);
				unionpayChoose.setBackgroundResource(R.drawable.btn_choose_off);
				zfbChoose.setBackgroundResource(R.drawable.btn_choose_off);
				type = 3;
			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (type == 1) {// 银联

					// createPayBy("UnionPay");

				} else if (type == 2) {// 支付宝
					createPayBy("AliPay");

				} else if (type == 3) {// 微信

					// createPayBy("AliPay");
					// createPayBy("WePay");
				}

			}
		});

	}

	public void createPayBy(String type) {
		confirmBtn.setClickable(false);
		// 支付单类型:fullAmount全款, downPayment 首付, staging 分期
		if (payType.endsWith("fullAmount")) {
			createPay(orderInfo.getSysId(), payType, repaymentIdArray, null, null, type);
		} else if (payType.endsWith("downPayment")) {
			createPay(orderInfo.getSysId(), payType, repaymentIdArray, paymentSaleLoan.getSysId(),
					loanAlgorithm.getSysId(), type);

		} else if (payType.endsWith("staging")) {
			createPay(orderInfo.getSysId(), payType, repaymentIdArray, null, null, type);

		} else if (payType.endsWith("payId")) {

			if (type.equals("UnionPay")) {// 银联
				payMobileCreate(payId, "UnionPay");// 是否是需要的
			} else if (type.equals("AliPay")) {// 支付宝

				AlipayOperator alipayOperator = new AlipayOperator(PayFullActivity.this, mHandler, price, payId);
				alipayOperator.pay();

			} else if (type.equals("WePay")) {// 微信

			}

		}

	}


	/**
	 * @Description:验证支付密码接口
	 * @author hunaixin
	 * @parame payPassword
	 * @return resultMessage
	 */
	public void payPasswordCheck(final String payPassword) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.payPasswordCheck(payPassword);
					return true;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					try {
						JSONObject jObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jObject.optString("code"))) {
							startActivity(PaySuccessActivity.class);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
						} else if (jObject.optString("code").equals("fail")) {
							// startActivity(PayFailActivity.class);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}

				super.onPostExecute(result);
			}

		});
	}

	/**
	 * 银联支付 tn
	 * 
	 * @author hunaixin 
	 * @param  orderId - 订单Id loanId - 分期业务Id loanAlgorithmId - 分期期限Id
	 *         repaymentIdArray[] - 分期期限Id数组 actionType - 支付单类型:fullAmount全款,
	 *         downPayment 首付, staging 分期
	 * @return resultMessage
	 */
	public void createPay(final String orderId, final String actionType, final String repaymentIdArray,
			final String loanId, final String loanAlgorithmId, final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.createPay(orderId, actionType, repaymentIdArray, loanId,
							loanAlgorithmId, type);
					return true;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
							JSONObject jObject = jsonObject.optJSONObject("data");
							payId = jObject.optString("payId");
							if (type.equals("UnionPay")) {// 银联
								doStartUnionPayPlugin(PayFullActivity.this, jObject.optString("tn"),
										Contants.YINLIAN_MODE);
							} else if (type.equals("AliPay")) {// 支付宝
								AlipayOperator alipayOperator = new AlipayOperator(PayFullActivity.this, mHandler,
										price, payId);
								alipayOperator.pay();

							} else if (type.equals("WePay")) {// 微信

							}
						} else {
							Toast.makeText(PayFullActivity.this, jsonObject.optString("message"), Toast.LENGTH_LONG)
									.show();
						}

					} catch (JSONException e) {
						e.printStackTrace();
					}
				}

				super.onPostExecute(result);
			}

		});
	}

	/**
	 * 启动支付界面
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
		confirmBtn.setClickable(true);
		Bundle bundle = data.getExtras();

		String str = bundle.getString("pay_result");
		if (str.equalsIgnoreCase("success")) {
			msg = "支付成功！";
			String sign = bundle.getString("result_data");
			payMobileFront(payId, sign, "UnionPay");

		} else if (str.equalsIgnoreCase("fail")) {
			msg = "支付失败！";

		} else if (str.equalsIgnoreCase("cancel")) {

			msg = "用户取消了支付";
		}
		// 支付完成,处理自己的业务逻辑!
		ToastUtils.show(PayFullActivity.this, msg, Toast.LENGTH_SHORT);

	}

	/**
	 * @Description:银联支付回调
	 * @author hunaixin
	 * @parame payNumber, jsonData, type
	 * @return resultMessage
	 */
	public void payMobileFront(final String payNumber, final String jsonData, final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络，获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.unionpayMobileFront(payNumber, jsonData, type);
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				try {
					JSONObject jsonObject = new JSONObject(resultMessage);
					if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
						Intent intent = new Intent();
						intent.setClass(PayFullActivity.this, PaySuccessActivity.class);
						intent.putExtra("activityType", activityType);
						if (payType.equals("payId")) {
							intent.putExtra("name", "支付类型：" + ordertype);
						} else {
							intent.putExtra("name", "车辆全名:" + orderInfo.getCarAllName());
						}

						intent.putExtra("money", price);
						intent.putExtra("fome", "银联支付");

						startActivity(intent);
						finish();
					} else {
						ToastUtils.show(PayFullActivity.this, jsonObject.optString("message"), Toast.LENGTH_SHORT);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onPostExecute(result);
			}

		});

	}

	/**
	 * @Description:银联支付接口
	 * @author hunaixin
	 * @parame payId, type
	 * @return resultMessage
	 */
	public void payMobileCreate(final String payId, final String type) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络，获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.unionpayMobileCreate(payId, type);
					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				if (result) {
					try {
						JSONObject jObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jObject.optString("code"))) {
							JSONObject jsonObject = jObject.optJSONObject("data");
							doStartUnionPayPlugin(PayFullActivity.this, jsonObject.optString("tn"),
									Contants.YINLIAN_MODE);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
						} else {

							// startActivity(PayFailActivity.class);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
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

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				PayResult payResult = new PayResult((String) msg.obj);

				// 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
				String resultStatus = payResult.getResultStatus();
				String resultInfo = payResult.getResult();
				@SuppressWarnings("unused")
				String orderId = payResult.getOutTradeNumber();
				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.makeText(PayFullActivity.this, R.string.pay_success, Toast.LENGTH_SHORT).show();

					payMobileFront(payId, resultInfo, "AliPay");
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

}
