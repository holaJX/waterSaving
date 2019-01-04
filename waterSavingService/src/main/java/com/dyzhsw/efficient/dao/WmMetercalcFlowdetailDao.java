package com.dyzhsw.efficient.dao;

import com.dyzhsw.efficient.entity.WmMetercalcFlowdetail;

import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface WmMetercalcFlowdetailDao {

	List<Map<String, Object>> selectSfList(Map<String, Object> map);

	List<Map<String, Object>> getSfList(Map<String, Object> map);
	
	List<Map<String, Object>> getWmMFList(Map<String, Object> map);
	
	List<Map<String, Object>> selectSfOrder(Map<String, Object> map);
	
	Map<String, Object> getWmMFnewdata(Map<String, Object> map);

	WmMetercalcFlowdetail getDetailInfoByEquNo(String equNo);
	
	List<WmMetercalcFlowdetail> selectSfInfoList(@Param("start")String start,@Param("end")String end,@Param("address")String address);
}
