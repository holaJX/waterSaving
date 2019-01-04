package com.dyzhsw.efficient.service;

import com.dyzhsw.efficient.entity.EquipmentRelation;

import com.dyzhsw.efficient.entity.EquipmentRelation;

/**
 * 设备绑定关系信息相关Service
 */
public interface EquipmentRelationService {
	
	EquipmentRelation selectInfoByFlowmeterId(String flowmeterId);

    EquipmentRelation selectInfoByPiezometerId(String piezometerId);
    
	EquipmentRelation selectInfoByPercolatorId(String percolatorId);

    EquipmentRelation selectInfoByValveControllerId(String valveControllerId);

    void addEquRelationInfo(EquipmentRelation equipmentRelation);

    EquipmentRelation selectByPercolatorId(String percolatorId);

    void updateEquRelationInfo(EquipmentRelation equipmentRelation);

    EquipmentRelation selectByValveControllerId(String valveControllerId);

    void deletedByFlowmeterId(String flowmeterId);

    void deletedByValveControllerId(String valveControllerId);

}
