package com.fenqipai.fenqipaiandroid.fragment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.fenqipai.fenqipaiandroid.ImageShowActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.photo.PhotoViewAttacher;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * 图片详情fragment
 * 
 * @name ImageDetailFragment
 * @author zhaoqingyang
 * @date 2015年11月25日
 * @modify
 * @modifyDate 2015年11月25日
 * @modifyContent
 */
public class ImageShowFragment extends BaseFragment {

	private ImageShowActivity activity;

	@ViewInject(R.id.image)
	private ImageView mImageView;

	@ViewInject(R.id.loading)
	private ProgressBar progressBar;

	@SuppressWarnings("unused")
	private PhotoViewAttacher mAttacher;

	private String mImageUrl;

	private String savePath = "";

	public static ImageShowFragment newInstance(String imageUrl) {
		final ImageShowFragment f = new ImageShowFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (ImageShowActivity) getActivity();

		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);

		// 注入view和事件
		ViewUtils.inject(this, v);

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		application.imageLoader.displayImage(mImageUrl, mImageView, application.options);

	}

	/**
	 * 保存图片到本地
	 * 
	 */
	protected void saveImg(final String imageUrl) {
		activity.putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				try {
					saveFile(imageUrl);
					return true;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return false;

			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {
					ToastUtils.show(activity, "图片保存成功" + savePath, ToastUtils.TOAST_LONG);

					scanFileAsync(activity, savePath);

					scanDirAsync(activity, Contants.DOWN_PATH);
				} else {
					ToastUtils.show(activity, "图片保存失败", ToastUtils.TOAST_LONG);
				}
			}
		});

	}

	/**
	 * 保存文件
	 * 
	 * @param bm
	 * @param fileName
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void saveFile(String imageUrl) throws ClientProtocolException, IOException {

		// httpGet连接对象
		HttpGet httpRequest = new HttpGet(imageUrl);
		// 取得HttpClient 对象
		HttpClient httpclient = new DefaultHttpClient();
		// 请求httpClient ，取得HttpRestponse
		HttpResponse httpResponse = httpclient.execute(httpRequest);
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 取得相关信息 取得HttpEntiy
			HttpEntity httpEntity = httpResponse.getEntity();
			// 获得一个输入流
			InputStream is = httpEntity.getContent();
			System.out.println(is.available());
			System.out.println("Get, Yes!");
			Bitmap bitmap = BitmapFactory.decodeStream(is);
			is.close();
			File dirFile = new File(Contants.DOWN_PATH);
			if (!dirFile.exists()) {
				dirFile.mkdir();
			}
			savePath = Contants.DOWN_PATH + System.currentTimeMillis() + ".jpg";
			File myCaptureFile = new File(savePath);
			BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myCaptureFile));
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
			bos.flush();
			bos.close();
		}

	}

	// 扫描指定文件
	public void scanFileAsync(Context ctx, String filePath) {
		Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		scanIntent.setData(Uri.fromFile(new File(filePath)));
		ctx.sendBroadcast(scanIntent);
	}

	// 扫描指定目录
	public static final String ACTION_MEDIA_SCANNER_SCAN_DIR = "android.intent.action.MEDIA_SCANNER_SCAN_DIR";

	public void scanDirAsync(Context ctx, String dir) {
		Intent scanIntent = new Intent(ACTION_MEDIA_SCANNER_SCAN_DIR);
		scanIntent.setData(Uri.fromFile(new File(dir)));
		ctx.sendBroadcast(scanIntent);
	}

}
