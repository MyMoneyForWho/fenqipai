package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

public class StagingBill extends BaseModel {

	private static final long serialVersionUID = 1L;

	// 订单编号
	private String orderNumber;

	// 当前还款期数
	private String currentPeriods;

	// 还款日期
	private String repaymentDate;

	// 还款类型
	private String repaymentType;

	// 本期金额
	private String currentPrice;

	// 已还金额
	private String hasPayPrice;

	// 状态
	private String orderRepaymentStatusString;

	// 当前分期Id
	private String currentRepaymentId;

	// 总还款期数
	private String totalPeriods;

	// 车辆全名
	private String carAllName;

	// 订单状态
	private String orderStatus;

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCarAllName() {
		return carAllName;
	}

	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}

	public String getTotalPeriods() {
		return totalPeriods;
	}

	public void setTotalPeriods(String totalPeriods) {
		this.totalPeriods = totalPeriods;
	}

	public String getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(String orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCurrentPeriods() {
		return currentPeriods;
	}

	public void setCurrentPeriods(String currentPeriods) {
		this.currentPeriods = currentPeriods;
	}

	public String getRepaymentDate() {
		return repaymentDate;
	}

	public void setRepaymentDate(String repaymentDate) {
		this.repaymentDate = repaymentDate;
	}

	public String getRepaymentType() {
		return repaymentType;
	}

	public void setRepaymentType(String repaymentType) {
		this.repaymentType = repaymentType;
	}

	public String getCurrentPrice() {
		return currentPrice;
	}

	public void setCurrentPrice(String currentPrice) {
		this.currentPrice = currentPrice;
	}

	public String getHasPayPrice() {
		return hasPayPrice;
	}

	public void setHasPayPrice(String hasPayPrice) {
		this.hasPayPrice = hasPayPrice;
	}

	public String getOrderRepaymentStatusString() {
		return orderRepaymentStatusString;
	}

	public void setOrderRepaymentStatusString(String orderRepaymentStatusString) {
		this.orderRepaymentStatusString = orderRepaymentStatusString;
	}

	public String getCurrentRepaymentId() {
		return currentRepaymentId;
	}

	public void setCurrentRepaymentId(String currentRepaymentId) {
		this.currentRepaymentId = currentRepaymentId;
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
						application.dBManager.deleteAllStagingBill();
					}
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					StagingBill stagingBill = new StagingBill();

					stagingBill.setSysId(jObject.optString("id"));
					stagingBill.setOrderNumber(jObject.optString("orderNumber"));
					stagingBill.setCurrentPeriods(jObject.optString("currentPeriods"));
					stagingBill.setRepaymentDate(jObject.optString("repaymentDate"));
					stagingBill.setRepaymentType(jObject.optString("repaymentType"));
					stagingBill.setCurrentPrice(jObject.optString("currentPrice"));
					stagingBill.setHasPayPrice(jObject.optString("hasPayPrice"));
					stagingBill.setOrderRepaymentStatusString(jObject.optString("orderRepaymentStatusString"));
					stagingBill.setCurrentRepaymentId(jObject.optString("currentRepaymentId"));
					stagingBill.setTotalPeriods(jObject.optString("totalPeriods"));
					stagingBill.setCarAllName(jObject.optString("carAllName"));
					stagingBill.setOrderStatus(jObject.optString("orderStatus"));

					// 保存分期账单信息
					application.dBManager.saveStagingBill(stagingBill);

				}

			} else {
				application.dBManager.deleteAllStagingBill();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

}
