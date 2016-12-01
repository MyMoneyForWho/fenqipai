package com.fenqipai.fenqipaiandroid;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.adapter.GridPricedadapter;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.HorizontalListView;
import com.fenqipai.fenqipaiandroid.view.TimeTextView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description:出价界面
 * @author hunaixin
 * @date 2016年11月25日 上午10:51:51
 */
public class AddPriceActivity extends BaseActivity {

	@ViewInject(R.id.price_gridView)
	private HorizontalListView mListView;

	@ViewInject(R.id.pop_layout)
	private LinearLayout priceLayout;

	@ViewInject(R.id.pop_confirm_layout)
	private LinearLayout confirmLayout;

	@ViewInject(R.id.back_to_price)
	private ImageView backImg;

	@ViewInject(R.id.price_pop_close)
	private ImageView closeImg;

	@ViewInject(R.id.pop_confirm_price)
	private TextView confirmPrice;

	@ViewInject(R.id.pop_bid_price)
	private TextView bidPrice;

	@ViewInject(R.id.pop_nowprice)
	private TextView nowPrice;

	@ViewInject(R.id.pop_free_bid)
	private EditText freeBidEt;

	@ViewInject(R.id.btn_price)
	private LinearLayout freeBidBtn;

	@ViewInject(R.id.confirm_bid_layout)
	private LinearLayout confirmBidLayout;

	@ViewInject(R.id.car_confirm_startingprice)
	private TextView confirmNowPrice;

	@ViewInject(R.id.confirm_now_price)
	private TextView myNowPrice;

	@ViewInject(R.id.car_countdown_timer)
	private TimeTextView countdownTimer;

	@ViewInject(R.id.car_countdown_other_timer)
	private TimeTextView otherCountDownTimer;

	private SingleCarDetails carInfo = new SingleCarDetails();

	private List<String> priceList = new ArrayList<String>();

	public String resultMessage;

	private String auctionId;

	private int resultPrice = 0;

	private String resultTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_price);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

		// carList
		Bundle bundle = getIntent().getExtras().getBundle("bundle");
		auctionId = getIntent().getStringExtra("auctionId");
		carInfo = (SingleCarDetails) bundle.getSerializable("carInfo");

		// priceList
		String[] addPrices = { "200", "400", "800", "1600", "3200" };

		for (int i = 0; i < addPrices.length; i++) {
			priceList.add(addPrices[i]);
		}

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
	private void initView() {
		// listview
		mListView.setAdapter(new GridPricedadapter(application, priceList));

		// 当前价
		nowPrice.setText(MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
		myNowPrice.setText("(当前价：" + MoneyUtils.toWan(carInfo.getNowPrice()) + "万");
		confirmNowPrice.setText(MoneyUtils.toWan(carInfo.getNowPrice()) + "万");

		getClientTime();
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initEvent() {
		// 切换动画
		final AnimationSet animationIn = new AnimationSet(true);
		TranslateAnimation tranIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1f,

				Animation.RELATIVE_TO_SELF, 0f,

				Animation.RELATIVE_TO_SELF, 0f,

				Animation.RELATIVE_TO_SELF, 0f);
		tranIn.setDuration(300);
		animationIn.addAnimation(tranIn);

		final AnimationSet animationOut = new AnimationSet(true);
		TranslateAnimation tranOut = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f,

				Animation.RELATIVE_TO_SELF, -1f,

				Animation.RELATIVE_TO_SELF, 0f,

				Animation.RELATIVE_TO_SELF, 0f);
		tranOut.setDuration(300);
		animationOut.addAnimation(tranOut);

		final AnimationSet animation = new AnimationSet(true);
		TranslateAnimation trant = new TranslateAnimation(0, 0, 0, 1);
		trant.setDuration(300);
		animation.addAnimation(trant);

		// 点击et时，直接弹出数字键盘
		freeBidEt.setInputType(EditorInfo.TYPE_CLASS_PHONE);

		freeBidBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final String freeBid = freeBidEt.getText().toString().trim();

				// 系统键盘消失
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(freeBidBtn.getWindowToken(), 0);

				// 延迟执行，先隐藏键盘再切换
				new Handler().postDelayed(new Runnable() {
					public void run() {
						// 切换
						if (freeBid.length() > 0) {
							if (Integer.valueOf(freeBid) >= 100 && Integer.valueOf(freeBid) % 100 == 0) {
								int value1 = (int) Double.parseDouble(carInfo.getNowPrice());
								int value2 = Integer.valueOf(freeBid);
								resultPrice = value1 + value2;
								confirmPrice.setText(MoneyUtils.toWan(String.valueOf(resultPrice)) + "万");

								bidPrice.setText(MoneyUtils.toWan(freeBid) + "万)");

								priceLayout.startAnimation(animationOut);
								priceLayout.setVisibility(View.GONE);
								confirmLayout.startAnimation(animationIn);
								confirmLayout.setVisibility(View.VISIBLE);
							} else {
								ToastUtils.show(AddPriceActivity.this, "请输入100或者100的整数倍", ToastUtils.TOAST_SHORT);
							}
						} else {
							ToastUtils.show(AddPriceActivity.this, "请输入报价", ToastUtils.TOAST_SHORT);
						}
					}
				}, 200);

			}
		});

		// 加价listview
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos, long arg3) {
				bidPrice.setText(MoneyUtils.toWan(priceList.get(pos)) + "万)");

				int value1 = (int) Double.parseDouble(carInfo.getNowPrice());
				int value2 = Integer.parseInt(priceList.get(pos));
				resultPrice = value1 + value2;
				confirmPrice.setText(MoneyUtils.toWan(String.valueOf(resultPrice)) + "万");

				Animation animationIn = AnimationUtils.loadAnimation(AddPriceActivity.this, R.anim.tran_in);
				Animation animationOut = AnimationUtils.loadAnimation(AddPriceActivity.this, R.anim.tran_out);

				priceLayout.startAnimation(animationOut);
				priceLayout.setVisibility(View.GONE);

				confirmLayout.startAnimation(animationIn);
				confirmLayout.setVisibility(View.VISIBLE);
			}
		});

		// 返回
		backImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultPrice = 0;
				Animation animationIn = AnimationUtils.loadAnimation(AddPriceActivity.this, R.anim.tran_in_back);
				Animation animationOut = AnimationUtils.loadAnimation(AddPriceActivity.this, R.anim.tran_out_back);
				priceLayout.startAnimation(animationIn);
				priceLayout.setVisibility(View.VISIBLE);
				confirmLayout.startAnimation(animationOut);
				confirmLayout.setVisibility(View.GONE);
			}
		});

		// 关闭
		closeImg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				resultPrice = 0;
				finish();
			}
		});

		// 确认出价
		confirmBidLayout.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (application.getLoginState()) {
					auctionRaisePrice(auctionId, String.valueOf(resultPrice));
				} else {
					startActivity(LoginActivity.class);
				}

			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int priceHeight = priceLayout.getTop();
		int confirmHeight = confirmLayout.getTop();
		int y = (int) event.getY();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (y < priceHeight || y < confirmHeight) {
				finish();
			}
		}
		return true;
	}
	
	
    /**
	 * @Description: 出价接口
	 * @author hunaixin
	 * @parame auctionId, money
	 * @return resultMessage
	 */
	public void auctionRaisePrice(final String auctionId, final String money) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.auctionRaisePrice(auctionId, money);

					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (result) {
					try {
						JSONObject jObject = new JSONObject(resultMessage);
						String code = jObject.optString("code");
						if (application.getLoginTimeOut(application, code)) {
							setResult(Contants.CAR_DETAIL_RETURN);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						} else if (code.equals("notLogined")) {// 会员未登录
							startActivity(LoginActivity.class);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						} else if (code.equals("timeout")) {// 该车竞拍时间已过！
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						} else if (code.equals("priceIsLow")) {// 金额必须大于当前最高价！
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						} else if (code.equals("error")) {// 操作异常
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						} else if (code.equals("bailIsNotEnough")) {// 保证金余额不足，拍卖保证金为1万元
							startActivity(RecognizanceActivity.class);
							ToastUtils.show(application, jObject.optString("message"), ToastUtils.TOAST_SHORT);
							finish();
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(application, R.string.no_net, ToastUtils.TOAST_SHORT);
				}

			}
		});
	}

	private Long difference = 0L;

	/**
	 * @Description:获取服务器时间接口
	 * @author hunaixin
	 * @parame 
	 * @return resultTime
	 */
	public void getClientTime() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				resultTime = application.getClientTime();
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				// 主线程 刷新UI
				if (result) {
					difference = Long.parseLong(resultTime) - 1000 - System.currentTimeMillis();
					long duration = Long.parseLong(carInfo.getEndTime()) - (System.currentTimeMillis() + difference);

					if (!countdownTimer.isRun()) {
						countdownTimer.setTimes(duration);
						countdownTimer.start();
					}

					if (!otherCountDownTimer.isRun()) {
						otherCountDownTimer.setTimes(duration);
						otherCountDownTimer.start();
					}
				}

			}

		});
	}

}
