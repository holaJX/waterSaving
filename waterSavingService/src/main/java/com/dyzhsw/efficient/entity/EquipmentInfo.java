package com.dyzhsw.efficient.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 设备信息实体类
 */
@ApiModel(value = "设备信息")
public class EquipmentInfo implements Serializable {

    @ApiModelProperty(value = "设备id")
    private String id;

    @ApiModelProperty(value = "父id")
    private String parentId;

    @ApiModelProperty(value = "用户id")
    private String userId;

    @ApiModelProperty(value = "所属机构id")
    private String officeId;

    @ApiModelProperty(value = "所属机构名称")
    private String officeName;

    @ApiModelProperty(value = "所属机构id数组")
    private List<String> officeIdList;

    @ApiModelProperty(value = "区域id")
    private String areaId;

    @ApiModelProperty(value = "设备名称")
    private String name;

    @ApiModelProperty(value = "设备编号")
    private String equipmentNo;

    @ApiModelProperty(value = "设备密码")
    private String password;

    @ApiModelProperty(value = "在线状态(0-离线1-在线)")
    private Integer isonline;

    @ApiModelProperty(value = "经度")
    private BigDecimal pointlng;

    @ApiModelProperty(value = "纬度")
    private BigDecimal pointlat;

    @ApiModelProperty(value = "设备类型编号")
    private Integer equipmentType;

    @ApiModelProperty(value = "设备类型名称")
    private String equipmentTypeName;

    @ApiModelProperty(value = "图标地址")
    private String iconurl;

    @ApiModelProperty(value = "排序")
    private BigDecimal sort;

    @ApiModelProperty(value = "设备分组id")
    private String groupingId;

    @ApiModelProperty(value = "设备分组名称")
    private String groupingName;

    @ApiModelProperty(value = "创建者")
    private String createBy;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新者")
    private String updateBy;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "备注信息")
    private String remarks;

    @ApiModelProperty(value = "删除标记（0：未删除 1：已删除）")
    private Integer delFlag;

    private String signLabel;

    private Integer equipClassify;

    @ApiModelProperty(value = "上限值")
    private String maxValue;

    @ApiModelProperty(value = "下限值")
    private String minValue;

    @ApiModelProperty(value = "（废弃）阀值报警开关状态(0:关闭 1:开启)")
    private Integer thresholdAlarmState;

    @ApiModelProperty(value = "（废弃）离线报警开关状态(0:关闭 1:开启)")
    private Integer offlineAlarmState;

    @ApiModelProperty(value = "阀门1开关状态(0表示未知状态，1表示关阀，2表示开阀)")
    private Integer valve1Status;

    @ApiModelProperty(value = "阀门2开关状态(0表示未知状态，1表示关阀，2表示开阀)")
    private Integer valve2Status;

    @ApiModelProperty(value = "施肥泵状态(0表示关，1表示开，255表示未知)")
    private Integer wmState;

    @ApiModelProperty(value = "施肥机施肥速度(单位L/h)")
    private BigDecimal instantaneousflow;

    @ApiModelProperty(value = "绑定设备的id")
    private String relationEquId;

    @ApiModelProperty(value = "绑定设备的名称")
    private String relationEquName;
    
    @ApiModelProperty(value = "绑定的设备")
    private EquipmentInfo relationEqu;

    @ApiModelProperty(value = "绑定的设备列表")
    private List<EquipmentInfo> relationEquList;

    @ApiModelProperty(value = "设备归属园区")
    private String regionName;
    
    @ApiModelProperty(value = "设备归属片区")
    private String zoneName;
    
    @ApiModelProperty(value = "网关地址")
    private String telemetryStationAddr;


    
	public String getTelemetryStationAddr() {
		return telemetryStationAddr;
	}

	public void setTelemetryStationAddr(String telemetryStationAddr) {
		this.telemetryStationAddr = telemetryStationAddr;
	}

	public String getId() {
		return id;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId;
    }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

    public String getAreaId() {
        return areaId;
    }

    public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEquipmentNo() {
        return equipmentNo;
    }

    public void setEquipmentNo(String equipmentNo) {
        this.equipmentNo = equipmentNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getIsonline() {
        return isonline;
    }

    public void setIsonline(Integer isonline) {
        this.isonline = isonline;
    }

    public BigDecimal getPointlng() {
        return pointlng;
    }

    public void setPointlng(BigDecimal pointlng) {
        this.pointlng = pointlng;
    }

    public BigDecimal getPointlat() {
        return pointlat;
    }

    public void setPointlat(BigDecimal pointlat) {
        this.pointlat = pointlat;
    }

    public Integer getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(Integer equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getEquipmentTypeName() {
        return equipmentTypeName;
    }

    public void setEquipmentTypeName(String equipmentTypeName) {
        this.equipmentTypeName = equipmentTypeName;
    }

    public String getIconurl() {
        return iconurl;
    }

    public void setIconurl(String iconurl) {
        this.iconurl = iconurl;
    }

    public BigDecimal getSort() {
        return sort;
    }

    public void setSort(BigDecimal sort) {
        this.sort = sort;
    }

    public String getGroupingId() {
        return groupingId;
    }

    public void setGroupingId(String groupingId) {
        this.groupingId = groupingId;
    }

    public String getGroupingName() {
        return groupingName;
    }

    public void setGroupingName(String groupingName) {
        this.groupingName = groupingName;
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

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public String getSignLabel() {
        return signLabel;
    }

    public void setSignLabel(String signLabel) {
        this.signLabel = signLabel;
    }

    public Integer getEquipClassify() {
        return equipClassify;
    }

    public void setEquipClassify(Integer equipClassify) {
        this.equipClassify = equipClassify;
    }

    public String getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(String maxValue) {
        this.maxValue = maxValue;
    }

    public String getMinValue() {
        return minValue;
    }

    public void setMinValue(String minValue) {
        this.minValue = minValue;
    }

    public Integer getThresholdAlarmState() {
        return thresholdAlarmState;
    }

    public void setThresholdAlarmState(Integer thresholdAlarmState) {
        this.thresholdAlarmState = thresholdAlarmState;
    }

    public Integer getOfflineAlarmState() {
        return offlineAlarmState;
    }

    public void setOfflineAlarmState(Integer offlineAlarmState) {
        this.offlineAlarmState = offlineAlarmState;
    }

    public Integer getValve1Status() {
        return valve1Status;
    }

    public void setValve1Status(Integer valve1Status) {
        this.valve1Status = valve1Status;
    }

    public Integer getValve2Status() {
        return valve2Status;
    }

    public void setValve2Status(Integer valve2Status) {
        this.valve2Status = valve2Status;
    }

    public Integer getWmState() {
        return wmState;
    }

    public void setWmState(Integer wmState) {
        this.wmState = wmState;
    }

    public BigDecimal getInstantaneousflow() {
        return instantaneousflow;
    }

    public void setInstantaneousflow(BigDecimal instantaneousflow) {
        this.instantaneousflow = instantaneousflow;
    }

    public String getRelationEquId() {
        return relationEquId;
    }

    public void setRelationEquId(String relationEquId) {
        this.relationEquId = relationEquId;
    }

    public String getRelationEquName() {
        return relationEquName;
    }

    public void setRelationEquName(String relationEquName) {
        this.relationEquName = relationEquName;
    }

    public EquipmentInfo getRelationEqu() {
        return relationEqu;
    }

    public void setRelationEqu(EquipmentInfo relationEqu) {
        this.relationEqu = relationEqu;
    }

    public List<EquipmentInfo> getRelationEquList() {
        return relationEquList;
    }

    public void setRelationEquList(List<EquipmentInfo> relationEquList) {
        this.relationEquList = relationEquList;
    }

}
