package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.adapter.BankCardAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;
/**
 * @Description:保证金提现界面
 * @author hunaixin
 * @date 2016年11月25日 下午2:01:08
 */
public class RecognizanceWithdrawalsActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.recognizance_withdrawals_titleBar)
	private TitleBarView titleBarView;

	// 姓名
	@ViewInject(R.id.name_et)
	private EditText nameEt;

	// 银行
	@ViewInject(R.id.bank_et)
	private TextView bankEt;
	
	// 银行卡号
	@ViewInject(R.id.bank_num)
	private TextView bankNum;

	// 银行外面的linearlayout
	@ViewInject(R.id.bank_et_linearlayout)
	private LinearLayout bankLayout;

	// 支持银行下拉图标
	@ViewInject(R.id.more_bank)
	private ImageView downImg;

	// 提现金额
	@ViewInject(R.id.money_et)
	private EditText moneyEt;

	// 提现密码
	@ViewInject(R.id.password_et)
	private EditText passwordEt;

	// 确认按钮
	@ViewInject(R.id.confirm_btn)
	private Button confirmBtn;

	// 修改提现密码
	@ViewInject(R.id.change_withdrawword)
	private TextView changeWithdrawWord;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	// 整体linearlayout
	@ViewInject(R.id.activity_recognizance)
	private LinearLayout mLayout;

	private List<BankCardList> list;

	// 选择银行popWindow
	private PopupWindow myPopupWindow;
	
	private PopupWindow successPop;
	
	private PopupWindow failedPop;

	public String resultMessage;

	private String cardNoId;
	
	private float myBalance;
	
	private String cardUserName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_recognizance_withdrawals);
		
		myBalance =  Float.parseFloat(getIntent().getStringExtra("balance"));

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
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back_red);
		titleBarView.setTitleText(R.string.withdraw_title);

		list = new ArrayList<BankCardList>();
		// 支持的银行
		getBankList();

		nameEt.setOnFocusChangeListener(myListener);
		moneyEt.setOnFocusChangeListener(myListener);
		passwordEt.setOnFocusChangeListener(myListener);
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initEvents() {
		// 返回
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		bankLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopWindow(bankLayout, bankLayout.getWidth());

			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (StringUtils.isEmpty(moneyEt.getText().toString())) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "提现金额不得为空", ToastUtils.TOAST_SHORT);
				}else if (Float.parseFloat(moneyEt.getText().toString()) > myBalance) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "提现金额不得大于现有余额", ToastUtils.TOAST_SHORT);
				}else if (StringUtils.isEmpty(bankEt.getText().toString())) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "请选择提现银行", ToastUtils.TOAST_SHORT);
				}else if (StringUtils.isEmpty(nameEt.getText().toString())) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "持卡人姓名不得为空", ToastUtils.TOAST_SHORT);
				}else if (!cardUserName.equals(nameEt.getText().toString())) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "持卡人姓名与绑定银行卡用户姓名不符", ToastUtils.TOAST_SHORT);
				}else if (!isMoney(moneyEt.getText().toString())) {
					ToastUtils.show(RecognizanceWithdrawalsActivity.this, "整百起提现", ToastUtils.TOAST_SHORT);
				}else {
					withdraw();
				}
				
			}
		});

		changeWithdrawWord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizanceWithdrawalsActivity.this, ChangeLoginPasswordActivity.class);
				intent.putExtra("passwordType", "payPassword");
				startActivity(intent);
			}
		});
	}

	/**
	 * 选择银行popWindow
	 * 
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view, int width) {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}
		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_withdraw_bank, null);
		initPopWindow(v);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(width);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAsDropDown(view);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});

	}

	private ListView bankList;

	private BankCardAdapter mAdapter;

	@SuppressLint("InflateParams")
	private void initPopWindow(View v) {
		bankList = (ListView) v.findViewById(R.id.bank_listview);

		View footerView = LayoutInflater.from(this).inflate(R.layout.bank_card_footer, null);

		bankList.addFooterView(footerView);

		bankList.setAdapter(mAdapter);

		bankList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				cardUserName = list.get(position).getCardUserName();
				bankEt.setText(list.get(position).getBankName());
				bankEt.setTextColor(getResources().getColor(R.color.car_detials_name_text));
				bankNum.setText("尾号" + list.get(position).getNo());
				cardNoId = mAdapter.getList().get(position).getSysId();
				myPopupWindow.dismiss();
			}
		});

		footerView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(RecognizanceWithdrawalsActivity.this, AddBankCardActivity.class);
				intent.putExtra("type", "RecognizanceWithdrawalsActivity");
				startActivityForResult(intent, Contants.WITHDRAW_ADDCARD_REQUST);
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Contants.WITHDRAW_ADDCARD_REQUST:
			if (resultCode == Contants.WITHDRAW_ADDCARD_RETURN) {
				bankEt.setText(data.getExtras().getString("bankname"));
				bankEt.setTextColor(getResources().getColor(R.color.car_detials_name_text));
				String num = data.getExtras().getString("num"); 
				bankNum.setText("尾号" + num.substring(num.length() - 4, num.length()));
				myPopupWindow.dismiss();
			}
			break;
		}
	}

	/**
	 * 提交成功popWindow
	 * 
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void successPopWindow() {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		successPop = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_withdraw_successfully, null);
		successPop.setContentView(v);
		successPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		successPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		successPop.setBackgroundDrawable(new BitmapDrawable());
		successPop.setFocusable(true);
		successPop.setOutsideTouchable(true);
		successPop.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
		successPop.update();

		successPop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});
		;
	}
	
	/**
	 * 提交失败popWindow
	 * 
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void failedPopWindow() {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		failedPop = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_withdraw_failed, null);
		initFailedPopWindow(v);
		failedPop.setContentView(v);
		failedPop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		failedPop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		failedPop.setBackgroundDrawable(new BitmapDrawable());
		failedPop.setFocusable(true);
		failedPop.setOutsideTouchable(true);
		failedPop.showAtLocation(mLayout, Gravity.CENTER, 0, 0);
		failedPop.update();

		failedPop.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});
		;
	}
	
	private TextView failedMsg;
	
	private void initFailedPopWindow(View v) {
		failedMsg = (TextView) v.findViewById(R.id.failed_msg);
		
		
	}

	/**
	 * @Description:获取绑定银行卡
	 * @author hunaixin
	 * @parame 
	 * @return list
	 */
	public void getBankList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.bankCardList();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mAdapter = new BankCardAdapter(RecognizanceWithdrawalsActivity.this, list);
					mAdapter.notifyDataSetChanged();
					
					if (list.size() > 0) {
						cardNoId = list.get(0).getSysId();
						cardUserName = list.get(0).getCardUserName();
					}
				    
				}

				super.onPostExecute(result);
			}

		});
	}

	/**
	 * @Description:保证金提现
	 * @author hunaixin
	 * @parame String name, String cardNoId, String cashPrice, String password
	 * @return resultMessage
	 */
	public void withdraw() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.withdraw(nameEt.getText().toString(), cardNoId,
							moneyEt.getText().toString(), passwordEt.getText().toString());
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
							successPopWindow();
							new Handler().postDelayed(new Runnable() {

								@Override
								public void run() {
									finish();
								}
							}, 500);
						} else if (jObject.optString("code").equals("notSetPassword")) {
							failedPopWindow();
							failedMsg.setText(jObject.optString("message"));
						} else if (jObject.optString("code").equals("passwordError")) {
							failedPopWindow();
							failedMsg.setText(jObject.optString("message"));
						} else if (jObject.optString("code").equals("balanceIsNotEnough")) {
							failedPopWindow();
							failedMsg.setText(jObject.optString("message"));
						} else if (jObject.optString("code").equals("cardNoIdError")) {
							failedPopWindow();
							failedMsg.setText(jObject.optString("message"));
						}

					} catch (JSONException e) {

						e.printStackTrace();
					}

				}

				super.onPostExecute(result);
			}

		});
	}

	OnFocusChangeListener myListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et_no);
			} else {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et);
			}
			v.setPadding(60, 0, 0, 0);
		}
	};

	@Override
	protected void onRestart() {
		// 获取绑定银行卡列表
		getBankList();
		
		super.onRestart();
	}
	
	/**
	 * @Description:判断提现金额输入是否正确
	 * @author hunaixin
	 * @parame money
	 * @return true/false
	 */
	public static boolean isMoney(String money) {
		String str = "^(?!000$)[1-9]?[0-9]+00$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(money);
		return m.matches();
	}

}
