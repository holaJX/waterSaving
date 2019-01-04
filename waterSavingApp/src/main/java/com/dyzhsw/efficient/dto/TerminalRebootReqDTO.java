package com.dyzhsw.efficient.dto;

/**
 * @Author: pjx
 * @Date: 2018/12/12 14:25
 * @Version 1.0
 */
public class TerminalRebootReqDTO {
    /**
     * 重启时间
     */
    private  String rebootTime;
    /**
     * 终端id
     */
    private  String terminalId;

    public String getRebootTime() {
        return rebootTime;
    }

    public void setRebootTime(String rebootTime) {
        this.rebootTime = rebootTime;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}
