package com.dyzhsw.efficient.service.impl;


import com.dyzhsw.efficient.dao.EquipmentRelationDao;
import com.dyzhsw.efficient.entity.EquipmentRelation;
import com.dyzhsw.efficient.service.EquipmentRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentRelationServiceImpl implements EquipmentRelationService {
	@Autowired
	private EquipmentRelationDao equipmentRelationDao;

	@Override
	public EquipmentRelation selectInfoByPercolatorId(String percolatorId) {
		return equipmentRelationDao.selectInfoByPercolatorId(percolatorId);
	}

	@Override
	public EquipmentRelation selectInfoByValveControllerId(String valveControllerId) {
		return equipmentRelationDao.selectInfoByValveControllerId(valveControllerId);
	}

	@Override
	public EquipmentRelation selectInfoByFlowmeterId(String flowmeterId) {
		return equipmentRelationDao.selectInfoByFlowmeterId(flowmeterId);
	}

	@Override
	public EquipmentRelation selectInfoByPiezometerId(String piezometerId) {
		return equipmentRelationDao.selectInfoByPiezometerId(piezometerId);
	}


    @Override
    public void addEquRelationInfo(EquipmentRelation equipmentRelation) {

        int i = equipmentRelationDao.addEquRelationInfo(equipmentRelation);
    }

    @Override
    public EquipmentRelation selectByPercolatorId(String percolatorId) {
        return equipmentRelationDao.selectInfoByPercolatorId(percolatorId);
    }

    @Override
    public void updateEquRelationInfo(EquipmentRelation equipmentRelation) {

        int i = equipmentRelationDao.updateEquRelationInfo(equipmentRelation);
    }

    @Override
    public EquipmentRelation selectByValveControllerId(String valveControllerId) {
        return equipmentRelationDao.selectInfoByValveControllerId(valveControllerId);
    }

    @Override
	public void deletedByFlowmeterId(String flowmeterId) {
		equipmentRelationDao.deletedByFlowmeterId(flowmeterId);
	}

	@Override
	public void deletedByValveControllerId(String valveControllerId) {
		equipmentRelationDao.deletedByValveControllerId(valveControllerId);
	}

}
