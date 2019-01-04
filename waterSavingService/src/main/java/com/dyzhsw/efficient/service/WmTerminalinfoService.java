package com.dyzhsw.efficient.service;

import java.util.List;

import com.dyzhsw.efficient.entity.WmMetercalcFlowdetail;
import com.dyzhsw.efficient.entity.WmTerminalStateInfo;
import com.dyzhsw.efficient.entity.WmTerminalinfo;
/**
 * 施肥机相关service
 * create by yr
 */
public interface WmTerminalinfoService {

	List<WmTerminalinfo> selectAll(WmTerminalinfo wm);

	WmTerminalinfo selectById(String id);

	void insertInfo(WmTerminalinfo wm);

	int deleteById(int id);

	int updateById(WmTerminalinfo wm);

	WmTerminalinfo selectByEquNo(String equNo);
	
	List<WmMetercalcFlowdetail> selectSfInfoList(String start,String end,String address);
	
}                               