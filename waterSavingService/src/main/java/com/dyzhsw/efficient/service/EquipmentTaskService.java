package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.EquipmentTask;
/**
 * 定时任务相关service
 * create by yr
 */
public interface EquipmentTaskService {

	List<Map<String,Object>> selectAll(String name,String officeId,String type);

	Map<String,Object> selectById(String id);

	void insertInfo(EquipmentTask task);

	int deleteById(String id[]);

	int updateById(EquipmentTask task);
	
	List<EquipmentTask> selectInfo(EquipmentTask equipmentTask);

	EquipmentTask selectByIdApp(String id);

	List<Map<String, Object>> selectAllTask();
	
	List<EquipmentTask> getEquipmentTaskExport (EquipmentTask equipmentTask);
	
	List<Map<String,Object>> selectAllExport(EquipmentTask equipmentTask);

	List<Map<String,String>> selectByEquId(String equId);

}                               