package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.util.MoneyUtils;

/**
 * 分期车辆详情实体类
 * 
 * @author hunaixin
 *
 */
public class SingleSaleCarDetails extends CarInfo {

	// 初登日期
	private String firstRegist;

	// 里程
	private String mileages;

	// 加价
	private String addPrice;

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

	// 指导价
	private String officialguidePrice;
	
	// 更多属性URL
	private String viewUrl;
	
	// 库存
	private int remainnum;
	
	// 车辆颜色
	private String carColor;
	
	
	public String getCarColor() {
		return carColor;
	}

	public void setCarColor(String carColor) {
		this.carColor = carColor;
	}

	public int getRemainnum() {
		return remainnum;
	}

	public void setRemainnum(int remainnum) {
		this.remainnum = remainnum;
	}

	public String getViewUrl() {
		return viewUrl;
	}

	public void setViewUrl(String viewUrl) {
		this.viewUrl = viewUrl;
	}

	public String getOfficialguidePrice() {
		return officialguidePrice;
	}

	public void setOfficialguidePrice(String officialguidePrice) {
		this.officialguidePrice = officialguidePrice;
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
	 * 
	 * @author hunaixin
	 * @param auctionId
	 * 
	 */
	public static void parse(BaseApplication application, String result) {
		SingleSaleCarDetails singleSaleCarDetails = new SingleSaleCarDetails();

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONObject jObject = jsonObject.optJSONObject("data");
				singleSaleCarDetails.setSysId(jObject.optString("id"));
				singleSaleCarDetails.setCarsourceId(jObject.optString("carsourceId"));
				singleSaleCarDetails.setStartingPrice(MoneyUtils.toWan(jObject.optString("salesprice")));
				singleSaleCarDetails.setCarAllName(jObject.optString("carAllName"));
				singleSaleCarDetails.setImagePath(jObject.optString("imagePath"));
				singleSaleCarDetails.setOfficialguidePrice(MoneyUtils.toWan(jObject.optString("guidePrice")));
				singleSaleCarDetails.setViewUrl(jObject.optString("viewUrl"));
				singleSaleCarDetails.setRemainnum(jObject.optInt("remainnum"));

				// 配置参数
				singleSaleCarDetails.setSaleType(jObject.optString("c_4ebfd23a-2eca-41c7-b133-d1620040266a"));
				singleSaleCarDetails.setYears(jObject.optString("c_42da9715-f5df-4bb4-874e-6ac2ce0e84f8"));
				singleSaleCarDetails.setMaxSpeed(jObject.optString("c_93f1103c-cde5-438d-961f-33d17ea04ca8"));
				singleSaleCarDetails.setOfficialAcceleration(jObject.optString("c_37fa034a-09e9-4d44-b243-263a2dbac4c7"));
				singleSaleCarDetails.setFoundAcceleration(jObject.optString("c_dc75987c-870e-4463-9b02-bbeb7e4b2fc4"));
				singleSaleCarDetails.setFoundBrake(jObject.optString("c_77cd5efa-bb66-4bc3-98c8-1ac08f9e4957"));
				singleSaleCarDetails.setOfficialFuel(jObject.optString("c_280ac240-a6d3-440e-a524-6d8e006bf2e3"));
				singleSaleCarDetails.setFoundFuel(jObject.optString("c_5ce85279-ce62-428a-85a8-3a6286c21633"));
				singleSaleCarDetails.setWarranty(jObject.optString("c_816e5c8a-d42b-413b-8344-ecee2c595994"));
				singleSaleCarDetails.setLevel(jObject.optString("c_a3936a87-eaed-4b1d-808b-db6c729c3f3a"));
				singleSaleCarDetails.setCarColor(jObject.optString("colorName"));

				// 保存信息
				application.dBManager.saveSaleInfo(singleSaleCarDetails);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
