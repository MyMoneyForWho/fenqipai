package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;

public class SaleCar extends CarInfo {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	
	//????
	private String flatlyPrice;

	//开始时间
	private String startTime;

	//起始价格
	private String startingPrice;

	//加价倍数
	private String increasePrice;

	//结束时间
	private String endTime;

	//车商品详细id
	private String carsourceId;

	//出价人数
	private String auctionCount;

	// 年检到期. 
	public  String annualInspection;
	
	// 年款. 
	public  String ageLimit ;
	
	// 里程. 
	public  String mileage ;

	// 过户次数. 
	public  String changeNumber;

	// 上牌时间. 
	public  String  registrationTime ;

	// 保险到期. 
	public  String insurance ;

	// 价格. 
	public String carPrice;

	// 颜色. 
	public  String carColor ;
	
	//是否是自营
	public String adminUserId;
	
	//排量
	public String displacement;
	
	//当前价格
	public String nowPrice;
	
	// 围观人数
	private String onlookersCount;
	
	// 出价人数
	private String personCount;
	
	// 竞拍类别 1高拍 2快拍
	private String auctionType;
	
	
	public String getAuctionType() {
		return auctionType;
	}

	public void setAuctionType(String auctionType) {
		this.auctionType = auctionType;
	}

	public String getNowPrice() {
		return nowPrice;
	}

	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}

	public String getDisplacement() {
		return displacement;
	}

	public void setDisplacement(String displacement) {
		this.displacement = displacement;
	}


	public String getAnnualInspection() {
		return annualInspection;
	}

	public void setAnnualInspection(String annualInspection) {
		this.annualInspection = annualInspection;
	}

	public String getAgeLimit() {
		return ageLimit;
	}

	public void setAgeLimit(String ageLimit) {
		this.ageLimit = ageLimit;
	}

	public String getChangeNumber() {
		return changeNumber;
	}

	public void setChangeNumber(String changeNumber) {
		this.changeNumber = changeNumber;
	}

	public String getRegistrationTime() {
		return registrationTime;
	}

	public void setRegistrationTime(String registrationTime) {
		this.registrationTime = registrationTime;
	}

	public String getCarPrice() {
		return carPrice;
	}

	public void setCarPrice(String carPrice) {
		this.carPrice = carPrice;
	}

	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public String getAdminUserId() {
		return adminUserId;
	}

	public void setAdminUserId(String adminUserId) {
		this.adminUserId = adminUserId;
	}


	public String getMileage() {
		return mileage;
	}

	public void setMileage(String mileage) {
		this.mileage = mileage;
	}


	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}


	public String getAuctionCount() {
		return auctionCount;
	}

	public void setAuctionCount(String auctionCount) {
		this.auctionCount = auctionCount;
	}

	public String getFlatlyPrice() {
		return flatlyPrice;
	}

	public void setFlatlyPrice(String flatlyPrice) {
		this.flatlyPrice = flatlyPrice;
	}
	

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(String startingPrice) {
		this.startingPrice = startingPrice;
	}

	public String getIncreasePrice() {
		return increasePrice;
	}

	public void setIncreasePrice(String increasePrice) {
		this.increasePrice = increasePrice;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCarsourceId() {
		return carsourceId;
	}

	public void setCarsourceId(String carsourceId) {
		this.carsourceId = carsourceId;
	}
	
	public String getOnlookersCount() {
		return onlookersCount;
	}

	public void setOnlookersCount(String onlookersCount) {
		this.onlookersCount = onlookersCount;
	}

	public String getPersonCount() {
		return personCount;
	}

	public void setPersonCount(String personCount) {
		this.personCount = personCount;
	}

	/**
	 * 解析字符串
	 * 
	 * @title parse
	 * @author zhaoqingyang
	 * @param application
	 * @param isLoad
	 * @param http_post
	 * @return
	 */
	public static void parse(BaseApplication application, String result,
			boolean isLoad) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				if (jsonArray.length() >= 0) {
					// 更新操作
					if (!isLoad) {
						application.dBManager.deleteAllAuctionList();
					}
				}

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					SaleCar saleCar = new SaleCar();
					saleCar.setSysId(jObject.optString("id"));
					saleCar.setStartTime(jObject.optString("startTime"));
					saleCar.setStartingPrice(MoneyUtils.toWan(jObject.optString("startingPrice")));
					saleCar.setCarAllName(jObject.optString("carAllName"));
					saleCar.setImagePath(jObject.optString("imagePath"));
					saleCar.setEndTime(jObject.optString("endTime"));
					saleCar.setCarsourceId(jObject.optString("carsourceId"));
					saleCar.setIncreasePrice(jObject.optString("increasePrice"));
					saleCar.setFlatlyPrice(jObject.optString("buyPrice"));
					saleCar.setAuctionCount(jObject.optString("auctionCount"));
					saleCar.setAdminUserId(jObject.optString("adminUserId"));
					saleCar.setNowPrice(MoneyUtils.toWan(jObject.optString("nowprice")));
					saleCar.setOnlookersCount(jObject.optString("onlookersCountString"));
					saleCar.setPersonCount(jObject.optString("personCount"));
					saleCar.setAuctionType(jObject.optString("auctionType"));
					saleCar.setCarStatus(jObject.optString("carstatus"));
					
					saleCar.setDisplacement(MoneyUtils.toDisplacement(jObject.optString(Contants.DISPLACEMENT)));
					saleCar.setAnnualInspection(jObject.optString(Contants.ANNUAL_INSPECTION));
					saleCar.setAgeLimit(jObject.optString(Contants.AGE_LIMIT));
					saleCar.setMileage(jObject.optString(Contants.MILEAGE));
					saleCar.setChangeNumber(jObject.optString(Contants.CHANGE_NUMBER));
					saleCar.setRegistrationTime(jObject.optString(Contants.REGISTRATION_TIME));
					saleCar.setInsurance(jObject.optString(Contants.INSURANCE));
					saleCar.setCarPrice(jObject.optString(Contants.CAR_PRICE));
					saleCar.setCarColor(jObject.optString(Contants.CAR_COLOR));

					// 保存求助信息
					application.dBManager.saveAuction(saleCar);

				}
				
			}else {
				application.dBManager.deleteAllAuctionList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

}
