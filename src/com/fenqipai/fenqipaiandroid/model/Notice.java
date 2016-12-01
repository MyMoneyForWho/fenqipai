package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;


/**
 * 轮播图实体类
 * 
 * @name Help
 * @author zhaoqingyang
 * @date 2015年9月29日
 * @modify
 * @modifyDate 2015年9月29日
 * @modifyContent
 */
public class Notice implements Serializable {

	private static final long serialVersionUID = 1333389510526737769L;

	// 本地数据库id
	private Long id = 0L;

	// web端数据库id
	private String sysId;

	// web端更新时间
	private long updateTime;

	// 轮播标题
	private String noticeTitle;

	// 轮播内容
	private String noticeContent;

	// 轮播图片
	private String noticeCover;

	// 排序的
	private int noticeNum;

	// 轮播地址
	private String noticeDetailUrl;

	// 轮播分享地址
	private String noticeShareUrl;

	// 轮播图简介(分享用)
	private String noticeOutline;

	/**
	 * 解析字符串
	 * 
	 * @title parse
	 * @author zhaoqingyang
	 * @param application
	 * @param http_post
	 * @return
	 */
	public static void parse(BaseApplication application, String result) {

		if (!result.equals("")) {
			JSONArray jsonArray = null;
			try {
				jsonArray = new JSONArray(result);

				if (jsonArray.length() >= 0) {
					application.dBManager.delAllNotice();
				}

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.optJSONObject(i);
					Notice notice = new Notice();

					notice.setSysId(jsonObject.optString("noticeId"));

					notice.setUpdateTime(jsonObject.optLong("dateTimestamp"));

					notice.setNoticeTitle(jsonObject.optString("noticeTitle"));

					notice.setNoticeContent(jsonObject.optString("noticeContent"));

					notice.setNoticeCover(jsonObject.optString("noticeCover"));

					notice.setNoticeDetailUrl(jsonObject.optString("noticeDetailUrl"));

					notice.setNoticeShareUrl(jsonObject.optString("noticeShareUrl"));

					notice.setNoticeOutline(jsonObject.optString("noticeOutline"));

					notice.setNoticeNum(jsonObject.optInt("noticeNum"));

					// 保存求助信息
					application.dBManager.saveNotice(notice);

				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 解析一条数据
	 * 
	 * @title parseById
	 * @author liuchengbao
	 * @param application
	 * @param result
	 */
	public static void parseById(BaseApplication application, String result) {
		if (!result.equals("")) {
			try {
				JSONObject jsonObject = new JSONObject(result);
				Notice notice = new Notice();

				notice.setSysId(jsonObject.optString("noticeId"));

				notice.setUpdateTime(jsonObject.optLong("dateTimestamp"));

				notice.setNoticeTitle(jsonObject.optString("noticeTitle"));

				notice.setNoticeContent(jsonObject.optString("noticeContent"));

				notice.setNoticeCover(jsonObject.optString("noticeCover"));

				notice.setNoticeDetailUrl(jsonObject.optString("noticeDetailUrl"));

				notice.setNoticeShareUrl(jsonObject.optString("noticeShareUrl"));

				notice.setNoticeOutline(jsonObject.optString("noticeOutline"));

				notice.setNoticeNum(jsonObject.optInt("noticeNum"));

				// 保存求助信息
				application.dBManager.saveNotice(notice);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public int getNoticeNum() {
		return noticeNum;
	}

	public void setNoticeNum(int noticeNum) {
		this.noticeNum = noticeNum;
	}

	public String getNoticeCover() {
		return noticeCover;
	}

	public void setNoticeCover(String noticeCover) {
		this.noticeCover = noticeCover;
	}

	public String getNoticeDetailUrl() {
		return noticeDetailUrl;
	}

	public void setNoticeDetailUrl(String noticeDetailUrl) {
		this.noticeDetailUrl = noticeDetailUrl;
	}

	public String getNoticeShareUrl() {
		return noticeShareUrl;
	}

	public void setNoticeShareUrl(String noticeShareUrl) {
		this.noticeShareUrl = noticeShareUrl;
	}

	public String getNoticeOutline() {
		return noticeOutline;
	}

	public void setNoticeOutline(String noticeOutline) {
		this.noticeOutline = noticeOutline;
	}

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

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

}
