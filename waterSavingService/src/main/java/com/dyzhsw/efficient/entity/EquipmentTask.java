package com.dyzhsw.efficient.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import io.swagger.annotations.ApiModel;

/**
 * 定时任务
 * @author 
 *
 */
@ApiModel(value = "定时任务model")
public class EquipmentTask {
	
	
    private String id;
	
    private String name;//名称
	
    private String type;//类型
	
    private String officeId;//片区id
	
    private String createBy;//创建人id
	
    private String returnTime; //循环周期：1:星期1,2:星期2,3:星期3,4:星期4,5:星期5,6:星期6,0:星期7
	
    private Integer status;//启用状态（0：启用 1；暂停）

    private Date createTime;//创建时间
  
    private String remarks;//备注
   
    private String openmin;//定时开始分钟
   
    private String openhour;//定时开始小时
    
    private String closemin;//定时开始分钟
   
    private String closehour;//定时结束小时
    
    private String delFlag;
    
    private Date updateTime;//创建时间
    
    private String fertilizingTime;//自动施肥设定时间
    
    private String fertilizingAmount; //施肥量
    
    private String channelNo; //通道
    
    private Integer routeOne; //通道1
    
    private Integer routeTwo; //通道2
    
    private  TaskEquList task ;

    private  List<TaskEquList> taskequlist ;
    
    private  String equList ;

    
	public String getEquList() {
		return equList;
	}

	public void setEquList(String equList) {
		this.equList = equList;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}


	public String getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getOpenmin() {
		return openmin;
	}

	public void setOpenmin(String openmin) {
		this.openmin = openmin;
	}

	public String getOpenhour() {
		return openhour;
	}

	public void setOpenhour(String openhour) {
		this.openhour = openhour;
	}

	public String getClosemin() {
		return closemin;
	}

	public void setClosemin(String closemin) {
		this.closemin = closemin;
	}

	public String getClosehour() {
		return closehour;
	}

	public void setClosehour(String closehour) {
		this.closehour = closehour;
	}


	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getOfficeId() {
		return officeId;
	}

	public void setOfficeId(String officeId) {
		this.officeId = officeId;
	}

	public TaskEquList getTask() {
		return task;
	}

	public void setTask(TaskEquList task) {
		this.task = task;
	}

	public Integer getRouteOne() {
		return routeOne;
	}

	public void setRouteOne(Integer routeOne) {
		this.routeOne = routeOne;
	}

	public Integer getRouteTwo() {
		return routeTwo;
	}

	public void setRouteTwo(Integer routeTwo) {
		this.routeTwo = routeTwo;
	}

	public List<TaskEquList> getTaskequlist() {
		return taskequlist;
	}

	public void setTaskequlist(List<TaskEquList> taskequlist) {
		this.taskequlist = taskequlist;
	}

	public String getFertilizingTime() {
		return fertilizingTime;
	}

	public void setFertilizingTime(String fertilizingTime) {
		this.fertilizingTime = fertilizingTime;
	}

	public String getFertilizingAmount() {
		return fertilizingAmount;
	}

	public void setFertilizingAmount(String fertilizingAmount) {
		this.fertilizingAmount = fertilizingAmount;
	}

	

	


}