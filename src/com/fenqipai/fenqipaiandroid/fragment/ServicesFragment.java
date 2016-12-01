package com.fenqipai.fenqipaiandroid.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.LoginActivity;
import com.fenqipai.fenqipaiandroid.MainActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.ServiceEvaluationActivity;
import com.fenqipai.fenqipaiandroid.ServiceWebViewActivity;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
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
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * 增值服务页面
 * @name ServicesFragment
 * @author hunaixin
 */
public class ServicesFragment extends BaseFragment {

	// fragment保存数据的key
	@SuppressWarnings("unused")
	private static final String KEY_CONTENT = "ServicesFragment:Content";

	private MainActivity activity;

	@ViewInject(R.id.service_titleBar)
	private TitleBarView titleBarView;

	// 维修与保养
	@ViewInject(R.id.service_fix)
	private LinearLayout fix;

	// 延保服务
	@ViewInject(R.id.service_yanbao)
	private LinearLayout yanbao;

	// 车辆评估
	@ViewInject(R.id.service_pinggu)
	private LinearLayout pinggu;

	// 车险
	@ViewInject(R.id.service_insurance)
	private LinearLayout insurance;

	// 卖车
	@ViewInject(R.id.service_maiche)
	private LinearLayout maiche;

	// 服务评价
	@ViewInject(R.id.service_pingjia)
	private LinearLayout pingjia;

	private PopupWindow myPopupWindow;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	public TextView confirm, back;
	
	private String resultMessage;
	
	private String serviceType;

	public static ServicesFragment newInstance() {
		ServicesFragment fragment = new ServicesFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (MainActivity) getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_service, container, false);

		ViewUtils.inject(this, rootView);

		initView();

		initEvents();

		return rootView;
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 * @author hunaixin
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setTitleText(R.string.service_title);

	}

	/**
	 * 初始化事件
	 * 
	 * @title initEvents
	 * @author hunaixin
	 */
	private void initEvents() {
		// 维修与保养
		fix.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					Intent intent = new Intent(activity, ServiceWebViewActivity.class);
					intent.putExtra("title", "维修与保养");
					startActivity(intent);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

		// 延保服务
		yanbao.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					serviceType = "ExtendedWarranty";
					showPopWindow(v);
				} else {
					startActivity(LoginActivity.class);
				}

			}
		});

		pinggu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					serviceType = "Evaluations";
					showPopWindow(v);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

		insurance.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					serviceType = "Insurance";
					showPopWindow(v);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

		maiche.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					serviceType = "SellCar";
					showPopWindow(v);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

		pingjia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					startActivity(ServiceEvaluationActivity.class);
				} else {
					startActivity(LoginActivity.class);
				}
			}
		});

	}

	/**
	 * 提交增值服务popWindow
	 * 
	 * @param view
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view) {

		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		myPopupWindow = new PopupWindow(activity);
		View v = LayoutInflater.from(activity).inflate(R.layout.popupwindows_buy_car, null);
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

	public void initPopWindow(View v) {
		confirm = (TextView) v.findViewById(R.id.pop_confirm);
		final EditText mobile = (EditText) v.findViewById(R.id.popup_mobile);
		confirm.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!StringUtils.isEmpty(mobile.getText().toString())
						&& StringUtils.judgePhoneNums(mobile.getText().toString())) {
					inserServicePhone(serviceType,mobile.getText().toString());
				} else {
					ToastUtils.show(application, "请检查手机号是否正确", ToastUtils.TOAST_SHORT);
				}

				myPopupWindow.dismiss();

			}
		});

	}


	/**
	 * @Title: buyApply 
	 * @Description: 提交增值服务
	 * @param  服务类型： Evaluations车辆评估, SellCar我要买车， Insurance代办车险，ExtendedWarranty延保服务
	 */
	public void inserServicePhone(final String serviceType, final String mobile) {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				if (NetUtils.isConnected(application)) {
					resultMessage = application.inserServicePhone(serviceType, mobile);
					return true;
				}

				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {

				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(resultMessage);
						if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
							ToastUtils.show(activity, jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
						} else {
							ToastUtils.show(activity, jsonObject.optString("message"), ToastUtils.TOAST_SHORT);
							myPopupWindow.dismiss();

						}
					} catch (JSONException e) {
						e.printStackTrace();
					}

				} else {
					ToastUtils.show(application, R.string.no_net, ToastUtils.TOAST_SHORT);
				}
				super.onPostExecute(result);
			}
		});
	}

}
