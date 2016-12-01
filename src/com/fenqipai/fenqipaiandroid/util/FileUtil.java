package com.fenqipai.fenqipaiandroid.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.fenqipai.fenqipaiandroid.common.Contants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Base64;

public class FileUtil {

	/**
	 * base 64 转图片
	 * 
	 * @param image
	 * @return
	 */
	public static Bitmap base64ToBitmap(String image) {
		byte[] data = Base64.decode(image, Base64.DEFAULT);
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}

	/**
	 * base64存文件
	 */
	public static void base64SaveFile(String base64Code, String fileName) {
		byte[] result = Base64.decode(base64Code, Base64.DEFAULT);

		String storageState = Environment.getExternalStorageState();

		OutputStream out1;

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL;

			File dirFile = new File(filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			File file = new File(filePath + fileName);

			if (file.exists()) {
				file.delete();
			}

			try {
				out1 = new FileOutputStream(filePath + fileName);
				out1.write(result);
				out1.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws AppException
	 * @throws IOException
	 */
	public static File saveFile(Bitmap bm, String fileName) {

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL;

			File dirFile = new File(filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			try {
				File myCaptureFile = new File(filePath + fileName);

				if (myCaptureFile.exists()) {
					myCaptureFile.delete();
				}

				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
				bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);

				bos.flush();
				bos.close();

				return myCaptureFile;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {// 先判断是否已经回收
				if (bm != null && !bm.isRecycled()) {
					// 回收并且置为null
					bm.recycle();
					bm = null;
				}
				System.gc();
			}
		}
		return null;
	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws AppException
	 * @throws IOException
	 */
	public static void saveFile(InputStream is, String fileName) {

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL;

			File dirFile = new File(filePath);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			try {
				// 1K的数据缓冲
				byte[] bs = new byte[1024];
				// 读取到的数据长度
				int len;
				// 输出的文件流
				OutputStream os = new FileOutputStream(filePath + fileName);
				// 开始读取
				while ((len = is.read(bs)) != -1) {
					os.write(bs, 0, len);
				}
				// 完毕，关闭所有链接
				os.close();
				is.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean fileIsExist(String fileName) {
		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {

			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL;

			File file = new File(filePath + fileName);

			return file.exists();

		}
		return false;
	}

	/**
	 * 获取sd卡中的图片
	 * 
	 * @param fileName
	 */
	public static Bitmap getBitmap(String fileName) {

		Bitmap bitmap = null;

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL
					+ fileName;

			bitmap = BitmapFactory.decodeFile(filePath);
		}

		return bitmap;

	}

	/**
	 * 获取sd卡中的图片
	 * 
	 * @param fileName
	 */
	public static Drawable getBitmapDrawable(String fileName) {

		Drawable drawable = null;

		String storageState = Environment.getExternalStorageState();

		if (storageState.equals(Environment.MEDIA_MOUNTED)) {
			String filePath = Environment.getExternalStorageDirectory().getAbsolutePath() + Contants.DOWN_URL
					+ fileName;

			drawable = BitmapDrawable.createFromPath(filePath);
		}

		return drawable;

	}

	/**
	 * 计算目录大小
	 * 
	 * @param dir
	 * @return
	 */
	public static long getDirSize(File dir) {
		if (dir == null) {
			return 0;
		}
		// 不是目录
		if (!dir.isDirectory()) {
			return 0;
		}
		long dirSize = 0;

		File[] files = dir.listFiles();

		for (File file : files) {
			if (file.isFile()) {
				dirSize += file.length();
			} else if (file.isDirectory()) {
				dirSize += file.length();
				// 递归调用
				dirSize += getDirSize(file);
			}

		}
		return dirSize;
	}

	/**
	 * 转换文件大小
	 * 
	 * @param fileS
	 * @return B/KB/MB/GB
	 */
	public static String formatFileSize(long fileS) {

		if (fileS == 0) {
			return "0.00B";
		}

		DecimalFormat dFormat = new DecimalFormat("#.00");

		String fileSizeString = "";

		if (fileS < 1024) {
			fileSizeString = dFormat.format((double) fileS) + "B";
		} else if (fileS < 1048576) {
			fileSizeString = dFormat.format((double) fileS / 1024) + "KB";
		} else if (fileS < 1073741824) {
			fileSizeString = dFormat.format((double) fileS / 1048576) + "MB";
		} else {
			fileSizeString = dFormat.format((double) fileS / 1073741824) + "GB";
		}
		return fileSizeString;
	}

	/**
	 * 递归删除 文件/文件夹
	 * 
	 * @param file
	 */
	public static void deleteFile(File file) {

		if (file.exists()) {
			if (file.isFile()) {
				file.delete();
			} else if (file.isDirectory()) {
				File files[] = file.listFiles();
				for (int i = 0; i < files.length; i++) {
					deleteFile(files[i]);
				}
			}
			file.delete();
		}
	}
	
	  public static File createTmpFile(Context context){

	        String state = Environment.getExternalStorageState();
	        if(state.equals(Environment.MEDIA_MOUNTED)){
	            // 已挂载
	            File pic = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(pic, fileName+".jpg");
	            return tmpFile;
	        }else{
	            File cacheDir = context.getCacheDir();
	            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
	            String fileName = "multi_image_"+timeStamp+"";
	            File tmpFile = new File(cacheDir, fileName+".jpg");
	            return tmpFile;
	        }

	    }
	  
	  
		/**
		 * 保存Bitmap 文件
		 * 
		 * @param file
		 */
	  public static File saveImage(Bitmap bmp) {
		    File appDir = new File(Environment.getExternalStorageDirectory(), "/fenqipai/image/");
		    if (!appDir.exists()) {
		        appDir.mkdir();
		    }
		    String fileName = System.currentTimeMillis() + ".jpg";
		    File file = new File(appDir, fileName);
		    try {
		        FileOutputStream fos = new FileOutputStream(file);
		        bmp.compress(CompressFormat.JPEG, 100, fos);
		        fos.flush();
		        fos.close();
		    } catch (FileNotFoundException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			return file;
		}
	  
	  
	  

}
