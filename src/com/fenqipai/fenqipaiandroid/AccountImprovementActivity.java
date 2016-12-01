package com.fenqipai.fenqipaiandroid;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseActivity;
import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.FileUtil;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.StringUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.ChangeDatePopwindow;
import com.fenqipai.fenqipaiandroid.view.CircleImageView;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

/**
 * @Description:用户信息完善界面
 * @author hunaixin
 * @date 2016年11月25日 上午10:07:04
 */
public class AccountImprovementActivity extends BaseActivity {

	// 标题栏
	@ViewInject(R.id.cart_titleBar)
	private TitleBarView titleBarView;

	// 头像布局
	@ViewInject(R.id.image)
	private LinearLayout llImage;

	// 头像
	@ViewInject(R.id.my_img)
	private CircleImageView headImg;

	// 真实姓名
	@ViewInject(R.id.true_name)
	private EditText svtTrueName;

	// 身份证
	@ViewInject(R.id.id_card)
	private EditText svIdCard;

	// 性别
	@ViewInject(R.id.sex)
	private TextView svSex;

	// 头性别布局
	@ViewInject(R.id.sex_linearlayout)
	private LinearLayout sex;

	// 生日布局
	@ViewInject(R.id.brth_linearlayout)
	private LinearLayout birthday;

	// 生日
	@ViewInject(R.id.brth)
	private TextView svBrth;

	// 职业
	@ViewInject(R.id.job)
	private EditText svJob;

	// 手机
	@ViewInject(R.id.phone_number)
	private EditText phoneNumber;

	// 手机号linearlayout
	@ViewInject(R.id.phone)
	private LinearLayout phone;

	// 地址
	@ViewInject(R.id.address_text)
	private EditText addressText;

	// 地址linearlayout
	@ViewInject(R.id.address)
	private LinearLayout address;

	// 昵称
	@ViewInject(R.id.pet_name)
	private EditText svPetName;

	// 邮箱
	@ViewInject(R.id.e_mail)
	private EditText svEmail;

	// popWindow背景层
	@ViewInject(R.id.pop_background)
	private LinearLayout background;

	// 确认btn
	@ViewInject(R.id.conifrm)
	private Button conifrmBtn;

	// 头像popWindow
	private PopupWindow myPopupWindow;

	// 性别popWindow
	private PopupWindow mySexPopupWindow;

	// 头像文件名称
	private static final String IMAGE_FILE_NAME = "avatarImage.jpg";

	// 记录路径
	@SuppressWarnings("unused")
	private String flag;

	private String message;

	// private UserInfo userInfo;

	public ImageLoader headImgLoader;

	public DisplayImageOptions options;

	private ChangeDatePopwindow mChangeBirthDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_improvement);
		// 注入view和事件
		ViewUtils.inject(this);

		headImgLoader = ImageLoader.getInstance();

		initHeadImageLoader();

		initView();

		initEvent();

	}

	/**
	 * @Description:初始化视图
	 * @author hunaixin
	 * @parame
	 * @return
	 */
	public void initView() {
		application = (BaseApplication) getApplication();
		titleBarView.setCommonTitle(View.GONE, View.GONE, View.VISIBLE, View.GONE, View.GONE);
		titleBarView.setBtnLeft(R.drawable.btn_back);
		titleBarView.setTitleText(R.string.accountImprovement_activity_title);

		svtTrueName.setOnFocusChangeListener(myListener);
		svIdCard.setOnFocusChangeListener(myListener);
		svEmail.setOnFocusChangeListener(myListener);
		svPetName.setOnFocusChangeListener(myListener);
		svJob.setOnFocusChangeListener(myListener);
		phoneNumber.setOnFocusChangeListener(myListener);
		addressText.setOnFocusChangeListener(myListener);

		phoneNumber.setText(getIntent().getStringExtra("phone"));

		// 选择生日popWindow
		Calendar c = Calendar.getInstance();
		mChangeBirthDialog = new ChangeDatePopwindow(AccountImprovementActivity.this);
		mChangeBirthDialog.setDate(String.valueOf(c.get(Calendar.YEAR)), String.valueOf(c.get(Calendar.MONTH)),
				String.valueOf(c.get(Calendar.DATE)));
	}

	/**
	 * @Description:初始化事件
	 * @author hunaixin
	 * @parame
	 * @return
	 */
	public void initEvent() {

		titleBarView.setBtnLeftOnclickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		// 头像布局
		llImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showPopWindow(v);
			}
		});
		// 真实姓名
		svtTrueName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		// 身份证
		svIdCard.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		// 性别
		sex.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showSexPopWindow(v);
			}
		});
		// 职业
		svJob.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
			}
		});
		// 生日
		birthday.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mChangeBirthDialog.showAtLocation(findViewById(R.id.account_manager_lin), Gravity.BOTTOM, 0, 0);
				mChangeBirthDialog.setBirthdayListener(new ChangeDatePopwindow.OnBirthListener() {

					@Override
					public void onClick(String year, String month, String day) {
						svBrth.setText(year + month + day);
						svBrth.setTextColor(getResources().getColor(R.color.car_detials_name_text));
					}
				});

			}
		});
		// 手机号
		phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		// 地址
		address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			}
		});
		// 昵称
		svPetName.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		// 邮箱
		svEmail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});
		// 确认btn
		conifrmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String nickname = svPetName.getText().toString();
				String email = svEmail.getText().toString();
				String truename = svtTrueName.getText().toString();
				String sex = svSex.getText().toString();
				String phone = phoneNumber.getText().toString();

				if (StringUtils.isEmpty(phone)) {
					ToastUtils.show(getApplicationContext(), "请输入手机号", ToastUtils.TOAST_SHORT);
					return;
				} else if (StringUtils.isEmpty(nickname)) {
					ToastUtils.show(getApplicationContext(), "请输入昵称", ToastUtils.TOAST_SHORT);
					return;
				} else if (!StringUtils.judgePhoneNums(phone)) {
					ToastUtils.show(getApplicationContext(), "手机号格式错误", ToastUtils.TOAST_SHORT);
					return;
				} else if (StringUtils.isEmpty(truename)) {
					ToastUtils.show(getApplicationContext(), "请输入真实姓名", ToastUtils.TOAST_SHORT);
					return;
				} else if (StringUtils.isEmpty(sex)) {
					ToastUtils.show(getApplicationContext(), "请输入性别", ToastUtils.TOAST_SHORT);
					return;
				} else if (!StringUtils.isEmpty(email) && !isEmail(email)) {
					ToastUtils.show(getApplicationContext(), "邮箱格式错误", ToastUtils.TOAST_SHORT);
				} else if (!StringUtils.isEmpty(svIdCard.getText().toString())
						&& !isIdCard(svIdCard.getText().toString())) {
					ToastUtils.show(getApplicationContext(), StringUtils.IDCardValidate(svIdCard.getText().toString()),
							ToastUtils.TOAST_SHORT);
				} else {
					updateNbuser(null, nickname, truename, sex, svBrth.getText().toString(), email,
							svIdCard.getText().toString(), addressText.getText().toString(),
							svJob.getText().toString());
				}

			}
		});
	}

	/**
	 * @Description:头像imageLoader配置
	 * @author hunaixin
	 * @parame
	 * @return
	 */
	public void initHeadImageLoader() {
		ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(this);
		config.threadPoolSize(3);// 线程池内加载的数量
		config.threadPriority(Thread.NORM_PRIORITY - 2);
		config.denyCacheImageMultipleSizesInMemory();// 不缓存图片的多种尺寸在内存中
		config.discCacheFileNameGenerator(new Md5FileNameGenerator());// 将保存的时候的URI名称用MD5
		config.discCacheSize(50 * 1024 * 1024); // 50 MiB
		config.tasksProcessingOrder(QueueProcessingType.LIFO);
		config.writeDebugLogs();// Remove for release app
		// 初始化ImageLoader
		ImageLoader.getInstance().init(config.build());

		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.user)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.user)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.user)// 设置图片加载/解码过程中错误时候显示的图片
				.delayBeforeLoading(100)// 设置延时多少时间后开始下载
				.cacheInMemory(true)// 设置下载的图片是否缓存在内存中
				.cacheOnDisc(true)// 设置下载的资源是否缓存在SD卡中
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)// 设置图片以何种编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565) // 设置图片的解码类型
				.displayer(new RoundedBitmapDisplayer(20))// 是否设置为圆角，弧度为多少
				.displayer(new FadeInBitmapDisplayer(100))// 是否图片加载好后渐入的动画时间
				.build();

	}

	/**
	 * @Description:更换头像popWindow
	 * @author hunaixin
	 * @parame view
	 * @return
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showPopWindow(View view) {
		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}
		myPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.head_img_check, null);
		myPopupWindow.setContentView(v);
		myPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		myPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		myPopupWindow.setFocusable(true);
		myPopupWindow.setOutsideTouchable(true);
		myPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		initPopWindow(v);
		myPopupWindow.update();

		myPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});

	}

	/**
	 * @Description:初始化更换头像popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	public void initPopWindow(View v) {
		LinearLayout takePhoto = (LinearLayout) v.findViewById(R.id.auth_photo);
		LinearLayout checkPhoto = (LinearLayout) v.findViewById(R.id.checkPhoto);
		File dirFile = new File(Contants.DOWN_PATH);
		if (!dirFile.exists()) {
			dirFile.mkdirs();
		}
		takePhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent takeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				// 下面这句指定调用相机拍照后的照片存储的路径
				takeIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						Uri.fromFile(new File(Contants.DOWN_PATH, IMAGE_FILE_NAME)));
				takeIntent.addCategory(Intent.CATEGORY_DEFAULT);
				startActivityForResult(takeIntent, Contants.PICK_PHOTO_REQUEST_CODE);

				if (myPopupWindow != null && myPopupWindow.isShowing()) {
					myPopupWindow.dismiss();
				}
			}
		});
		checkPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(AccountImprovementActivity.this, MultiImageSelectorActivity.class);
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SHOW_CAMERA, true);
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_COUNT, 1);
				intent.putExtra(MultiImageSelectorActivity.EXTRA_SELECT_MODE, MultiImageSelectorActivity.MODE_MULTI);
				startActivityForResult(intent, Contants.TAKE_PICTURE_REQUEST);

				if (myPopupWindow != null && myPopupWindow.isShowing()) {
					myPopupWindow.dismiss();
				}
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case Contants.TAKE_PICTURE_REQUEST:
			if (resultCode == RESULT_OK) {
				// 获取返回的图片列表
				ArrayList<String> pathLists = data.getStringArrayListExtra(MultiImageSelectorActivity.EXTRA_RESULT);

				File temp = new File(pathLists.get(0));
				flag = pathLists.get(0);
				startPhotoZoom(Uri.fromFile(temp));

			}
			break;
		case Contants.PICK_PHOTO_REQUEST_CODE:
			// 拍照
			flag = Contants.DOWN_PATH + IMAGE_FILE_NAME;
			if (data != null) {

			} else {
				File temp = new File(Contants.DOWN_PATH + IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(temp));
			}

			break;
		case Contants.CUTTING_REQUESTCODE:
			// 裁剪
			if (data != null && resultCode != 0) {
				Bundle extras = data.getExtras();
				Bitmap photo = extras.getParcelable("data");
				File file = FileUtil.saveImage(photo);

				// 删除拍照
				Map<String, File> files = new HashMap<String, File>();
				files.put("portrait", file);

				String nickname = svPetName.getText().toString();
				String email = svEmail.getText().toString();
				String truename = svtTrueName.getText().toString();
				String sex = svSex.getText().toString();
				// String phone = phoneNumber.getText().toString();
				ajax_updateNbuser(null, nickname, truename, sex, svBrth.getText().toString(), email,
						svIdCard.getText().toString(), addressText.getText().toString(), svJob.getText().toString(),
						files);
			}
			break;
		}
	}

	/**
	 * @Description:裁剪图片方法实现
	 * @author hunaixin
	 * @parame uri
	 * @return
	 */
	public void startPhotoZoom(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽高
		intent.putExtra("outputX", Contants.PORTRAIT_SIZE);
		intent.putExtra("outputY", Contants.PORTRAIT_SIZE);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, Contants.CUTTING_REQUESTCODE);
	}

	/**
	 * @Description:头像上传接口
	 * @author hunaixin
	 * @parame account,nickName,trueName,sex,birthday,email,card,address,job,files
	 * @return message
	 */
	public void ajax_updateNbuser(String account, final String nickName, final String trueName, final String sex,
			final String birthday, final String email, final String card, final String address, final String job,
			final Map<String, File> files) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected void onPreExecute() {
				loadingDialogShow("头像上传中");
				super.onPreExecute();
			}

			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(AccountImprovementActivity.this)) {
					message = application.updateUserHeadImg(null, nickName, trueName, sex, birthday, email, card,
							address, job, files);
					return true;
				}

				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				loadingDialogDismiss();
				if (result) {
					try {
						JSONObject jsonObject = new JSONObject(message);
						if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
							SPUtils.putPortrait(application, Contants.USER_PORTRAIT, jsonObject.optString("data"));

						}
						headImgLoader.displayImage(application.getPortrait(), headImg, options);

					} catch (JSONException e) {
						e.printStackTrace();
					}

					files.get("portrait").delete();

				}

				super.onPostExecute(result);
			}
		});

	}

	/**
	 * @Description:修改个人信息接口
	 * @author hunaixin
	 * @parame account, nickName, trueName, sex, birthday, email, card, address,job
	 * @return message
	 */
	protected void updateNbuser(String account, final String nickName, final String trueName, final String sex,
			final String birthday, final String email, final String card, final String address, final String job) {
		putAsyncTask(new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void... params) {
				if (NetUtils.isConnected(application)) {
					message = application.updateNbuser(null, nickName, trueName, sex, birthday, email, card, address,
							job);

					return true;
				}
				return false;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				super.onPostExecute(result);

				if (result) {
					try {
						JSONObject jObject = new JSONObject(message);
						if (application.getLoginTimeOut(application, jObject.optString("code"))) {
							ToastUtils.show(AccountImprovementActivity.this, jObject.optString("message"), 1000);
							// 填写完毕，回到主页面
							startActivity(MainActivity.class);
						} else if (jObject.optString("code").equals("fail")) {
							ToastUtils.show(AccountImprovementActivity.this, jObject.optString("message"), 1000);
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

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mChangeBirthDialog != null && mChangeBirthDialog.isShowing()) {
			mChangeBirthDialog.dismiss();
			mChangeBirthDialog = null;
		}
		return super.onTouchEvent(event);
	}

	
	/**
	 * @Description: 选择性别popWindow
	 * @author hunaixin
	 * @parame view
	 * @return
	 */
	@SuppressLint("InflateParams")
	@SuppressWarnings("deprecation")
	public void showSexPopWindow(View view) {
		if (background.getVisibility() == View.GONE) {
			background.setVisibility(View.VISIBLE);
		}

		mySexPopupWindow = new PopupWindow(this);
		View v = LayoutInflater.from(this).inflate(R.layout.sex_change, null);
		mySexPopupWindow.setContentView(v);
		mySexPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
		mySexPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
		mySexPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mySexPopupWindow.setFocusable(true);
		mySexPopupWindow.setOutsideTouchable(true);
		mySexPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
		initSexPopWindow(v);
		mySexPopupWindow.update();

		mySexPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				background.setVisibility(View.GONE);
			}
		});
	}

	/**
	 * @Description:初始化选择性别popWindow组件
	 * @author hunaixin
	 * @parame v
	 * @return
	 */
	private void initSexPopWindow(View v) {
		LinearLayout boy = (LinearLayout) v.findViewById(R.id.boy);
		LinearLayout girl = (LinearLayout) v.findViewById(R.id.girl);
		final TextView boyText = (TextView) v.findViewById(R.id.boy_text);
		final TextView girlText = (TextView) v.findViewById(R.id.girl_text);

		boy.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				svSex.setText(boyText.getText().toString());
				svSex.setTextColor(getResources().getColor(R.color.car_detials_name_text));
				mySexPopupWindow.dismiss();
			}
		});

		girl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				svSex.setText(girlText.getText().toString());
				svSex.setTextColor(getResources().getColor(R.color.car_detials_name_text));
				mySexPopupWindow.dismiss();
			}
		});

	}

	/**
	 * @Description: 输入框改变背景监听方法
	 * @author hunaixin
	 * @parame 
	 * @return
	 */
	OnFocusChangeListener myListener = new OnFocusChangeListener() {

		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			if (hasFocus) {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et_no);
			} else {
				v.setBackgroundResource(R.drawable.bg_withdrawals_et);
			}
			v.setPadding(60, 0, 0, 0);
		}
	};

	/**
	 * @Description:判断邮箱格式是否正确
	 * @author hunaixin
	 * @parame email
	 * @return true/false
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * @Description:判断身份证号码输入是否正确
	 * @author hunaixin
	 * @parame id
	 * @return true/false
	 */
	public static boolean isIdCard(String id) {
		String str = "/(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)/";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(id);
		return m.matches();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (KeyEvent.KEYCODE_BACK == keyCode) {
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
}