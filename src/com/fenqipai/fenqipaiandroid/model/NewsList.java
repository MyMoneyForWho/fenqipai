package com.fenqipai.fenqipaiandroid.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;
import com.fenqipai.fenqipaiandroid.base.BaseModel;
import com.fenqipai.fenqipaiandroid.util.TimeUtils;

import android.text.Html;
import android.util.Log;

public class NewsList extends BaseModel{
	
	// 新闻标题
	private String title;
	
	// 图片路径
	private String imagePath;
	
	// 时间
	private String dateTimestamp;
	
	// 新闻详情的Url
	private String viewurl;
	
	// 新闻内容
	private String body;
	

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getDateTimestamp() {
		return dateTimestamp;
	}

	public void setDateTimestamp(String dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public String getViewurl() {
		return viewurl;
	}

	public void setViewurl(String viewurl) {
		this.viewurl = viewurl;
	}
	
	

	/**
	 * 解析字符串
	 * @author hunaixin
	 */
	public static void parse(BaseApplication application, String result,boolean isLoad) {

		try {
			Log.i("AAASSSS", result);
			JSONObject jsonObject = new JSONObject(result);
			if (application.getLoginTimeOut(application, jsonObject.optString("code"))) {
				JSONArray jsonArray = jsonObject.optJSONArray("data");
				Log.i("ZZZZZZZZZSSSS", ""+jsonArray.length());
				// 更新操作
				if (!isLoad) {
						application.dBManager.deleteAllNews();
				}
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jObject = jsonArray.optJSONObject(i);
					NewsList newsList = new NewsList();
					newsList.setSysId(jObject.optString("id"));
                    newsList.setTitle(jObject.optString("title"));
                    newsList.setImagePath(jObject.optString("imagePath"));
                    newsList.setDateTimestamp(TimeUtils.convertTime(jObject.optString("dateTimestamp")));
					newsList.setViewurl(jObject.optString("viewurl"));
					newsList.setBody(jObject.optString("body"));
					
					// 保存订单列表信息
					application.dBManager.saveNews(newsList);

				}
				
			}else {
				application.dBManager.deleteAllNews();
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
	}
	

}
