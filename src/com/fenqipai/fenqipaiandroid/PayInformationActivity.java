package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.adapter.PayInformationAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.PayInfo;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.Effectstype;
import com.fenqipai.fenqipaiandroid.view.ExitDialogBuilder;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshBase.OnRefreshListener;
import com.fenqipai.fenqipaiandroid.view.refresh.PullToRefreshListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

/**
 * @Description:支付信息页面
 * @author hunaixin
 * @date 2016年11月25日 下午1:44:37
 */
public class PayInformationActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.order_list_titleBar)
	private TitleBarView titleBarView;

	private ListView listView;

	// false 是刷新操作 true 是加载操作
	protected boolean isLoad = true;

	// 加载索引
	protected int loadIndex = 0;

	@ViewInject(R.id.pay_information_list_listview)
	private PullToRefreshListView mPullListView;

	private PayInformationAdapter adapter;

	private List<PayInfo> payInfo = new ArrayList<PayInfo>();

	private String resultMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_information);

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
		titleBarView.setTitleText(R.string.pay_information);

		mPullListView.setPullLoadEnabled(false);
		mPullListView.setScrollLoadEnabled(true);

		listView = mPullListView.getRefreshableView();

		listView.setVerticalScrollBarEnabled(false);

		((ViewGroup) listView.getParent()).addView(emptyView);

		listView.setEmptyView(emptyView);
		listView.setDivider(null);

		emptyView.setVisibility(View.GONE);

		adapter = new PayInformationAdapter(this, payInfo,mHandler);

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
				getPayInfo();
				
			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				isLoad = true;
				loadIndex++;
				getPayInfo();
			}

		});

	}
	
	private int pos;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			@SuppressWarnings("unused")
			View view = (View) msg.obj;
			pos = msg.arg1;
			switch (msg.what) {
			case 1:
				ExitDialogBuilder(pos, adapter.getList().get(pos).getSysId());
				break;

			default:
				break;
			}
		};
	};
	
	/**
	 * 删除订单信息dialog
	 * 
	 * @author wangZhonghan
	 */
	private void ExitDialogBuilder(final int position, final String payId) {

		final ExitDialogBuilder dialogBuilder = ExitDialogBuilder.getInstance(this);

		dialogBuilder.withTitle("您是否删除此支付?").isCancelableOnTouchOutside(true)
				.withDuration(ExitDialogBuilder.DEFAULT_DURATION).withEffect(Effectstype.Fadein).withButton1Text("确定")
				.withButton2Text("取消").setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
						closePay(payId);
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();

	}
	
	/**
	 * @Description:删除支付信息
	 * @author wangZhonghan
	 * @parame payId
	 * @return resultMessage
	 */
	public void closePay(final String payId) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.closePay(payId);
					return true;
				}
				return null;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					try {
						JSONObject jObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jObject.optString("code"))) {
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);

							mPullListView.doPullRefreshing(true, 500);
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}

				super.onPostExecute(result);
			}

		});
	}


	/**
	 * @Description:获取支付列表接口
	 * @author hunaixin
	 * @parame payStatus, isLoad, curRows, pageSize
	 * @return payInfo
	 */
	public void getPayInfo() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				payInfo = application.getPayInfo("pendingpay", isLoad, loadIndex * Contants.PAGE_SIZE, Contants.PAGE_SIZE);
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mPullListView.onPullUpRefreshComplete();
					mPullListView.onPullDownRefreshComplete();

					if (isLoad) {
						// 加载更多
						adapter.getList().addAll(payInfo);
						adapter.notifyDataSetChanged();
						mPullListView.setHasMoreData(payInfo.size() != 0);
						emptyView.setVisibility(View.GONE);

					} else {
						// 刷新
						adapter.setList(payInfo);
						adapter.notifyDataSetChanged();
						listView.setSelection(0);
						if (payInfo.size() == 0) {
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
