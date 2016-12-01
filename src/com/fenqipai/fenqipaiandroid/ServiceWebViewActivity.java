package com.fenqipai.fenqipaiandroid;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.util.WebViewUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.LinearLayout;

/**
 * 增值服务webview界面
 * @author hunaixin
 *
 */
public class ServiceWebViewActivity extends BaseActivity {
	
	@ViewInject(R.id.service_titlebar)
	private TitleBarView titlebar;
	
	@ViewInject(R.id.service_webview)
	private WebView mWebView;
	
	// web_not_found的界面
	@ViewInject(R.id.web_not_found)
	private LinearLayout webNotFound;
	
	private String type;
	
	private String title;
	
	public String resultMessage;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service_webview);
		
		Intent intent = getIntent();
		
		title = intent.getStringExtra("title");
		
		// 注入view和事件
		ViewUtils.inject(this);

		initView();

		initEvent();
		
	}

    /**
     * 初始化视图
     * @author hunaixin
     */
	private void initView() {
		titlebar.setCommonTitle(View.VISIBLE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titlebar.setBtnLeft(R.drawable.btn_back);
		titlebar.setTitleText(title);
		
		getMRepairListUrl();

	}

    /**
     * 初始化事件
     * @author hunaixin
     */
	private void initEvent() {
		titlebar.setBtnLeftOnclickListener(new OnClickListener() {
			
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
//	private void initWeb() {
//		WebViewUtils.init(this, mWebView);
//
//		mWebView.loadUrl(url);
//	}
	
	/**
	 * 获取维修与保养列表webviewUrl
	 * @author hunaixin
	 */
	public void getMRepairListUrl() {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {
				// 如果有网络 获取网络数据
				if (NetUtils.isConnected(application)) {
					resultMessage = application.getMRepairListUrl();
					
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
							
							WebViewUtils.init(ServiceWebViewActivity.this, mWebView);
							mWebView.loadUrl(jsonObject.optString("data"));
							
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
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtils.show(application, R.string.no_net, ToastUtils.TOAST_SHORT);
				}

			}
		});
	}
	
	
}
