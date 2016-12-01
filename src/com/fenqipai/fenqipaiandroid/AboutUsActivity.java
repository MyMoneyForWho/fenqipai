package com.fenqipai.fenqipaiandroid;
import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

/**
 * @Description:关于我们界面
 * @author hunaixin
 * @date 2016年11月25日 上午10:02:33
 */
public class AboutUsActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 关于公司
	@ViewInject(R.id.about_us)
	private TextView txAboutUs;
	
	// 关于公司
	@ViewInject(R.id.about_us_second)
	private TextView txAboutUsSecond;

	// 关于公司描述第一段
	private String body = "分期拍成立于2016年，是提供专业的新车及二手车线上交易和金融服务品牌，是国内领先的新车及二手车网络交易平台。我们的核心业务是“分期拍”新车网络交易服务，通过出价、安全支付、售后服务及远程物流在内的一站式服务，为国内各汽车生产厂商的品牌经销商、二手车经营机构、大型用车企业及车主提供最具时效性的新车及二手车交易服务。";
	
	// 关于公司描述第二段
	private String secondBody = "迄今，分期拍已在大连为首的各业务核心城市拥有超过千家品牌4S店级卖家用户，并与全国各地的专业买家用户达成合作，通过分期拍平台提高用户的经营效率和规模。 分期拍将继续真诚协作，求真务实，不断提高业务能力，完善服务体系，与合作伙伴携手共赢，联手创造新车及二手车流通价值，共同成长。同时，我们也真诚期待国内外新车及二手车企业及个人界的深度关注和积极参与。";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
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
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.about_us_title);
		txAboutUs.setText("    " + body);
		txAboutUsSecond.setText("    " +secondBody);
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	private void initEvent() {
		
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

	}

}
