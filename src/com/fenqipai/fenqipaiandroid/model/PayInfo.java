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
public class PayInfo extends BaseModel{
	
	// 支付流水号
	private String payInfoNumber;

	// 创建时间
	private String  orderTime;
	
	//购买类型
	private String payType;
	
	//金额
	private String money;
	
	//状态
	private String state;
	
	
	

	public String getPayInfoNumber() {
		return payInfoNumber;
	}

	public void setPayInfoNumber(String payInfoNumber) {
		this.payInfoNumber = payInfoNumber;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getPayType() {
		return payType;
	}

	public void setPayType(String payType) {
		this.payType = payType;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	/**
	 * 解析字符串
	 * @author wangZhonghan
	 */
	public static void parse(BaseApplication application, String result, boolean isLoad) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				if (jsonArray.length() >= 0) {
					// 更新操作
					if (!isLoad) {
						application.dBManager.deletePayInfo();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					
					PayInfo payInfo = new PayInfo();
					payInfo.setSysId(jObject.optString("id"));
					payInfo.setPayInfoNumber(jObject.optString("payNumber"));
					payInfo.setOrderTime(jObject.optString("dateTimestamp"));
					payInfo.setPayType(jObject.optString("buyTypeString"));
					payInfo.setMoney(jObject.optString("payPrice"));
					payInfo.setState(jObject.optString("payStatusString"));
                                          
                    
                    
					// 保存订单列表信息
					application.dBManager.savePayInfo(payInfo);

				}
				
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	

}
