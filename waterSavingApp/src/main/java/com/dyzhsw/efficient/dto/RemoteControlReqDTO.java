package com.dyzhsw.efficient.dto;

/**
 * @Author: pjx
 * @Date: 2018/12/12 14:22
 * @Version 1.0
 */
public class RemoteControlReqDTO {
    private String other;
    /**
     * 控制参数
     */
    private String paramStr ;

    /**
     * 终端设备编号
     */

    private  String terminalId;

    public String getOther() {
        return other;
    }

    public void setOther(String other) {
        this.other = other;
    }

    public String getParamStr() {
        return paramStr;
    }

    public void setParamStr(String paramStr) {
        this.paramStr = paramStr;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


}
