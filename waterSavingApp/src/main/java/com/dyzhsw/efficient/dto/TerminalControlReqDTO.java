package com.dyzhsw.efficient.dto;

/**
 * @Author: pjx
 * @Date: 2018/12/12 14:20
 * @Version 1.0
 */
public class TerminalControlReqDTO {
//    /**
//     * 终端子设备（id编号）
//     */
//    private String subterminalId;
    /**
     * 开关状态
     */
    private String switchState ;
    /**
     * 终端设备编号
     */
    private  String terminalId;
//    /**
//     * 二类设备（工作状态）
//     */
//    private  String workTime;



    public String getSwitchState() {
        return switchState;
    }

    public void setSwitchState(String switchState) {
        this.switchState = switchState;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }


}
