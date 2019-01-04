package com.dyzhsw.efficient.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.ControlLogMapper;
import com.dyzhsw.efficient.entity.ControlLog;
import com.dyzhsw.efficient.service.ControlLogService;
import com.dyzhsw.efficient.utils.IDUtils;


/**
 * @author: yaorui 
 * @date: 2018年12月12日 上午10:34:12 
 */

@Service
public class ControlLogServiceImpl implements ControlLogService {

	@Autowired
	ControlLogMapper controlLogMapper;

	
	@Override
	public List<ControlLog> selectAll(String name,String type,String start ,String end, String officeId) {	
		Map<String,Object>map = new HashMap<>();
		map.put("name", name);
		map.put("type", type);
		map.put("start", start);
		map.put("end", end);	
		map.put("officeId", officeId);	
		return controlLogMapper.selectAll(map);
	}
	

	@Override
	public ControlLog selectById(int id) {
		return controlLogMapper.selectById(id);
	}

	@Override
	public void insertInfo(ControlLog con) {
		con.setId(IDUtils.createUUID());
		con.setCreateDate(new Date());
		int i = controlLogMapper.insertInfo(con);	
	}

	@Override
	public int deleteById(String id[]) {
		return controlLogMapper.deleteById(id);
	}

	@Override
	public int updateById(ControlLog con) {
		return controlLogMapper.updateById(con);
	}

	/**
	 * lhl获取智能控制操作日志
	 */
	@Override
	public List<Map<String, Object>> selectcontrolAll(Map<String, Object> map) {
		return controlLogMapper.selectcontrolAll(map);
	}


	@Override
	public List<ControlLog> selectByInfo(ControlLog con) {
		return controlLogMapper.selectByInfo(con);
	}


	@Override
	public List<ControlLog> getLogExport(String name,String type,String start,String end,String officeId) {
		return controlLogMapper.getLogExport(name,type,start,end,officeId);
	}

}
