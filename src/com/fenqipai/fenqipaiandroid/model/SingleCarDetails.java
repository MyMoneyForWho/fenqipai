package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.common.Contants;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;

/**
 * 竞拍车辆详情实体类
 * @author hunaixin
 *
 */
public class SingleCarDetails extends CarInfo{
	
	// 初登日期
	private String firstRegist;
	
	// 里程
	private String mileages;
	
	// 加价
	private String addPrice;
	
	// 围观人数
	private String onlookersCount;
	
	// 出价人数
	private String personCount;
	
	// 销售状态(厂商车况)
	private String saleType;
	
	// 年款
	private String years;
	
	// 最高车速
	private String maxSpeed;
	
	// 官方0-100km/h加速(s)
	private String officialAcceleration;
	
	// 实测0-100km/h加速(s) 
	private String foundAcceleration;
	
	// 实测100-0km/h制动(m)
	private String foundBrake;
	
	// 工信部综合油耗(L/100km) 
	private String officialFuel;
	
	// 实测油耗(L/100km)
	private String foundFuel;
	
	// 整车质保
	private String warranty;
	
	// 级别
	private String level;

	//当前价格
	public String nowPrice;
	
	// 系统时间
	private String serverTime;
	
	// 更多属性URL
    private String viewUrl;
    
    // 车辆颜色
    private String carColor;
	
    
	
	public String getCarColor() {
		return carColor;
	}


	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}


	public String getViewUrl() {
		return viewUrl;
	}


	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}


	public String getServerTime() {
		return serverTime;
	}


	public void setServerTime(String serverTime) {
		this.serverTime = serverTime;
	}


	public String getNowPrice() {
		return nowPrice;
	}


	public void setNowPrice(String nowPrice) {
		this.nowPrice = nowPrice;
	}


	public String getSaleType() {
		return saleType;
	}


	public void setSaleType(String saleType) {
		this.saleType = saleType;
	}


	public String getYears() {
		return years;
	}


	public void setYears(String years) {
		this.years = years;
	}


	public String getMaxSpeed() {
		return maxSpeed;
	}


	public void setMaxSpeed(String maxSpeed) {
		this.maxSpeed = maxSpeed;
	}


	public String getOfficialAcceleration() {
		return officialAcceleration;
	}


	public void setOfficialAcceleration(String officialAcceleration) {
		this.officialAcceleration = officialAcceleration;
	}


	public String getFoundAcceleration() {
		return foundAcceleration;
	}


	public void setFoundAcceleration(String foundAcceleration) {
		this.foundAcceleration = foundAcceleration;
	}


	public String getFoundBrake() {
		return foundBrake;
	}


	public void setFoundBrake(String foundBrake) {
		this.foundBrake = foundBrake;
	}


	public String getOfficialFuel() {
		return officialFuel;
	}


	public void setOfficialFuel(String officialFuel) {
		this.officialFuel = officialFuel;
	}


	public String getFoundFuel() {
		return foundFuel;
	}


	public void setFoundFuel(String foundFuel) {
		this.foundFuel = foundFuel;
	}


	public String getWarranty() {
		return warranty;
	}


	public void setWarranty(String warranty) {
		this.warranty = warranty;
	}


	public String getLevel() {
		return level;
	}


	public void setLevel(String level) {
		this.level = level;
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


	public String getFirstRegist() {
		return firstRegist;
	}


	public void setFirstRegist(String firstRegist) {
		this.firstRegist = firstRegist;
	}


	public String getMileages() {
		return mileages;
	}


	public void setMileages(String mileages) {
		this.mileages = mileages;
	}
	

	public String getAddPrice() {
		return addPrice;
	}

	public void setAddPrice(String addPrice) {
		this.addPrice = addPrice;
	}



	/**
	 * 解析字符串
	 * @author hunaixin
	 * @param auctionId
	 * 
	 */
	public static void parse(BaseApplication application, String result) {
		SingleCarDetails singleCarDetails = new SingleCarDetails();
		
		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONObject jObject = jsonObject.optJSONObject("data");
				
				singleCarDetails.setSysId(jObject.optString("id"));
				singleCarDetails.setStartTime(jObject.optString("startTime"));
				singleCarDetails.setEndTime(jObject.optString("endTime"));
				singleCarDetails.setCarsourceId(jObject.optString("carsourceId"));
				singleCarDetails.setStartingPrice(jObject.optString("startingPrice"));
				singleCarDetails.setIncreasePrice(jObject.optString("increasePrice"));
				singleCarDetails.setFlatlyPrice(MoneyUtils.toWan(jObject.optString("buyPrice")));
				singleCarDetails.setCarAllName(jObject.optString("carAllName"));
				singleCarDetails.setImagePath(jObject.optString("imagePath"));
				singleCarDetails.setFirstRegist(jObject.optString("s_daa3c84d-4e46-4dc3-a3f1-505393d2bd90"));
				singleCarDetails.setMileages(jObject.optString(Contants.MILEAGE));
				singleCarDetails.setImagePath(jObject.optString("imagePath"));
				singleCarDetails.setNowPrice(jObject.optString("nowPrice"));
				singleCarDetails.setOnlookersCount(jObject.optString("onlookersCountString"));
				singleCarDetails.setPersonCount(jObject.optString("personCount"));
				singleCarDetails.setServerTime(jObject.optString("serverTime"));
				singleCarDetails.setViewUrl(jObject.optString("viewUrl"));
				
				// 配置参数
				singleCarDetails.setSaleType(jObject.optString("c_4ebfd23a-2eca-41c7-b133-d1620040266a"));
				singleCarDetails.setYears(jObject.optString("c_42da9715-f5df-4bb4-874e-6ac2ce0e84f8"));
				singleCarDetails.setMaxSpeed(jObject.optString("c_93f1103c-cde5-438d-961f-33d17ea04ca8"));
				singleCarDetails.setOfficialAcceleration(jObject.optString("c_37fa034a-09e9-4d44-b243-263a2dbac4c7"));
				singleCarDetails.setFoundAcceleration(jObject.optString("c_dc75987c-870e-4463-9b02-bbeb7e4b2fc4"));
				singleCarDetails.setFoundBrake(jObject.optString("c_77cd5efa-bb66-4bc3-98c8-1ac08f9e4957"));
				singleCarDetails.setOfficialFuel(jObject.optString("c_280ac240-a6d3-440e-a524-6d8e006bf2e3"));
				singleCarDetails.setFoundFuel(jObject.optString("c_5ce85279-ce62-428a-85a8-3a6286c21633"));
				singleCarDetails.setWarranty(jObject.optString("c_816e5c8a-d42b-413b-8344-ecee2c595994"));
				singleCarDetails.setLevel(jObject.optString("c_a3936a87-eaed-4b1d-808b-db6c729c3f3a"));
				singleCarDetails.setCarColor(jObject.optString("colorName"));
				
				// 出价列表
				JSONObject mObject=jObject.optJSONObject("priceList");
				singleCarDetails.setAddPrice(mObject.optString("1"));
				singleCarDetails.setAddPrice(singleCarDetails.getAddPrice()+","+mObject.optString("2"));
				singleCarDetails.setAddPrice(singleCarDetails.getAddPrice()+","+mObject.optString("3"));
				singleCarDetails.setAddPrice(singleCarDetails.getAddPrice()+","+mObject.optString("4"));
				singleCarDetails.setAddPrice(singleCarDetails.getAddPrice()+","+mObject.optString("5"));
				
				
				// 保存信息
				application.dBManager.saveAuctionInfo(singleCarDetails);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
