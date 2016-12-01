package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

public class Moneyrecord extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String dateTimestamp;

	private String price;

	private String auctionPrice;

	private String type;

	private String actionType;

	private String auctionId;

	private String typeString;



	public String getDateTimestamp() {
		return dateTimestamp;
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getAuctionPrice() {
		return auctionPrice;
	}

	public void setAuctionPrice(String auctionPrice) {
		this.auctionPrice = auctionPrice;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getActionType() {
		return actionType;
	}

	public void setActionType(String actionType) {
		this.actionType = actionType;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}

	public String getTypeString() {
		return typeString;
	}

	public void setTypeString(String typeString) {
		this.typeString = typeString;
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
				if (jsonArray.length() >= 0) {
					// 更新操作
					if (!isLoad) {
						application.dBManager.deleteAllBailList();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					Moneyrecord moneyRecord = new Moneyrecord();
					moneyRecord.setSysId(jObject.optString("id"));
					moneyRecord.setDateTimestamp(jObject.optString("dateTimestamp"));
					moneyRecord.setPrice(jObject.optString("price"));
					moneyRecord.setAuctionPrice(jObject.optString("auctionPrice"));
					moneyRecord.setType(jObject.optString("type"));
					moneyRecord.setActionType(jObject.optString("actionType"));
					moneyRecord.setAuctionId(jObject.optString("auctionId"));
					moneyRecord.setTypeString(jObject.optString("typeString"));
					// 保存订单列表信息
					application.dBManager.saveBailList(moneyRecord);

				}
				
			}else {
				application.dBManager.deleteAllBailList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
}
