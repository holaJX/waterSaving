package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.EquipmentRelation;

public interface EquipmentRelationDao {


    EquipmentRelation selectInfoByFlowmeterId(String flowmeterId);

    EquipmentRelation selectInfoByPiezometerId(String piezometerId);

    EquipmentRelation selectInfoByPercolatorId(String percolatorId);


    EquipmentRelation selectInfoByValveControllerId(String valveControllerId);

    int addEquRelationInfo(EquipmentRelation equipmentRelation);

    int updateEquRelationInfo(EquipmentRelation equipmentRelation);

    void deletedByPercolatorId(String percolatorId);

    void deletedByValveControllerId(String valveControllerId);

    void deletedByFlowmeterId(String flowmeterId);

    void deletedByPiezometerId(String piezometerId);


}
