package com.dyzhsw.efficient.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * 阀控器状态信息实体类
 */
public class FkqValveInfoVo implements Serializable {


    private String id;

    private String equipmentId;
    
    private String name;

    private String telemetryStationAddr;

    private String centerStationAddr;

    private Integer isonline;

    private Integer valve1Status;

    private Integer valve2Status;

    private Integer water1Status;

    private Integer water2Status;

    private Integer valveSignalQuality;

    private Double valveVoltage;

    private Date updateTime;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelemetryStationAddr() {
        return telemetryStationAddr;
    }

    public void setTelemetryStationAddr(String telemetryStationAddr) {
        this.telemetryStationAddr = telemetryStationAddr;
    }

    public String getCenterStationAddr() {
        return centerStationAddr;
    }

    public void setCenterStationAddr(String centerStationAddr) {
        this.centerStationAddr = centerStationAddr;
    }

    public Integer getIsonline() {
        return isonline;
    }

    public void setIsonline(Integer isonline) {
        this.isonline = isonline;
    }

    public Integer getValve1Status() {
        return valve1Status;
    }

    public void setValve1Status(Integer valve1Status) {
        this.valve1Status = valve1Status;
    }

    public Integer getValve2Status() {
        return valve2Status;
    }

    public void setValve2Status(Integer valve2Status) {
        this.valve2Status = valve2Status;
    }

    public Integer getWater1Status() {
        return water1Status;
    }

    public void setWater1Status(Integer water1Status) {
        this.water1Status = water1Status;
    }

    public Integer getWater2Status() {
        return water2Status;
    }

    public void setWater2Status(Integer water2Status) {
        this.water2Status = water2Status;
    }

    public Integer getValveSignalQuality() {
        return valveSignalQuality;
    }

    public void setValveSignalQuality(Integer valveSignalQuality) {
        this.valveSignalQuality = valveSignalQuality;
    }

    public Double getValveVoltage() {
        return valveVoltage;
    }

    public void setValveVoltage(Double valveVoltage) {
        this.valveVoltage = valveVoltage;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }



}
