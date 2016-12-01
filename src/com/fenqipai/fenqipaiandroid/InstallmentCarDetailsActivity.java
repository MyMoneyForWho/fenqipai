package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.InstallmentCar;
import com.fenqipai.fenqipaiandroid.model.OrderMsg;
import com.fenqipai.fenqipaiandroid.model.SingleSaleCarDetails;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshScrollView;
import com.fenqipai.fenqipaiandroid.view.viewpager.CycleViewPager;
import com.fenqipai.fenqipaiandroid.view.viewpager.CycleViewPager.ImageCycleViewListener;
import com.fenqipai.fenqipaiandroid.view.viewpager.ViewFactory;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * 分期车辆详情界面
 * 
 * @author hunaixin
 * 
 */
public class InstallmentCarDetailsActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.car_details_titleBar)
	private TitleBarView titleBarView;

	private PullToRefreshScrollView mPullScrollView;

	private ScrollView scrollView;

	private CycleViewPager cycleViewPager;

	@SuppressWarnings("unused")
	private Map<String, Object> map = new HashMap<String, Object>();
	private List<View> views = new ArrayList<View>();
	private List<String> carouselList;

	public SingleSaleCarDetails carInfo;

	private String saleId;

	// 查看详情
	@SuppressWarnings("unused")
	private TextView checkDetails;

	// 车辆全名
	private TextView carName;

	// 车辆当前价
	private TextView nowPrice;

	// 车辆售价
	private TextView salePrice;

	// 车辆指导价
	private TextView guidePrice;

	// 预约看车
	private LinearLayout bookCar;

	// 购车
	private RelativeLayout buyCar;

	private TextView buyCarText;

	// 配置参数（一系列）
	private TextView saleType, years, maxSpeed, officialAcceleration, foundAcceleration, foundBrake, officialFuel,
			foundFuel, warranty, level;

	@SuppressWarnings("unused")
	private Intent intent;

	@SuppressWarnings("unused")
	private ArrayList<String> list;

	private PopupWindow myPopupWindow;

	// popWindow背景层
	private LinearLayout background;

	private String resultMessage;

	public TextView confirm, back;

	// 查看更多属性
	private RelativeLayout moreParams;

	private InstallmentCar datas;

	private TextView insurance, tax, minPayRatioPrice, amr, gpsPrice, term;
	
	private TextView carColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_installment_car_details);

		// 获取车辆Id
		Intent intent = getIntent();
		saleId = intent.getStringExtra("saleId");
		datas = (InstallmentCar) intent.getSerializableExtra("saleList");

		// 注册EventBus
		EventBus.getDefault().register(this);

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullScrollView.doPullRefreshing(true, 500);

	}

	/**
	 * 初始化组件
	 * @author hunaixin
	 */
	@SuppressLint("InflateParams")
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.titlebar_installment_car_details);

		carInfo = new SingleSaleCarDetails();
		// 推送订阅内容
		// SubscribeMessage.getInstance().subscribe("auction",0);

		mPullScrollView = (PullToRefreshScrollView) findViewById(R.id.car_details__scroll);
		mPullScrollView.setPullLoadEnabled(false);
		mPullScrollView.setScrollLoadEnabled(true);

		scrollView = mPullScrollView.getRefreshableView();
		scrollView.addView(LayoutInflater.from(this).inflate(R.layout.activity_installment_car_details_child, null));
		scrollView.setVerticalScrollBarEnabled(false);

		cycleViewPager = (CycleViewPager) findViewById(R.id.car_details_cycle_viewpager);
		carName = (TextView) findViewById(R.id.car_all_name);
		background = (LinearLayout) findViewById(R.id.pop_background);
		bookCar = (LinearLayout) findViewById(R.id.car_details_book);
		buyCar = (RelativeLayout) findViewById(R.id.car_details_buy);
		buyCarText = (TextView) findViewById(R.id.car_details_buy_text);
		salePrice = (TextView) findViewById(R.id.starting_price);
		guidePrice = (TextView) findViewById(R.id.pinggu_price);
		insurance = (TextView) findViewById(R.id.insurance_price);
		tax = (TextView) findViewById(R.id.tax);
		minPayRatioPrice = (TextView) findViewById(R.id.shoufu_price);
		amr = (TextView) findViewById(R.id.rent_price);
		gpsPrice = (TextView) findViewById(R.id.gps_price);
		term = (TextView) findViewById(R.id.term);

		// 配置参数
		saleType = (TextView) findViewById(R.id.saleType);
		years = (TextView) findViewById(R.id.years);
		maxSpeed = (TextView) findViewById(R.id.maxspeed);
		officialAcceleration = (TextView) findViewById(R.id.officialAcceleration);
		foundAcceleration = (TextView) findViewById(R.id.foundAcceleration);
		foundBrake = (TextView) findViewById(R.id.foundBrake);
		officialFuel = (TextView) findViewById(R.id.officialFuel);
		foundFuel = (TextView) findViewById(R.id.foundFuel);
		warranty = (TextView) findViewById(R.id.warranty);
		level = (TextView) findViewById(R.id.level);
		moreParams = (RelativeLayout) findViewById(R.id.more_params);
        carColor = (TextView) findViewById(R.id.car_color);
		
		//
		minPayRatioPrice.setText("¥" + datas.getMinPayRatio());
		amr.setText("¥" + datas.getAMR());
		gpsPrice.setText("¥" + datas.getGpsExpanse());
		term.setText(datas.getLoanTerm());
		if (datas.getInsurance().equals("0")) {
			insurance.setText("自付");
		} else {
			insurance.setText("赠送");
		}
		if (datas.getTax().equals("0")) {
			tax.setText("自付");
		} else {
			tax.setText("赠送");
		}
	}

	/**
	 * 初始化事件
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

				// 获取车辆信息
				getSaleInfo();
			}
		});

		moreParams.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(InstallmentCarDetailsActivity.this, CarParamsActivity.class);
				intent.putExtra("webUrl", carInfo.getViewUrl());
				startActivity(intent);
			}
		});

		bookCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到拨号界面，同时传递电话号码
				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));
				startActivity(dialIntent);
			}
		});

		buyCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {

					showPopWindow(v);
					if (background.getVisibility() == View.GONE) {
						background.setVisibility(View.VISIBLE);
					}
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

	}
	
	
    /**
	 * @Description:初始化轮播图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	@SuppressLint("NewApi")
	private void initViewPager() {

		if (carouselList.size() == 0) {
			cycleViewPager.setVisibility(View.GONE);
			return;
		} else {
			cycleViewPager.setVisibility(View.VISIBLE);
		}
		if (views == null) {
			views = new ArrayList<View>();
		}
		views.clear();
		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, application, carouselList.get(carouselList.size() - 1)));
		for (int i = 0; i < carouselList.size(); i++) {
			views.add(ViewFactory.getImageView(this, application, carouselList.get(i)));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(this, application, carouselList.get(0)));

		// 设置循环，在调用setData方法前调用
		cycleViewPager.setCycle(true);

		// 在加载数据前设置是否循环
		cycleViewPager.setData(views, carouselList, mAdCycleViewListener);
		// 设置轮播
		cycleViewPager.setWheel(true);

		// 设置轮播时间，默认5000ms
		cycleViewPager.setTime(3000);
		// 设置圆点指示图标组居中显示，默认靠右
		cycleViewPager.setIndicatorCenter();

	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(String notice, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				Bundle bundle = new Bundle();
				bundle.putString("auctionId", saleId);
				;
				startActivity(ImageShowActivity.class, bundle);
			}

		}

	};

	/**
	 * @Description:获取某个拍卖的车辆详细信息
	 * @author hunaixin
	 * @parame saleId
	 * @return carInfo
	 */
	public void getSaleInfo() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				carInfo = application.getSaleInfo(saleId);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					carName.setText(carInfo.getCarAllName());
					salePrice.setText("¥" + carInfo.getStartingPrice() + "万");
					guidePrice.setText("¥" + datas.getDownPaymentPrice() + "万");

					// 配置参数
					saleType.setText(carInfo.getSaleType());
					years.setText(carInfo.getYears());
					maxSpeed.setText(carInfo.getMaxSpeed());
					officialAcceleration.setText(carInfo.getOfficialAcceleration());
					foundAcceleration.setText(carInfo.getFoundAcceleration());
					foundBrake.setText(carInfo.getFoundBrake());
					officialFuel.setText(carInfo.getOfficialFuel());
					foundFuel.setText(carInfo.getFoundFuel());
					warranty.setText(carInfo.getWarranty());
					level.setText(carInfo.getLevel());
					carColor.setText(carInfo.getCarColor());

					if (carInfo.getRemainnum() > 0) {

						buyCar.setClickable(true);
					} else {
						buyCar.setClickable(false);
						ToastUtils.show(InstallmentCarDetailsActivity.this, "库存不足", ToastUtils.TOAST_SHORT);
						buyCar.setBackgroundColor(getResources().getColor(R.color.gray_color_line));
						buyCarText.setText("库存不足");
					}

					// 轮播图
					if (carInfo.getImagePath() != null) {
						getJrjhNotice(carInfo.getImagePath().split(","));
					}

					// 加载完数据，结束刷新
					mPullScrollView.onPullDownRefreshComplete();
				}

			}

		});
	}

	/**
	 * 获取轮播图数据
	 */
	protected void getJrjhNotice(String[] imgPaths) {
		carouselList = new ArrayList<String>();

		for (int i = 0; i < imgPaths.length; i++) {
			carouselList.add(imgPaths[i]);
		}

		initViewPager();
	}

	/**
	 * @Description:接收信息
	 * @author hunaixin
	 * @parame orderMsg
	 * @return
	 */
	public void onEventMainThread(OrderMsg orderMsg) {
		if (orderMsg != null) {
			if (saleId.equals(orderMsg.getAuctionId())) {
				nowPrice.setText(orderMsg.getBidPrice());
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 取消注册EventBus
		EventBus.getDefault().unregister(this);
	}

	/**
	 * 购车popWindow
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view) {

		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_buy_car, null);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		initPopWindow(v);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {

				background.setVisibility(View.GONE);
			}
		});
		;
	}

	/**
	 * @Description:初始化购车popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	public void initPopWindow(View v) {
		confirm = (TextView) v.findViewById(R.id.pop_confirm);
		final EditText mobile = (EditText) v.findViewById(R.id.popup_mobile);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String saleId = carInfo.getSysId();
				String mobileId = mobile.getText().toString();
				if (!StringUtils.isEmpty(mobileId) && StringUtils.judgePhoneNums(mobileId)) {
					buyApply(saleId, mobileId);
					myPopupWindow.dismiss();
				} else {
					ToastUtils.show(application, "请检查手机号是否正确", ToastUtils.TOAST_SHORT);
				}

			}
		});

	}

	/**
	 * @Description: 提交我要购买车辆接口
	 * @param saleId, mobile
	 * @return resultMessage
	 */
	public void buyApply(final String saleId, final String mobile) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				if (NetUtils.isConnected(application)) {
					resultMessage = application.buyApply(saleId, mobile);
					return true;
				}

				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);
							finish();
						} else {
							ToastUtils.show(getApplicationContext(), jsonObject.optString("message"),
									ToastUtils.TOAST_SHORT);
							myPopupWindow.dismiss();
							// showOtherPopWindow(v);
							if (background.getVisibility() == View.VISIBLE) {
								background.setVisibility(View.GONE);
							}

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtils.show(application, R.string.no_net, ToastUtils.TOAST_SHORT);
				}
				super.onPostExecute(result);
			}
		});
	}

}
