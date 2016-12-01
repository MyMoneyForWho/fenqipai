package com.fenqipai.fenqipaiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @Description:保证金界面
 * @author hunaixin
 * @date 2016年11月25日 下午2:00:29
 */
public class RecognizanceActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.my_money_titleBar)
	private TitleBarView titleBarView;

	// 提现
	@ViewInject(R.id.get_out_bai)
	private RelativeLayout getOutMoney;

	// 充值
	@ViewInject(R.id.get_in_bail)
	private RelativeLayout getInMoney;

	// 保证金记录
	@ViewInject(R.id.money_record)
	private RelativeLayout marginRecord;

	// 保证金余额
	@ViewInject(R.id.my_Balance)
	private TextView myBalance;

	// 冻结保证金
	@ViewInject(R.id.my_Balance_no)
	private TextView myBalNo;

	private String resultMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognizance);

		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();
	}

	/**
	 * 初始化视图
	 * 
	 * @author hunaixin
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE,
				View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.recognizance_activity_title);
		myBalance.setText("¥" + application.getUserBalance());
		myBalNo.setText("¥" + application.getUserFreezeBalance());

		getMyBalance();
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
		// 提现
		getOutMoney.setOnClickListener(new OnClickListener() {
 
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizanceActivity.this,RecognizanceWithdrawalsActivity.class);
				intent.putExtra("balance", application.getUserBalance());
                startActivity(intent);
			}
		});
		// 充值
		getInMoney.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
                Intent intent = new Intent(RecognizanceActivity.this,RechargeActivity.class);
                intent.putExtra("inputType", "RecognizanceActivity");
				startActivity(intent);
			}
		});

		// 冻结保证金记录
		marginRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(MoneyRecordActivity.class);
			}
		});

	}

	/**
	 * @Description:获取余额接口
	 */
	public void getMyBalance() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					resultMessage = application.getMyBalance();
					return true;
				}
				return false;

			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application,
								jsonObject.optString("code"))) {
							JSONObject jObject = jsonObject
									.optJSONObject("data");
							myBalance.setText("¥" + jObject.optString("balance"));
							myBalNo.setText("¥" + jObject.optString("bail"));
							SPUtils.put(application, Contants.USER_BALANCE,
									jObject.optString("balance"));

							SPUtils.put(application,
									Contants.USER_FREEZE_BALANCE,
									jObject.optString("bail"));

						} else {
							ToastUtils.show(getApplicationContext(),
									R.string.no_net, ToastUtils.TOAST_SHORT);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(getApplicationContext(), R.string.no_net,
							ToastUtils.TOAST_SHORT);
				}

				super.onPostExecute(result);
			}
    
		});
	}

	@Override
	protected void onRestart() {
		getMyBalance();
		super.onRestart();
	}
}
