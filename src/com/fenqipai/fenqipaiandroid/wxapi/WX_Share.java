package com.fenqipai.fenqipaiandroid.wxapi;

import java.io.ByteArrayOutputStream;
import java.io.File;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;

/**
 * 微信分享
 * 
 * @name WX_Share
 * @author liuchengbao
 * @Description TODO
 * @date 2015年10月19日
 * @modify
 * @modifyDate 2015年10月19日
 * @modifyContent
 */
public class WX_Share implements IWXAPIEventHandler {

	private Context mContext;

	// true为好友，false为朋友圈
	private boolean isFriend;

	private IWXAPI api;

	public WX_Share(Context context, boolean flag) {
		this.mContext = context;
		this.isFriend = flag;

		registWX();
	}

	public void registWX() {
		api = WXAPIFactory.createWXAPI(mContext, Contants.WEIXIN_APP_ID, false);
		api.registerApp(Contants.WEIXIN_APP_ID);
	}

	/**
	 * 微信分享
	 * 
	 * @title shareToWX
	 * @author liuchengbao
	 * @param title
	 *            标题
	 * @param url
	 *            网址
	 * @param description
	 *            描述
	 */
	public void shareToWX(String title, String url, String description, String imageUrl, BaseApplication application) {

		if (!api.isWXAppInstalled()) {
			ToastUtils.show(mContext, "您还未安装微信客户端", ToastUtils.TOAST_SHORT);
			return;
		}

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

		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = url;

		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = title;
		msg.description = description;
		if (StringUtils.isEmpty(imageUrl)) {
			Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo);
			msg.thumbData = bmpToByteArray(thumb, true);
		} else {
			msg.thumbData = getShareBitmap(imageUrl, application);
		}

		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.message = msg;

		if (isFriend) {
			req.scene = SendMessageToWX.Req.WXSceneSession;
		} else {
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}

		api.sendReq(req);
	}

	private byte[] getShareBitmap(String imageUrl, BaseApplication application) {
		Bitmap bmp = null;
		byte[] result;
		try {
			File file = application.imageLoader.getDiscCache().get(imageUrl);
			bmp = BitmapFactory.decodeFile(file.getPath());
			result = bmpToByteArray(imageZoom(bmp, 30.00), true);
			bmp.recycle();
		} catch (Exception e) {
			Bitmap thumb = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.logo);
			result = bmpToByteArray(thumb, true);
		}
		return result;
	}

	/**
	 * 压缩的办法
	 * 
	 * @param bmp
	 * @param maxSize
	 * @return
	 */
	private static Bitmap imageZoom(Bitmap bmp, double maxSize) {
		// 图片允许最大空间 单位：KB
		// double maxSize =400.00;
		// 将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
		Bitmap bitMap = bmp;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		byte[] b = baos.toByteArray();
		// 将字节换成KB
		double mid = b.length / 1024;
		// 判断bitmap占用空间是否大于允许最大空间 如果大于则压缩 小于则不压缩
		while (mid > maxSize) {
			// 获取bitmap大小 是允许最大大小的多少倍
			double i = mid / maxSize;
			// 开始压缩 此处用到平方根 将宽带和高度压缩掉对应的平方根倍
			// （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
			bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i), bitMap.getHeight() / Math.sqrt(i));
			mid = bmpToByteArray(bitMap, false).length / 1024;
		}
		return bitMap;
	}

	/***
	 * 图片的缩放方法
	 * 
	 * @param bgimage
	 *            ：源图片资源
	 * @param newWidth
	 *            ：缩放后宽度
	 * @param newHeight
	 *            ：缩放后高度
	 * @return
	 */
	public static Bitmap zoomImage(Bitmap bgimage, double newWidth, double newHeight) {
		// 获取这个图片的宽和高
		float width = bgimage.getWidth();
		float height = bgimage.getHeight();
		// 创建操作图片用的matrix对象
		Matrix matrix = new Matrix();
		// 计算宽高缩放率
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// 缩放图片动作
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width, (int) height, matrix, true);
		return bitmap;
	}

	@Override
	public void onReq(BaseReq req) {

	}

	@Override
	public void onResp(BaseResp resp) {

		String result = "";

		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "-sucessful-";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result = "-cancle-";
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = "-error code-";
			break;
		default:
			result = "-unknow  error-";
			break;
		}

		ToastUtils.show(mContext, result, ToastUtils.TOAST_SHORT);

	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

}
