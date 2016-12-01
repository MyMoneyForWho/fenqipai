package com.fenqipai.fenqipaiandroid.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import com.fenqipai.fenqipaiandroid.ImagePagerActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebStorage.QuotaUpdater;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * webView的工具类
 * 
 * @name WebViewUtils
 * @author zhaoqingyang
 * @Description
 * @date
 * @modify
 * @modifyDate
 * @modifyContent
 */
public class WebViewUtils {

	private WebViewUtils() {
		/* cannot be instantiated */
		throw new UnsupportedOperationException("WebViewUtils cannot be instantiated");
	}

	/**
	 * 初始化webview
	 * 
	 * @title getWebSettings
	 * @author zhaoqingyang
	 * @return
	 */
	@SuppressLint("SetJavaScriptEnabled")
	public static void init(final Context context, final WebView mWebView) {

		WebSettings webSettings = mWebView.getSettings();
		// 开启Javascript脚本
		webSettings.setJavaScriptEnabled(true);
		// 添加js交互接口类，并起别名 imagelistner
		mWebView.addJavascriptInterface(new JavascriptInterface(context), "imagelistener");
		// 启用localStorage 和 essionStorage
		webSettings.setDomStorageEnabled(true);
		// 启用Webdatabase数据库
		webSettings.setDatabaseEnabled(true);
		webSettings.setRenderPriority(RenderPriority.HIGH);

		if (NetUtils.isConnected(context)) {
			webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
		} else {
			webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
		}

		String appCacheDir = context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath();
		webSettings.setAppCachePath(appCacheDir);
		webSettings.setAppCacheMaxSize(1024 * 1024 * 10);// 设置缓冲大小
		webSettings.setAllowFileAccess(true);

		String databaseDir = context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
		webSettings.setDatabasePath(databaseDir);// 设置数据库路径

		// 开启应用程序缓存
		webSettings.setAppCacheEnabled(true);

		mWebView.setWebChromeClient(new WebChromeClient() {

			public void onExceededDatabaseQuota(String url, String databaseIdentifier, long currentQuota,
					long estimatedSize, long totalUsedQuota, android.webkit.WebStorage.QuotaUpdater quotaUpdater) {

				quotaUpdater.updateQuota(estimatedSize * 2);
			}

			@Override
			public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota, QuotaUpdater quotaUpdater) {
				quotaUpdater.updateQuota(spaceNeeded * 2);
			}

		});

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onLoadResource(WebView view, String url) {

				super.onLoadResource(view, url);

				// 监听器加载这是为了防止动态加载图片时新加载的图片无法预览
				addImageClickListner(view.getContext(), view);
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			// 页面开始时调用
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
			}

			// 页面加载完成调用
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
			}

			@Override
			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				super.onReceivedError(view, errorCode, description, failingUrl);

				// 用javascript隐藏系统定义的404页面信息
//				String data = "Page NO FOUND！";
//				view.loadUrl("javascript:document.body.innerHTML=\"" + data + "\"");
			}
		});

	}

	// 注入js函数监听
	public static void addImageClickListner(Context context, WebView mWebView) {

		// 这段js函数的功能就是，遍历所有的img几点，并添加onclick函数，函数的功能是在图片点击的时候调用本地java接口并传递url过去
		String imageloadJS = getFromAssets(context, "imageload.js");
		if (!TextUtils.isEmpty(imageloadJS)) {
			mWebView.loadUrl(imageloadJS);
		}
	}

	// 读取assets中的文件
	private static String getFromAssets(Context context, String fileName) {
		try {
			InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open(fileName));
			BufferedReader bufReader = new BufferedReader(inputReader);
			String line = "";
			String Result = "";
			while ((line = bufReader.readLine()) != null)
				Result += line;
			return Result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	// js通信接口
	public static class JavascriptInterface {

		private Context context;

		public JavascriptInterface(Context context) {
			this.context = context;
		}

		public void openImage(String img, int position) {

			String[] str = img.split(",");
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putInt(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
			bundle.putStringArray(ImagePagerActivity.EXTRA_IMAGE_URLS, str);
			intent.putExtras(bundle);
			intent.setClass(context, ImagePagerActivity.class);
			context.startActivity(intent);
		}
	}

	/**
	 * 清除WebView缓存
	 */
	public static void clearWebViewCache(Context context) {

		// 清理Webview缓存数据库
		try {
			context.deleteDatabase("webview.db");
			context.deleteDatabase("webviewCache.db");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// WebView 缓存文件
		File appCacheDir = new File(context.getApplicationContext().getDir("cache", Context.MODE_PRIVATE).getPath());

		File webviewCacheDir = new File(
				context.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath());

		// 删除webview 缓存目录
		if (webviewCacheDir.exists()) {
			FileUtil.deleteFile(webviewCacheDir);
		}
		// 删除webview 缓存 缓存目录
		if (appCacheDir.exists()) {
			FileUtil.deleteFile(appCacheDir);
		}
	}

}
