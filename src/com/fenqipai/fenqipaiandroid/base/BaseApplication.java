package com.fenqipai.fenqipaiandroid.base;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.mapapi.SDKInitializer;
import com.fenqipai.fenqipaiandroid.LoginActivity;
import com.fenqipai.fenqipaiandroid.R;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.common.DBManager;
import com.fenqipai.fenqipaiandroid.model.BankCardList;
import com.fenqipai.fenqipaiandroid.model.BankList;
import com.fenqipai.fenqipaiandroid.model.Commodity;
import com.fenqipai.fenqipaiandroid.model.IndexNewsList;
import com.fenqipai.fenqipaiandroid.model.InstallmentCar;
import com.fenqipai.fenqipaiandroid.model.LoanAlgorithmList;
import com.fenqipai.fenqipaiandroid.model.MoneyChangeRecord;
import com.fenqipai.fenqipaiandroid.model.Moneyrecord;
import com.fenqipai.fenqipaiandroid.model.MyLocation;
import com.fenqipai.fenqipaiandroid.model.NewsList;
import com.fenqipai.fenqipaiandroid.model.Notice;
import com.fenqipai.fenqipaiandroid.model.OrderInfo;
import com.fenqipai.fenqipaiandroid.model.OrderList;
import com.fenqipai.fenqipaiandroid.model.PayInfo;
import com.fenqipai.fenqipaiandroid.model.PaymentSaleLoan;
import com.fenqipai.fenqipaiandroid.model.SaleCar;
import com.fenqipai.fenqipaiandroid.model.SingleCarDetails;
import com.fenqipai.fenqipaiandroid.model.SingleSaleCarDetails;
import com.fenqipai.fenqipaiandroid.model.StagingBill;
import com.fenqipai.fenqipaiandroid.model.StagingBillDetails;
import com.fenqipai.fenqipaiandroid.model.UserInfo;
import com.fenqipai.fenqipaiandroid.net.NetClient;
import com.fenqipai.fenqipaiandroid.util.NetUtils;
import com.fenqipai.fenqipaiandroid.util.SPUtils;
import com.fenqipai.fenqipaiandroid.util.ToastUtils;
import com.fenqipai.fenqipaiandroid.view.TitleBarView;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

/**
 * 整个应用的Application
 * 
 * @name BaseApplication
 * @author zhaoqingyang
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class BaseApplication extends Application {

	public DBManager dBManager;

	public ImageLoader imageLoader;

	public DisplayImageOptions options;

	@Override
	public void onCreate() {
		super.onCreate();

		dBManager = new DBManager(this);

		imageLoader = ImageLoader.getInstance();

		initImageLoader();

		// 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
//		SDKInitializer.initialize(this);

		// // 初始化异常处理类
		// CrashHandler crashHandler = CrashHandler.getInstance();
		// crashHandler.init(getApplicationContext(), this);

	}

	/*
	 * 自定义配置
	 */
	public void initImageLoader() {
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

		options = new DisplayImageOptions.Builder().showStubImage(R.drawable.no_img)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.no_img)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.no_img)// 设置图片加载/解码过程中错误时候显示的图片
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
	 * 清除缓存
	 */
	public void clearCache(){
		imageLoader.clearDiscCache();
		imageLoader.clearMemoryCache();
	}
	

	/**
	 * 获取用户ID
	 * 
	 * @title getUserId
	 * @author qianyuhang
	 * @return
	 */
	public String getUserId() {
		return (String) SPUtils.get(this, Contants.USER_ID, "");
	}

	/**
	 * 获取用户token
	 * 
	 * @title getUserId
	 * @author qianyuhang
	 * @return
	 */
	public String getUserToken() {
		return (String) SPUtils.get(this, Contants.USER_TOKEN, "");
	}

	/**
	 * 获取用户头像
	 * 
	 * @title getUserId
	 * @author qianyuhang
	 * @return
	 */
	public String getPortrait() {
		return (String) SPUtils.get(this, Contants.USER_PORTRAIT, "");
	}

	/**
	 * 获取用户名
	 * 
	 * @title getUserName
	 * @author qianyuhang
	 * @return
	 */
	public String getUserName() {
		return (String) SPUtils.get(this, Contants.USER_NAME, "");
	}

	/**
	 * 获取用户名
	 * 
	 * @title getUserName
	 * @author qianyuhang
	 * @return
	 */
	public String getUserMobile() {
		return (String) SPUtils.get(this, Contants.USER_MOBILE, "");
	}

	/**
	 * 获取余额
	 * 
	 * @title getUserBalance
	 * @author qianyuhang
	 * @return
	 */
	public String getUserBalance() {
		return (String) SPUtils.get(this, Contants.USER_BALANCE, "0.0");
	}

	/**
	 * 获取冻结余额
	 * 
	 * @title getUserFreezeBalance
	 * @author qianyuhang
	 * @return
	 */
	public String getUserFreezeBalance() {
		return (String) SPUtils.get(this, Contants.USER_FREEZE_BALANCE, "0.0");
	}

	/**
	 * 判断用户是否登陆
	 * 
	 * @title getLoginState
	 * @author qianyuhang
	 * @return
	 */
	public boolean getLoginState() {
		int type = (Integer) SPUtils.get(this, Contants.LOGIN_TYPE, 0);

		if (type == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断用户是否登录超时
	 * 
	 * @title getLoginState
	 * @author qianyuhang
	 * @return
	 */
	public boolean getLoginTimeOut(BaseApplication application, String str) {

		if (TextUtils.isEmpty(str)) {
			return false;
		} else if (str.equals("tokenError")) {
			ToastUtils.show(application, "登录超时", ToastUtils.TOAST_SHORT);
			SPUtils.put(application, Contants.LOGIN_TYPE, 0);
			SPUtils.remove(application, Contants.USER_ID);
			SPUtils.remove(application, Contants.USER_NAME);
			SPUtils.remove(application, Contants.USER_PORTRAIT);
			SPUtils.remove(application, Contants.USER_TOKEN);
			SPUtils.remove(application, Contants.USER_BALANCE);
			SPUtils.remove(application, Contants.USER_FREEZE_BALANCE);
			SPUtils.remove(application, Contants.USER_MOBILE);
			startLogin(application);
			return false;
		} else if (str.equals("tokenTimeout")) {
			ToastUtils.show(application, "登录超时", ToastUtils.TOAST_SHORT);
			SPUtils.put(application, Contants.LOGIN_TYPE, 0);
			SPUtils.remove(application, Contants.USER_ID);
			SPUtils.remove(application, Contants.USER_NAME);
			SPUtils.remove(application, Contants.USER_PORTRAIT);
			SPUtils.remove(application, Contants.USER_TOKEN);
			SPUtils.remove(application, Contants.USER_BALANCE);
			SPUtils.remove(application, Contants.USER_FREEZE_BALANCE);
			SPUtils.remove(application, Contants.USER_MOBILE);
			startLogin(application);
			return false;
		} else if (str.equals("userIdNull")) {
			ToastUtils.show(application, "请登录", ToastUtils.TOAST_SHORT);
			startLogin(application);
			return false;
		}else if (str.equals("404")) {
			ToastUtils.show(application, "数据不存在", ToastUtils.TOAST_SHORT);
			return false;
		}else if (str.equals("success")) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 启动登陆页面
	 * 
	 * @title startLogin
	 * @author qianyuhang
	 */
	public void startLogin(Context context) {
		Intent in = new Intent(this, LoginActivity.class);
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(in);
	}

	/**
	 * 获取时间戳
	 * 
	 * @title getUpdateTime
	 * @author zhaoqingyang
	 * @return
	 */
	public long getUpdateTime() {
		return (Long) SPUtils.get(this, Contants.UPDATE_TIME, System.currentTimeMillis());
	}

	/**
	 * 保存bug信息
	 * 
	 * @title saveBugInfo
	 * @author zhaoqingyang
	 * @param result
	 */
	public boolean saveBugInfo(String info) {
		String result = NetClient.saveBugInfo(this, info);

		if (result.equals("ok")) {
			return true;
		}

		return false;

	}

	/**
	 * 获取今日江湖速递
	 * 
	 * @title getJRJHExpress
	 * @author qianyuhang
	 * @return
	 */
	public List<Commodity> getCommodityList() {
		if (NetUtils.isConnected(this)) {
			NetClient.getCommodityList(this);
		}
		return dBManager.getCommodityList();
	}

	/**
	 * 设置提醒图标
	 * 
	 * @title setMessageImg
	 * @author zhaoQingyang
	 */
	public void setMessageImg(TitleBarView titleBarView) {

		// 如果有未读的消息s
		if (IsNotReadMsg()) {
			titleBarView.setBtnLeft(R.drawable.mail_notread_button);
		} else {
			titleBarView.setBtnLeft(R.drawable.mail_button);
		}

	}

	/**
	 * 获取今日江湖轮播图
	 * 
	 * @title getJRJHNotice
	 * @author qianyuhang
	 * @param map
	 */
	public void getJRJHNotice(Map<String, Object> map) {

		// 在线人数
		int onlineUserCount = 987;

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			// 获取首页数据
			String result = NetClient.getJRJHNotice(this);
			if (!result.equals("网络连接超时")) {
				try {
					JSONObject jsonObject = new JSONObject(result);

					Notice.parse(this, jsonObject.optString("noticeList"));

					onlineUserCount = jsonObject.optInt("onlineUserCount");

				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		}
		map.put("noticeList", dBManager.getNoticeFour());
		map.put("onlineUserCount", onlineUserCount);
	}

	/**
	 * 发送短信
	 * 
	 * @title sendSMS
	 * @author qianyuhang
	 * @param phone
	 * @return
	 */
	public String sendSMS(String phone, String captchaCode, String actionType) {
		return NetClient.sendSMS(this, phone, captchaCode, actionType);
	}

	/**
	 * 是否有未读的消息
	 * 
	 * @title IsNotReadMsg
	 * @author zhaoqingyang
	 * @return
	 */
	public boolean IsNotReadMsg() {

		// true 有未读的系统消息 false 没有未读的系统消息
		// boolean sysMsg = (Boolean) SPUtils.get(this, Contants.SYSTEM_MSG,
		// false);

		// true 有未读的聊天消息 false 没有未读的聊天消息
		// boolean chatMsg = (Boolean) SPUtils.get(this, Contants.CHAT_MSG,
		// false);
		//
		// if (sysMsg || chatMsg) {
		// // 有未读的消息
		// return true;
		// }

		// 没有未读的消息
		return false;
	}

	/**
	 * 手机号登陆验证
	 * 
	 * @title phoneLoginCheck
	 * @author qianyuhang
	 * @param phone
	 * @param password
	 * @return
	 */
	public String phoneLoginCheck(String phone, String password, String captchaCode) {

		return NetClient.phoneLoginCheck(this, phone, password, captchaCode);
	}

	/**
	 * @Title: 手机号注册 @Description: @param phone @param password @param
	 *         verifyCode @param receivedCode @throws
	 */

	public String register(String account, String phone, String password, String messageCode) {
		return NetClient.register(this, account, phone, password, messageCode);
	}

	/**
	 * @Title: qq验证是否注册 @Description: @param phone @param password @param
	 *         verifyCode @param receivedCode @throws
	 */
	public String qqMobileLogin(String openId, String type) {
		return NetClient.qqMobileLogin(this, openId, type);
	}

	/**
	 * @Title: qq验证是否注册 @Description: @param phone @param password @param
	 *         verifyCode @param receivedCode @throws
	 */
	public String weixinLoginCallback() {
		return NetClient.weixinLoginCallback(this);
	}

	/**
	 * @Title: 忘記密碼
	 * @Description:
	 * @param mobile
	 * @param password
	 * @param messageCode
	 * @throws @Title:
	 *             忘記密碼 @Description: @param mobile @param password @param
	 *             messageCode @throws
	 * 
	 */
	public String forgetPassword(String mobile, String password, String messageCode,String type) {
		return NetClient.forgetPassword(this, mobile, password, messageCode,type);
	}

	/**
	 * @Title: qq首次注册 @Description: @param openId @param password @param
	 *         verifyCode @param receivedCode @throws
	 */
	public String qqMobileRegist(String openId, String mobile, String nickName, String portrait, String messageCode,
			String sex, String type) {
		return NetClient.qqMobileRegist(this, openId, mobile, nickName, portrait, messageCode, sex, type);
	}

	/**
	 * 手机号登陆验证
	 * 
	 * @title phoneLoginCheck
	 * @author qianyuhang
	 * @param phone
	 * @param password
	 * @return
	 */
	public String loginMobile(String mobile, String messageCode) {

		return NetClient.loginMobile(this, mobile, messageCode);

	}

	/**
	 * 获取拍卖列表
	 * 
	 * @title getAuctionList
	 * @author auctionType
	 * @param isLoad
	 * @param curRows
	 * @return
	 */
	public List<SaleCar> getAuctionList(boolean isLoad, int curRows, String auctionType) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.auctionList(this, updateTime, isLoad, curRows, Contants.PAGE_SIZE, auctionType);
		}

		// 没有网络获取本地数据
		return dBManager.getAuctionList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 首页获取列表
	 * 
	 * @title getAuctionList
	 * @author auctionType
	 * @param isLoad
	 * @param curRows
	 * @return
	 */
	public List<SaleCar> getHomeAuctionList(boolean isLoad, int curRows, String auctionType) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.auctionList(this, updateTime, isLoad, curRows, 4, auctionType);
		}

		// 没有网络获取本地数据
		return dBManager.getAuctionList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取某个拍卖的车辆详细信息
	 * 
	 * @param auctionId
	 *            - 拍卖id
	 */
	public SingleCarDetails getAuctionInfo(String auctionId) {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.auctionInfo(this, auctionId);
		}
		// 没有网络获取本地数据
		return dBManager.getAuctionInfo(auctionId);
	}

	/**
	 * 获取分期车辆列表
	 * 
	 * @title getInstallmentList
	 * @author auctionType
	 * @param isLoad
	 * @param curRows
	 * @return
	 */
	public List<InstallmentCar> getInstallmentList(boolean isLoad, int curRows) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.installmentList(this, updateTime, isLoad, curRows, Contants.PAGE_SIZE);
		}
		// 没有网络获取本地数据
		return dBManager.getInstallmentList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取某个分期的车辆详细信息
	 * 
	 * @param auctionId
	 *            - 拍卖id
	 */
	public SingleSaleCarDetails getSaleInfo(String saleId) {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.saleInfo(this, saleId);
		}
		// 没有网络获取本地数据
		return dBManager.getsaleInfo(saleId);
	}

	/**
	 * 获取银联支付tn
	 */
	public String getYLpatTn(String money,String type) {

		return NetClient.getYLpayTn(this, money,type, SPUtils.get(this, Contants.USER_TOKEN, "").toString());
	}

	/**
	 * 拍卖出价
	 */
	public String auctionRaisePrice(String auctionId, String money) {

		return NetClient.auctionRaisePrice(this, auctionId, money,
				SPUtils.get(this, Contants.USER_TOKEN, "").toString(),
				SPUtils.get(this, Contants.USER_ID, "").toString());
	}

	/**
	 * 获取订单列表
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<OrderList> getOrderList(boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getOrderList(this, updateTime, isLoad, curRows, pageSize, getUserToken().toString());
		}
		return dBManager.getOrderList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取订单详细
	 * 
	 * @author hunaixin
	 * @param orderId
	 */
	public OrderInfo getOrderInfo(String orderId) {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getOrderInfo(this, orderId, getUserToken().toString());
		}
		// 没有网络获取本地数据
		return dBManager.getOrderInfo(orderId);
	}

	/**
	 * 获取支持银行列表
	 * 
	 * @author hunaixin
	 */
	public List<BankList> getBankList() {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getBankList(this, getUserToken().toString());
		}
		// 没有网络获取本地数据
		return dBManager.getBankList();
	}

	/**
	 * 获取个人信息
	 * 
	 * @title getNbuserInfoResult
	 * @return
	 */
	public UserInfo getNbuserInfoResult() {

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getNbuserInfoResult(this, getUserToken().toString());
		}
		return dBManager.getUserInfo(getUserId());
	}

	/**
	 * 会员修改密码
	 * 
	 * @author WangZhonghan
	 */
	public String updateNbuserPassword(String passwordType, String oldPassword, String newPassword) {
		return NetClient.updateNbuserPassword(this, passwordType, oldPassword, newPassword, getUserToken());
	}

	/**
	 * 变更会员基础信息
	 * 
	 * @author WangZhonghan
	 */
	public String updateNbuser(String account, String nickName, String trueName, String sex, String birthday,
			String email, String card, String address,String job) {
		return NetClient.updateNbuser(this, account, nickName, trueName, sex, birthday, email, card, address,job,
				getUserToken());
	}

	/**
	 * 验证支付密码
	 */
	public String payPasswordCheck(String payPassword) {
		return NetClient.payPasswordCheck(this, payPassword, getUserToken().toString());
	}

	/**
	 * 添加银行卡
	 */
	public String insertCarNo(String no, String bankId, String cardUserName) {

		return NetClient.insertCarNo(this, no, bankId, cardUserName, getUserToken().toString());
	}

	/**
	 * 修改用户头像
	 */
	public String updateUserHeadImg(String account, String nickName, String trueName, String sex, String birthday,
			String email, String card, String address,String job,Map<String, File> files) {

		return NetClient.updateUserHeadImg(this, account, nickName, trueName, sex, birthday, email, card, address,job,files, getUserToken().toString());
	}

	/**
	 * 绑定银行卡列表
	 */
	public List<BankCardList> bankCardList() {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.bankCardList(this, getUserToken().toString());
		}
		return dBManager.getBankCardList();
	}

	/**
	 * 解除绑定银行卡
	 */
	public String deleteCarNo(String cardNoId) {

		return NetClient.deleteCarNo(this, cardNoId, getUserToken().toString());
	}

	/**
	 * 销售方案确认查询列表
	 */
	public List<PaymentSaleLoan> paymentSaleLoanList(String saleId) {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.paymentSaleLoan(this, saleId, getUserToken().toString());
		}
		return dBManager.getPaymentSaleLoanList();
	}

	/**
	 * 分期业务获取对应信息列表
	 */
	public List<LoanAlgorithmList> loanAlgorithmList(String cid, String type, String loanId, String orderId) {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.loanAlgorithmList(this, cid, type, loanId, orderId, getUserToken().toString());
		}
		return dBManager.getLoanAlgorithmList();
	}

	/**
	 * 预约购车  服务类型： Evaluations车辆评估, SellCar我要买车， Insurance代办车险，ExtendedWarranty延保服务
	 */
	public String inserServicePhone(String serviceType, String mobile) {
		// 如果有网络，获取网络数据
		return NetClient.inserServicePhone(this, serviceType, mobile, getUserToken().toString());

	}
	
	/**
	 * 增值服务
	 */

	public String buyApply(String saleId, String mobile) {
		// 如果有网络，获取网络数据
		return NetClient.buyApply(this, saleId, mobile, getUserToken().toString());

	}

	/**
	 * 银联支付回调
	 */

	public String unionpayMobileFront(String payId, String jsonData,String type) {
		// 如果有网络，获取网络数据
		return NetClient.unionpayMobileFront(this, payId, jsonData, type,getUserToken().toString());

	}

	/**
	 * 根据支付单获取银联订单号
	 */

	public String unionpayMobileCreate(String payId,String type) {
		// 如果有网络，获取网络数据
		return NetClient.unionpayMobileCreate(this, payId,type, getUserToken().toString());

	}

	/**
	 * 获取保证金记录
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<Moneyrecord> getBailList(boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getBailList(this, updateTime, isLoad, curRows, pageSize, getUserToken().toString());
		}
		return dBManager.getBailList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取服务器时间
	 */

	public String getClientTime() {
		// 如果有网络，获取网络数据
		return NetClient.getClientTime(this);

	}

	/**
	 * 查询余额
	 */

	public String getMyBalance() {
		// 如果有网络，获取网络数据
		return NetClient.getMyBalance(this, getUserToken().toString());
	}

	/**
	 * 获取分期账单列表
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<StagingBill> getRepaymentOrderList(boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();
		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getRepaymentOrderList(this, updateTime, isLoad, curRows, pageSize, getUserToken().toString());
		}
		return dBManager.getRepaymentOrderList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取支付列表
	 * 
	 * @param curRows
	 * @param pageSize
	 */

	public List<PayInfo> getPayInfo(String payStatus, boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();
		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}
		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getPayInfo(this, payStatus, updateTime, isLoad, curRows, pageSize, getUserToken().toString());
		}
		return dBManager.getPayInfo(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 首页新闻和轮播图
	 */
	public List<IndexNewsList> newsList() {
		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.newsList(this);
		}
		return dBManager.getNewsList();
	}

	/**
	 * 删除支付信息
	 */
	public String closePay(String payId) {

		return NetClient.closePay(this, payId, getUserToken().toString());
	}

	/**
	 * 新闻列表
	 */
	public List<NewsList> getNewsList(boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络，获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getNewsList(this, updateTime, isLoad, curRows, pageSize);
		}
		return dBManager.getNews(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 验证支付tn
	 * 
	 */
	public String createPay(String orderId, String actionType, String repaymentIdArray, String loanId,
			String loanAlgorithmId,String type) {
		return NetClient.createPay(this, orderId, actionType, repaymentIdArray, loanId, loanAlgorithmId,
				type,getUserToken().toString());
	}

	/**
	 * 获取分期账单列表
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<StagingBillDetails> getOrderRepaymentList(String orderId, String orderRepaymentStatus, boolean isLoad,
			int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();
		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getOrderRepaymentList(this, orderId, orderRepaymentStatus, updateTime, isLoad, curRows, pageSize,
					getUserToken().toString());
		}
		return dBManager.getOrderRepaymentList(curRows, Contants.PAGE_SIZE);
	}

	/**
	 * 获取活动位置列表
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<MyLocation> getAllSeeCarPoint() {

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.seeCarPoint(this);
		}
		return dBManager.getSeeCarPoint();
	}

	/**
	 * 保证金提现
	 */
	public String withdraw(String name, String cardNoId, String cashPrice, String password) {

		return NetClient.withdraw(this, name, cardNoId, cashPrice, password, getUserToken().toString());
	}

	/**
	 * 维修与保养列表webviewUrl
	 */
	public String getMRepairListUrl() {

		return NetClient.getMRepairListUrl(this);
	}

	/**
	 * 获取资金记录记录
	 * 
	 * @param curRows
	 * @param pageSize
	 */
	public List<MoneyChangeRecord> getMoneyChangeRecord(boolean isLoad, int curRows, int pageSize) {
		long updateTime = System.currentTimeMillis();

		// 如果是加载
		if (isLoad) {
			updateTime = getUpdateTime();
		} else {
			curRows = 0;
			SPUtils.put(this, Contants.UPDATE_TIME, updateTime);
		}

		// 如果有网络 获取网络数据
		if (NetUtils.isConnected(this)) {
			NetClient.getMoneyChangeRecord(this, updateTime, isLoad, curRows, pageSize, getUserToken().toString());
		}
		return dBManager.getMoneyChangeRecord(curRows, Contants.PAGE_SIZE);
	}

}
