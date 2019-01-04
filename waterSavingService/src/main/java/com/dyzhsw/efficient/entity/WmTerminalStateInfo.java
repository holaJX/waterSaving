package com.dyzhsw.efficient.entity;

import java.util.Date;

public class WmTerminalStateInfo {


    private String id;

    private String msgId;

    private String terminalId;

    private Integer serialNoServer;

    private Integer serialNoTerminal;

    private Date collectDate;

    private String subterminalNo;

    private String subterminalName;

    private Integer state;

    private String position;

    private Integer devicetype;

    private Integer runState;

    private Date createTime;

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

    public String getSubterminalNo() {
        return subterminalNo;
    }

    public void setSubterminalNo(String subterminalNo) {
        this.subterminalNo = subterminalNo;
    }

    public String getSubterminalName() {
        return subterminalName;
    }

    public void setSubterminalName(String subterminalName) {
        this.subterminalName = subterminalName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getDevicetype() {
        return devicetype;
    }

    public void setDevicetype(Integer devicetype) {
        this.devicetype = devicetype;
    }

    public Integer getRunState() {
        return runState;
    }

    public void setRunState(Integer runState) {
        this.runState = runState;
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
