
package com.fenqipai.fenqipaiandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.CarDetailsActivity;
import com.fenqipai.fenqipaiandroid.LocationActivity;
import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.NewsDetailsActivity;
import com.fenqipai.fenqipaiandroid.NewsListActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.adapter.HomeFragmentAdapter;
import com.fenqipai.fenqipaiandroid.adapter.HomeFragmentNewsAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.HomeLunboList;
import com.fenqipai.fenqipaiandroid.model.IndexNewsList;
import com.fenqipai.fenqipaiandroid.model.OrderMsg;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.view.NoScrollListView;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import de.greenrobot.event.EventBus;

/**
 * @Description:首页
 * @author hunaixin
 * @date 2016年11月25日 下午2:12:27
 */
public class HomeFragment extends BaseFragment {

	// fragment保存数据的key
	@SuppressWarnings("unused")
	private static final String KEY_CONTENT = "HomeFragment:Content";

	private MainActivity activity;

	private ScrollView scrollView;

	private PullToRefreshScrollView mPullScrollView;

	@ViewInject(R.id.cycle_viewpager_content)
	private CycleViewPager cycleViewPager;

	@ViewInject(R.id.to_location)
	private RelativeLayout location;

	private List<View> views = new ArrayList<View>();

	private List<String> carouselList;

	private List<SaleCar> list = new ArrayList<SaleCar>();

	private List<IndexNewsList> newsLists = new ArrayList<IndexNewsList>();

	private List<HomeLunboList> lunboLists = new ArrayList<HomeLunboList>();

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	// 电话
	@ViewInject(R.id.home_phone)
	private ImageView phoneImg;

	// 位置
	@ViewInject(R.id.home_position)
	private ImageView positionImg;

	// 热推车hot_listview
	@ViewInject(R.id.hot_listview)
	private NoScrollListView hotListview;

	private HomeFragmentAdapter mAdapter;

	// 查看更多车
	@ViewInject(R.id.more_car)
	private TextView moreCar;

	// 查看更多新闻
	@ViewInject(R.id.more_news)
	private TextView moreNews;

	// 新闻资讯Listview
	@ViewInject(R.id.news_listview)
	private NoScrollListView newsListview;

	private HomeFragmentNewsAdapter newsAdapter;

	public static HomeFragment newInstance() {
		HomeFragment fragment = new HomeFragment();
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
		View rootView = inflater.inflate(R.layout.fragment_home, container, false);

		mPullScrollView = (PullToRefreshScrollView) rootView.findViewById(R.id.home_scroll);

		mPullScrollView.setPullLoadEnabled(false);
		mPullScrollView.setScrollLoadEnabled(true);

		scrollView = mPullScrollView.getRefreshableView();

		scrollView.addView(inflater.inflate(R.layout.fragment_home_child, null));

		scrollView.setVerticalScrollBarEnabled(false);

		// 注入view和事件
		ViewUtils.inject(this, rootView);

		// 注册EventBus
		EventBus.getDefault().register(this);

		initView();

		initEvent();

		mPullScrollView.doPullRefreshing(true, 500);

		return rootView;

	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initView() {

	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvents
	 */
	private void initEvent() {

		mPullScrollView.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
			}

			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// 获取车辆列表
				isLoad = false;
				loadIndex = 0;
				getAuctionList();

				// 新闻
				getNewsList();
			}
		});

		phoneImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到拨号界面，同时传递电话号码
				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));
				startActivity(dialIntent);

			}
		});

		positionImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(LocationActivity.class);
			}
		});
		location.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(LocationActivity.class);
			}
		});
		hotListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
				Intent intent = new Intent(activity, CarDetailsActivity.class);
				intent.putExtra("auctionId", mAdapter.getList().get(position).getSysId());
				intent.putExtra("auctionType", mAdapter.getList().get(position).getAuctionType());
				intent.putExtra("carstatus", mAdapter.getList().get(position).getCarStatus());
				intent.putExtra("carType", "bidding");
				activity.startActivity(intent);
			}
		});

		moreCar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				activity.showFragment(1);
				activity.getRbBidding().setChecked(true);
			}
		});

		moreNews.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(NewsListActivity.class);
			}
		});

		newsListview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent intent = new Intent(activity, NewsDetailsActivity.class);
				intent.putExtra("url", newsAdapter.getList().get(pos).getViewurl());
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

		views.clear();

		// 将最后一个ImageView添加进来
		views.add(ViewFactory.getImageView(activity, application, carouselList.get(carouselList.size() - 1)));
		for (int i = 0; i < carouselList.size(); i++) {
			views.add(ViewFactory.getImageView(activity, application, carouselList.get(i)));
		}
		// 将第一个ImageView添加进来
		views.add(ViewFactory.getImageView(activity, application, carouselList.get(0)));

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

	public void getJrjhNotice(List<HomeLunboList> lunboLists) {
		carouselList = new ArrayList<String>();
		for (int i = 0; i < lunboLists.size(); i++) {
			carouselList.add(lunboLists.get(i).getImagePath());
		}
		initViewPager();
	}

	private ImageCycleViewListener mAdCycleViewListener = new ImageCycleViewListener() {

		@Override
		public void onImageClick(String notice, int position, View imageView) {
			if (cycleViewPager.isCycle()) {
				position = position - 1;
				// Notice notice2 = carouselList.get(position);
				// Bundle bundle = new Bundle();
				// bundle.putSerializable("notice", notice2);
				// startActivity(NoticeDetailActivity.class, bundle);
			}

		}

	};

	/**
	 * @Title:getAuctionList
	 * @Description:获取拍卖列表
	 */
	public void getAuctionList() {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.getHomeAuctionList(isLoad, loadIndex * Contants.PAGE_SIZE, "");
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				mPullScrollView.onPullUpRefreshComplete();
				mPullScrollView.onPullDownRefreshComplete();

				if (result) {
					// 热推车辆
					mAdapter = new HomeFragmentAdapter(application, list);
					hotListview.setAdapter(mAdapter);

					if (isLoad) {
						// 加载更多
						mAdapter.getList().addAll(list);
						mAdapter.notifyDataSetChanged();
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						mAdapter.setList(list);
						mAdapter.notifyDataSetChanged();
						hotListview.setSelection(0);
						if (list.size() == 0) {
							emptyView.setVisibility(View.VISIBLE);
						} else {
							emptyView.setVisibility(View.GONE);
						}
					}

				}

				super.onPostExecute(result);
			}

		});
	}

	/**
	 * @Description:接收消息
	 * @author hunaixin
	 * @parame orderMsg
	 * @return
	 */
	public void onEventMainThread(OrderMsg orderMsg) {
		if (orderMsg != null) {
			for (int i = 0; i < mAdapter.getList().size(); i++) {
				if (mAdapter.getList().get(i).getSysId().equals(orderMsg.getAuctionId())) {
					mAdapter.getList().get(i).setNowPrice(MoneyUtils.toWan(orderMsg.getBidPrice()));
					mAdapter.getList().get(i).setPersonCount(orderMsg.getPcount());
				}
			}
			mAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * @Description:获取新闻和轮播数据
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void getNewsList() {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				newsLists = application.newsList();
				lunboLists = application.dBManager.getLunboList();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				mPullScrollView.onPullUpRefreshComplete();
				mPullScrollView.onPullDownRefreshComplete();

				if (result) {
					// 新闻资讯
					newsAdapter = new HomeFragmentNewsAdapter(application, newsLists);
					newsListview.setAdapter(newsAdapter);

					/**
					 * @Description:获取轮播图
					 * @author hunaixin
					 * @parame 
					 * @return
					 */
					getJrjhNotice(lunboLists);

				}

				super.onPostExecute(result);
			}

		});
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册EventBus
		EventBus.getDefault().unregister(this);
	}

}
