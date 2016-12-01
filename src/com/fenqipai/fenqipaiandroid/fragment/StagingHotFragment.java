package com.fenqipai.fenqipaiandroid.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fenqipai.fenqipaiandroid.CarDetailsActivity;
import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.adapter.ConstellationAdapter;
import com.fenqipai.fenqipaiandroid.adapter.GirdDropDownAdapter;
import com.fenqipai.fenqipaiandroid.adapter.ListDropDownAdapter;
import com.fenqipai.fenqipaiandroid.adapter.StagingHotFragmentAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.view.DropDownMenu;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 分期热门页面              已弃用
 * 
 * @name StagingHotFragment
 * @author hunaixin
 */
public class StagingHotFragment extends BaseFragment {

	// fragment保存数据的key
	private static final String KEY_CONTENT = "CommunityFragment:Content";

	private MainActivity activity;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	@ViewInject(R.id.staging_hot_listview)
	private PullToRefreshListView mPullListView;

	private DropDownMenu mDropDownMenu;
	private String headers[] = { "城市", "年龄", "性别", "星座" };
	private List<View> popupViews;

	private List<SaleCar> list = new ArrayList<SaleCar>();

	private GirdDropDownAdapter cityAdapter;
	private ListDropDownAdapter ageAdapter;
	private ListDropDownAdapter sexAdapter;
	private ConstellationAdapter constellationAdapter;

	private String citys[] = { "不限", "武汉", "北京", "上海", "成都", "广州", "深圳", "重庆",
			"天津", "西安", "南京", "杭州" };
	private String ages[] = { "不限", "18岁以下", "18-22岁", "23-26岁", "27-35岁",
			"35岁以上" };
	private String sexs[] = { "不限", "男", "女" };
	private String constellations[] = { "不限", "白羊座", "金牛座", "双子座", "巨蟹座",
			"狮子座", "处女座", "天秤座", "天蝎座", "射手座", "摩羯座", "水瓶座", "双鱼座" };

	private int constellationPosition = 0;
	
	private StagingHotFragmentAdapter adapter;
	
	
	public static StagingHotFragment newInstance() {
		StagingHotFragment fragment = new StagingHotFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (MainActivity) getActivity();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_staging_hot,
				container, false);

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
	 * @author liuchengbao
	 */
	private void initViews(View view) {
		listView = mPullListView.getRefreshableView();
		
		listView.setVerticalScrollBarEnabled(false);
		
		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);

		emptyView.setVisibility(View.GONE);
		
		adapter = new StagingHotFragmentAdapter(
				application, list);
		
		
		listView.setAdapter(adapter);

	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvents
	 * @author liuchengbao
	 */
	private void initEvents() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent intent = new Intent(activity,
						CarDetailsActivity.class);
				
				intent.putExtra("auctionId", list.get(position).getSysId());			
				activity.startActivity(intent);
			}
		});

		mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isLoad = false;
				loadIndex = 0;
				getAuctionList() ;

			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
 				getAuctionList() ;
			}

		});

	}

	/**
	 * @Title:getAuctionList
	 * @Description:获取拍卖列表
	 * @param
	 * @return
	 * @throws
	 */

	public void getAuctionList() {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			
			
			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.getAuctionList(isLoad, loadIndex, "1");
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
	
	
	public void initDropDownMenu(){
		
		        // menu
                // mDropDownMenu = (DropDownMenu)view. findViewById(R.id.dropDownMenu);
				// init city menu
				final ListView cityView = new ListView(getContext());
				cityAdapter = new GirdDropDownAdapter(getContext(),
						Arrays.asList(citys));
				cityView.setDividerHeight(0);
				cityView.setAdapter(cityAdapter);

				// init age menu
				final ListView ageView = new ListView(getContext());
				ageView.setDividerHeight(0);
				ageAdapter = new ListDropDownAdapter(getContext(), Arrays.asList(ages));
				ageView.setAdapter(ageAdapter);

				// init sex menu
				final ListView sexView = new ListView(getContext());
				sexView.setDividerHeight(0);
				sexAdapter = new ListDropDownAdapter(getContext(), Arrays.asList(sexs));
				sexView.setAdapter(sexAdapter);

				// init constellation
				final View constellationView = getActivity().getLayoutInflater()
						.inflate(R.layout.custom_layout, null);
				GridView constellation = (GridView) constellationView
						.findViewById(R.id.constellation);
				constellationAdapter = new ConstellationAdapter(getContext(),
						Arrays.asList(constellations));
				constellation.setAdapter(constellationAdapter);
				TextView ok = (TextView) constellationView.findViewById(R.id.ok);
				ok.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mDropDownMenu
								.setTabText(constellationPosition == 0 ? headers[3]
										: constellations[constellationPosition]);
						mDropDownMenu.closeMenu();
					}
				});

				// init popupViews
				popupViews = new ArrayList<View>();
				popupViews.add(cityView);
				popupViews.add(ageView);
				popupViews.add(sexView);
				popupViews.add(constellationView);

				// add item click event
				cityView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						cityAdapter.setCheckItem(position);
						mDropDownMenu.setTabText(position == 0 ? headers[0]
								: citys[position]);
						mDropDownMenu.closeMenu();
					}
				});

				ageView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						ageAdapter.setCheckItem(position);
						mDropDownMenu.setTabText(position == 0 ? headers[1]
								: ages[position]);
						mDropDownMenu.closeMenu();
					}
				});

				sexView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view,
							int position, long id) {
						sexAdapter.setCheckItem(position);
						mDropDownMenu.setTabText(position == 0 ? headers[2]
								: sexs[position]);
						mDropDownMenu.closeMenu();
					}
				});

				constellation
						.setOnItemClickListener(new AdapterView.OnItemClickListener() {
							@Override
							public void onItemClick(AdapterView<?> parent, View view,
									int position, long id) {
								constellationAdapter.setCheckItem(position);
								constellationPosition = position;
							}
						});

				// init context view
				TextView contentView = new TextView(getContext());
				contentView.setLayoutParams(new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
				contentView.setText("内容显示区域");
				contentView.setGravity(Gravity.CENTER);
				contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
				contentView.setVisibility(View.GONE);

				// init dropdownview
				mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews,
						contentView);
		
		
		
		
	}

}
