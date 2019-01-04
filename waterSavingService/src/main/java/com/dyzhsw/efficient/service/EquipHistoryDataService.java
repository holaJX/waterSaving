package com.dyzhsw.efficient.service;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.PressureHistory;


/**
 * 
 * @author lhl 
 * @creatDate 2018年12月12日
 * @description 描述
 */
public interface EquipHistoryDataService {
	
	//获取历史数据
		//压力计
	 List<PressureHistory> getPressureList(Map<String,String> map);
	 //施肥机
	 List<Map<String, Object>> getWmMFList(Map<String, Object> map);
	 //流量计
	 List<Map<String, Object>> getWaterListApp(Map<String, Object> map);
	 //阀控器
	 List<Map<String, Object>> selectFkqAllApp(Map<String, Object> map);
	 
	 // 实时数据获取
	 
	 PressureHistory selectnewdata(Map<String,String> map);
	 
	 Map<String, Object> getWmMFnewdata(Map<String, Object> map);
	 
	 Map<String, Object> getWaternewdataApp (Map<String, Object> map);
	 
	 Map<String, Object> selectFkqNewApp(Map<String, Object>map );
}
