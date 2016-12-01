package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;


/**
 * 分期车辆实体类
 * @author hunaixin
 *
 */
public class InstallmentCar extends CarInfo {

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
	
	// 首付
	private String minPayRatio;
	
	// 月租金
	private String AMR;
	
	// GPS费用
	private String gpsExpanse;
	
	// 期限（月）
	private String loanTerm;
	
	// 保险
	private String insurance;
	
	// 购置税
	private String tax;
	
	// 前期提车费
	private String downPaymentPrice;
	

	public String getDownPaymentPrice() {
		return downPaymentPrice;
	}

	public void setDownPaymentPrice(String downPaymentPrice) {
		this.downPaymentPrice = downPaymentPrice;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getMinPayRatio() {
		return minPayRatio;
	}

	public void setMinPayRatio(String minPayRatio) {
		this.minPayRatio = minPayRatio;
	}

	public String getAMR() {
		return AMR;
	}

	public void setAMR(String aMR) {
		AMR = aMR;
	}

	public String getGpsExpanse() {
		return gpsExpanse;
	}

	public void setGpsExpanse(String gpsExpanse) {
		this.gpsExpanse = gpsExpanse;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
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

	/**
	 * 解析字符串
	 * 
	 * @title parse
	 * @author 
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
						application.dBManager.deleteAllInstallmentList();
					}
				}

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					InstallmentCar installmentCar = new InstallmentCar();
					
					installmentCar.setSysId(jObject.optString("id"));
					installmentCar.setCarAllName(jObject.optString("carAllName"));
					installmentCar.setImagePath(jObject.optString("imagePath"));
					installmentCar.setCarPrice(jObject.optString("salesprice"));
					installmentCar.setDownPaymentPrice(jObject.optString("downPaymentPrice"));
					installmentCar.setCarStatus(jObject.optString("carstatus"));
					JSONObject jsObject = jObject.optJSONObject("loanAlgorithm");
					if (jsObject==null) {
						jsObject=new JSONObject();
					}
					installmentCar.setInsurance(jsObject.optString("insurance"));
					installmentCar.setTax(jsObject.optString("tax"));
					installmentCar.setMinPayRatio(jsObject.optString("minPayRatio"));
					installmentCar.setAMR(jsObject.optString("AMR"));
					installmentCar.setGpsExpanse(jsObject.optString("gpsExpanse"));
					installmentCar.setLoanTerm(jsObject.optString("loanTerm"));

					// 保存求助信息
					application.dBManager.saveInstallmentList(installmentCar);

				}
				
			}else {
				application.dBManager.deleteAllInstallmentList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

}
