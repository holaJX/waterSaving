package com.dyzhsw.efficient.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 过滤器信息实体类
 */
public class FilterInfo implements Serializable {


    private String id;

    private Date terminalDatetime;

    private String terminalVoltage;

    private String csq;

    private String version;

    private String address;

    private String channelId;

    private Date createTime;

    private Date updateTime;

    private String creator;

    private String isonline;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
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

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getIsonline() {
        return isonline;
    }

    public void setIsonline(String isonline) {
        this.isonline = isonline;
    }

}
