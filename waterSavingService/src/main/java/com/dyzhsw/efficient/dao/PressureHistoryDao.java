package com.dyzhsw.efficient.dao;

import java.util.List;
import java.util.Map;

import com.dyzhsw.efficient.entity.PressureHistory;

/**
 * 
 * @author lhl 
 * @creatDate 2018年12月11日
 * @description 压力计历史数据
 */
public interface PressureHistoryDao {
	//查找全部
    List<PressureHistory> selectAll(Map<String,String> map);
    
    PressureHistory  selectnewdata(Map<String,String> map);

    List<PressureHistory> selectGraphByEquNo(String equNo);

    List<PressureHistory> selectByEquNo(String equNo);
}
