package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.net.URL;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @Description:支付确认界面
 * @author hunaixin
 * @date 2016年11月25日 下午1:37:46
 */
public class PayConfirmActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.pay_confirm_titleBar)
	private TitleBarView titleBarView;

	private PullToRefreshScrollView mPullScrollView;

	private ScrollView scrollView;

	// 确认button
	// @ViewInject(R.id.pay)
	private Button payBtn;

	// 分期账单button
	// @ViewInject(R.id.staging_bill)
	private LinearLayout stagingBill;

	// 订单编号
	private TextView orderNum;

	// 拍卖品编号
	// private TextView lotNum;

	// 创建时间
	private TextView createTime;

	// 创建时间（min）
	private TextView createMin;

	// 支付时间
	private TextView payTime;

	// 支付时间（min）
	private TextView payMin;

	// 订单类型
	private TextView oderType;

	// 状态
	private TextView state;

	// 车辆图片
	private ImageView carImg;

	// 车辆全名
	private TextView carName;

	// 购车方案
	private TextView productName;
	// 共支付
	private TextView allPrice;

	private OrderInfo orderInfo;

	private String orderId;

	// 应支付种类（全款或者前期提车费）
	private TextView payType;

	// 应支付钱数
	private TextView payNum;

	// 应支付状态
	private TextView payStatus;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_confirm);

		orderId = getIntent().getStringExtra("orderId");

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullScrollView.doPullRefreshing(true, 500);
	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	@SuppressLint("InflateParams")
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.titlebar_pay_confirm);

		mPullScrollView = (PullToRefreshScrollView) findViewById(R.id.pay_confirm__scroll);
		mPullScrollView.setPullLoadEnabled(false);
		mPullScrollView.setScrollLoadEnabled(true);

		scrollView = mPullScrollView.getRefreshableView();
		scrollView.addView(LayoutInflater.from(this).inflate(R.layout.activity_pay_confirm_child, null));
		scrollView.setVerticalScrollBarEnabled(false);

		orderNum = (TextView) findViewById(R.id.order_number);
		// lotNum = (TextView) findViewById(R.id.pay_confirm_lot_num);
		createTime = (TextView) findViewById(R.id.create_day);
		createMin = (TextView) findViewById(R.id.create_min);
		carImg = (ImageView) findViewById(R.id.pay_confirm_car_img);
		carName = (TextView) findViewById(R.id.car_name);
		productName = (TextView) findViewById(R.id.instalment_scheme);
		oderType = (TextView) findViewById(R.id.order_type);
		state = (TextView) findViewById(R.id.state);
		payTime = (TextView) findViewById(R.id.pay_time);
		payMin = (TextView) findViewById(R.id.pay_min);
		allPrice = (TextView) findViewById(R.id.car_price);
		payBtn = (Button) findViewById(R.id.pay);
		stagingBill = (LinearLayout) findViewById(R.id.staging_bills);
		stagingBill.setClickable(false);
		stagingBill.setVisibility(View.GONE);
		payType = (TextView) findViewById(R.id.pay_type_name);
		payNum = (TextView) findViewById(R.id.pay_num);
		payStatus = (TextView) findViewById(R.id.pay_status);
	}

	/**
	 * 初始化事件
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

		mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

			}

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 获取订单详细
				getOrderInfo();

			}
		});

		payBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String status = orderInfo.getOrderStatus();
				if (status.equals("unconfirmed")) {// 待确认
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderInfo", orderInfo);
					startActivity(PayStagingActivity.class, bundle);

				} else if (status.equals("pendingpay")) {// 待付款

					Intent intent = new Intent(PayConfirmActivity.this, PayFullActivity.class);
					intent.putExtra("price", orderInfo.getPayPrice());
					intent.putExtra("ordertype", orderInfo.getOrderTypeString());
					intent.putExtra("payType", "payId");
					intent.putExtra("activityType", "2");
					intent.putExtra("payId", orderInfo.getPayId());
					startActivity(intent);

				} else if (status.equals("paying")) {// 交易中
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderInfo", orderInfo);
					startActivity(PayStagingActivity.class, bundle);

				} else if (status.equals("completed")) {// 交易完成
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderInfo", orderInfo);
					startActivity(PayStagingActivity.class, bundle);

				} else if (status.equals("cancelled")) {// 交易失败
					Bundle bundle = new Bundle();
					bundle.putSerializable("orderInfo", orderInfo);
					startActivity(PayStagingActivity.class, bundle);

				}

			}
		});
		stagingBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				startActivity(StagingBillActivity.class);
			}
		});

	}

	/**
	 * @Description:获取订单详细接口
	 * @author hunaixin
	 * @parame orderId
	 * @return orderInfo
	 */
	public void getOrderInfo() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				orderInfo = application.getOrderInfo(orderId);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {

					orderNum.setText(orderInfo.getOrderNumber());
					// um.setText(orderInfo.getSysId());
					createTime.setText(TimeUtils.convertTimeToDate(orderInfo.getOrderTime()));
					createMin.setText(TimeUtils.convertTimeToMin(orderInfo.getOrderTime()));
					application.imageLoader.displayImage(URL.getURL(URL.FILE_UPLOAD) + orderInfo.getCarImgPath(),
							carImg, application.options);
					payTime.setText(TimeUtils.convertTimeToDate(orderInfo.getOrderTime()));
					payMin.setText(TimeUtils.convertTimeToMin(orderInfo.getOrderTime()));
					allPrice.setText(MoneyUtils.toWan(orderInfo.getOrderPrice()) + "万元");
					carName.setText(orderInfo.getCarAllName());
					oderType.setText(orderInfo.getOderTpye());
					// 订单不同状态不同的设置
					String status = orderInfo.getOrderStatus();
					if (status.equals("unconfirmed")) {
						state.setText("待确认");
						payStatus.setText("(" + "待确认" + ")");
					} else if (status.equals("pendingpay")) {
						state.setText("待付款");
						payStatus.setText("(" + "待付款" + ")");
						String productNames = orderInfo.getProductName();
						if (productNames.equals("全款购车")) {
							productName.setText(productNames);
							stagingBill.setVisibility(View.GONE);
							payType.setText("全款购车费：");
							payNum.setText(orderInfo.getPayPrice() + "元");
						} else {
							stagingBill.setVisibility(View.VISIBLE);
							stagingBill.setClickable(true);
							productName.setText(productNames + "(" + orderInfo.getLoanTerm() + "期" + ")");
							payType.setText("前期提车费：");
							payNum.setText(orderInfo.getPayPrice() + "元");
						}
					} else if (status.equals("paying")) {
						state.setText("交易中");
						payStatus.setText("(" + "交易中" + ")");
						if (orderInfo.getOrderRepaymentType().equals("staging")) {
							stagingBill.setVisibility(View.VISIBLE);
							stagingBill.setClickable(true);
							productName.setText(orderInfo.getProductName() + "(" + orderInfo.getLoanTerm() + "期" + ")");
							payType.setText("分期金额：");
							payNum.setText(orderInfo.getStagingPrice() + "元");
						} else {
							stagingBill.setVisibility(View.GONE);
							productName.setText(orderInfo.getProductName());
							payType.setText("应支付：");
							payNum.setText(orderInfo.getOrderPrice() + "元");
						}
						payBtn.setClickable(false);
						payBtn.setVisibility(View.GONE);
					} else if (status.equals("completed")) {
						state.setText("交易完成");
						payStatus.setVisibility(View.GONE);
						payType.setVisibility(View.GONE);
						payNum.setVisibility(View.GONE);
						payBtn.setClickable(false);
						payBtn.setVisibility(View.GONE);
					} else if (status.equals("cancelled")) {
						state.setText("交易失败");
						allPrice.setText("0");
						payStatus.setVisibility(View.GONE);
						payType.setVisibility(View.GONE);
						payNum.setVisibility(View.GONE);
						payBtn.setClickable(false);
						payBtn.setVisibility(View.GONE);
					}


					// 加载完数据，结束刷新
					mPullScrollView.onPullDownRefreshComplete();
				}

			}

		});
	}

	@Override
	protected void onRestart() {
		mPullScrollView.doPullRefreshing(true, 500);
		super.onRestart();
	}
}
