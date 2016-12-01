package com.fenqipai.fenqipaiandroid.fragment;

import com.fenqipai.fenqipaiandroid.AccountManagementActivity;
import com.fenqipai.fenqipaiandroid.BankCardActivity;
import com.fenqipai.fenqipaiandroid.LoginActivity;
import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.MoneyChangeRecordActivity;
import com.fenqipai.fenqipaiandroid.OrderListActivity;
import com.fenqipai.fenqipaiandroid.PayInformationActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.RecognizanceActivity;
import com.fenqipai.fenqipaiandroid.SettingsActivity;
import com.fenqipai.fenqipaiandroid.StagingBillActivity;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.UserInfo;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.view.CircleImageView;
import com.fenqipai.fenqipaiandroid.view.MyItemView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshScrollView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * @Description:我的页面
 * @author hunaixin
 * @date 2016年11月25日 下午2:18:11
 */
public class MyFragment extends BaseFragment {

	// fragment保存数据的key

	private MainActivity activity;

	private ScrollView eScroll;

	private PullToRefreshScrollView mPullScrollView;

	// private BaseApplication application;

	// 公共title
	@ViewInject(R.id.my_titleBar)
	private TitleBarView titleBarView;

	// 我的头像
	@ViewInject(R.id.my_img)
	private CircleImageView myImg;

	// 我的名字
	@ViewInject(R.id.my_name)
	private TextView myName;

	// 保证金
	@ViewInject(R.id.margin)
	private MyItemView myMargin;

	// 资金变更记录
	@ViewInject(R.id.change_record)
	private MyItemView myChangeRecord;

	// 订单信息
	@ViewInject(R.id.order_information)
	private MyItemView myOrderInformation;

	// 分期账单
	@ViewInject(R.id.instalment_bill)
	private MyItemView myInstalmentBill;

	// 支付信息
	@ViewInject(R.id.pay_information)
	private MyItemView myPayInformation;

	// 银行卡
	@ViewInject(R.id.bank_card)
	private MyItemView myBank_Card;

	// 系统设置
	@ViewInject(R.id.system_settings)
	private MyItemView mySystemSettings;

	// 登陆页返回信息
	@ViewInject(R.id.return_information)
	private LinearLayout llReturnInformation;

	// 显示地址
	@ViewInject(R.id.account_management)
	private MyItemView myAccountManagement;

	// 显示地址
	@ViewInject(R.id.address)
	private TextView tvAddress;

	// 显示身份信息
	@ViewInject(R.id.id_card)
	private TextView tvIdCard;

	// 头像，昵称布局
	@ViewInject(R.id.my_fra_lin)
	private LinearLayout myLoginBtn;

	public UserInfo usrInfo;

	public ImageLoader headImgLoader;

	public DisplayImageOptions options;

	public static MyFragment newInstance() {
		MyFragment fragment = new MyFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (MainActivity) getActivity();

	}

	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_my, container, false);

		mPullScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.my_scroll);

		eScroll = mPullScrollView.getRefreshableView();

		eScroll.addView(inflater.inflate(R.layout.fragment_my_child, null));

		eScroll.setVerticalScrollBarEnabled(false);

		// 注入view和事件
		ViewUtils.inject(this, rootView);

		headImgLoader = ImageLoader.getInstance();

		initHeadImageLoader();

		initView();

		initEvent();

		mPullScrollView.doPullRefreshing(true, 500);

		return rootView;
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 * @author liuChengbao
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setTitleText("我的");

		myMargin.setItemName(R.string.activity_recognizance_text);
		myMargin.setItemImg(R.drawable.btn_in);

		myChangeRecord.setItemName(R.string.change_record);
		myChangeRecord.setItemImg(R.drawable.btn_in);

		myOrderInformation.setItemName(R.string.order_msg);
		myOrderInformation.setItemImg(R.drawable.btn_in);

		myInstalmentBill.setItemName(R.string.instalment_bill);
		myInstalmentBill.setItemImg(R.drawable.btn_in);

		myPayInformation.setItemName(R.string.pay_information);
		myPayInformation.setItemImg(R.drawable.btn_in);

		myBank_Card.setItemName(R.string.bank_card);
		myBank_Card.setItemImg(R.drawable.btn_in);

		myAccountManagement.setItemName(R.string.account_management);
		myAccountManagement.setItemImg(R.drawable.btn_in);

		mySystemSettings.setItemName(R.string.system_settings);
		mySystemSettings.setItemImg(R.drawable.btn_in);

		// 获取用户头像
		String nick = (String) SPUtils.get(application, Contants.USER_NAME, "");
		if (application.getLoginState()) {
			if (TextUtils.isEmpty(nick)) {
				myName.setText((String) SPUtils.get(application, Contants.USER_MOBILE, "登录"));
			} else {
				myName.setText(nick);
			}
			headImgLoader.displayImage((String) SPUtils.get(application, Contants.USER_PORTRAIT, ""), myImg, options);
			llReturnInformation.setVisibility(View.VISIBLE);

		} else {
			myName.setText("登录");
			llReturnInformation.setVisibility(View.GONE);
		}
	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvents
	 * @author liuChengbao
	 */
	private void initEvent() {
		mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			}

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				if (application.getLoginState()) {
					getNbuserInfoResult();
				} else {
					mPullScrollView.onPullDownRefreshComplete();
				}

			}
		});
		// 头像，昵称布局
		myLoginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					if (usrInfo != null) {
					}

				} else {
					startActivity(LoginActivity.class);
				}

			}
		});
		// 我的头像
		myImg.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					if (usrInfo != null) {
						Intent intent = new Intent();
						intent.setClass(activity, AccountManagementActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("userInfo", usrInfo);
						intent.putExtras(bundle);
						startActivityForResult(intent, Contants.TO_CHENG_MESSAGE);
					}

				} else {
					startActivity(LoginActivity.class);
				}

			}
		});
		// 系统设置
		mySystemSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(activity, SettingsActivity.class);
				startActivityForResult(intent, Contants.TO_SETTING);

			}
		});
		// 账户管理
		myAccountManagement.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					if (usrInfo != null) {
						Intent intent = new Intent();
						intent.setClass(activity, AccountManagementActivity.class);
						Bundle bundle = new Bundle();
						bundle.putSerializable("userInfo", usrInfo);
						intent.putExtras(bundle);
						startActivityForResult(intent, Contants.TO_CHENG_MESSAGE);
					}

				} else {
					startActivity(LoginActivity.class);
				}

			}
		});
		// 支付信息列表页
		myPayInformation.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(PayInformationActivity.class);
				} else {
					startActivity(LoginActivity.class);

				}

			}
		});
		// 保证金
		myMargin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(RecognizanceActivity.class);

				} else {
					startActivity(LoginActivity.class);

				}

			}
		});
		// 银行卡
		myBank_Card.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(BankCardActivity.class);

				} else {
					startActivity(LoginActivity.class);

				}

			}
		});
		// 订单信息
		myOrderInformation.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(OrderListActivity.class);

				} else {
					startActivity(LoginActivity.class);

				}

			}
		});
		// 分期账单
		myInstalmentBill.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(StagingBillActivity.class);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});
		// 资金变更记录
		myChangeRecord.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(MoneyChangeRecordActivity.class);

				} else {
					startActivity(LoginActivity.class);
				}

			}
		});

	}

	/*
	 * 自定义配置
	 */
	public void initHeadImageLoader() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(activity);
		config.threadPoolSize(3);// 线程池内加载的数量
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();// 不缓存图片的多种尺寸在内存中
		config.discCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
		config.discCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs();// Remove for release app
		// 初始化ImageLoader
		ImageLoader.getInstance().init(config.build());

		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.user)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.user)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.user)// 设置图片加载/解码过程中错误时候显示的图片
				.delayBeforeLoading(100)// 设置延时多少时间后开始下载
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的资源是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// 设置图片以何种编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();

	}


	/**
	 * @Description:获取用户信息
	 * @author hunaixin
	 * @parame 
	 * @return usrInfo
	 */
	public void getNbuserInfoResult() {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				usrInfo = application.getNbuserInfoResult();

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				mPullScrollView.onPullDownRefreshComplete();

				if (!TextUtils.isEmpty(usrInfo.getPortrait())) {
					SPUtils.putPortrait(application, Contants.USER_PORTRAIT, usrInfo.getPortrait());
				}

				SPUtils.put(application, Contants.USER_NAME, usrInfo.getNickName());

				SPUtils.put(application, Contants.USER_MOBILE, usrInfo.getMobile());

				SPUtils.put(application, Contants.USER_PASSWORD, usrInfo.getIsPassword());

				SPUtils.put(application, Contants.USER_PAYPASSWORD, usrInfo.getIsPayPassword());

				if (TextUtils.isEmpty(application.getUserName())) {
					myName.setText(usrInfo.getMobile());
				} else {
					myName.setText(usrInfo.getNickName());
				}
				headImgLoader.displayImage(application.getPortrait(), myImg, options);

				super.onPostExecute(result);
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Contants.TO_CHENG_MESSAGE:

			mPullScrollView.doPullRefreshing(true, 500);
			break;

		// 更改昵称
		case Contants.TO_SETTING:
			if (resultCode == Contants.RETURN_MESSAGE) {
				headImgLoader.displayImage((String) SPUtils.get(application, Contants.USER_PORTRAIT, ""), myImg,
						options);

				myImg.setImageResource(R.drawable.user);
				myName.setText((String) SPUtils.get(application, Contants.USER_MOBILE, "登录"));
				llReturnInformation.setVisibility(View.GONE);
			}
			break;

		}

	}

	@Override
	public void onStart() {
		mPullScrollView.doPullRefreshing(true, 500);
		super.onStart();
	}

}
