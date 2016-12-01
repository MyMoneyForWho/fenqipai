package com.fenqipai.fenqipaiandroid.net;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;

/**
 * 访问网络的地址管理类
 * 
 * @name URL
 * @author liuchengbao
 * @date 2015年9月11日
 * @modify
 * @modifyDate 2015年9月11日
 * @modifyContent
 */
public class URL {

	// 网络
	public final static String URL_HOST = "139.129.204.125";
	
	public final static String LOGIN_URL = "/fortel/login";

	public final static String GET_COMMODITY_LIST = "/application/test";

	public final static String URL_OFFLINE_li = "10.0.12.105";
	public final static String URL_OFFLINE_dong = "10.0.12.236";
	public final static String URL_OFFLINE_hong = "10.0.12.36";
	 
	// 保存bug信息
	public final static String SAVE_BUG_URL = "/Systems/saveSysBug";

	/**
	 * 获取轮播数据
	 */
	public final static String GET_JRJH_NOTICE = "/Application/indexNotice";
	
	public final static String URL_SHARE = "www.meitanjianghu.com";

	// 获取热门求助列表
	public final static String GET_HELP_HOT_URL = "/helps/hotHelpList";
	
	/**发送短信*/
	public final static String SEND_SMS="/ports/sendMessageCode";

	/** 登录 */
	public final static String LOGIN_IN = "/logins/login";

	/** 手机快捷登录 */
	public final static String LOGIN_MOBILE = "/logins/loginMobile";

	/** 注册 */
	public final static String REGIST = "/logins/regist";
	
	/** 获取图片验证码 */
	public final static String CAPTCHA = "/captcha.jpg";

	/** qq验证是否注册 */
	public final static String QQ_MOBILE_LOGIN = "/logins/qqMobileLogin";

	/** qq首次注册并绑定手机 */
	public final static String QQ_MOBILE_REGIST = "/logins/qqMobileRegist";

	/** qq验证是否注册 */
	public final static String WEIXIN_MOBILE_LOGIN = "/logins/weixinLoginCallback";

	/** 获取拍卖列表  */
	public final static String AUCTION_LIST = "/ports/auctionList";
	
	/** 获取拍卖车辆信息  */
	public final static String AUCTION_INFO = "/ports/auctionInfo";
	
	/** 获取分期车辆信息  */
	public final static String SALE_INFO = "/ports/saleInfo";

	/** 忘记密码重置   */
	public final static String FORGET_PASSWORD = "/logins/forgetPassword";
	
	/** 图片路径   */
	public final static String FILE_UPLOAD = "/public/fileupload/";
	  
	/** 汽车详细图片   */
	public final static String AUCTION_IMG_LIST = "/ports/auctionImgList";

	/** 银联支付获取tn   */
	public final static String YIN_LIAN_TN = "/PortsAuth/createRecharge";
	
	/** 获取订单列表  */
	public final static String ORDER_LIST = "/PortsAuth/getOrderList";
	
	/** 获取用户基本信息  */
	public final static String GET_USERINFO = "/portsAuth/getNbuserInfoResult";

	/** 拍卖加价    */
	public final static String AUCTION_RAISE_PRICE = "/PortsAuth/ajax_auctionRaisePrice";
	
	/** 获取订单详细  */
	public final static String OREDER_INFO = "/PortsAuth/getOrderById";
	
	/** 获取支持银行列表  */
	public final static String BANK_LIST = "/PortsAuth/getBankList";
	
	/** 验证支付密码  */
	public final static String PASSWORD_CHECK = "/PortsAuth/payPasswordCheck";

	/** 会员修改密码   */
	public final static String UPDATE_PASSWORD = "/PortsAuth/ajax_updateNbuserPassword";
	
	/** 修改变更会员基础信息  */
	public final static String UPDATE_INFORMATION = "/PortsAuth/ajax_updateNbuser";

	/** 添加银行卡  */
	public final static String INSERT_CARD = "/PortsAuth/ajax_insertCardNo";
	
	/** 绑定银行卡列表  */
	public final static String CARDNO_LIST = "/PortsAuth/getCardNoList";
	
	/** 解除绑定银行卡列表  */
	public final static String DELETE_CARD = "/PortsAuth/ajax_deleteCardNo";
	
	/** 销售方案确认查询  */
	public final static String SALE_LOANLIST = "/PortsAuth/paymentSaleLoanlist";

	/** 修改用户头像  */
	public final static String UPDATE_USER_HEADIMG = "/PortsAuth/ajax_updateNbuser";

	/** 根据分期业务获取对应信息  */
	public final static String LOAN_LIST = "/PortsAuth/getLoanAlgorithmList";

	/** 预约购车  */
	public final static String BUY_APPLY = "/PortsAuth/buyApply";
	
	/** 预约购车  */
	public final static String INSERT_SERVICE_PHONE = "/PortsAuth/ajax_insertServicePhone";
	
	/** 获取分期列表  */
	public final static String SALE_LIST = "/ports/saleList";

	/** 支付回调  */
	public final static String UNIONPAY_MOBILE_FRONT = "/PortsAuth/payMobileFront";

	/** 资金记录  */
	public final static String BAIL_LIST = "/PortsAuth/bailList";
	
	/** 获取服务器时间  */
	public final static String CLIENT_TIME = "/Ports/getLongTime";

	/** 查询余额  */
	public final static String GET_MY_BALANCE = "/PortsAuth/getMyBalance";
	
	/**获取支付信息列表 */
	public final static String PAY_LIST = "/PortsAuth/payList";
	
	/** 获取分期订单列表  */
	public final static String GET_REPAYMENT_ORDERLIST = "/PortsAuth/getRepaymentOrderList";
	
	/** 首页新闻与轮播图列表  */
	public final static String NEWS_LIST = "/Ports/indexNewsList";

	/** 验证支付密码  */
	public final static String CREATE_PAY = "/PortsAuth/createPay";
	
	/** 删除支付信息  */
	public final static String DELETE_PAY= "/PortsAuth/closePay";
	
	/** 新闻列表  */
	public final static String GET_NEWS_LIST = "/Ports/getNewsList";
	
	/** 获取分期账单详细列表  */
	public final static String GET_ORDER_REPAYMENT_LIST = "/PortsAuth/getOrderRepaymentList";

	/** 根据支付单获取银联订单号  */
	public final static String  UNIONPAY_MOBILE_CREATE = "/PortsAuth/payMobileCreate";
	
	/** 保证金提现  */
	public final static String WITH_DRAW = "/PortsAuth/withdraw";
	
	/** 维修与保养列表webviewUrl */
	public final static String GET_MREPAIR_LIST_URL = "/Ports/getMRepairListUrl ";
	
	/** 获取活动地理位置坐标  */
	public final static String SEE_CAR_POINT = "/Ports/seeCarPoint";
	
	/** 获取资金变更记录 */
	public final static String BOOK_LIST = "/PortsAuth/bookList";
	
	
	public static String getURL(String url) {
		return "http://" + getUSERURL()+ ":" + 8080 + url;
		
	}
	
	public static String getUSERURL() {
		return  URL_HOST;
		
	}

	public static String getShareURL(BaseApplication application,String url) {
		return "http://" + URL_SHARE + url;
	}
	
	
}

