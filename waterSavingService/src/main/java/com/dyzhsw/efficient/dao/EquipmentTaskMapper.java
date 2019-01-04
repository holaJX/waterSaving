package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.EquipmentTask;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface EquipmentTaskMapper {
	//查找全部
    List<Map<String,Object>> selectAll(Map<String,Object>map);
	//根据id查找
    Map<String,Object> selectById(String id);
	//增加水表
	int insertInfo(EquipmentTask pipeline);
	//删除水表
	int deleteById(String[]id);
	//修改水表
	int updateById(EquipmentTask pipeline);
	
	//查找全部
    List<EquipmentTask> selectInfo(EquipmentTask equipmentTask);

	EquipmentTask selectByIdApp(String id);
	
	List<Map<String, Object>> selectAllTask();
	
	List<EquipmentTask> getEquipmentTaskExport (EquipmentTask equipmentTask);
	
	List<Map<String,Object>> selectAllExport(EquipmentTask equipmentTask);
	
	List<Map<String,String>> selectByEquId(String equId);

	List<String> selectByOfficeId(String officeId);

	int deleteEquTask(String[] idAry);
}
