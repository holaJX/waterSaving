package com.dyzhsw.efficient.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dyzhsw.efficient.dao.WmMetercalcFlowdetailDao;
import com.dyzhsw.efficient.dao.WmTerminalStateInfoDao;
import com.dyzhsw.efficient.dao.WmTerminalinfoDao;
import com.dyzhsw.efficient.entity.WmMetercalcFlowdetail;
import com.dyzhsw.efficient.entity.WmTerminalinfo;
import com.dyzhsw.efficient.service.WmTerminalinfoService;


/**
 * @author: yaorui 
 * @date: 2018年12月2日 上午10:34:12 
 */

@Service
public class WmTerminalinfoServiceImpl implements WmTerminalinfoService {

	@Autowired
	WmTerminalinfoDao wmTerminalinfoDao;
	@Autowired
	WmMetercalcFlowdetailDao wmMetercalcFlowdetailDao;
	

	
	@Override
	public List<WmTerminalinfo> selectAll(WmTerminalinfo wm) {		
		return wmTerminalinfoDao.selectAll(wm);
	}
	

	@Override
	public WmTerminalinfo selectById(String id) {
		return wmTerminalinfoDao.selectById(id);
	}

	@Override
	public void insertInfo(WmTerminalinfo wm) {
		int i = wmTerminalinfoDao.insertInfo(wm);	
	}

	@Override
	public int deleteById(int id) {
		return wmTerminalinfoDao.deleteById(id);
	}

	@Override
	public int updateById(WmTerminalinfo wm) {
		return wmTerminalinfoDao.updateById(wm);
	}

	@Override
	public WmTerminalinfo selectByEquNo(String equNo) {
		return wmTerminalinfoDao.selectByEquNo(equNo);
	}


	@Override
	public List<WmMetercalcFlowdetail> selectSfInfoList(String start, String end,String address) {
		return wmMetercalcFlowdetailDao.selectSfInfoList(start, end,address);
	}

}
