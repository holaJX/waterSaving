package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.dyzhsw.efficient.entity.ControlLog;

/**
 * @author: yaorui 
 * @date: 2018年9月12日 上午10:30:42 
 */
public interface ControlLogMapper {
	//查找全部
    List<ControlLog> selectAll(Map<String,Object>map);
	//根据id查找
    ControlLog selectById(int id);
	//增加
	int insertInfo(ControlLog con);
	//删除
	int deleteById(String id[]);
	//修改
	int updateById(ControlLog con);
	
	List<Map<String, Object>> getSfList(Map<String, Object> map);

	/**
	 * lhl获取智能控制操作日志
	 */
	List<Map<String, Object>> selectcontrolAll(Map<String, Object> map);
	
	List<ControlLog> selectByInfo(ControlLog con);
	
	List<ControlLog> getLogExport(@Param("name")String name,@Param("type")String type,@Param("start")String start,@Param("end")String end,@Param("officeId")String officeId);
	
}
