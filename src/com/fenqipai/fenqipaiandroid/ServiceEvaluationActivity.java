package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;

/**
 * @Description:服务评分界面
 * @author hunaixin
 * @date 2016年11月25日 下午2:06:37
 */
public class ServiceEvaluationActivity extends BaseActivity {
	// 标题栏
	@ViewInject(R.id.service_evaluation_titleBar)
	private TitleBarView titleBarView;

	// 确认提交
	@ViewInject(R.id.confirm_btn)
	private Button confirmBtn;
	
	// 评分条
	@ViewInject(R.id.ratingbar)
	private RatingBar mRatingBar;
	
	// 反馈内容
	@ViewInject(R.id.feedback_et)
	private EditText feedbackEt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_evaluation);
		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();
	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initView() {
		application = (BaseApplication) getApplication();

		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.service_evaluation);

		
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

		mRatingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
				
			}
		});
		
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (StringUtils.isEmpty(feedbackEt.getText().toString())) {
					ToastUtils.show(ServiceEvaluationActivity.this, "反馈内容不得为空", ToastUtils.TOAST_SHORT);
				}else {
					ToastUtils.show(ServiceEvaluationActivity.this, "提交成功", ToastUtils.TOAST_SHORT);
					finish();
				}
			}
		});
	}
}