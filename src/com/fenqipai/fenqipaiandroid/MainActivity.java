package com.fenqipai.fenqipaiandroid;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

import com.fenqipai.fenqipaiandroid.base.AppManager;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.fragment.BiddingFragment;
import com.fenqipai.fenqipaiandroid.fragment.HomeFragment;
import com.fenqipai.fenqipaiandroid.fragment.InstallmentFragment;
import com.fenqipai.fenqipaiandroid.fragment.MyFragment;
import com.fenqipai.fenqipaiandroid.fragment.ServicesFragment;
import com.fenqipai.fenqipaiandroid.service.MqttService;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.Effectstype;
import com.fenqipai.fenqipaiandroid.view.ExitDialogBuilder;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.umeng.analytics.MobclickAgent;

/**
 * @Description:主界面
 * @author hunaixin
 * @date 2016年11月25日 下午1:11:33
 */
public class MainActivity extends BaseActivity {

	private RadioGroup myTabRg;
	//主页面
	private HomeFragment homeFragment;
	//竞拍
	private BiddingFragment biddingFragment;
	//分期
	private InstallmentFragment installmentFragment;
	//增值服务
	private ServicesFragment servicesFragment;
	//我的
	private MyFragment myFragment;

	@SuppressWarnings("unused")
	private String checkMsg = "";

	private FragmentManager fragmentManager = null;

	private FragmentTransaction transaction = null;

	PopupWindow popWin = null;
	
	@ViewInject(R.id.rb_merchandise)
	private RadioButton rbBidding;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		createLoadingDialog(MainActivity.this);

		// 注册EventBus
		// EventBus.getDefault().register(this);
		
		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		checkUpdate(false);

		loginState();

	}

	protected void checkUpdate(final boolean isPopup) {

	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	public void initView() {
		fragmentManager = this.getSupportFragmentManager();

		showFragment(0);

		myTabRg = (RadioGroup) findViewById(R.id.tab_menu);
		myTabRg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId) {
				case R.id.rb_home:
						showFragment(0);
					break;
				case R.id.rb_merchandise:
						showFragment(1);
					break;
				case R.id.rb_community:
						showFragment(2);
					break;
				case R.id.rb_cart:
						showFragment(3);
					break;
				case R.id.rb_my:
						showFragment(4);
					break;
				default:
					break;
				}
			}
		});
	}

	/**
	 * 用于显示在线人员数量
	 */
	private void loginState() {
		int login_type = (Integer) SPUtils.get(getApplicationContext(), Contants.LOGIN_TYPE, 0);
		switch (login_type) {
		case 1:
			MobclickAgent.onProfileSignIn("QQ", application.getUserId());
			break;
		case 2:
			MobclickAgent.onProfileSignIn("WB", application.getUserId());
			break;
		case 3:
			MobclickAgent.onProfileSignIn("WX", application.getUserId());
			break;
		case 4:
			MobclickAgent.onProfileSignIn(application.getUserId());
			break;
		default:
			break;
		}
	}

	/**
	 * 显示fragment
	 */
	@SuppressWarnings("static-access")
	public void showFragment(int index) {
		transaction = fragmentManager.beginTransaction();

		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);

		// 想要显示一个fragment,先隐藏所有fragment，防止重叠
		hideFragments(transaction);

		switch (index) {
		case 0:
			if (homeFragment != null)
				transaction.show(homeFragment);
			else {
				homeFragment = HomeFragment.newInstance();
				transaction.add(R.id.main_content, homeFragment);
			}
			break;
		case 1:
			// 如果fragment1已经存在则将其显示出来
			if (biddingFragment != null)
				transaction.show(biddingFragment);
			// 否则是第一次切换则添加fragment1，注意添加后是会显示出来的，replace方法也是先remove后add
			else {
				biddingFragment = BiddingFragment.newInstance();
				transaction.add(R.id.main_content, biddingFragment);
			}
			break;
		case 2:
			if (installmentFragment != null)
				transaction.show(installmentFragment);
			else {
				installmentFragment = installmentFragment.newInstance();
				transaction.add(R.id.main_content, installmentFragment);
			}
			break;
		case 3:
			if (servicesFragment != null)
				transaction.show(servicesFragment);
			else {
				servicesFragment = servicesFragment.newInstance();
				transaction.add(R.id.main_content, servicesFragment);
			}
			break;
		case 4:
			if (myFragment != null)
				transaction.show(myFragment);
			else {
				myFragment = MyFragment.newInstance();
				transaction.add(R.id.main_content, myFragment);
			}
			break;
		}
		transaction.commitAllowingStateLoss();
	}

	/**
	 * 当fragment已被实例化，就隐藏起来
	 */
	public void hideFragments(FragmentTransaction ft) {
		if (homeFragment != null)
			ft.hide(homeFragment);
		if (biddingFragment != null)
			ft.hide(biddingFragment);
		if (installmentFragment != null)
			ft.hide(installmentFragment);
		if (servicesFragment != null)
			ft.hide(servicesFragment);
		if (myFragment != null)
			ft.hide(myFragment);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 取消注册EventBus
//		EventBus.getDefault().unregister(this);
	}

	/**
	 * 监听返回键退出程序
	 * @author hunaixin
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (popWin != null && popWin.isShowing()) {
				popWin.dismiss();
			} else {
				AlertDialog();
			}

			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	/**
	 * 退出程序dialog
	 * @author hunaixin
	 */
	private void AlertDialog() {

		final ExitDialogBuilder dialogBuilder = ExitDialogBuilder.getInstance(MainActivity.this);

		dialogBuilder.withTitle("确定退出？").isCancelableOnTouchOutside(false).withDuration(ExitDialogBuilder.DEFAULT_DURATION)
				.withEffect(Effectstype.Fadein).withButton1Text("确定").withButton2Text("取消")
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();

						AppManager.getAppManager().finishAllActivity();
						finish();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();

	}
	@Override
	protected void onResume() {
		super.onResume();
		try {
			MqttService.actionStart(application);
		} catch (Exception e) {
			ToastUtils.show(getApplicationContext(), R.string.no_net,
					ToastUtils.TOAST_SHORT); 
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public RadioButton getRbBidding() {
		return rbBidding;
	}

	public void setRbBidding(RadioButton rbBidding) {
		this.rbBidding = rbBidding;
	}
	
    
}