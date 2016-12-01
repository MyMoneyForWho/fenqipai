package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

public class MyLocation extends BaseModel {

	//经度
	private String longitude;
	
	//维度
	private String latitude;
	
	//电话
	private String tel;
	
	//地址
	private String address;
	
	//标题
	private String title;
	
	//标记  1默认，，0未选
	private String flag="0";



	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 解析字符串
	 * @author hunaixin
	 */
	public static void parse(BaseApplication application, String result){
		
		

		try {
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				if (jsonArray.length() >=0) {
					// 更新操作
						application.dBManager.deleteAllSeeCarPoint();
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					MyLocation myLocation = new MyLocation();
					myLocation.setSysId(jObject.optString("id"));
					myLocation.setTitle(jObject.optString("title"));
					myLocation.setTel(jObject.optString("tel"));
					myLocation.setAddress(jObject.optString("address"));
					myLocation.setLatitude(jObject.optString("latitude"));
					myLocation.setLongitude(jObject.optString("longitude"));
					if (i==0) {
						myLocation.setFlag("1");
					}else {
						myLocation.setFlag("0");
					}
					// 保存订单列表信息
					application.dBManager.saveSeeCarPoint(myLocation);

				}
				
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		
		
		
		
	}
}
