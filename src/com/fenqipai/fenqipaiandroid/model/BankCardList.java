package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 绑定银行卡列表实体类
 * @author hunaixin
 *
 */
public class BankCardList extends BaseModel implements Serializable{

	// 银行名字
	private String bankName;
	
	// 排序
	private String orderby;
	
	// 是否删除
	private String isdelete;
	
	// 银行卡号
	private String no;
	
	// 用户姓名
	private String cardUserName;

	
	public String getCardUserName() {
		return cardUserName;
	}

	public void setCardUserName(String cardUserName) {
		this.cardUserName = cardUserName;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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
					application.dBManager.deleteAllBankCardList();
				}
				
				
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					BankCardList bankCardList = new BankCardList();
                    bankCardList.setSysId(jObject.optString("id"));
                    bankCardList.setBankName(jObject.optString("bankName"));
                    bankCardList.setNo(jObject.optString("NO"));
                    bankCardList.setCardUserName(jObject.optString("cardUserName"));
                    
					// 保存求助信息
					application.dBManager.saveBankCardList(bankCardList);

				}
				
			}else {
				application.dBManager.deleteAllBankCardList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	
	
}
