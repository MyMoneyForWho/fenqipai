package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 根据分期业务获取对应信息实体类
 * @author hunaixin
 *
 */
@SuppressWarnings("serial")
public class LoanAlgorithmList extends BaseModel{

	// 分期期限
	private String loanTerm;
	
	// 每月还款金额
	private String AMR;
	
	// 前期提车费
	private String downPayment;
	
	// 总价
	private String totalPrice;
	
	
	

	public String getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}

	public String getDownPayment() {
		return downPayment;
	}

	public void setDownPayment(String downPayment) {
		this.downPayment = downPayment;
	}

	public String getLoanTerm() {
		return loanTerm;
	}

	public void setLoanTerm(String loanTerm) {
		this.loanTerm = loanTerm;
	}

	public String getAMR() {
		return AMR;
	}

	public void setAMR(String aMR) {
		AMR = aMR;
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
					application.dBManager.deleteAllLoanAlgorithmListt();
				}
				
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					LoanAlgorithmList loanAlgorithmList = new LoanAlgorithmList();
                    loanAlgorithmList.setSysId(jObject.optString("id"));
                    loanAlgorithmList.setLoanTerm(jObject.optString("loanTerm"));
                    loanAlgorithmList.setAMR(jObject.optString("AMR"));
                    loanAlgorithmList.setDownPayment(jObject.optString("downPayment"));
                    loanAlgorithmList.setTotalPrice(jObject.optString("totalPrice"));
                    
                    
					// 保存求助信息
					application.dBManager.saveLoanAlgorithmList(loanAlgorithmList);;

				}
				
			}else {
				application.dBManager.deleteAllLoanAlgorithmListt();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
	
}
