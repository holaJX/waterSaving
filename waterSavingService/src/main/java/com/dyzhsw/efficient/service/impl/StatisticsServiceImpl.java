package com.dyzhsw.efficient.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.ControlLogMapper;
import com.dyzhsw.efficient.dao.GsmMeterCumulativeflowfordayDao;
import com.dyzhsw.efficient.dao.WmMetercalcFlowdetailDao;
import com.dyzhsw.efficient.service.StatisticsService;


/**
 * @author: yaorui 
 * @date: 2018年12月12日 上午10:34:12 
 */

@Service
public class StatisticsServiceImpl implements StatisticsService {

	@Autowired
	GsmMeterCumulativeflowfordayDao gsmMeterCumulativeflowfordayDao;
	@Autowired
	WmMetercalcFlowdetailDao wmMetercalcFlowdetailDao;
	
	@Autowired
	ControlLogMapper controlLogMapper;
	
	
	@Override
	public List<Map<String, Object>> selectWaterList(String id, String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("id", id);
		map.put("start", start);
		map.put("end", end);		
		return gsmMeterCumulativeflowfordayDao.selectWaterList(map);
	}
	
	@Override
	public List<Map<String, Object>> selectWaterOrder( String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);		
		return gsmMeterCumulativeflowfordayDao.selectWaterOrder(map);
	}
	
	@Override
	public List<Map<String, Object>> selectSfOrder( String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("start", start);
		map.put("end", end);		
		return wmMetercalcFlowdetailDao.selectSfOrder(map);
	}
	
	
	@Override
	public List<Map<String, Object>> selectSfList(String equId, String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("equId", equId);
		map.put("start", start);
		map.put("end", end);		
		return wmMetercalcFlowdetailDao.selectSfList(map);
	}
	
	
	@Override
	public List<Map<String, Object>> getSfList(String address, String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("address", address);
		map.put("start", start);
		map.put("end", end);		
		return wmMetercalcFlowdetailDao.getSfList(map);
	}
	
	
	@Override
	public List<Map<String, Object>> getWaterList(String address, String start, String end) {	
		Map<String,Object>map = new HashMap<>();
		map.put("address", address);
		map.put("start", start);
		map.put("end", end);		
		return gsmMeterCumulativeflowfordayDao.getWaterList(map);
	}
	

	

}
