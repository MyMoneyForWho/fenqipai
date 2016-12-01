package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 订单列表实体类
 * @author hunaixin
 *
 */
@SuppressWarnings("serial")
public class OrderList extends BaseModel{
	
	// 订单编号
	private String orderNumber;
	
	// 销售ID
	private String saleId;
	
	// 创建时间
	private String  orderTime;
	
	// 车辆全名
	private String carAllName;
	
	// 车辆图片路径
	private String carImgPath;
	
	// 订单状态
	private String orderStatus;
	
	// 金额
	private String orderMoney;
	
	// 订单类型
	private String orderType;
	
	// 车辆价格
	private String carPrice;
	
	
	public String getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}
	
	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	


	public String getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(String orderMoney) {
		this.orderMoney = orderMoney;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCarImgPath() {
		return carImgPath;
	}

	public void setCarImgPath(String carImgPath) {
		this.carImgPath = carImgPath;
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

	public String getCarAllName() {
		return carAllName;
	}


	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}



	/**
	 * 解析字符串
	 * @author hunaixin
	 */
	public static void parse(BaseApplication application, String result, boolean isLoad) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				if (jsonArray.length() >=0) {
					// 更新操作
					if (!isLoad) {
						application.dBManager.deleteAllOrderList();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					OrderList orderList = new OrderList();
					orderList.setSysId(jObject.optString("id"));
					orderList.setOrderNumber(jObject.optString("orderNumber"));
					orderList.setSaleId(jObject.optString("saleId"));
					orderList.setOrderTime(jObject.optString("dateTimestamp"));
                    orderList.setCarAllName(jObject.optString("carAllName"));

                    orderList.setCarImgPath(jObject.optString("carImgPath"));
                    orderList.setOrderStatus(jObject.optString("orderStatus"));
                    orderList.setOrderMoney(jObject.optString("orderPrice"));
                    orderList.setOrderType(jObject.optString("orderTypeString"));
                    orderList.setCarPrice(jObject.optString("carPrice"));
					// 保存订单列表信息
					application.dBManager.saveOrderList(orderList);

				}
				
			}else {
				application.dBManager.deleteAllOrderList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	

}
