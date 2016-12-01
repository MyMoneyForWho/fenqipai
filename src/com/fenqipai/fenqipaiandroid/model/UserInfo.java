package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;

/**
 * 用户信息
 * @author hunaixin
 */
public class UserInfo extends BaseModel   implements Serializable {

	private static final long serialVersionUID = -3776896662484413483L;
    
	// 本地数据库id
	private Long id = 0L;

	// web端数据库id
	private String sysId;

	// web端更新时间
	private long updateTime;
    
	// 账号
	public String account;
	 
	// 用户id
	public String userId;
		
	// 昵称
	public String nickName;

	// 密码
	public String password;

	// 手机
	public String mobile;

	// 头像
	public String protrait;

	// 注册时间
	public String registerDate;

	// 注册时间戳
	public String dateTimestamp;

	// 真实姓名
	public String trueName;

	// 性别：男，女
	public String sex;

	// 生日
	public String birthday;

	// 地址
	public String address;

	// 职业
	public String job;

	// 身份证号
	public String card;

	// 邮箱
	public String email;

	// 简介
	public String description;

	// QQ登陆绑定码
	public String qqBindingCode;

	// 微信登陆绑定码
	public String weixinBindingCode;

	// 客户来源，手机、微信、微博等
	public String userFrom;

	// 手机类型
	public String phoneType;

	// 用户状态:0正常,1锁定
	public String status;
	
	//登录状态
	private Boolean loginState;
	
	//余额
	private String balance;
	
	
	private String token;
	
	//分期额度 
	private String loanValue;

	//用户等级名称那个
	private String userGradeName;

	//是否认证
	private String isAuth;

	//保证金
	private String bail;
	
	//是否有提现密码 1代表true 0代表false 
	private int isPayPassword;
	
	// 是否有登录密码
	private int isPassword;
	
	

	public int getIsPayPassword() {
		return isPayPassword;
	}



	public void setIsPayPassword(int isPayPassword) {
		this.isPayPassword = isPayPassword;
	}



	public int getIsPassword() {
		return isPassword;
	}



	public void setIsPassword(int isPassword) {
		this.isPassword = isPassword;
	}



	public String getProtrait() {
		return protrait;
	}



	public void setProtrait(String protrait) {
		this.protrait = protrait;
	}



	public String getBail() {
		return bail;
	}



	public void setBail(String bail) {
		this.bail = bail;
	}



	public String getToken() {
		return token;
	}



	public String getLoanValue() {
		return loanValue;
	}



	public void setLoanValue(String loanValue) {
		this.loanValue = loanValue;
	}



	public String getUserGradeName() {
		return userGradeName;
	}



	public void setUserGradeName(String userGradeName) {
		this.userGradeName = userGradeName;
	}



	public String getIsAuth() {
		return isAuth;
	}



	public void setIsAuth(String isAuth) {
		this.isAuth = isAuth;
	}



	public void setToken(String token) {
		this.token = token;
	}



	public String getBalance() {
		return balance;
	}



	public void setBalance(String balance) {
		this.balance = balance;
	}



	public Boolean getLoginState() {
		return loginState;
	}
	
	

	public String getUserId() {
		return userId;
	}



	public void setUserId(String userId) {
		this.userId = userId;
	}



	public void setLoginState(Boolean loginState) {
		this.loginState = loginState;
	}


	public void setId(Long id) {
		this.id = id;
	}

	public String getSysId() {
		return sysId;
	}

	public void setSysId(String sysId) {
		this.sysId = sysId;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getPortrait() {
		return protrait;
	}

	public void setPortrait(String protrait) {
		this.protrait = protrait;
	}

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public String getDateTimestamp() {		
		return dateTimestamp;
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public String getTrueName() {
		return trueName;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getQqBindingCode() {
		return qqBindingCode;
	}

	public void setQqBindingCode(String qqBindingCode) {
		this.qqBindingCode = qqBindingCode;
	}

	public String getWeixinBindingCode() {
		return weixinBindingCode;
	}

	public void setWeixinBindingCode(String weixinBindingCode) {
		this.weixinBindingCode = weixinBindingCode;
	}

	public String getUserFrom() {
		return userFrom;
	}

	public void setUserFrom(String userFrom) {
		this.userFrom = userFrom;
	}

	public String getPhoneType() {
		return phoneType;
	}

	public void setPhoneType(String phoneType) {
		this.phoneType = phoneType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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
	public static void parse(BaseApplication application, String result) {

		try {
			
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONObject jbject = jsonObject.optJSONObject("data");
				UserInfo userInfo=new UserInfo();
				userInfo.setSysId(jbject.optString("id"));
				userInfo.setAccount(jbject.optString("account"));
				userInfo.setNickName(jbject.optString("nickName"));
				userInfo.setMobile(jbject.optString("mobile"));
				userInfo.setPortrait(jbject.optString("portrait"));
//				userInfo.setRegisterDate(jbject.optString("registerDate"));
				userInfo.setTrueName(jbject.optString("trueName"));
				userInfo.setSex(jbject.optString("sex"));
				userInfo.setBirthday(jbject.optString("birthday"));
				userInfo.setAddress(jbject.optString("address"));
				userInfo.setJob(jbject.optString("job"));
				userInfo.setCard(jbject.optString("card"));
				userInfo.setEmail(jbject.optString("email"));
				userInfo.setStatus(jbject.optString("status"));
				userInfo.setIsAuth(jbject.optString("isAuth"));
				userInfo.setBalance(jbject.optString("balance"));
				userInfo.setBail(jbject.optString("bail"));
				userInfo.setLoanValue(jbject.optString("loanValue"));
				userInfo.setUserGradeName(jbject.optString("userGradeName"));
				userInfo.setIsPassword(jbject.optInt("isPassword"));
				userInfo.setIsPayPassword(jbject.optInt("isPayPassword"));
				// 保存求助信息
				application.dBManager.saveMyInfo(userInfo);
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
 	}
    
	
}
