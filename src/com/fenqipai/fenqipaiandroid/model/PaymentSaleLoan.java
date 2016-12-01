package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 销售方案确认查询实体类
 * @author hunaixin
 *
 */
@SuppressWarnings("serial")
public class PaymentSaleLoan extends BaseModel implements Serializable{
	
	// 方案名称
	private String productName;
	
	// 首付
	private int minPayRatio;
	
	// 保证金
	private int bailRatio;
	
	// 购置税
	private String tax;
	
	// 保险
	private String insurance;
	
	// GPS
	private String GPS;
	
	// 车款全名
	private String carAllName;
	

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public int getMinPayRatio() {
		return minPayRatio;
	}

	public void setMinPayRatio(int minPayRatio) {
		this.minPayRatio = minPayRatio;
	}

	public int getBailRatio() {
		return bailRatio;
	}

	public void setBailRatio(int bailRatio) {
		this.bailRatio = bailRatio;
	}

	public String getTax() {
		return tax;
	}

	public void setTax(String tax) {
		this.tax = tax;
	}

	public String getInsurance() {
		return insurance;
	}

	public void setInsurance(String insurance) {
		this.insurance = insurance;
	}

	public String getGPS() {
		return GPS;
	}

	public void setGPS(String gPS) {
		GPS = gPS;
	}

	public String getCarAllName() {
		return carAllName;
	}

	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}
	
	
	/**
	 * 解析字符串
	 * 
	 * @title parse
	 * @return
	 */
	public static void parse(BaseApplication application, String result) {

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				
				//删除数据库所有以前记录
				if (jsonArray.length()>=0) {
					application.dBManager.deleteAllPaymentSaleLoanList();
				}
				
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					PaymentSaleLoan paymentSaleLoan = new PaymentSaleLoan();
                    paymentSaleLoan.setSysId(jObject.optString("id"));
                    paymentSaleLoan.setProductName(jObject.optString("productName"));
                    paymentSaleLoan.setMinPayRatio(jObject.optInt("minPayRatio"));
                    paymentSaleLoan.setGPS(jObject.optString("gpsExpanse"));
                    paymentSaleLoan.setTax(jObject.optString("tax"));
                    paymentSaleLoan.setInsurance(jObject.optString("insurance"));
                    paymentSaleLoan.setBailRatio(jObject.optInt("bailRatio"));
                    
                    
					// 保存求助信息
					application.dBManager.savePaymentSaleLoanList(paymentSaleLoan);

				}
				
			}else {
				application.dBManager.deleteAllPaymentSaleLoanList();;
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
	

}
