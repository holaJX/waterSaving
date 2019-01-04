package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface GsmMeterCumulativeWaterDao {

	List<Map<String, Object>> selectWaterList(Map<String, Object> map);

	List<Map<String, Object>> selectSfList(Map<String, Object> map);

	List<Map<String, Object>> getSfList(Map<String, Object> map);

	List<Map<String, Object>> getWaterList(Map<String, Object> map);
	
	/**
	 * 
			 * 
			 * @auth lhl
			 * @description 描述
			 * @creatDate 2018年12月17日
			 * @param param1
			 * @param param2
			 * @return移动端数据获取接口
	 */
	List<Map<String, Object>> getWaterListApp(Map<String, Object> map);
	
	Map<String, Object> getWaternewdataApp(Map<String, Object> map);
	
	List<Map<String, Object>> getWaterareadataApp();
	
	/************结束*******************************************/
	
	List<Map<String, Object>> selectWatercount(Map<String, Object> map);
	
	
	
}
