package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.MoneyRecordAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.Moneyrecord;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @Description:保证金记录
 * @author qianyuhang
 * @date 2016-9-5 下午4:15:38
 */
public class MoneyRecordActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.money_titleBar)
	private TitleBarView titleBarView;

	@SuppressWarnings("unused")
	private String resultMessage;

	@ViewInject(R.id.money_list_listview)
	private PullToRefreshListView mPullListView;

	private ListView listView;

	private MoneyRecordAdapter adapter;

	private List<Moneyrecord> MoneyList = new ArrayList<Moneyrecord>();

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_money_record);

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullListView.doPullRefreshing(true, 500);
	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame
	 * @return
	 */
	public void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText("保证金记录");

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);

		listView.setDivider(null);

		emptyView.setVisibility(View.GONE);

		adapter = new MoneyRecordAdapter(this, MoneyList);

		listView.setAdapter(adapter);
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame
	 * @return
	 */
	public void initEvent() {
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
				getMoneyRecord();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getMoneyRecord();
			}

		});

	}

	/**
	 * @Description:获取保证金记录接口
	 * @author hunaixin
	 * @parame isLoad, curRows, pageSize
	 * @return MoneyList
	 */
	public void getMoneyRecord() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				MoneyList = application.getBailList(isLoad, loadIndex * Contants.PAGE_SIZE, Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(MoneyList);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(MoneyList.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(MoneyList);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						if (MoneyList.size() == 0) {
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
