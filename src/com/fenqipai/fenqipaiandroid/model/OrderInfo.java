package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 订单详细信息实体类
 * 
 * @author hunaixin
 *
 */
@SuppressWarnings("serial")
public class OrderInfo extends BaseModel {

	// 订单编号
	private String orderNumber;

	// 销售ID
	private String saleId;

	// 创建时间
	private String orderTime;
	
	// 支付时间......
	private String payTime;
	
	// 订单类型.........
	private String oderTpye;

	// 车辆全名
	private String carAllName;

	// 车辆图片路径
	private String carImgPath;

	// 订单状态      "unconfirmed"待确认  pendingpay待付款  paying交易中   completed交易完成      cancelled交易失败
	private String orderStatus;

	// 订单价格
	private String orderPrice;

	// 订单类型
	private String orderTypeString;

	// 购车方案 
	private String productName;

	// 分期期数
	private String loanTerm;

	// d订单编号
	private String payId;
	
	//当前支付价
	private String payPrice;
	
	// 分期支付价
	private String stagingPrice;
	
	// 订单支付类型
	private String orderRepaymentType;
	

	public String getOrderRepaymentType() {
		return orderRepaymentType;
	}

	public void setOrderRepaymentType(String orderRepaymentType) {
		this.orderRepaymentType = orderRepaymentType;
	}

	public String getStagingPrice() {
		return stagingPrice;
	}

	public void setStagingPrice(String stagingPrice) {
		this.stagingPrice = stagingPrice;
	}

	public String getPayPrice() {
		return payPrice;
	}

	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getOrderTypeString() {
		return this.orderTypeString;
	}

	public void setOrderTypeString(String orderTypeString) {
		this.orderTypeString = orderTypeString;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getSaleId() {
		return saleId;
	}

	public void setSaleId(String saleId) {
		this.saleId = saleId;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getOderTpye() {
		return oderTpye;
	}

	public void setOderTpye(String oderTpye) {
		this.oderTpye = oderTpye;
	}

	public String getCarAllName() {
		return carAllName;
	}

	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}

	public String getCarImgPath() {
		return carImgPath;
	}

	public void setCarImgPath(String carImgPath) {
		this.carImgPath = carImgPath;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderPrice() {
		return orderPrice;
	}

	public void setOrderPrice(String orderPrice) {
		this.orderPrice = orderPrice;
	}


	/**
	 * 解析字符串
	 * 
	 * @author hunaixin
	 * @param orderId
	 * 
	 */
	public static void parse(BaseApplication application, String result) {
		

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONObject jObject = jsonObject.optJSONObject("data");
				OrderInfo orderInfo = new OrderInfo();
				orderInfo.setSysId(jObject.optString("id"));
				orderInfo.setOrderNumber(jObject.optString("orderNumber"));
				orderInfo.setSaleId(jObject.optString("saleId"));
				orderInfo.setOrderTime(jObject.optString("dateTimestamp"));
				orderInfo.setCarAllName(jObject.optString("carAllName"));
				orderInfo.setCarImgPath(jObject.optString("carImgPath"));
				orderInfo.setOrderStatus(jObject.optString("orderStatus"));
				orderInfo.setOrderPrice(jObject.optString("orderPrice"));
				orderInfo.setOrderTypeString(jObject.optString("orderTypeString"));
				orderInfo.setProductName(jObject.optString("productName"));
				orderInfo.setLoanTerm(jObject.optString("loanTerm"));
				orderInfo.setOderTpye(jObject.optString("ordertype"));
				orderInfo.setPayId(jObject.optString("payId"));
				orderInfo.setPayPrice(jObject.optString("payPrice"));
				orderInfo.setStagingPrice(jObject.optString("stagingPrice"));
				orderInfo.setOrderRepaymentType(jObject.optString("orderRepaymentType"));
				
				// 保存信息
				application.dBManager.saveOrderInfo(orderInfo);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
