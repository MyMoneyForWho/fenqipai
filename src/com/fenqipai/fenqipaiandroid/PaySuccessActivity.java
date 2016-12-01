package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * @Description:充值成功界面
 * @author hunaixin
 * @date 2016年11月25日 下午1:57:08
 */
public class PaySuccessActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.pay_success_titleBar)
	private TitleBarView titleBarView;

	// 确认button
	@ViewInject(R.id.confirm_btn)
	private Button confirmBtn;
	
	// 车名
	@ViewInject(R.id.car_name)
	private TextView carName;

	// 金额
	@ViewInject(R.id.pay_money)
	private TextView payMoney;

	// 支付通道
	@ViewInject(R.id.come_form)
	private TextView comeForm;

	// 是否成功
	@ViewInject(R.id.type)
	private TextView carType;

	//页面来源，及出口    1,支付列表页  payInformationActivity
	//            2,分期列表页 payStagingAcitvity
	private String activityType;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pay_success);
		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();

	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	private void initView() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setBtnRight(R.drawable.btn_phone_white);
		titleBarView.setTitleText(R.string.titlebar_pay_success);
		
		activityType=getIntent().getStringExtra("activityType");
		String name=getIntent().getStringExtra("name");
		String money=getIntent().getStringExtra("money");
		String fome=getIntent().getStringExtra("fome");
		
		if (name != null) {
			carName.setText(name);
		}else {
			carName.setText("充值");
		}
	
		payMoney.setText(money);
		comeForm.setText(fome);
		carType.setText("付款成功");
		
	}

	/**
	 * 初始化组件
	 * 
	 * @author hunaixin
	 */
	private void initEvent() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				if (!TextUtils.isEmpty(activityType)&&activityType.equals("1")) {
					intent.setClass(PaySuccessActivity.this, PayInformationActivity.class);
				}else if(activityType.equals("2")){
					intent.setClass(PaySuccessActivity.this, OrderListActivity.class);
				}else if(activityType.equals("3")){
					finish();
					return;
				}else if (activityType.equals("4")) {
					intent.setClass(PaySuccessActivity.this, StagingBillDetailsActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
		});

		titleBarView.setBtnRightOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 跳转到拨号界面，同时传递电话号码
				Intent dialIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(Contants.SERVICE_TELEPHONE));
				startActivity(dialIntent);
			}
		});

		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				Intent intent=new Intent();
				if (!TextUtils.isEmpty(activityType)&&activityType.equals("1")) {
					intent.setClass(PaySuccessActivity.this, PayInformationActivity.class);
				}else if(activityType.equals("2")){
					intent.setClass(PaySuccessActivity.this, OrderListActivity.class);
				}else if(activityType.equals("3")){
					finish();
					return;
				}else if (activityType.equals("4")) {
					intent.setClass(PaySuccessActivity.this, StagingBillActivity.class);
				}
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
				
			
			}
		});

	}
    
    /** 
     * 监听Back键按下事件,方法2: 
     * 注意: 
     * 返回值表示:是否能完全处理该事件 
     * 在此处返回false,所以会继续传播该事件. 
     * 在具体项目中此处的返回值视情况而定. 
     */  
     @Override  
     public boolean onKeyDown(int keyCode, KeyEvent event) {  
         if ((keyCode == KeyEvent.KEYCODE_BACK)) {  
          	Intent intent=new Intent();
			if (!TextUtils.isEmpty(activityType)&&activityType.equals("1")) {
				intent.setClass(PaySuccessActivity.this, PayInformationActivity.class);
			}else if(activityType.equals("2")){
				intent.setClass(PaySuccessActivity.this, OrderListActivity.class);
			}else if(activityType.equals("3")){
				finish();
				return true;
			}
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
              return true;  
         }else {  
             return super.onKeyDown(keyCode, event);  
         }  
           
     } 
     
     
     
	public void finishToSwitch(){
		
	}
	

}
