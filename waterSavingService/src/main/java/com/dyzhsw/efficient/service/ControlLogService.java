package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.ControlLog;

/**
 * 操作日志相关service
 * create by yr
 */
public interface ControlLogService {

	List<ControlLog> selectAll(String name,String type,String start ,String end, String officeId);

	ControlLog selectById(int id);

	void insertInfo(ControlLog con);

	int deleteById(String id[]);

	int updateById(ControlLog con);

	/**
	 * lhl获取智能控制操作日志
	 */
	List<Map<String, Object>> selectcontrolAll(Map<String, Object> map);

	
	List<ControlLog> selectByInfo(ControlLog con);
	
	List<ControlLog> getLogExport(String name,String type,String start,String end,String officeId);
	
}                               