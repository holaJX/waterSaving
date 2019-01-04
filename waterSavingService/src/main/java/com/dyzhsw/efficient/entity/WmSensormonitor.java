package com.dyzhsw.efficient.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

@ApiModel(value = "水肥机压力传感器数据")
public class WmSensormonitor {


    @ApiModelProperty(value = "数据id")
    private String id;

    @ApiModelProperty(value = "消息id")
    private String msgId;

    @ApiModelProperty(value = "设备编号")
    private String terminalId;

    @ApiModelProperty(value = "消息流水号(服务端)")
    private Integer serialNoServer;

    @ApiModelProperty(value = "消息流水号(终端)")
    private Integer serialNoTerminal;

    @ApiModelProperty(value = "采集时间")
    private Date collectDate;

    @ApiModelProperty(value = "设备终端id")
    private String name;

    @ApiModelProperty(value = "数值")
    private BigDecimal amount;

    @ApiModelProperty(value = "当前状态0：正常1：未采集到新数据2：采集到的数据异常")
    private Integer state;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public Integer getSerialNoServer() {
        return serialNoServer;
    }

    public void setSerialNoServer(Integer serialNoServer) {
        this.serialNoServer = serialNoServer;
    }

    public Integer getSerialNoTerminal() {
        return serialNoTerminal;
    }

    public void setSerialNoTerminal(Integer serialNoTerminal) {
        this.serialNoTerminal = serialNoTerminal;
    }

    public Date getCollectDate() {
        return collectDate;
    }

    public void setCollectDate(Date collectDate) {
        this.collectDate = collectDate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

}
