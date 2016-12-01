package com.fenqipai.fenqipaiandroid.fragment;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.CarDetailsActivity;
import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.adapter.StagingHotFragmentAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.OrderMsg;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import de.greenrobot.event.EventBus;

/**
 * @Description:竞拍页面
 * @author hunaixin
 * @date 2016年11月25日 下午2:11:33
 */
public class BiddingFragment extends BaseFragment {

	// fragment保存数据的key
	@SuppressWarnings("unused")
	private static final String KEY_CONTENT = "BiddingFragment:Content";

	private MainActivity activity;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	@ViewInject(R.id.staging_hot_listview)
	private PullToRefreshListView mPullListView;
	
	@ViewInject(R.id.bid_titleBar)
	private TitleBarView titleBarView;

	private StagingHotFragmentAdapter adapter;

	private List<SaleCar> list = new ArrayList<SaleCar>();

	public static BiddingFragment newInstance() {
		BiddingFragment fragment = new BiddingFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_merchandise, container, false);

		// 注册EventBus
		EventBus.getDefault().register(this);
		
		// 注入view和事件
		ViewUtils.inject(this, rootView);

		initViews(rootView);

		initEvents();

		mPullListView.doPullRefreshing(true, 500);

		return rootView;
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initViews(View view) {
		
		titleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setTitleText(R.string.bidding_title);
		
		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);
		
		listView.setDivider(null);

		listView.setEmptyView(emptyView);

		adapter = new StagingHotFragmentAdapter(application, list);

		emptyView.setVisibility(View.GONE);
		
		listView.setAdapter(adapter);
	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvents
	 */
	private void initEvents() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				Intent intent = new Intent(activity, CarDetailsActivity.class);
				intent.putExtra("auctionId",adapter.getList().get(position).getSysId());
				intent.putExtra("auctionType", adapter.getList().get(position).getAuctionType());
				intent.putExtra("carstatus", adapter.getList().get(position).getCarStatus());
				activity.startActivity(intent);

			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = false;
				loadIndex = 0;
				getAuctionList();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getAuctionList();
			}

		});
		emptyView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPullListView.doPullRefreshing(true, 500);
			}
		});
	}

	/**
	 * @Description:获取拍卖列表 
	 * @param 
	 * @return 
	 */
	public void getAuctionList() {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.getAuctionList(isLoad, loadIndex * Contants.PAGE_SIZE, "");
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(list);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(list.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(list);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
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
	 * @Description:接收信息
	 * @author hunaixin
	 * @parame orderMsg
	 * @return
	 */
	public void onEventMainThread(OrderMsg orderMsg) {
		if (orderMsg != null) {
			
			for (int i = 0; i < adapter.getList().size(); i++) {
				if (adapter.getList().get(i).getSysId().equals(orderMsg.getAuctionId())) {
					adapter.getList().get(i).setNowPrice(MoneyUtils.toWan(orderMsg.getBidPrice()));
					adapter.getList().get(i).setPersonCount(orderMsg.getPcount());
				}
				
			}
			adapter.notifyDataSetChanged();
		}
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		// 取消注册EventBus
		EventBus.getDefault().unregister(this);
	}
	
	
	
}
