package com.fenqipai.fenqipaiandroid.weibo;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.StringUtils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

/**
 * 微博分享
 * 
 * @name WeiBo_Share
 * @author liuchengbao
 * @Description TODO
 * @date 2015年11月9日
 * @modify
 * @modifyDate 2015年11月9日
 * @modifyContent
 */
public class WeiBo_Share {

	/** 微博分享的接口实例 */
	private IWeiboShareAPI mWeiboShareAPI;

	private Activity activity;

	public WeiBo_Share(Activity activity) {

		this.activity = activity;

		registWB();
	}

	public void registWB() {
		// 创建微博 SDK 接口实例
		mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(activity, Contants.WEIBO_APP_KEY);

		// 注册到新浪微博
		mWeiboShareAPI.registerApp();
	}

	/**
	 * 分享到微博
	 * 
	 * @title shareToWB
	 * @author liuchengbao
	 * @param title
	 *            标题
	 * @param url
	 *            网址
	 * @param description
	 *            描述
	 */
	public void shareToWB(String title, String url, String description) {
		if (mWeiboShareAPI.isWeiboAppSupportAPI()) {
			// 1. 初始化微博的分享消息
			WeiboMultiMessage weiboMessage = new WeiboMultiMessage();

			weiboMessage.imageObject = getImageObj();

			// 用户可以分享其它媒体资源（网页）
			weiboMessage.mediaObject = getWebpageObj(url, title, description);

			// 2. 初始化从第三方到微博的消息请求
			SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
			// 用transaction唯一标识一个请求
			request.transaction = String.valueOf(System.currentTimeMillis());
			request.multiMessage = weiboMessage;

			// 3. 发送请求消息到微博，唤起微博分享界面
			mWeiboShareAPI.sendRequest(activity, request);
		} else {
			Toast.makeText(activity, "微博客户端不支持 SDK 分享或微博客户端未安装或微博客户端是非官方版本。", Toast.LENGTH_SHORT).show();
		}

	}

	/**
	 * 创建图片消息对象。
	 * 
	 * @return 图片消息对象。
	 */
	private ImageObject getImageObj() {
		ImageObject imageObject = new ImageObject();
		// 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo);
		imageObject.setImageObject(bitmap);
		return imageObject;
	}

	/**
	 * 创建多媒体（网页）消息对象。
	 * 
	 * @return 多媒体（网页）消息对象。
	 */
	private WebpageObject getWebpageObj(String url, String title, String description) {
		if (StringUtils.isEmpty(description)) {
			if (StringUtils.isEmpty(title)) {
				description = "煤炭江湖";
			} else {
				description = title;
			}
		} else {
			if (description.length() > 50) {
				description = description.substring(0, 48);
			}
		}

		WebpageObject mediaObject = new WebpageObject();
		mediaObject.identify = Utility.generateGUID();
		mediaObject.title = title;
		mediaObject.description = description;

		Bitmap bitmap = BitmapFactory.decodeResource(activity.getResources(), R.drawable.logo);
		// 设置 Bitmap 类型的图片到视频对象里 设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
		mediaObject.setThumbImage(bitmap);
		mediaObject.actionUrl = url;
		mediaObject.defaultText = title;
		return mediaObject;
	}

	public IWeiboShareAPI getmWeiboShareAPI() {
		return mWeiboShareAPI;
	}

}
