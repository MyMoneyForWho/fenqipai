package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fenqipai.fenqipaiandroid.adapter.BankCardActivityAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.NoScrollListView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Description:银行卡界面
 * @author hunaixin
 * @date 2016年11月25日 上午11:00:32
 */
public class BankCardActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.bankcard_titleBar)
	private TitleBarView titleView;

	// 添加银行卡
	@ViewInject(R.id.add_bank_card)
	private RelativeLayout addBankCard;

	// 银行卡listview
	@ViewInject(R.id.bank_card_listview)
	private NoScrollListView mList;

	private BankCardActivityAdapter mAdapter;

	private PopupWindow myPopupWindow;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	private List<BankCardList> list = new ArrayList<BankCardList>();

	private String resultMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bankcard);

		// 注入view和事件
		ViewUtils.inject(this);

		initViews();

		initEvents();

	}
  
	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initViews() {
		titleView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleView.setBtnLeft(R.drawable.btn_back);
		titleView.setTitleText(R.string.bank_card);
		
		// 获取绑定银行卡列表
		bankCardList();

	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initEvents() {
		titleView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		// 添加银行卡
		addBankCard.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(BankCardActivity.this,AddBankCardActivity.class);
				intent.putExtra("type", "BankCardActivity");
				startActivity(intent);
			}
		});

	}

	private int pos;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			View view = (View) msg.obj;
			pos = msg.arg1;
			switch (msg.what) {
			case 1:
				showPopWindow(view);
				break;

			default:
				break;
			}
		};
	};

	/**
	 * 解除绑定popWindow
	 * 
	 * @param view
	 */
	/**
	 * @Description:解除绑定popWindow
	 * @author hunaixin
	 * @parame view
	 * @return
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view) {
		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_del_bank_card, null);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		initPopWindow(v);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});
		;
	}

	private TextView confirm, back;
 
	/**
	 * @Description:初始化解除绑定popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	private void initPopWindow(View v) {
		confirm = (TextView) v.findViewById(R.id.pop_confirm);

		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myPopupWindow.dismiss();
				deleteCarNo(mAdapter.getList().get(pos).getSysId());
				list.remove(pos);
				mAdapter.setList(list);
			}
		});

		back = (TextView) v.findViewById(R.id.pop_back);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				myPopupWindow.dismiss();

			}
		});

	}

	@Override
	protected void onRestart() {
		// 获取绑定银行卡列表
		bankCardList();
		super.onRestart();
	}

	/**
	 * @Description:获取绑定银行卡列表接口
	 * @author hunaixin
	 * @parame 
	 * @return list
	 */
	public void bankCardList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.bankCardList();
				return true;

			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mAdapter = new BankCardActivityAdapter(BankCardActivity.this, list, mHandler);

					mList.setAdapter(mAdapter);
				}

				super.onPostExecute(result);
			}

		});
	}
	
	/**
	 * @Description:解除绑定银行卡接口
	 * @author hunaixin
	 * @parame cardNoId
	 * @return resultMessage
	 */
	public void deleteCarNo(final String cardNoId) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.deleteCarNo(cardNoId);
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

}
