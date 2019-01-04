package com.dyzhsw.efficient.dto;

/**
 * @Author: pjx
 * @Date: 2018/12/12 15:17
 * @Version 1.0
 */
public class ControlReqDTO {
    /**
     * 阀控器设备id
     */
    private String equipmentId;
    /**
     * 一路执行开关状态（2为开,1为关）
     */
    private String control1Status;
    /**
     * 二路执行开关状态（2为开,1为关）
     */
    private String Control2Status;

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getControl1Status() {
        return control1Status;
    }

    public void setControl1Status(String control1Status) {
        this.control1Status = control1Status;
    }

    public String getControl2Status() {
        return Control2Status;
    }

    public void setControl2Status(String control2Status) {
        Control2Status = control2Status;
    }
}
