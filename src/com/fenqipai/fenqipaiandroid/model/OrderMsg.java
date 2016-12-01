package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseModel;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.text.TextUtils;


/**
 * @ClassName:OrderMsg
 * @Description: 推送车辆加价信息
 * @author qianyuhang
 * @date 2016-9-18 下午4:27:21
 */


public class OrderMsg extends BaseModel{

	private static final long serialVersionUID = 1L;
	
	//出价人数
	private String pcount;
	
	//拍卖Id
	private String auctionId;
	
	//出价人手机号
	private String A_mobile;
	
	//出价人姓名
	private String A_trueName;
	
	//当前价格
	private String bidPrice;
	
	//出价人昵称
	private String nickName;
	
	//出价人id
	private String userId;
	
	//出价人数
	private String account;

	//记录生成时间
	private String pushtime;

	//系统时间
	private String dateTimestamp;

	//加价类型 "start": "拍卖开始","end": "拍卖加价","RPrice": "拍卖加价","message": "人人消息"
	private String event;

	// "1": "送给个人的消息","2": "主题推送","3": "广播"
	private String type;

	public String getPcount() {
		return pcount;
	}

	public void setPcount(String pcount) {
		this.pcount = pcount;
	}

	public String getAuctionId() {
		return auctionId;
	}

	public void setAuctionId(String auctionId) {
		this.auctionId = auctionId;
	}
 
	public String getA_mobile() {
		return A_mobile;
	}

	public void setA_mobile(String a_mobile) {
		A_mobile = a_mobile;
	}

	public String getA_trueName() {
		return A_trueName;
	}

	public void setA_trueName(String a_trueName) {
		A_trueName = a_trueName;
	}

	public String getBidPrice() {
		return bidPrice;
	}

	public void setBidPrice(String bidPrice) {
		this.bidPrice = bidPrice;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPushtime() {
		return pushtime;
	}

	public void setPushtime(String pushtime) {
		this.pushtime = pushtime;
	}

	public String getDateTimestamp() {
		return TimeUtils.convertTime(dateTimestamp);
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public String getEvent() {
		return event;
	}

	public void setEvent(String event) {
		this.event = event;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
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
	public static OrderMsg parse(String result) {
		OrderMsg orderMsg=new OrderMsg();
		try {
			
			JSONObject jsonObject = new JSONObject(result);
			String type=jsonObject.optString("type");
				if (TextUtils.isEmpty(type)) {
				}else if (type.equals("1")) {
				
					return orderMsg;
				}else if (type.equals("2")) {
					orderMsg.setEvent(jsonObject.optString("event"));
					orderMsg.setType(jsonObject.optString("type"));
					orderMsg.setPushtime(jsonObject.optString("pushtime"));
					orderMsg.setDateTimestamp(jsonObject.optString("dateTimestamp"));
					JSONObject jObject=jsonObject.optJSONObject("message");
					orderMsg.setPcount(jObject.optString("pcount"));
					orderMsg.setAuctionId(jObject.optString("auctionId"));
					JSONObject mObject=jObject.optJSONObject("pricemap");
					orderMsg.setA_mobile(mObject.optString("A_mobile"));
					orderMsg.setA_trueName(mObject.optString("A_trueName"));
					orderMsg.setBidPrice(mObject.optString("bidPrice"));
					orderMsg.setNickName(mObject.optString("nickName"));
					orderMsg.setUserId(mObject.optString("userId"));
					orderMsg.setAccount(mObject.optString("account"));
					
					
					
					return orderMsg;
				}else if (type.equals("3")) {
					
					
					
					
					return orderMsg;
				}
				
			
			
			
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return orderMsg;
	}
	
	
	

}
