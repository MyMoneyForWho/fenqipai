package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 支持银行列表实体类
 * @author hunaixin
 *
 */
public class BankList extends BaseModel implements Serializable{

	// 银行姓名
	private String name;
	
	// 排序
	private String orderby;
	
	// 是否删除
	private String isdelete;
	
	// 是否选择
	private Boolean isChoose;
	

	public Boolean getIsChoose() {
		return isChoose;
	}

	public void setIsChoose(Boolean isChoose) {
		this.isChoose = isChoose;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOrderby() {
		return orderby;
	}

	public void setOrderby(String orderby) {
		this.orderby = orderby;
	}

	public String getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(String isdelete) {
		this.isdelete = isdelete;
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

				if (jsonArray.length()>=0) {//删除数据库所有以前记录
					application.dBManager.deleteAllBankList();
				}
				
				
				for (int i = 0; i < jsonArray.length(); i++) {
					
					JSONObject jObject = jsonArray.optJSONObject(i);
					BankList bankList = new BankList();
                    bankList.setSysId(jObject.optString("id"));
                    bankList.setName(jObject.optString("name"));
                    bankList.setOrderby(jObject.optString("orderby"));
                    bankList.setIsdelete(jObject.optString("isdelete"));
                    bankList.setIsChoose(false);
                    
					// 保存求助信息
					application.dBManager.saveBankList(bankList);

				}
				
			}else {
				application.dBManager.deleteAllBankList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
}
