package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.ProgramGridadapter;
import com.fenqipai.fenqipaiandroid.adapter.TermGridAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.PaymentSaleLoan;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @Description:分期方案页面
 * @author hunaixin
 * @date 2016年11月25日 下午1:52:38
 */
public class PayStagingActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.pay_staging_titleBar)
	private TitleBarView titleBarView;

	// 确认button
	@ViewInject(R.id.confirm_staging_btn)
	private Button confirmBtn;

	// 车辆全名
	@ViewInject(R.id.staging_car_name)
	private TextView carName;

	// 车辆售价
	@ViewInject(R.id.staging_order_price)
	private TextView orderPrice;

	// 首付
	@ViewInject(R.id.minPayRatio)
	private TextView minPayRatio;

	// GPS费用
	@ViewInject(R.id.gps)
	private TextView gpsPrice;

	// 保险
	@ViewInject(R.id.insurance)
	private TextView insurancePrice;

	// 购置税
	@ViewInject(R.id.tax)
	private TextView tax;

	// 月租金
	@ViewInject(R.id.rentprice)
	private TextView rentPrice;

	// 全局
	@ViewInject(R.id.my_scrollview)
	private ScrollView scrollview;

	// 期限（月）
	@ViewInject(R.id.term)
	private TextView stagingTerm;

	// 订单号
	@ViewInject(R.id.order_num)
	private TextView orderNumber;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	// 选择方案
	@ViewInject(R.id.pay_staging_program_linearlayout)
	private LinearLayout program;

	// 选择分期期限
	@ViewInject(R.id.choose_term_linearlayout)
	private LinearLayout term;

	// 选择方案Text
	@ViewInject(R.id.pay_staging_program)
	private TextView programText;

	// 选择分期期限text
	@ViewInject(R.id.choose_term)
	private TextView termText;

	@SuppressWarnings("unused")
	private TermGridAdapter mTermGridAdapter;

	private OrderInfo orderInfo;

	private String orderId;

	private List<PaymentSaleLoan> paymentSaleLoan = new ArrayList<PaymentSaleLoan>();

	private List<LoanAlgorithmList> loanAlgorithmLists = new ArrayList<LoanAlgorithmList>();

	private Boolean isTermChoose = false;

	private int isTermNum = 0;
	private int isPaymentNum = 0;

	private String orderNum;

	private PopupWindow myPopupWindow;
	
	private String carPrice;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_staging);

		orderInfo = (OrderInfo) getIntent().getSerializableExtra("orderInfo");
		orderId = getIntent().getStringExtra("orderId");
		orderNum = getIntent().getStringExtra("orderNum");
		carPrice = getIntent().getStringExtra("carPrice");
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
		titleBarView.setTitleText(R.string.pay_staging_titlebar);

		getpaymentSaleLoanList(orderId);

		carName.setText(orderInfo.getCarAllName());
		orderNumber.setText(orderNum);
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

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Bundle bundle = new Bundle();
				// 支付单类型:fullAmount全款, downPayment 首付, staging 分期
				if (isTermChoose) {
					bundle.putString("payType", "downPayment");
					bundle.putSerializable("orderInfo", orderInfo);
					bundle.putString("price",
							loanAlgorithmLists.get(isTermNum).getDownPayment());
					bundle.putString("activityType", "2");
					if (isTermNum >= 0) {
						bundle.putSerializable("LoanAlgorithmList", loanAlgorithmLists.get(isTermNum));
						bundle.putSerializable("PaymentSaleLoan", paymentSaleLoan.get(isPaymentNum));
					}
					startActivity(PayFullActivity.class, bundle);
				} else if (isPaymentNum == 0) {
					bundle.putString("activityType", "2");
					bundle.putString("payType", "fullAmount");
					bundle.putSerializable("orderInfo", orderInfo);
					bundle.putString("price", carPrice);
					startActivity(PayFullActivity.class, bundle);
				} else {
					ToastUtils.show(PayStagingActivity.this, "请选择分期期限", ToastUtils.TOAST_SHORT);
				}

			}
		});

		program.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopWindow(program, program.getWidth());

			}
		});

		term.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showTermPopWindow(term, term.getWidth());
			}
		});

	}
	
	/**
	 * @Description:获取销售方案确认查询
	 * @author hunaixin
	 * @parame orderId
	 * @return paymentSaleLoan
	 */
	public void getpaymentSaleLoanList(final String orderId) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				paymentSaleLoan = application.paymentSaleLoanList(orderId);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					mAdapter = new ProgramGridadapter(PayStagingActivity.this, paymentSaleLoan);

					// 默认选择第一个
					programText.setText(paymentSaleLoan.get(0).getProductName());
					termText.setText("无");
					orderPrice.setText(MoneyUtils.toWan(carPrice) + "万");
					minPayRatio.setText("无");
					rentPrice.setText("无");
					stagingTerm.setText("无");
					term.setClickable(false);
					if (paymentSaleLoan.get(0).getGPS().equals("")) {
						gpsPrice.setText("无");
					} else {
						gpsPrice.setText("¥" + paymentSaleLoan.get(0).getGPS());
					}
					insurancePrice.setText("¥" + paymentSaleLoan.get(0).getInsurance());
					tax.setText("¥" + paymentSaleLoan.get(0).getTax());

				}

			}

		});
	}

	/**
	 * @Description:根据分期业务获取对应信息
	 * @author hunaixin
	 * @parame cid, type, loanId, orderId
	 * @return loanAlgorithmLists
	 */
	public void getLoanAlgorithmList(final String cid, final String type, final String loanId, final String orderId) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				loanAlgorithmLists = application.loanAlgorithmList(cid, type, loanId, orderId);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					termAdapter = new TermGridAdapter(PayStagingActivity.this, loanAlgorithmLists);

				}

			}

		});
	}

	/**
	 * @Description:分期方案popWindow
	 * @author hunaixin
	 * @parame view width
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public void showPopWindow(View view, int width) {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}
		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_program, null);
		initPopWindow(v);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(width);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAsDropDown(view);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});

	}

	private ListView programList;

	private ProgramGridadapter mAdapter;

	/**
	 * @Description:初始化分期方案popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	private void initPopWindow(View v) {
		programList = (ListView) v.findViewById(R.id.program_listview);

		programList.setAdapter(mAdapter);

		programList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				// 没选中分期
				isTermChoose = false;
				isTermNum = -1;
				isPaymentNum = position;
				// 不同方案的不同内容
				if (position == 0) {
					termText.setText("无");
					orderPrice.setText(MoneyUtils.toWan(carPrice) + "万");
					minPayRatio.setText("无");
					rentPrice.setText("无");
					stagingTerm.setText("无");
					term.setClickable(false);
				} else {
					termText.setText("选择分期期限");
					term.setClickable(true);
					// 首付加保证金
					int minPay = paymentSaleLoan.get(position).getMinPayRatio() + paymentSaleLoan.get(position).getBailRatio();
					minPayRatio.setText("¥" + minPay);
				}

				// GPS费用
				if (paymentSaleLoan.get(position).getGPS().equals("")) {
					gpsPrice.setText("无");
				} else {
					gpsPrice.setText("¥" + paymentSaleLoan.get(position).getGPS());
				}
				insurancePrice.setText("¥" + paymentSaleLoan.get(position).getInsurance());
				tax.setText("¥" + paymentSaleLoan.get(position).getTax());
				programText.setText(paymentSaleLoan.get(position).getProductName());
				// 不同方案不同期限
				getLoanAlgorithmList(orderInfo.getSaleId(), "2", paymentSaleLoan.get(position).getSysId(),
						orderInfo.getSysId());

				myPopupWindow.dismiss();
			}
		});
	}

	/**
	 * @Description:分期期限popWindow
	 * @author hunaixin
	 * @parame view
	 * @return
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	public void showTermPopWindow(View view, int width) {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}
		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_term, null);
		initTermPopWindow(v);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(width);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAsDropDown(view);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});

	}

	private ListView termList;

	private TermGridAdapter termAdapter;

	/**
	 * @Description:初始化期限popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	private void initTermPopWindow(View v) {
		termList = (ListView) v.findViewById(R.id.term_listview);

		termList.setAdapter(termAdapter);
		
		termList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// 选中了分期
				isTermChoose = true;
				isTermNum = position;
				// 前期提车费
				if (loanAlgorithmLists.size() == 0) {
					orderPrice.setText(MoneyUtils.toWan(orderInfo.getOrderPrice()) + "万");
					minPayRatio.setText("无");
					rentPrice.setText("无");
					stagingTerm.setText("无");
				} else {
					termText.setText(loanAlgorithmLists.get(position).getLoanTerm() + "期");
					rentPrice.setText("¥" + loanAlgorithmLists.get(position).getAMR());
					stagingTerm.setText("" + loanAlgorithmLists.get(position).getLoanTerm());
					orderPrice.setText(MoneyUtils.toWan(loanAlgorithmLists.get(position).getTotalPrice()) + "万");
				}

				myPopupWindow.dismiss();
			}
		});
	}

}
