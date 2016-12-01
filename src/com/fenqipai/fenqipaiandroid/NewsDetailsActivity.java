package com.fenqipai.fenqipaiandroid;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.util.WebViewUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * @Description:新闻详情webview页面
 * @author hunaixin
 * @date 2016年11月25日 下午1:34:06
 */
public class NewsDetailsActivity extends BaseActivity {

	// 公共title
	@ViewInject(R.id.news_details_titleBar)
	private TitleBarView titleBarView;
	
	// webveiw
	@ViewInject(R.id.news_details_webview)
	private WebView mWebView;
	
	// web_not_found的界面
	@ViewInject(R.id.web_not_found)
	private LinearLayout webNotFound;
	
	// webview的url
	private String url;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_news_details);
		
		url = getIntent().getStringExtra("url");

		// 注入view和事件
		ViewUtils.inject(this);

		initViews();

		initEvents();
		
		initWeb();

	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initViews() {
		titleBarView.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.VISIBLE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.news_list_title);

		
	}

	/**
	 * 初始化视图
	 * 
	 * @title initViews
	 */
	private void initEvents() {
		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
	}
	
	/**
	 * 初始化webview
	 * @author hunaixin
	 */
	private void initWeb() {
		WebViewUtils.init(this, mWebView);

		mWebView.loadUrl(url);
		
		mWebView.setWebChromeClient(new WebChromeClient(){
			@Override
			public void onReceivedTitle(WebView view, String title) {
				super.onReceivedTitle(view, title);

				// 判断标题 title 中是否包含有“error”字段，如果包含“error”字段，则设置加载失败，显示加载失败的视图
				if (title.contains("Not found")||title.contains("error")) {
					mWebView.setVisibility(View.GONE);
					webNotFound.setVisibility(View.VISIBLE);
				}
			}
		});
	}
 
	

}
