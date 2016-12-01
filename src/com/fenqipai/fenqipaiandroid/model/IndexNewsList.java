package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

public class IndexNewsList extends BaseModel{

	// 新闻标题
	private String title;
	
	// 新闻内容
	private String body;
	
	// 新闻缩略图
	private String imagePath;
	
	// url
	private String viewurl;
	
	// 时间
	private String time;
	
	

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getViewurl() {
		return viewurl;
	}

	public void setViewurl(String viewurl) {
		this.viewurl = viewurl;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
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
				JSONObject jObject = jsonObject.optJSONObject("data");
				// 首页新闻列表
				JSONArray jsonArray = jObject.optJSONArray("news_platform_List");
				if (jsonArray.length() >= 0) {
				    application.dBManager.deleteNewsList();
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsObject = jsonArray.optJSONObject(i);
					IndexNewsList indexNewsList = new IndexNewsList();
 
					indexNewsList.setSysId(jsObject.optString("id"));
					indexNewsList.setTitle(jsObject.optString("title"));
					indexNewsList.setBody(jsObject.optString("body"));
					indexNewsList.setImagePath(jsObject.optString("imagePath"));
					indexNewsList.setViewurl(jsObject.optString("viewurl"));
					indexNewsList.setTime(TimeUtils.convertTimeToDate(jsObject.optString("dateTimestamp")));
					// 保存求助信息
					application.dBManager.saveNewsList(indexNewsList);
				}
				
				// 首页轮播图的数据
				JSONArray jArray = jObject.optJSONArray("news_homelunbo_List");
				if (jArray.length() >= 0) {
					application.dBManager.deleteLunboList();
				}
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject object = jArray.optJSONObject(i);
					HomeLunboList homeLunboList = new HomeLunboList();
					
					homeLunboList.setSysId(object.optString("id"));
					homeLunboList.setImagePath(object.optString("imagePath"));
					homeLunboList.setViewurl(object.optString("viewurl"));
					// 保存信息
					application.dBManager.saveLunboList(homeLunboList);;
				}
				
				
			}else {
				application.dBManager.deleteNewsList();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}

	
	
}
