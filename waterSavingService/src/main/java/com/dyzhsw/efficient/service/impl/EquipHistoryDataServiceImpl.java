package com.dyzhsw.efficient.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.FkqValveInfoDao;
import com.dyzhsw.efficient.dao.GsmMeterCumulativeWaterDao;
import com.dyzhsw.efficient.dao.PressureHistoryDao;
import com.dyzhsw.efficient.dao.WmMetercalcFlowdetailDao;
import com.dyzhsw.efficient.entity.PressureHistory;
import com.dyzhsw.efficient.service.EquipHistoryDataService;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月12日
 * @description 描述
 */

@Service
public class EquipHistoryDataServiceImpl implements EquipHistoryDataService{
	
	@Autowired
	PressureHistoryDao pressureHistorydao;
	
	@Autowired
	WmMetercalcFlowdetailDao wmMetercalcFlowdetailDao;
	
	@Autowired
	GsmMeterCumulativeWaterDao gsmMeterCumulativeWaterDao;
	
	@Autowired
	FkqValveInfoDao fkqValveInfoDao;
	
	@Override
	public List<PressureHistory> getPressureList(Map<String, String> map) {
		// TODO Auto-generated method stub
		return pressureHistorydao.selectAll(map);
	}

	@Override
	public List<Map<String, Object>> getWmMFList(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return wmMetercalcFlowdetailDao.getWmMFList(map);
	}

	@Override
	public List<Map<String, Object>> getWaterListApp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return gsmMeterCumulativeWaterDao.getWaterListApp(map);
	}

	@Override
	public PressureHistory selectnewdata(Map<String, String> map) {
		// TODO Auto-generated method stub
		return pressureHistorydao.selectnewdata(map);
	}

	@Override
	public Map<String, Object> getWmMFnewdata(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return wmMetercalcFlowdetailDao.getWmMFnewdata(map);
	}

	@Override
	public Map<String, Object> getWaternewdataApp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return gsmMeterCumulativeWaterDao.getWaternewdataApp(map);
	}

	@Override
	public List<Map<String, Object>> selectFkqAllApp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return fkqValveInfoDao.selectFkqAllApp(map);
	}

	@Override
	public Map<String, Object> selectFkqNewApp(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return fkqValveInfoDao.selectFkqNewApp(map);
	}

}
