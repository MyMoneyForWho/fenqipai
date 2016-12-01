package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.StagingBillListAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.StagingBill;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
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
 * @ClassName:StagingBillActivity
 * @Description:分期账单列表
 * @author qianyuhang
 * @date 2016-9-8 上午11:12:22
 */
public class StagingBillActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.staging_bill_titleBar)
	private TitleBarView titleBarView;

	@ViewInject(R.id.staging_list_listview)
	private PullToRefreshListView mPullListView;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	private StagingBillListAdapter adapter;

	private List<StagingBill> stagingBillList = new ArrayList<StagingBill>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staging_bill);

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullListView.doPullRefreshing(true, 500);

	}

	/**
	 * 初始化视图
	 * 
	 * @author hunaixin
	 */
	private void initView() {

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.staging_bill_title);

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);

		listView.setDivider(null);

		emptyView.setVisibility(View.GONE);

		adapter = new StagingBillListAdapter(this, stagingBillList);

		listView.setAdapter(adapter);

	}

	/**
	 * 初始化事件
	 * 
	 * @author hunaixin
	 */
	private void initEvent() {
		// 返回
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = false;
				loadIndex = 0;
				getStagingBillList();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getStagingBillList();
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				if (adapter.getList().get(pos).getOrderStatus().equals("pendingpay")) {// 待付款
					ToastUtils.show(StagingBillActivity.this, "请先确认缴纳前期提车费", ToastUtils.TOAST_SHORT);
					Intent intent = new Intent();
					intent.setClass(StagingBillActivity.this, PayConfirmActivity.class);
					intent.putExtra("orderId", adapter.getList().get(pos).getSysId());
					startActivity(intent);
				}else if (adapter.getList().get(pos).getCurrentPeriods().equals(adapter.getList().get(pos).getTotalPeriods())) {
					ToastUtils.show(StagingBillActivity.this, "您已经全部还清", ToastUtils.TOAST_SHORT);
				}else {
					Intent intent = new Intent(StagingBillActivity.this, StagingBillDetailsActivity.class);
					intent.putExtra("Id", adapter.getList().get(pos).getSysId());
					startActivity(intent);
				}
			}
		});

	}

	/**
	 * @Description:获取订单列表
	 * @author hunaixin
	 * @parame boolean isLoad, int curRows, int pageSize
	 * @return stagingBillList
	 */
	public void getStagingBillList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				stagingBillList = application.getRepaymentOrderList(isLoad, loadIndex * Contants.PAGE_SIZE,
						Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(stagingBillList);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(stagingBillList.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(stagingBillList);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						if (stagingBillList.size() == 0) {
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

}
