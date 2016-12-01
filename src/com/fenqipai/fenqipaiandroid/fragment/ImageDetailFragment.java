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

import com.fenqipai.fenqipaiandroid.ImagePagerActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.base.BaseFragment;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.Effectstype;
import com.fenqipai.fenqipaiandroid.view.NiftyDialogBuilder;
import com.fenqipai.fenqipaiandroid.view.photo.PhotoViewAttacher;
import com.fenqipai.fenqipaiandroid.view.photo.PhotoViewAttacher.OnPhotoTapListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnLongClickListener;
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
public class ImageDetailFragment extends BaseFragment {

	private ImagePagerActivity activity;

	@ViewInject(R.id.image)
	private ImageView mImageView;

	@ViewInject(R.id.loading)
	private ProgressBar progressBar;

	private PhotoViewAttacher mAttacher;

	private String mImageUrl;

	private String savePath = "";

	public static ImageDetailFragment newInstance(String imageUrl) {
		final ImageDetailFragment f = new ImageDetailFragment();

		final Bundle args = new Bundle();
		args.putString("url", imageUrl);
		f.setArguments(args);

		return f;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		activity = (ImagePagerActivity) getActivity();

		mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);

		// 注入view和事件
		ViewUtils.inject(this, v);

		mAttacher = new PhotoViewAttacher(mImageView);

		mAttacher.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View arg0, float arg1, float arg2) {
				getActivity().finish();
			}
		});

		mAttacher.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {

				final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(getActivity());

				dialogBuilder.withTitle("江湖提示").withMessage("是否保存图片到本地?")
						.withIcon(getResources().getDrawable(R.drawable.logo)).isCancelableOnTouchOutside(true)
						.withDuration(NiftyDialogBuilder.DEFAULT_DURATION).withEffect(Effectstype.Fadein)
						.withButton1Text("确定").withButton2Text("取消").setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {

								// saveImg(mImageUrl);

								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();

				return true;
			}
		});

		return v;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		// if(!mImageUrl.contains("file")){
		// if (!mImageUrl.contains("http")) {
		// mImageUrl = URL.getURL(application, mImageUrl);
		// }
		// }

		application.imageLoader.displayImage(mImageUrl, mImageView, application.options,
				new SimpleImageLoadingListener() {
					@Override
					public void onLoadingStarted(String imageUri, View view) {
						progressBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
						String message = null;
						switch (failReason.getType()) {
						case IO_ERROR:
							message = "下载错误";
							break;
						case DECODING_ERROR:
							message = "图片无法显示";
							break;
						case NETWORK_DENIED:
							message = "网络有问题，无法下载";
							break;
						case OUT_OF_MEMORY:
							message = "图片太大无法显示";
							break;
						case UNKNOWN:
							message = "未知的错误";
							break;
						}
						ToastUtils.show(getActivity(), message, ToastUtils.TOAST_LONG);
						progressBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						progressBar.setVisibility(View.GONE);
						mAttacher.update();
					}
				});

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
