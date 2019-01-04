package com.dyzhsw.efficient.entity;

import java.util.Date;

/**
 * 任务设备表
 * @author 
 *
 */
public class TaskEquList {
    private String id;

    private String equId;
    
    private String taskId;

    private Date createTime;
    
    private String remarks;
    
    private String equipmentNo;
    
    private String equipmentName;


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

	public String getTaskId() {
		return taskId;
	}

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}


	public String getEquId() {
		return equId;
	}

	public void setEquId(String equId) {
		this.equId = equId;
	}

	public String getEquipmentNo() {
		return equipmentNo;
	}

	public void setEquipmentNo(String equipmentNo) {
		this.equipmentNo = equipmentNo;
	}

	public String getEquipmentName() {
		return equipmentName;
	}

	public void setEquipmentName(String equipmentName) {
		this.equipmentName = equipmentName;
	}
	
	
}