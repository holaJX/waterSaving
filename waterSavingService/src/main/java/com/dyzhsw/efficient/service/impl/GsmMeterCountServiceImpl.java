package com.dyzhsw.efficient.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dyzhsw.efficient.dao.GsmMeterCumulativeWaterDao;
import com.dyzhsw.efficient.dao.GsmMeterCumulativeflowfordayDao;
import com.dyzhsw.efficient.entity.GsmMeterCumulativeflowforday;
import com.dyzhsw.efficient.service.GsmMeterCountService;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月13日
 * @description 用水量统计
 */

@Service
public class GsmMeterCountServiceImpl implements GsmMeterCountService{
	
	
	@Autowired
	GsmMeterCumulativeWaterDao gsmMeterCumulativeWaterDao;
	
	@Autowired
	GsmMeterCumulativeflowfordayDao gsmMeterCumulativeflowfordayDao;

	@Override
	public List<Map<String, Object>> selectWatercount(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return gsmMeterCumulativeWaterDao.selectWatercount(map);
	}

	@Override
	public List<GsmMeterCumulativeflowforday> selectWaterInfoList(String start, String end,String address) {
		return gsmMeterCumulativeflowfordayDao.selectWaterInfoList(start, end,address);
	}

	@Override
	public List<Map<String, Object>> getWaterareadataApp() {
		// TODO Auto-generated method stub
		return gsmMeterCumulativeWaterDao.getWaterareadataApp();
	}

}
