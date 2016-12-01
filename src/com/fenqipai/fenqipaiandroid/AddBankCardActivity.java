package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.adapter.AddBankCardAdapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.BankList;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
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
 * @Description:添加银行卡界面
 * @author hunaixin
 * @date 2016年11月25日 上午9:58:14
 */
public class AddBankCardActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.add_bankcard_titleBar)
	private TitleBarView titleView;

	// 银行名字
	@ViewInject(R.id.bank_et)
	private TextView bankName;

	// 选择银行图标
	@ViewInject(R.id.more_bank)
	private ImageView chooseBank;

	// 选择银行
	@ViewInject(R.id.bank_et_linearlayout)
	private LinearLayout chooseLayout;

	// 选择银行popWindow
	private PopupWindow myPopupWindow;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	// 确认btn
	@ViewInject(R.id.add_bankcard_conifrm)
	private Button confirmBtn;

	// 银行卡号et
	@ViewInject(R.id.card_num_et)
	private EditText noEt;

	// 持卡人姓名et
	@ViewInject(R.id.name_et)
	private EditText nameEt;

	// true 确认 false 不确认
	@SuppressWarnings("unused")
	private Boolean flag = false;

	private List<BankList> list;

	public String resultMessage;

	private String bankId;

	private String type;

	private ListView bankList;

	private AddBankCardAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_bankcard);

		type = getIntent().getStringExtra("type");

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
		titleView.setTitleText(R.string.activity_add_bankcard);

		list = new ArrayList<BankList>();

		getBankList();

		nameEt.setOnFocusChangeListener(myListener);
		noEt.setOnFocusChangeListener(myListener);

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

		// 选择银行popWindow
		chooseLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showPopWindow(v, chooseLayout.getWidth());

			}
		});

		// 确认btn
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				if (!isBankCardNum(noEt.getText().toString().trim())) {
					ToastUtils.show(AddBankCardActivity.this, "银行卡号格式不正确", ToastUtils.TOAST_SHORT);
				}else {
					insertCarNo(noEt.getText().toString().trim(), bankId, nameEt.getText().toString().trim());
				}
				
			}
		});

	}

	/**
	 * 选择银行popWindow
	 * 
	 * @param view
	 */
	
	/**
	 * @Description:选择银行popWindow
	 * @author hunaixin
	 * @parame view
	 * @return
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view, int width) {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.popupwindows_choose_bank, null);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(width);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAsDropDown(view);
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

	/**
	 * @Description:初始化选择银行popw组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	private void initPopWindow(View v) {
		bankList = (ListView) v.findViewById(R.id.bank_listview);

		bankList.setAdapter(mAdapter);

		bankList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				list = mAdapter.getList();
				for (int i = 0; i < list.size(); i++) {
					list.get(i).setIsChoose(false);

				}
				list.get(position).setIsChoose(true);
				bankName.setText(list.get(position).getName());
				bankName.setTextColor(getResources().getColor(R.color.car_detials_name_text));
				bankId = list.get(position).getSysId();
				mAdapter.setList(list);
				myPopupWindow.dismiss();
			}
		});

	}

	/**
	 * @Description:获取支持银行列表接口
	 * @author hunaixin
	 * @parame 
	 * @return list
	 */
	public void getBankList() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				list = application.getBankList();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					mAdapter = new AddBankCardAdapter(AddBankCardActivity.this, list);

				}

				super.onPostExecute(result);
			}

		});
	}

	/**
	 * @Description:添加银行卡接口
	 * @author hunaixin
	 * @parame no, bankId, cardUserName
	 * @return resultMessage
	 */
	public void insertCarNo(final String no, final String bankId, final String cardUserName) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.insertCarNo(no, bankId, cardUserName);
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
							// 绑定成功之后返回银行卡列表页,如果从保证金提现界面跳转过来则回到保证金提现界面
							if (type.equals("RecognizanceWithdrawalsActivity")) {
								Intent intent = new Intent();
								intent.putExtra("bankname", bankName.getText().toString());
								intent.putExtra("num", noEt.getText().toString().trim());
								setResult(Contants.WITHDRAW_ADDCARD_RETURN, intent);
								finish();
							} else {
								finish();
							}

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
	 * @Description: 输入框改变背景监听方法
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
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
	
	/**
	 * @Description:判断昵称输入密码是否正确
	 * @author hunaixin
	 * @parame num
	 * @return true/false
	 */
	public static boolean isBankCardNum(String num) {
		String str = "^([0-9]{16}|[0-9]{19})$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(num);
		return m.matches();
	}
}
