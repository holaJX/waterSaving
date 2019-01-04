package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dyzhsw.efficient.entity.GsmMeterCumulativeflowforday;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface GsmMeterCumulativeflowfordayDao {

	List<Map<String, Object>> selectWaterList(Map<String, Object> map);

	List<Map<String, Object>> getWaterList(Map<String, Object> map);

	List<Map<String, Object>> selectWaterOrder(Map<String, Object> map);

	List<GsmMeterCumulativeflowforday> selectWaterInfoList(@Param("start")String start ,@Param("end")String end,@Param("address")String address);
}
