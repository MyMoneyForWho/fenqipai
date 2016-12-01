package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.NewsListAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.NewsList;
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
 * @Description:新闻列表页面
 * @author hunaixin
 * @date 2016年11月25日 下午1:34:29
 */
public class NewsListActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.news_list_titleBar)
	private TitleBarView titleBarView;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	@ViewInject(R.id.news_list_listview)
	private PullToRefreshListView mPullListView;

	private NewsListAdapter adapter;

	private List<NewsList> newsLists = new ArrayList<NewsList>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_list);

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

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.news_list_title);

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);
		
		listView.setDivider(null);

		emptyView.setVisibility(View.GONE);

		adapter = new NewsListAdapter(this, newsLists);

		listView.setAdapter(adapter);
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
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = false;
				loadIndex = 0;
				getNewsList();

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getNewsList();
			}

		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				Intent intent = new Intent(NewsListActivity.this,NewsDetailsActivity.class);
				intent.putExtra("url", adapter.getList().get(pos).getViewurl());
                startActivity(intent);  
			}
		});
	}
 
	/**
	 * @Description:获取新闻列表
	 * @author hunaixin
	 * @parame isLoad, curRows, pageSize
	 * @return newsLists
	 */
	public void getNewsList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				newsLists = application.getNewsList(isLoad, loadIndex * Contants.PAGE_SIZE, Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();
					
					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(newsLists);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(newsLists.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(newsLists);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						if (newsLists.size() == 0) {
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
