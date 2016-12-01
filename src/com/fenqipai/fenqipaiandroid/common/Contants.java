package com.fenqipai.fenqipaiandroid.common;

import android.os.Environment;

/**
 * 系统常量
 * 
 * @name Contants
 * @author zhaoqingyang
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class Contants {

	// SharedPreferences 名称
	public final static String SHARD_PRE_NAME = "MT_FENQIPAI_NAME";

	/** 下载保存路径 */
	public final static String DOWN_URL = "/fenqipai/down/";

	/** 下载APK */
	public final static String DOWN_APP = "/fenqipai/app/";

	/** 分页数据 */
	public final static int PAGE_SIZE = 10;

	/** 分页数据较大 */
	public final static int PAGE_SIZE_BIGGER = 10;

	/** 显示照片的最大个数 */
	public final static int PICURE_COUNT = 6;

	/** 显示回复最大图片数量 */
	public final static int PICURE_COUNT_REPLY = 3;

	/** 时间戳 */
	public final static String UPDATE_TIME = "update_time";

	/** 新消息提醒 */
	public final static String SETTING_NOTIFY = "setting_notify";

	/** 图片保存地址 */
	public final static String FILE_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ Contants.DOWN_URL;
	/** 图片保存地址 */
	public final static String DOWN_PATH = Environment
			.getExternalStorageDirectory().getAbsolutePath()
			+ "/fenqipai/image/";

	/** 百科webWiew缓存 */
	public static final String WIKI_APP_CACAHE_DIRNAME = "/webcache";

	/** 数据库名称 */
	public final static String DATABASE_NAME = "FENQIPAI_DB";

	/** 更新提醒 */
	public final static String NOTIFICATION_FLAG = "notification_flag";

	/** 正在聊天的用户ID */
	public final static String CHAT_USER_ID = "chat_user_id";

	// 个推:clientid
	public final static String GETUI_CLIENT_ID = "getui_clientid";

	/** 登录状态 */
	public final static String LOGIN_STATE = "login_state";

	/** 用户ID */
	public final static String USER_ID = "user_id";

	/** 用户姓名 */
	public final static String USER_NAME = "user_name";

	/** 用户头像 */
	public final static String USER_PORTRAIT = "user_portrait";

	/** 用户余额 */
	public final static String USER_BALANCE = "user_balance";

	/** 用户电话 */
	public final static String USER_MOBILE = "user_mobile";
	
	/** 用户是否有登录密码 */
	public final static String USER_PASSWORD = "user_password";
	
	/** 用户是否有提现密码 */
	public final static String USER_PAYPASSWORD = "user_paypassword";

	/** 用户冻结余额 */
	public final static String USER_FREEZE_BALANCE = "user_freeze_balance";

	/** 应用在SD卡保存数据的根路径 */
	public final static String ROOT_SD_PATH = "fenqipai/";

	/** 捕获异常保存路径 */
	public final static String CRASH_SD_PAHT = ROOT_SD_PATH + "crash/";

	/** 填写从短信SDK应用后台注册得到的APPKEY */
	public final static String SMS_APPKEY = "a5cb844239c0";

	/** 填写从短信SDK应用后台注册得到的APPSECRET */
	public final static String SMS_APPSECRET = "4de1d33f9794d2885adae21a0fa986b2";

	/** QQ登陆appid */
	public static final String QQ_APP_ID = "1105438173";

	/** 微博登陆APP_KEY */
	public static final String WEIBO_APP_KEY = "2965687052";

	/** 微信登陆appid */
	/** 微信支付 */
	// 请同时修改 androidmanifest.xml里面，.PayActivityd里的属性<data
	// android:scheme="wxb4ba3c02aa476ea1"/>为新设置的appid
	public static final String WEIXIN_APP_ID = "wx7df6b183bc448180";

	/** 微信申请的app_secret */
	public static final String WEIXIN_SECRET = "cb433a2d2d906dfa3db5b22c5fb5d7bd";

	/** API密钥，在商户平台设置 */
	public static final String API_KEY = "BeiJinghaidayundingkejiyxgs20151";

	/** 商户号 */
	public static final String MCH_ID = "1278339001";

	/**
	 * 当前应用的回调页。
	 * 
	 * <p>
	 * 注：关于授权回调页对移动客户端应用来说对用户是不可见的，所以定义为何种形式都将不影响， 但是没有定义将无法使用 SDK 认证登录。
	 * 建议使用默认回调页：https://api.weibo.com/oauth2/default.html
	 * </p>
	 */
	public static final String WEIBO_REDIRECT_URL = "http://www.sina.com";

	/**
	 * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
	 * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利 选择赋予应用的功能。
	 * 
	 * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的 使用权限，高级权限需要进行申请。
	 * 
	 * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
	 * 
	 * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
	 * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
	 */
	public static final String WEIBO_SCOPE = "email,direct_messages_read,direct_messages_write,"
			+ "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
			+ "follow_app_official_microblog," + "invitation_write";

	/** 登录类型(1.QQ登陆2.微信登陆3.手机快捷登录4.账号登陆) */
	public final static String LOGIN_TYPE = "login_type";

	/** QQ登陆的token */
	public final static String QQ_TOKEN = "login_qq_token";

	/** QQ登陆的openid */
	public final static String QQ_OPENID = "login_qq_openid";

	/** QQ登陆的有效时间 */
	public final static String QQ_EXPIRES = "login_qq_expires";

	/** 微博登陆的token */
	public final static String WEIBO_TOKEN = "login_weibo_token";

	/** 微博登陆的uid */
	public final static String WEIBO_UID = "login_weibo_uid";

	/** 微博登陆的有效时间 */
	public final static String WEIBO_EXPIRES = "login_weibo_expires";

	/** 微博登陆的刷新refresh_token */
	public final static String WEIBO_REFRESH_TOKEN = "login_weibo_refresh_token";

	/** 微信登陆的openid */
	public final static String WEIXIN_PENID = "login_weixin_openid";

	/** 微信支付的openid */
	public final static String WEIXIN_PAY_PENID = "";

	/** 微信登陆access_token */
	public final static String WEIXIN_ACCESS_TOKEN = "login_weixin_access_token";

	/** 微信登录有效时间 */
	public final static String WEIXIN_EXPIRES = "login_weixin_expires";

	/** 微信登陆的刷新refresh_token */
	public final static String WEIXIN_REFRESH_TOKEN = "login_weixn_refresh_token";

	/** token */
	public final static String USER_TOKEN = "token";

	/** 编辑词条 */
	public final static int WIKI_EDITOR = 4;

	/** 添加词条 */
	public final static int WIKI_ADD = 5;

	/** 二维码信息标识 */
	public static final String CODE_INFO = "codeInfo";

	/** 选择照片请求码. */
	public static final int PICK_PHOTO_REQUEST_CODE = 207;

	/** 客服中心电话 */
	public static final String SERVICE_TELEPHONE = "tel:4008386777";

	/** 图片选择请求码 */
	public static final int TAKE_PICTURE_REQUEST = 601;

	/** 图片选择返回码 */
	public static final int TAKE_PICTURE_RETURN = 602;

	/** 头像大小. */
	public static final int PORTRAIT_SIZE = 300;

	/** 裁剪请求码. */
	public static final int CUTTING_REQUESTCODE = 208;

	/** 跳转至个人信息. */
	public static final int TO_CHENG_MESSAGE = 603;

	/** 修改昵称请求码. */
	public static final int CHANGE_NAME_REQUEST = 101;

	/** 修改昵称返回码. */
	public static final int CHANGE_NAME_RETURN = 201;
	
	/** 修改职业请求码. */
	public static final int CHANGE_JOB_REQUEST = 141;

	/** 修改职业返回码. */
	public static final int CHANGE_JOB_RETURN = 241;
	
	/** 修改地址请求码. */
	public static final int CHANGE_ADDRESS_REQUEST = 131;

	/** 修改地址返回码. */
	public static final int CHANGE_ADDRESS_RETURN = 231;
	
	/** 修改真实姓名请求码. */
	public static final int CHANGE_TRUENAME_REQUEST = 111;

	/** 修改真实姓名返回码. */
	public static final int CHANGE_TRUENAME_RETURN = 211;

	/** 修改邮箱返回码. */
	public static final int CHANGE_EMAIL_RETURN = 202;

	/** 修改邮箱返回码. */
	public static final int CHANGE_EMAIL_REQUEST = 102;
	
	/** 修改身份证返回码. */
	public static final int CHANGE_CARD_RETURN = 212;

	/** 修改身份证请求码. */
	public static final int CHANGE_CARD_REQUEST = 112;

	/** 跳转到设置页面. */
	public static final int TO_SETTING = 203;

	/** 返回个人信息页面. */
	public static final int RETURN_MESSAGE = 204;

	/** 银联支付mode 设置测试模式:01为测试 00为正式环境. */
	public static final String YINLIAN_MODE = "01";

	/** 银联支付. */
	public static final int PLUGIN_VALID = 0;

	/** 银联支付. */
	public static final int PLUGIN_NOT_INSTALLED = -1;

	/** 银联支付. */
	public static final int PLUGIN_NEED_UPGRADE = 2;

	/** 车辆详情加价请求码. */
	public static final int CAR_DETAIL_REQUST = 604;

	/** 车辆详情加价返回码. */
	public static final int CAR_DETAIL_RETURN = 605;
	
	/** 保证金提现添加银行卡请求码. */
	public static final int WITHDRAW_ADDCARD_REQUST = 222;

	/** 保证金提现添加银行卡返回码. */
	public static final int WITHDRAW_ADDCARD_RETURN = 333;

	/**
	 * -----------------------------------车辆属性-------（开始）----------------------
	 * -------------------
	 */

	/** 年检到期. */
	public static final String ANNUAL_INSPECTION = "s_1cdeb282-8243-4a89-b0c8-308d118c86f4";

	/** 年款. */
	public static final String AGE_LIMIT = "c_f9dc78b9-89e0-4cf3-89c3-ec6034441bc8";

	/** 里程. */
	public static final String MILEAGE = "s_b9f6ae75-bf05-48d6-b226-5758263ee836";

	/** 过户次数. */
	public static final String CHANGE_NUMBER = "cb2f96d4-a976-4c26-acce-088a04f58543";

	/** 上牌时间. */
	public static final String REGISTRATION_TIME = "s_daa3c84d-4e46-4dc3-a3f1-505393d2bd90";

	/** 保险到期. */
	public static final String INSURANCE = "efaeaa17-d2c4-4fba-b7d7-621adedf44b3";

	/** 价格. */
	public static final String CAR_PRICE = "b9eeb270-36d1-4112-80d5-a268fbda1c63";

	/** 颜色. */
	public static final String CAR_COLOR = "f4dd8ed2-dc8c-4cce-8adc-d48807c0a206";

	/** 排量. */
	public static final String DISPLACEMENT = "c_f2f76a71-303e-4ae1-8ef7-a78e3b9722f2";

	/**
	 * -----------------------------------车辆属性--------（结束）----------------------
	 * ------------------
	 */
}
