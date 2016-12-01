package com.fenqipai.fenqipaiandroid.base;

import java.io.Serializable;

/**
 * 所有model的基类
 * 
 * @name BaseModel
 * @author zhaoqingyang
 * @date 2015年10月26日
 * @modify
 * @modifyDate 2015年10月26日
 * @modifyContent
 */
public class BaseModel implements Serializable{

	// 本地数据库id
	protected Long id = 0L;

	// web端数据库id
	protected String sysId;

	// web端更新时间
	protected long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
