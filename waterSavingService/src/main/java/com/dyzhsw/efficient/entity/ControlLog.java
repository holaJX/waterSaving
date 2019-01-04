package com.dyzhsw.efficient.entity;

import java.util.Date;

/**
 * 施肥机
 * @author 
 *
 */
public class ControlLog {
    private String id;

    private String equipmentId;
    
    private String taskId;
    
    private String userId;
    
    private int controlType;
    
    private String content;
    
    private int state;
    
    private String createBy;    

    private Date createDate;
    
    private String remarks;
    
    private String delFlag;
    
    private String name;
    
    private String equipmentName;
    
    private String openhour;
    
    private String openmin;
    
    private String closehour;
    
    private String closemin;
    
    private String taskType;
    
    private String equList;
    
    
    

    	
	public String getEquList() {
		return equList;
	}

	public void setEquList(String equList) {
		this.equList = equList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}

	public String getOpenhour() {
		return openhour;
	}

	public void setOpenhour(String openhour) {
		this.openhour = openhour;
	}

	public String getOpenmin() {
		return openmin;
	}

	public void setOpenmin(String openmin) {
		this.openmin = openmin;
	}

	public String getClosehour() {
		return closehour;
	}

	public void setClosehour(String closehour) {
		this.closehour = closehour;
	}

	public String getClosemin() {
		return closemin;
	}

	public void setClosemin(String closemin) {
		this.closemin = closemin;
	}

	public String getTaskType() {
		return taskType;
	}

	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(String equipmentId) {
		this.equipmentId = equipmentId;
	}

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getControlType() {
		return controlType;
	}

	public void setControlType(int controlType) {
		this.controlType = controlType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}



}