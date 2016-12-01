package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

public class MoneyChangeRecord extends BaseModel {

	private static final long serialVersionUID = 1L;

	private String dateTimestamp;

	private String payPrice;

	private String remark;
	

	public String getDateTimestamp() {
		return dateTimestamp;
	}


	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}


	public String getPayPrice() {
		return payPrice;
	}


	public void setPayPrice(String payPrice) {
		this.payPrice = payPrice;
	}


	public String getRemark() {
		return remark;
	}


	public void setRemark(String remark) {
		this.remark = remark;
	}


	/**
	 * 解析字符串
	 * 
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
						application.dBManager.deleteAllMoneyChangeRecord();;
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					MoneyChangeRecord moneyChangeRecord = new MoneyChangeRecord();
					moneyChangeRecord.setSysId(jObject.optString("id"));
					moneyChangeRecord.setDateTimestamp(jObject.optString("dateTimestamp"));
					moneyChangeRecord.setPayPrice(jObject.optString("payPrice"));
					moneyChangeRecord.setRemark(jObject.optString("remark"));
					// 保存订单列表信息
					application.dBManager.saveBMoneyChangeRecord(moneyChangeRecord);;

				}

			}else {
				application.dBManager.deleteAllMoneyChangeRecord();;
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

}
