package com.dyzhsw.efficient.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.EquipmentTaskMapper;
import com.dyzhsw.efficient.entity.EquipmentTask;
import com.dyzhsw.efficient.service.EquipmentTaskService;


/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:34:12 
 */

@Service
public class EquipmentTaskServiceImpl implements EquipmentTaskService {

	@Autowired
	EquipmentTaskMapper taskMapper;

	
	@Override
	public List<Map<String,Object>> selectAll(String name,String officeId,String type) {
		Map<String,Object>map = new HashMap<>();
		map.put("name", name);
		map.put("officeId", officeId);	
		map.put("type", type);	
		return taskMapper.selectAll(map);
	}
	
	@Override
	public List<Map<String,Object>> selectAllTask() {
		return taskMapper.selectAllTask();
	}
	

	@Override
	public Map<String,Object> selectById(String id) {
		return taskMapper.selectById(id);
	}

	@Override
	public void insertInfo(EquipmentTask task) {
		int i = taskMapper.insertInfo(task);	
	}

	@Override
	public int deleteById(String[]id) {
		return taskMapper.deleteById(id);
	}

	@Override
	public int updateById(EquipmentTask task) {
		return taskMapper.updateById(task);
	}


	@Override
	public List<EquipmentTask> selectInfo(EquipmentTask equipmentTask) {
		return taskMapper.selectInfo(equipmentTask);
	}

	@Override
	public EquipmentTask selectByIdApp(String id) {
		// TODO Auto-generated method stub
		return taskMapper.selectByIdApp(id);
	}

	@Override
	public List<EquipmentTask> getEquipmentTaskExport(EquipmentTask equipmentTask) {
		return taskMapper.getEquipmentTaskExport(equipmentTask);
	}

	@Override
	public List<Map<String, Object>> selectAllExport(EquipmentTask equipmentTask) {
		return taskMapper.selectAllExport(equipmentTask);
	}
	
/*	@Override
	public List<EquipmentTask> selectTotalList(String name,String type[]){
		Map<String,Object>map = new HashMap<>();
		map.put("name", name);
		map.put("type", type);
		List<EquipmentTask>listpi = taskMapper.selectByName(map);
		return listpi;
		
	}*/
	
	@Override
	public List<Map<String,String>> selectByEquId(String equId) {
		return taskMapper.selectByEquId(equId);
	}
}
