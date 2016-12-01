package com.fenqipai.fenqipaiandroid.model;

import java.io.Serializable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.fenqipai.fenqipaiandroid.base.BaseApplication;

/**
 *  汽车信息实体类
 * @author hunaixin
 *
 */
public class CarInfo implements Serializable{
	
	// 本地数据库id
	private Long id = 0L;

    // web端数据库id
	private String sysId;

	// web端更新时间
	private long updateTime;
	
	// 系统时间
	public long dateTimestamp;
	
	// 是否删除
	public int isdelete;
	
	// 评价内容
	public String content;
	
	// 车款id
	public String vehiclesId;
	
	// 排序
	public double orderby;
	
	// 车款属性类型(属性值id或文本)
	public String attrName;
	
	// 属性类型id
	public String attrTypeId;
	
	// 属性分类类型
	public String classifyName;
	
	// 备注
	public String info;
	
	// 所属车款属性分类
	public String attrClassifyId;
	
	// 品牌名称
	public String brandName;
	
	// logo图片路径
	public String logoImagePath;
	
	// 品牌简介
	public String introduction;
	
	// 国别
	public String country;
	
	// 首字母
	public String initials;
	
	// 官网
	public String officialWebsite;
	
	// 品牌id
	public String brandId;
	
	// 厂商名称
	public String companyName;
	
	// 汽车主题id
	public String subjectId;
	
	// 主题名称
	public String subjectName;
	
	// 图片描述
	public String desrc;
	
	// 所属厂商id
	public String companyId;
	
	// 车系名称
	public String modelsName;
	
	// 车系级别
	public String level;
	
	// 车身结构
	public String structure;
	
	// 生产方式
	public String production;
	
	// 简介
	public String instroduction;
	
	// 指导价（最高）
	public Double guideTopPrice;
	
	// 指导价（最低）
	public Double guideBotomPrice;
	
	// 二手价格（最低）
	public Double userBottomPrices;
	
	// 二手价格（最高）
	public Double userTopPrices;
	
	// 预览图
	public String imagePath;
	
	// 车系状态（1.新车 2.二手车）
	public String carStatus;
	
	// 所属车系
	public String modelsId;
	
	// 车款名称
	public String carName;
	
	// 车款全名
	public String carAllName;
	
	// 指导价
	public Double guidePrice;
	
	// 开始时间
	public String startTime;
	
	// 结束时间
	public String endTime;
	
	// 车源id
	public String carsourceId;
	
	// 起拍价
	public String startingPrice;
	
	// 每次最低加价价格
	public String increasePrice;
	
	// 一口价
	public String flatlyPrice;

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

	public long getDateTimestamp() {
		return dateTimestamp;
	}

	public void setDateTimestamp(long dateTimestamp) {
		this.dateTimestamp = dateTimestamp;
	}

	public int getIsdelete() {
		return isdelete;
	}

	public void setIsdelete(int isdelete) {
		this.isdelete = isdelete;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getVehiclesId() {
		return vehiclesId;
	}

	public void setVehiclesId(String vehiclesId) {
		this.vehiclesId = vehiclesId;
	}

	public double getOrderby() {
		return orderby;
	}

	public void setOrderby(double orderby) {
		this.orderby = orderby;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public String getAttrTypeId() {
		return attrTypeId;
	}

	public void setAttrTypeId(String attrTypeId) {
		this.attrTypeId = attrTypeId;
	}

	public String getClassifyName() {
		return classifyName;
	}

	public void setClassifyName(String classifyName) {
		this.classifyName = classifyName;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getAttrClassifyId() {
		return attrClassifyId;
	}

	public void setAttrClassifyId(String attrClassifyId) {
		this.attrClassifyId = attrClassifyId;
	}

	public String getBrandName() {
		return brandName;
	}

	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getLogoImagePath() {
		return logoImagePath;
	}

	public void setLogoImagePath(String logoImagePath) {
		this.logoImagePath = logoImagePath;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getOfficialWebsite() {
		return officialWebsite;
	}

	public void setOfficialWebsite(String officialWebsite) {
		this.officialWebsite = officialWebsite;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getDesrc() {
		return desrc;
	}

	public void setDesrc(String desrc) {
		this.desrc = desrc;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getModelsName() {
		return modelsName;
	}

	public void setModelsName(String modelsName) {
		this.modelsName = modelsName;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStructure() {
		return structure;
	}

	public void setStructure(String structure) {
		this.structure = structure;
	}

	public String getProduction() {
		return production;
	}

	public void setProduction(String production) {
		this.production = production;
	}

	public String getInstroduction() {
		return instroduction;
	}

	public void setInstroduction(String instroduction) {
		this.instroduction = instroduction;
	}

	public Double getGuideTopPrice() {
		return guideTopPrice;
	}

	public void setGuideTopPrice(Double guideTopPrice) {
		this.guideTopPrice = guideTopPrice;
	}

	public Double getGuideBotomPrice() {
		return guideBotomPrice;
	}

	public void setGuideBotomPrice(Double guideBotomPrice) {
		this.guideBotomPrice = guideBotomPrice;
	}

	public Double getUserBottomPrices() {
		return userBottomPrices;
	}

	public void setUserBottomPrices(Double userBottomPrices) {
		this.userBottomPrices = userBottomPrices;
	}

	public Double getUserTopPrices() {
		return userTopPrices;
	}

	public void setUserTopPrices(Double userTopPrices) {
		this.userTopPrices = userTopPrices;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getCarStatus() {
		return carStatus;
	}

	public void setCarStatus(String carStatus) {
		this.carStatus = carStatus;
	}

	public String getModelsId() {
		return modelsId;
	}

	public void setModelsId(String modelsId) {
		this.modelsId = modelsId;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public String getCarAllName() {
		return carAllName;
	}

	public void setCarAllName(String carAllName) {
		this.carAllName = carAllName;
	}

	public Double getGuidePrice() {
		return guidePrice;
	}

	public void setGuidePrice(Double guidePrice) {
		this.guidePrice = guidePrice;
	}
	
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getCarsourceId() {
		return carsourceId;
	}

	public void setCarsourceId(String carsourceId) {
		this.carsourceId = carsourceId;
	}

	public String getStartingPrice() {
		return startingPrice;
	}

	public void setStartingPrice(String startingPrice) {
		this.startingPrice = startingPrice;
	}

	public String getIncreasePrice() {
		return increasePrice;
	}

	public void setIncreasePrice(String increasePrice) {
		this.increasePrice = increasePrice;
	}

	public String getFlatlyPrice() {
		return flatlyPrice;
	}

	public void setFlatlyPrice(String flatlyPrice) {
		this.flatlyPrice = flatlyPrice;
	}

	
}
