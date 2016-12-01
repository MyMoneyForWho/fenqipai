package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

public class StagingBillDetails extends BaseModel{
	
	// 总期数
	private String orderRepSize;
	
	// 订单编号
	private String orderNumber;
	
	// 每期应还钱数
	private String repaymentPrice;
	
	// 还款期数
	private String periods;
	
	// 
	private String dateTimestamp;
	
	// 是否选择
	private Boolean isChecked;

	// 车名
	private String carAllName;

	// 还款期限
	private String lastDate;
	
	
	
	public String getLastDate() {
		return lastDate;
	}

	public void setLastDate(String lastDate) {
		this.lastDate = lastDate;
	}

	public String getCarAllName() {
		return carAllName;
	}

	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}

	public String getOrderRepSize() {
		return orderRepSize;
	}

	public void setOrderRepSize(String orderRepSize) {
		this.orderRepSize = orderRepSize;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getRepaymentPrice() {
		return repaymentPrice;
	}

	public void setRepaymentPrice(String repaymentPrice) {
		this.repaymentPrice = repaymentPrice;
	}

	public String getPeriods() {
		return periods;
	}

	public void setPeriods(String periods) {
		this.periods = periods;
	}

	public String getDateTimestamp() {
		return dateTimestamp;
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public Boolean getIsChecked() {
		return isChecked;
	}

	public void setIsChecked(Boolean isChecked) {
		this.isChecked = isChecked;
	}
	
	/**
	 * 解析字符串
	 * @author hunaixin
	 */
	public static void parse(BaseApplication application, String result, boolean isLoad) {
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONObject jsObject = jsonObject.optJSONObject("data");
				JSONArray jsonArray = jsObject.optJSONArray("repaymentList");
				if (jsonArray.length() >= 0) {
					// 更新操作
					if (!isLoad) {
						application.dBManager.deleteAllStagingBillDetails();
					}
				}
				if (jsonArray!=null&&jsonArray.length()>=0) {
					
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					StagingBillDetails stagingBillDetails = new StagingBillDetails();
					
					stagingBillDetails.setOrderRepSize(jsObject.optString("orderRepSize"));
					stagingBillDetails.setOrderNumber(jsObject.optString("orderNumber"));
					stagingBillDetails.setCarAllName(jsObject.optString("carAllName"));
					stagingBillDetails.setSysId(jObject.optString("id"));
                    stagingBillDetails.setDateTimestamp(TimeUtils.convertTimeToDate(jObject.optString("dateTimestamp")));   
					stagingBillDetails.setPeriods(jObject.optString("periods"));
					stagingBillDetails.setRepaymentPrice(jObject.optString("repaymentPrice"));
					stagingBillDetails.setLastDate(jObject.optString("lastDate"));
					stagingBillDetails.setIsChecked(false);
					
					// 保存分期账单信息
					application.dBManager.saveStagingBillDetails(stagingBillDetails);;

				}
				}
				
			}else {
				application.dBManager.deleteAllStagingBillDetails();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	

}
