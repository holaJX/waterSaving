package com.dyzhsw.efficient.service;

import java.util.List;

import com.dyzhsw.efficient.entity.EquipmentWarn;

/**
 * 设备报警信息相关service
 */
public interface EquipmentWarnService {

	 List<EquipmentWarn> selectAll(EquipmentWarn equipmentWarn);
	 List<EquipmentWarn> getEquipmentWarnExport(Integer equType, Integer warnType, String warnInfo,String remarks,String startTime,String endTime);

}
