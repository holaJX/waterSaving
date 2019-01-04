package com.dyzhsw.efficient.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;


/**
 * 设备绑定信息实体类
 */
@ApiModel(value = "设备绑定关系")
public class EquipmentRelation implements Serializable {


    @ApiModelProperty(value = "关联关系id")
    private String id;

    @ApiModelProperty(value = "过滤器id")
    private String percolatorId;

    @ApiModelProperty(value = "流量计id")
    private String flowmeterId;

    @ApiModelProperty(value = "压力计id")
    private String piezometerId;

    @ApiModelProperty(value = "阀控器id")
    private String valveControllerId;

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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPercolatorId() {
        return percolatorId;
    }

    public void setPercolatorId(String percolatorId) {
        this.percolatorId = percolatorId;
    }

    public String getFlowmeterId() {
        return flowmeterId;
    }

    public void setFlowmeterId(String flowmeterId) {
        this.flowmeterId = flowmeterId;
    }

    public String getPiezometerId() {
        return piezometerId;
    }

    public void setPiezometerId(String piezometerId) {
        this.piezometerId = piezometerId;
    }

    public String getValveControllerId() {
        return valveControllerId;
    }

    public void setValveControllerId(String valveControllerId) {
        this.valveControllerId = valveControllerId;
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


}
