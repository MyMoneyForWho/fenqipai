package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 速递资讯列表实体类
 * 
 * @name ExpressList
 * @author liuchengbao
 * @date 2015年10月8日
 * @modify
 * @modifyDate 2015年10月8日
 * @modifyContent
 */
public class Commodity implements Serializable {

	private static final long serialVersionUID = -8745593874203819348L;

	// 本地数据库id
	private Long id = 0L;

	// web端数据库id
	private String sysId;

	// web端更新时间
	private long updateTime;
	
	private String content;
	
	public Long getId() {
		return id;
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
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}
	
	/**
	 * 解析字符串
	 * 
	 * @title parse
	 * @author zhaoqingyang
	 * @param application
	 * @param isLoad
	 * @param http_post
	 */
	public static void parse(BaseApplication application, String result, boolean isLoad) {
		if (!result.equals("")) {
			Gson gson = new Gson();
			List<String> resultList = gson.fromJson(result, new TypeToken<List<String>>(){}.getType());

			// 如果是更新操作
			if (!isLoad) {
				application.dBManager.deleteAllCommoditysList("");
			}

			for (int i = 0; i < resultList.size(); i++) {
				Commodity commodity = new Commodity();
				String r = resultList.get(i);
				commodity.setSysId(i + "");
				commodity.setContent(r);
				// 保存速递信息
				application.dBManager.saveCommodityInfo(commodity);

			}
		}

	}

	/**
	 * 解析一条数据
	 */
	public static void parseById(BaseApplication application, String result) {
		if (!result.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				Commodity commodity = new Commodity();

				// 保存速递信息
				application.dBManager.saveCommodityInfo(commodity);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}


}
