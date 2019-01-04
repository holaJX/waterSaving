package com.dyzhsw.efficient.dto;

import com.dyzhsw.efficient.entity.EquipmentInfo;

/**
 * @Author: pjx
 * @Date: 2018/12/13 10:24
 * @Version 1.0
 */
public class AddEquipmentInfoDTO {

    private EquipmentInfo equipmentInfo;
    private  String currentUserId;
    private  String piezometerId;
    private  String flowmeterId;
    private  String valveCtrlId;

    public EquipmentInfo getEquipmentInfo() {
        return equipmentInfo;
    }

    public void setEquipmentInfo(EquipmentInfo equipmentInfo) {
        this.equipmentInfo = equipmentInfo;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }

    public void setCurrentUserId(String currentUserId) {
        this.currentUserId = currentUserId;
    }

    public String getPiezometerId() {
        return piezometerId;
    }

    public void setPiezometerId(String piezometerId) {
        this.piezometerId = piezometerId;
    }

    public String getFlowmeterId() {
        return flowmeterId;
    }

    public void setFlowmeterId(String flowmeterId) {
        this.flowmeterId = flowmeterId;
    }

    public String getValveCtrlId() {
        return valveCtrlId;
    }

    public void setValveCtrlId(String valveCtrlId) {
        this.valveCtrlId = valveCtrlId;
    }
}
