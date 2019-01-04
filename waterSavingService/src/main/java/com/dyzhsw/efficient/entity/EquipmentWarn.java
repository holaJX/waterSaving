package com.dyzhsw.efficient.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * 设备报警信息实体类
 */
@ApiModel(value = "设备报警信息")
public class EquipmentWarn implements Serializable {

    @ApiModelProperty(value = "报警信息id")
    private String id;

    @ApiModelProperty(value = "报警类型")
    private String warnType;

    @ApiModelProperty(value = "报警类型名称")
    private String warnTypeName;

    @ApiModelProperty(value = "报警内容")
    private String warnInfo;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "删除标记(0:未删除 1:已删除)")
    private Integer delFlag;

    @ApiModelProperty(value = "设备类型")
    private Integer equipmentType;

    @ApiModelProperty(value = "抄录日期")
    private Date copyDate;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "设备信息")
    private EquipmentInfo equipmentInfo;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWarnType() {
		return warnType;
	}

	public void setWarnType(String warnType) {
		this.warnType = warnType;
	}

    public String getWarnTypeName() {
        return warnTypeName;
    }

    public void setWarnTypeName(String warnTypeName) {
        this.warnTypeName = warnTypeName;
    }

	public String getWarnInfo() {
        return warnInfo;
    }

    public void setWarnInfo(String warnInfo) {
        this.warnInfo = warnInfo;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
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

    public Integer getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Integer delFlag) {
        this.delFlag = delFlag;
    }

    public Integer getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Date getCopyDate() {
        return copyDate;
    }

    public void setCopyDate(Date copyDate) {
        this.copyDate = copyDate;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public EquipmentInfo getEquipmentInfo() {
		return equipmentInfo;
	}

	public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
		this.equipmentInfo = equipmentInfo;
	}

    
}
