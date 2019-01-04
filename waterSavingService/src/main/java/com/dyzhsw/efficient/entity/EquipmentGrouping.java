package com.dyzhsw.efficient.entity;


import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 设备分组信息实体类
 */
@ApiModel(value = "分组信息")
public class EquipmentGrouping implements Serializable {

    @ApiModelProperty(value = "分组id")
    private String id;

    @ApiModelProperty(value = "分组名称")
    private String name;

    @ApiModelProperty(value = "分组类型")
    private Integer groupType;

    @ApiModelProperty(value = "是否启用(0:启用 1:不启用)")
    private Integer isenabled;

    @ApiModelProperty(value = "排序")
    private Integer sort;

    @ApiModelProperty(value = "所属机构id")
    private String officeId;

    @ApiModelProperty(value = "所属机构名称")
    private String officeName;

    @ApiModelProperty(value = "所属用户id")
    private String userId;

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

    @ApiModelProperty(value = "删除标记(0:未删除 1:已删除)")
    private Integer delFlag;

    @ApiModelProperty(value = "分组内的设备列表")
    private PageInfo<EquipmentInfo> pageInfo;

    @ApiModelProperty(value = "父归属")
    private String parentOfficeName;

    @ApiModelProperty(value = "所属机构id数组")
    private List<String> officeIdList;



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

    public Integer getGroupType() {
        return groupType;
    }

    public void setGroupType(Integer groupType) {
        this.groupType = groupType;
    }

    public Integer getIsenabled() {
        return isenabled;
    }

    public void setIsenabled(Integer isenabled) {
        this.isenabled = isenabled;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public PageInfo<EquipmentInfo> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<EquipmentInfo> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getParentOfficeName() {
        return parentOfficeName;
    }

    public void setParentOfficeName(String parentOfficeName) {
        this.parentOfficeName = parentOfficeName;
    }

    public List<String> getOfficeIdList() {
        return officeIdList;
    }

    public void setOfficeIdList(List<String> officeIdList) {
        this.officeIdList = officeIdList;
    }

}
