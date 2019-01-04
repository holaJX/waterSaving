package com.dyzhsw.efficient.entity;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.Date;

@ApiModel(value = "流量计指定日期瞬时流量")
public class GsmMeterInstantaneousflowforday {


    @ApiModelProperty(value = "数据id")
    private String id;

    @ApiModelProperty(value = "设备编号")
    private String address;

    @ApiModelProperty(value = "抄录时间")
    private Date copyDate;

    @ApiModelProperty(value = "报警数据")
    private String warnData;

    @ApiModelProperty(value = "瞬时流量")
    private String instantaneousflow;

    @ApiModelProperty(value = "中断时间")
    private Date terminalDatetime;

    @ApiModelProperty(value = "终端电压")
    private String terminalVoltage;

    @ApiModelProperty(value = "信号强度")
    private String csq;

    @ApiModelProperty(value = "版本信息")
    private String version;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "序号")
    private Integer num;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getCopyDate() {
        return copyDate;
    }

    public void setCopyDate(Date copyDate) {
        this.copyDate = copyDate;
    }

    public String getWarnData() {
        return warnData;
    }

    public void setWarnData(String warnData) {
        this.warnData = warnData;
    }

    public String getInstantaneousflow() {
        return instantaneousflow;
    }

    public void setInstantaneousflow(String instantaneousflow) {
        this.instantaneousflow = instantaneousflow;
    }

    public Date getTerminalDatetime() {
        return terminalDatetime;
    }

    public void setTerminalDatetime(Date terminalDatetime) {
        this.terminalDatetime = terminalDatetime;
    }

    public String getTerminalVoltage() {
        return terminalVoltage;
    }

    public void setTerminalVoltage(String terminalVoltage) {
        this.terminalVoltage = terminalVoltage;
    }

    public String getCsq() {
        return csq;
    }

    public void setCsq(String csq) {
        this.csq = csq;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

}
