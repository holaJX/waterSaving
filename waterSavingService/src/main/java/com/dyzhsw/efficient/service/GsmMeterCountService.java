package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.GsmMeterCumulativeflowforday;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月13日
 * @description 用水量统计
 * 
 */
public interface GsmMeterCountService {

	List<Map<String, Object>> selectWatercount(Map<String, Object> map);
	
	List<GsmMeterCumulativeflowforday> selectWaterInfoList(String start ,String end,String address);
	
	List<Map<String, Object>> getWaterareadataApp();
}
