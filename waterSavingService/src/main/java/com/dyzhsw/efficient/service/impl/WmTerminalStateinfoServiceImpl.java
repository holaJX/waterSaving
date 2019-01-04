package com.dyzhsw.efficient.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.dyzhsw.efficient.dao.WmMetercalcFlowdetailDao;
import com.dyzhsw.efficient.dao.WmTerminalStateInfoDao;
import com.dyzhsw.efficient.dao.WmTerminalinfoDao;
import com.dyzhsw.efficient.entity.WmMetercalcFlowdetail;
import com.dyzhsw.efficient.entity.WmTerminalStateInfo;
import com.dyzhsw.efficient.entity.WmTerminalinfo;
import com.dyzhsw.efficient.service.WmTerminalStateinfoService;
import com.dyzhsw.efficient.service.WmTerminalinfoService;


/**
 * @author: yaorui 
 * @date: 2018年12月2日 上午10:34:12 
 */

@Service
public class WmTerminalStateinfoServiceImpl implements WmTerminalStateinfoService {

	@Autowired
	private WmTerminalStateInfoDao wmTerminalStateInfoDao;
	
	@Override
	public WmTerminalStateInfo selectByEquNo(String equNo) {
		return wmTerminalStateInfoDao.selectByEquNo(equNo);
	}

	
}
