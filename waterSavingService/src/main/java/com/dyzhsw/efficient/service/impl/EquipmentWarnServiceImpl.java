package com.dyzhsw.efficient.service.impl;


import com.dyzhsw.efficient.dao.EquipmentWarnDao;
import com.dyzhsw.efficient.entity.EquipmentWarn;
import com.dyzhsw.efficient.service.EquipmentWarnService;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentWarnServiceImpl implements EquipmentWarnService {
	
	@Autowired
	EquipmentWarnDao equipmentWarnDao;
	
	
	@Override
	public List<EquipmentWarn> selectAll(EquipmentWarn equipmentWarn) {
		return equipmentWarnDao.selectAll(equipmentWarn);
	}


	@Override
	public List<EquipmentWarn> getEquipmentWarnExport(Integer equType, Integer warnType, String warnInfo,String remarks,String startTime,String endTime) {
		return equipmentWarnDao.getEquipmentWarnExport(equType, warnType, warnInfo,remarks,startTime,endTime);
	}


}
