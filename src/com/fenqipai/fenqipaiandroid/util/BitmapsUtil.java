package com.fenqipai.fenqipaiandroid.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import net.bither.util.NativeUtil;

/**
 * 图片处理工具类
 * 
 * @name BitmapsUtils
 * @author liuchengbao
 * @Description TODO
 * @date 2015年12月28日
 * @modify
 * @modifyDate 2015年12月28日
 * @modifyContent
 */
public class BitmapsUtil {

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			inSampleSize = heightRatio <= widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap1(String filePath, BaseApplication application, int w, int h) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, h, w);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		FileInputStream fs = null;
		Bitmap bmp = null;

		try {
			fs = new FileInputStream(filePath);

			if (fs != null) {
				bmp = BitmapFactory.decodeFileDescriptor(fs.getFD(), null, options);
			}
		} catch (Exception e) {
			application.onLowMemory();
		}

		return bmp;
	}

	/**
	 * 根据路径获得图片并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static File getSmallBitmap(String filePath, BaseApplication application) {

		File dirFile = new File(Contants.FILE_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		File file = new File(Contants.FILE_PATH + System.currentTimeMillis() + ".jpg");

		Bitmap bitmap = null;

		try {
			// 如果不是64位cpu
			if (!AppUtils.checkIfCPUx86(application)) {

				bitmap = getSmallBitmap1(filePath, application, 800, 1200);

				NativeUtil.compressBitmap(bitmap, 80, file.getAbsolutePath(), true);
			} else {
				bitmap = getSmallBitmap1(filePath, application, 480, 800);

				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
				bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

				bos.flush();
				bos.close();

			}

			if (bitmap != null) {
				bitmap.recycle();

				bitmap = null;
			}

			System.gc();

		} catch (OutOfMemoryError ofMemoryError) {
			ToastUtils.show(application, "加载的图片过大", ToastUtils.TOAST_LONG);
			application.imageLoader.clearMemoryCache();
			System.gc();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}

		return file;
	}

	/**
	 * 获取本地相册图片地址
	 */
	public static String getImagePath(Uri uri, Context context) {
		Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
		cursor.moveToFirst();
		String document_id = cursor.getString(0);
		document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
		cursor.close();

		cursor = context.getContentResolver().query(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null,
				MediaStore.Images.Media._ID + " = ? ", new String[] { document_id }, null);
		cursor.moveToFirst();
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		cursor.close();

		return path;
	}

}
