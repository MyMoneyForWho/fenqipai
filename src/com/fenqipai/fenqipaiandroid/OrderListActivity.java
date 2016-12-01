package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.OrderListAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * @Description:订单列表页面
 * @author hunaixin
 * @date 2016年11月25日 下午1:36:04
 */
public class OrderListActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.order_list_titleBar)
	private TitleBarView titleBarView;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	@ViewInject(R.id.order_list_listview)
	private PullToRefreshListView mPullListView;

	private OrderListAdapter adapter;

	private List<OrderList> orderList = new ArrayList<OrderList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_list);

		// 注入view和事件
		ViewUtils.inject(this);

		initViews();

		initEvents();

		mPullListView.doPullRefreshing(true, 500);
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initViews() {

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE,
				View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.order_list);

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);

		listView.setDivider(null);

		emptyView.setVisibility(View.GONE);

		adapter = new OrderListAdapter(this, orderList);

		listView.setAdapter(adapter);
		
		createLoadingDialog(OrderListActivity.this);
		
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initEvents() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isLoad = false;
				loadIndex = 0;
				getOrderList();

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getOrderList();
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent();
				String status = adapter.getList().get(position)
						.getOrderStatus();
				if (status.equals("unconfirmed")) {// 待确认
					
					getOrderInfo(adapter.getList().get(position).getSysId(),position);

				} else if (status.equals("pendingpay")) {// 待付款
					intent.setClass(OrderListActivity.this,
							PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(position)
							.getSysId());
					startActivity(intent);

				} else if (status.equals("paying")) {// 交易中
					intent.setClass(OrderListActivity.this,
							PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(position)
							.getSysId());
					startActivity(intent);

				} else if (status.equals("completed")) {// 交易完成
					intent.setClass(OrderListActivity.this,
							PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(position)
							.getSysId());
					startActivity(intent);

				} else if (status.equals("cancelled")) {// 交易失败
					intent.setClass(OrderListActivity.this,
							PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(position)
							.getSysId());
					startActivity(intent);

				} else {
					intent.setClass(OrderListActivity.this,
							PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(position)
							.getSysId());
					startActivity(intent);

				}

			}
		});
	}
	
	/**
	 * @Description:获取订单列表接口
	 * @author hunaixin
	 * @parame isLoad, curRows, pageSize
	 * @return orderList
	 */
	public void getOrderList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				orderList = application.getOrderList(isLoad, loadIndex
						* Contants.PAGE_SIZE, Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(orderList);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(orderList.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(orderList);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						if (orderList.size() == 0) {
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
	 * @Description:获取订单详细接口
	 * @author hunaixin
	 * @parame orderId
	 * @return orderInfo
	 */
	public void getOrderInfo(final String orderId,final int pos) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			OrderInfo orderInfo;
			@Override
			protected void onPreExecute() {
				loadingDialogShow("");
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				orderInfo = application.getOrderInfo(orderId);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);
				loadingDialogDismiss();
				// 主线程 刷新UI
				if (result) {
					Intent intent = new Intent();
					intent.setClass(OrderListActivity.this,
							PayStagingActivity.class);
					intent.putExtra("orderInfo", orderInfo);
					intent.putExtra("orderId",orderId);
					intent.putExtra("orderNum", orderList.get(pos).getOrderNumber());
					intent.putExtra("carPrice", orderList.get(pos).getCarPrice());
					startActivity(intent);

				}

			}

		});
	}
}
