package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import com.fenqipai.fenqipaiandroid.adapter.StagingBillDetailsAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.StagingBillDetails;
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
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Description:分期账单详情界面
 * @author hunaixin
 * @date 2016年11月25日 下午2:08:28
 */
public class StagingBillDetailsActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.staging_bill_details_titleBar)
	private TitleBarView titleBarView;

	@ViewInject(R.id.staging_details_listview)
	private PullToRefreshListView mPullListView;

	// 分期listview
	private ListView listview;

	private StagingBillDetailsAdapter mAdapter;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	private List<StagingBillDetails> stagingBillDetails = new ArrayList<StagingBillDetails>();

	// 订单ID
	private String orderId;

	// 全选checkbox
	private CheckBox chooseAll;

	// 全选的LinearLayout
	private LinearLayout mLayout;

	// 记录选中的条目数量
	private int checkNum = 0;

	// 记录全选状态
	private Boolean isAllChoose = false;

	// 选择了多少期
	private TextView currentPeriods;

	// 订单编号
	// private TextView orderNum;

	@ViewInject(R.id.staging_pay)
	private TextView payText;

	@ViewInject(R.id.total_price)
	private TextView payMoney;

	private int choosePos;

	private Boolean ifChooseRight = false;
	
	private Boolean payClickAble = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_staging_bill_details);

		orderId = getIntent().getStringExtra("Id");

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

		mPullListView.doPullRefreshing(true, 500);

	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.staging_bill_details_title);

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listview = mPullListView.getRefreshableView();

		listview.setVerticalScrollBarEnabled(false);

		((ViewGroup) listview.getParent()).addView(emptyView);

		listview.setDivider(null);

		listview.setEmptyView(emptyView);

		emptyView.setVisibility(View.GONE);

		mAdapter = new StagingBillDetailsAdapter(this, stagingBillDetails);

		listview.setAdapter(mAdapter);

		chooseAll = (CheckBox) findViewById(R.id.choose_all);
		mLayout = (LinearLayout) findViewById(R.id.choose_all_linearlayout);
		currentPeriods = (TextView) findViewById(R.id.currentPeriods);
		// orderNum = (TextView) findViewById(R.id.order_num);
		// TextView显示最新的选中数目
		currentPeriods.setText("已选中" + checkNum + "期");

	}

	/**
	 * 初始化事件
	 * 
	 * @author hunaixin
	 */
	private void initEvent() {
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

				chooseAll.setChecked(false);

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getStagingBillList();

			}

		});

		// 全选
		mLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (isAllChoose) {
					isAllChoose = false;
					chooseAll.setChecked(isAllChoose);
					for (int i = 0; i < mAdapter.getList().size(); i++) {
						mAdapter.getList().get(i).setIsChecked(isAllChoose);
					}

				} else {
					isAllChoose = true;
					chooseAll.setChecked(isAllChoose);
					for (int i = 0; i < mAdapter.getList().size(); i++) {
						mAdapter.getList().get(i).setIsChecked(isAllChoose);
					}
				}
				mAdapter.notifyDataSetChanged();

				isCheck();

			}
		});

		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long id) {

				Boolean flag = mAdapter.getList().get(pos).getIsChecked();

				mAdapter.getList().get(pos).setIsChecked(!flag);

				mAdapter.notifyDataSetChanged();

				isCheck();

			}
		});

		payText.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 首先判断是否选择了一期
				for (int i = 0; i < mAdapter.getList().size(); i++) {
					if (mAdapter.getList().get(i).getIsChecked()) {
						payClickAble = true;
						break;
					}else {
						payClickAble = false;
						break;
					}
				}
				
				// 接着判断是否按顺序还款
				if (payClickAble) {
					for (int i = 0; i < mAdapter.getList().size(); i++) {
						if (mAdapter.getList().get(i).getIsChecked()) {
							choosePos = i;
						}
					}

					for (int j = 0; j <= choosePos; j++) {
						if (mAdapter.getList().get(j).getIsChecked()) {
							ifChooseRight = true;
						} else {
							ifChooseRight = false;
							break;
						}
					}

					if (ifChooseRight) {
						Intent intent = new Intent();
						intent.setClass(StagingBillDetailsActivity.this, PayFullActivity.class);
						Bundle bundle = new Bundle();
						bundle.putString("payType", "staging");
						Double price = 0.0;
						String idArray = "";
						String carAllName = "";
						for (int i = 0; i < mAdapter.getList().size(); i++) {
							if (mAdapter.getList().get(i).getIsChecked()) {
								price += Double.valueOf(mAdapter.getList().get(i).getRepaymentPrice());
								idArray = idArray + mAdapter.getList().get(i).getSysId() + "&";
								carAllName = mAdapter.getList().get(i).getCarAllName();
							}
						}
						bundle.putString("price", String.valueOf(price));
						bundle.putString("repaymentIdArray", idArray);
						OrderInfo orderInfo = new OrderInfo();
						orderInfo.setSysId(orderId);
						orderInfo.setCarAllName(carAllName);
						bundle.putSerializable("orderInfo", orderInfo);
						bundle.putString("activityType", "4");
						intent.putExtras(bundle);
						startActivity(intent);
					} else {
						ToastUtils.show(StagingBillDetailsActivity.this, "不能跨期还款，请按时间顺序选择", ToastUtils.TOAST_SHORT);
					}

				}else {
					ToastUtils.show(StagingBillDetailsActivity.this, "请至少选择一期", ToastUtils.TOAST_SHORT);
				}
				
			}
		});
	}

	public void isCheck() {
		int checkNum = 0;
		Double price = 0.0;
		if (mAdapter != null) {
			for (int i = 0; i < mAdapter.getList().size(); i++) {
				if (mAdapter.getList().get(i).getIsChecked()) {
					checkNum++;
					price += Double.valueOf(mAdapter.getList().get(i).getRepaymentPrice());
				}
			}
		}
		currentPeriods.setText("已选中" + checkNum + "期");
		payMoney.setText(price + "元");
	}

	/**
	 * @Description:获取分期账单列表
	 * @author hunaixin
	 * @parame String orderId, String orderRepaymentStatus, boolean isLoad, int curRows, int pageSize
	 * @return stagingBillDetails
	 */
	public void getStagingBillList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				stagingBillDetails = application.getOrderRepaymentList(orderId, "noRepayment", isLoad,
						loadIndex * Contants.PAGE_SIZE, Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						mAdapter.getList().addAll(stagingBillDetails);
						mAdapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(stagingBillDetails.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						mAdapter.setList(stagingBillDetails);
						listview.setSelection(0);
						if (stagingBillDetails.size() == 0) {
							emptyView.setVisibility(View.VISIBLE);
						} else {
							emptyView.setVisibility(View.GONE);
						}
					}
				}

			}

		});
	}

}
