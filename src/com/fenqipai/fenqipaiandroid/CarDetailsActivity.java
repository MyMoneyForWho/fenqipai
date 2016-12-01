package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.OrderMsg;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;
import com.fenqipai.fenqipaiandroid.view.TimeTextView;
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
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @Description:车辆详情界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:03:09
 */
public class CarDetailsActivity extends BaseActivity {

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

	private SingleCarDetails carInfo;

	private String auctionId;

	// 倒计时
	private TimeTextView countDownTimerView;

	// 车辆全名
	private TextView carName;

	// 出价（底部）
	private LinearLayout bidText;

	// 当前价
	private TextView startPrice;

	private Intent intent;

	@SuppressWarnings("unused")
	private ArrayList<String> list;

	// 起拍价
	private TextView startingPrice;

	// 评估价
	private TextView pingguPrice;

	// 围观人数
	private TextView onlookers;

	// 出价人数
	private TextView bidNums;

	// 配置参数（一系列）
	private TextView saleType, years, maxSpeed, officialAcceleration, foundAcceleration, foundBrake, officialFuel,
			foundFuel, warranty, level;
	
	// 查看更多参数
	private RelativeLayout moreParams;
	
	// 截止时间
	private TextView endTime,endTimeTwo;
	
	private String carstatus,auctionType;
	
	// 车辆种类
	private ImageView carstatusImg;
	
	// 拍卖类别
	private ImageView auctionTypeImg;
	
	// 车辆颜色
	private TextView carColor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_car_details);

		// 获取车辆Id
		Intent intent = getIntent();
		auctionId = intent.getStringExtra("auctionId");
		auctionType = intent.getStringExtra("auctionType");
		carstatus = intent.getStringExtra("carstatus");
		// 注册EventBus
		EventBus.getDefault().register(this);

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullScrollView.doPullRefreshing(true, 500);

	}

	/**
	 * @Description:初始化组件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	@SuppressLint("InflateParams")
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.titlebar_car_details);

		carInfo = new SingleCarDetails();
		// 推送订阅内容
		// SubscribeMessage.getInstance().subscribe("auction",0);

		mPullScrollView = (PullToRefreshScrollView) findViewById(R.id.car_details__scroll);
		mPullScrollView.setPullLoadEnabled(false);
		mPullScrollView.setScrollLoadEnabled(true);

		scrollView = mPullScrollView.getRefreshableView();
		scrollView.addView(LayoutInflater.from(this).inflate(R.layout.activity_car_details_child, null));
		scrollView.setVerticalScrollBarEnabled(false);

		// 倒计时
		countDownTimerView = (TimeTextView) findViewById(R.id.car_countdown_timer);

		cycleViewPager = (CycleViewPager) findViewById(R.id.car_details_cycle_viewpager);
		carName = (TextView) findViewById(R.id.car_all_name);
		bidText = (LinearLayout) findViewById(R.id.car_details_bid);
		startingPrice = (TextView) findViewById(R.id.starting_price);
		pingguPrice = (TextView) findViewById(R.id.pinggu_price);
		onlookers = (TextView) findViewById(R.id.bid_onlookers);
		bidNums = (TextView) findViewById(R.id.bid_num);
		startPrice = (TextView) findViewById(R.id.car_details_startingprice);
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
		endTime = (TextView) findViewById(R.id.end_time);
		endTimeTwo = (TextView) findViewById(R.id.end_time_two);
		carstatusImg = (ImageView) findViewById(R.id.carstatus);
		auctionTypeImg = (ImageView) findViewById(R.id.auctionType);
		carColor = (TextView) findViewById(R.id.car_color);
		
	    if (auctionType.equals("1")) {
        	auctionTypeImg.setImageResource(R.drawable.icon_gaopai);
		}else {
			auctionTypeImg.setImageResource(R.drawable.icon_kuaipai);
		}
	    if (carstatus.equals("1")) {
			carstatusImg.setImageResource(R.drawable.icon_new_car);
		}else {
			carstatusImg.setImageResource(R.drawable.icon_used_car);
		}
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
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
				getAuctionInfo();

			}
		});

		moreParams.setOnClickListener(new OnClickListener() {
		
		 @Override
		 public void onClick(View v) {
		 Intent intent = new Intent(CarDetailsActivity.this,CarParamsActivity.class);
		 intent.putExtra("webUrl", carInfo.getViewUrl());
		 startActivity(intent);
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
				bundle.putString("auctionId", auctionId);
				startActivity(ImageShowActivity.class, bundle);
			}

		}

	};
	
	private Long difference = 0L;
	
	/**
	 * @Description:获取某个拍卖的车辆详细信息接口
	 * @author hunaixin
	 * @parame auctionId
	 * @return carInfo
	 */
	public void getAuctionInfo() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				carInfo = application.getAuctionInfo(auctionId);

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					carName.setText(carInfo.getCarAllName());
					startingPrice.setText("¥" + MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
					pingguPrice.setText("¥" + carInfo.getFlatlyPrice() + "万");
					onlookers.setText(carInfo.getOnlookersCount());
					bidNums.setText(carInfo.getPersonCount());
					startPrice.setText(MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
					endTime.setText(TimeUtils.convertTimeToDate(carInfo.getEndTime()));
					endTimeTwo.setText(TimeUtils.convertTimeToMin(carInfo.getEndTime()));
					try {
						difference = Long.parseLong(carInfo.getServerTime()) - 1000 - System.currentTimeMillis();	
					} catch (Exception e) {
						difference = 1000L;
					}
					long duration = Long.parseLong(carInfo.getEndTime()) - (System.currentTimeMillis() + difference);

					if (!countDownTimerView.isRun()) {
						countDownTimerView.setTimes(duration);
						countDownTimerView.start();
					}

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
					// 轮播图
					getJrjhNotice(carInfo.getImagePath().split(","));

					// 加载完数据，结束刷新
					mPullScrollView.onPullDownRefreshComplete();
					
					bidText.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							if (application.getLoginState()) {
								// 跳转到加价页面
								Bundle bundle = new Bundle();
								bundle.putSerializable("carInfo", carInfo);

								intent = new Intent();
								intent.setClass(CarDetailsActivity.this, AddPriceActivity.class);
								intent.putExtra("bundle", bundle);
								intent.putExtra("auctionId", auctionId);
								intent.putExtra("list", carInfo.getAddPrice());
								startActivityForResult(intent, Contants.CAR_DETAIL_REQUST);
							} else {
								startActivity(LoginActivity.class);
							}

						}
					});
					
				}

			}

		});
	}

	/**
	 * @Description:获取轮播图数据
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	protected void getJrjhNotice(String[] imgPaths) {
		carouselList = new ArrayList<String>();

		for (int i = 0; i < imgPaths.length; i++) {
			carouselList.add(imgPaths[i]);
		}

		initViewPager();
	}


	/**
	 * @Description:接收消息
	 * @author hunaixin
	 * @parame orderMsg
	 * @return
	 */
	public void onEventMainThread(OrderMsg orderMsg) {
		if (orderMsg != null) {
			if (auctionId.equals(orderMsg.getAuctionId())) {
				carInfo.setNowPrice(orderMsg.getBidPrice());
				carInfo.setPersonCount(orderMsg.getPcount());
//				 nowPrice.setText(orderMsg.getBidPrice());
				startingPrice.setText(MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
				bidNums.setText(carInfo.getPersonCount());
				startPrice.setText(MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
			}
		}
	}

	@Override
	protected void onActivityResult(int arg0, int arg1, Intent arg2) {
		if (arg0==Contants.CAR_DETAIL_REQUST&&arg1==Contants.CAR_DETAIL_RETURN) {
			mPullScrollView.doPullRefreshing(true, 500);
		}
		super.onActivityResult(arg0, arg1, arg2);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 取消注册EventBus
		EventBus.getDefault().unregister(this);
	}

}
